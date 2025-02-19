package com.example.demo.util;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import java.math.BigDecimal;
import java.util.List;

public class BookTestUtil {
    public static CreateBookRequestDto createRequestDto() {
        return new CreateBookRequestDto()
                .setAuthor("Joseph Heller")
                .setPrice(BigDecimal.valueOf(15.99))
                .setTitle("Catch-22")
                .setIsbn("12324564789")
                .setDescription("Great novel.")
                .setCategories(List.of());
    }

    public static BookDto createBookDto() {
        CreateBookRequestDto requestDto = createRequestDto();
        return new BookDto()
                .setId(1L)
                .setAuthor(requestDto.getAuthor())
                .setPrice(requestDto.getPrice())
                .setTitle(requestDto.getTitle())
                .setIsbn(requestDto.getIsbn())
                .setDescription(requestDto.getDescription())
                .setCategories(requestDto.getCategories());
    }

    public static List<BookDto> createBookDtoList() {
        return List.of(
                new BookDto()
                        .setId(1L)
                        .setAuthor("J.K.Rowling")
                        .setTitle("Harry Potter")
                        .setPrice(BigDecimal.valueOf(45.99))
                        .setDescription("Magic book.")
                        .setIsbn("123456789")
                        .setCategories(List.of()),
                new BookDto()
                        .setId(2L)
                        .setAuthor("Franz Kafka")
                        .setTitle("Castle")
                        .setPrice(BigDecimal.valueOf(19.99))
                        .setDescription("Great novel.")
                        .setIsbn("223456789")
                        .setCategories(List.of()),
                new BookDto()
                        .setId(3L)
                        .setAuthor("Franz Kafka")
                        .setTitle("The Trial")
                        .setPrice(BigDecimal.valueOf(7.99))
                        .setDescription("Great novel.")
                        .setIsbn("323456789")
                        .setCategories(List.of())
        );
    }

    public static BookDtoWithoutCategoryIds createBookDtoWithoutCategories() {
        return new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setAuthor("J.K.Rowling")
                .setTitle("Harry Potter")
                .setPrice(BigDecimal.valueOf(45.99))
                .setIsbn("1232456789")
                .setDescription("Magic book.");
    }

    public static CreateBookRequestDto createUpdateRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Castle")
                .setAuthor("Franz Kafka")
                .setPrice(BigDecimal.valueOf(40))
                .setIsbn("123456333")
                .setDescription("Great Novel.")
                .setCategories(List.of());
    }

    public static BookDto createUpdateBookDto() {
        CreateBookRequestDto requestDto = createUpdateRequestDto();
        return new BookDto()
                .setId(2L)
                .setPrice(requestDto.getPrice())
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setDescription(requestDto.getDescription())
                .setIsbn(requestDto.getIsbn())
                .setCategories(requestDto.getCategories());
    }
}
