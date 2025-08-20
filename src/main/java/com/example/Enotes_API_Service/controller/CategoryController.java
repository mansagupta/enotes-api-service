package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.CategoryDto;
import com.example.Enotes_API_Service.dto.CategoryResponse;
import com.example.Enotes_API_Service.endpoint.CategoryControllerEndpoint;
import com.example.Enotes_API_Service.service.CategoryService;
import com.example.Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController implements CategoryControllerEndpoint {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
        Boolean saveCategory = categoryService.saveCategory(categoryDto);
        if(saveCategory){
            return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
        }
        else{
            return CommonUtil.createErrorResponseMessage("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Cacheable("allCategory")
    public ResponseEntity<?> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        if(CollectionUtils.isEmpty(allCategory)){
            return ResponseEntity.noContent().build();
        }
        else{
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> getActiveCategory() {
        List<CategoryResponse> allCategory = categoryService.getActiveCategory();
        if(CollectionUtils.isEmpty(allCategory)){
            return ResponseEntity.noContent().build();
        }
        else{
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception {

        CategoryDto categoryDto = categoryService.getCategoryById(id);
        if(ObjectUtils.isEmpty(categoryDto)){
            return CommonUtil.createErrorResponseMessage("Internal server error", HttpStatus.NOT_FOUND);
        }
        else{
            return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id) {
        Boolean deleted = categoryService.deleteCategoryById(id);
        if(deleted){
            return CommonUtil.createBuildResponseMessage("Category deleted successfully", HttpStatus.OK);
        }
        else{
            return CommonUtil.createErrorResponseMessage("Category deleted successfully", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
