package com.example.demo.service.category;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CategoryDto categoryDto);

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto update(Long id, CategoryDto categoryDto);

    Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);

    void deleteById(Long id);
}
