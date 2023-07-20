package com.scand.bookshop.BookControllerTests;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    private HttpEntity<MultiValueMap<String, Object>> createEntityWithFile(String path) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        FileSystemResource resource = new FileSystemResource(new File(path));
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        if (!path.equals("")) {
            body.add("file", resource);
        }
        return new HttpEntity<>(body, headers);
    }

    @Test
    public void shouldUploadBook() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = createEntityWithFile("src/test/resources/files/book1.pdf");
        ResponseEntity<BookResponseDTO> response = restTemplate.postForEntity("http://localhost:" + port + "/books/upload", requestEntity, BookResponseDTO.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUuid()).isNotNull();
        assertThat(bookRepository.findByUuid(response.getBody().getUuid())).isPresent();
    }

    @Test
    void shouldThrowExceptionForWrongExtension() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = createEntityWithFile("src/test/resources/files/bg.jpg");
        assertThrows(org.springframework.web.client.HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.postForEntity("http://localhost:" + port + "/books/upload", requestEntity, BookResponseDTO.class);
        });
    }

    @Test
    void shouldThrowExceptionForNoFile() {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = createEntityWithFile("");
        assertThrows(org.springframework.web.client.HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.postForEntity("http://localhost:" + port + "/books/upload", requestEntity, BookResponseDTO.class);
        });
    }

    @Test
    void shouldUpdateBook() {
        RestTemplate restTemplate = new RestTemplate();
        Book book = new Book(null, "old title", "old genre", 0.0, "old author", "old filepath", UUID.randomUUID().toString());
        book = bookRepository.save(book);
        BookRequestDTO bookRequestDto = new BookRequestDTO("new title", "new genre", 1.00, "new author", null);
        ResponseEntity<BookResponseDTO> response =
                restTemplate.postForEntity("http://localhost:" + port + "/books/" + book.getUuid() + "/update",
                        bookRequestDto,
                        BookResponseDTO.class);
        Optional<Book> updatedBook = bookRepository.findByUuid(book.getUuid());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getTitle()).isEqualTo(bookRequestDto.getTitle());
        assertThat(updatedBook.get().getAuthor()).isEqualTo(bookRequestDto.getAuthor());
        assertThat(updatedBook.get().getGenre()).isEqualTo(bookRequestDto.getGenre());
        assertThat(updatedBook.get().getPrice() == (bookRequestDto.getPrice())).isTrue();
    }

    @Test
    void shouldNotUpdateBookWithNotFullData() {
        RestTemplate restTemplate = new RestTemplate();
        Book book = new Book(null, "old title", "old genre", 0.0, "old author", "old filepath", UUID.randomUUID().toString());
        book = bookRepository.save(book);
        BookRequestDTO bookRequestDto = new BookRequestDTO("new title", "new genre", 1.00, null, null);
        String uuid = book.getUuid();
        assertThrows(org.springframework.web.client.HttpClientErrorException.BadRequest.class, () -> {
            restTemplate.postForEntity("http://localhost:" + port + "/books/" + uuid + "/update",
                    bookRequestDto,
                    BookResponseDTO.class);
        });
    }
}
