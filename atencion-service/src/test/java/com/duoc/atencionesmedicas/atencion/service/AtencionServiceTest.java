package com.duoc.atencionesmedicas.atencion.service;

import com.duoc.atencionesmedicas.atencion.client.MedicoClient;
import com.duoc.atencionesmedicas.atencion.client.PacienteClient;
import com.duoc.atencionesmedicas.atencion.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.atencion.dto.AtencionRequestDTO;
import com.duoc.atencionesmedicas.atencion.dto.AtencionResponseDTO;
import com.duoc.atencionesmedicas.atencion.dto.MedicoDTO;
import com.duoc.atencionesmedicas.atencion.dto.PacienteDTO;
import com.duoc.atencionesmedicas.atencion.model.Atencion;
import com.duoc.atencionesmedicas.atencion.repository.AtencionRepository;
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
class AtencionServiceTest {

    @Mock
    private AtencionRepository atencionRepository;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private MedicoClient medicoClient;

    @InjectMocks
    private AtencionService atencionService;

    @Test
    void listarAtenciones_deberiaRetornarListaDeAtenciones() {
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-24");
        atencion.setHora("10:30");
        atencion.setMotivoConsulta("Dolor de cabeza");
        atencion.setObservacion("Paciente estable");
        atencion.setPacienteId(1);
        atencion.setMedicoId(2);

        when(atencionRepository.findAll()).thenReturn(List.of(atencion));

        List<AtencionResponseDTO> resultado = atencionService.listarAtenciones();

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdAtencion());
        assertEquals("2026-06-24", resultado.get(0).getFecha());
        assertEquals("10:30", resultado.get(0).getHora());
        assertEquals("Dolor de cabeza", resultado.get(0).getMotivoConsulta());
        assertEquals("Paciente estable", resultado.get(0).getObservacion());
        assertEquals(1, resultado.get(0).getPacienteId());
        assertEquals(2, resultado.get(0).getMedicoId());

        verify(atencionRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarAtencion() {
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-24");
        atencion.setHora("11:00");
        atencion.setMotivoConsulta("Control médico");
        atencion.setObservacion("Sin observaciones");
        atencion.setPacienteId(3);
        atencion.setMedicoId(4);

        when(atencionRepository.findById(1)).thenReturn(Optional.of(atencion));

        AtencionResponseDTO resultado = atencionService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAtencion());
        assertEquals("2026-06-24", resultado.getFecha());
        assertEquals("11:00", resultado.getHora());
        assertEquals("Control médico", resultado.getMotivoConsulta());
        assertEquals("Sin observaciones", resultado.getObservacion());
        assertEquals(3, resultado.getPacienteId());
        assertEquals(4, resultado.getMedicoId());

        verify(atencionRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorPacienteId_deberiaRetornarAtencionesFiltradas() {
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-25");
        atencion.setHora("09:00");
        atencion.setMotivoConsulta("Dolor abdominal");
        atencion.setObservacion("Derivar a exámenes");
        atencion.setPacienteId(5);
        atencion.setMedicoId(2);

        when(atencionRepository.findByPacienteId(5)).thenReturn(List.of(atencion));

        List<AtencionResponseDTO> resultado = atencionService.buscarPorPacienteId(5);

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getPacienteId());
        assertEquals("Dolor abdominal", resultado.get(0).getMotivoConsulta());

        verify(atencionRepository, times(1)).findByPacienteId(5);
    }

    @Test
    void buscarPorMedicoId_deberiaRetornarAtencionesFiltradas() {
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-25");
        atencion.setHora("12:00");
        atencion.setMotivoConsulta("Chequeo general");
        atencion.setObservacion("Paciente sano");
        atencion.setPacienteId(4);
        atencion.setMedicoId(8);

        when(atencionRepository.findByMedicoId(8)).thenReturn(List.of(atencion));

        List<AtencionResponseDTO> resultado = atencionService.buscarPorMedicoId(8);

        assertEquals(1, resultado.size());
        assertEquals(8, resultado.get(0).getMedicoId());
        assertEquals("12:00", resultado.get(0).getHora());

        verify(atencionRepository, times(1)).findByMedicoId(8);
    }

    @Test
    void guardarAtencion_deberiaValidarPacienteMedicoGuardarYRetornarAtencion() {
        AtencionRequestDTO dto = new AtencionRequestDTO();
        dto.setFecha("2026-06-27");
        dto.setHora("15:30");
        dto.setMotivoConsulta("Dolor de garganta");
        dto.setObservacion("Recetar medicamento");
        dto.setPacienteId(1);
        dto.setMedicoId(2);

        PacienteDTO pacienteDTO = mock(PacienteDTO.class);
        MedicoDTO medicoDTO = mock(MedicoDTO.class);

        Atencion atencionGuardada = new Atencion();
        atencionGuardada.setIdAtencion(1);
        atencionGuardada.setFecha(dto.getFecha());
        atencionGuardada.setHora(dto.getHora());
        atencionGuardada.setMotivoConsulta(dto.getMotivoConsulta());
        atencionGuardada.setObservacion(dto.getObservacion());
        atencionGuardada.setPacienteId(dto.getPacienteId());
        atencionGuardada.setMedicoId(dto.getMedicoId());

        when(pacienteClient.obtenerPacientePorId(1)).thenReturn(pacienteDTO);
        when(medicoClient.obtenerMedicoPorId(2)).thenReturn(medicoDTO);
        when(atencionRepository.save(any(Atencion.class))).thenReturn(atencionGuardada);

        AtencionResponseDTO resultado = atencionService.guardarAtencion(dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAtencion());
        assertEquals("2026-06-27", resultado.getFecha());
        assertEquals("15:30", resultado.getHora());
        assertEquals("Dolor de garganta", resultado.getMotivoConsulta());
        assertEquals("Recetar medicamento", resultado.getObservacion());
        assertEquals(1, resultado.getPacienteId());
        assertEquals(2, resultado.getMedicoId());

        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, times(1)).obtenerMedicoPorId(2);
        verify(atencionRepository, times(1)).save(any(Atencion.class));
    }

