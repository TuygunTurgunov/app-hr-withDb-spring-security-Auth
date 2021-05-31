package uz.pdp.online.m6l5apphr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.online.m6l5apphr.entity.Status;
import uz.pdp.online.m6l5apphr.entity.Task;
import uz.pdp.online.m6l5apphr.entity.User;
import uz.pdp.online.m6l5apphr.entity.enums.RoleName;
import uz.pdp.online.m6l5apphr.entity.enums.StatusName;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.QueryDto;
import uz.pdp.online.m6l5apphr.payload.TaskDto;
import uz.pdp.online.m6l5apphr.payload.TaskDtoEdit;
import uz.pdp.online.m6l5apphr.repository.StatusRepository;
import uz.pdp.online.m6l5apphr.repository.TaskRepository;
import uz.pdp.online.m6l5apphr.repository.UserRepository;
import uz.pdp.online.m6l5apphr.service.emailService.CustomService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomService customService;


    public ApiResponse add(TaskDto taskDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        String userEmail = currentUser.getEmail();
        if (userEmail.equals(taskDto.getWorkerEmail()))
            return new ApiResponse("O'ziga o'zi task jo'natmasin", false);


        boolean currentUserIsManager = true;
        boolean currentUserIsDirector = true;

        /*
                task ni kim qo'shayotganini aniqlash
         */
        for (GrantedAuthority authority : currentUser.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_WORKER.name()))
                return new ApiResponse("worker can't add task", false);
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())) {
                currentUserIsManager = false;
                break;
            }
        }
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        Optional<Status> optionalStatus = statusRepository.findById(1);
        if (!optionalStatus.isPresent())
            return new ApiResponse("Status = new ", false);

        task.setStatusName(optionalStatus.get());

      //taskga biriktirilgan hodim
        Optional<User> optionalUserDto = userRepository.findByEmail(taskDto.getWorkerEmail());
        if (!optionalUserDto.isPresent())
            return new ApiResponse("worker by this email not found", false);

        //dto da email orqali kelayotgan user
        User userDtoByEmail = optionalUserDto.get();

        // ishchi managerga yoki manager directorga task biriktirib qoymasligini tekshirish
        if (currentUserIsManager) {
            for (GrantedAuthority authority : currentUser.getAuthorities()) {
                if (authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name())) {
                    for (GrantedAuthority userDtoByEmailAuthority : userDtoByEmail.getAuthorities()) {
                        if (userDtoByEmailAuthority.getAuthority().equals(RoleName.ROLE_WORKER.name())) {
                            task.setWorkerEmail(taskDto.getWorkerEmail());
                            break;
                        }
                        if(userDtoByEmailAuthority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())){
                            return new ApiResponse("manger can send task only worker",false);
                        }

                    }
                    currentUserIsDirector=false;
                    break;
                }
            }
        }

        if (currentUserIsDirector){
            for (GrantedAuthority authority : currentUser.getAuthorities()) {
                if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())){
                    task.setWorkerEmail(taskDto.getWorkerEmail());
                    break;
                }
            }
        }


        //kimga yuborilvotganligi ishchi managerga
