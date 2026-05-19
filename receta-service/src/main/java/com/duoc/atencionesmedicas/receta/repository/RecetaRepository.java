package com.duoc.atencionesmedicas.receta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.atencionesmedicas.receta.model.Receta;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {

    List<Receta> findByDiagnosticoId(Integer diagnosticoId);
}