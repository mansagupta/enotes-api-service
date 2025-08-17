package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.ToDoDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/toDo")
public interface ToDoControllerEndpoint {

    @PostMapping("/save")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> saveToDo(@RequestBody ToDoDto toDoDto) throws Exception;

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getToDo(@PathVariable Integer id) throws Exception;

    @GetMapping("/list")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getAllToDoByUser();
}
