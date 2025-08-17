package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.PasswordChangeRequest;
import com.example.Enotes_API_Service.dto.UserResponse;
import com.example.Enotes_API_Service.endpoint.UserControllerEndpoint;
import com.example.Enotes_API_Service.entity.User;
import com.example.Enotes_API_Service.service.UserService;
import com.example.Enotes_API_Service.util.CommonUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController implements UserControllerEndpoint {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<?> getProfile() {
        User loggedInUser = CommonUtil.getLoggedInUser();
        UserResponse userResponse = mapper.map(loggedInUser, UserResponse.class);
        return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        userService.changePassword(request);
        return CommonUtil.createBuildResponseMessage("Password changed successfully", HttpStatus.OK);
    }
}
