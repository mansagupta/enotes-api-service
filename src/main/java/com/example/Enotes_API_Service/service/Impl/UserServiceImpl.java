package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.dto.EmailRequest;
import com.example.Enotes_API_Service.dto.UserDto;
import com.example.Enotes_API_Service.entity.AccountStatus;
import com.example.Enotes_API_Service.entity.Role;
import com.example.Enotes_API_Service.entity.User;
import com.example.Enotes_API_Service.repository.RoleRepository;
import com.example.Enotes_API_Service.repository.UserRepository;
import com.example.Enotes_API_Service.service.UserService;
import com.example.Enotes_API_Service.service.EmailService;
import com.example.Enotes_API_Service.util.Validation;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Boolean register(UserDto userDto, String url) throws Exception{

        validation.userValidation(userDto);
        User user = mapper.map(userDto, User.class);
        setRole(userDto, user);

        AccountStatus status = AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();
        user.setStatus(status);

        User saveUser = userRepository.save(user);
        if(!ObjectUtils.isEmpty(saveUser)){
            emailSend(saveUser, url);
            return true;
        }
        return false;
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

    private void setRole(UserDto userDto, User user) {
        List<Integer> reqRoleId = userDto.getRoles().stream().map(UserDto.RoleDto::getId).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleId);
        user.setRoles(roles);
    }
}
