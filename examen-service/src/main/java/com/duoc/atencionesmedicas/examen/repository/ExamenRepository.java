package com.duoc.atencionesmedicas.examen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.atencionesmedicas.examen.model.Examen;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Integer> {

    List<Examen> findByAtencionId(Integer atencionId);

    List<Examen> findByNombreExamenContainingIgnoreCase(String nombreExamen);
}