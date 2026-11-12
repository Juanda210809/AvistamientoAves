package com.avistamientos.aves.interfaceadapters.gateways;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.avistamientos.aves.model.Avistamiento;
import com.avistamientos.aves.usecases.AvistamientoGateway;

/**
 * Adaptador de persistencia: implementa el puerto de salida que definió el
 * caso de uso, usando Spring Data JPA.
 */
@Component
public class AvistamientoGatewayJpa implements AvistamientoGateway {

    private final AvistamientoJpaRepository repo;

    public AvistamientoGatewayJpa(AvistamientoJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Avistamiento> listarTodos() {
        return repo.findAll();
    }

    @Override
    public Optional<Avistamiento> buscarPorId(Long id) {
        return repo.findById(id);
    }

    @Override
    public Avistamiento guardar(Avistamiento avistamiento) {
        return repo.save(avistamiento);
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
