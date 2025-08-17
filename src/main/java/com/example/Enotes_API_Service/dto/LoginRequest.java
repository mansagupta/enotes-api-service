package com.example.Enotes_API_Service.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;

    private String password;
}
