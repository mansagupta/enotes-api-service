package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.LoginRequest;
import com.example.Enotes_API_Service.dto.UserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "User Authentication", description = "APIs for managing user authentication/registration")
@RequestMapping("/api/v1/auth")
public interface AuthControllerEndpoint {

    @Operation(summary = "Register endpoint", tags = {"Auth"}, description = "Registration of new users")
    @PostMapping("/save")
    ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest, HttpServletRequest request) throws Exception;

    @Operation(summary = "Login endpoint", tags = {"Auth"}, description = "User login")
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request);
}
