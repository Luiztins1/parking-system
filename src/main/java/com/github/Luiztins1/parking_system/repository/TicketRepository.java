package com.github.Luiztins1.parking_system.repository;

import com.github.Luiztins1.parking_system.model.entity.Ticket;
import com.github.Luiztins1.parking_system.model.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Optional<Ticket> findByTicketNumberAndStatus(String ticketNumber, TicketStatus status);
    Ticket findByTicketNumber(String ticketNumber);
    long countByStatus(TicketStatus status);
}
