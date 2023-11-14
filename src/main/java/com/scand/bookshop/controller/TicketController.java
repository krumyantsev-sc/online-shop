package com.scand.bookshop.controller;

import com.scand.bookshop.facade.TicketFacade;
import com.scand.bookshop.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {
  private final TicketFacade ticketFacade;

  @GetMapping("/create")
  public void createTicket(@AuthenticationPrincipal UserDetailsImpl userPrincipal) {
    ticketFacade.createTicket();
  }
}
