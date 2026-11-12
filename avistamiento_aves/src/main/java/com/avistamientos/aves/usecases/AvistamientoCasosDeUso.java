package com.avistamientos.aves.usecases;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.avistamientos.aves.entities.Avistamiento;

/**
 * Puerto de entrada (input boundary) de los casos de uso.
 *
 * El controlador, en el círculo de adaptadores de interfaz, depende de esta
 * interfaz y no de la clase que la implementa.
 */
public interface AvistamientoCasosDeUso {

    List<Avistamiento> listar();

    Optional<Avistamiento> buscar(Long id);

    Avistamiento crear(Avistamiento avistamiento);

    Optional<Avistamiento> actualizar(Long id, Avistamiento avistamiento);

    boolean eliminar(Long id);

    Map<String, Long> resumen();
}
