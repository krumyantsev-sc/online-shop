package com.scand.bookshop.controller;
import com.scand.bookshop.facade.BookFacade;
import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("books")
public class BookController {

    private final BookFacade bookFacade;

    public BookController(BookFacade bookFacade) {
        this.bookFacade = bookFacade;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BookResponseDTO uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return bookFacade.uploadBook(file);
    }

    @GetMapping("/")
    public List<BookResponseDTO> getAllBooks() {
        return bookFacade.getAllBooks();
    }

    @GetMapping("/{uuid}")
    public BookResponseDTO getBook(@PathVariable String uuid) {
        return bookFacade.getBook(uuid);
    }

    @DeleteMapping("/{uuid}")
    public void deleteBook(@PathVariable String uuid) {
        bookFacade.deleteBook(uuid);
    }

    @PostMapping("/{uuid}/update")
    public BookResponseDTO updateBook(@PathVariable String uuid, @RequestBody BookRequestDTO updatedBook) {
       return bookFacade.updateBook(uuid, updatedBook);
    }
}