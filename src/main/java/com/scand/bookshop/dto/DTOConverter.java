package com.scand.bookshop.dto;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Comment;
import com.scand.bookshop.entity.User;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DTOConverter {
    public static BookResponseDTO toDTO(Book book) {
        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setTitle(book.getTitle());
        responseDTO.setGenre(book.getGenre());
        responseDTO.setAuthor(book.getAuthor());
        responseDTO.setUuid(book.getUuid());
        responseDTO.setDescription(book.getDescription());
        return responseDTO;
    }

    public static UserResponseDTO toUserDTO(User user) {
        return  new UserResponseDTO(user.getLogin(), user.getEmail(), user.getRegistrationDate().toLocalDate().toString());
    }

    public static CommentResponseDTO toDTO(Comment comment) {
        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setText(comment.getText());
        responseDTO.setUsername(comment.getUser().getLogin());
        responseDTO.setTimestamp(comment.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        responseDTO.setUuid(comment.getUuid());
        return responseDTO;
    }
}
