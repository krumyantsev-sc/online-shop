package com.scand.bookshop.controller;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.facade.BookFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("books")
public class BookController {

    private final BookFacade bookFacade;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BookResponseDTO uploadFile(@RequestParam("file") @NotNull MultipartFile file) {
        return bookFacade.uploadBook(file);
    }

    @GetMapping("/list")
    public List<BookResponseDTO> getAllBooks() {
        return bookFacade.getAllBooks();
    }

    @GetMapping("/{uuid}")
    public BookResponseDTO getBook(@PathVariable String uuid) {
        return bookFacade.getBook(uuid);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{uuid}")
    public void deleteBook(@PathVariable String uuid) {
        bookFacade.deleteBook(uuid);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{uuid}/update")
    public BookResponseDTO updateBook(@PathVariable String uuid, @RequestBody @Valid BookRequestDTO updatedBook) {
        return bookFacade.updateBook(uuid, updatedBook);
    }
}