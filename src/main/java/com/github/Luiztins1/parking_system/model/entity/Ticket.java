package com.github.Luiztins1.parking_system.model.entity;

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

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    @Column(name = "exit_time", nullable = false)
    private LocalDateTime exitTime;

    @Column(name = "ticket_number", nullable = false, unique = true, length = 8)
    private String ticketNumber;

    @Column(name = "person", nullable = false)
    private Person person;

    @PrePersist
    public void randomTicketNumber(){
        Random random = new Random();
        int number = 10000000 + random.nextInt(900000000);
        this.ticketNumber = String.valueOf(number);
    }
}
