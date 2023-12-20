package com.scand.bookshop.dto;

import com.scand.bookshop.entity.*;

import com.scand.bookshop.entity.User.Role;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class DTOConverter {

  private static final DateTimeFormatter messageDateFormatter = DateTimeFormatter.ofPattern(
          "dd MMM HH:mm")
      .withLocale(Locale.ENGLISH);
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(
      "dd-MM-yyyy HH:mm:ss");

  public static BookResponseDTO toDTO(Book book, Boolean isPaid) {
    BookResponseDTO responseDTO = new BookResponseDTO();
    responseDTO.setTitle(book.getTitle());
    responseDTO.setGenre(book.getGenre());
    responseDTO.setAuthor(book.getAuthor());
    responseDTO.setUuid(book.getUuid());
    responseDTO.setDescription(book.getDescription());
    responseDTO.setPrice(book.getPrice());
    responseDTO.setIsPaid(isPaid);
    return responseDTO;
  }

  public static AdminPanelUserResponseDTO toDTO(User user) {
    return new AdminPanelUserResponseDTO(
        user.getUuid().toString(),
        user.getLogin(),
        user.getEmail(),
        user.getRegistrationDate().format(dateFormatter),
        user.getRole()
    );
  }

  public static UserResponseDTO toUserDTO(User user) {
    return new UserResponseDTO(user.getLogin(),
        user.getEmail(),
        user.getRegistrationDate().toLocalDate().toString());
  }

  public static CommentResponseDTO toDTO(Comment comment) {
    CommentResponseDTO responseDTO;
    if (!comment.isRemoved()) {
      responseDTO = new CommentResponseDTO();
      responseDTO.setText(comment.getText());
      responseDTO.setUsername(comment.getUser().getLogin());
      responseDTO.setTimestamp(comment.getTimestamp()
          .format(dateFormatter));
      responseDTO.setUuid(comment.getUuid());
      responseDTO.setReplies(comment.getReplies()
          .stream()
          .map(DTOConverter::toDTO)
          .collect(Collectors.toList()));
      responseDTO.setRemoved(comment.isRemoved());
    } else {
      responseDTO = new CommentResponseDTO(
          null,
          null,
          null,
          null,
          null,
          comment.isRemoved());
      responseDTO.setReplies(comment.getReplies()
          .stream()
          .map(DTOConverter::toDTO)
          .collect(Collectors.toList()));
    }
    return responseDTO;
  }

  private static OrderDetailResponseDTO toDTO(OrderDetail orderDetail) {
    return new OrderDetailResponseDTO(orderDetail.getBook().getUuid(), orderDetail.getUnitPrice());
  }

  public static OrderResponseDTO toDTO(Order order) {
    return new OrderResponseDTO(order.getUuid(),
        order.getUser().getLogin(),
        order.getOrderDate().format(dateFormatter),
        order.getTotalPrice(),
        order.getStatus().toString(),
        order.getOrderDetails().stream()
            .map(DTOConverter::toDTO)
            .collect(Collectors.toList()));
  }

  public static CreateOrderResponseDTO toCreateDTO(Order order) {
    return new CreateOrderResponseDTO(order.getUuid());
  }

  public static CartBookResponseDTO toDTO(CartItem cartItem) {
    CartBookResponseDTO cartBookResponseDTO = new CartBookResponseDTO();
    cartBookResponseDTO.setPrice(cartItem.getBook().getPrice());
    cartBookResponseDTO.setUuid(cartItem.getBook().getUuid());
    return cartBookResponseDTO;
  }

  public static TicketResponseDTO toDTO(Ticket ticket, User user) {
    TicketResponseDTO ticketResponseDTO = new TicketResponseDTO();
    ticketResponseDTO.setUsername(ticket.getUser().getLogin());
    ticketResponseDTO.setTitle(ticket.getTitle());
    Message lastMessage = ticket.getMessages()
        .get(ticket.getMessages().size() - 1);
    ticketResponseDTO.setLastMessage(lastMessage.getContent());
    ticketResponseDTO.setIsRead(
        user.getRole().equals(Role.USER) ? ticket.getIsReadByUser() : ticket.getIsReadByAdmin());
    ticketResponseDTO.setTimestamp(lastMessage.getTimestamp().format(messageDateFormatter));
    ticketResponseDTO.setUuid(ticket.getUuid());
    ticketResponseDTO.setStatus(ticket.getStatus());
    return ticketResponseDTO;
  }

  public static MessageResponseDTO toDTO(Message message) {
    MessageResponseDTO messageResponseDTO = new MessageResponseDTO();
    messageResponseDTO.setContent(message.getContent());
    messageResponseDTO.setUsername(message.getUser().getLogin());
    messageResponseDTO.setTimestamp(message.getTimestamp().format(messageDateFormatter));
    return messageResponseDTO;
  }

  public static RatingResponseDTO toDTO(double rating) {
    return new RatingResponseDTO(rating);
  }
}
