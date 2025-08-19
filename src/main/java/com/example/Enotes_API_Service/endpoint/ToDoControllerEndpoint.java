package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.ToDoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ToDo", description = "APIs for managing todo")
@RequestMapping("/api/v1/toDo")
public interface ToDoControllerEndpoint {

    @Operation(summary = "Save todo", tags = {"ToDo", "User"}, description = "Create new todo and save them")
    @PostMapping("/save")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> saveToDo(@RequestBody ToDoDto toDoDto) throws Exception;

    @Operation(summary = "Get todo by id", tags = {"ToDo", "User"}, description = "Retrieve user todo by id")
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getToDo(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Get all todo", tags = {"ToDo", "User"}, description = "Retrieve all the todo available for a user")
    @GetMapping("/list")
    @PreAuthorize("hasRole('user')")
    ResponseEntity<?> getAllToDoByUser();
}
