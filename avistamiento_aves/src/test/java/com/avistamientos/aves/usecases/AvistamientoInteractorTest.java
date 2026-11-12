package com.avistamientos.aves.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.avistamientos.aves.entities.Avistamiento;
import com.avistamientos.aves.entities.AvistamientoInvalidoException;
import com.avistamientos.aves.entities.ValidadorDeAvistamiento;

@ExtendWith(MockitoExtension.class)
class AvistamientoInteractorTest {

    @Mock
    private AvistamientoGateway gateway;

    private AvistamientoInteractor service;

    @BeforeEach
    void preparar() {
        // Reloj es una interfaz con un solo método: un lambda hace de stub con fecha fija
        var validador = new ValidadorDeAvistamiento(() -> LocalDate.of(2026, 10, 14));
        service = new AvistamientoInteractor(gateway, validador);
    }

    private Avistamiento valido(String especie) {
        Avistamiento a = new Avistamiento();
        a.setEspecie(especie);
        a.setLugar("Chingaza");
        a.setFecha("2026-10-01");
        a.setObservador("Ana");
        return a;
    }

    @Test
    void crearIgnoraElIdRecibidoYGuardaConIdNulo() {
        // Arrange
        Avistamiento a = valido("colibri");
        a.setId(99L);
        when(gateway.guardar(any())).thenAnswer(inv -> inv.getArgument(0));
        // Act
        service.crear(a);
        // Assert
        ArgumentCaptor<Avistamiento> capturado = ArgumentCaptor.forClass(Avistamiento.class);
        verify(gateway).guardar(capturado.capture());
        assertNull(capturado.getValue().getId());
    }

    @Test
    void crearConDatosInvalidosNoLlegaAlRepositorio() {
        // Arrange
        Avistamiento a = valido("ab");
        // Act
        assertThrows(AvistamientoInvalidoException.class, () -> service.crear(a));
        // Assert
        verifyNoInteractions(gateway);
    }

    @Test
    void actualizarCopiaLosCamposNuevosAlAvistamientoExistente() {
        // Arrange
        Avistamiento existente = valido("aguila");
        existente.setId(1L);
        Avistamiento nuevo = valido("colibri");
        nuevo.setLugar("Sumapaz");
        when(gateway.buscarPorId(1L)).thenReturn(Optional.of(existente));
        when(gateway.guardar(any())).thenAnswer(inv -> inv.getArgument(0));
        // Act
        Optional<Avistamiento> resultado = service.actualizar(1L, nuevo);
        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("colibri", resultado.get().getEspecie());
        assertEquals("Sumapaz", resultado.get().getLugar());
        assertEquals(1L, resultado.get().getId());
    }

    @Test
    void actualizarDevuelveVacioYNoGuardaCuandoElIdNoExiste() {
        // Arrange
        when(gateway.buscarPorId(5L)).thenReturn(Optional.empty());
        // Act
        Optional<Avistamiento> resultado = service.actualizar(5L, valido("colibri"));
        // Assert
        assertTrue(resultado.isEmpty());
        verify(gateway, never()).guardar(any());
    }

    @Test
    void actualizarConDatosInvalidosFallaAunqueElIdNoExista() {
        // Arrange
        Avistamiento invalido = valido("ab");
        // Act
        assertThrows(AvistamientoInvalidoException.class, () -> service.actualizar(5L, invalido));
        // Assert: la validación va antes de buscar (400 antes que 404)
        verifyNoInteractions(gateway);
    }

    @Test
    void eliminarBorraYDevuelveTrueCuandoElIdExiste() {
        // Arrange
        when(gateway.existe(9L)).thenReturn(true);
        // Act
        boolean resultado = service.eliminar(9L);
        // Assert
        assertTrue(resultado);
        verify(gateway).eliminar(9L);
    }

    @Test
    void eliminarDevuelveFalseYNoBorraCuandoElIdNoExiste() {
        // Arrange
        when(gateway.existe(9L)).thenReturn(false);
        // Act
        boolean resultado = service.eliminar(9L);
        // Assert
        assertFalse(resultado);
        verify(gateway, never()).eliminar(any());
    }

    @Test
    void resumenCuentaLosAvistamientosPorEspecie() {
        // Arrange
        when(gateway.listarTodos()).thenReturn(
                List.of(valido("colibri"), valido("colibri"), valido("aguila")));
        // Act
        Map<String, Long> resumen = service.resumen();
        // Assert
        assertEquals(Map.of("colibri", 2L, "aguila", 1L), resumen);
    }
}