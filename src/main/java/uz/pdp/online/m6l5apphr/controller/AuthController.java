package uz.pdp.online.m6l5apphr.controller;

import org.apache.tomcat.util.descriptor.web.ContextHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.m6l5apphr.entity.Role;
import uz.pdp.online.m6l5apphr.entity.User;
import uz.pdp.online.m6l5apphr.entity.enums.RoleName;
import uz.pdp.online.m6l5apphr.payload.ApiResponse;
import uz.pdp.online.m6l5apphr.payload.LoginDto;
import uz.pdp.online.m6l5apphr.payload.RegisterDto;
import uz.pdp.online.m6l5apphr.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = authService.registerUser(registerDto,authentication);
        return ResponseEntity.status(apiResponse.getIsSuccess() ? 201 : 409).body(apiResponse);

    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email,@RequestBody LoginDto loginDto) {
        ApiResponse apiResponse = authService.verifyEmail(emailCode, email,loginDto);
        return ResponseEntity.status(apiResponse.getIsSuccess() ? 200 : 409).body(apiResponse);
    }


    @PostMapping("/login")
    public HttpEntity<?>login(@RequestBody LoginDto loginDto){
        ApiResponse apiResponse =authService.login(loginDto);
        return ResponseEntity.status(apiResponse.getIsSuccess()?200:401).body(apiResponse);

    }
}
