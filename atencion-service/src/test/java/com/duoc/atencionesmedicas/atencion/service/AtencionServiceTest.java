package com.duoc.atencionesmedicas.atencion.service;

import com.duoc.atencionesmedicas.atencion.client.MedicoClient;
import com.duoc.atencionesmedicas.atencion.client.PacienteClient;
import com.duoc.atencionesmedicas.atencion.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.atencion.dto.AtencionRequestDTO;
import com.duoc.atencionesmedicas.atencion.dto.AtencionResponseDTO;
import com.duoc.atencionesmedicas.atencion.dto.MedicoDTO;
import com.duoc.atencionesmedicas.atencion.dto.PacienteDTO;
import com.duoc.atencionesmedicas.atencion.exception.ReglaNegocioException;
import com.duoc.atencionesmedicas.atencion.model.Atencion;
import com.duoc.atencionesmedicas.atencion.repository.AtencionRepository;
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
        // Given
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-24");
        atencion.setHora("10:30");
        atencion.setMotivoConsulta("Dolor de cabeza");
        atencion.setObservacion("Paciente estable");
        atencion.setPacienteId(1);
        atencion.setMedicoId(2);

        when(atencionRepository.findAll()).thenReturn(List.of(atencion));

        // When
        List<AtencionResponseDTO> resultado = atencionService.listarAtenciones();

        // Then
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
        // Given
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-24");
        atencion.setHora("11:00");
        atencion.setMotivoConsulta("Control médico");
        atencion.setObservacion("Sin observaciones");
        atencion.setPacienteId(3);
        atencion.setMedicoId(4);

        when(atencionRepository.findById(1)).thenReturn(Optional.of(atencion));

        // When
        AtencionResponseDTO resultado = atencionService.buscarPorId(1);

        // Then
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
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(atencionRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> atencionService.buscarPorId(99)
        );

        assertNotNull(exception);
        verify(atencionRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorPacienteId_deberiaRetornarAtencionesFiltradas() {
        // Given
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-25");
        atencion.setHora("09:00");
        atencion.setMotivoConsulta("Dolor abdominal");
        atencion.setObservacion("Derivar a exámenes");
        atencion.setPacienteId(5);
        atencion.setMedicoId(2);

        when(atencionRepository.findByPacienteId(5)).thenReturn(List.of(atencion));

        // When
        List<AtencionResponseDTO> resultado = atencionService.buscarPorPacienteId(5);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getPacienteId());
        assertEquals("Dolor abdominal", resultado.get(0).getMotivoConsulta());

        verify(atencionRepository, times(1)).findByPacienteId(5);
    }

    @Test
    void buscarPorPacienteId_cuandoNoTieneAtenciones_deberiaLanzarExcepcion() {
        // Given
        when(atencionRepository.findByPacienteId(999)).thenReturn(List.of());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> atencionService.buscarPorPacienteId(999)
        );

        assertNotNull(exception);
        verify(atencionRepository, times(1)).findByPacienteId(999);
    }

    @Test
    void buscarPorMedicoId_deberiaRetornarAtencionesFiltradas() {
        // Given
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-25");
        atencion.setHora("12:00");
        atencion.setMotivoConsulta("Chequeo general");
        atencion.setObservacion("Paciente sano");
        atencion.setPacienteId(4);
        atencion.setMedicoId(8);

        when(atencionRepository.findByMedicoId(8)).thenReturn(List.of(atencion));

        // When
        List<AtencionResponseDTO> resultado = atencionService.buscarPorMedicoId(8);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(8, resultado.get(0).getMedicoId());
        assertEquals("12:00", resultado.get(0).getHora());

        verify(atencionRepository, times(1)).findByMedicoId(8);
    }

    @Test
    void buscarPorMedicoId_cuandoNoTieneAtenciones_deberiaLanzarExcepcion() {
        // Given
        when(atencionRepository.findByMedicoId(999)).thenReturn(List.of());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> atencionService.buscarPorMedicoId(999)
        );

        assertNotNull(exception);
        verify(atencionRepository, times(1)).findByMedicoId(999);
    }

    @Test
    void guardarAtencion_deberiaValidarPacienteMedicoGuardarYRetornarAtencion() {
        // Given
        AtencionRequestDTO dto = new AtencionRequestDTO();
        dto.setFecha("2026-06-27");
        dto.setHora("15:30");
        dto.setMotivoConsulta("Dolor de garganta");
        dto.setObservacion("Recetar medicamento");
        dto.setPacienteId(1);
        dto.setMedicoId(2);

        PacienteDTO pacienteDTO = crearPacienteDTO(1);
        MedicoDTO medicoDTO = crearMedicoDTO(2);

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

        // When
        AtencionResponseDTO resultado = atencionService.guardarAtencion(dto);

        // Then
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
    void guardarAtencion_cuandoPacienteNoExiste_noDebeGuardar() {
        // Given
        AtencionRequestDTO dto = new AtencionRequestDTO();
        dto.setFecha("2026-06-27");
        dto.setHora("15:30");
        dto.setMotivoConsulta("Dolor de garganta");
        dto.setObservacion("Recetar medicamento");
        dto.setPacienteId(999);
        dto.setMedicoId(2);

        FeignException.NotFound notFound = crearFeignNotFound("/api/pacientes/999", "Paciente no encontrado");

        when(pacienteClient.obtenerPacientePorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> atencionService.guardarAtencion(dto)
        );

        assertNotNull(exception);
        verify(pacienteClient, times(1)).obtenerPacientePorId(999);
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
        verify(atencionRepository, never()).save(any(Atencion.class));
    }

    @Test
    void guardarAtencion_cuandoMedicoNoExiste_noDebeGuardar() {
        // Given
        AtencionRequestDTO dto = new AtencionRequestDTO();
        dto.setFecha("2026-06-27");
        dto.setHora("15:30");
        dto.setMotivoConsulta("Dolor de garganta");
        dto.setObservacion("Recetar medicamento");
        dto.setPacienteId(1);
        dto.setMedicoId(999);

        PacienteDTO pacienteDTO = crearPacienteDTO(1);
        FeignException.NotFound notFound = crearFeignNotFound("/api/medicos/999", "Médico no encontrado");

        when(pacienteClient.obtenerPacientePorId(1)).thenReturn(pacienteDTO);
        when(medicoClient.obtenerMedicoPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> atencionService.guardarAtencion(dto)
        );

        assertNotNull(exception);
        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, times(1)).obtenerMedicoPorId(999);
        verify(atencionRepository, never()).save(any(Atencion.class));
    }

    @Test
    void guardarAtencion_cuandoPacienteServiceFalla_noDebeGuardar() {
        // Given
        AtencionRequestDTO dto = new AtencionRequestDTO();
        dto.setFecha("2026-06-27");
        dto.setHora("15:30");
        dto.setMotivoConsulta("Dolor de garganta");
        dto.setObservacion("Recetar medicamento");
        dto.setPacienteId(1);
        dto.setMedicoId(2);

        FeignException errorRemoto = crearFeignInternalServerError("/api/pacientes/1", "Servicio paciente no disponible");

        when(pacienteClient.obtenerPacientePorId(1)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> atencionService.guardarAtencion(dto)
        );

        assertNotNull(exception);
        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
        verify(atencionRepository, never()).save(any(Atencion.class));
    }

    @Test
    void actualizarAtencion_deberiaActualizarYRetornarAtencion() {
        // Given
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

        PacienteDTO pacienteDTO = crearPacienteDTO(2);
        MedicoDTO medicoDTO = crearMedicoDTO(3);

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

        // When
        AtencionResponseDTO resultado = atencionService.actualizarAtencion(1, dto);

        // Then
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
    void actualizarAtencion_cuandoAtencionNoExiste_noDebeGuardarNiValidarRemotos() {
        // Given
        AtencionRequestDTO dto = new AtencionRequestDTO();
        dto.setFecha("2026-06-28");
        dto.setHora("16:00");
        dto.setMotivoConsulta("Motivo actualizado");
        dto.setObservacion("Observación actualizada");
        dto.setPacienteId(2);
        dto.setMedicoId(3);

        when(atencionRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> atencionService.actualizarAtencion(99, dto)
        );

        assertNotNull(exception);
        verify(atencionRepository, times(1)).findById(99);
        verify(pacienteClient, never()).obtenerPacientePorId(anyInt());
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
        verify(atencionRepository, never()).save(any(Atencion.class));
    }

    @Test
    void eliminarAtencion_deberiaEliminarAtencionCuandoExiste() {
        // Given
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-29");
        atencion.setHora("17:00");
        atencion.setMotivoConsulta("Control final");
        atencion.setObservacion("Alta médica");
        atencion.setPacienteId(1);
        atencion.setMedicoId(1);

        when(atencionRepository.findById(1)).thenReturn(Optional.of(atencion));

        // When
        atencionService.eliminarAtencion(1);

        // Then
        verify(atencionRepository, times(1)).findById(1);
        verify(atencionRepository, times(1)).delete(atencion);
    }

    @Test
    void eliminarAtencion_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(atencionRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> atencionService.eliminarAtencion(99)
        );

        assertNotNull(exception);
        verify(atencionRepository, times(1)).findById(99);
        verify(atencionRepository, never()).delete(any(Atencion.class));
    }

    @Test
    void buscarDetallePorId_deberiaRetornarDetalleAtencion() {
        // Given
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-30");
        atencion.setHora("18:00");
        atencion.setMotivoConsulta("Consulta médica");
        atencion.setObservacion("Detalle completo");
        atencion.setPacienteId(1);
        atencion.setMedicoId(2);

        PacienteDTO pacienteDTO = crearPacienteDTO(1);
        MedicoDTO medicoDTO = crearMedicoDTO(2);

        when(atencionRepository.findById(1)).thenReturn(Optional.of(atencion));
        when(pacienteClient.obtenerPacientePorId(1)).thenReturn(pacienteDTO);
        when(medicoClient.obtenerMedicoPorId(2)).thenReturn(medicoDTO);

        // When
        AtencionDetalleDTO resultado = atencionService.buscarDetallePorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdAtencion());
        assertEquals("2026-06-30", resultado.getFecha());
        assertEquals("18:00", resultado.getHora());
        assertEquals("Consulta médica", resultado.getMotivoConsulta());
        assertEquals("Detalle completo", resultado.getObservacion());
        assertEquals(1, resultado.getPaciente().getIdPaciente());
        assertEquals(2, resultado.getMedico().getIdMedico());

        verify(atencionRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, times(1)).obtenerMedicoPorId(2);
    }

    @Test
    void buscarDetallePorId_cuandoPacienteNoExiste_deberiaLanzarReglaNegocioException() {
        // Given
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-30");
        atencion.setHora("18:00");
        atencion.setMotivoConsulta("Consulta médica");
        atencion.setObservacion("Detalle completo");
        atencion.setPacienteId(999);
        atencion.setMedicoId(2);

        FeignException.NotFound notFound = crearFeignNotFound("/api/pacientes/999", "Paciente no encontrado");

        when(atencionRepository.findById(1)).thenReturn(Optional.of(atencion));
        when(pacienteClient.obtenerPacientePorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> atencionService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(atencionRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(999);
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
    }

    @Test
    void buscarDetallePorId_cuandoServicioRemotoFalla_deberiaLanzarReglaNegocioException() {
        // Given
        Atencion atencion = new Atencion();
        atencion.setIdAtencion(1);
        atencion.setFecha("2026-06-30");
        atencion.setHora("18:00");
        atencion.setMotivoConsulta("Consulta médica");
        atencion.setObservacion("Detalle completo");
        atencion.setPacienteId(1);
        atencion.setMedicoId(2);

        FeignException errorRemoto = crearFeignInternalServerError("/api/pacientes/1", "Servicio remoto no disponible");

        when(atencionRepository.findById(1)).thenReturn(Optional.of(atencion));
        when(pacienteClient.obtenerPacientePorId(1)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> atencionService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(atencionRepository, times(1)).findById(1);
        verify(pacienteClient, times(1)).obtenerPacientePorId(1);
        verify(medicoClient, never()).obtenerMedicoPorId(anyInt());
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