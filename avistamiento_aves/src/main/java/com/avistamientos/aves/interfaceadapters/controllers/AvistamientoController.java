package com.avistamientos.aves.interfaceadapters.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avistamientos.aves.usecases.AvistamientoCasosDeUso;

/**
 * Adaptador de entrada HTTP. Traduce la petición a una llamada al caso de uso
 * y el resultado a un código de estado y un JSON. No contiene reglas de negocio.
 */
@RestController
@RequestMapping("/avistamientos")
public class AvistamientoController {

    private final AvistamientoCasosDeUso casosDeUso;

    public AvistamientoController(AvistamientoCasosDeUso casosDeUso) {
        this.casosDeUso = casosDeUso;
    }

    @GetMapping
    public List<AvistamientoResponse> listar() {
        return casosDeUso.listar().stream().map(AvistamientoResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvistamientoResponse> ver(@PathVariable Long id) {
        return casosDeUso.buscar(id)
                .map(AvistamientoResponse::desde)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AvistamientoResponse> crear(@RequestBody AvistamientoRequest peticion) {
        var creado = casosDeUso.crear(peticion.haciaDominio());
        return ResponseEntity.status(HttpStatus.CREATED).body(AvistamientoResponse.desde(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvistamientoResponse> actualizar(@PathVariable Long id,
                                                           @RequestBody AvistamientoRequest peticion) {
        return casosDeUso.actualizar(id, peticion.haciaDominio())
                .map(AvistamientoResponse::desde)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return casosDeUso.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // Bonus: cuántos avistamientos hay por especie
    @GetMapping("/resumen")
    public Map<String, Long> resumen() {
        return casosDeUso.resumen();
    }
}
