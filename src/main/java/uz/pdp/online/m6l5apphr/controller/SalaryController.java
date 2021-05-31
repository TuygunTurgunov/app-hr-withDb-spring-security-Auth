package uz.pdp.online.m6l5apphr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.m6l5apphr.entity.Salary;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.SalaryDto;
import uz.pdp.online.m6l5apphr.service.SalaryService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    @Autowired
    SalaryService salaryService;

    @PostMapping
    public HttpEntity<?>add(@Valid @RequestBody SalaryDto salaryDto){

            ApiResponse apiResponse=salaryService.add(salaryDto);
            return ResponseEntity.status(apiResponse.getIsSuccess()?201:409).body(apiResponse);

    }


    @PutMapping("/{id}")
    public HttpEntity<?>edit(@PathVariable UUID id,@RequestBody SalaryDto salaryDto){

    ApiResponse apiResponse=salaryService.edit(id,salaryDto);
    return ResponseEntity.status(apiResponse.getIsSuccess()?200:409).body(apiResponse);

    }

    @DeleteMapping("/{id}")
    public HttpEntity<?>delete(@PathVariable UUID id){

        ApiResponse apiResponse =salaryService.delete(id);
        return ResponseEntity.status(apiResponse.getIsSuccess()?200:409).body(apiResponse);
    }



    @GetMapping
    public HttpEntity<?>getAll(){
        List<Salary> salaryList=salaryService.getAll();
        return ResponseEntity.status(salaryList!=null?200:409).body(salaryList);
    }


    @GetMapping("/{id}")
    public HttpEntity<?>getOne(@PathVariable UUID id){

        Salary salary=salaryService.getOne(id);
        return ResponseEntity.status(salary!=null?200:409).body(salary);

    }

    @GetMapping("/query/byUserId/{id}")
    public HttpEntity<?>getInfoByUserId(@PathVariable UUID id){

        List<Salary> infoByUserId = salaryService.getInfoByUserId(id);
        return ResponseEntity.status(infoByUserId!=null?200:409).body(infoByUserId);
    }

    @GetMapping("/query/byMonthId/{id}")
    public HttpEntity<?>getInfoByMonthId(@PathVariable Integer id){

        List<Salary> infoByMonthId = salaryService.getInfoByMonthId(id);

        return ResponseEntity.status(infoByMonthId!=null?200:409).body(infoByMonthId);
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
