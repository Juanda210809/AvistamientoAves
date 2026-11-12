package com.avistamientos.aves.interfaceadapters.controllers;

import com.avistamientos.aves.entities.Avistamiento;

/**
 * El JSON que devuelve la API. Lo dejé aparte de la entidad para que un
 * cambio en el formato de respuesta no obligue a tocar el dominio.
 */
public class AvistamientoResponse {

    private final Long id;
    private final String especie;
    private final String lugar;
    private final String fecha;
    private final String observador;

    private AvistamientoResponse(Avistamiento a) {
        this.id = a.getId();
        this.especie = a.getEspecie();
        this.lugar = a.getLugar();
        this.fecha = a.getFecha();
        this.observador = a.getObservador();
    }

    public static AvistamientoResponse desde(Avistamiento a) {
        return new AvistamientoResponse(a);
    }

    public Long getId() {
        return id;
    }

    public String getEspecie() {
        return especie;
    }

    public String getLugar() {
        return lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public String getObservador() {
        return observador;
    }
}
