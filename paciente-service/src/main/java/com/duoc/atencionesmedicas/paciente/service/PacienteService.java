package com.duoc.atencionesmedicas.paciente.service;

import com.duoc.atencionesmedicas.paciente.dto.PacienteRequestDTO;
import com.duoc.atencionesmedicas.paciente.dto.PacienteResponseDTO;
import com.duoc.atencionesmedicas.paciente.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.paciente.model.Paciente;
import com.duoc.atencionesmedicas.paciente.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    private PacienteResponseDTO mapToResponseDTO(Paciente paciente) {
        return new PacienteResponseDTO(
                paciente.getIdPaciente(),
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getRut(),
                paciente.getEdad(),
                paciente.getCorreo(),
                paciente.getTelefono()
        );
    }

    private Paciente buscarEntidadPorId(Integer id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Paciente no encontrado con id: " + id));
    }

    public List<PacienteResponseDTO> listarPacientes() {
        return pacienteRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public PacienteResponseDTO buscarPorId(Integer id) {
        Paciente paciente = buscarEntidadPorId(id);
        return mapToResponseDTO(paciente);
    }

    public PacienteResponseDTO buscarPorRut(String rut) {
        Paciente paciente = pacienteRepository.findByRut(rut)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Paciente no encontrado con rut: " + rut));

        return mapToResponseDTO(paciente);
    }

    public PacienteResponseDTO guardarPaciente(PacienteRequestDTO dto) {
        Paciente paciente = new Paciente(
                null,
                dto.getNombre(),
                dto.getApellido(),
                dto.getRut(),
                dto.getEdad(),
                dto.getCorreo(),
                dto.getTelefono()
        );

        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        log.info("Paciente guardado correctamente con id: {}",
                pacienteGuardado.getIdPaciente());

        return mapToResponseDTO(pacienteGuardado);
    }

    public PacienteResponseDTO actualizarPaciente(Integer id, PacienteRequestDTO dto) {
        Paciente paciente = buscarEntidadPorId(id);

        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setRut(dto.getRut());
        paciente.setEdad(dto.getEdad());
        paciente.setCorreo(dto.getCorreo());
        paciente.setTelefono(dto.getTelefono());

        Paciente pacienteActualizado = pacienteRepository.save(paciente);

        log.info("Paciente id {} actualizado correctamente.", id);

        return mapToResponseDTO(pacienteActualizado);
    }

    public void eliminarPaciente(Integer id) {
        Paciente paciente = buscarEntidadPorId(id);
        pacienteRepository.delete(paciente);

        log.info("Paciente id {} eliminado correctamente.", id);
    }
}