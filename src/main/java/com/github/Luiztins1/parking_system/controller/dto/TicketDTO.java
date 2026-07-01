package com.github.Luiztins1.parking_system.controller.dto;

import com.github.Luiztins1.parking_system.model.enums.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TicketDTO(
        UUID id,
        BigDecimal value,
        LocalDateTime checkInTime,
        LocalDateTime exitTime,
        String ticketNumber,
        String barcodeSvg,
        TicketStatus status) {
}
