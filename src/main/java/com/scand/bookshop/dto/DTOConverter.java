package com.scand.bookshop.dto;

import com.scand.bookshop.entity.Book;

public class DTOConverter {
    public static BookResponseDTO convertEntityToDTO(Book book) {
        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setTitle(book.getTitle());
        responseDTO.setGenre(book.getGenre());
        responseDTO.setPrice(book.getPrice());
        responseDTO.setAuthor(book.getAuthor());
        responseDTO.setUuid(book.getUuid());
        return responseDTO;
    }
}
