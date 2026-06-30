package com.duoc.atencionesmedicas.medico.repository;

import com.duoc.atencionesmedicas.medico.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
}