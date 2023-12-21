package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
