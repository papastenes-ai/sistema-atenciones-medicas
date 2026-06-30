package com.duoc.atencionesmedicas.diagnostico.service;

import com.duoc.atencionesmedicas.diagnostico.client.AtencionClient;
import com.duoc.atencionesmedicas.diagnostico.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoDetalleDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoRequestDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoResponseDTO;
import com.duoc.atencionesmedicas.diagnostico.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.diagnostico.exception.ReglaNegocioException;
import com.duoc.atencionesmedicas.diagnostico.model.Diagnostico;
import com.duoc.atencionesmedicas.diagnostico.repository.DiagnosticoRepository;
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
class DiagnosticoServiceTest {

    @Mock
    private DiagnosticoRepository diagnosticoRepository;

    @Mock
    private AtencionClient atencionClient;

    @InjectMocks
    private DiagnosticoService diagnosticoService;

    @Test
    void listarDiagnosticos_deberiaRetornarListaDeDiagnosticos() {
        // Given
        Diagnostico diagnostico = crearDiagnostico(
                1,
                "Gripe común",
                "Reposo e hidratación",
                "2026-06-30",
                10
        );

        when(diagnosticoRepository.findAll()).thenReturn(List.of(diagnostico));

        // When
        List<DiagnosticoResponseDTO> resultado = diagnosticoService.listarDiagnosticos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdDiagnostico());
        assertEquals("Gripe común", resultado.get(0).getDescripcion());
        assertEquals("Reposo e hidratación", resultado.get(0).getTratamiento());
        assertEquals("2026-06-30", resultado.get(0).getFechaDiagnostico());
        assertEquals(10, resultado.get(0).getAtencionId());

