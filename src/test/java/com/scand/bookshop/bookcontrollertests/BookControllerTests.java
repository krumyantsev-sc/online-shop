package com.scand.bookshop.bookcontrollertests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.service.RegistrationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTests extends BaseTest {
    private String jwtToken;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeAll
    void createAdmin() {
        createAdmin(registrationService, "admin", "admin@mail.ru");
    }

    @BeforeEach
    public void authenticate() {
        jwtToken = login(testRestTemplate, "admin", "admin");
    }

    @Test
    public void shouldUploadBook() {
        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                createEntityWithFile("src/test/resources/files/book1.pdf", jwtToken,"file");
        ResponseEntity<BookResponseDTO> response =
                makePostRequestWithFile("/books/upload", requestEntity, BookResponseDTO.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUuid()).isNotNull();
        assertThat(bookRepository.findByUuid(response.getBody().getUuid())).isPresent();
    }

    @Test
    void shouldThrowExceptionForWrongExtension() {
        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                createEntityWithFile("src/test/resources/files/bg.jpg", jwtToken,"file");
        ResponseEntity<String> stringResponseEntity =
                makePostRequestWithFile("/books/upload", requestEntity, String.class);
        assertThat(stringResponseEntity.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldThrowExceptionForNoFile() {
        ResponseEntity<String> stringResponseEntity =
                makePostRequestWithToken(jwtToken, "/books/upload", null, String.class);
        assertThat(stringResponseEntity.getStatusCode().is4xxClientError()).isTrue();
    }

    @Test
    void shouldUpdateBook() {
        Book book = new Book(null,
                "old title",
                "old genre",
                "old author",
                "old filepath",
                UUID.randomUUID().toString());
        book = bookRepository.save(book);
        BookRequestDTO bookRequestDto = new BookRequestDTO("new title",
                "new genre",
                "new author",
                "new filepath");
        ResponseEntity<BookResponseDTO> response =
                makePostRequestWithToken(jwtToken,
                        "/books/" + book.getUuid() + "/update",
                        bookRequestDto,
                        BookResponseDTO.class);
        Optional<Book> updatedBook = bookRepository.findByUuid(book.getUuid());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getTitle()).isEqualTo(bookRequestDto.getTitle());
        assertThat(updatedBook.get().getAuthor()).isEqualTo(bookRequestDto.getAuthor());
        assertThat(updatedBook.get().getGenre()).isEqualTo(bookRequestDto.getGenre());
    }

    @Test
    void shouldNotUpdateBookWithNotFullData() {
        Book book = new Book(null,
                "old title",
                "old genre",
                "old author",
                "old filepath",
                UUID.randomUUID().toString());
        book = bookRepository.save(book);
        BookRequestDTO bookRequestDto = new BookRequestDTO("new title",
                "new genre",
                null,
                null);
        String uuid = book.getUuid();
        makePostRequestWithToken(jwtToken, "/books/" + uuid + "/update", bookRequestDto, BookResponseDTO.class);
        Optional<Book> updatedBook = bookRepository.findByUuid(book.getUuid());
        assertThat(updatedBook).isPresent();
        assertThat(updatedBook.get().getTitle()).isEqualTo("old title");
        assertThat(updatedBook.get().getAuthor()).isEqualTo("old author");
    }
}
