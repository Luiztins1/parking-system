package com.github.Luiztins1.parking_system.controller.dto;

import com.github.Luiztins1.parking_system.model.enums.TypeBrand;

import java.util.UUID;

public record CarDTO(
        UUID id,
        String plate,
        TypeBrand brand,
        String model,
        UUID person) {
}