    @Test
    void actualizarAtencion_deberiaActualizarYRetornarAtencion() {
        Atencion atencionExistente = new Atencion();
        atencionExistente.setIdAtencion(1);
        atencionExistente.setFecha("2026-06-20");
        atencionExistente.setHora("08:00");
        atencionExistente.setMotivoConsulta("Motivo antiguo");
        atencionExistente.setObservacion("Observación antigua");
        atencionExistente.setPacienteId(1);
        atencionExistente.setMedicoId(1);

        AtencionRequestDTO dto = new AtencionRequestDTO();
        dto.setFecha("2026-06-28");
        dto.setHora("16:00");
        dto.setMotivoConsulta("Motivo actualizado");
        dto.setObservacion("Observación actualizada");
        dto.setPacienteId(2);
        dto.setMedicoId(3);

        PacienteDTO pacienteDTO = mock(PacienteDTO.class);
        MedicoDTO medicoDTO = mock(MedicoDTO.class);

        Atencion atencionActualizada = new Atencion();
        atencionActualizada.setIdAtencion(1);
        atencionActualizada.setFecha(dto.getFecha());
        atencionActualizada.setHora(dto.getHora());
        atencionActualizada.setMotivoConsulta(dto.getMotivoConsulta());
        atencionActualizada.setObservacion(dto.getObservacion());
        atencionActualizada.setPacienteId(dto.getPacienteId());
        atencionActualizada.setMedicoId(dto.getMedicoId());

        when(atencionRepository.findById(1)).thenReturn(Optional.of(atencionExistente));
        when(pacienteClient.obtenerPacientePorId(2)).thenReturn(pacienteDTO);
        when(medicoClient.obtenerMedicoPorId(3)).thenReturn(medicoDTO);
        when(atencionRepository.save(any(Atencion.class))).thenReturn(atencionActualizada);

        AtencionResponseDTO resultado = atencionService.actualizarAtencion(1, dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAtencion());
        assertEquals("2026-06-28", resultado.getFecha());
        assertEquals("16:00", resultado.getHora());
        assertEquals("Motivo actualizado", resultado.getMotivoConsulta());
        assertEquals("Observación actualizada", resultado.getObservacion());
        assertEquals(2, resultado.getPacienteId());
        assertEquals(3, resultado.getMedicoId());

        verify(atencionRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(2);
        verify(medicoClient, times(1)).obtenerMedicoPorId(3);
        verify(atencionRepository, times(1)).save(any(Atencion.class));
    }

    @Test
    void eliminarAtencion_deberiaEliminarAtencionCuandoExiste() {
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-29");
        atencion.setHora("17:00");
        atencion.setMotivoConsulta("Control final");
        atencion.setObservacion("Alta médica");
        atencion.setPacienteId(1);
        atencion.setMedicoId(1);

        when(atencionRepository.findById(1)).thenReturn(Optional.of(atencion));

        atencionService.eliminarAtencion(1);

        verify(atencionRepository, times(1)).findById(1);
        verify(atencionRepository, times(1)).delete(atencion);
    }

    @Test
    void buscarDetallePorId_deberiaRetornarDetalleAtencion() {
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-30");
        atencion.setHora("18:00");
        atencion.setMotivoConsulta("Consulta médica");
        atencion.setObservacion("Detalle completo");
        atencion.setPacienteId(1);
        atencion.setMedicoId(2);

        PacienteDTO pacienteDTO = mock(PacienteDTO.class);
        MedicoDTO medicoDTO = mock(MedicoDTO.class);

        when(atencionRepository.findById(1)).thenReturn(Optional.of(atencion));
        when(pacienteClient.obtenerPacientePorId(1)).thenReturn(pacienteDTO);
        when(medicoClient.obtenerMedicoPorId(2)).thenReturn(medicoDTO);

        AtencionDetalleDTO resultado = atencionService.buscarDetallePorId(1);

        assertNotNull(resultado);

        verify(atencionRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, times(1)).obtenerMedicoPorId(2);
    }
}