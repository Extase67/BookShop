package com.example.demo.repository.book.spec;

import static com.example.demo.repository.book.BookSpecificationBuilder.ISBN_KEY;

import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return ISBN_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(ISBN_KEY).in(Arrays.stream(params).toArray());
    }
}
