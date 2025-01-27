package com.example.demo.service.category;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.category.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public CategoryDto save(CategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find category with id: "
                        + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id " + id));
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoriesId(id, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }
}
