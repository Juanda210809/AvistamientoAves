package com.avistamientos.aves.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.avistamientos.aves.model.Avistamiento;

@Repository
public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long>{
	

}
