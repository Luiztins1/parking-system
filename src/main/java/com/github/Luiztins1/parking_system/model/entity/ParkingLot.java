package com.github.Luiztins1.parking_system.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "parking_lot")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "total_spots", nullable = false)
    private Integer totalSpots = 200;

    @Column(name = "occupied_spots", nullable = false)
    private Integer occupiedSpots = 0;

    public Integer getAvailableSpots(){
        return this.totalSpots - this.occupiedSpots;
    }

}
