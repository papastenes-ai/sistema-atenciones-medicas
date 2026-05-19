package com.duoc.atencionesmedicas.examen.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.examen.client.AtencionClient;
import com.duoc.atencionesmedicas.examen.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenDetalleDTO;
import com.duoc.atencionesmedicas.examen.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.examen.model.Examen;
import com.duoc.atencionesmedicas.examen.repository.ExamenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamenService {

    private final ExamenRepository examenRepository;
    private final AtencionClient atencionClient;

    public List<Examen> listarExamenes() {
        return examenRepository.findAll();
    }

    public Examen buscarPorId(Integer id) {

        return examenRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Examen no encontrado con id: " + id));
    }

    public List<Examen> buscarPorAtencionId(Integer atencionId) {

        List<Examen> examenes =
                examenRepository.findByAtencionId(atencionId);

        if (examenes.isEmpty()) {

            throw new RecursoNoEncontradoException(
                    "No existen exámenes para la atención id: " + atencionId);
        }

        return examenes;
    }

    public List<Examen> buscarPorNombreExamen(String nombreExamen) {

        List<Examen> examenes =
                examenRepository.findByNombreExamenContainingIgnoreCase(
                        nombreExamen);

        if (examenes.isEmpty()) {

            throw new RecursoNoEncontradoException(
                    "No existen exámenes con nombre: " + nombreExamen);
        }

        return examenes;
    }

    public Examen guardarExamen(Examen examen) {

        try {

            return examenRepository.save(examen);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al guardar el examen: " + e.getMessage());
        }
    }

    public Examen actualizarExamen(Integer id,
                                   Examen examenActualizado) {

        try {

            Examen examen = buscarPorId(id);

            examen.setNombreExamen(
                    examenActualizado.getNombreExamen());

            examen.setResultado(
                    examenActualizado.getResultado());

            examen.setFechaExamen(
                    examenActualizado.getFechaExamen());

            examen.setAtencionId(
                    examenActualizado.getAtencionId());

            return examenRepository.save(examen);

        } catch (RecursoNoEncontradoException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al actualizar el examen: " + e.getMessage());
        }
    }

    public void eliminarExamen(Integer id) {

        try {

            Examen examen = buscarPorId(id);

            examenRepository.delete(examen);

        } catch (RecursoNoEncontradoException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al eliminar el examen: " + e.getMessage());
        }
    }

    public ExamenDetalleDTO buscarDetallePorId(Integer id) {

        try {

            Examen examen = buscarPorId(id);

            AtencionDetalleDTO atencion =
                    atencionClient.obtenerAtencionPorId(
                            examen.getAtencionId());

            return new ExamenDetalleDTO(
                    examen.getIdExamen(),
                    examen.getNombreExamen(),
                    examen.getResultado(),
                    examen.getFechaExamen(),
                    atencion
            );

        } catch (RecursoNoEncontradoException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al obtener el detalle del examen: " + e.getMessage());
        }
    }
}