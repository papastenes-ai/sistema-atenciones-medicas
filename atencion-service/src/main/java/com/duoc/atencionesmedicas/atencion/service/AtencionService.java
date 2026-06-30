package com.duoc.atencionesmedicas.atencion.service;

import com.duoc.atencionesmedicas.atencion.client.MedicoClient;
import com.duoc.atencionesmedicas.atencion.client.PacienteClient;
import com.duoc.atencionesmedicas.atencion.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.atencion.dto.AtencionRequestDTO;
import com.duoc.atencionesmedicas.atencion.dto.AtencionResponseDTO;
import com.duoc.atencionesmedicas.atencion.dto.MedicoDTO;
import com.duoc.atencionesmedicas.atencion.dto.PacienteDTO;
import com.duoc.atencionesmedicas.atencion.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.atencion.exception.ReglaNegocioException;
import com.duoc.atencionesmedicas.atencion.model.Atencion;
import com.duoc.atencionesmedicas.atencion.repository.AtencionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    private AtencionResponseDTO mapToResponseDTO(Atencion atencion) {
        return new AtencionResponseDTO(
                atencion.getIdAtencion(),
                atencion.getFecha(),
                atencion.getHora(),
                atencion.getMotivoConsulta(),
                atencion.getObservacion(),
                atencion.getPacienteId(),
                atencion.getMedicoId()
        );
    }

    private void validarPaciente(Integer pacienteId) {
        try {
            pacienteClient.obtenerPacientePorId(pacienteId);
            log.info("Paciente id {} validado correctamente mediante Feign.", pacienteId);

        } catch (FeignException.NotFound e) {
            log.warn("Paciente id {} no existe en paciente-service.", pacienteId);
            throw new ReglaNegocioException("No se puede registrar la atención. El paciente con id "
                    + pacienteId + " no existe.");

        } catch (FeignException e) {
            log.error("Error al comunicarse con paciente-service: {}", e.getMessage());
            throw new ReglaNegocioException("No se pudo validar el paciente. Servicio paciente no disponible.");
        }
    }

    private void validarMedico(Integer medicoId) {
        try {
            medicoClient.obtenerMedicoPorId(medicoId);
            log.info("Médico id {} validado correctamente mediante Feign.", medicoId);

        } catch (FeignException.NotFound e) {
            log.warn("Médico id {} no existe en medico-service.", medicoId);
            throw new ReglaNegocioException("No se puede registrar la atención. El médico con id "
                    + medicoId + " no existe.");

        } catch (FeignException e) {
            log.error("Error al comunicarse con medico-service: {}", e.getMessage());
            throw new ReglaNegocioException("No se pudo validar el médico. Servicio médico no disponible.");
        }
    }

    private void validarDatosRemotos(Integer pacienteId, Integer medicoId) {
        validarPaciente(pacienteId);
        validarMedico(medicoId);
    }

    public List<AtencionResponseDTO> listarAtenciones() {
        return atencionRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AtencionResponseDTO buscarPorId(Integer id) {
        Atencion atencion = atencionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Atención no encontrada con id: " + id));

        return mapToResponseDTO(atencion);
    }

    private Atencion buscarEntidadPorId(Integer id) {
        return atencionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Atención no encontrada con id: " + id));
    }

    public List<AtencionResponseDTO> buscarPorPacienteId(Integer pacienteId) {
        List<Atencion> atenciones = atencionRepository.findByPacienteId(pacienteId);

        if (atenciones.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen atenciones para el paciente id: " + pacienteId);
        }

        return atenciones.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AtencionResponseDTO> buscarPorMedicoId(Integer medicoId) {
        List<Atencion> atenciones = atencionRepository.findByMedicoId(medicoId);

        if (atenciones.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen atenciones para el médico id: " + medicoId);
        }

        return atenciones.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AtencionResponseDTO guardarAtencion(AtencionRequestDTO dto) {
        validarDatosRemotos(dto.getPacienteId(), dto.getMedicoId());

        Atencion atencion = new Atencion(
                null,
                dto.getFecha(),
                dto.getHora(),
                dto.getMotivoConsulta(),
                dto.getObservacion(),
                dto.getPacienteId(),
                dto.getMedicoId()
        );

        Atencion atencionGuardada = atencionRepository.save(atencion);

        log.info("Atención guardada correctamente con id: {}",
                atencionGuardada.getIdAtencion());

        return mapToResponseDTO(atencionGuardada);
    }

    public AtencionResponseDTO actualizarAtencion(Integer id, AtencionRequestDTO dto) {
        Atencion atencion = buscarEntidadPorId(id);

        validarDatosRemotos(dto.getPacienteId(), dto.getMedicoId());

        atencion.setFecha(dto.getFecha());
        atencion.setHora(dto.getHora());
        atencion.setMotivoConsulta(dto.getMotivoConsulta());
        atencion.setObservacion(dto.getObservacion());
        atencion.setPacienteId(dto.getPacienteId());
        atencion.setMedicoId(dto.getMedicoId());

        Atencion atencionActualizada = atencionRepository.save(atencion);

        log.info("Atención id {} actualizada correctamente.", id);

        return mapToResponseDTO(atencionActualizada);
    }

    public void eliminarAtencion(Integer id) {
        Atencion atencion = buscarEntidadPorId(id);
        atencionRepository.delete(atencion);

        log.info("Atención id {} eliminada correctamente.", id);
    }

    public AtencionDetalleDTO buscarDetallePorId(Integer id) {
        Atencion atencion = buscarEntidadPorId(id);

        try {
            PacienteDTO paciente = pacienteClient.obtenerPacientePorId(atencion.getPacienteId());
            MedicoDTO medico = medicoClient.obtenerMedicoPorId(atencion.getMedicoId());

            log.info("Detalle de atención id {} construido correctamente.", id);

            return new AtencionDetalleDTO(
                    atencion.getIdAtencion(),
                    atencion.getFecha(),
                    atencion.getHora(),
                    atencion.getMotivoConsulta(),
                    atencion.getObservacion(),
                    paciente,
                    medico
            );

        } catch (FeignException.NotFound e) {
            log.warn("No se encontró información remota para construir el detalle de atención id {}.", id);
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle. El paciente o médico asociado no existe.");

        } catch (FeignException e) {
            log.error("Error al consultar servicios remotos para detalle de atención: {}", e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle de la atención. Servicio remoto no disponible.");
        }
    }
}