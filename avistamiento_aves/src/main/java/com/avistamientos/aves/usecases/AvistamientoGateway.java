package com.avistamientos.aves.usecases;

import java.util.List;
import java.util.Optional;

import com.avistamientos.aves.entities.Avistamiento;

/**
 * Puerto de salida: lo que el caso de uso necesita para guardar y consultar.
 *
 * La interfaz vive en usecases y la implementación está afuera
 * (AvistamientoGatewayJpa). Por eso el caso de uso no depende de la base de
 * datos: la flecha apunta hacia adentro.
 */
public interface AvistamientoGateway {

    List<Avistamiento> listarTodos();

    Optional<Avistamiento> buscarPorId(Long id);

    Avistamiento guardar(Avistamiento avistamiento);

    boolean existe(Long id);

    void eliminar(Long id);
}
