package com.duoc.atencionesmedicas.atencion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.atencion.client.MedicoClient;
import com.duoc.atencionesmedicas.atencion.client.PacienteClient;
import com.duoc.atencionesmedicas.atencion.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.atencion.dto.MedicoDTO;
import com.duoc.atencionesmedicas.atencion.dto.PacienteDTO;
import com.duoc.atencionesmedicas.atencion.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.atencion.model.Atencion;
import com.duoc.atencionesmedicas.atencion.repository.AtencionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AtencionService {

    private final AtencionRepository atencionRepository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    public List<Atencion> listarAtenciones() {
        return atencionRepository.findAll();
    }

    public Atencion buscarPorId(Integer id) {
        return atencionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Atención no encontrada con id: " + id));
    }

    public List<Atencion> buscarPorPacienteId(Integer pacienteId) {
        List<Atencion> atenciones = atencionRepository.findByPacienteId(pacienteId);

        if (atenciones.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen atenciones para el paciente id: " + pacienteId);
        }

        return atenciones;
    }

    public List<Atencion> buscarPorMedicoId(Integer medicoId) {
        List<Atencion> atenciones = atencionRepository.findByMedicoId(medicoId);

        if (atenciones.isEmpty()) {
            throw new RecursoNoEncontradoException("No existen atenciones para el médico id: " + medicoId);
        }

        return atenciones;
    }

    public Atencion guardarAtencion(Atencion atencion) {
        try {
            return atencionRepository.save(atencion);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la atención: " + e.getMessage());
        }
    }

    public Atencion actualizarAtencion(Integer id, Atencion atencionActualizada) {
        try {
            Atencion atencion = buscarPorId(id);

            atencion.setFecha(atencionActualizada.getFecha());
            atencion.setHora(atencionActualizada.getHora());
            atencion.setMotivoConsulta(atencionActualizada.getMotivoConsulta());
            atencion.setObservacion(atencionActualizada.getObservacion());
            atencion.setPacienteId(atencionActualizada.getPacienteId());
            atencion.setMedicoId(atencionActualizada.getMedicoId());

            return atencionRepository.save(atencion);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la atención: " + e.getMessage());
        }
    }

    public void eliminarAtencion(Integer id) {
        try {
            Atencion atencion = buscarPorId(id);
            atencionRepository.delete(atencion);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la atención: " + e.getMessage());
        }
    }

    public AtencionDetalleDTO buscarDetallePorId(Integer id) {
        try {
            Atencion atencion = buscarPorId(id);

            PacienteDTO paciente = pacienteClient.obtenerPacientePorId(atencion.getPacienteId());
            MedicoDTO medico = medicoClient.obtenerMedicoPorId(atencion.getMedicoId());

            return new AtencionDetalleDTO(
                    atencion.getIdAtencion(),
                    atencion.getFecha(),
                    atencion.getHora(),
                    atencion.getMotivoConsulta(),
                    atencion.getObservacion(),
                    paciente,
                    medico
            );

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el detalle de la atención: " + e.getMessage());
        }
    }
}