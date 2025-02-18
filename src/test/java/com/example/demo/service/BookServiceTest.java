package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.service.book.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getById_WithNonExisingBook_ShouldThrowException() {
        Long bookId = 100L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findBookById(bookId));

        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }

    @Test
    void getById_WithValidBook_ShouldReturnBook() {
        // given
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Winnie the Pooh");
        book.setAuthor("A.A.Milne");
        book.setPrice(BigDecimal.valueOf(5.99));
        book.setCategories(Set.of());

        BookDtoWithoutCategoryIds expected = new BookDtoWithoutCategoryIds()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setPrice(book.getPrice());

        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(bookMapper.toDtoWithoutCategories(book)).willReturn(expected);

        // when
        BookDtoWithoutCategoryIds actual = bookService.findBookById(bookId);

        // then
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDtoWithoutCategories(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void add_WithValidBookRequestDto_ShouldReturnBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Winnie the Pooh")
                .setAuthor("A.A.Milne")
                .setPrice(BigDecimal.valueOf(4.55))
                .setCategories(List.of());

        Book book = new Book();
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setPrice(requestDto.getPrice());
        book.setCategories(Set.of());

        BookDto expected = new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setPrice(requestDto.getPrice())
                .setCategories(requestDto.getCategories());

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.save(requestDto);

        assertEquals(expected, actual);
        verify(bookMapper).toModel(requestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void findAll_ValidPageable_ReturnsAllBooks() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("The Trial");
        book.setAuthor("Franz Kafka");
        book.setPrice(BigDecimal.valueOf(10.11));
        book.setCategories(Set.of());

        BookDto bookDto = new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setPrice(book.getPrice())
                .setCategories(List.of());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 5);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> actual = bookService.findAll(pageable);

        assertEquals(1, actual.getContent().size());
        assertEquals(bookDto, actual.getContent().get(0));
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void update_WithValidBookRequestDto_ShouldReturnBookDto() {
        Long bookId = 3L;
        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("The Trial");
        existingBook.setAuthor("Franz Kafka");
        existingBook.setPrice(BigDecimal.valueOf(77.77));
        existingBook.setCategories(Set.of());

        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Updated Title")
                .setAuthor("Updated Author")
                .setPrice(BigDecimal.valueOf(88.88))
                .setCategories(List.of());

        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle(requestDto.getTitle());
        updatedBook.setAuthor(requestDto.getAuthor());
        updatedBook.setPrice(requestDto.getPrice());
        updatedBook.setCategories(existingBook.getCategories());

        BookDto expected = new BookDto()
                .setId(bookId)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setPrice(requestDto.getPrice())
                .setCategories(requestDto.getCategories());

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expected);

        BookDto actual = bookService.update(bookId, requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).updateBookFromDto(requestDto, existingBook);
        verify(bookRepository).save(existingBook);
        verify(bookMapper).toDto(updatedBook);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    void update_WithNullId_ShouldThrowException() {
        Long bookId = null;
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setPrice(BigDecimal.TEN);

        // When
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(bookId, requestDto)
        );

        // Then
        assertEquals("Cannot find book by id: " + bookId, exception.getMessage());
    }

    @Test
    void update_WithNegativeId_ShouldThrowException() {
        Long invalidId = -1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Winnie the Pooh")
                .setAuthor("A.A.Milne")
                .setPrice(BigDecimal.valueOf(4.55))
                .setCategories(List.of());

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(invalidId, requestDto)
        );

        assertEquals("Cannot find book by id: " + invalidId, exception.getMessage());
        verify(bookRepository).findById(invalidId);
        verifyNoMoreInteractions(bookRepository);
        verifyNoInteractions(bookMapper);
    }
}
