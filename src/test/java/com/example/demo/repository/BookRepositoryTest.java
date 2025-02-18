package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @Sql(scripts = {
            "classpath:database/books/cleanup-db.sql",
            "classpath:database/books/category/insert-books-with-categories.sql"})
    void findAllByCategoriesId_ValidCategoryId_ShouldReturnBooksInCategory() {
        // given
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<Book> bookPage = bookRepository.findAllByCategoriesId(categoryId, pageable);
        // then
        assertNotNull(bookPage);
        assertFalse(bookPage.getContent().isEmpty());
        bookPage.getContent().forEach(book ->
                assertTrue(book.getCategories().stream()
                        .anyMatch(category -> category.getId().equals(categoryId)))
        );
    }

    @Test
    @Sql(scripts = "classpath:database/books/cleanup-db.sql")
    void findAllByCategoriesId_NonExistentCategoryId_ShouldReturnEmptyPage() {
        // given
        Long nonExistentCategoryId = 999L;
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<Book> bookPage = bookRepository.findAllByCategoriesId(nonExistentCategoryId, pageable);
        // then
        assertNotNull(bookPage);
        assertTrue(bookPage.getContent().isEmpty());
    }
}
