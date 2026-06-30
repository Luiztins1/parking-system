package com.github.Luiztins1.parking_system.model.mapper;

import com.github.Luiztins1.parking_system.controller.dto.CarDTO;
import com.github.Luiztins1.parking_system.model.entity.Car;
import com.github.Luiztins1.parking_system.model.entity.Person;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class CarMapper {

    public static CarDTO toDto(Car car){
        if(car == null) return null;


        return new CarDTO(
                car.getId(),
                car.getPlate(),
                car.getBrand(),
                car.getModel(),
                //Provisório
                car.getPerson().getId() != null ? car.getPerson().getId() : null
        );
    }

    public static Car toEntity(CarDTO carDTO){
        if(carDTO == null) return null;

        Car car = new Car();
        Person person = car.getPerson();

        car.setId(carDTO.id());
        car.setPlate(carDTO.plate());
        car.setBrand(carDTO.brand());
        car.setModel(carDTO.model());
        car.setPerson(person);


        return car;
    }
}
