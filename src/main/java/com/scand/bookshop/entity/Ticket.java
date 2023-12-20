package com.scand.bookshop.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ticket_id")
  private Long ticketId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "title", nullable = false)
  private String title;

  @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Message> messages;

  @Column(name = "is_read_by_user", nullable = false)
  private Boolean isReadByUser;

  @Column(name = "is_read_by_admin", nullable = false)
  private Boolean isReadByAdmin;

  @Column(name = "status", nullable = false)
  private TicketStatus status;

  @Column(name = "uuid", nullable = false)
  private String uuid;

}