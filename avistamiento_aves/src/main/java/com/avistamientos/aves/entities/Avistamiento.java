package com.avistamientos.aves.entities;

/**
 * Entidad del dominio: un avistamiento.
 *
 * Es una clase normal, sin @Entity ni nada de Spring. Cómo se guarda en la
 * base lo decide AvistamientoJpa, en interfaceadapters/gateways.
 */
public class Avistamiento {

    private Long id;
    private String especie;
    private String lugar;
    private String fecha;
    private String observador;

    public Avistamiento() {
    }

    public Avistamiento(Long id, String especie, String lugar, String fecha, String observador) {
        this.id = id;
        this.especie = especie;
        this.lugar = lugar;
        this.fecha = fecha;
        this.observador = observador;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getObservador() {
        return observador;
    }

    public void setObservador(String observador) {
        this.observador = observador;
    }
}
