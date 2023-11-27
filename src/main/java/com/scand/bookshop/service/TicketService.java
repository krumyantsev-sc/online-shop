package com.scand.bookshop.service;

import com.scand.bookshop.entity.Message;
import com.scand.bookshop.entity.Ticket;
import com.scand.bookshop.entity.TicketStatus;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.TicketRepository;
import com.scand.bookshop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

  private final TicketRepository ticketRepository;
  private final UserRepository userRepository;
  private final MessageService messageService;
  private final MessageSource messageSource;
  private final HttpServletRequest request;

  @Transactional
  public void createTicket(User user, String title, String messageText) {
    Ticket ticket = new Ticket(
        null,
        user,
        title,
        new ArrayList<Message>(),
        true,
        TicketStatus.OPEN,
        UUID.randomUUID().toString());
    ticketRepository.save(ticket);
    Message message = messageService.createMessage(user, messageText, ticket);
    ticket.getMessages().add(message);
  }

  public Optional<Ticket> findTicketByUuid(String uuid) {
    return ticketRepository.findByUuid(uuid);
  }

  public List<Ticket> findTicketsByUser(User user) {
    return ticketRepository.findByUser(user);
  }

  public Ticket getTicketByUuid(String uuid) {
    return findTicketByUuid(uuid)
        .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
            "book_not_found", null, request.getLocale())));
  }

  @Transactional
  public void readTicket(Ticket ticket) {
    ticket = ticketRepository.getReferenceById(ticket.getTicketId());
    ticket.setIsRead(true);
  }

  @Transactional
  public void closeTicket(Ticket ticket) {
    ticket = ticketRepository.getReferenceById(ticket.getTicketId());
    ticket.setStatus(TicketStatus.CLOSED);
  }

  public List<Message> getTicketMessages(Ticket ticket) {
    return ticket.getMessages();
  }

  public List<Ticket> findAllTickets() {
    return ticketRepository.findAll();
  }

  @Transactional
  public void createMessage(Ticket ticket, String content, User user) {
    ticket = ticketRepository.getReferenceById(ticket.getTicketId());
    user = userRepository.getReferenceById(user.getId());
    messageService.createMessage(user, content, ticket);
  }
}
