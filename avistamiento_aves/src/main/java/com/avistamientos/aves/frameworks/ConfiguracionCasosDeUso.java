package com.avistamientos.aves.frameworks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avistamientos.aves.entities.Reloj;
import com.avistamientos.aves.entities.ValidadorDeAvistamiento;
import com.avistamientos.aves.usecases.AvistamientoCasosDeUso;
import com.avistamientos.aves.usecases.AvistamientoGateway;
import com.avistamientos.aves.usecases.AvistamientoInteractor;

/**
 * Ensamblaje (parte "Main" del círculo de frameworks y drivers).
 *
 * Las entidades y los casos de uso no llevan anotaciones de Spring; aquí se
 * crean y se conectan con sus implementaciones concretas. Es el único lugar
 * que conoce a la vez el núcleo y los detalles.
 */
@Configuration
public class ConfiguracionCasosDeUso {

    @Bean
    public ValidadorDeAvistamiento validadorDeAvistamiento(Reloj reloj) {
        return new ValidadorDeAvistamiento(reloj);
    }

    @Bean
    public AvistamientoCasosDeUso avistamientoCasosDeUso(AvistamientoGateway gateway,
                                                         ValidadorDeAvistamiento validador) {
        return new AvistamientoInteractor(gateway, validador);
    }
}
