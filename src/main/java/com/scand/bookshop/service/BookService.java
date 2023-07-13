package com.scand.bookshop.service;

import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Book createBook(String title, String author, String subject, String extension, byte[] content) {
        String uniqueFilename = UUID.randomUUID().toString();
        String filePath = "uploads/" + uniqueFilename + "." + extension;
        Book book = new Book(null, title, subject, 0.0, author, uniqueFilename, filePath);
        book = bookRepository.save(book);
        writeFile(Paths.get(book.getFilePath()), content);
        return book;
    }

    private void writeFile(Path path, byte[] content) {
        try {
            Files.write(path, content);
        } catch (IOException e) {
            throw new RuntimeException("Error");
        }
    }

    public Optional<Book> findBookByUuid(String uuid) {
        return bookRepository.findByUuid(uuid);
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    @Transactional
    public Book updateBook(Book book, String title, String genre, Double price, String author) {
        book.setTitle(title);
        book.setGenre(genre);
        book.setPrice(price);
        book.setAuthor(author);
        return book;
    }


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}