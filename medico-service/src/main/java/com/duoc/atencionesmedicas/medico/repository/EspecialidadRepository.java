package com.duoc.atencionesmedicas.medico.repository;

import com.duoc.atencionesmedicas.medico.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
}