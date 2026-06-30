package com.github.Luiztins1.parking_system.controller.dto;

import java.util.UUID;

public record PersonDTO(
        UUID id,
        String name,
        String cpf,
        UUID carList) {
}
