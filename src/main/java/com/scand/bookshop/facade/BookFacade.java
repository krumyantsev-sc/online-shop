package com.scand.bookshop.facade;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.Metadataextractor.EpubExtractor;
import com.scand.bookshop.service.Metadataextractor.Extractor;
import com.scand.bookshop.service.Metadataextractor.PdfExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class BookFacade {
    private final BookService bookService;
    private final List<Extractor> extractors = Arrays.asList(new EpubExtractor(), new PdfExtractor());

    public BookFacade(BookService bookService) {
        this.bookService = bookService;
    }

    public BookResponseDTO uploadBook(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || (!(originalFilename.endsWith(".pdf")) && !(originalFilename.endsWith(".epub")))) {
            throw new IllegalArgumentException("Unsupported format");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        Extractor extractor = extractors.stream()
                .filter(fileExtractor -> fileExtractor.getExtension().equals(extension))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No extractor found for the extension"));
        Book book = bookService.createBook(extractor.extractMetaData(file), extension);
        Path path = Paths.get("uploads/" + book.getUuid() + "." + extension);
        Files.write(path, file.getBytes());
        return bookService.convertEntityToDTO(book);
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(bookService::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public BookResponseDTO getBook(@PathVariable String uuid) {
        return bookService.convertEntityToDTO(bookService.getBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Book not found")));
    }

    public void deleteBook(@PathVariable String uuid) {
        Book book = bookService.getBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        bookService.deleteBook(book);
    }

    public BookResponseDTO updateBook(@PathVariable String uuid, @RequestBody BookRequestDTO updatedBook) {
        Book book = bookService.getBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));

        bookService.updateBook(book,
                updatedBook.getTitle() == null ? book.getTitle() : updatedBook.getTitle(),
                updatedBook.getGenre() == null ? book.getGenre() : updatedBook.getGenre(),
                updatedBook.getPrice() == 0.0 ? book.getPrice() : updatedBook.getPrice(),
                updatedBook.getAuthor() == null ? book.getAuthor() : updatedBook.getAuthor()
        );

        return bookService.convertEntityToDTO(book);
    }
}
