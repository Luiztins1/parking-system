package com.github.Luiztins1.parking_system.controller.dto;

import java.util.UUID;

public record ParkingLotDTO(
        UUID id,
        Integer totalSpots,
        Integer occupiedSpots) {
}
