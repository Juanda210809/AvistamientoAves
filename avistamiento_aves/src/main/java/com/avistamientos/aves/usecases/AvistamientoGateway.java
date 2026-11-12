package com.avistamientos.aves.usecases;

import java.util.List;
import java.util.Optional;

import com.avistamientos.aves.entities.Avistamiento;

/**
 * Puerto de salida (gateway) de los casos de uso.
 *
 * Lo define el círculo de casos de uso con lo que necesita guardar y consultar,
 * y lo implementa un adaptador de un círculo exterior. Así el caso de uso no
 * depende de la base de datos: la dependencia apunta hacia adentro
 * (inversión de dependencias).
 */
public interface AvistamientoGateway {

    List<Avistamiento> listarTodos();

    Optional<Avistamiento> buscarPorId(Long id);

    Avistamiento guardar(Avistamiento avistamiento);

    boolean existe(Long id);

    void eliminar(Long id);
}