//        if (currentUserIsManager) {
//            for (GrantedAuthority authority : userDtoByEmail.getAuthorities()) {
//                if (authority.getAuthority().equals(RoleName.ROLE_WORKER.name())) {
//                    currentUserIsDirector = false;
//                    task.setWorkerEmail(taskDto.getWorkerEmail());
//                    break;
//                }
//            }
//            if (currentUserIsDirector) {
//                return new ApiResponse("manager can send task only workers", false);
//            }
//        }
//        if (currentUserIsDirector)
//            task.setWorkerEmail(taskDto.getWorkerEmail());

        /**
         *  Timestamp deadline = new Timestamp(System.currentTimeMillis() + taskDto.getDeadlineDate());
         *  o'zim tekshirvotganimda long qo'shib tekshirish uchun  +  ni ishlatdim
         */
        Timestamp deadline = new Timestamp(taskDto.getDeadlineDate());

        task.setDeadlineDate(deadline);
        task.setTaskCode(UUID.randomUUID().toString());

        taskRepository.save(task);
        customService.sendEmail(task.getWorkerEmail(), task.getTaskCode(), "verifyTask", task.getName(), task.getDescription());
        return new ApiResponse("muvaffaqiyatli sqalandi  va ishchini  email ga  tasdiqlashga  xabar yuborildi", true);


    }

    public ApiResponse verifyTask(String taskCode, String email, TaskDtoEdit statusId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        if (!user.getUsername().equals(email)) {
            return new ApiResponse("Problem in  token", false);
        }

        Optional<Status> optionalStatus = statusRepository.findById(2);
        if (!optionalStatus.isPresent())
            return new ApiResponse("status progress only ", false);


        Optional<Task> optionalTask = taskRepository.findByWorkerEmailAndTaskCode(email, taskCode);
        if (!optionalTask.isPresent())
            return new ApiResponse("task not found or already committed", false);

        Task task = optionalTask.get();
        task.setStatusName(optionalStatus.get());
        task.setTaskCode(null);
        taskRepository.save(task);

        UUID createdBy = task.getCreatedBy();
        Optional<User> optionalCreatedUser = userRepository.findById(createdBy);
        if (!optionalCreatedUser.isPresent())
            return new ApiResponse("task owner not found",false);

        User taskCreatedByThisUser = optionalCreatedUser.get();
        String usernameTaskCreator = taskCreatedByThisUser.getUsername();


        customService.sendEmail(usernameTaskCreator,task.getName(),"Accepted by "+task.getWorkerEmail());
        return new ApiResponse("Task qabul qiliondi va statusi=" + task.getStatusName().toString() + " ga", true);
    }


    public ApiResponse edit(UUID id, TaskDtoEdit taskDtoEdit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("task not found by id", false);

        Optional<Status> optionalStatus = statusRepository.findById(taskDtoEdit.getStatusId());
        if (!optionalStatus.isPresent())
            return new ApiResponse("status not found by id", false);


        Task task = optionalTask.get();
        User createdUser = userRepository.getById(task.getCreatedBy());


        User authUser = (User) authentication.getPrincipal();
        if (!authUser.getUsername().equals(task.getWorkerEmail())
                || authUser.getUsername().equals(createdUser.getUsername()))
            return new ApiResponse("task can change by owner task or task worker .", false);

        if (taskDtoEdit.getStatusId() == 3) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (task.getDeadlineDate().after(timestamp)) {
                task.setCompletedOnTime(true);
            } else task.setCompletedOnTime(false);
            task.setCompleted(true);
            task.setStatusName(statusRepository.findByStatusName(StatusName.STATUS_COMPLETED));
        } else {
            task.setStatusName(optionalStatus.get());
            task.setCompleted(false);
            task.setCompletedOnTime(false);
        }


        String createdUserTask = createdUser.getUsername();
//        task.setTaskCode(UUID.randomUUID().toString());
        taskRepository.save(task);
        customService.sendEmail(createdUserTask, task.getWorkerEmail(), task.getName(), task.getStatusName().toString());
        return new ApiResponse("Vazifa biriktirgan hodimning emailga yuborildi", true);
    }


    public ApiResponse delete(UUID id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("task not found by id", false);

        Task task = optionalTask.get();

        User authenticationUser = (User) authentication.getPrincipal();
        String authenticationUserUsername = authenticationUser.getUsername();

        Optional<User> optionalUser = userRepository.findById(task.getCreatedBy());
        if (!optionalUser.isPresent())
            return new ApiResponse("task owner not found", false);

        User createdUser = optionalUser.get();
        String createdUserUsername = createdUser.getUsername();

        String workerEmail = task.getWorkerEmail();

        if (!authenticationUserUsername.equals(createdUserUsername) && !authenticationUserUsername.equals(workerEmail))
            return new ApiResponse("problem in token", false);

        taskRepository.delete(task);
        return new ApiResponse("task deleted", true);

    }


    public List<Task> getAllTask(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())
                    || authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name()))

                return taskRepository.findAll();
        }
        return null;


    }

    public Task getOneTask(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return null;

        Task task = optionalTask.get();

        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())
                    || authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name()))

                return task;
        }
        return null;

    }

    public Set<Task> getTasksByQuery(QueryDto queryDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticationUser = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : authenticationUser.getAuthorities()) {
            if (!authority.getAuthority().equals(RoleName.ROLE_WORKER.name())) {
                return taskRepository.findAllByWorkerEmailAndCompletedAndCompletedOnTime(
                        queryDto.getEmail(), queryDto.getCompleted(), queryDto.getCompletedOnTime());
            }
        }
        return null;
    }
}
