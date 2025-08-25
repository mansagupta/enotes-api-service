package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.CategoryDto;
import com.example.Enotes_API_Service.dto.CategoryResponse;
import com.example.Enotes_API_Service.entity.Category;
import com.example.Enotes_API_Service.service.CategoryService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    CategoryDto categoryDto = null;
    Category category = null;
    CategoryResponse categoryResponse = null;
    List<Category> categories = new ArrayList<>();
    List<CategoryDto> categoriesDto = new ArrayList<>();
    List<CategoryResponse> categoryResponses = new ArrayList<>();
    Integer id = null;

    @BeforeEach
    public void initialize() {
        categoryDto = CategoryDto.builder()
                .id(null)
                .name("Java Notes")
                .description("Java Notes")
                .isActive(true).build();

        category = Category.builder()
                .id(null)
                .name("Java Notes")
                .description("Java Notes")
                .isActive(true)
                .isDeleted(false).build();

        categoryResponse = CategoryResponse.builder()
                .id(null)
                .name("Java Notes")
                .description("Java Notes").build();

        categories.add(category);
        categoriesDto.add(categoryDto);
        categoryResponses.add(categoryResponse);

        id = 1;
    }

    @Test
    public void testSaveCategory() {
        when(categoryService.saveCategory(categoryDto)).thenReturn(true);
        ResponseEntity<?> response = categoryController.saveCategory(categoryDto);
        Object body = response.getBody();

        Map<String, String> json = (Map<String, String>)body;

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assert json != null;
        assertEquals(json.get("status"), "success");
        assertEquals(json.get("message"), "saved success");
    }

    @Test
    public void testCategoryNotSaved() {
        when(categoryService.saveCategory(categoryDto)).thenReturn(false);
        ResponseEntity<?> response = categoryController.saveCategory(categoryDto);
        Object body = response.getBody();

        Map<String, String> json = (Map<String, String>)body;

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assert json != null;
        assertEquals(json.get("status"), "failed");
        assertEquals(json.get("message"), "not saved");
    }

    @Test
    public void testGetAllCategory() {
        when(categoryService.getAllCategory()).thenReturn(categoriesDto);
        ResponseEntity<?> response = categoryController.getAllCategory();
        Object body = response.getBody();

        assertNotNull(body);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Map<String, Object> json = (Map<String, Object>) body;
        assertEquals(json.get("status"), "success");
        assertEquals(json.get("message"), "success");

        List<CategoryDto> data = (List<CategoryDto>) json.get("data");
        assertNotNull(data);
        assertEquals(categoriesDto.size(), data.size());
    }

    @Test
    public void testGetAllActiveCategory() {
        when(categoryService.getActiveCategory()).thenReturn(categoryResponses);
        ResponseEntity<?> response = categoryController.getActiveCategory();
        Object body = response.getBody();

        assertNotNull(body);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Map<String, Object> json = (Map<String, Object>) body;
        assertEquals(json.get("status"), "success");
        assertEquals(json.get("message"), "success");

        List<CategoryResponse> data = (List<CategoryResponse>) json.get("data");
        assertNotNull(data);
        assertEquals(categoryResponses.size(), data.size());
    }

    @Test
    public void testGetCategoryById() throws Exception {
        when(categoryService.getCategoryById(id)).thenReturn(categoryDto);
        ResponseEntity<?> response = categoryController.getCategoryDetailsById(id);
        Object body = response.getBody();

        assertNotNull(body);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        Map<String, Object> json = (Map<String, Object>) body;
        assertEquals(json.get("status"), "success");
        assertEquals(json.get("message"), "success");

        CategoryDto data = (CategoryDto) json.get("data");
        assertNotNull(data);
        assertEquals(categoryDto, data);
    }

    @Test
    public void testDeleteCategoryById() {
        when(categoryService.deleteCategoryById(id)).thenReturn(true);
        ResponseEntity<?> response = categoryController.deleteCategoryById(id);
        Object body = response.getBody();

        Map<String, String> json = (Map<String, String>)body;

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assert json != null;
        assertEquals(json.get("status"), "success");
        assertEquals(json.get("message"), "Category deleted successfully");
    }
}
