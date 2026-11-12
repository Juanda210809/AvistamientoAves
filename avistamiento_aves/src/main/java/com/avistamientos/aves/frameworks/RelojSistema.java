package com.avistamientos.aves.frameworks;

import java.time.LocalDate;

import com.avistamientos.aves.entities.Reloj;
import org.springframework.stereotype.Component;

@Component
public class RelojSistema implements Reloj {
    @Override
    public LocalDate hoy() {
        return LocalDate.now();
    }
}