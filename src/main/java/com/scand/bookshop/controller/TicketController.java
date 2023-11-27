package com.scand.bookshop.controller;

import com.scand.bookshop.dto.CreateMessageRequestDTO;
import com.scand.bookshop.dto.MessageResponseDTO;
import com.scand.bookshop.dto.TicketUuidRequestDTO;
import com.scand.bookshop.dto.TicketRequestDTO;
import com.scand.bookshop.dto.TicketResponseDTO;
import com.scand.bookshop.facade.TicketFacade;
import com.scand.bookshop.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

  private final TicketFacade ticketFacade;

  @PostMapping("/create")
  public void createTicket(@AuthenticationPrincipal UserDetailsImpl userPrincipal,
                           @Valid @RequestBody TicketRequestDTO ticketRequestDTO) {
    ticketFacade.createTicket(userPrincipal, ticketRequestDTO);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @GetMapping("/list")
  public List<TicketResponseDTO> getAllChats() {
    return ticketFacade.getAllChats();
  }

  @GetMapping("/all")
  public List<TicketResponseDTO> getUserChats(
      @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
    return ticketFacade.getUserChats(userPrincipal);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/close")
  public void closeChat(
      @RequestBody @Valid TicketUuidRequestDTO ticketUuidRequestDTO) {
    ticketFacade.closeTicket(ticketUuidRequestDTO);
  }

  @PostMapping("/read")
  public void read(@RequestBody @Valid TicketUuidRequestDTO ticketUuidRequestDTO) {
    ticketFacade.read(ticketUuidRequestDTO);
  }

  @PostMapping("/messages")
  public List<MessageResponseDTO> getTicketMessages(
      @RequestBody TicketUuidRequestDTO ticketUuidRequestDTO) {
    return ticketFacade.getTicketMessages(ticketUuidRequestDTO);
  }

  @PostMapping("/messages/send")
  public void createMessage(@RequestBody @Valid CreateMessageRequestDTO createMessageRequestDTO,
                            @AuthenticationPrincipal UserDetailsImpl userPrincipal) {
    ticketFacade.createMessage(createMessageRequestDTO, userPrincipal);
  }
}
