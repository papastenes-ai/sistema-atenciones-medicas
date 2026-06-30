package com.duoc.atencionesmedicas.paciente.repository;

import com.duoc.atencionesmedicas.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    Optional<Paciente> findByRut(String rut);
}