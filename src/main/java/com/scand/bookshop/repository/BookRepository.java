package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByUuid(String uuid);
}
