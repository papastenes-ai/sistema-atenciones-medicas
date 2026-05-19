package com.duoc.atencionesmedicas.medico.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.atencionesmedicas.medico.model.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    Optional<Medico> findByRut(String rut);

    List<Medico> findByApellido(String apellido);

}