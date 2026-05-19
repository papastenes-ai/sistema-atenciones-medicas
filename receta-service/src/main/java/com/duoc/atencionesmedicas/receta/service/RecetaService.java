package com.duoc.atencionesmedicas.receta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.receta.client.DiagnosticoClient;
import com.duoc.atencionesmedicas.receta.dto.DiagnosticoDetalleDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaDetalleDTO;
import com.duoc.atencionesmedicas.receta.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.receta.model.Receta;
import com.duoc.atencionesmedicas.receta.repository.RecetaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final DiagnosticoClient diagnosticoClient;

    public List<Receta> listarRecetas() {
        return recetaRepository.findAll();
    }

    public Receta buscarPorId(Integer id) {
        return recetaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Receta no encontrada con id: " + id));
    }

    public List<Receta> buscarPorDiagnosticoId(Integer diagnosticoId) {
        List<Receta> recetas = recetaRepository.findByDiagnosticoId(diagnosticoId);

        if (recetas.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen recetas para el diagnóstico id: " + diagnosticoId);
        }

        return recetas;
    }

    public Receta guardarReceta(Receta receta) {
        try {
            return recetaRepository.save(receta);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la receta: " + e.getMessage());
        }
    }

    public Receta actualizarReceta(Integer id, Receta recetaActualizada) {
        try {
            Receta receta = buscarPorId(id);

            receta.setMedicamento(recetaActualizada.getMedicamento());
            receta.setDosis(recetaActualizada.getDosis());
            receta.setIndicaciones(recetaActualizada.getIndicaciones());
            receta.setFechaReceta(recetaActualizada.getFechaReceta());
            receta.setDiagnosticoId(recetaActualizada.getDiagnosticoId());

            return recetaRepository.save(receta);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la receta: " + e.getMessage());
        }
    }

    public void eliminarReceta(Integer id) {
        try {
            Receta receta = buscarPorId(id);
            recetaRepository.delete(receta);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la receta: " + e.getMessage());
        }
    }

    public RecetaDetalleDTO buscarDetallePorId(Integer id) {
        try {
            Receta receta = buscarPorId(id);

            DiagnosticoDetalleDTO diagnostico =
                    diagnosticoClient.obtenerDiagnosticoPorId(receta.getDiagnosticoId());

            return new RecetaDetalleDTO(
                    receta.getIdReceta(),
                    receta.getMedicamento(),
                    receta.getDosis(),
                    receta.getIndicaciones(),
                    receta.getFechaReceta(),
                    diagnostico
            );

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el detalle de la receta: " + e.getMessage());
        }
    }
}