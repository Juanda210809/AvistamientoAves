package com.avistamientos.aves.service;

import com.avistamientos.aves.model.Avistamiento;
import com.avistamientos.aves.repository.AvistamientoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvistamientoService {

	
	
    private final AvistamientoRepository repo;

    
    
    public AvistamientoService(AvistamientoRepository repo) {
        this.repo = repo;
    }

    public List<Avistamiento> listar() {
        return repo.findAll();
    }

    public Optional<Avistamiento> buscar(Long id) {
        return repo.findById(id);
    }

    public Avistamiento crear(Avistamiento a) {
        a.setId(null); // el id lo asigna el sistema, ignoramos el que venga en el JSON
        return repo.save(a);
    }

    public Optional<Avistamiento> actualizar(Long id, Avistamiento a) {
        return repo.findById(id).map(existente -> {
            existente.setEspecie(a.getEspecie());
            existente.setLugar(a.getLugar());
            existente.setFecha(a.getFecha());
            existente.setObservador(a.getObservador());
            return repo.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    public Map<String, Long> resumen() {
        return repo.findAll().stream()
                .collect(Collectors.groupingBy(Avistamiento::getEspecie, Collectors.counting()));
    }
}
