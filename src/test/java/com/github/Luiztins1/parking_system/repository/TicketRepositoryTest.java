package com.github.Luiztins1.parking_system.repository;

import com.github.Luiztins1.parking_system.model.entity.Ticket;
import com.github.Luiztins1.parking_system.model.enums.TicketStatus;
import com.github.Luiztins1.parking_system.utils.BarcodeGeneratorUtil;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class TicketRepositoryTest {

    @Autowired
    TicketRepository ticketRepository;

    Ticket ticketInit;
    BarcodeGeneratorUtil barcodeGeneratorUtil;

    @BeforeEach
    void setUp(){
        ticketRepository.deleteAll();
        ticketInit = new Ticket();
        ticketInit.initTicketNumber();
        ticketRepository.save(ticketInit);
    }

    @Test
    void shouldSaveTicket(){
        assertNotNull(ticketInit.getId());
        assertEquals(TicketStatus.PENDENTE, ticketInit.getStatus());
    }

    @Test
    void shouldFindAllTicket(){
        var ticket = ticketInit;
        List<Ticket> ticketList = ticketRepository.findAll();

        assertNotNull(ticket.getId());
        assertThat(ticketList.isEmpty()).isFalse();
        assertThat(ticketList.contains(ticket)).isTrue();
        assertThat(ticketList).isNotEmpty();
    }

    @Test
    void shouldFindByIdTicket(){
        var ticket = ticketInit;

        var ticketId = ticketRepository.findById(ticket.getId());

        assertNotNull(ticket.getId());
        assertTrue(ticketId.isPresent());
    }

    @Test
    void shouldDeleteTicket(){
        var ticket = ticketInit;

        ticketRepository.delete(ticket);

        Optional<Ticket> ticketDelete = ticketRepository.findById(ticket.getId());

        assertNotNull(ticket.getId());
        assertThat(ticketDelete).isEmpty();
    }

    @Test
    void shouldFindByTicketNumberAndStatus(){
        var ticket = ticketInit;

        Optional<Ticket> ticketSearch = ticketRepository
                .findByTicketNumberAndStatus(ticket.getTicketNumber(),
                        ticket.getStatus());

        assertNotNull(ticket.getId());
        assertThat(ticketSearch).isNotEmpty();
        assertEquals(ticket.getTicketNumber(), ticketSearch.get().getTicketNumber());
        assertEquals(ticket.getStatus(), ticketSearch.get().getStatus());

    }

    @Test
    void shouldFindByTicketNumber(){
        var ticket = ticketInit;

        var ticketNumber = ticketRepository.findByTicketNumber(ticket.getTicketNumber());

        assertNotNull(ticket.getId());
        assertThat(ticketNumber).isNotNull();
        assertEquals(ticket.getTicketNumber(), ticketNumber.getTicketNumber());
    }

    @Test
    void shouldCountByStatus(){
        var ticket = ticketInit;

        long countStatus = ticketRepository.countByStatus(ticket.getStatus());

        assertNotNull(ticket.getId());
        assertThat(countStatus).isGreaterThan(0);
    }
}
