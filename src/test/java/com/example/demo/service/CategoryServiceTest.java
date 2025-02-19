package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.category.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @Sql(scripts = "classpath:database/books/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getById_WithNonExisingCategory_ShouldThrowException() {
        // Given
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        // When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );
        // Then
        String expected = "Can't find category with id: " + categoryId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void getById_WithValidCategory_ShouldReturnCategory() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Horror");
        category.setDescription("Scary books.");

        CategoryDto expected = new CategoryDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());

        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);
        // When
        CategoryDto actual = categoryService.getById(categoryId);
        // Then
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void save_WithValidCategoryRequestDto_ShouldReturnCategoryDto() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");
        category.setDescription("Scary books.");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        when(categoryMapper.toModel(any(CategoryDto.class)))
                .thenReturn(category);
        when(categoryMapper.toDto(any(Category.class)))
                .thenReturn(categoryDto);
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(category);

        // When
        CategoryDto savedCategoryDto = categoryService.save(categoryDto);

        // Then
        assertNotNull(savedCategoryDto);
        assertEquals(categoryDto, savedCategoryDto);

        // Verify interactions
        verify(categoryMapper).toModel(categoryDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    public void findAll_ValidPageable_ReturnsAllCategories() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");
        category.setDescription("Scary books.");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, 5);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        List<CategoryDto> categoriesDto = categoryService.findAll(pageable);

        // Then
        assertEquals(1,categoriesDto.size());
        assertEquals(categoryDto,categoriesDto.get(0));

        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void update_WithValidCategoryRequestDto_ShouldReturnCategoryDto() {
        // Given
        Category existingCategory = new Category();
        existingCategory.setId(3L);
        existingCategory.setName("History");
        existingCategory.setDescription("History books");

        String updatedName = "Updated History";
        String updatedDescription = "Updated History books";

        CategoryDto categoryDtoToUpdate = new CategoryDto();
        categoryDtoToUpdate.setId(existingCategory.getId());
        categoryDtoToUpdate.setName(updatedName);
        categoryDtoToUpdate.setDescription(updatedDescription);

        when(categoryRepository.findById(any()))
                .thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(existingCategory);
        when(categoryMapper.toDto(any(Category.class)))
                .thenReturn(categoryDtoToUpdate);

        // When
        CategoryDto updatedCategoryDto = categoryService.update(3L, categoryDtoToUpdate);

        // Then
        assertNotNull(updatedCategoryDto);
        assertEquals(updatedName, updatedCategoryDto.getName());
        assertEquals(updatedDescription, updatedCategoryDto.getDescription());

        verify(categoryRepository, times(1))
                .save(any(Category.class));
        verify(categoryMapper, times(1))
                .updateCategoryFromDto(any(CategoryDto.class), any(Category.class));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    void update_WithNullId_ShouldThrowException() {
        CategoryDto categoryDto = new CategoryDto()
                .setName("Test Category")
                .setDescription("Test Description");

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.update(null, categoryDto)
        );

        assertEquals("Can't find category by id null", exception.getMessage());
        verify(categoryRepository).findById(isNull());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}
