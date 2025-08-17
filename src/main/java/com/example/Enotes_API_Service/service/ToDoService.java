package com.example.Enotes_API_Service.service;

import com.example.Enotes_API_Service.dto.ToDoDto;

import java.util.List;

public interface ToDoService {

    public Boolean saveToDo(ToDoDto todo) throws Exception;

    public ToDoDto getToDoById(Integer id) throws Exception;

    public List<ToDoDto> getToDoByUser();
}
