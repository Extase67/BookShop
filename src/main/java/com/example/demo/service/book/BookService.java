package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface BookService {
    Page<BookDto> findAll(Pageable pageable, Sort sort);

    BookDto findBookById(Long id);

    BookDto save(CreateBookRequestDto bookDto);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    List<BookDto> search(BookSearchParametersDto searchParameters);

    Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable);
}
