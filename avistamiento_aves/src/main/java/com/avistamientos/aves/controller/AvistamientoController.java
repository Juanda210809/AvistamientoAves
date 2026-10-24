package com.avistamientos.aves.controller;

import com.avistamientos.aves.model.Avistamiento;

import com.avistamientos.aves.service.AvistamientoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/avistamientos")
public class AvistamientoController {

    private final AvistamientoService service;

    public AvistamientoController(AvistamientoService service) {
    
        this.service = service;
    }

    @GetMapping
    public List<Avistamiento> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avistamiento> ver(@PathVariable Long id) {
        return service.buscar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Avistamiento> crear( @RequestBody Avistamiento a) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(a));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avistamiento> actualizar(@PathVariable Long id,
                                                    @RequestBody Avistamiento a) {
        return service.actualizar(id, a)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Bonus: cuántos avistamientos hay por especie
    @GetMapping("/resumen")
    public Map<String, Long> resumen() {
        return service.resumen();
    }
}