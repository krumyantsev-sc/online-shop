package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Ticket;
import com.scand.bookshop.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

  Optional<Ticket> findByUuid(String uuid);

  List<Ticket> findByUser(User user);
}
