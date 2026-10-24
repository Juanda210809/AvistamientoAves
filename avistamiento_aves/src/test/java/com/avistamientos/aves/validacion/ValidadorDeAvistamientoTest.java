package com.avistamientos.aves.validacion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.avistamientos.aves.model.Avistamiento;

class ValidadorDeAvistamientoTest {

    private static final LocalDate HOY = LocalDate.of(2026, 10, 14);

    private final ValidadorDeAvistamiento validador =
            new ValidadorDeAvistamiento(new RelojFijo(HOY));

    // Construye un avistamiento válido, cambiando solo el campo que cada prueba necesita
    private Avistamiento avistamiento(String especie, String fecha, String lugar, String observador) {
        Avistamiento a = new Avistamiento();
        a.setEspecie(especie);
        a.setFecha(fecha);
        a.setLugar(lugar);
        a.setObservador(observador);
        return a;
    }

    private Avistamiento conEspecie(String especie) {
        return avistamiento(especie, "2026-10-01", "Chingaza", "Ana");
    }

    private Avistamiento conFecha(String fecha) {
        return avistamiento("colibri", fecha, "Chingaza", "Ana");
    }

    // ---------- Especie ----------

    @Test
    void aceptaEspecieDeTresCaracteres() {
        // Arrange
        Avistamiento a = conEspecie("abc");
        // Act + Assert
        assertDoesNotThrow(() -> validador.validar(a));
    }

    @Test
    void rechazaEspecieDeDosCaracteres() {
        // Arrange
        Avistamiento a = conEspecie("ab");
        // Act
        var ex = assertThrows(AvistamientoInvalidoException.class, () -> validador.validar(a));
        // Assert
        assertEquals("especie", ex.getCampo());
    }

    @Test
    void aceptaEspecieRepresentativa() {
        // Arrange
        Avistamiento a = conEspecie("colibri");
        // Act + Assert
        assertDoesNotThrow(() -> validador.validar(a));
    }

    @Test
    void aceptaEspecieDeCincuentaCaracteres() {
        // Arrange
        Avistamiento a = conEspecie("a".repeat(50));
        // Act + Assert
        assertDoesNotThrow(() -> validador.validar(a));
    }

    @Test
    void rechazaEspecieDeCincuentaYUnCaracteres() {
        // Arrange
        Avistamiento a = conEspecie("a".repeat(51));
        // Act
        var ex = assertThrows(AvistamientoInvalidoException.class, () -> validador.validar(a));
        // Assert
        assertEquals("especie", ex.getCampo());
    }

    @Test
    void rechazaEspecieVacia() {
        // Arrange
        Avistamiento a = conEspecie("");
        // Act
        var ex = assertThrows(AvistamientoInvalidoException.class, () -> validador.validar(a));
        // Assert
        assertEquals("especie", ex.getCampo());
    }

    @Test
    void rechazaEspecieCuyoLargoSinEspaciosEsMenorATres() {
        // Arrange
        Avistamiento a = conEspecie("   ab   ");
        // Act
        var ex = assertThrows(AvistamientoInvalidoException.class, () -> validador.validar(a));
        // Assert
        assertEquals("especie", ex.getCampo());
    }

    // ---------- Fecha ----------

    @Test
    void aceptaFechaDeHoy() {
        // Arrange
        Avistamiento a = conFecha("2026-10-14");
        // Act + Assert
        assertDoesNotThrow(() -> validador.validar(a));
    }

    @Test
    void rechazaFechaDeManana() {
        // Arrange
        Avistamiento a = conFecha("2026-10-15");
        // Act
        var ex = assertThrows(AvistamientoInvalidoException.class, () -> validador.validar(a));
        // Assert
        assertEquals("fecha", ex.getCampo());
    }

    @Test
    void aceptaFechaPasada() {
        // Arrange
        Avistamiento a = conFecha("2026-10-01");
        // Act + Assert
        assertDoesNotThrow(() -> validador.validar(a));
    }

    @Test
    void rechazaFechaConFormatoDiferenteAAAAMMDD() {
        // Arrange
        Avistamiento a = conFecha("14/10/2026");
        // Act
        var ex = assertThrows(AvistamientoInvalidoException.class, () -> validador.validar(a));
        // Assert
        assertEquals("fecha", ex.getCampo());
    }

    @Test
    void rechazaFechaQueNoExisteEnElCalendario() {
        // Arrange
        Avistamiento a = conFecha("2026-02-30");
        // Act
        var ex = assertThrows(AvistamientoInvalidoException.class, () -> validador.validar(a));
        // Assert
        assertEquals("fecha", ex.getCampo());
    }

    // ---------- Lugar y observador ----------

    @Test
    void rechazaLugarEnBlanco() {
        // Arrange
        Avistamiento a = avistamiento("colibri", "2026-10-01", "  ", "Ana");
        // Act
        var ex = assertThrows(AvistamientoInvalidoException.class, () -> validador.validar(a));
        // Assert
        assertEquals("lugar", ex.getCampo());
    }

    @Test
    void rechazaObservadorVacio() {
        // Arrange
        Avistamiento a = avistamiento("colibri", "2026-10-01", "Chingaza", "");
        // Act
        var ex = assertThrows(AvistamientoInvalidoException.class, () -> validador.validar(a));
        // Assert
        assertEquals("observador", ex.getCampo());
    }
}