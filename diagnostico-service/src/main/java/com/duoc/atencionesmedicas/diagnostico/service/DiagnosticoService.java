package com.duoc.atencionesmedicas.diagnostico.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.diagnostico.client.AtencionClient;
import com.duoc.atencionesmedicas.diagnostico.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoDetalleDTO;
import com.duoc.atencionesmedicas.diagnostico.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.diagnostico.model.Diagnostico;
import com.duoc.atencionesmedicas.diagnostico.repository.DiagnosticoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiagnosticoService {

    private final DiagnosticoRepository diagnosticoRepository;
    private final AtencionClient atencionClient;

    public List<Diagnostico> listarDiagnosticos() {
        return diagnosticoRepository.findAll();
    }

    public Diagnostico buscarPorId(Integer id) {
        return diagnosticoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Diagnóstico no encontrado con id: " + id));
    }

    public List<Diagnostico> buscarPorAtencionId(Integer atencionId) {

        List<Diagnostico> diagnosticos =
                diagnosticoRepository.findByAtencionId(atencionId);

        if (diagnosticos.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen diagnósticos para la atención id: " + atencionId);
        }

        return diagnosticos;
    }

    public Diagnostico guardarDiagnostico(Diagnostico diagnostico) {

        try {
            return diagnosticoRepository.save(diagnostico);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al guardar el diagnóstico: " + e.getMessage());
        }
    }

    public Diagnostico actualizarDiagnostico(Integer id,
                                             Diagnostico diagnosticoActualizado) {

        try {

            Diagnostico diagnostico = buscarPorId(id);

            diagnostico.setDescripcion(
                    diagnosticoActualizado.getDescripcion());

            diagnostico.setTratamiento(
                    diagnosticoActualizado.getTratamiento());

            diagnostico.setFechaDiagnostico(
                    diagnosticoActualizado.getFechaDiagnostico());

            diagnostico.setAtencionId(
                    diagnosticoActualizado.getAtencionId());

            return diagnosticoRepository.save(diagnostico);

        } catch (RecursoNoEncontradoException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al actualizar el diagnóstico: " + e.getMessage());
        }
    }

    public void eliminarDiagnostico(Integer id) {

        try {

            Diagnostico diagnostico = buscarPorId(id);

            diagnosticoRepository.delete(diagnostico);

        } catch (RecursoNoEncontradoException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al eliminar el diagnóstico: " + e.getMessage());
        }
    }

    public DiagnosticoDetalleDTO buscarDetallePorId(Integer id) {

        try {

            Diagnostico diagnostico = buscarPorId(id);

            AtencionDetalleDTO atencion =
                    atencionClient.obtenerAtencionPorId(
                            diagnostico.getAtencionId());

            return new DiagnosticoDetalleDTO(
                    diagnostico.getIdDiagnostico(),
                    diagnostico.getDescripcion(),
                    diagnostico.getTratamiento(),
                    diagnostico.getFechaDiagnostico(),
                    atencion
            );

        } catch (RecursoNoEncontradoException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al obtener el detalle del diagnóstico: " + e.getMessage());
        }
    }
}