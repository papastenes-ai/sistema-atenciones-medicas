package com.duoc.atencionesmedicas.paciente.repository;

import com.duoc.atencionesmedicas.paciente.model.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {
}