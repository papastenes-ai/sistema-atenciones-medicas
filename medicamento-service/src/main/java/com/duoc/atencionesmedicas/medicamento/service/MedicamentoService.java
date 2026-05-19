package com.duoc.atencionesmedicas.medicamento.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.medicamento.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.medicamento.model.Medicamento;
import com.duoc.atencionesmedicas.medicamento.repository.MedicamentoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public List<Medicamento> listarMedicamentos() {
        return medicamentoRepository.findAll();
    }

    public Medicamento buscarPorId(Integer id) {

        return medicamentoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Medicamento no encontrado con id: " + id));
    }

    public List<Medicamento> buscarPorNombre(String nombre) {

        List<Medicamento> medicamentos =
                medicamentoRepository.findByNombreContainingIgnoreCase(nombre);

        if (medicamentos.isEmpty()) {

            throw new RecursoNoEncontradoException(
                    "No existen medicamentos con nombre: " + nombre);
        }

        return medicamentos;
    }

    public Medicamento guardarMedicamento(Medicamento medicamento) {

        try {

            return medicamentoRepository.save(medicamento);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al guardar el medicamento: " + e.getMessage());
        }
    }

    public Medicamento actualizarMedicamento(Integer id, Medicamento medicamentoActualizado) {

        try {

            Medicamento medicamento = buscarPorId(id);

            medicamento.setNombre(
                    medicamentoActualizado.getNombre());

            medicamento.setDescripcion(
                    medicamentoActualizado.getDescripcion());

            medicamento.setPresentacion(
                    medicamentoActualizado.getPresentacion());

            medicamento.setStock(
                    medicamentoActualizado.getStock());

            return medicamentoRepository.save(medicamento);

        } catch (RecursoNoEncontradoException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al actualizar el medicamento: " + e.getMessage());
        }
    }

    public void eliminarMedicamento(Integer id) {

        try {

            Medicamento medicamento = buscarPorId(id);

            medicamentoRepository.delete(medicamento);

        } catch (RecursoNoEncontradoException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al eliminar el medicamento: " + e.getMessage());
        }
    }
}