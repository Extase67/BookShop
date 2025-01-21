package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.book.BookSpecificationBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto findBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book by id: " + id));
    }

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toModel(bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable, Sort sort) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find book by id: " + id));
        bookMapper.updateModelFromDto(book, bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = bookSpecificationBuilder.build(searchParameters);
        return bookMapper.toDtoList(bookRepository.findAll(spec));
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategories_Id(categoryId, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }
}
