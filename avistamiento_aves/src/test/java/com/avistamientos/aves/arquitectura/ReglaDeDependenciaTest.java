package com.avistamientos.aves.arquitectura;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Revisa los imports del código para comprobar la regla de dependencia:
 * un círculo de adentro no puede importar nada de uno de afuera.
 * Si alguien la rompe, esta prueba lo avisa.
 */
class ReglaDeDependenciaTest {

    private static final Path RAIZ = Path.of("src/main/java/com/avistamientos/aves");
    private static final String BASE = "com.avistamientos.aves.";

    private List<String> importsDe(String circulo) throws IOException {
        try (Stream<Path> archivos = Files.walk(RAIZ.resolve(circulo))) {
            return archivos.filter(p -> p.toString().endsWith(".java"))
                    .flatMap(p -> {
                        try {
                            return Files.readAllLines(p).stream();
                        } catch (IOException e) {
                            throw new IllegalStateException(e);
                        }
                    })
                    .filter(linea -> linea.startsWith("import "))
                    .toList();
        }
    }

    private void assertNoImporta(String circulo, String... prohibidos) throws IOException {
        for (String linea : importsDe(circulo)) {
            for (String prohibido : prohibidos) {
                assertTrue(!linea.contains(prohibido),
                        circulo + " no puede depender de " + prohibido + ": " + linea);
            }
        }
    }

    @Test
    void lasEntidadesNoDependenDeNingunOtroCirculoNiDeFrameworks() throws IOException {
        assertNoImporta("entities",
                BASE + "usecases", BASE + "interfaceadapters", BASE + "frameworks",
                "org.springframework", "jakarta.");
    }

    @Test
    void losCasosDeUsoSoloDependenDeLasEntidades() throws IOException {
        assertNoImporta("usecases",
                BASE + "interfaceadapters", BASE + "frameworks",
                "org.springframework", "jakarta.");
    }

    @Test
    void losAdaptadoresNoDependenDelCirculoDeFrameworks() throws IOException {
        assertNoImporta("interfaceadapters", BASE + "frameworks");
    }
}
