package com.example.Enotes_API_Service.service;

import com.example.Enotes_API_Service.dto.LoginRequest;
import com.example.Enotes_API_Service.dto.LoginResponse;
import com.example.Enotes_API_Service.dto.UserRequest;

public interface AuthService {

    public Boolean register(UserRequest userRequest, String url) throws Exception;

    public LoginResponse login(LoginRequest loginRequest);
}
