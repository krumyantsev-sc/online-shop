package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final FileService fileService;
    private final BookCoverService bookCoverService;

    @Transactional
    public Book createBook(String title, String author, String subject, String extension, byte[] content) {
        String uniqueFilename = UUID.randomUUID().toString();
        String filePath = "uploads/" + uniqueFilename + "." + extension;
        Book book = new Book(null, title, subject, author, filePath, uniqueFilename);
        book = bookRepository.save(book);
        fileService.writeFile(Paths.get(book.getFilePath()), content);
        String coverPath = "uploads/covers/" + uniqueFilename + ".png";
        fileService.writeFile(Paths.get(coverPath), bookCoverService.generateCover(content));
        return book;
    }

    public Resource getCover(Book book) {
        String coverFilePath = "uploads/covers/" + book.getUuid() + ".png";
        Path coverPath = Paths.get(coverFilePath);
        if (!Files.exists(coverPath)) {
            throw new IllegalArgumentException("ERROR");
        }
        return new org.springframework.core.io.PathResource(coverPath);
    }

    public Optional<Book> findBookByUuid(String uuid) {
        return bookRepository.findByUuid(uuid);
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    @Transactional
    public Book updateBook(Book book, String title, String genre, String author) {
        book = bookRepository.getReferenceById(book.getId());
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        return book;
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Page<Book> searchBooks(String searchTerm, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCaseOrAuthorContainingIgnoreCase(searchTerm, searchTerm, searchTerm, pageable);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}