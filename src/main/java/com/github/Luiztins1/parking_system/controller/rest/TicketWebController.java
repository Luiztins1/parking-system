package com.github.Luiztins1.parking_system.controller.rest;

import com.github.Luiztins1.parking_system.model.entity.Ticket;
import com.github.Luiztins1.parking_system.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TicketWebController {

    private final TicketService ticketService;

    @GetMapping("/tickets/novo")
    @Transactional
    public String generatedTicket(Model model){
        Ticket ticket = ticketService.issueTicket();
        popularModel(model, ticket);
        return "ticket";
    }

    @PostMapping("/tickets/enter")
    @Transactional
    public String enter(Model model){
        Ticket ticket = ticketService.issueTicket();
        popularModel(model, ticket);
        return "ticket";
    }

    @PostMapping("/tickets/exit")
    @Transactional
    public String exit(@RequestParam String ticketNumber, Model model){
        Ticket ticket = ticketService.processExit(ticketNumber);
        popularModel(model, ticket);
        return "ticket";
    }

    private void popularModel(Model model, Ticket ticket){
        model.addAttribute("ticketNumber", ticket.getTicketNumber());
        model.addAttribute("checkInTime", ticket.getCheckInTime());
        model.addAttribute("exitTime", ticket.getExitTime());
        model.addAttribute("value", ticket.getValue());
        model.addAttribute("status", ticket.getStatus());
        model.addAttribute("barcodeSvg", ticket.getBarcodeSvg());
    }
}
