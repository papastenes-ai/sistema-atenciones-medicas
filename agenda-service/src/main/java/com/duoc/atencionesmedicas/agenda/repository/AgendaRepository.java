package com.duoc.atencionesmedicas.agenda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.atencionesmedicas.agenda.model.Agenda;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Integer> {

    List<Agenda> findByPacienteId(Integer pacienteId);

    List<Agenda> findByMedicoId(Integer medicoId);

    List<Agenda> findByEstado(String estado);
}