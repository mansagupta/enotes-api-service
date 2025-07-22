package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.dto.UserDto;
import com.example.Enotes_API_Service.entity.Role;
import com.example.Enotes_API_Service.entity.User;
import com.example.Enotes_API_Service.repository.RoleRepository;
import com.example.Enotes_API_Service.repository.UserRepository;
import com.example.Enotes_API_Service.service.UserService;
import com.example.Enotes_API_Service.util.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

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

    @Override
    public Boolean register(UserDto userDto) {

        validation.userValidation(userDto);
        User user = mapper.map(userDto, User.class);
        setRole(userDto, user);
        User saveUser = userRepository.save(user);
        return(!ObjectUtils.isEmpty(saveUser));
    }

    private void setRole(UserDto userDto, User user) {
        List<Integer> reqRoleId = userDto.getRoles().stream().map(UserDto.RoleDto::getId).toList();
        List<Role> roles = roleRepository.findAllById(reqRoleId);
        user.setRoles(roles);
    }
}
