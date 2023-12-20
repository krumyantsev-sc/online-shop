package com.scand.bookshop.dto;

import com.scand.bookshop.entity.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TicketResponseDTO {

  private String username;
  private String lastMessage;
  private String title;
  private String timestamp;
  private String uuid;
  private Boolean isRead;
  private TicketStatus status;
}