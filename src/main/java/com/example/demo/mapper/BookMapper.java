package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categories", ignore = true)
    BookDto toDto(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    List<BookDto> toDtoList(List<Book> books);

    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategory(CreateBookRequestDto requestDto, @MappingTarget Book book) {
        book.setCategories(map(requestDto.getCategories()));
    }

    @AfterMapping
    default void setCategoriesIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Category> booksCategories = book.getCategories();
        List<String> bookDtoCategories = new ArrayList<>();
        for (Category c : booksCategories) {
            bookDtoCategories.add(c.getName());
        }
        bookDto.setCategories(bookDtoCategories);
    }

    default Set<Category> map(List<String> value) {
        return value.stream()
                .map(Category::new)
                .collect(Collectors.toSet());
    }
}
