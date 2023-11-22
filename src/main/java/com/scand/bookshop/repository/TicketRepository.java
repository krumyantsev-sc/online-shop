package com.scand.bookshop.repository;

import com.scand.bookshop.entity.Ticket;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

  Optional<Ticket> findByUuid(String uuid);
}
