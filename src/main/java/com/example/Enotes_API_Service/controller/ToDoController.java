package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.ToDoDto;
import com.example.Enotes_API_Service.endpoint.ToDoControllerEndpoint;
import com.example.Enotes_API_Service.service.ToDoService;
import com.example.Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ToDoController implements ToDoControllerEndpoint {

    @Autowired
    private ToDoService toDoService;

    @Override
    public ResponseEntity<?> saveToDo(@RequestBody ToDoDto toDoDto) throws Exception {
        Boolean saveToDo = toDoService.saveToDo(toDoDto);
        if(saveToDo) {
            return CommonUtil.createBuildResponseMessage("ToDo saved. Success!", HttpStatus.CREATED);
        } else {
            return CommonUtil.createErrorResponseMessage("ToDo not saved!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getToDo(@PathVariable Integer id) throws Exception {
        ToDoDto toDo = toDoService.getToDoById(id);
        return CommonUtil.createBuildResponse(toDo, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllToDoByUser() {
        List<ToDoDto> toDoList = toDoService.getToDoByUser();
        if(CollectionUtils.isEmpty(toDoList)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(toDoList, HttpStatus.OK);
    }
}
