package com.example.Enotes_API_Service.controller;

import com.example.Enotes_API_Service.dto.CategoryDto;
import com.example.Enotes_API_Service.dto.CategoryResponse;
import com.example.Enotes_API_Service.entity.Category;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.service.CategoryService;
import com.example.Enotes_API_Service.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/save")
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto){
        Boolean saveCategory = categoryService.saveCategory(categoryDto);
        if(saveCategory){
            return CommonUtil.createBuildResponseMessage("saved success", HttpStatus.CREATED);
//            return new ResponseEntity<>("saved", HttpStatus.CREATED);
        }
        else{
            return CommonUtil.createErrorResponseMessage("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
//            return new ResponseEntity<>("not saved", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        if(CollectionUtils.isEmpty(allCategory)){
            return ResponseEntity.noContent().build();
        }
        else{
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
//            return new ResponseEntity<>(allCategory, HttpStatus.OK);
        }
    }

    @GetMapping("/getActive")
    public ResponseEntity<?> getActiveCategory(){
        List<CategoryResponse> allCategory = categoryService.getActiveCategory();
        if(CollectionUtils.isEmpty(allCategory)){
            return ResponseEntity.noContent().build();
        }
        else{
            return CommonUtil.createBuildResponse(allCategory, HttpStatus.OK);
//            return new ResponseEntity<>(allCategory, HttpStatus.OK);
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getCategoryDetailsById(@PathVariable Integer id) throws Exception {

        CategoryDto categoryDto = categoryService.getCategoryById(id);
        if(ObjectUtils.isEmpty(categoryDto)){
            return CommonUtil.createErrorResponseMessage("Internal server error", HttpStatus.NOT_FOUND);
//            return new ResponseEntity<>("Category not found with id: "+id, HttpStatus.NOT_FOUND);
        }
        else{
            return CommonUtil.createBuildResponse(categoryDto, HttpStatus.OK);
//            return new ResponseEntity<>(categoryDto, HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
        Boolean deleted = categoryService.deleteCategoryById(id);
        if(deleted){
            return CommonUtil.createBuildResponseMessage("Category deleted successfully", HttpStatus.OK);
//            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        }
        else{
            return CommonUtil.createErrorResponseMessage("Category deleted successfully", HttpStatus.INTERNAL_SERVER_ERROR);
//            return new ResponseEntity<>("Category deleted successfully", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
