package com.avistamientos.aves.usecases;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.avistamientos.aves.entities.ValidadorDeAvistamiento;
import com.avistamientos.aves.model.Avistamiento;

/**
 * Interactor: implementa los casos de uso de la aplicación (reglas propias de
 * esta aplicación). No conoce Spring, HTTP ni JPA; solo las entidades y el
 * puerto {@link AvistamientoGateway}.
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
