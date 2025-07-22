package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.UserDto;
import com.example.Enotes_API_Service.service.UserService;
import com.example.Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        Boolean register = userService.register(userDto);
        if(register){
            return CommonUtil.createBuildResponseMessage("Registration sucessfull", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Registration failed!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
