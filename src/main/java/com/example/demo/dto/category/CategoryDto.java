package com.example.demo.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {
    @NotNull
    @Positive
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
