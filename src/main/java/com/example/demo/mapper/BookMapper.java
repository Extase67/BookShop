package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.model.Book;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto bookDto);

    List<BookDto> toDtoList(List<Book> books);

    void updateModelFromDto(@MappingTarget Book book, CreateBookRequestDto bookDto);
}
