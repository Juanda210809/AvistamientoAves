package com.avistamientos.aves.interfaceadapters.controllers;

import java.util.Map;

import com.avistamientos.aves.entities.AvistamientoInvalidoException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorDeErrores {

    @ExceptionHandler(AvistamientoInvalidoException.class)
    public ResponseEntity<Map<String, String>> invalido(AvistamientoInvalidoException e) {
        return ResponseEntity.badRequest()
                .body(Map.of("campo", e.getCampo(), "error", e.getMessage()));
    }
}