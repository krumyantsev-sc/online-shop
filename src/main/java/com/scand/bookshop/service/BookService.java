package com.scand.bookshop.service;

import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import org.springframework.stereotype.Service;

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

    public Book createBook(Map<String, String> metadata, String extension) {
        Book book = new Book();
        book.setTitle(metadata.get("Title"));
        book.setAuthor(metadata.get("Author"));
        book.setGenre(metadata.get("Subject"));
        String uniqueFilename = UUID.randomUUID().toString();
        book.setUuid(uniqueFilename);
        book.setFilePath("uploads/" + uniqueFilename + "." + extension);
        book = bookRepository.save(book);
        return book;
    }

    public Optional<Book> getBookByUuid(String uuid) {
        return bookRepository.findByUuid(uuid);
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    public Book updateBook(Book book, String title, String genre, Double price, String author) {
        book.setTitle(title);
        book.setGenre(genre);
        book.setPrice(price);
        book.setAuthor(author);
        book = bookRepository.save(book);
        return book;
    }

    public BookResponseDTO convertEntityToDTO(Book book) {
        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setTitle(book.getTitle());
        responseDTO.setGenre(book.getGenre());
        responseDTO.setPrice(book.getPrice());
        responseDTO.setAuthor(book.getAuthor());
        responseDTO.setUuid(book.getUuid());
        return responseDTO;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}