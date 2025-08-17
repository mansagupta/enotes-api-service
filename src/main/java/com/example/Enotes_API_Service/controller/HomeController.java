package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.PasswordResetRequest;
import com.example.Enotes_API_Service.endpoint.HomeControllerEndpoint;
import com.example.Enotes_API_Service.service.HomeService;
import com.example.Enotes_API_Service.service.UserService;
import com.example.Enotes_API_Service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController implements HomeControllerEndpoint {

    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer userId, @RequestParam String code) throws Exception {
        Boolean verifyAccount = homeService.verifyAccount(userId, code);
        if(verifyAccount){
            return CommonUtil.createBuildResponseMessage("Account verification completed.", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception {
        userService.sendEmailPasswordReset(email, request);
        return CommonUtil.createBuildResponseMessage("Email sent! Check mail and reset password.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer userId, @RequestParam String code) throws Exception{
        userService.verifyPasswordResetLink(userId, code);
        return CommonUtil.createBuildResponseMessage("Verification successful", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception {
        userService.resetPassword(passwordResetRequest);
        return CommonUtil.createBuildResponseMessage("Password reset successful", HttpStatus.OK);
    }
}
