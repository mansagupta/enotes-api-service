package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.PasswordResetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Home", description = "Internal APIs")
@RequestMapping("/api/v1/home")
public interface HomeControllerEndpoint {

    @Operation(summary = "Verify user account", tags = {"Home"}, description = "Verify account of newly registered user")
    @GetMapping("/verify")
    ResponseEntity<?> verifyUserAccount(@RequestParam Integer userId, @RequestParam String code) throws Exception;

    @Operation(summary = "Send email for password reset", tags = {"Home"}, description = "Send password reset emails to user")
    @GetMapping("/send-email")
    ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception;

    @Operation(summary = "Password reset link", tags = {"Home"}, description = "Verify the password reset link")
    @GetMapping("/password-link")
    ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer userId, @RequestParam String code) throws Exception;

    @Operation(summary = "Reset password", tags = {"Home"}, description = "User password reset")
    @PostMapping("/reset-password")
    ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception;
}
