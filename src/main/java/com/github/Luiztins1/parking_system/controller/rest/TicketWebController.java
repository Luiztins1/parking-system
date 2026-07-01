package com.github.Luiztins1.parking_system.controller.rest;

import com.github.Luiztins1.parking_system.model.entity.Ticket;
import com.github.Luiztins1.parking_system.repository.TicketRepository;
import com.github.Luiztins1.parking_system.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class TicketWebController {

    private final TicketRepository ticketRepository;

    @GetMapping("/tickets/{ticketNumber}")
    @Transactional(readOnly = true)
    public String viewTicket(@PathVariable String ticketNumber, Model model){
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber);

        model.addAttribute("ticketNumber", ticketNumber);
        model.addAttribute("checkInTime", ticket.getCheckInTime());
        model.addAttribute("value", ticket.getValue());
        model.addAttribute("status", ticket.getStatus());

        String ticketBarcodeSvg = ticket.getBarcodeSvg();
        String barcodeSvgFormat = String.format("<svg>%s</svg>",ticketBarcodeSvg);

        model.addAttribute("barcodeSvg", barcodeSvgFormat);

        return "ticket";
    }

}
