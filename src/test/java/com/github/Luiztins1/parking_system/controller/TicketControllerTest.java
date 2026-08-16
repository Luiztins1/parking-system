package com.github.Luiztins1.parking_system.controller;

import com.github.Luiztins1.parking_system.controller.dto.TicketDTO;
import com.github.Luiztins1.parking_system.controller.rest.TicketController;
import com.github.Luiztins1.parking_system.model.entity.Ticket;
import com.github.Luiztins1.parking_system.model.enums.TicketStatus;
import com.github.Luiztins1.parking_system.model.mapper.TicketMapper;
import com.github.Luiztins1.parking_system.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class TicketControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TicketService ticketService;

    Ticket ticketInit;
    TicketDTO expected;

    @BeforeEach
    void setUp(){
       ticketInit = new Ticket(
               UUID.randomUUID(),
               null,
               LocalDateTime.now(),
               null,
               "123456789",
               "123456789",
               TicketStatus.PENDENTE);

       expected = TicketMapper.toDto(ticketInit);
    }

    @Test
    void shouldIssueTicket() throws Exception{

       when(ticketService.issueTicket()).thenReturn(ticketInit);



        mvc.perform(post("/api/v1/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketNumber").value(expected.ticketNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(expected.status().toString().trim()));
    }

    @Test
    void shouldFindAll() throws Exception{
        when(ticketService.findAllTickets()).thenReturn(List.of(ticketInit));

        mvc.perform(get("/api/v1/tickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldSearchTicketActiveForPayment() throws Exception{
        when(ticketService.searchTicketActiveForPayment(ticketInit.getTicketNumber()))
                .thenReturn(ticketInit);

        mvc.perform(get("/api/v1/tickets/{ticketNumber}", ticketInit.getTicketNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketNumber").value(expected.ticketNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(expected.status().toString().trim()));
    }

    @Test
    void shouldProcessExit() throws Exception{
        when(ticketService.processExit(ticketInit.getTicketNumber()))
                .thenReturn(ticketInit);

        mvc.perform(put("/api/v1/tickets/{ticketNumber}/checkout", ticketInit.getTicketNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketNumber").value(expected.ticketNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(expected.status().toString().trim()));
    }
}
