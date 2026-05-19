package com.duoc.atencionesmedicas.centromedico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.atencionesmedicas.centromedico.model.CentroMedico;

@Repository
public interface CentroMedicoRepository
        extends JpaRepository<CentroMedico, Integer> {

    List<CentroMedico> findByComuna(String comuna);

    List<CentroMedico> findByEstado(String estado);

    List<CentroMedico> findByNombreContainingIgnoreCase(String nombre);
}