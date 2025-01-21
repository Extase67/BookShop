package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface CategoryService {
    CategoryDto save(CategoryDto categoryDto);

    Page<CategoryDto> findAll(Pageable pageable, Sort sort);

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
