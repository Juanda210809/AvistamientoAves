package com.avistamientos.aves.interfaceadapters.gateways;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repositorio de Spring Data. Solo lo usa el adaptador {@link AvistamientoGatewayJpa}. */
@Repository
public interface AvistamientoJpaRepository extends JpaRepository<AvistamientoJpa, Long> {
}
