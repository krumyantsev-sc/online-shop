package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Comment;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.CommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MessageSource messageSource;
    private final HttpServletRequest request;
    private static final org.apache.logging.log4j.Logger logger =
            org.apache.logging.log4j.LogManager.getLogger(CommentService.class);

    public void add(String text, Book book, User user) {
        logger.info("Adding comment for book with ID: " + book.getId() + " by user with ID: " + user.getId());
        Comment comment = new Comment(null, text, book, user, LocalDateTime.now(), UUID.randomUUID().toString());
        commentRepository.save(comment);
        logger.info("Comment saved with UUID: " + comment.getUuid());
    }

    public Optional<Comment> findCommentByUuid(String uuid) {
        return commentRepository.findByUuid(uuid);
    }

    public Comment getCommentByUuid(String uuid) {
        logger.info("Fetching comment with UUID: " + uuid);
        return findCommentByUuid(uuid)
                .orElseThrow(() -> {
                    logger.warn("Comment with UUID: " + uuid + " not found");
                    return new NoSuchElementException(messageSource.getMessage("comment_not_found", null, request.getLocale()));
                });
    }

    public void deleteComment(Comment comment) {
        logger.info("Deleting comment with ID: " + comment.getId() + " and UUID: " + comment.getUuid());
        commentRepository.delete(comment);
        logger.info("Comment deleted");
    }

    @Transactional
    public void updateComment(Comment comment, String newText) {
        logger.info("Updating comment with ID: " + comment.getId());
        comment = commentRepository.getReferenceById(comment.getId());
        comment.setText(newText);
        logger.info("Comment updated");
    }

    public List<Comment> getComments(Book book) {
        logger.info("Fetching comments for book with ID: " + book.getId());
        return commentRepository.findByBook(book);
    }
}
