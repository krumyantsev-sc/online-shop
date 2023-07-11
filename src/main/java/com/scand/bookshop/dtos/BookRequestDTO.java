package com.scand.bookshop.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequestDTO {
    private String title;
    private String genre;
    private double price;
    private String author;
    private String filePath;
}
