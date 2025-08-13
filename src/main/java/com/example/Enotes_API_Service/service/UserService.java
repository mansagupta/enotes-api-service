package com.example.Enotes_API_Service.service;

import com.example.Enotes_API_Service.dto.PasswordChangeRequest;
import com.example.Enotes_API_Service.dto.PasswordResetRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    public void changePassword(PasswordChangeRequest request);

    void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception;

    void verifyPasswordResetLink(Integer userId, String code) throws Exception;

    void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception;
}
