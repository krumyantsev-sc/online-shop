package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.dtos.BookRequestDTO;
import com.scand.bookshop.dtos.BookResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.scand.bookshop.service.MetadataExtractor.extractPdfMetadata;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponseDTO createBook(MultipartFile file) throws IOException {
        Map<String, String> metadata = extractPdfMetadata(file);
        Book book = new Book();
        book.setTitle(metadata.get("Title"));
        book.setAuthor(metadata.get("Author"));
        book.setGenre(metadata.get("Subject"));
        String uniqueFilename = UUID.randomUUID().toString();
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
        Path path = Paths.get("uploads/" + uniqueFilename + extension);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, file.getBytes());
        book.setFilePath(path.toString());

        book.setUuid(uniqueFilename);
        book = bookRepository.save(book);

        return convertEntityToDTO(book);
    }

    public Optional<BookResponseDTO> getBookByUuid(String uuid) {
        Optional<Book> bookOptional = bookRepository.findByUuid(uuid);
        return bookOptional.map(this::convertEntityToDTO);
    }

    public void deleteBookByUuid(String uuid) {
        bookRepository.deleteByUuid(uuid);
    }

    public BookResponseDTO updateBook(String uuid, BookRequestDTO bookRequestDTO) {
        Optional<Book> bookOptional = bookRepository.findByUuid(uuid);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setTitle(bookRequestDTO.getTitle());
            book.setGenre(bookRequestDTO.getGenre());
            book.setPrice(bookRequestDTO.getPrice());
            book.setAuthor(bookRequestDTO.getAuthor());
            book = bookRepository.save(book);
            return convertEntityToDTO(book);
        } else {
            throw new RuntimeException("Book not found");
        }
    }

    private BookResponseDTO convertEntityToDTO(Book book) {
        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setTitle(book.getTitle());
        responseDTO.setGenre(book.getGenre());
        responseDTO.setPrice(book.getPrice());
        responseDTO.setAuthor(book.getAuthor());
        responseDTO.setUuid(book.getUuid());
        return responseDTO;
    }

    private BookResponseDTO convertToBookResponseDTO(Book book) {
        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setTitle(book.getTitle());
        responseDTO.setAuthor(book.getAuthor());
        responseDTO.setGenre(book.getGenre());
        responseDTO.setPrice(book.getPrice());
        responseDTO.setUuid(book.getUuid());
        return responseDTO;
    }

    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToBookResponseDTO)
                .collect(Collectors.toList());
    }
}