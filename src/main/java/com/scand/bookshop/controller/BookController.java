package com.scand.bookshop.controller;

import com.scand.bookshop.dto.BookRequestDTO;
import com.scand.bookshop.dto.BookResponseDTO;
import com.scand.bookshop.dto.PageResponseDTO;
import com.scand.bookshop.facade.BookFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public PageResponseDTO getAllBooks(@RequestParam(defaultValue = "id") String sortField,
                                       @RequestParam(defaultValue = "ASC") String sortDirection,
                                       @RequestParam int page,
                                       @RequestParam int size,
                                       @RequestParam(required = false) String searchTerm) {
        return bookFacade.getBooksPage(page, size, sortField, sortDirection, searchTerm);
    }

    @GetMapping("/{uuid}")
    public BookResponseDTO getBook(@PathVariable String uuid) {
        return bookFacade.getBook(uuid);
    }

    @GetMapping("/{uuid}/cover")
    public ResponseEntity<Resource> getBookCover(@PathVariable String uuid) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(bookFacade.getBookCover(uuid));
    }

    @GetMapping("/{uuid}/preview")
    public List<String> getPreview(@PathVariable String uuid) {
        return bookFacade.getPreviewImages(uuid);
    }

    @GetMapping("/{uuid}/download")
    public ResponseEntity<byte[]> downloadBook(@PathVariable String uuid) {
        return bookFacade.downloadBook(uuid);
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