package com.github.Luiztins1.parking_system.service;

import com.github.Luiztins1.parking_system.model.entity.Ticket;
import com.github.Luiztins1.parking_system.model.enums.TicketStatus;
import com.github.Luiztins1.parking_system.repository.TicketRepository;
import com.github.Luiztins1.parking_system.utils.BarcodeGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final BarcodeGeneratorUtil barcodeGeneratorUtil;

    @Transactional
    public Ticket issueTicket(){

        long activeTickets = ticketRepository.countByStatus(TicketStatus.PENDENTE);
        long parkingLot = 200;

        if(activeTickets >= parkingLot) throw new RuntimeException("Estacionamento lotado! Não é possível emitir ticket.");

        var ticket = new Ticket();
        ticket.initTicketNumber();

        String barcodeSvg = barcodeGeneratorUtil.generateTicketBarcode(ticket.getTicketNumber());
        ticket.setBarcodeSvg(barcodeSvg);

        return ticketRepository.save(ticket);
    };

    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
    }

    public Ticket searchTicketActiveForPayment(String ticketNumber){
        return ticketRepository.findByTicketNumberAndStatus(ticketNumber, TicketStatus.PENDENTE)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado ou já finalizado."));
    }

    @Transactional
    public Ticket processExit(String ticketNumber){
        Ticket ticket = searchTicketActiveForPayment(ticketNumber);
        ticket.setExitTime(LocalDateTime.now());

        BigDecimal payment = paymentExit(ticket.getCheckInTime(), ticket.getExitTime());
        ticket.setValue(payment);  

        ticket.setStatus(TicketStatus.PAGO);

        return ticketRepository.save(ticket);
    }

    public BigDecimal paymentExit(LocalDateTime checkInTime, LocalDateTime exitTime){
        Duration timeDifference = Duration.between(checkInTime, exitTime);
        long timeInMinutes = timeDifference.toMinutes();

        if(timeInMinutes <= 5) return BigDecimal.ZERO;

        double hours = Math.ceil(timeInMinutes/60.0);

        BigDecimal valuePerHour = new BigDecimal("6.50");

        return valuePerHour.multiply(BigDecimal.valueOf(hours)).setScale(2, RoundingMode.HALF_UP);
    }
}
