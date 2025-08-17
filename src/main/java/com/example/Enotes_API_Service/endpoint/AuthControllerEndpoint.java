package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.LoginRequest;
import com.example.Enotes_API_Service.dto.UserRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthControllerEndpoint {

    @PostMapping("/save")
    ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest, HttpServletRequest request) throws Exception;

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request);
}
