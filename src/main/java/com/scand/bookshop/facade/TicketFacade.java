package com.scand.bookshop.facade;

import com.scand.bookshop.dto.DTOConverter;
import com.scand.bookshop.dto.MessageResponseDTO;
import com.scand.bookshop.dto.ReadTicketRequestDTO;
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
import org.springframework.security.core.parameters.P;
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

  public List<TicketResponseDTO> getAllChats() {
    return ticketService.findAllTickets().stream()
        .map(DTOConverter::toDTO)
        .collect(Collectors.toList());
  }

  public void read(ReadTicketRequestDTO readTicketRequestDTO) {
    Ticket ticket = ticketService.getTicketByUuid(readTicketRequestDTO.getUuid());
    ticketService.readTicket(ticket);
  }

  public List<MessageResponseDTO> getTicketMessages(ReadTicketRequestDTO readTicketRequestDTO) {
    Ticket ticket = ticketService.getTicketByUuid(readTicketRequestDTO.getUuid());
    return ticketService.getTicketMessages(ticket).stream()
        .map(DTOConverter::toDTO)
        .collect(Collectors.toList());
  }
}
