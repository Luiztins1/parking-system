package com.github.Luiztins1.parking_system.model.entity;

import com.github.Luiztins1.parking_system.model.enums.TypeBrand;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "car")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "plate", nullable = false)
    private String plate;

    @Column(name = "brand", nullable = false)
    private TypeBrand brand;

    @Column(name = "model", nullable = false)
    private String model;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
