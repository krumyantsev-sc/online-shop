package com.scand.bookshop.dtos;

import lombok.Getter;
import lombok.Setter;

public class BookResponseDTO {
    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String genre;

    @Getter
    @Setter
    private double price;

    @Getter
    @Setter
    private String author;

    @Getter
    @Setter
    private String uuid;

}