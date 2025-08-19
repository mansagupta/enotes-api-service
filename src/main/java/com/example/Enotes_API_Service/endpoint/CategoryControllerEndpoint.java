package com.example.Enotes_API_Service.endpoint;

import com.example.Enotes_API_Service.dto.CategoryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category", description = "APIs for managing category")
@RequestMapping("/api/v1/category")
public interface CategoryControllerEndpoint {

    @Operation(summary = "Save category", tags = {"Category", "Admin"}, description = "Create new categories and save them")
    @PostMapping("/save")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto);

    @Operation(summary = "Get category", tags = {"Category", "Admin"}, description = "Retrieve all the categories available")
    @GetMapping("/get")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> getAllCategory();

    @Operation(summary = "Get active category", tags = {"Category", "Admin", "User"}, description = "Retrieve all the active categories available")
    @GetMapping("/getActive")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    ResponseEntity<?> getActiveCategory();

    @Operation(summary = "Get category details by id", tags = {"Category", "Admin"}, description = "Retrieve all the details of a category by it's id")
    @GetMapping("/getById/{id}")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Delete category by id", tags = {"Category", "Admin"}, description = "Delete any category by it's id")
    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('admin')")
    ResponseEntity<?> deleteCategoryById(@PathVariable Integer id);
}
