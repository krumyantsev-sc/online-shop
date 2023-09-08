package com.scand.bookshop.facade;

import com.scand.bookshop.dto.*;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.security.service.UserDetailsImpl;
import com.scand.bookshop.service.BookService;
import com.scand.bookshop.service.FileService;
import com.scand.bookshop.service.RatingService;
import com.scand.bookshop.service.UserService;
import com.scand.bookshop.service.metadataextractor.Extractor;
import com.scand.bookshop.service.metadataextractor.Metadata;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookFacade {
    private final BookService bookService;
    private final List<Extractor> extractors;
    private final FileService fileService;
    private final MessageSource messageSource;
    private final HttpServletRequest request;
    private final UserService userService;
    private final RatingService ratingService;

    public BookResponseDTO uploadBook(MultipartFile file, PriceDTO priceDTO) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException(messageSource.getMessage("file_empty", null, request.getLocale()));
        }
        String extension = fileService.getExtension(file);
        Extractor extractor = extractors.stream()
                .filter(fileExtractor -> fileExtractor.getExtension().equals(extension))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(messageSource.getMessage(
                                "extractor_not_found",
                                null,
                                request.getLocale())));
        Metadata metadata = extractor.extractMetaData(file);
        Book book = bookService.createBook(metadata.getTitle(),
                metadata.getAuthor(),
                metadata.getSubject(),
                extension,
                metadata.getContent(),
                priceDTO.getPrice());
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
        return DTOConverter.toDTO(bookService.getBookByUuid(uuid));
    }

    public Resource getBookCover(String uuid) {
        Book book = bookService.getBookByUuid(uuid);
        return bookService.getCover(book);
    }

    public List<String> getPreviewImages(String uuid) {
        Book book = bookService.getBookByUuid(uuid);
        return bookService.getPreviewImages(book);
    }

    public void deleteBook(String uuid) {
        Book book = bookService.getBookByUuid(uuid);
        bookService.deleteBook(book);
    }

    public BookResponseDTO updateBook(String uuid, BookRequestDTO updatedBook) {
        Book book = bookService.getBookByUuid(uuid);
        book = bookService.updateBook(book,
                updatedBook.getTitle(),
                updatedBook.getGenre(),
                updatedBook.getAuthor(),
                updatedBook.getDescription(),
                updatedBook.getPrice()
        );
        return DTOConverter.toDTO(book);
    }

    public ResponseEntity<byte[]> downloadBook(String uuid) {
        Book book = bookService.getBookByUuid(uuid);
        byte[] content = bookService.downloadBook(book);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "book.pdf");
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    public void addRating(String uuid, RatingRequestDTO ratingRequestDTO, UserDetailsImpl userPrincipal) {
        Book book = bookService.getBookByUuid(uuid);
        User user = userService.getUserById(userPrincipal.getId());
        ratingService.addRating(book,user,ratingRequestDTO.getRatingValue());
    }

    public RatingResponseDTO getRating(String uuid) {
        Book book = bookService.getBookByUuid(uuid);
        return DTOConverter.toDTO(ratingService.calculateAverageRating(book));
    }
}
