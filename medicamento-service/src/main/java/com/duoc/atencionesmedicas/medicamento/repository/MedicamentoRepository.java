package com.duoc.atencionesmedicas.medicamento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.atencionesmedicas.medicamento.model.Medicamento;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {

    List<Medicamento> findByNombreContainingIgnoreCase(String nombre);
}