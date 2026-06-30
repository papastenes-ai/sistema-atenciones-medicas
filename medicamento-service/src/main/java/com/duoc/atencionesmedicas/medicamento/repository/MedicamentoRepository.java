package com.duoc.atencionesmedicas.medicamento.repository;

import com.duoc.atencionesmedicas.medicamento.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {

    List<Medicamento> findByNombreMedicamentoContainingIgnoreCase(String nombreMedicamento);
}