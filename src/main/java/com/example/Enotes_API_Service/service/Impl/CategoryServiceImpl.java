package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.dto.CategoryDto;
import com.example.Enotes_API_Service.dto.CategoryResponse;
import com.example.Enotes_API_Service.entity.Category;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.repository.CategoryRepository;
import com.example.Enotes_API_Service.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Boolean saveCategory(CategoryDto categoryDto) {

        Category category = mapper.map(categoryDto, Category.class);

        if(ObjectUtils.isEmpty(category.getId())){
            category.setIsDeleted(false);
            category.setCreatedBy(1);
            category.setCreatedOn(new Date());
        } else {
            updateCategory(category);
        }
        Category saveCategory = categoryRepository.save(category);
        return !ObjectUtils.isEmpty(saveCategory);
    }

    private void updateCategory(Category category) {
        Optional<Category> findById = categoryRepository.findById(category.getId());
        if (findById.isPresent()) {
            Category existing = findById.get();
            category.setCreatedBy(existing.getCreatedBy());
            category.setCreatedOn(existing.getCreatedOn());
            category.setIsDeleted(existing.getIsDeleted());

            category.setUpdatedBy(1);
            category.setUpdatedOn(new Date());
        }
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> categories = categoryRepository.findByIsDeletedFalse();
        return categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();
    }

    @Override
    public List<CategoryResponse> getActiveCategory() {
        List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
        return categories.stream().map(cat -> mapper.map(cat, CategoryResponse.class)).toList();
    }

    @Override
    public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: "+id));
        if(!ObjectUtils.isEmpty(category)){
            return mapper.map(category, CategoryDto.class);
        }
        return null;
    }

    @Override
    public Boolean deleteCategoryById(Integer id) {
        Optional<Category> findByCategory = categoryRepository.findById(id);
        if(findByCategory.isPresent()){
            Category category = findByCategory.get();
            category.setIsDeleted(true);
            categoryRepository.save(category);
            return true;
        }
        return false;
    }
}
