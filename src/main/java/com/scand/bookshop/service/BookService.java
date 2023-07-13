package com.scand.bookshop.service;

import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Book createBook(String title, String author, String subject, String extension) {
        String uniqueFilename = UUID.randomUUID().toString();
        String filePath = "uploads/" + uniqueFilename + "." + extension;
        Book book = new Book(title,author,subject,uniqueFilename, filePath);
        book = bookRepository.save(book);
        return book;
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