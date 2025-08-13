package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.LoginRequest;
import com.example.Enotes_API_Service.dto.LoginResponse;
import com.example.Enotes_API_Service.dto.UserRequest;
import com.example.Enotes_API_Service.service.AuthService;
import com.example.Enotes_API_Service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/save")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest, HttpServletRequest request) throws Exception {
        String url = CommonUtil.getUrl(request);
        Boolean register = authService.register(userRequest, url);
        if(register){
            return CommonUtil.createBuildResponseMessage("Registration successful", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Registration failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) throws Exception {
        LoginResponse loginResponse = authService.login(loginRequest);
        if(ObjectUtils.isEmpty(loginResponse)){
            return CommonUtil.createErrorResponseMessage("Invalid credentials", HttpStatus.BAD_REQUEST);
        }
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }
}
