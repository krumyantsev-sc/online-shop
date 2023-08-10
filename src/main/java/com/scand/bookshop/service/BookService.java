package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final FileService fileService;
    private final BookCoverService bookCoverService;
    private final MessageSource messageSource;
    private final HttpServletRequest request;
    private static final org.apache.logging.log4j.Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(BookService.class);

    @Transactional
    public Book createBook(String title, String author, String subject, String extension, byte[] content) {
        String uniqueFilename = UUID.randomUUID().toString();
        String filePath = String.format("uploads/%s.%s", uniqueFilename, extension);
        Book book = new Book(null, title, subject, author, filePath, uniqueFilename,null);
        book = bookRepository.save(book);
        fileService.writeFile(Paths.get(book.getFilePath()), content);
        String coverPath = "uploads/covers/" + uniqueFilename + ".png";
        fileService.writeFile(Paths.get(coverPath), bookCoverService.generateCover(content, 0));
        logger.info("Book with id '{}' created", book.getId());
        return book;
    }

    public byte[] downloadBook(Book book) {
        Path path = Paths.get(book.getFilePath());
        if (!Files.exists(path)) {
            logger.warn("File of the book with UUID '{}' not found", book.getUuid());
            throw new IllegalArgumentException(messageSource.getMessage(
                    "file_not_found", null, request.getLocale()));
        }
        return fileService.readFile(book.getFilePath());
    }

    public Resource getCover(Book book) {
        String coverFilePath = "uploads/covers/" + book.getUuid() + ".png";
        Path coverPath = Paths.get(coverFilePath);
        if (!Files.exists(coverPath)) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "file_not_found", null, request.getLocale()));
        }
        return new org.springframework.core.io.PathResource(coverPath);
    }

    public List<String> getPreviewImages(Book book) {
        Path path = Paths.get(book.getFilePath());
        if (!Files.exists(path)) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "file_not_found", null, request.getLocale()));
        }
        List<String> previewImages = new ArrayList<>();
        byte[] fileContent = fileService.readFile(book.getFilePath());
        for (int i = 0; i < 4; i++) {
            String base64Image = Base64.getEncoder().encodeToString(bookCoverService.generateCover(fileContent, i));
            previewImages.add(base64Image);
        }
        return previewImages;
    }

    public Optional<Book> findBookByUuid(String uuid) {
        return bookRepository.findByUuid(uuid);
    }

    public Book getBookByUuid(String uuid) {
        return findBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "book_not_found", null, request.getLocale())));
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
        logger.info("Book with UUID '{}' deleted", book.getUuid());
    }

    @Transactional
    public Book updateBook(Book book, String title, String genre, String author, String description) {
        book = bookRepository.getReferenceById(book.getId());
        book.setTitle(title);
        book.setGenre(genre);
        book.setAuthor(author);
        book.setDescription(description);
        logger.info("Book with UUID '{}' updated", book.getUuid());
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