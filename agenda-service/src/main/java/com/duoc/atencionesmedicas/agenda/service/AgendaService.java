package com.duoc.atencionesmedicas.agenda.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.agenda.client.MedicoClient;
import com.duoc.atencionesmedicas.agenda.client.PacienteClient;
import com.duoc.atencionesmedicas.agenda.dto.AgendaDetalleDTO;
import com.duoc.atencionesmedicas.agenda.dto.AgendaRequestDTO;
import com.duoc.atencionesmedicas.agenda.dto.AgendaResponseDTO;
import com.duoc.atencionesmedicas.agenda.dto.MedicoDTO;
import com.duoc.atencionesmedicas.agenda.dto.PacienteDTO;
import com.duoc.atencionesmedicas.agenda.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.agenda.exception.ReglaNegocioException;
import com.duoc.atencionesmedicas.agenda.model.Agenda;
import com.duoc.atencionesmedicas.agenda.repository.AgendaRepository;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final PacienteClient pacienteClient;
    private final MedicoClient medicoClient;

    private AgendaResponseDTO mapToResponseDTO(Agenda agenda) {
        return new AgendaResponseDTO(
                agenda.getIdAgenda(),
                agenda.getFecha(),
                agenda.getHora(),
                agenda.getEstado(),
                agenda.getPacienteId(),
                agenda.getMedicoId()
        );
    }

    private Agenda buscarEntidadPorId(Integer id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Agenda no encontrada con id: " + id));
    }

    private void validarPaciente(Integer pacienteId) {
        try {
            pacienteClient.obtenerPacientePorId(pacienteId);
            log.info("Paciente id {} validado correctamente mediante Feign.", pacienteId);

        } catch (FeignException.NotFound e) {
            log.warn("Paciente id {} no existe en paciente-service.", pacienteId);
            throw new ReglaNegocioException(
                    "No se puede registrar la agenda. El paciente con id "
                            + pacienteId + " no existe.");

        } catch (FeignException e) {
            log.error("Error al comunicarse con paciente-service: {}", e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo validar el paciente. Servicio paciente no disponible.");
        }
    }

    private void validarMedico(Integer medicoId) {
        try {
            medicoClient.obtenerMedicoPorId(medicoId);
            log.info("Médico id {} validado correctamente mediante Feign.", medicoId);

        } catch (FeignException.NotFound e) {
            log.warn("Médico id {} no existe en medico-service.", medicoId);
            throw new ReglaNegocioException(
                    "No se puede registrar la agenda. El médico con id "
                            + medicoId + " no existe.");

        } catch (FeignException e) {
            log.error("Error al comunicarse con medico-service: {}", e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo validar el médico. Servicio médico no disponible.");
        }
    }

    private void validarDatosRemotos(Integer pacienteId, Integer medicoId) {
        validarPaciente(pacienteId);
        validarMedico(medicoId);
    }

    public List<AgendaResponseDTO> listarAgendas() {
        return agendaRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AgendaResponseDTO buscarPorId(Integer id) {
        Agenda agenda = buscarEntidadPorId(id);
        return mapToResponseDTO(agenda);
    }

    public List<AgendaResponseDTO> buscarPorPacienteId(Integer pacienteId) {
        List<Agenda> agendas = agendaRepository.findByPacienteId(pacienteId);

        if (agendas.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen agendas para el paciente id: " + pacienteId);
        }

        return agendas.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendaResponseDTO> buscarPorMedicoId(Integer medicoId) {
        List<Agenda> agendas = agendaRepository.findByMedicoId(medicoId);

        if (agendas.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen agendas para el médico id: " + medicoId);
        }

        return agendas.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AgendaResponseDTO> buscarPorEstado(String estado) {
        List<Agenda> agendas = agendaRepository.findByEstado(estado);

        if (agendas.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen agendas con estado: " + estado);
        }

        return agendas.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AgendaResponseDTO guardarAgenda(AgendaRequestDTO dto) {
        validarDatosRemotos(dto.getPacienteId(), dto.getMedicoId());

        Agenda agenda = new Agenda(
                null,
                dto.getFecha(),
                dto.getHora(),
                dto.getEstado(),
                dto.getPacienteId(),
                dto.getMedicoId()
        );

        Agenda agendaGuardada = agendaRepository.save(agenda);

        log.info("Agenda guardada correctamente con id: {}",
                agendaGuardada.getIdAgenda());

        return mapToResponseDTO(agendaGuardada);
    }

    public AgendaResponseDTO actualizarAgenda(Integer id, AgendaRequestDTO dto) {
        Agenda agenda = buscarEntidadPorId(id);

        validarDatosRemotos(dto.getPacienteId(), dto.getMedicoId());

        agenda.setFecha(dto.getFecha());
        agenda.setHora(dto.getHora());
        agenda.setEstado(dto.getEstado());
        agenda.setPacienteId(dto.getPacienteId());
        agenda.setMedicoId(dto.getMedicoId());

        Agenda agendaActualizada = agendaRepository.save(agenda);

        log.info("Agenda id {} actualizada correctamente.", id);

        return mapToResponseDTO(agendaActualizada);
    }

    public void eliminarAgenda(Integer id) {
        Agenda agenda = buscarEntidadPorId(id);
        agendaRepository.delete(agenda);

        log.info("Agenda id {} eliminada correctamente.", id);
    }

    public AgendaDetalleDTO buscarDetallePorId(Integer id) {
        Agenda agenda = buscarEntidadPorId(id);

        try {
            PacienteDTO paciente = pacienteClient.obtenerPacientePorId(agenda.getPacienteId());
            MedicoDTO medico = medicoClient.obtenerMedicoPorId(agenda.getMedicoId());

            log.info("Detalle de agenda id {} construido correctamente.", id);

            return new AgendaDetalleDTO(
                    agenda.getIdAgenda(),
                    agenda.getFecha(),
                    agenda.getHora(),
                    agenda.getEstado(),
                    paciente,
                    medico
            );

        } catch (FeignException.NotFound e) {
            log.warn("No se encontró información remota para construir el detalle de agenda id {}.", id);
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle. El paciente o médico asociado no existe.");

        } catch (FeignException e) {
            log.error("Error al consultar servicios remotos para detalle de agenda: {}", e.getMessage());
            throw new ReglaNegocioException(
                    "No se pudo construir el detalle de la agenda. Servicio remoto no disponible.");
        }
    }
}