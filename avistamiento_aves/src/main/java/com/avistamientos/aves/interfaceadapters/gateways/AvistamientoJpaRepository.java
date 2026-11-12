package com.avistamientos.aves.interfaceadapters.gateways;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avistamientos.aves.model.Avistamiento;

/** Repositorio de Spring Data. Solo lo usa el adaptador {@link AvistamientoGatewayJpa}. */
@Repository
public interface AvistamientoJpaRepository extends JpaRepository<Avistamiento, Long> {
}
