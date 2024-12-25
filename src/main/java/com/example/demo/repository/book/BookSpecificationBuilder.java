package com.example.demo.repository.book;

import com.example.demo.dto.BookSearchParametersDto;
import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationBuilder;
import com.example.demo.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR_KEY = "author";
    private static final String TITLE_KEY = "title";
    private static final String ISBN_KEY = "isbn";
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParameters.authors()));
        }
        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParameters.titles()));
        }
        if (searchParameters.isbns() != null && searchParameters.isbns().length > 0) {
            spec = spec.and(specificationProviderManager.getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParameters.isbns()));
        }
        return spec;
    }
}