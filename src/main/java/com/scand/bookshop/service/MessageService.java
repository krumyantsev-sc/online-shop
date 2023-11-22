package com.scand.bookshop.service;

import com.scand.bookshop.entity.Message;
import com.scand.bookshop.entity.Ticket;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.MessageRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final MessageRepository messageRepository;

  @Transactional
  public Message createMessage(User user, String messageText, Ticket ticket) {
    Message message = new Message(null,messageText, ticket, user, LocalDateTime.now());
    return messageRepository.save(message);
  }
}
