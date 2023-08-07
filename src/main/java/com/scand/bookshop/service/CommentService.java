package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Comment;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
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

    public void add(String text, Book book, User user) {
        Comment comment = new Comment(null, text, book, user, LocalDateTime.now(), UUID.randomUUID().toString());
        commentRepository.save(comment);
    }

    public Optional<Comment> findCommentByUuid(String uuid) {
        return commentRepository.findByUuid(uuid);
    }

    public Comment getCommentByUuid(String uuid) {
        return findCommentByUuid(uuid)
                .orElseThrow(() -> new NoSuchElementException("Comment not found"));
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Transactional
    public void updateComment(Comment comment, String newText) {
        comment = commentRepository.getReferenceById(comment.getId());
        comment.setText(newText);
    }

    public List<Comment> getComments(Book book) {
        return commentRepository.findByBook(book);
    }
}
