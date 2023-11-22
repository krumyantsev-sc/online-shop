package com.scand.bookshop.controller;

import com.scand.bookshop.dto.MessageResponseDTO;
import com.scand.bookshop.dto.ReadTicketRequestDTO;
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

  @PostMapping("/read")
  public void read(@RequestBody ReadTicketRequestDTO readTicketRequestDTO) {
    ticketFacade.read(readTicketRequestDTO);
  }

  @PostMapping("/messages")
  public List<MessageResponseDTO> getTicketMessages(@RequestBody ReadTicketRequestDTO readTicketRequestDTO) {
    return ticketFacade.getTicketMessages(readTicketRequestDTO);
  }
}
