package com.example.demo.repository.book.spec;

import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ISBN_FIELD = "isbn";

    @Override
    public String getKey() {
        return ISBN_FIELD;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(ISBN_FIELD).in(Arrays.stream(params).toArray());
    }
}
