package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.service.HomeService;
import com.example.Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer userId, @RequestParam String code) throws Exception {
        Boolean verifyAccount = homeService.verifyAccount(userId, code);
        if(verifyAccount){
            return CommonUtil.createBuildResponseMessage("Account verification completed.", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
    }
}
