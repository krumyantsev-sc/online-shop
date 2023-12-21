package com.scand.bookshop.facade;

import com.scand.bookshop.dto.CreateMessageRequestDTO;
import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.dto.MessageResponseDTO;
import com.scand.bookshop.dto.TicketUuidRequestDTO;
import com.scand.bookshop.dto.TicketRequestDTO;
import com.scand.bookshop.dto.TicketResponseDTO;
import com.scand.bookshop.entity.Ticket;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.security.service.UserDetailsImpl;
import com.scand.bookshop.service.TicketService;
import com.scand.bookshop.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketFacade {

  private final TicketService ticketService;
  private final UserService userService;

  public void createTicket(UserDetailsImpl userDetails, TicketRequestDTO ticketRequestDTO) {
    User user = userService.getUserById(userDetails.getId());
    ticketService.createTicket(user, ticketRequestDTO.getTitle(), ticketRequestDTO.getMessage());
  }

  public List<TicketResponseDTO> getAllChats(UserDetailsImpl userDetails) {
    User user = userService.getUserById(userDetails.getId());
    return ticketService.findAllTickets().stream()
        .map(ticket -> DTOConverter.toDTO(ticket, user))
        .collect(Collectors.toList());
  }

  public List<TicketResponseDTO> getUserChats(UserDetailsImpl userDetails) {
    User user = userService.getUserById(userDetails.getId());
    return ticketService.findTicketsByUser(user).stream()
        .map(ticket -> DTOConverter.toDTO(ticket, user))
        .collect(Collectors.toList());
  }

  public void read(TicketUuidRequestDTO ticketUuidRequestDTO, UserDetailsImpl userDetails) {
    User user = userService.getUserById(userDetails.getId());
    Ticket ticket = ticketService.getTicketByUuid(ticketUuidRequestDTO.getUuid());
    ticketService.readTicket(ticket, user);
  }

  public List<MessageResponseDTO> getTicketMessages(TicketUuidRequestDTO ticketUuidRequestDTO) {
    Ticket ticket = ticketService.getTicketByUuid(ticketUuidRequestDTO.getUuid());
    return ticketService.getTicketMessages(ticket).stream()
        .map(DTOConverter::toDTO)
        .collect(Collectors.toList());
  }

  public void closeTicket(TicketUuidRequestDTO ticketUuidRequestDTO) {
    Ticket ticket = ticketService.getTicketByUuid(ticketUuidRequestDTO.getUuid());
    ticketService.closeTicket(ticket);
  }

  public void createMessage(CreateMessageRequestDTO createMessageRequestDTO,
                            UserDetailsImpl userDetails) {
    ticketService.createMessage(ticketService.getTicketByUuid(createMessageRequestDTO.getUuid()),
        createMessageRequestDTO.getMessage(),
        userService.getUserById(userDetails.getId()));
  }
}
