package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.PasswordResetRequest;
import com.example.Enotes_API_Service.service.HomeService;
import com.example.Enotes_API_Service.service.UserService;
import com.example.Enotes_API_Service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer userId, @RequestParam String code) throws Exception {
        Boolean verifyAccount = homeService.verifyAccount(userId, code);
        if(verifyAccount){
            return CommonUtil.createBuildResponseMessage("Account verification completed.", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/send/email")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception {
        userService.sendEmailPasswordReset(email, request);
        return CommonUtil.createBuildResponseMessage("Email sent! Check mail and reset password.", HttpStatus.OK);
    }

    @GetMapping("/password/link")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer userId, @RequestParam String code) throws Exception{
        userService.verifyPasswordResetLink(userId, code);
        return CommonUtil.createBuildResponseMessage("Verification successful", HttpStatus.OK);
    }

    @PostMapping("/reset/password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception {
        userService.resetPassword(passwordResetRequest);
        return CommonUtil.createBuildResponseMessage("Password reset successful", HttpStatus.OK);
    }
}
