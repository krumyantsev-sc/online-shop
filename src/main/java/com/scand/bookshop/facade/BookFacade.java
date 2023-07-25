package com.scand.bookshop.facade;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.dto.PageResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.metadataextractor.Extractor;
import com.scand.bookshop.service.metadataextractor.Metadata;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookFacade {
    private final BookService bookService;
    private final List<Extractor> extractors;

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
        Metadata metadata = extractor.extractMetaData(file);
        Book book = bookService.createBook(metadata.getTitle(),
                metadata.getAuthor(),
                metadata.getSubject(),
                extension,
                metadata.getContent());
        return DTOConverter.toDTO(book);
    }

    public PageResponseDTO getBooksPage(int pageNumber, int pageSize, String sortField, String sortDirection, String searchTerm) {
        Sort.Direction direction = Sort.Direction.valueOf(sortDirection.toUpperCase());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortField));
        Page<Book> bookPage;
        if (searchTerm == null) {
            bookPage = bookService.getAllBooks(pageable);
        } else {
            bookPage = bookService.searchBooks(searchTerm, pageable);
        }
        int totalPages = bookPage.getTotalPages();
        return new PageResponseDTO(bookPage.map(DTOConverter::toDTO).getContent(), totalPages);
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks().stream()
                .map(DTOConverter::toDTO)
                .collect(Collectors.toList());
    }

    public BookResponseDTO getBook(String uuid) {
        return DTOConverter.toDTO(bookService.findBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Book not found")));
    }

    public void deleteBook(String uuid) {
        Book book = bookService.findBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        bookService.deleteBook(book);
    }

    public BookResponseDTO updateBook(String uuid, BookRequestDTO updatedBook) {
        Book book = bookService.findBookByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Book not found"));
        book = bookService.updateBook(book,
                updatedBook.getTitle(),
                updatedBook.getGenre(),
                updatedBook.getAuthor()
        );
        return DTOConverter.toDTO(book);
    }
}
