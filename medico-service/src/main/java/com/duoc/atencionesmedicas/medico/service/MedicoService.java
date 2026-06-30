package com.duoc.atencionesmedicas.medico.service;

import com.duoc.atencionesmedicas.medico.dto.MedicoRequestDTO;
import com.duoc.atencionesmedicas.medico.dto.MedicoResponseDTO;
import com.duoc.atencionesmedicas.medico.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.medico.model.Especialidad;
import com.duoc.atencionesmedicas.medico.model.Medico;
import com.duoc.atencionesmedicas.medico.repository.EspecialidadRepository;
import com.duoc.atencionesmedicas.medico.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;

    private MedicoResponseDTO mapToResponseDTO(Medico medico) {

    Integer especialidadId = null;
    String especialidadNombre = null;

    if (medico.getEspecialidad() != null) {
        especialidadId = medico.getEspecialidad().getIdEspecialidad();
        especialidadNombre = medico.getEspecialidad().getNombreEspecialidad();
    }

    return new MedicoResponseDTO(
            medico.getIdMedico(),
            medico.getRut(),
            medico.getNombre(),
            medico.getApellido(),
            medico.getCorreo(),
            medico.getTelefono(),
            especialidadId,
            especialidadNombre
    );
}
    private Medico buscarEntidadPorId(Integer id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Médico no encontrado con id: " + id));
    }

    private Especialidad buscarEspecialidadPorId(Integer id) {
        return especialidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Especialidad no encontrada con id: " + id));
    }

    public List<MedicoResponseDTO> listarMedicos() {
        return medicoRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicoResponseDTO buscarPorId(Integer id) {
        Medico medico = buscarEntidadPorId(id);
        return mapToResponseDTO(medico);
    }

    public List<MedicoResponseDTO> buscarPorEspecialidad(String especialidad) {
        List<Medico> medicos =
        medicoRepository.findByEspecialidadNombreEspecialidadContainingIgnoreCase(especialidad);

        if (medicos.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen médicos con especialidad: " + especialidad);
        }

        return medicos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicoResponseDTO guardarMedico(MedicoRequestDTO dto) {
        Especialidad especialidad = buscarEspecialidadPorId(dto.getEspecialidadId());

        Medico medico = new Medico(
                null,
                dto.getRut(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getTelefono(),
                especialidad
        );

        Medico medicoGuardado = medicoRepository.save(medico);

        log.info("Médico guardado correctamente con id: {}",
                medicoGuardado.getIdMedico());

        return mapToResponseDTO(medicoGuardado);
    }

    public MedicoResponseDTO actualizarMedico(Integer id, MedicoRequestDTO dto) {
        Medico medico = buscarEntidadPorId(id);
        Especialidad especialidad = buscarEspecialidadPorId(dto.getEspecialidadId());

        medico.setRut(dto.getRut());
        medico.setNombre(dto.getNombre());
        medico.setApellido(dto.getApellido());
        medico.setCorreo(dto.getCorreo());
        medico.setTelefono(dto.getTelefono());
        medico.setEspecialidad(especialidad);

        Medico medicoActualizado = medicoRepository.save(medico);

        log.info("Médico id {} actualizado correctamente.", id);

        return mapToResponseDTO(medicoActualizado);
    }

    public void eliminarMedico(Integer id) {
        Medico medico = buscarEntidadPorId(id);
        medicoRepository.delete(medico);

        log.info("Médico id {} eliminado correctamente.", id);
    }
}