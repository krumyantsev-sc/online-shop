package com.scand.bookshop.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.scand.bookshop.entity.Message;
import com.scand.bookshop.entity.Ticket;
import com.scand.bookshop.entity.TicketStatus;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.entity.User.Role;
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

  private static final String ADMINS_ROOM_NAME = "admins";
  private static final String NEW_MESSAGE_EVENT = "newMessage";

  private final TicketRepository ticketRepository;
  private final UserRepository userRepository;
  private final MessageService messageService;
  private final MessageSource messageSource;
  private final HttpServletRequest request;
  private final SocketIOServer server;

  @Transactional
  public void createTicket(User user, String title, String messageText) {
    Ticket ticket = new Ticket(
        null,
        user,
        title,
        new ArrayList<Message>(),
        user.getRole().equals(Role.USER),
        user.getRole().equals(Role.ADMIN),
        TicketStatus.OPEN,
        UUID.randomUUID().toString());
    ticketRepository.save(ticket);
    Message message = messageService.createMessage(user, messageText, ticket);
    ticket.getMessages().add(message);
    server.getRoomOperations(ADMINS_ROOM_NAME).sendEvent(NEW_MESSAGE_EVENT);
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
  public void readTicket(Ticket ticket, User user) {
    ticket = ticketRepository.getReferenceById(ticket.getTicketId());
    if (user.getRole().equals(Role.USER)) {
      ticket.setIsReadByUser(true);
    } else {
      ticket.setIsReadByAdmin(true);
    }
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
    if (ticket.getStatus().equals(TicketStatus.OPEN)) {
      ticket = ticketRepository.getReferenceById(ticket.getTicketId());
      user = userRepository.getReferenceById(user.getId());
      messageService.createMessage(user, content, ticket);
      if (user.getRole().equals(Role.USER)) {
        ticket.setIsReadByAdmin(false);
        ticket.setIsReadByUser(true);
        server.getRoomOperations(ADMINS_ROOM_NAME).sendEvent(NEW_MESSAGE_EVENT);
      } else {
        ticket.setIsReadByUser(false);
        ticket.setIsReadByAdmin(true);
        server.getRoomOperations(user.getUuid().toString()).sendEvent(NEW_MESSAGE_EVENT);
      }
    } else {
      throw new RuntimeException("Ticket is already closed");
    }
  }
}
