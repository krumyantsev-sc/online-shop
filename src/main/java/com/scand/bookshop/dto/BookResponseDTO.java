package com.scand.bookshop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BookResponseDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String genre;
    @NotNull
    private double price;
    @NotBlank
    private String author;
    @NotBlank
    private String uuid;
}