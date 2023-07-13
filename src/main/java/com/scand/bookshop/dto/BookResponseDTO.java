package com.scand.bookshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponseDTO {
    private String title;
    private String genre;
    private double price;
    private String author;
    private String uuid;
}