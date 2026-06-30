package com.duoc.atencionesmedicas.examen.service;

import com.duoc.atencionesmedicas.examen.client.AtencionClient;
import com.duoc.atencionesmedicas.examen.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenDetalleDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenRequestDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenResponseDTO;
import com.duoc.atencionesmedicas.examen.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.examen.exception.ReglaNegocioException;
import com.duoc.atencionesmedicas.examen.model.Examen;
import com.duoc.atencionesmedicas.examen.repository.ExamenRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamenService {

    private final ExamenRepository examenRepository;
    private final AtencionClient atencionClient;

    private ExamenResponseDTO mapToResponseDTO(Examen examen) {
        return new ExamenResponseDTO(
                examen.getIdExamen(),
                examen.getNombreExamen(),
                examen.getResultado(),
                examen.getFechaExamen(),
                examen.getAtencionId()
        );
    }

    private Examen buscarEntidadPorId(Integer id) {
        return examenRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Examen no encontrado con id: " + id));
    }

    private void validarAtencion(Integer atencionId) {
        try {
            atencionClient.obtenerAtencionPorId(atencionId);
            log.info("Atención id {} validada correctamente mediante Feign.", atencionId);

        } catch (FeignException.NotFound e) {
            log.warn("Atención id {} no existe en atencion-service.", atencionId);
            throw new ReglaNegocioException(
                    "No se puede registrar el examen. La atención con id "
                            + atencionId + " no existe.");

        } catch (FeignException e) {
            log.error("Error al comunicarse con atencion-service: {}", e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo validar la atención. Servicio atención no disponible.");
        }
    }

    public List<ExamenResponseDTO> listarExamenes() {
        return examenRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ExamenResponseDTO buscarPorId(Integer id) {
        Examen examen = buscarEntidadPorId(id);
        return mapToResponseDTO(examen);
    }

    public List<ExamenResponseDTO> buscarPorAtencionId(Integer atencionId) {
        List<Examen> examenes = examenRepository.findByAtencionId(atencionId);

        if (examenes.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen exámenes para la atención id: " + atencionId);
        }

        return examenes.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ExamenResponseDTO> buscarPorNombreExamen(String nombreExamen) {
        List<Examen> examenes =
                examenRepository.findByNombreExamenContainingIgnoreCase(nombreExamen);

        if (examenes.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen exámenes con nombre: " + nombreExamen);
        }

        return examenes.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public ExamenResponseDTO guardarExamen(ExamenRequestDTO dto) {
        validarAtencion(dto.getAtencionId());

        Examen examen = new Examen(
                null,
                dto.getNombreExamen(),
                dto.getResultado(),
                dto.getFechaExamen(),
                dto.getAtencionId()
        );

        Examen examenGuardado = examenRepository.save(examen);

        log.info("Examen guardado correctamente con id: {}",
                examenGuardado.getIdExamen());

        return mapToResponseDTO(examenGuardado);
    }

    public ExamenResponseDTO actualizarExamen(Integer id, ExamenRequestDTO dto) {
        Examen examen = buscarEntidadPorId(id);

        validarAtencion(dto.getAtencionId());

        examen.setNombreExamen(dto.getNombreExamen());
        examen.setResultado(dto.getResultado());
        examen.setFechaExamen(dto.getFechaExamen());
        examen.setAtencionId(dto.getAtencionId());

        Examen examenActualizado = examenRepository.save(examen);

        log.info("Examen id {} actualizado correctamente.", id);

        return mapToResponseDTO(examenActualizado);
    }

    public void eliminarExamen(Integer id) {
        Examen examen = buscarEntidadPorId(id);
        examenRepository.delete(examen);

        log.info("Examen id {} eliminado correctamente.", id);
    }

    public ExamenDetalleDTO buscarDetallePorId(Integer id) {
        Examen examen = buscarEntidadPorId(id);

        try {
            AtencionDetalleDTO atencion =
                    atencionClient.obtenerAtencionPorId(examen.getAtencionId());

            log.info("Detalle de examen id {} construido correctamente.", id);

            return new ExamenDetalleDTO(
                    examen.getIdExamen(),
                    examen.getNombreExamen(),
                    examen.getResultado(),
                    examen.getFechaExamen(),
                    atencion
            );

        } catch (FeignException.NotFound e) {
            log.warn("No se encontró la atención asociada al examen id {}.", id);
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle. La atención asociada no existe.");

        } catch (FeignException e) {
            log.error("Error al consultar atencion-service para detalle de examen: {}",
                    e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle del examen. Servicio atención no disponible.");
        }
    }
}