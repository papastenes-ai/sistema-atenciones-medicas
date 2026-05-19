package com.duoc.atencionesmedicas.atencion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.atencionesmedicas.atencion.model.Atencion;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Integer> {

    List<Atencion> findByPacienteId(Integer pacienteId);

    List<Atencion> findByMedicoId(Integer medicoId);
}