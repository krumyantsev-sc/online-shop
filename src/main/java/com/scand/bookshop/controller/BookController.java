package com.scand.bookshop.controller;


import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.scand.bookshop.service.MetadataExtractor.extractPdfMetadata;

@RestController
public class BookController {
    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !(originalFilename.endsWith(".pdf") || originalFilename.endsWith(".epub"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "File type not supported"));
        }
        String uniqueFilename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        Path path = Paths.get("uploads/" + uniqueFilename + extension);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, file.getBytes());
        Map<String, String> metadata = extractPdfMetadata(file);

        Book book = new Book();
        book.setTitle(metadata.get("Title"));
        book.setAuthor(metadata.get("Author"));
        book.setGenre(metadata.get("Subject"));
        book.setUuid(uniqueFilename);
        book.setFilePath(path.toString());
        bookRepository.save(book);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("books/{uuid}")
    public ResponseEntity<Void> deleteBook(@PathVariable String uuid) {
        Optional<Book> bookOptional = bookRepository.findByUuid(uuid);
        if (bookOptional.isPresent()) {
            bookRepository.delete(bookOptional.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("books/{uuid}/update")
    public ResponseEntity<Book> updateBook(@PathVariable String uuid, @RequestBody Book updatedBook) {
        Optional<Book> bookOptional = bookRepository.findByUuid(uuid);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setTitle(updatedBook.getTitle());
            book.setGenre(updatedBook.getGenre());
            book.setPrice(updatedBook.getPrice());
            book.setAuthor(updatedBook.getAuthor());
            bookRepository.save(book);
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}