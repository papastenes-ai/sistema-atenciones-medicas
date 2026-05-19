package com.duoc.atencionesmedicas.agenda.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.agenda.client.MedicoClient;
import com.duoc.atencionesmedicas.agenda.client.PacienteClient;
import com.duoc.atencionesmedicas.agenda.dto.AgendaDetalleDTO;
import com.duoc.atencionesmedicas.agenda.dto.MedicoDTO;
import com.duoc.atencionesmedicas.agenda.dto.PacienteDTO;
import com.duoc.atencionesmedicas.agenda.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.agenda.model.Agenda;
import com.duoc.atencionesmedicas.agenda.repository.AgendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    public List<Agenda> listarAgendas() {
        return agendaRepository.findAll();
    }

    public Agenda buscarPorId(Integer id) {
        return agendaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Agenda no encontrada con id: " + id));
    }

    public List<Agenda> buscarPorPacienteId(Integer pacienteId) {
        List<Agenda> agendas = agendaRepository.findByPacienteId(pacienteId);

        if (agendas.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen agendas para el paciente id: " + pacienteId);
        }

        return agendas;
    }

    public List<Agenda> buscarPorMedicoId(Integer medicoId) {
        List<Agenda> agendas = agendaRepository.findByMedicoId(medicoId);

        if (agendas.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen agendas para el médico id: " + medicoId);
        }

        return agendas;
    }

    public List<Agenda> buscarPorEstado(String estado) {
        List<Agenda> agendas = agendaRepository.findByEstado(estado);

        if (agendas.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen agendas con estado: " + estado);
        }

        return agendas;
    }

    public Agenda guardarAgenda(Agenda agenda) {
        try {
            return agendaRepository.save(agenda);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la agenda: " + e.getMessage());
        }
    }

    public Agenda actualizarAgenda(Integer id, Agenda agendaActualizada) {
        try {
            Agenda agenda = buscarPorId(id);

            agenda.setFecha(agendaActualizada.getFecha());
            agenda.setHora(agendaActualizada.getHora());
            agenda.setEstado(agendaActualizada.getEstado());
            agenda.setPacienteId(agendaActualizada.getPacienteId());
            agenda.setMedicoId(agendaActualizada.getMedicoId());

            return agendaRepository.save(agenda);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la agenda: " + e.getMessage());
        }
    }

    public void eliminarAgenda(Integer id) {
        try {
            Agenda agenda = buscarPorId(id);
            agendaRepository.delete(agenda);

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la agenda: " + e.getMessage());
        }
    }

    public AgendaDetalleDTO buscarDetallePorId(Integer id) {
        try {
            Agenda agenda = buscarPorId(id);

            PacienteDTO paciente = pacienteClient.obtenerPacientePorId(agenda.getPacienteId());
            MedicoDTO medico = medicoClient.obtenerMedicoPorId(agenda.getMedicoId());

            return new AgendaDetalleDTO(
                    agenda.getIdAgenda(),
                    agenda.getFecha(),
                    agenda.getHora(),
                    agenda.getEstado(),
                    paciente,
                    medico
            );

        } catch (RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el detalle de la agenda: " + e.getMessage());
        }
    }
}