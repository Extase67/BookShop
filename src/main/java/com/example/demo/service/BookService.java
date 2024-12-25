package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {

    BookDto findBookById(Long id);

    BookDto save(CreateBookRequestDto bookDto);

    List<BookDto> findAll();

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);
}
