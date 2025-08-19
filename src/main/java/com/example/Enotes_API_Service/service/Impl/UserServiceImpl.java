package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.dto.EmailRequest;
import com.example.Enotes_API_Service.dto.PasswordChangeRequest;
import com.example.Enotes_API_Service.dto.PasswordResetRequest;
import com.example.Enotes_API_Service.entity.User;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.repository.UserRepository;
import com.example.Enotes_API_Service.service.UserService;
import com.example.Enotes_API_Service.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void changePassword(PasswordChangeRequest request) {
        User loggedInUser = CommonUtil.getLoggedInUser();
        if(!passwordEncoder.matches(request.getOldPassword(), loggedInUser.getPassword())) {
            throw new IllegalArgumentException("Old Password is incorrect!");
        }
        loggedInUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(loggedInUser);
    }

    @Override
    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception{
        User user = userRepository.findByEmail(email);
        if(ObjectUtils.isEmpty(user)) {
            throw new ResourceNotFoundException("Invalid Email");
        }

        //Generate unique password reset token
        String passwordResetToken = UUID.randomUUID().toString();
        user.getStatus().setPasswordResetToken(passwordResetToken);
        User updateUser = userRepository.save(user);

        String url = CommonUtil.getUrl(request);
        sendEmailRequest(updateUser, url);
    }

    @Override
    public void verifyPasswordResetLink(Integer userId, String code) throws Exception{
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Invalid user!"));
        verifyPasswordResetCode(user.getStatus().getPasswordResetToken(), code);
    }

    private void verifyPasswordResetCode(String existToken, String requestToken) {

        if(StringUtils.hasText(requestToken)){
            if(!StringUtils.hasText(existToken)){
                throw new IllegalArgumentException("Already password reset!");
            }
            if(!existToken.equals(requestToken)){
                throw new IllegalArgumentException("Invalid url");
            }
        } else {
            throw new IllegalArgumentException("Invalid Token");
        }
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception{
        User user = userRepository.findById(passwordResetRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Invalid user!"));
        String encodePassword = passwordEncoder.encode(passwordResetRequest.getNewPassword());
        user.setPassword(encodePassword);
        user.getStatus().setPasswordResetToken(null);
        userRepository.save(user);
    }

    private void sendEmailRequest(User user, String url) throws Exception{
        String message = "Hi,<b>[[username]]</b>"
                +"<br><p> You have requested to reset your password. </p>"
                +"<p> Click the link below to change password </p>"
                +"<p><a href='[[url]]'+>Click Here</a></p> "
                +"<p> Ignore this email if you do remember your password,"
                +"or you do not made this request.</p><br>"
                +"Thanks,<br>Enotes.com"
                ;

        message = message.replace("[[username]]", user.getFirstName());
        message = message.replace("[[url]]", url+"/api/v1/home/password/link?userId="+user.getId()+"&&code="+user.getStatus().getPasswordResetToken());

        EmailRequest emailRequest = EmailRequest.builder()
                .to(user.getEmail())
                .title("Password Reset!")
                .subject("Password Reset Link.")
                .message(message)
                .build();
        emailService.send(emailRequest);
    }
}
