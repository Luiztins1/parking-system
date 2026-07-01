package com.github.Luiztins1.parking_system.controller.common;

import com.github.Luiztins1.parking_system.controller.dto.FieldErrorDTO;
import com.github.Luiztins1.parking_system.controller.dto.ResponseErrorDTO;
import com.github.Luiztins1.parking_system.exceptions.DataIntegrityViolationException;
import com.github.Luiztins1.parking_system.exceptions.DuplicateException;
import com.github.Luiztins1.parking_system.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorDTO httpMessageNotReadableException(HttpMessageNotReadableException e){
        return new ResponseErrorDTO(HttpStatus.BAD_REQUEST, "Erro de leitura de JSON ou valor inválido.", List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErrorDTO methodArgumentNotValidException (MethodArgumentNotValidException e){
        List<FieldErrorDTO> fieldErrorDTOList =
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
                        .toList();
        return new ResponseErrorDTO(HttpStatus.BAD_REQUEST, "Campos com valores inválidos ou vazios.", fieldErrorDTOList);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorDTO dataIntegrityViolationException (DataIntegrityViolationException e){
        return new ResponseErrorDTO(HttpStatus.CONFLICT, "Erro de integridade ou restrição de banco de dados.", List.of());
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseErrorDTO duplicateException (DuplicateException e){
        return new ResponseErrorDTO(HttpStatus.CONFLICT, e.getMessage(), List.of());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseErrorDTO notFoundException (NotFoundException e){
        return new ResponseErrorDTO(HttpStatus.NOT_FOUND, e.getMessage(), List.of());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseErrorDTO acessDenied(AuthorizationDeniedException e){
        return new ResponseErrorDTO(HttpStatus.FORBIDDEN, "Você não possui permissão para essa função.", List.of());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseErrorDTO handleGenericException(Exception e) {
        return new ResponseErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro interno inesperado no servidor. Por favor, tente novamente mais tarde.",
                List.of()
        );
    }
}
