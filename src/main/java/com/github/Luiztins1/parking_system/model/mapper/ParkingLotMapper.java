package com.github.Luiztins1.parking_system.model.mapper;

import com.github.Luiztins1.parking_system.controller.dto.ParkingLotDTO;
import com.github.Luiztins1.parking_system.model.entity.ParkingLot;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ParkingLotMapper {

    public static ParkingLotDTO toDto(ParkingLot parkingLot){
        if(parkingLot == null) return null;

        return new ParkingLotDTO(
                parkingLot.getId(),
                parkingLot.getTotalSpots(),
                parkingLot.getOccupiedSpots()
        );
    }

    public static ParkingLot toEntity(ParkingLotDTO parkingLotDTO){
        if(parkingLotDTO == null) return null;

        ParkingLot parkingLot = new ParkingLot();

        parkingLot.setId(parkingLotDTO.id());
        parkingLot.setTotalSpots(parkingLotDTO.totalSpots());
        parkingLot.setOccupiedSpots(parkingLotDTO.occupiedSpots());


        return parkingLot;
    }
}
