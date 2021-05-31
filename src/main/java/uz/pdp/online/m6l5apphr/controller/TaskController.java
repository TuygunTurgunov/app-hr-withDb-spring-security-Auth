package uz.pdp.online.m6l5apphr.controller;

import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.m6l5apphr.entity.Task;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.QueryDto;
import uz.pdp.online.m6l5apphr.payload.TaskDto;
import uz.pdp.online.m6l5apphr.payload.TaskDtoEdit;
import uz.pdp.online.m6l5apphr.service.TaskService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @PostMapping
    public HttpEntity<?>add(@Valid @RequestBody TaskDto taskDto){

        ApiResponse apiResponse=taskService.add(taskDto);
        return ResponseEntity.status(apiResponse.getIsSuccess()? HttpStatus.CREATED:HttpStatus.CONFLICT).body(apiResponse);

    }
    @PatchMapping("/verifyTask")
    public HttpEntity<?> verifyTask(@RequestParam String taskCode, @RequestParam String email, @RequestBody TaskDtoEdit taskDtoEdit) {
        ApiResponse apiResponse = taskService.verifyTask(taskCode, email,taskDtoEdit);
        return ResponseEntity.status(apiResponse.getIsSuccess() ? 200 : 409).body(apiResponse);
    }

    @PatchMapping("/edit/{id}")
    public HttpEntity<?>edit(@PathVariable UUID id,@RequestBody TaskDtoEdit taskDtoEdit){
        ApiResponse apiResponse=taskService.edit(id,taskDtoEdit);



        return ResponseEntity.status(apiResponse.getIsSuccess()?HttpStatus.OK:HttpStatus.CONFLICT).body(apiResponse) ;
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?>delete(@PathVariable UUID id){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse=taskService.delete(id);
        return ResponseEntity.status(apiResponse.getIsSuccess()?200:409).body(apiResponse);
    }

    @GetMapping("/query")
    public HttpEntity<?>getByQuery(@RequestBody QueryDto queryDto){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<Task> taskList=taskService.getTasksByQuery(queryDto);
        return ResponseEntity.status(taskList!=null?200:409).body(taskList);
    }


    @GetMapping
    public HttpEntity<?>getAllTask(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Task> tasksList=taskService.getAllTask(authentication);
        return ResponseEntity.status(tasksList!=null?200:409).body(tasksList);
    }
    @GetMapping("/{id}")
    public HttpEntity<?>getOneTask(@PathVariable UUID id){

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Task task = taskService.getOneTask(id);
        return ResponseEntity.status(task!=null?200:409).body(task);
    }




    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }






}
