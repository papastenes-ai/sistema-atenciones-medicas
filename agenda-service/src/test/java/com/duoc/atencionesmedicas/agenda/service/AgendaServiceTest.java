package com.duoc.atencionesmedicas.agenda.service;

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
import feign.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
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
        // Given
        Agenda agenda = crearAgenda(
                1,
                "2026-06-30",
                "10:30",
                "PROGRAMADA",
                5,
                7
        );

        when(agendaRepository.findAll()).thenReturn(List.of(agenda));

        // When
        List<AgendaResponseDTO> resultado = agendaService.listarAgendas();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdAgenda());
        assertEquals("2026-06-30", resultado.get(0).getFecha());
        assertEquals("10:30", resultado.get(0).getHora());
        assertEquals("PROGRAMADA", resultado.get(0).getEstado());
        assertEquals(5, resultado.get(0).getPacienteId());
        assertEquals(7, resultado.get(0).getMedicoId());

        verify(agendaRepository, times(1)).findAll();
    }

    @Test
    void listarAgendas_cuandoNoHayAgendas_deberiaRetornarListaVacia() {
        // Given
        when(agendaRepository.findAll()).thenReturn(List.of());

        // When
        List<AgendaResponseDTO> resultado = agendaService.listarAgendas();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(agendaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarAgenda() {
        // Given
        Agenda agenda = crearAgenda(
                1,
                "2026-07-01",
                "11:00",
                "CONFIRMADA",
                2,
                3
        );

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agenda));

        // When
        AgendaResponseDTO resultado = agendaService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAgenda());
        assertEquals("2026-07-01", resultado.getFecha());
        assertEquals("11:00", resultado.getHora());
        assertEquals("CONFIRMADA", resultado.getEstado());
        assertEquals(2, resultado.getPacienteId());
        assertEquals(3, resultado.getMedicoId());

        verify(agendaRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(agendaRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> agendaService.buscarPorId(99)
        );

        assertEquals("Agenda no encontrada con id: 99", exception.getMessage());

        verify(agendaRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorPacienteId_cuandoExistenAgendas_deberiaRetornarLista() {
        // Given
        Agenda agenda = crearAgenda(
                1,
                "2026-07-02",
                "09:00",
                "PROGRAMADA",
                10,
                4
        );

        when(agendaRepository.findByPacienteId(10)).thenReturn(List.of(agenda));

        // When
        List<AgendaResponseDTO> resultado = agendaService.buscarPorPacienteId(10);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(10, resultado.get(0).getPacienteId());
        assertEquals(4, resultado.get(0).getMedicoId());
        assertEquals("PROGRAMADA", resultado.get(0).getEstado());

        verify(agendaRepository, times(1)).findByPacienteId(10);
    }

    @Test
    void buscarPorPacienteId_cuandoNoExistenAgendas_deberiaLanzarExcepcion() {
        // Given
        when(agendaRepository.findByPacienteId(999)).thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> agendaService.buscarPorPacienteId(999)
        );

        assertEquals("No existen agendas para el paciente id: 999", exception.getMessage());

        verify(agendaRepository, times(1)).findByPacienteId(999);
    }

    @Test
    void buscarPorMedicoId_cuandoExistenAgendas_deberiaRetornarLista() {
        // Given
        Agenda agenda = crearAgenda(
                1,
                "2026-07-03",
                "15:00",
                "CONFIRMADA",
                8,
                12
        );

        when(agendaRepository.findByMedicoId(12)).thenReturn(List.of(agenda));

        // When
        List<AgendaResponseDTO> resultado = agendaService.buscarPorMedicoId(12);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(12, resultado.get(0).getMedicoId());
        assertEquals(8, resultado.get(0).getPacienteId());
        assertEquals("CONFIRMADA", resultado.get(0).getEstado());

        verify(agendaRepository, times(1)).findByMedicoId(12);
    }

    @Test
    void buscarPorMedicoId_cuandoNoExistenAgendas_deberiaLanzarExcepcion() {
        // Given
        when(agendaRepository.findByMedicoId(999)).thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> agendaService.buscarPorMedicoId(999)
        );

        assertEquals("No existen agendas para el médico id: 999", exception.getMessage());

        verify(agendaRepository, times(1)).findByMedicoId(999);
    }

    @Test
    void buscarPorEstado_cuandoExistenAgendas_deberiaRetornarLista() {
        // Given
        Agenda agenda = crearAgenda(
                1,
                "2026-07-04",
                "16:30",
                "CANCELADA",
                3,
                6
        );

        when(agendaRepository.findByEstado("CANCELADA")).thenReturn(List.of(agenda));

        // When
        List<AgendaResponseDTO> resultado = agendaService.buscarPorEstado("CANCELADA");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("CANCELADA", resultado.get(0).getEstado());
        assertEquals(3, resultado.get(0).getPacienteId());
        assertEquals(6, resultado.get(0).getMedicoId());

        verify(agendaRepository, times(1)).findByEstado("CANCELADA");
    }

    @Test
    void buscarPorEstado_cuandoNoExistenAgendas_deberiaLanzarExcepcion() {
        // Given
        when(agendaRepository.findByEstado("NO_EXISTE")).thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> agendaService.buscarPorEstado("NO_EXISTE")
        );

        assertEquals("No existen agendas con estado: NO_EXISTE", exception.getMessage());

        verify(agendaRepository, times(1)).findByEstado("NO_EXISTE");
    }

    @Test
    void guardarAgenda_cuandoPacienteYMedicoExisten_deberiaGuardarYRetornarAgenda() {
        // Given
        AgendaRequestDTO dto = crearRequestDTO(
                "2026-07-05",
                "12:00",
                "PROGRAMADA",
                1,
                2
        );

        PacienteDTO paciente = crearPacienteDTO(1);
        MedicoDTO medico = crearMedicoDTO(2);

        Agenda agendaGuardada = crearAgenda(
                1,
                dto.getFecha(),
                dto.getHora(),
                dto.getEstado(),
                dto.getPacienteId(),
                dto.getMedicoId()
        );

        when(pacienteClient.obtenerPacientePorId(1)).thenReturn(paciente);
        when(medicoClient.obtenerMedicoPorId(2)).thenReturn(medico);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agendaGuardada);

        // When
        AgendaResponseDTO resultado = agendaService.guardarAgenda(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAgenda());
        assertEquals("2026-07-05", resultado.getFecha());
        assertEquals("12:00", resultado.getHora());
        assertEquals("PROGRAMADA", resultado.getEstado());
        assertEquals(1, resultado.getPacienteId());
        assertEquals(2, resultado.getMedicoId());

        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, times(1)).obtenerMedicoPorId(2);
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    void guardarAgenda_cuandoPacienteNoExiste_noDebeGuardar() {
        // Given
        AgendaRequestDTO dto = crearRequestDTO(
                "2026-07-05",
                "12:00",
                "PROGRAMADA",
                999,
                2
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/pacientes/999",
                "Paciente no encontrado"
        );

        when(pacienteClient.obtenerPacientePorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> agendaService.guardarAgenda(dto)
        );

        assertNotNull(exception);
        verify(pacienteClient, times(1)).obtenerPacientePorId(999);
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
        verify(agendaRepository, never()).save(any(Agenda.class));
    }

    @Test
    void guardarAgenda_cuandoMedicoNoExiste_noDebeGuardar() {
        // Given
        AgendaRequestDTO dto = crearRequestDTO(
                "2026-07-05",
                "12:00",
                "PROGRAMADA",
                1,
                999
        );

        PacienteDTO paciente = crearPacienteDTO(1);

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/medicos/999",
                "Médico no encontrado"
        );

        when(pacienteClient.obtenerPacientePorId(1)).thenReturn(paciente);
        when(medicoClient.obtenerMedicoPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> agendaService.guardarAgenda(dto)
        );

        assertNotNull(exception);
        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, times(1)).obtenerMedicoPorId(999);
        verify(agendaRepository, never()).save(any(Agenda.class));
    }

    @Test
    void guardarAgenda_cuandoPacienteServiceFalla_noDebeGuardar() {
        // Given
        AgendaRequestDTO dto = crearRequestDTO(
                "2026-07-05",
                "12:00",
                "PROGRAMADA",
                1,
                2
        );

        FeignException errorRemoto = crearFeignInternalServerError(
                "/api/pacientes/1",
                "Servicio paciente no disponible"
        );

        when(pacienteClient.obtenerPacientePorId(1)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> agendaService.guardarAgenda(dto)
        );

        assertNotNull(exception);
        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
        verify(agendaRepository, never()).save(any(Agenda.class));
    }

    @Test
    void actualizarAgenda_cuandoExistePacienteYMedico_deberiaActualizarYRetornarAgenda() {
        // Given
        Agenda agendaExistente = crearAgenda(
                1,
                "2026-07-01",
                "08:00",
                "PROGRAMADA",
                1,
                2
        );

        AgendaRequestDTO dto = crearRequestDTO(
                "2026-07-10",
                "14:00",
                "CONFIRMADA",
                3,
                4
        );

        PacienteDTO paciente = crearPacienteDTO(3);
        MedicoDTO medico = crearMedicoDTO(4);

        Agenda agendaActualizada = crearAgenda(
                1,
                dto.getFecha(),
                dto.getHora(),
                dto.getEstado(),
                dto.getPacienteId(),
                dto.getMedicoId()
        );

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agendaExistente));
        when(pacienteClient.obtenerPacientePorId(3)).thenReturn(paciente);
        when(medicoClient.obtenerMedicoPorId(4)).thenReturn(medico);
        when(agendaRepository.save(any(Agenda.class))).thenReturn(agendaActualizada);

        // When
        AgendaResponseDTO resultado = agendaService.actualizarAgenda(1, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAgenda());
        assertEquals("2026-07-10", resultado.getFecha());
        assertEquals("14:00", resultado.getHora());
        assertEquals("CONFIRMADA", resultado.getEstado());
        assertEquals(3, resultado.getPacienteId());
        assertEquals(4, resultado.getMedicoId());

        verify(agendaRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(3);
        verify(medicoClient, times(1)).obtenerMedicoPorId(4);
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    void actualizarAgenda_cuandoAgendaNoExiste_noDebeValidarRemotosNiGuardar() {
        // Given
        AgendaRequestDTO dto = crearRequestDTO(
                "2026-07-10",
                "14:00",
                "CONFIRMADA",
                3,
                4
        );

        when(agendaRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> agendaService.actualizarAgenda(99, dto)
        );

        assertEquals("Agenda no encontrada con id: 99", exception.getMessage());

        verify(agendaRepository, times(1)).findById(99);
        verify(pacienteClient, never()).obtenerPacientePorId(anyInt());
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
        verify(agendaRepository, never()).save(any(Agenda.class));
    }

    @Test
    void actualizarAgenda_cuandoMedicoNoExiste_noDebeGuardar() {
        // Given
        Agenda agendaExistente = crearAgenda(
                1,
                "2026-07-01",
                "08:00",
                "PROGRAMADA",
                1,
                2
        );

        AgendaRequestDTO dto = crearRequestDTO(
                "2026-07-10",
                "14:00",
                "CONFIRMADA",
                3,
                999
        );

        PacienteDTO paciente = crearPacienteDTO(3);

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/medicos/999",
                "Médico no encontrado"
        );

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agendaExistente));
        when(pacienteClient.obtenerPacientePorId(3)).thenReturn(paciente);
        when(medicoClient.obtenerMedicoPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> agendaService.actualizarAgenda(1, dto)
        );

        assertNotNull(exception);
        verify(agendaRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(3);
        verify(medicoClient, times(1)).obtenerMedicoPorId(999);
        verify(agendaRepository, never()).save(any(Agenda.class));
    }

    @Test
    void eliminarAgenda_cuandoExiste_deberiaEliminarAgenda() {
        // Given
        Agenda agenda = crearAgenda(
                1,
                "2026-07-11",
                "17:00",
                "CANCELADA",
                1,
                2
        );

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agenda));

        // When
        agendaService.eliminarAgenda(1);

        // Then
        verify(agendaRepository, times(1)).findById(1);
        verify(agendaRepository, times(1)).delete(agenda);
    }

    @Test
    void eliminarAgenda_cuandoNoExiste_noDebeEliminarYLanzaExcepcion() {
        // Given
        when(agendaRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> agendaService.eliminarAgenda(99)
        );

        assertEquals("Agenda no encontrada con id: 99", exception.getMessage());

        verify(agendaRepository, times(1)).findById(99);
        verify(agendaRepository, never()).delete(any(Agenda.class));
    }

    @Test
    void buscarDetallePorId_cuandoExisteAgendaPacienteYMedico_deberiaRetornarDetalle() {
        // Given
        Agenda agenda = crearAgenda(
                1,
                "2026-07-12",
                "18:00",
                "CONFIRMADA",
                5,
                6
        );

        PacienteDTO paciente = crearPacienteDTO(5);
        MedicoDTO medico = crearMedicoDTO(6);

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agenda));
        when(pacienteClient.obtenerPacientePorId(5)).thenReturn(paciente);
        when(medicoClient.obtenerMedicoPorId(6)).thenReturn(medico);

        // When
        AgendaDetalleDTO resultado = agendaService.buscarDetallePorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAgenda());
        assertEquals("2026-07-12", resultado.getFecha());
        assertEquals("18:00", resultado.getHora());
        assertEquals("CONFIRMADA", resultado.getEstado());
        assertEquals(5, resultado.getPaciente().getIdPaciente());
        assertEquals(6, resultado.getMedico().getIdMedico());

        verify(agendaRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(5);
        verify(medicoClient, times(1)).obtenerMedicoPorId(6);
    }

    @Test
    void buscarDetallePorId_cuandoAgendaNoExiste_noDebeConsultarRemotos() {
        // Given
        when(agendaRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> agendaService.buscarDetallePorId(99)
        );

        assertEquals("Agenda no encontrada con id: 99", exception.getMessage());

        verify(agendaRepository, times(1)).findById(99);
        verify(pacienteClient, never()).obtenerPacientePorId(anyInt());
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
    }

    @Test
    void buscarDetallePorId_cuandoPacienteAsociadoNoExiste_deberiaLanzarReglaNegocioException() {
        // Given
        Agenda agenda = crearAgenda(
                1,
                "2026-07-12",
                "18:00",
                "CONFIRMADA",
                999,
                6
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/pacientes/999",
                "Paciente no encontrado"
        );

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agenda));
        when(pacienteClient.obtenerPacientePorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> agendaService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(agendaRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(999);
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
    }

    @Test
    void buscarDetallePorId_cuandoServicioRemotoFalla_deberiaLanzarReglaNegocioException() {
        // Given
        Agenda agenda = crearAgenda(
                1,
                "2026-07-12",
                "18:00",
                "CONFIRMADA",
                5,
                6
        );

        FeignException errorRemoto = crearFeignInternalServerError(
                "/api/pacientes/5",
                "Servicio paciente no disponible"
        );

        when(agendaRepository.findById(1)).thenReturn(Optional.of(agenda));
        when(pacienteClient.obtenerPacientePorId(5)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> agendaService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(agendaRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(5);
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
    }

    private Agenda crearAgenda(
            Integer id,
            String fecha,
            String hora,
            String estado,
            Integer pacienteId,
            Integer medicoId
    ) {
        Agenda agenda = new Agenda();
        agenda.setIdAgenda(id);
        agenda.setFecha(fecha);
        agenda.setHora(hora);
        agenda.setEstado(estado);
        agenda.setPacienteId(pacienteId);
        agenda.setMedicoId(medicoId);
        return agenda;
    }

    private AgendaRequestDTO crearRequestDTO(
            String fecha,
            String hora,
            String estado,
            Integer pacienteId,
            Integer medicoId
    ) {
        AgendaRequestDTO dto = new AgendaRequestDTO();
        dto.setFecha(fecha);
        dto.setHora(hora);
        dto.setEstado(estado);
        dto.setPacienteId(pacienteId);
        dto.setMedicoId(medicoId);
        return dto;
    }

    private PacienteDTO crearPacienteDTO(Integer id) {
        PacienteDTO paciente = new PacienteDTO();
        paciente.setIdPaciente(id);
        paciente.setRut("11111111-1");
        paciente.setNombre("Juan");
        paciente.setApellido("Pérez");
        paciente.setEdad(30);
        return paciente;
    }

    private MedicoDTO crearMedicoDTO(Integer id) {
        MedicoDTO medico = new MedicoDTO();
        medico.setIdMedico(id);
        medico.setRut("22222222-2");
        medico.setNombre("Ana");
        medico.setApellido("González");
        medico.setCorreo("ana.gonzalez@clinica.cl");
        medico.setTelefono("987654321");
        return medico;
    }

    private FeignException.NotFound crearFeignNotFound(String url, String mensaje) {
        return new FeignException.NotFound(
                mensaje,
                crearRequest(url),
                null,
                Collections.emptyMap()
        );
    }

    private FeignException.InternalServerError crearFeignInternalServerError(String url, String mensaje) {
        return new FeignException.InternalServerError(
                mensaje,
                crearRequest(url),
                null,
                Collections.emptyMap()
        );
    }

    private Request crearRequest(String url) {
        return Request.create(
                Request.HttpMethod.GET,
                url,
                Collections.emptyMap(),
                null,
                StandardCharsets.UTF_8,
                null
        );
    }
}