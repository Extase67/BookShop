package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import java.util.List;
import java.util.Set;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);

    List<BookDto> toDtoList(List<Book> books);

    void updateModelFromDto(@MappingTarget Book book, CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    default List<Long> map(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .toList();
    }

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategories(map(book.getCategories()));
    }
}
