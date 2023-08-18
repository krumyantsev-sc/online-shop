package com.scand.bookshop.commentservicetests;

import com.scand.bookshop.BaseTest;
import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Comment;
import com.scand.bookshop.repository.BookRepository;
import com.scand.bookshop.repository.CommentRepository;
import com.scand.bookshop.repository.UserRepository;
import com.scand.bookshop.service.CommentService;
import com.scand.bookshop.service.RegistrationService;
import com.scand.bookshop.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CommentServiceTests extends BaseTest {
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    private Book book;

    @BeforeAll
    private void setUp() {
        createAdmin(registrationService, "adminCommentService", "admin");
        book = new Book(null,
                "testService",
                "test",
                "test",
                "testpath",
                UUID.randomUUID().toString(),
                "desc");
        book = bookRepository.save(book);
    }

    @Test
    public void createComment() {
        commentService.add("test",book, userRepository.findByLogin("adminCommentService").get(), null);
        assertThat(commentRepository.findByBook(book)).isNotEmpty();
    }

    @Test
    public void updateComment() {
        Comment comment = new Comment(null,
                null,
                null,
                "created",
                book,
                userService.findUserByUsername("adminCommentService").get(),
                LocalDateTime.now(),
                UUID.randomUUID().toString());
        comment = commentRepository.save(comment);
        commentService.updateComment(comment,"testUpdate");
        assertThat(commentService.getCommentByUuid(comment.getUuid()).getText()).isEqualTo("testUpdate");
    }

    @Test
    public void deleteComment() {
        Comment comment = new Comment(null,
                null,
                null,
                "created",
                book,
                userService.findUserByUsername("adminCommentService").get(),
                LocalDateTime.now(),
                UUID.randomUUID().toString());
        comment = commentRepository.save(comment);
        commentService.deleteComment(comment);
        assertThat(commentService.findCommentByUuid(comment.getUuid())).isNotPresent();
    }
}
