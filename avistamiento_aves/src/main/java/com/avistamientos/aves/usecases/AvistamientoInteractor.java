package com.avistamientos.aves.usecases;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avistamientos.aves.entities.ValidadorDeAvistamiento;
import com.avistamientos.aves.entities.Avistamiento;

/**
 * Aquí están los casos de uso de la API (listar, crear, actualizar...).
 * Ojo: esta clase no sabe nada de Spring, HTTP o JPA. Solo usa las entidades
 * y la interfaz {@link AvistamientoGateway} para guardar.
 */
public class AvistamientoInteractor implements AvistamientoCasosDeUso {

    private final AvistamientoGateway gateway;
    private final ValidadorDeAvistamiento validador;

    public AvistamientoInteractor(AvistamientoGateway gateway, ValidadorDeAvistamiento validador) {
        this.gateway = gateway;
        this.validador = validador;
    }

    @Override
    public List<Avistamiento> listar() {
        return gateway.listarTodos();
    }

    @Override
    public Optional<Avistamiento> buscar(Long id) {
        return gateway.buscarPorId(id);
    }

    @Override
    public Avistamiento crear(Avistamiento a) {
        validador.validar(a);
        a.setId(null); // el id lo asigna el sistema, ignoramos el que venga en el JSON
        return gateway.guardar(a);
    }

    @Override
    public Optional<Avistamiento> actualizar(Long id, Avistamiento a) {
        validador.validar(a); // se valida antes de buscar: un PUT inválido a un id inexistente da 400
        return gateway.buscarPorId(id).map(existente -> {
            existente.setEspecie(a.getEspecie());
            existente.setLugar(a.getLugar());
            existente.setFecha(a.getFecha());
            existente.setObservador(a.getObservador());
            return gateway.guardar(existente);
        });
    }

    @Override
    public boolean eliminar(Long id) {
        if (!gateway.existe(id)) {
            return false;
        }
        gateway.eliminar(id);
        return true;
    }

    @Override
    public Map<String, Long> resumen() {
        return gateway.listarTodos().stream()
                .collect(Collectors.groupingBy(Avistamiento::getEspecie, Collectors.counting()));
    }
}
