package com.example.Enotes_API_Service.service;

import com.example.Enotes_API_Service.dto.LoginRequest;
import com.example.Enotes_API_Service.dto.LoginResponse;
import com.example.Enotes_API_Service.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    public Boolean register(UserDto userDto, String url) throws Exception;

    LoginResponse login(LoginRequest loginRequest);
}