        verify(diagnosticoRepository, times(1)).findAll();
    }

    @Test
    void listarDiagnosticos_cuandoNoHayDiagnosticos_deberiaRetornarListaVacia() {
        // Given
        when(diagnosticoRepository.findAll()).thenReturn(List.of());

        // When
        List<DiagnosticoResponseDTO> resultado = diagnosticoService.listarDiagnosticos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(diagnosticoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDiagnostico() {
        // Given
        Diagnostico diagnostico = crearDiagnostico(
                1,
                "Bronquitis",
                "Inhalador y control médico",
                "2026-06-29",
                5
        );

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnostico));

        // When
        DiagnosticoResponseDTO resultado = diagnosticoService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDiagnostico());
        assertEquals("Bronquitis", resultado.getDescripcion());
        assertEquals("Inhalador y control médico", resultado.getTratamiento());
        assertEquals("2026-06-29", resultado.getFechaDiagnostico());
        assertEquals(5, resultado.getAtencionId());

        verify(diagnosticoRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(diagnosticoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> diagnosticoService.buscarPorId(99)
        );

        assertEquals("Diagnóstico no encontrado con id: 99", exception.getMessage());

        verify(diagnosticoRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorAtencionId_cuandoExistenDiagnosticos_deberiaRetornarLista() {
        // Given
        Diagnostico diagnostico = crearDiagnostico(
                1,
                "Migraña",
                "Analgésico y reposo",
                "2026-06-28",
                7
        );

        when(diagnosticoRepository.findByAtencionId(7)).thenReturn(List.of(diagnostico));

        // When
        List<DiagnosticoResponseDTO> resultado = diagnosticoService.buscarPorAtencionId(7);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(7, resultado.get(0).getAtencionId());
        assertEquals("Migraña", resultado.get(0).getDescripcion());
        assertEquals("Analgésico y reposo", resultado.get(0).getTratamiento());

        verify(diagnosticoRepository, times(1)).findByAtencionId(7);
    }

    @Test
    void buscarPorAtencionId_cuandoNoExistenDiagnosticos_deberiaLanzarExcepcion() {
        // Given
        when(diagnosticoRepository.findByAtencionId(999)).thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> diagnosticoService.buscarPorAtencionId(999)
        );

        assertEquals("No existen diagnósticos para la atención id: 999", exception.getMessage());

        verify(diagnosticoRepository, times(1)).findByAtencionId(999);
    }

    @Test
    void guardarDiagnostico_cuandoAtencionExiste_deberiaGuardarYRetornarDiagnostico() {
        // Given
        DiagnosticoRequestDTO dto = crearRequestDTO(
                "Amigdalitis",
                "Antibiótico y reposo",
                "2026-06-27",
                3
        );

        AtencionDetalleDTO atencionDetalle = mock(AtencionDetalleDTO.class);

        Diagnostico diagnosticoGuardado = crearDiagnostico(
                1,
                dto.getDescripcion(),
                dto.getTratamiento(),
                dto.getFechaDiagnostico(),
                dto.getAtencionId()
        );

        when(atencionClient.obtenerAtencionPorId(3)).thenReturn(atencionDetalle);
        when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnosticoGuardado);

        // When
        DiagnosticoResponseDTO resultado = diagnosticoService.guardarDiagnostico(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDiagnostico());
        assertEquals("Amigdalitis", resultado.getDescripcion());
        assertEquals("Antibiótico y reposo", resultado.getTratamiento());
        assertEquals("2026-06-27", resultado.getFechaDiagnostico());
        assertEquals(3, resultado.getAtencionId());

        verify(atencionClient, times(1)).obtenerAtencionPorId(3);
        verify(diagnosticoRepository, times(1)).save(any(Diagnostico.class));
    }

    @Test
    void guardarDiagnostico_cuandoAtencionNoExiste_noDebeGuardar() {
        // Given
        DiagnosticoRequestDTO dto = crearRequestDTO(
                "Diagnóstico prueba",
                "Tratamiento prueba",
                "2026-06-27",
                999
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/atenciones/999/detalle",
                "Atención no encontrada"
        );

        when(atencionClient.obtenerAtencionPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> diagnosticoService.guardarDiagnostico(dto)
        );

        assertNotNull(exception);
        verify(atencionClient, times(1)).obtenerAtencionPorId(999);
        verify(diagnosticoRepository, never()).save(any(Diagnostico.class));
    }

    @Test
    void guardarDiagnostico_cuandoAtencionServiceFalla_noDebeGuardar() {
        // Given
        DiagnosticoRequestDTO dto = crearRequestDTO(
                "Diagnóstico remoto",
                "Tratamiento remoto",
                "2026-06-27",
                2
        );

        FeignException errorRemoto = crearFeignInternalServerError(
                "/api/atenciones/2/detalle",
                "Servicio atención no disponible"
        );

        when(atencionClient.obtenerAtencionPorId(2)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> diagnosticoService.guardarDiagnostico(dto)
        );

        assertNotNull(exception);
        verify(atencionClient, times(1)).obtenerAtencionPorId(2);
        verify(diagnosticoRepository, never()).save(any(Diagnostico.class));
    }

    @Test
    void actualizarDiagnostico_cuandoExisteYAtencionExiste_deberiaActualizarYRetornarDiagnostico() {
        // Given
        Diagnostico diagnosticoExistente = crearDiagnostico(
                1,
                "Descripción antigua",
                "Tratamiento antiguo",
                "2026-06-20",
                1
        );

        DiagnosticoRequestDTO dto = crearRequestDTO(
                "Descripción actualizada",
                "Tratamiento actualizado",
                "2026-06-30",
                4
        );

        AtencionDetalleDTO atencionDetalle = mock(AtencionDetalleDTO.class);

        Diagnostico diagnosticoActualizado = crearDiagnostico(
                1,
                dto.getDescripcion(),
                dto.getTratamiento(),
                dto.getFechaDiagnostico(),
                dto.getAtencionId()
        );

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnosticoExistente));
        when(atencionClient.obtenerAtencionPorId(4)).thenReturn(atencionDetalle);
        when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnosticoActualizado);

        // When
        DiagnosticoResponseDTO resultado = diagnosticoService.actualizarDiagnostico(1, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDiagnostico());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals("Tratamiento actualizado", resultado.getTratamiento());
        assertEquals("2026-06-30", resultado.getFechaDiagnostico());
        assertEquals(4, resultado.getAtencionId());

        verify(diagnosticoRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(4);
        verify(diagnosticoRepository, times(1)).save(any(Diagnostico.class));
    }

    @Test
    void actualizarDiagnostico_cuandoDiagnosticoNoExiste_noDebeValidarAtencionNiGuardar() {
        // Given
        DiagnosticoRequestDTO dto = crearRequestDTO(
                "Descripción",
                "Tratamiento",
                "2026-06-30",
                1
        );

        when(diagnosticoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> diagnosticoService.actualizarDiagnostico(99, dto)
        );

        assertEquals("Diagnóstico no encontrado con id: 99", exception.getMessage());

        verify(diagnosticoRepository, times(1)).findById(99);
        verify(atencionClient, never()).obtenerAtencionPorId(anyInt());
        verify(diagnosticoRepository, never()).save(any(Diagnostico.class));
    }

    @Test
    void actualizarDiagnostico_cuandoAtencionNoExiste_noDebeGuardar() {
        // Given
        Diagnostico diagnosticoExistente = crearDiagnostico(
                1,
                "Diagnóstico antiguo",
                "Tratamiento antiguo",
                "2026-06-20",
                1
        );

        DiagnosticoRequestDTO dto = crearRequestDTO(
                "Diagnóstico actualizado",
                "Tratamiento actualizado",
                "2026-06-30",
                999
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/atenciones/999/detalle",
                "Atención no encontrada"
        );

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnosticoExistente));
        when(atencionClient.obtenerAtencionPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> diagnosticoService.actualizarDiagnostico(1, dto)
        );

        assertNotNull(exception);
        verify(diagnosticoRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(999);
        verify(diagnosticoRepository, never()).save(any(Diagnostico.class));
    }

    @Test
    void eliminarDiagnostico_cuandoExiste_deberiaEliminarDiagnostico() {
        // Given
        Diagnostico diagnostico = crearDiagnostico(
                1,
                "Diagnóstico a eliminar",
                "Tratamiento",
                "2026-06-29",
                1
        );

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnostico));

        // When
        diagnosticoService.eliminarDiagnostico(1);

        // Then
        verify(diagnosticoRepository, times(1)).findById(1);
        verify(diagnosticoRepository, times(1)).delete(diagnostico);
    }

    @Test
    void eliminarDiagnostico_cuandoNoExiste_noDebeEliminarYLanzaExcepcion() {
        // Given
        when(diagnosticoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> diagnosticoService.eliminarDiagnostico(99)
        );

        assertEquals("Diagnóstico no encontrado con id: 99", exception.getMessage());

        verify(diagnosticoRepository, times(1)).findById(99);
        verify(diagnosticoRepository, never()).delete(any(Diagnostico.class));
    }

    @Test
    void buscarDetallePorId_cuandoExisteDiagnosticoYAtencion_deberiaRetornarDetalle() {
        // Given
        Diagnostico diagnostico = crearDiagnostico(
                1,
                "Hipertensión",
                "Control y dieta baja en sodio",
                "2026-06-30",
                8
        );

        AtencionDetalleDTO atencionDetalle = mock(AtencionDetalleDTO.class);

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnostico));
        when(atencionClient.obtenerAtencionPorId(8)).thenReturn(atencionDetalle);

        // When
        DiagnosticoDetalleDTO resultado = diagnosticoService.buscarDetallePorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDiagnostico());
        assertEquals("Hipertensión", resultado.getDescripcion());
        assertEquals("Control y dieta baja en sodio", resultado.getTratamiento());
        assertEquals("2026-06-30", resultado.getFechaDiagnostico());
        assertNotNull(resultado.getAtencion());

        verify(diagnosticoRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(8);
    }

    @Test
    void buscarDetallePorId_cuandoDiagnosticoNoExiste_noDebeConsultarAtencion() {
        // Given
        when(diagnosticoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> diagnosticoService.buscarDetallePorId(99)
        );

        assertEquals("Diagnóstico no encontrado con id: 99", exception.getMessage());

        verify(diagnosticoRepository, times(1)).findById(99);
        verify(atencionClient, never()).obtenerAtencionPorId(anyInt());
    }

    @Test
    void buscarDetallePorId_cuandoAtencionAsociadaNoExiste_deberiaLanzarReglaNegocioException() {
        // Given
        Diagnostico diagnostico = crearDiagnostico(
                1,
                "Diagnóstico con atención inexistente",
                "Tratamiento",
                "2026-06-30",
                999
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/atenciones/999/detalle",
                "Atención no encontrada"
        );

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnostico));
        when(atencionClient.obtenerAtencionPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> diagnosticoService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(diagnosticoRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(999);
    }

    @Test
    void buscarDetallePorId_cuandoAtencionServiceFalla_deberiaLanzarReglaNegocioException() {
        // Given
        Diagnostico diagnostico = crearDiagnostico(
                1,
                "Diagnóstico remoto",
                "Tratamiento remoto",
                "2026-06-30",
                2
        );

        FeignException errorRemoto = crearFeignInternalServerError(
                "/api/atenciones/2/detalle",
                "Servicio atención no disponible"
        );

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnostico));
        when(atencionClient.obtenerAtencionPorId(2)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> diagnosticoService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(diagnosticoRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(2);
    }

    private Diagnostico crearDiagnostico(
            Integer id,
            String descripcion,
            String tratamiento,
            String fechaDiagnostico,
            Integer atencionId
    ) {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setIdDiagnostico(id);
        diagnostico.setDescripcion(descripcion);
        diagnostico.setTratamiento(tratamiento);
        diagnostico.setFechaDiagnostico(fechaDiagnostico);
        diagnostico.setAtencionId(atencionId);
        return diagnostico;
    }

    private DiagnosticoRequestDTO crearRequestDTO(
            String descripcion,
            String tratamiento,
            String fechaDiagnostico,
            Integer atencionId
    ) {
        DiagnosticoRequestDTO dto = new DiagnosticoRequestDTO();
        dto.setDescripcion(descripcion);
        dto.setTratamiento(tratamiento);
        dto.setFechaDiagnostico(fechaDiagnostico);
        dto.setAtencionId(atencionId);
        return dto;
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