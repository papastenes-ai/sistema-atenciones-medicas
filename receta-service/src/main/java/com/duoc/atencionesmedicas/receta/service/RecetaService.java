package com.duoc.atencionesmedicas.receta.service;

import com.duoc.atencionesmedicas.receta.client.DiagnosticoClient;
import com.duoc.atencionesmedicas.receta.dto.DiagnosticoDetalleDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaDetalleDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaRequestDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaResponseDTO;
import com.duoc.atencionesmedicas.receta.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.receta.exception.ReglaNegocioException;
import com.duoc.atencionesmedicas.receta.model.Receta;
import com.duoc.atencionesmedicas.receta.repository.RecetaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final DiagnosticoClient diagnosticoClient;

    private RecetaResponseDTO mapToResponseDTO(Receta receta) {
        return new RecetaResponseDTO(
                receta.getIdReceta(),
                receta.getMedicamento(),
                receta.getDosis(),
                receta.getIndicaciones(),
                receta.getFechaReceta(),
                receta.getDiagnosticoId()
        );
    }

    private Receta buscarEntidadPorId(Integer id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Receta no encontrada con id: " + id));
    }

    private void validarDiagnostico(Integer diagnosticoId) {
        try {
            diagnosticoClient.obtenerDiagnosticoPorId(diagnosticoId);
            log.info("Diagnóstico id {} validado correctamente mediante Feign.", diagnosticoId);

        } catch (FeignException.NotFound e) {
            log.warn("Diagnóstico id {} no existe en diagnostico-service.", diagnosticoId);
            throw new ReglaNegocioException(
                    "No se puede registrar la receta. El diagnóstico con id "
                            + diagnosticoId + " no existe.");

        } catch (FeignException e) {
            log.error("Error al comunicarse con diagnostico-service: {}", e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo validar el diagnóstico. Servicio diagnóstico no disponible.");
        }
    }

    public List<RecetaResponseDTO> listarRecetas() {
        return recetaRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public RecetaResponseDTO buscarPorId(Integer id) {
        Receta receta = buscarEntidadPorId(id);
        return mapToResponseDTO(receta);
    }

    public List<RecetaResponseDTO> buscarPorDiagnosticoId(Integer diagnosticoId) {
        List<Receta> recetas = recetaRepository.findByDiagnosticoId(diagnosticoId);

        if (recetas.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen recetas para el diagnóstico id: " + diagnosticoId);
        }

        return recetas.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public RecetaResponseDTO guardarReceta(RecetaRequestDTO dto) {
        validarDiagnostico(dto.getDiagnosticoId());

        Receta receta = new Receta(
                null,
                dto.getMedicamento(),
                dto.getDosis(),
                dto.getIndicaciones(),
                dto.getFechaReceta(),
                dto.getDiagnosticoId()
        );

        Receta recetaGuardada = recetaRepository.save(receta);

        log.info("Receta guardada correctamente con id: {}",
                recetaGuardada.getIdReceta());

        return mapToResponseDTO(recetaGuardada);
    }

    public RecetaResponseDTO actualizarReceta(Integer id, RecetaRequestDTO dto) {
        Receta receta = buscarEntidadPorId(id);

        validarDiagnostico(dto.getDiagnosticoId());

        receta.setMedicamento(dto.getMedicamento());
        receta.setDosis(dto.getDosis());
        receta.setIndicaciones(dto.getIndicaciones());
        receta.setFechaReceta(dto.getFechaReceta());
        receta.setDiagnosticoId(dto.getDiagnosticoId());

        Receta recetaActualizada = recetaRepository.save(receta);

        log.info("Receta id {} actualizada correctamente.", id);

        return mapToResponseDTO(recetaActualizada);
    }

    public void eliminarReceta(Integer id) {
        Receta receta = buscarEntidadPorId(id);
        recetaRepository.delete(receta);

        log.info("Receta id {} eliminada correctamente.", id);
    }

    public RecetaDetalleDTO buscarDetallePorId(Integer id) {
        Receta receta = buscarEntidadPorId(id);

        try {
            DiagnosticoDetalleDTO diagnostico =
                    diagnosticoClient.obtenerDiagnosticoPorId(receta.getDiagnosticoId());

            log.info("Detalle de receta id {} construido correctamente.", id);

            return new RecetaDetalleDTO(
                    receta.getIdReceta(),
                    receta.getMedicamento(),
                    receta.getDosis(),
                    receta.getIndicaciones(),
                    receta.getFechaReceta(),
                    diagnostico
            );

        } catch (FeignException.NotFound e) {
            log.warn("No se encontró el diagnóstico asociado a la receta id {}.", id);
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle. El diagnóstico asociado no existe.");

        } catch (FeignException e) {
            log.error("Error al consultar diagnostico-service para detalle de receta: {}",
                    e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle de la receta. Servicio diagnóstico no disponible.");
        }
    }
}