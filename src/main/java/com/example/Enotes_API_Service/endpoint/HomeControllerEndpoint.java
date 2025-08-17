package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.PasswordResetRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/home")
public interface HomeControllerEndpoint {

    @GetMapping("/verify")
    ResponseEntity<?> verifyUserAccount(@RequestParam Integer userId, @RequestParam String code) throws Exception;

    @GetMapping("/send-email")
    ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception;

    @GetMapping("/password-link")
    ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer userId, @RequestParam String code) throws Exception;

    @PostMapping("/reset-password")
    ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception;
}
