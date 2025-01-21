package com.example.demo.dto.book;

public record BookSearchParametersDto(String[] titles, String[] authors, String[] isbns) {
}
