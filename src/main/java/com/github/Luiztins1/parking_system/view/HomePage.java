package com.github.Luiztins1.parking_system.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePage {
    @GetMapping("/")
    public String redirectToTicket() {
        return "redirect:/ticket/new";
    }
}
