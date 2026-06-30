package com.duoc.atencionesmedicas.medico.repository;

import com.duoc.atencionesmedicas.medico.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    Optional<Medico> findByRut(String rut);

    List<Medico> findByApellido(String apellido);

    List<Medico> findByEspecialidadNombreEspecialidadContainingIgnoreCase(String especialidad);
}