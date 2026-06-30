package com.github.Luiztins1.parking_system.repository;

import com.github.Luiztins1.parking_system.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
