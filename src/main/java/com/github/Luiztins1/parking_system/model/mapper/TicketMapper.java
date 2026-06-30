package com.github.Luiztins1.parking_system.model.mapper;

import com.github.Luiztins1.parking_system.controller.dto.TicketDTO;
import com.github.Luiztins1.parking_system.model.entity.Ticket;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TicketMapper {

    public static TicketDTO toDto(Ticket ticket){
        if(ticket == null) return null;

        return new TicketDTO(
                ticket.getId(),
                ticket.getValue(),
                ticket.getCheckInTime(),
                ticket.getExitTime(),
                ticket.getTicketNumber(),
                ticket.getStatus()
        );
    }

    public static Ticket toEntity(TicketDTO ticketDTO){
        if(ticketDTO == null) return null;

        Ticket ticket = new Ticket();

        ticket.setId(ticketDTO.id());
        ticket.setValue(ticketDTO.value());
        ticket.setCheckInTime(ticketDTO.checkInTime());
        ticket.setExitTime(ticketDTO.exitTime());
        ticket.setTicketNumber(ticketDTO.ticketNumber());
        ticket.setStatus(ticketDTO.status());

        return ticket;
    }
}
