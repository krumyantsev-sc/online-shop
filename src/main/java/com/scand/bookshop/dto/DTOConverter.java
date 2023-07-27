package com.scand.bookshop.dto;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.User;

import java.util.Optional;

public class DTOConverter {
    public static BookResponseDTO toDTO(Book book) {
        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setTitle(book.getTitle());
        responseDTO.setGenre(book.getGenre());
        responseDTO.setAuthor(book.getAuthor());
        responseDTO.setUuid(book.getUuid());
        return responseDTO;
    }

    public static UserResponseDTO toUserDTO(User user) {
        System.out.println(user.getRegistrationDate());
        return  new UserResponseDTO(user.getLogin(), user.getEmail(), user.getRegistrationDate().toLocalDate().toString());
    }
}
