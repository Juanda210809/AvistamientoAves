package com.avistamientos.aves.interfaceadapters.gateways;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.avistamientos.aves.entities.Avistamiento;

/**
 * Forma en que un avistamiento se guarda en la base de datos (fila de la tabla).
 *
 * Es distinta de la entidad del dominio: los datos de la base no cruzan hacia
 * los círculos interiores. Los métodos desde/haciaDominio hacen la conversión.
 * Se conserva el nombre de tabla "avistamiento" para no perder los datos ya
 * guardados en data/.
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
