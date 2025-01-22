package com.example.demo.service.category;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CategoryRequestDto categoryDto);

    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryRequestDto categoryDto);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);

    void deleteById(Long id);
}
