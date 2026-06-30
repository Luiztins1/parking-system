package com.github.Luiztins1.parking_system.model.entity;

import com.github.Luiztins1.parking_system.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "ticket_number", unique = true, length = 8)
    private String ticketNumber;

    @Column(name = "ticket_status")
    private TicketStatus status = TicketStatus.ACTIVE;

    @PrePersist
    public void randomTicketNumber(){
        this.checkInTime = LocalDateTime.now();

        Random random = new Random();
        int number = 10000000 + random.nextInt(900000000);
        this.ticketNumber = String.valueOf(number);
    }
}
