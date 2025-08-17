package com.example.Enotes_API_Service.repository;

import com.example.Enotes_API_Service.entity.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
    List<ToDo> findByCreatedBy(Integer userId);
}
