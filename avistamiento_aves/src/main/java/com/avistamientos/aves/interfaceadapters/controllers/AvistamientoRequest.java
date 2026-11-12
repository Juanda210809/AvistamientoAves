package com.avistamientos.aves.interfaceadapters.controllers;

import com.avistamientos.aves.entities.Avistamiento;

/**
 * Cuerpo JSON que llega en POST y PUT. Es una estructura de datos simple:
 * el controlador la convierte en la entidad del dominio antes de llamar al
 * caso de uso. Si el JSON trae un "id", se ignora.
 */
public record AvistamientoRequest(String especie, String lugar, String fecha, String observador) {

    public Avistamiento haciaDominio() {
        return new Avistamiento(null, especie, lugar, fecha, observador);
    }
}
