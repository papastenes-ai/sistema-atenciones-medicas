package com.duoc.atencionesmedicas.agenda.service;

import com.duoc.atencionesmedicas.agenda.client.MedicoClient;
import com.duoc.atencionesmedicas.agenda.client.PacienteClient;
import com.duoc.atencionesmedicas.agenda.dto.AgendaDetalleDTO;
import com.duoc.atencionesmedicas.agenda.dto.AgendaRequestDTO;
import com.duoc.atencionesmedicas.agenda.dto.AgendaResponseDTO;
import com.duoc.atencionesmedicas.agenda.dto.MedicoDTO;
import com.duoc.atencionesmedicas.agenda.dto.PacienteDTO;
import com.duoc.atencionesmedicas.agenda.model.Agenda;
import com.duoc.atencionesmedicas.agenda.repository.AgendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private MedicoClient medicoClient;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    void listarAgendas_deberiaRetornarListaDeAgendas() {
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(1);
        agenda.setFecha("2026-06-24");
        agenda.setHora("10:30");
        agenda.setEstado("PROGRAMADA");
        agenda.setPacienteId(1);
        agenda.setMedicoId(1);

        when(agendaRepository.findAll()).thenReturn(List.of(agenda));

        List<AgendaResponseDTO> resultado = agendaService.listarAgendas();

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdAgenda());
        assertEquals("2026-06-24", resultado.get(0).getFecha());
        assertEquals("10:30", resultado.get(0).getHora());
        assertEquals("PROGRAMADA", resultado.get(0).getEstado());
        assertEquals(1, resultado.get(0).getPacienteId());
        assertEquals(1, resultado.get(0).getMedicoId());

        verify(agendaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarAgenda() {
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(1);
        agenda.setFecha("2026-06-24");
        agenda.setHora("11:00");
        agenda.setEstado("PROGRAMADA");
        agenda.setPacienteId(2);
        agenda.setMedicoId(3);

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agenda));

        AgendaResponseDTO resultado = agendaService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAgenda());
        assertEquals("2026-06-24", resultado.getFecha());
        assertEquals("11:00", resultado.getHora());
        assertEquals("PROGRAMADA", resultado.getEstado());
        assertEquals(2, resultado.getPacienteId());
        assertEquals(3, resultado.getMedicoId());

        verify(agendaRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorPacienteId_deberiaRetornarAgendasFiltradas() {
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(1);
        agenda.setFecha("2026-06-25");
        agenda.setHora("09:00");
        agenda.setEstado("PROGRAMADA");
        agenda.setPacienteId(5);
        agenda.setMedicoId(2);

        when(agendaRepository.findByPacienteId(5)).thenReturn(List.of(agenda));

        List<AgendaResponseDTO> resultado = agendaService.buscarPorPacienteId(5);

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getPacienteId());
        assertEquals("PROGRAMADA", resultado.get(0).getEstado());

        verify(agendaRepository, times(1)).findByPacienteId(5);
    }

    @Test
    void buscarPorMedicoId_deberiaRetornarAgendasFiltradas() {
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(1);
        agenda.setFecha("2026-06-25");
        agenda.setHora("12:00");
        agenda.setEstado("PROGRAMADA");
        agenda.setPacienteId(4);
        agenda.setMedicoId(8);

        when(agendaRepository.findByMedicoId(8)).thenReturn(List.of(agenda));

        List<AgendaResponseDTO> resultado = agendaService.buscarPorMedicoId(8);

        assertEquals(1, resultado.size());
        assertEquals(8, resultado.get(0).getMedicoId());
        assertEquals("12:00", resultado.get(0).getHora());

        verify(agendaRepository, times(1)).findByMedicoId(8);
    }

    @Test
    void buscarPorEstado_deberiaRetornarAgendasFiltradas() {
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(1);
        agenda.setFecha("2026-06-26");
        agenda.setHora("14:00");
        agenda.setEstado("CONFIRMADA");
        agenda.setPacienteId(1);
        agenda.setMedicoId(1);

        when(agendaRepository.findByEstado("CONFIRMADA")).thenReturn(List.of(agenda));

        List<AgendaResponseDTO> resultado = agendaService.buscarPorEstado("CONFIRMADA");

        assertEquals(1, resultado.size());
        assertEquals("CONFIRMADA", resultado.get(0).getEstado());

        verify(agendaRepository, times(1)).findByEstado("CONFIRMADA");
    }

    @Test
    void guardarAgenda_deberiaValidarPacienteMedicoGuardarYRetornarAgenda() {
        AgendaRequestDTO dto = new AgendaRequestDTO();
        dto.setFecha("2026-06-27");
        dto.setHora("15:30");
        dto.setEstado("PROGRAMADA");
        dto.setPacienteId(1);
        dto.setMedicoId(2);

        PacienteDTO pacienteDTO = mock(PacienteDTO.class);
        MedicoDTO medicoDTO = mock(MedicoDTO.class);

        Agenda agendaGuardada = new Agenda();
        agendaGuardada.setIdAgenda(1);
        agendaGuardada.setFecha(dto.getFecha());
        agendaGuardada.setHora(dto.getHora());
        agendaGuardada.setEstado(dto.getEstado());
        agendaGuardada.setPacienteId(dto.getPacienteId());
        agendaGuardada.setMedicoId(dto.getMedicoId());

        when(pacienteClient.obtenerPacientePorId(1)).thenReturn(pacienteDTO);
        when(medicoClient.obtenerMedicoPorId(2)).thenReturn(medicoDTO);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agendaGuardada);

        AgendaResponseDTO resultado = agendaService.guardarAgenda(dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAgenda());
        assertEquals("2026-06-27", resultado.getFecha());
        assertEquals("15:30", resultado.getHora());
        assertEquals("PROGRAMADA", resultado.getEstado());
        assertEquals(1, resultado.getPacienteId());
        assertEquals(2, resultado.getMedicoId());

        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, times(1)).obtenerMedicoPorId(2);
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    void actualizarAgenda_deberiaActualizarYRetornarAgenda() {
        Agenda agendaExistente = new Agenda();
        agendaExistente.setIdAgenda(1);
        agendaExistente.setFecha("2026-06-20");
        agendaExistente.setHora("08:00");
        agendaExistente.setEstado("PROGRAMADA");
        agendaExistente.setPacienteId(1);
        agendaExistente.setMedicoId(1);

        AgendaRequestDTO dto = new AgendaRequestDTO();
        dto.setFecha("2026-06-28");
        dto.setHora("16:00");
        dto.setEstado("CONFIRMADA");
        dto.setPacienteId(2);
        dto.setMedicoId(3);

        PacienteDTO pacienteDTO = mock(PacienteDTO.class);
        MedicoDTO medicoDTO = mock(MedicoDTO.class);

        Agenda agendaActualizada = new Agenda();
        agendaActualizada.setIdAgenda(1);
        agendaActualizada.setFecha(dto.getFecha());
        agendaActualizada.setHora(dto.getHora());
        agendaActualizada.setEstado(dto.getEstado());
        agendaActualizada.setPacienteId(dto.getPacienteId());
        agendaActualizada.setMedicoId(dto.getMedicoId());

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agendaExistente));
        when(pacienteClient.obtenerPacientePorId(2)).thenReturn(pacienteDTO);
        when(medicoClient.obtenerMedicoPorId(3)).thenReturn(medicoDTO);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agendaActualizada);

        AgendaResponseDTO resultado = agendaService.actualizarAgenda(1, dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAgenda());
        assertEquals("2026-06-28", resultado.getFecha());
        assertEquals("16:00", resultado.getHora());
        assertEquals("CONFIRMADA", resultado.getEstado());
        assertEquals(2, resultado.getPacienteId());
        assertEquals(3, resultado.getMedicoId());

        verify(agendaRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(2);
        verify(medicoClient, times(1)).obtenerMedicoPorId(3);
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    void eliminarAgenda_deberiaEliminarAgendaCuandoExiste() {
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(1);
        agenda.setFecha("2026-06-29");
        agenda.setHora("17:00");
        agenda.setEstado("CANCELADA");
        agenda.setPacienteId(1);
        agenda.setMedicoId(1);

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agenda));

        agendaService.eliminarAgenda(1);

        verify(agendaRepository, times(1)).findById(1);
        verify(agendaRepository, times(1)).delete(agenda);
    }

    @Test
    void buscarDetallePorId_deberiaRetornarDetalleAgenda() {
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(1);
        agenda.setFecha("2026-06-30");
        agenda.setHora("18:00");
        agenda.setEstado("CONFIRMADA");
        agenda.setPacienteId(1);
        agenda.setMedicoId(2);

        PacienteDTO pacienteDTO = mock(PacienteDTO.class);
        MedicoDTO medicoDTO = mock(MedicoDTO.class);

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agenda));
        when(pacienteClient.obtenerPacientePorId(1)).thenReturn(pacienteDTO);
        when(medicoClient.obtenerMedicoPorId(2)).thenReturn(medicoDTO);

        AgendaDetalleDTO resultado = agendaService.buscarDetallePorId(1);

        assertNotNull(resultado);

        verify(agendaRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, times(1)).obtenerMedicoPorId(2);
    }
}