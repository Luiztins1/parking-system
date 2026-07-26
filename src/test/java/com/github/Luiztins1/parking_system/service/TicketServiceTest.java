package com.github.Luiztins1.parking_system.service;

import com.github.Luiztins1.parking_system.model.entity.Ticket;
import com.github.Luiztins1.parking_system.model.enums.TicketStatus;
import com.github.Luiztins1.parking_system.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class TicketServiceTest {

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketRepository ticketRepository;

    Ticket ticketInit;

    @BeforeEach
    void setUp(){
        ticketRepository.deleteAll();
        ticketInit = ticketService.issueTicket();
    }

    @Test
    void shouldIssueTicket(){
        assertNotNull(ticketInit.getId());
        assertThat(ticketInit.getTicketNumber()).isNotEmpty();
        assertThat(ticketInit.getBarcodeSvg()).isNotEmpty();
        assertThat(ticketInit.getStatus()).isEqualTo(TicketStatus.ACTIVE);
    }

    @Test
    void shouldIssueTicketWhenParkingAvailable(){
        for(int i = 0; i < 200; i++){
            Ticket t = new Ticket();
            t.initTicketNumber();
            t.setStatus(TicketStatus.ACTIVE);
            ticketRepository.save(t);
        }

        assertThatThrownBy(() -> ticketService.issueTicket())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Estacionamento lotado! Não é possível emitir ticket.");
    }

    @Test
    void shouldSearchTicketActiveForPayment(){
        var ticketActive = ticketService.searchTicketActiveForPayment(ticketInit.getTicketNumber());

        assertNotNull(ticketActive.getId());
        assertThat(ticketActive.getTicketNumber()).isNotEmpty();
    }

    @Test
    void shouldTicketActiveWhenNotFound(){
        assertThatThrownBy(() -> ticketService.searchTicketActiveForPayment("123456789"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ticket não encontrado ou já finalizado.");
    }

    @Test
    void processExit(){
        ticketInit.setExitTime(LocalDateTime.now().plusHours(2));

        BigDecimal value = ticketService.paymentExit(ticketInit.getCheckInTime(), ticketInit.getExitTime());
        var ticketSearch = ticketService.processExit(ticketInit.getTicketNumber());

        assertNotNull(ticketSearch.getTicketNumber());
        assertNotNull(ticketSearch.getCheckInTime());
        assertNotNull(ticketSearch.getExitTime());
        assertThat(value).isGreaterThan(BigDecimal.ZERO);
        assertThat(ticketSearch.getStatus()).isEqualTo(TicketStatus.CLOSED);
    }

    @Test
    void shouldReturnZeroWhenExitWithinFiveMinutes() {
        ticketInit.setExitTime(ticketInit.getCheckInTime().plusMinutes(5));

        BigDecimal value = ticketService.paymentExit(ticketInit.getCheckInTime(), ticketInit.getExitTime());

        assertThat(value).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    void shouldChargeCorrectlyForMultipleHours() {
        ticketInit.setExitTime(ticketInit.getCheckInTime().plusMinutes(125));
        BigDecimal value = ticketService.paymentExit(ticketInit.getCheckInTime(), ticketInit.getExitTime());
        assertThat(value).isEqualTo(new BigDecimal("19.50"));
    }
}
