package com.scand.bookshop.BookServiceTests;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class BookServiceTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @Test
    public void shouldCreateBook() {
        String title = "The Book";
        String author = "John Doe";
        String subject = "Computer Science";
        String extension = "pdf";
        byte[] content = "This is the content of the book.".getBytes();

        Book book = bookService.createBook(title, author, subject, extension, content);

        assertThat(book).isNotNull();
        assertThat(title.equals(book.getTitle())).isTrue();
        assertThat(author.equals(book.getAuthor())).isTrue();
        assertThat(subject.equals(book.getGenre())).isTrue();
    }

    @Test
    public void shouldRollbackIfBookCreationFails() {
        String author = "John Doe";
        String subject = "Computer Science";
        String extension = "pdf";
        byte[] content = "This is the content of the book.".getBytes();
        Book book = null; 
        try {
            book = bookService.createBook(null, author, subject, extension, content);
        } catch (Exception e) {
            // expected
        }
        assertThat(book).isNull();
    }

    @Test
    public void shouldUpdateBook() {
        String title = "The Book";
        String author = "John Doe";
        String subject = "Computer Science";
        String extension = "pdf";
        byte[] content = "This is the content of the book.".getBytes();

        Book book = bookService.createBook(title, author, subject, extension, content);

        String newTitle = "The New Book";
        String newAuthor = "Jane Doe";
        Double newPrice = 10.0;

        book = bookService.updateBook(book, newTitle, "new genre", newPrice, newAuthor);
        Optional<Book> newBook = bookService.findBookByUuid(book.getUuid());
        assertThat(newTitle.equals(newBook.get().getTitle())).isTrue();
        assertThat(newAuthor.equals(book.getAuthor())).isTrue();
        assertThat(newPrice.equals(book.getPrice())).isTrue();
    }

    @Test
    public void shouldRollbackIfBookUpdateFails() {
        String title = "The Book";
        String author = "John Doe";
        String subject = "Computer Science";
        String extension = "pdf";
        byte[] content = "This is the content of the book.".getBytes();

        Book book = bookService.createBook(title, author, subject, extension, content);

        String newTitle = null;

        try {
            bookService.updateBook(book, newTitle, null, null, null);
        } catch (Exception e) {
            // expected
        }

        Assertions.assertEquals(title, book.getTitle());
    }
}