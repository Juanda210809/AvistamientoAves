package com.avistamientos.aves.interfaceadapters.gateways;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.avistamientos.aves.entities.Avistamiento;

/**
 * La fila de la tabla en la base de datos. No es la misma clase que la
 * entidad del dominio; desdeDominio y haciaDominio pasan de una a otra.
 *
 * Mantengo el nombre de tabla "avistamiento" para no perder lo que ya estaba
 * guardado en data/. Los setters solo los usa la prueba de integración para
 * meter datos (Hibernate lee los campos directo).
 */
@Entity
@Table(name = "avistamiento")
public class AvistamientoJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String especie;
    private String lugar;
    private String fecha;
    private String observador;

    public AvistamientoJpa() {
    }

    public static AvistamientoJpa desdeDominio(Avistamiento a) {
        AvistamientoJpa fila = new AvistamientoJpa();
        fila.id = a.getId();
        fila.especie = a.getEspecie();
        fila.lugar = a.getLugar();
        fila.fecha = a.getFecha();
        fila.observador = a.getObservador();
        return fila;
    }

    public Avistamiento haciaDominio() {
        return new Avistamiento(id, especie, lugar, fecha, observador);
    }

    public Long getId() {
        return id;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setObservador(String observador) {
        this.observador = observador;
    }
}
