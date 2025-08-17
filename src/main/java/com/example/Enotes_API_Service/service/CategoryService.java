package com.example.Enotes_API_Service.service;

import com.example.Enotes_API_Service.dto.CategoryDto;
import com.example.Enotes_API_Service.dto.CategoryResponse;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;

import java.util.List;

public interface CategoryService {

    public Boolean saveCategory(CategoryDto category);

    public List<CategoryDto> getAllCategory();

    public List<CategoryResponse> getActiveCategory();

    public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException;

    public Boolean deleteCategoryById(Integer id);
}
