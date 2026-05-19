package com.duoc.atencionesmedicas.diagnostico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.atencionesmedicas.diagnostico.model.Diagnostico;

@Repository
public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Integer> {

    List<Diagnostico> findByAtencionId(Integer atencionId);
}