package com.example.demo.controller;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookSearchParametersDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book management", description = "Endpoints for managing books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books", description = "Get list of all books")
    public List<BookDto> findAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id", description = "Get book by id")
    public BookDto findById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create book", description = "Create new book")
    public BookDto save(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete book by id", description = "Delete book by id")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book by id", description = "Update book by id")
    public BookDto update(@PathVariable Long id, @RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.update(id, bookDto);
    }

    @PostMapping("/search")
    @Operation(summary = "Search books", description = "Search books by parameters")
    public List<BookDto> search(@RequestBody BookSearchParametersDto searchParameters) {
        return bookService.search(searchParameters);
    }
}
