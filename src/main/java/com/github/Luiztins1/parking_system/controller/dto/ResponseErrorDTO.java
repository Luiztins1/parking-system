package com.github.Luiztins1.parking_system.controller.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseErrorDTO(
        HttpStatus error,
        String messageError,
        List<FieldErrorDTO> fieldErrorList) {
}
