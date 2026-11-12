package com.avistamientos.aves.usecases;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.avistamientos.aves.entities.Avistamiento;

/**
 * Puerto de entrada. El controlador llama a esta interfaz, no al interactor
 * directamente.
 */
public interface AvistamientoCasosDeUso {

    List<Avistamiento> listar();

    Optional<Avistamiento> buscar(Long id);

    Avistamiento crear(Avistamiento avistamiento);

    Optional<Avistamiento> actualizar(Long id, Avistamiento avistamiento);

    boolean eliminar(Long id);

    Map<String, Long> resumen();
}
