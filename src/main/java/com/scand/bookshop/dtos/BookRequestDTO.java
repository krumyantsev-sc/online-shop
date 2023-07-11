package com.scand.bookshop.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequestDTO {
    @NotEmpty
    private String title;
    private String genre;
    private double price;
    private String author;
    private String filePath;
}
