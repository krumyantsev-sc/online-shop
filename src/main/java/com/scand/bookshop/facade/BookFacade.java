package com.scand.bookshop.facade;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.Metadataextractor.Extractor;
import com.scand.bookshop.service.Metadataextractor.Metadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookFacade {
    private final BookService bookService;
    private final List<Extractor> extractors;

    @Valid
    public BookResponseDTO uploadBook(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Files without name are not supported");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        Extractor extractor = extractors.stream()
                .filter(fileExtractor -> fileExtractor.getExtension().equals(extension))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No extractor found for the extension"));
        try {
            Metadata metadata = extractor.extractMetaData(file);
            Book book = bookService.createBook(metadata.getTitle(), metadata.getAuthor(), metadata.getSubject(), extension);
            Path path = Paths.get(book.getFilePath());
            Files.write(path, file.getBytes());
            return DTOConverter.convertEntityToDTO(book);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error writing a file on disk");
        }
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(DTOConverter::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    public BookResponseDTO getBook(String uuid) {
        return DTOConverter.convertEntityToDTO(bookService.findBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Book not found")));
    }

    public void deleteBook(String uuid) {
        Book book = bookService.findBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        bookService.deleteBook(book);
    }

    @Valid
    public BookResponseDTO updateBook(String uuid, @RequestBody BookRequestDTO updatedBook) {
        Book book = bookService.findBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        book = bookService.updateBook(book,
                updatedBook.getTitle(),
                updatedBook.getGenre(),
                updatedBook.getPrice(),
                updatedBook.getAuthor()
        );
        return DTOConverter.convertEntityToDTO(book);
    }
}
