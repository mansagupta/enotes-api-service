package com.example.Enotes_API_Service.util;

import com.example.Enotes_API_Service.dto.CategoryDto;
import com.example.Enotes_API_Service.dto.ToDoDto;
import com.example.Enotes_API_Service.dto.UserRequest;
import com.example.Enotes_API_Service.entity.Role;
import com.example.Enotes_API_Service.enums.ToDoStatus;
import com.example.Enotes_API_Service.exception.ExistDataException;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.exception.ValidationException;
import com.example.Enotes_API_Service.repository.RoleRepository;
import com.example.Enotes_API_Service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class Validation {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public void categoryValidation(CategoryDto categoryDto){

        Map<String, Object> error = new LinkedHashMap<>();

        if(ObjectUtils.isEmpty(categoryDto)){
            throw new IllegalArgumentException("category object shouldn't be null or empty");
        }else {
            // validation name field
            if(ObjectUtils.isEmpty(categoryDto.getName())) {
                error.put("name","name field is empty or null");
            }else {
                if(categoryDto.getName().length()<10) {
                    error.put("name", "name length min 10");
                }
                if(categoryDto.getName().length()>100) {
                    error.put("name", "name length max 100");
                }
            }

            // validation description field
            if(ObjectUtils.isEmpty(categoryDto.getDescription())) {
                error.put("description","description field is empty or null");
            }

            // validation isActive field\
            if(ObjectUtils.isEmpty(categoryDto.getIsActive())) {
                error.put("isActive","isActive field is empty or null");
            }else {
                if(!categoryDto.getIsActive()) {
                    error.put("isActive", "invalid isActive field");
                }
            }
        }
        if (!error.isEmpty()) {
            throw new ValidationException(error);
        }
    }

    public void toDoValidation(ToDoDto toDoDto) throws Exception{
        ToDoDto.StatusDto reqStatus = toDoDto.getStatus();
        boolean statusFound = false;

        for(ToDoStatus st: ToDoStatus.values()){
            if (st.getId().equals(reqStatus.getId())) {
                statusFound = true;
                break;
            }
        }
        if(!statusFound){
            throw new ResourceNotFoundException("Invalid status");
        }
    }

    public void userValidation(UserRequest userRequest){

        if(!StringUtils.hasText(userRequest.getFirstName())){
            throw new IllegalArgumentException("First Name is invalid!");
        }
        if(!StringUtils.hasText(userRequest.getLastName())){
            throw new IllegalArgumentException("Last Name is invalid!");
        }
        if(!StringUtils.hasText(userRequest.getEmail()) ||
                !userRequest.getEmail().matches(Contants.EMAIL_REGEX)){
            throw new IllegalArgumentException("Email is invalid!");
        } else {
            Boolean existEmail = userRepository.existsByEmail(userRequest.getEmail());
            if(existEmail){
                throw new ExistDataException("Email already exists!");
            }
        }
        if(!StringUtils.hasText(userRequest.getMobNo()) ||
                !userRequest.getMobNo().matches(Contants.MOBILE_NO_REGEX)){
            throw new IllegalArgumentException("Mobile no. is invalid!");
        }
        if(CollectionUtils.isEmpty(userRequest.getRoles())) {
            throw new IllegalArgumentException("Role is invalid");
        } else {
            List<Integer> roleIds = roleRepository.findAll().stream().map(Role::getId).toList();
            List<Integer> invalidReqRoleIds = userRequest.getRoles().stream().map(UserRequest.RoleDto::getId)
                    .filter(roleIds::contains).toList();

            if(CollectionUtils.isEmpty(invalidReqRoleIds)){
                throw new IllegalArgumentException("Role is invalid " + invalidReqRoleIds);
            }
        }

    }
}
