package com.github.Luiztins1.parking_system.service;

import com.github.Luiztins1.parking_system.controller.dto.TicketDTO;
import com.github.Luiztins1.parking_system.model.entity.Ticket;
import com.github.Luiztins1.parking_system.model.enums.TicketStatus;
import com.github.Luiztins1.parking_system.model.mapper.TicketMapper;
import com.github.Luiztins1.parking_system.repository.TicketRepository;
import com.github.Luiztins1.parking_system.utils.BarcodeGeneratorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TicketServiceTest {

    @InjectMocks
    TicketService ticketService;

    @Mock
    TicketRepository ticketRepository;

    @Mock
    BarcodeGeneratorUtil barcodeGeneratorUtil;

    TicketDTO dto;
    Ticket ticketInit;

    @BeforeEach
    void setUp(){
        ticketRepository.deleteAll();
        dto = new TicketDTO(
                UUID.randomUUID(),
                null,
                LocalDateTime.now(),
                null,
                "123456789",
                "123456789",
                TicketStatus.PENDENTE);

        ticketInit = TicketMapper.toEntity(dto);
    }

    @Test
    void shouldIssueTicket(){

        Mockito.when(ticketRepository.countByStatus(TicketStatus.PENDENTE))
                        .thenReturn(0L);

        Mockito.when(barcodeGeneratorUtil.generateTicketBarcode(Mockito.anyString()))
                        .thenReturn("fakeBarcode");

        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class)))
                        .thenAnswer(invocation ->{
                            Ticket saved = invocation.getArgument(0);
                            saved.setId(UUID.randomUUID());
                            return saved;
                        });
        var result = ticketService.issueTicket();

        assertThat(result.getTicketNumber()).isNotEmpty();
        assertThat(result.getBarcodeSvg()).isEqualTo("fakeBarcode");
        assertThat(result.getStatus()).isEqualTo(TicketStatus.PENDENTE);

        Mockito.verify(ticketRepository, Mockito.times(1))
                .save(Mockito.any(Ticket.class));
    }

    @Test
    void shouldIssueTicketWhenParkingAvailable(){

        Mockito.when(ticketRepository.countByStatus(TicketStatus.PENDENTE))
                        .thenReturn(200L);

        assertThatThrownBy(() -> ticketService.issueTicket())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Estacionamento lotado! Não é possível emitir ticket.");

        Mockito.verify(ticketRepository, Mockito.times(1))
                .countByStatus(Mockito.any(TicketStatus.class));
    }

    @Test
    void shouldSearchTicketActiveForPayment(){

        Mockito.when(ticketRepository.findByTicketNumberAndStatus(Mockito.anyString(),
                        Mockito.any(TicketStatus.class)))
                .thenReturn(Optional.of(ticketInit));

        var ticketActive = ticketService.searchTicketActiveForPayment(ticketInit.getTicketNumber());

        assertNotNull(ticketActive.getId());
        assertThat(ticketActive.getTicketNumber()).isNotEmpty();

        Mockito.verify(ticketRepository, Mockito.times(1))
                .findByTicketNumberAndStatus(Mockito.anyString(), Mockito.any(TicketStatus.class));
    }

    @Test
    void shouldTicketActiveWhenNotFound(){
        assertThatThrownBy(() -> ticketService.searchTicketActiveForPayment(ticketInit.getTicketNumber()))
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
        assertThat(ticketSearch.getStatus()).isEqualTo(TicketStatus.PAGO);
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
