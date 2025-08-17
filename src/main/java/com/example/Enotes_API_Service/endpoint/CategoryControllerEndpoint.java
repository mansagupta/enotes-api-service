package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/category")
public interface CategoryControllerEndpoint {

    @PostMapping("/save")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

    @GetMapping("/get")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> getAllCategory();

    @GetMapping("/getActive")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    ResponseEntity<?> getActiveCategory();

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception;

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);
}
