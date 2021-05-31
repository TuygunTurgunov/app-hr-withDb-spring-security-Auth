package uz.pdp.online.m6l5apphr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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


    public ApiResponse add(TaskDto taskDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String userEmail = user.getEmail();
        if (userEmail.equals(taskDto.getWorkerEmail()))
            return new ApiResponse("O'ziga o'zi task jo'natmasin", false);


        boolean isManager = true;

        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_WORKER.name()))
                return new ApiResponse("worker can'not add task", false);
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())) {
                isManager = false;
                break;
            }
        }
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        Optional<Status> optionalStatus = statusRepository.findById(taskDto.getStatusId());
        if (!optionalStatus.isPresent())
            return new ApiResponse("Status mot found by id", false);
        task.setStatusName(optionalStatus.get());

        Optional<User> optionalUserDto = userRepository.findByEmail(taskDto.getWorkerEmail());
        if (!optionalUserDto.isPresent())
            return new ApiResponse("user by this email not found", false);

        User userDtoByEmail = optionalUserDto.get();
        boolean director = true;
        if (isManager) {
            for (GrantedAuthority authority : userDtoByEmail.getAuthorities()) {
                if (authority.getAuthority().equals(RoleName.ROLE_WORKER.name())) {
                    director = false;
                    task.setWorkerEmail(taskDto.getWorkerEmail());
                    break;
                }
            }
            if (director) {
                return new ApiResponse("manager can send task only workers", false);
            }
        }
        if (director)
            task.setWorkerEmail(taskDto.getWorkerEmail());

        Timestamp deadline = new Timestamp(System.currentTimeMillis() + taskDto.getDeadlineDate());

        task.setDeadlineDate(deadline);
        task.setTaskCode(UUID.randomUUID().toString());
        UUID id = task.getId();

        taskRepository.save(task);
        customService.sendEmail(task.getWorkerEmail(), task.getTaskCode(), "verifyTask", task.getName(), task.getDescription());
        return new ApiResponse("muvaffaqiyatli sqalandi  va ishchini  email ga  tasdiqlashga  xabar yuborildi", true);


    }

    public ApiResponse verifyTask(String taskCode, String email, TaskDtoEdit statusId,Authentication authentication) {

        User user =(User) authentication.getPrincipal();
        if(!user.getUsername().equals(email)){
            return new ApiResponse("Problem in  token",false);
        }

        Optional<Status> optionalStatus = statusRepository.findById(statusId.getStatusId());
        if (!optionalStatus.isPresent()||statusId.getStatusId()==null)
            return new ApiResponse("such kind of statsus not found",false);


        Optional<Task> optionalTask = taskRepository.findByWorkerEmailAndTaskCode(email, taskCode);
        if(!optionalTask.isPresent())
            return new ApiResponse("task not found or already committed",false);

        Task task = optionalTask.get();
        task.setStatusName(optionalStatus.get());
        task.setTaskCode(null);
        taskRepository.save(task);
        return new ApiResponse("Task qabul qiliondi va statusi="+task.getStatusName().toString()+" ga",true);
    }


    public ApiResponse edit(UUID id, TaskDtoEdit taskDtoEdit, Authentication authentication) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("task not found by id",false);

        Optional<Status> optionalStatus = statusRepository.findById(taskDtoEdit.getStatusId());
        if(!optionalStatus.isPresent())
            return new ApiResponse("status not found by id",false);


        Task task = optionalTask.get();
        User createdUser = userRepository.getById(task.getCreatedBy());


        User authUser =(User) authentication.getPrincipal();
        if (!authUser.getUsername().equals(task.getWorkerEmail())
                ||authUser.getUsername().equals(createdUser.getUsername()))
            return new ApiResponse("task can change by owner task or task worker .",false);

        if (taskDtoEdit.getStatusId()==3){
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            if (task.getDeadlineDate().after(timestamp)){
                task.setCompletedOnTime(true);
                 }else task.setCompletedOnTime(false);
            task.setCompleted(true);
            task.setStatusName(statusRepository.findByStatusName(StatusName.STATUS_COMPLETED));
        }else{
            task.setStatusName(optionalStatus.get());
            task.setCompleted(false);
            task.setCompletedOnTime(false);
        }



        String createdUserTask = createdUser.getUsername();
//        task.setTaskCode(UUID.randomUUID().toString());
        taskRepository.save(task);
        customService.sendEmail(createdUserTask,task.getWorkerEmail(),task.getName(),task.getStatusName().toString());
        return new ApiResponse("Vazifa biriktirgan hodimning emailga yuborildi",true);
    }


    public ApiResponse delete(UUID id, Authentication authentication) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("task not found by id",false);

        Task task = optionalTask.get();

        User authenticationUser = (User) authentication.getPrincipal();
        String authenticationUserUsername = authenticationUser.getUsername();

        Optional<User> optionalUser = userRepository.findById(task.getCreatedBy());
        if (!optionalUser.isPresent())
            return new ApiResponse("task owner not found",false);

        User createdUser = optionalUser.get();
        String createdUserUsername = createdUser.getUsername();

        String workerEmail = task.getWorkerEmail();

        if (!authenticationUserUsername.equals(createdUserUsername)&&!authenticationUserUsername.equals(workerEmail))
            return new ApiResponse("problem in token",false);

        taskRepository.delete(task);
        return new ApiResponse("task deleted",true);

    }


    public List<Task> getAllTask(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())
                    ||authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name()))

                return taskRepository.findAll();
        }
        return null;




    }

    public Task getOneTask(UUID id, Authentication authentication) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent())
            return null;

        Task task = optionalTask.get();

        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())
                    ||authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name()))

                    return task;
        }
        return null;

    }

    public Set<Task> getTasksByQuery(QueryDto queryDto, Authentication authentication) {
        User authenticationUser = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : authenticationUser.getAuthorities()) {
            if (!authority.getAuthority().equals(RoleName.ROLE_WORKER.name())){
                return taskRepository.findAllByWorkerEmailAndCompletedAndCompletedOnTime(
                        queryDto.getEmail(), queryDto.getCompleted(), queryDto.getCompletedOnTime());
            }
        }
        return null;
    }
}
