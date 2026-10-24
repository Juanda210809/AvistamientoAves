package com.avistamientos.aves.validacion;

import java.time.LocalDate;

class RelojFijo implements Reloj {

    private final LocalDate fecha;

    RelojFijo(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public LocalDate hoy() {
        return fecha;
    }
}