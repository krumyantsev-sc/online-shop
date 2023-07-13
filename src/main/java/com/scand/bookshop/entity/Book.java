package com.scand.bookshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "book_id", nullable = false)
    private String uuid;
}