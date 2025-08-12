package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.config.security.CustomUserDetails;
import com.example.Enotes_API_Service.dto.EmailRequest;
import com.example.Enotes_API_Service.dto.LoginRequest;
import com.example.Enotes_API_Service.dto.LoginResponse;
import com.example.Enotes_API_Service.dto.UserRequest;
import com.example.Enotes_API_Service.entity.AccountStatus;
import com.example.Enotes_API_Service.entity.Role;
import com.example.Enotes_API_Service.entity.User;
import com.example.Enotes_API_Service.repository.RoleRepository;
import com.example.Enotes_API_Service.repository.UserRepository;
import com.example.Enotes_API_Service.service.JwtService;
import com.example.Enotes_API_Service.service.UserService;
import com.example.Enotes_API_Service.service.EmailService;
import com.example.Enotes_API_Service.util.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Validation validation;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public Boolean register(UserRequest userRequest, String url) throws Exception{

        validation.userValidation(userRequest);
        User user = mapper.map(userRequest, User.class);
        setRole(userRequest, user);

        AccountStatus status = AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveUser = userRepository.save(user);
        if(!ObjectUtils.isEmpty(saveUser)){
            emailSend(saveUser, url);
            return true;
        }
        return false;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        if(authenticate.isAuthenticated()){
            CustomUserDetails customUserDetails = (CustomUserDetails)authenticate.getPrincipal();

            String token = jwtService.generateToken(customUserDetails.getUser());

            return LoginResponse.builder()
                    .user(mapper.map(customUserDetails.getUser(), UserRequest.class))
                    .token(token).build();
        }
        return null;
    }

    private void emailSend(User saveUser, String url) throws Exception{
        String message = "Hi,<b>[[username]]</b>"
                +"<br> Your account has been registered successfully.<br>"
                +"<br> Click the link below and verify your account <br>"
                +"<a href='[[url]]'+>Click Here</a> <br><br>"
                +"Thanks,<br>Enotes.com"
                ;

        message = message.replace("[[username]]", saveUser.getFirstName());
        message = message.replace("[[url]]", url+"/api/v1/home/verify?userId="+saveUser.getId()+"&&code="+saveUser.getStatus().getVerificationCode());

        EmailRequest emailRequest = EmailRequest.builder()
                .to(saveUser.getEmail())
                .title("Account Registered!")
                .subject("Registered account verification.")
                .message(message)
                .build();
        emailService.send(emailRequest);

    }

    private void setRole(UserRequest userRequest, User user) {
        List<Integer> reqRoleId = userRequest.getRoles().stream().map(UserRequest.RoleDto::getId).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleId);
        user.setRoles(roles);
    }
}
