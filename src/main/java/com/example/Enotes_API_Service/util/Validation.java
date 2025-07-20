package com.example.Enotes_API_Service.util;

import com.example.Enotes_API_Service.dto.CategoryDto;
import com.example.Enotes_API_Service.dto.ToDoDto;
import com.example.Enotes_API_Service.enums.ToDoStatus;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.exception.ValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Validation {

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
}
