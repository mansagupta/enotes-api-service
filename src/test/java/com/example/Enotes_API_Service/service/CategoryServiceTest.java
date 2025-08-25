package com.example.Enotes_API_Service.service;

import com.example.Enotes_API_Service.dto.CategoryDto;
import com.example.Enotes_API_Service.dto.CategoryResponse;
import com.example.Enotes_API_Service.entity.Category;
import com.example.Enotes_API_Service.exception.ExistDataException;
import com.example.Enotes_API_Service.exception.ResourceNotFoundException;
import com.example.Enotes_API_Service.repository.CategoryRepository;
import com.example.Enotes_API_Service.service.Impl.CategoryServiceImpl;
import com.example.Enotes_API_Service.util.Validation;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private Validation validation;

    @Mock
    private ModelMapper mapper;

    CategoryDto categoryDto = null;
    Category category = null;
    List<Category> categories = new ArrayList<>();
    List<CategoryDto> categoriesDto = new ArrayList<>();
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

        categories.add(category);
        categoriesDto.add(categoryDto);

        id = 1;
    }

    @Test
    public void testSaveCategory() {

        // arrange
        when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(false);
        when(mapper.map(categoryDto, Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        // act
        Boolean saveCategory = categoryService.saveCategory(categoryDto);

        // assert
        assertTrue(saveCategory);

        // verify
        verify(validation).categoryValidation(categoryDto);
        verify(categoryRepository).existsByName(categoryDto.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    public void testCategoryExist() {
        when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(true);
        ExistDataException exception = assertThrows(ExistDataException.class, () -> {
            categoryService.saveCategory(categoryDto);
        });
        assertEquals("Category already exist!", exception.getMessage());
        verify(validation).categoryValidation(categoryDto);
        verify(categoryRepository).existsByName(categoryDto.getName());
        verify(categoryRepository, never()).save(category);
    }

    @Test
    public void testUpdateCategory() {
        categoryDto.setId(1);
        category.setId(1);

        when(categoryRepository.existsByName(categoryDto.getName())).thenReturn(false);
        when(mapper.map(categoryDto, Category.class)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        Boolean saveCategory = categoryService.saveCategory(categoryDto);
        assertTrue(saveCategory);

        verify(validation).categoryValidation(categoryDto);
        verify(categoryRepository).existsByName(categoryDto.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    public void testGetAllCategory() {
        when(categoryRepository.findByIsDeletedFalse()).thenReturn(categories);
        List<CategoryDto> allCategory = categoryService.getAllCategory();

        assertEquals(allCategory.size(), categories.size());
        verify(categoryRepository).findByIsDeletedFalse();
    }

    @Test
    public void testGetAllActiveCategory() {
        when(categoryRepository.findByIsActiveTrueAndIsDeletedFalse()).thenReturn(categories);
        List<CategoryResponse> allCategory = categoryService.getActiveCategory();

        assertEquals(allCategory.size(), categories.size());
        verify(categoryRepository).findByIsActiveTrueAndIsDeletedFalse();
    }

    @Test
    public void testGetCategoryById() throws ResourceNotFoundException {
        when(categoryRepository.findByIdAndIsDeletedFalse(id)).thenReturn(Optional.ofNullable(category));
        when(mapper.map(category, CategoryDto.class)).thenReturn(categoryDto);
        CategoryDto categoryDtoById = categoryService.getCategoryById(id);

        assertNotNull(categoryDtoById);
        verify(categoryRepository).findByIdAndIsDeletedFalse(id);
    }

    @Test
    public void testDeleteCategoryById() {
        when(categoryRepository.findById(id)).thenReturn(Optional.ofNullable(category));
        Boolean categoryById = categoryService.deleteCategoryById(id);

        assertTrue(categoryById);
        verify(categoryRepository).findById(id);
    }
}
