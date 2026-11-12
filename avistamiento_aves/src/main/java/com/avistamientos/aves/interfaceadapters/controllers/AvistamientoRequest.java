package com.avistamientos.aves.interfaceadapters.controllers;

import com.avistamientos.aves.entities.Avistamiento;

/**
 * Lo que llega en el cuerpo de POST y PUT. El controlador lo pasa a entidad
 * antes de llamar al caso de uso. Si viene un "id" en el JSON, no se usa.
 */
public record AvistamientoRequest(String especie, String lugar, String fecha, String observador) {

    public Avistamiento haciaDominio() {
        return new Avistamiento(null, especie, lugar, fecha, observador);
    }
}
