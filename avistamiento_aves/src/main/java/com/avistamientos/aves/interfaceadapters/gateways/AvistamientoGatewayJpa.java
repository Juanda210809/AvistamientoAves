package com.avistamientos.aves.interfaceadapters.gateways;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.avistamientos.aves.entities.Avistamiento;
import com.avistamientos.aves.usecases.AvistamientoGateway;

/**
 * Implementa el puerto AvistamientoGateway con Spring Data JPA.
 * Antes de guardar convierte a {@link AvistamientoJpa} y al leer devuelve la entidad.
 */
@Component
public class AvistamientoGatewayJpa implements AvistamientoGateway {

    private final AvistamientoJpaRepository repo;

    public AvistamientoGatewayJpa(AvistamientoJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Avistamiento> listarTodos() {
        return repo.findAll().stream().map(AvistamientoJpa::haciaDominio).toList();
    }

    @Override
    public Optional<Avistamiento> buscarPorId(Long id) {
        return repo.findById(id).map(AvistamientoJpa::haciaDominio);
    }

    @Override
    public Avistamiento guardar(Avistamiento avistamiento) {
        return repo.save(AvistamientoJpa.desdeDominio(avistamiento)).haciaDominio();
    }

    @Override
    public boolean existe(Long id) {
        return repo.existsById(id);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
