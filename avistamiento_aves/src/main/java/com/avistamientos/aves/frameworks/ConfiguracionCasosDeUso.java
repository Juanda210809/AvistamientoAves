package com.avistamientos.aves.frameworks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.avistamientos.aves.entities.Reloj;
import com.avistamientos.aves.entities.ValidadorDeAvistamiento;
import com.avistamientos.aves.usecases.AvistamientoCasosDeUso;
import com.avistamientos.aves.usecases.AvistamientoGateway;
import com.avistamientos.aves.usecases.AvistamientoInteractor;

/**
 * Como el validador y el interactor ya no tienen @Component ni @Service,
 * los creo aquí a mano. Es el único sitio que conoce el núcleo y también
 * las clases concretas (el "Main" del que habla Martin).
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
