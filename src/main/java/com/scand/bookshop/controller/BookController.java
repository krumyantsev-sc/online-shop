package com.scand.bookshop.controller;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.dtos.BookRequestDTO;
import com.scand.bookshop.dtos.BookResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !(originalFilename.endsWith(".pdf"))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bookService.createBook(file));
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/{uuid}")
    public ResponseEntity<BookResponseDTO> getBook(@PathVariable String uuid) {
        return bookService.getBookByUuid(uuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/books/{uuid}")
    public ResponseEntity<Void> deleteBook(@PathVariable String uuid) {
        bookService.deleteBookByUuid(uuid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/books/{uuid}/update")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable String uuid, @RequestBody BookRequestDTO updatedBook) {
        try {
            return ResponseEntity.ok(bookService.updateBook(uuid, updatedBook));
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}