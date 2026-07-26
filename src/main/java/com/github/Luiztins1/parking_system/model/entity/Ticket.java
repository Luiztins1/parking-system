package com.github.Luiztins1.parking_system.model.entity;

import com.github.Luiztins1.parking_system.model.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "ticket")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Ticket extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ticket_value")
    private BigDecimal value;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    @Column(name = "ticket_number", unique = true, length = 9)
    private String ticketNumber;

    @Column(name = "barcodeSvg", columnDefinition = "TEXT")
    private String barcodeSvg;

    @Column(name = "ticket_status")
    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.PENDENTE;

    public void initTicketNumber(){
        this.checkInTime = LocalDateTime.now();

        Random random = new Random();
        int number = 100000000 + random.nextInt(900000000);
        this.ticketNumber = String.valueOf(number);
    }
}
