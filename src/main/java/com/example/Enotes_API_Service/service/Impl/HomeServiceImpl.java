package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.entity.AccountStatus;
import com.example.Enotes_API_Service.entity.User;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.exception.SuccessException;
import com.example.Enotes_API_Service.repository.UserRepository;
import com.example.Enotes_API_Service.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HomeServiceImpl implements HomeService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception{

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Invalid user!"));
        if(user.getStatus().getVerificationCode() == null){
            throw new SuccessException("Account already verified!");
        }
        if(user.getStatus().getVerificationCode().equals(verificationCode)) {
            AccountStatus status = user.getStatus();
            status.setIsActive(true);
            status.setVerificationCode(null);

            userRepository.save(user);
            return true;
        }
        return false;
    }
}
