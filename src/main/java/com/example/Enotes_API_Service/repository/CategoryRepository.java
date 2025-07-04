package com.example.Enotes_API_Service.repository;

import com.example.Enotes_API_Service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByIdAndIsDeletedFalse(Integer id);

    List<Category> findByIsDeletedFalse();

    List<Category> findByIsActiveTrueAndIsDeletedFalse();

    Boolean existsByName(String name);
}
