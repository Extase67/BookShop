package com.example.demo.controller;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.service.book.BookService;
import com.example.demo.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create category", description = "Create new category")
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get all categories",
            description = "Get list of all categories with pagination and sorting")
    public Page<CategoryDto> getAll(Pageable pageable, @RequestParam(required = false) Sort sort) {
        return categoryService.findAll(pageable, sort);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get category by id", description = "Get category by id")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update category by id",
            description = "Update category by id")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by id",
            description = "Delete category by id")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by category id",
            description = "Get books by category id with pagination and sorting")
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id, Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
