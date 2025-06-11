package com.example.Enotes_API_Service.service.Impl;

import com.example.Enotes_API_Service.entity.Category;
import com.example.Enotes_API_Service.repository.CategoryRepository;
import com.example.Enotes_API_Service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Boolean saveCategory(Category category) {
        category.setIsDeleted(false);
        category.setCreatedBy(1);
        category.setCreatedOn(new Date());
        Category saveCategory = categoryRepository.save(category);
        if(ObjectUtils.isEmpty(saveCategory))
        {
            return false;
        }
        return true;
    }

    @Override
    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }
}
