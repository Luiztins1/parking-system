package com.github.Luiztins1.parking_system.controller.rest;

import com.github.Luiztins1.parking_system.controller.dto.TicketDTO;
import com.github.Luiztins1.parking_system.model.entity.Ticket;
import com.github.Luiztins1.parking_system.model.mapper.TicketMapper;
import com.github.Luiztins1.parking_system.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ap/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDTO> issueTicket(){
        var ticket = ticketService.issueTicket();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ticket.getId())
                .toUri();

        return ResponseEntity.created(location).body(TicketMapper.toDto(ticket));
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> findAll(){
        List<TicketDTO> ticketList = ticketService.findAllTickets()
                .stream()
                .map(TicketMapper::toDto)
                .toList();

        if(ticketList.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(ticketList);
    }

    @GetMapping("/{ticketNumber}")
    public ResponseEntity<TicketDTO> searchTicketActiveForPayment(@PathVariable String ticketNumber){
        var ticket = ticketService.searchTicketActiveForPayment(ticketNumber);
        var ticketDTO = TicketMapper.toDto(ticket);

        return ResponseEntity.ok(ticketDTO);
    }

    @PutMapping("/{ticketNumber}/checkout")
    public ResponseEntity<TicketDTO> processExit(@PathVariable String ticketNumber){
        var ticket = ticketService.processExit(ticketNumber);
        var ticketDTO = TicketMapper.toDto(ticket);

        return ResponseEntity.ok(ticketDTO);
    }

}
