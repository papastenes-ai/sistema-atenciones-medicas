package com.duoc.atencionesmedicas.medicamento.service;

import com.duoc.atencionesmedicas.medicamento.dto.MedicamentoRequestDTO;
import com.duoc.atencionesmedicas.medicamento.dto.MedicamentoResponseDTO;
import com.duoc.atencionesmedicas.medicamento.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.medicamento.model.Medicamento;
import com.duoc.atencionesmedicas.medicamento.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    private MedicamentoResponseDTO mapToResponseDTO(Medicamento medicamento) {
        return new MedicamentoResponseDTO(
                medicamento.getIdMedicamento(),
                medicamento.getNombreMedicamento(),
                medicamento.getDescripcion(),
                medicamento.getLaboratorio(),
                medicamento.getStock()
        );
    }

    private Medicamento buscarEntidadPorId(Integer id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Medicamento no encontrado con id: " + id));
    }

    public List<MedicamentoResponseDTO> listarMedicamentos() {
        return medicamentoRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicamentoResponseDTO buscarPorId(Integer id) {
        Medicamento medicamento = buscarEntidadPorId(id);
        return mapToResponseDTO(medicamento);
    }

    public List<MedicamentoResponseDTO> buscarPorNombreMedicamento(String nombreMedicamento) {
        List<Medicamento> medicamentos =
                medicamentoRepository.findByNombreMedicamentoContainingIgnoreCase(nombreMedicamento);

        if (medicamentos.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen medicamentos con nombre: " + nombreMedicamento);
        }

        return medicamentos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicamentoResponseDTO guardarMedicamento(MedicamentoRequestDTO dto) {
        Medicamento medicamento = new Medicamento(
                null,
                dto.getNombreMedicamento(),
                dto.getDescripcion(),
                dto.getLaboratorio(),
                dto.getStock()
        );

        Medicamento medicamentoGuardado = medicamentoRepository.save(medicamento);

        log.info("Medicamento guardado correctamente con id: {}",
                medicamentoGuardado.getIdMedicamento());

        return mapToResponseDTO(medicamentoGuardado);
    }

    public MedicamentoResponseDTO actualizarMedicamento(Integer id, MedicamentoRequestDTO dto) {
        Medicamento medicamento = buscarEntidadPorId(id);

        medicamento.setNombreMedicamento(dto.getNombreMedicamento());
        medicamento.setDescripcion(dto.getDescripcion());
        medicamento.setLaboratorio(dto.getLaboratorio());
        medicamento.setStock(dto.getStock());

        Medicamento medicamentoActualizado = medicamentoRepository.save(medicamento);

        log.info("Medicamento id {} actualizado correctamente.", id);

        return mapToResponseDTO(medicamentoActualizado);
    }

    public void eliminarMedicamento(Integer id) {
        Medicamento medicamento = buscarEntidadPorId(id);
        medicamentoRepository.delete(medicamento);

        log.info("Medicamento id {} eliminado correctamente.", id);
    }
}