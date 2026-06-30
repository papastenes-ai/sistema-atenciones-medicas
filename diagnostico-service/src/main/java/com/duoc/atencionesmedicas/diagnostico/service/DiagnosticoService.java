package com.duoc.atencionesmedicas.diagnostico.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.diagnostico.client.AtencionClient;
import com.duoc.atencionesmedicas.diagnostico.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoDetalleDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoRequestDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoResponseDTO;
import com.duoc.atencionesmedicas.diagnostico.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.diagnostico.exception.ReglaNegocioException;
import com.duoc.atencionesmedicas.diagnostico.model.Diagnostico;
import com.duoc.atencionesmedicas.diagnostico.repository.DiagnosticoRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final AtencionClient atencionClient;

    private DiagnosticoResponseDTO mapToResponseDTO(Diagnostico diagnostico) {
        return new DiagnosticoResponseDTO(
                diagnostico.getIdDiagnostico(),
                diagnostico.getDescripcion(),
                diagnostico.getTratamiento(),
                diagnostico.getFechaDiagnostico(),
                diagnostico.getAtencionId()
        );
    }

    private Diagnostico buscarEntidadPorId(Integer id) {
        return diagnosticoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Diagnóstico no encontrado con id: " + id));
    }

    private void validarAtencion(Integer atencionId) {
        try {
            atencionClient.obtenerAtencionPorId(atencionId);
            log.info("Atención id {} validada correctamente mediante Feign.", atencionId);

        } catch (FeignException.NotFound e) {
            log.warn("Atención id {} no existe en atencion-service.", atencionId);
            throw new ReglaNegocioException(
                    "No se puede registrar el diagnóstico. La atención con id "
                            + atencionId + " no existe.");

        } catch (FeignException e) {
            log.error("Error al comunicarse con atencion-service: {}", e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo validar la atención. Servicio atención no disponible.");
        }
    }

    public List<DiagnosticoResponseDTO> listarDiagnosticos() {
        return diagnosticoRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public DiagnosticoResponseDTO buscarPorId(Integer id) {
        Diagnostico diagnostico = buscarEntidadPorId(id);
        return mapToResponseDTO(diagnostico);
    }

    public List<DiagnosticoResponseDTO> buscarPorAtencionId(Integer atencionId) {
        List<Diagnostico> diagnosticos =
                diagnosticoRepository.findByAtencionId(atencionId);

        if (diagnosticos.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen diagnósticos para la atención id: " + atencionId);
        }

        return diagnosticos.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public DiagnosticoResponseDTO guardarDiagnostico(DiagnosticoRequestDTO dto) {
        validarAtencion(dto.getAtencionId());

        Diagnostico diagnostico = new Diagnostico(
                null,
                dto.getDescripcion(),
                dto.getTratamiento(),
                dto.getFechaDiagnostico(),
                dto.getAtencionId()
        );

        Diagnostico diagnosticoGuardado =
                diagnosticoRepository.save(diagnostico);

        log.info("Diagnóstico guardado correctamente con id: {}",
                diagnosticoGuardado.getIdDiagnostico());

        return mapToResponseDTO(diagnosticoGuardado);
    }

    public DiagnosticoResponseDTO actualizarDiagnostico(
            Integer id,
            DiagnosticoRequestDTO dto) {

        Diagnostico diagnostico = buscarEntidadPorId(id);

        validarAtencion(dto.getAtencionId());

        diagnostico.setDescripcion(dto.getDescripcion());
        diagnostico.setTratamiento(dto.getTratamiento());
        diagnostico.setFechaDiagnostico(dto.getFechaDiagnostico());
        diagnostico.setAtencionId(dto.getAtencionId());

        Diagnostico diagnosticoActualizado =
                diagnosticoRepository.save(diagnostico);

        log.info("Diagnóstico id {} actualizado correctamente.", id);

        return mapToResponseDTO(diagnosticoActualizado);
    }

    public void eliminarDiagnostico(Integer id) {
        Diagnostico diagnostico = buscarEntidadPorId(id);
        diagnosticoRepository.delete(diagnostico);

        log.info("Diagnóstico id {} eliminado correctamente.", id);
    }

    public DiagnosticoDetalleDTO buscarDetallePorId(Integer id) {
        Diagnostico diagnostico = buscarEntidadPorId(id);

        try {
            AtencionDetalleDTO atencion =
                    atencionClient.obtenerAtencionPorId(diagnostico.getAtencionId());

            log.info("Detalle de diagnóstico id {} construido correctamente.", id);

            return new DiagnosticoDetalleDTO(
                    diagnostico.getIdDiagnostico(),
                    diagnostico.getDescripcion(),
                    diagnostico.getTratamiento(),
                    diagnostico.getFechaDiagnostico(),
                    atencion
            );

        } catch (FeignException.NotFound e) {
            log.warn("No se encontró la atención asociada al diagnóstico id {}.", id);
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle. La atención asociada no existe.");

        } catch (FeignException e) {
            log.error("Error al consultar atencion-service para detalle de diagnóstico: {}",
                    e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle del diagnóstico. Servicio atención no disponible.");
        }
    }
}