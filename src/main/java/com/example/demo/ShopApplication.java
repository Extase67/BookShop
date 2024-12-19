package com.example.demo;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopApplication {
    private static final double FIRST_BOOK_PRICE = 19.99;
    private static final double SECOND_BOOK_PRICE = 29.99;

    @Autowired
    private BookService bookService;

    public static void main(final String[] args) {
        SpringApplication.run(ShopApplication.class, args);

    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book firstBook = new Book();
            firstBook.setTitle("Book 1");
            firstBook.setAuthor("Author 1");
            firstBook.setIsbn("1234567890");
            firstBook.setPrice(BigDecimal.valueOf(FIRST_BOOK_PRICE));
            firstBook.setDescription("Description 1");
            firstBook.setCoverImage("cover1.jpg");

            Book secondBook = new Book();
            secondBook.setTitle("Book 2");
            secondBook.setAuthor("Author 2");
            secondBook.setIsbn("0987654321");
            secondBook.setPrice(BigDecimal.valueOf(SECOND_BOOK_PRICE));
            secondBook.setDescription("Description 2");
            secondBook.setCoverImage("cover2.jpg");

            bookService.save(firstBook);
            bookService.save(secondBook);

            System.out.println("Books in the database:");
            bookService.findAll().forEach(book ->
                    System.out.println(book.getTitle()
                            + " by " + book.getAuthor()));
        };
    }
}
