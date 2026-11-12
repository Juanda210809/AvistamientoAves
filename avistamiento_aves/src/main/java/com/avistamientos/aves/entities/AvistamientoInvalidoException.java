package com.avistamientos.aves.entities;

public class AvistamientoInvalidoException extends RuntimeException {
    private final String campo;

    public AvistamientoInvalidoException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}