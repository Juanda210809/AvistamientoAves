package com.avistamientos.aves.validacion;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import com.avistamientos.aves.model.Avistamiento;

@Component
public class ValidadorDeAvistamiento {

    public static final int ESPECIE_MIN = 3;
    public static final int ESPECIE_MAX = 50;

    private final Reloj reloj;

    public ValidadorDeAvistamiento(Reloj reloj) {
        this.reloj = reloj;
    }

    // Se detiene en el primer campo inválido: así cada prueba falla por una sola razón.
    public void validar(Avistamiento a) {
        validarEspecie(a.getEspecie());
        validarFecha(a.getFecha());
        validarNoVacio("lugar", a.getLugar());
        validarNoVacio("observador", a.getObservador());
    }

    private void validarEspecie(String especie) {
        if (especie == null || especie.isBlank()) {
            throw new AvistamientoInvalidoException("especie", "La especie es obligatoria");
        }
        int largo = especie.trim().length();
        if (largo < ESPECIE_MIN || largo > ESPECIE_MAX) {
            throw new AvistamientoInvalidoException("especie",
                    "La especie debe tener entre " + ESPECIE_MIN + " y " + ESPECIE_MAX + " caracteres");
        }
    }

    private void validarFecha(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new AvistamientoInvalidoException("fecha", "La fecha es obligatoria");
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(texto); // acepta solo AAAA-MM-DD y fechas que existan
        } catch (DateTimeParseException e) {
            throw new AvistamientoInvalidoException("fecha", "La fecha debe tener formato AAAA-MM-DD");
        }
        if (fecha.isAfter(reloj.hoy())) {
            throw new AvistamientoInvalidoException("fecha", "La fecha no puede ser posterior a hoy");
        }
    }

    private void validarNoVacio(String campo, String valor) {
        if (valor == null || valor.isBlank()) {
            throw new AvistamientoInvalidoException(campo, "El campo " + campo + " es obligatorio");
        }
    }
}