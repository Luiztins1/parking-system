package com.github.Luiztins1.parking_system.model.mapper;

import com.github.Luiztins1.parking_system.controller.dto.PersonDTO;
import com.github.Luiztins1.parking_system.model.entity.Car;
import com.github.Luiztins1.parking_system.model.entity.Person;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class PersonMapper {

    public static PersonDTO toDto (Person person){
        if(person == null) return null;

        List<UUID> carList = person.getCarList()
                .stream()
                .map(Car::getId)
                .toList();

        return new PersonDTO(
                person.getId(),
                person.getName(),
                person.getCpf(),
                carList
        );
    }

    public static Person toEntity(PersonDTO personDTO){
        if(personDTO == null) return null;

        Person person = new Person();
        List<Car> carList = person.getCarList();

        person.setId(personDTO.id());
        person.setCpf(personDTO.cpf());
        person.setCarList(carList);

        return person;
    }
}
