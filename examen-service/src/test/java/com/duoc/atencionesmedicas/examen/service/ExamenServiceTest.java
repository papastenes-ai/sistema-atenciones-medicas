package com.duoc.atencionesmedicas.examen.service;

import com.duoc.atencionesmedicas.examen.client.AtencionClient;
import com.duoc.atencionesmedicas.examen.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenDetalleDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenRequestDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenResponseDTO;
import com.duoc.atencionesmedicas.examen.exception.ReglaNegocioException;
import com.duoc.atencionesmedicas.examen.model.Examen;
import com.duoc.atencionesmedicas.examen.repository.ExamenRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceTest {

    @Mock
    private ExamenRepository examenRepository;

    @Mock
    private AtencionClient atencionClient;

    @InjectMocks
    private ExamenService examenService;

    @Test
    void listarExamenes_deberiaRetornarListaDeExamenes() {
        // Given
        Examen examen = crearExamen(1, "Hemograma", "Normal", "2026-06-24", 1);

        when(examenRepository.findAll()).thenReturn(List.of(examen));

        // When
        List<ExamenResponseDTO> resultado = examenService.listarExamenes();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdExamen());
        assertEquals("Hemograma", resultado.get(0).getNombreExamen());
        assertEquals("Normal", resultado.get(0).getResultado());
        assertEquals("2026-06-24", resultado.get(0).getFechaExamen());
        assertEquals(1, resultado.get(0).getAtencionId());

        verify(examenRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarExamen() {
        // Given
        Examen examen = crearExamen(1, "Radiografía", "Sin hallazgos", "2026-06-25", 2);

        when(examenRepository.findById(1)).thenReturn(Optional.of(examen));

        // When
        ExamenResponseDTO resultado = examenService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdExamen());
        assertEquals("Radiografía", resultado.getNombreExamen());
        assertEquals("Sin hallazgos", resultado.getResultado());
        assertEquals("2026-06-25", resultado.getFechaExamen());
        assertEquals(2, resultado.getAtencionId());

        verify(examenRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(examenRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> examenService.buscarPorId(99)
        );

        assertNotNull(exception);
        verify(examenRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorAtencionId_deberiaRetornarExamenesFiltrados() {
        // Given
        Examen examen = crearExamen(1, "Perfil lipídico", "Colesterol elevado", "2026-06-26", 5);

        when(examenRepository.findByAtencionId(5)).thenReturn(List.of(examen));

        // When
        List<ExamenResponseDTO> resultado = examenService.buscarPorAtencionId(5);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getAtencionId());
        assertEquals("Perfil lipídico", resultado.get(0).getNombreExamen());
        assertEquals("Colesterol elevado", resultado.get(0).getResultado());

        verify(examenRepository, times(1)).findByAtencionId(5);
    }

    @Test
    void buscarPorAtencionId_cuandoNoHayExamenes_deberiaLanzarExcepcion() {
        // Given
        when(examenRepository.findByAtencionId(999)).thenReturn(List.of());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> examenService.buscarPorAtencionId(999)
        );

        assertNotNull(exception);
        verify(examenRepository, times(1)).findByAtencionId(999);
    }

    @Test
    void buscarPorNombreExamen_deberiaRetornarExamenesFiltrados() {
        // Given
        Examen examen = crearExamen(1, "Hemograma completo", "Normal", "2026-06-26", 1);

        when(examenRepository.findByNombreExamenContainingIgnoreCase("Hemo"))
                .thenReturn(List.of(examen));

        // When
        List<ExamenResponseDTO> resultado = examenService.buscarPorNombreExamen("Hemo");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Hemograma completo", resultado.get(0).getNombreExamen());
        assertEquals("Normal", resultado.get(0).getResultado());

        verify(examenRepository, times(1))
                .findByNombreExamenContainingIgnoreCase("Hemo");
    }

    @Test
    void buscarPorNombreExamen_cuandoNoHayResultados_deberiaLanzarExcepcion() {
        // Given
        when(examenRepository.findByNombreExamenContainingIgnoreCase("Resonancia"))
                .thenReturn(List.of());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> examenService.buscarPorNombreExamen("Resonancia")
        );

        assertNotNull(exception);
        verify(examenRepository, times(1))
                .findByNombreExamenContainingIgnoreCase("Resonancia");
    }

    @Test
    void guardarExamen_deberiaValidarAtencionGuardarYRetornarExamen() {
        // Given
        ExamenRequestDTO dto = crearRequestDTO(
                "Examen de orina",
                "Normal",
                "2026-06-27",
                1
        );

        AtencionDetalleDTO atencionDetalleDTO = mock(AtencionDetalleDTO.class);

        Examen examenGuardado = crearExamen(
                1,
                dto.getNombreExamen(),
                dto.getResultado(),
                dto.getFechaExamen(),
                dto.getAtencionId()
        );

        when(atencionClient.obtenerAtencionPorId(1)).thenReturn(atencionDetalleDTO);
        when(examenRepository.save(any(Examen.class))).thenReturn(examenGuardado);

        // When
        ExamenResponseDTO resultado = examenService.guardarExamen(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdExamen());
        assertEquals("Examen de orina", resultado.getNombreExamen());
        assertEquals("Normal", resultado.getResultado());
        assertEquals("2026-06-27", resultado.getFechaExamen());
        assertEquals(1, resultado.getAtencionId());

        verify(atencionClient, times(1)).obtenerAtencionPorId(1);
        verify(examenRepository, times(1)).save(any(Examen.class));
    }

    @Test
    void guardarExamen_cuandoAtencionNoExiste_noDebeGuardarExamen() {
        // Given
        ExamenRequestDTO dto = crearRequestDTO(
                "Resonancia",
                "Pendiente",
                "2026-06-30",
                999
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/atenciones/999",
                "Atención no encontrada"
        );

        when(atencionClient.obtenerAtencionPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> examenService.guardarExamen(dto)
        );

        assertNotNull(exception);
        verify(atencionClient, times(1)).obtenerAtencionPorId(999);
        verify(examenRepository, never()).save(any(Examen.class));
    }

    @Test
    void guardarExamen_cuandoAtencionServiceFalla_noDebeGuardarExamen() {
        // Given
        ExamenRequestDTO dto = crearRequestDTO(
                "Scanner",
                "Pendiente",
                "2026-06-30",
                1
        );

        FeignException errorRemoto = crearFeignInternalServerError(
                "/api/atenciones/1",
                "Servicio atención no disponible"
        );

        when(atencionClient.obtenerAtencionPorId(1)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> examenService.guardarExamen(dto)
        );

        assertNotNull(exception);
        verify(atencionClient, times(1)).obtenerAtencionPorId(1);
        verify(examenRepository, never()).save(any(Examen.class));
    }

    @Test
    void actualizarExamen_deberiaActualizarYRetornarExamen() {
        // Given
        Examen examenExistente = crearExamen(
                1,
                "Examen antiguo",
                "Resultado antiguo",
                "2026-06-20",
                1
        );

        ExamenRequestDTO dto = crearRequestDTO(
                "Examen actualizado",
                "Resultado actualizado",
                "2026-06-28",
                2
        );

        AtencionDetalleDTO atencionDetalleDTO = mock(AtencionDetalleDTO.class);

        Examen examenActualizado = crearExamen(
                1,
                dto.getNombreExamen(),
                dto.getResultado(),
                dto.getFechaExamen(),
                dto.getAtencionId()
        );

        when(examenRepository.findById(1)).thenReturn(Optional.of(examenExistente));
        when(atencionClient.obtenerAtencionPorId(2)).thenReturn(atencionDetalleDTO);
        when(examenRepository.save(any(Examen.class))).thenReturn(examenActualizado);

        // When
        ExamenResponseDTO resultado = examenService.actualizarExamen(1, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdExamen());
        assertEquals("Examen actualizado", resultado.getNombreExamen());
        assertEquals("Resultado actualizado", resultado.getResultado());
        assertEquals("2026-06-28", resultado.getFechaExamen());
        assertEquals(2, resultado.getAtencionId());

        verify(examenRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(2);
        verify(examenRepository, times(1)).save(any(Examen.class));
    }

    @Test
    void actualizarExamen_cuandoExamenNoExiste_noDebeValidarAtencionNiGuardar() {
        // Given
        ExamenRequestDTO dto = crearRequestDTO(
                "Examen actualizado",
                "Resultado actualizado",
                "2026-06-30",
                1
        );

        when(examenRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> examenService.actualizarExamen(99, dto)
        );

        assertNotNull(exception);
        verify(examenRepository, times(1)).findById(99);
        verify(atencionClient, never()).obtenerAtencionPorId(anyInt());
        verify(examenRepository, never()).save(any(Examen.class));
    }

    @Test
    void actualizarExamen_cuandoAtencionNoExiste_noDebeGuardar() {
        // Given
        Examen examenExistente = crearExamen(
                1,
                "Examen antiguo",
                "Resultado antiguo",
                "2026-06-20",
                1
        );

        ExamenRequestDTO dto = crearRequestDTO(
                "Examen actualizado",
                "Resultado actualizado",
                "2026-06-30",
                999
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/atenciones/999",
                "Atención no encontrada"
        );

        when(examenRepository.findById(1)).thenReturn(Optional.of(examenExistente));
        when(atencionClient.obtenerAtencionPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> examenService.actualizarExamen(1, dto)
        );

        assertNotNull(exception);
        verify(examenRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(999);
        verify(examenRepository, never()).save(any(Examen.class));
    }

    @Test
    void eliminarExamen_deberiaEliminarExamenCuandoExiste() {
        // Given
        Examen examen = crearExamen(
                1,
                "Examen a eliminar",
                "Resultado",
                "2026-06-29",
                1
        );

        when(examenRepository.findById(1)).thenReturn(Optional.of(examen));

        // When
        examenService.eliminarExamen(1);

        // Then
        verify(examenRepository, times(1)).findById(1);
        verify(examenRepository, times(1)).delete(examen);
    }

    @Test
    void eliminarExamen_cuandoNoExiste_deberiaLanzarExcepcion() {
        // Given
        when(examenRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> examenService.eliminarExamen(99)
        );

        assertNotNull(exception);
        verify(examenRepository, times(1)).findById(99);
        verify(examenRepository, never()).delete(any(Examen.class));
    }

    @Test
    void buscarDetallePorId_deberiaRetornarDetalleExamen() {
        // Given
        Examen examen = crearExamen(
                1,
                "Hemograma",
                "Normal",
                "2026-06-30",
                3
        );

        AtencionDetalleDTO atencionDetalleDTO = mock(AtencionDetalleDTO.class);

        when(examenRepository.findById(1)).thenReturn(Optional.of(examen));
        when(atencionClient.obtenerAtencionPorId(3)).thenReturn(atencionDetalleDTO);

        // When
        ExamenDetalleDTO resultado = examenService.buscarDetallePorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdExamen());
        assertEquals("Hemograma", resultado.getNombreExamen());
        assertEquals("Normal", resultado.getResultado());
        assertEquals("2026-06-30", resultado.getFechaExamen());

        verify(examenRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(3);
    }

    @Test
    void buscarDetallePorId_cuandoExamenNoExiste_noDebeConsultarAtencion() {
        // Given
        when(examenRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> examenService.buscarDetallePorId(99)
        );

        assertNotNull(exception);
        verify(examenRepository, times(1)).findById(99);
        verify(atencionClient, never()).obtenerAtencionPorId(anyInt());
    }

    @Test
    void buscarDetallePorId_cuandoAtencionAsociadaNoExiste_deberiaLanzarReglaNegocioException() {
        // Given
        Examen examen = crearExamen(
                1,
                "Hemograma",
                "Normal",
                "2026-06-30",
                999
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/atenciones/999",
                "Atención no encontrada"
        );

        when(examenRepository.findById(1)).thenReturn(Optional.of(examen));
        when(atencionClient.obtenerAtencionPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> examenService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(examenRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(999);
    }

    @Test
    void buscarDetallePorId_cuandoAtencionServiceFalla_deberiaLanzarReglaNegocioException() {
        // Given
        Examen examen = crearExamen(
                1,
                "Hemograma",
                "Normal",
                "2026-06-30",
                3
        );

        FeignException errorRemoto = crearFeignInternalServerError(
                "/api/atenciones/3",
                "Servicio atención no disponible"
        );

        when(examenRepository.findById(1)).thenReturn(Optional.of(examen));
        when(atencionClient.obtenerAtencionPorId(3)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> examenService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(examenRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(3);
    }

    private Examen crearExamen(Integer id, String nombre, String resultado, String fecha, Integer atencionId) {
        Examen examen = new Examen();
        examen.setIdExamen(id);
        examen.setNombreExamen(nombre);
        examen.setResultado(resultado);
        examen.setFechaExamen(fecha);
        examen.setAtencionId(atencionId);
        return examen;
    }

    private ExamenRequestDTO crearRequestDTO(String nombre, String resultado, String fecha, Integer atencionId) {
        ExamenRequestDTO dto = new ExamenRequestDTO();
        dto.setNombreExamen(nombre);
        dto.setResultado(resultado);
        dto.setFechaExamen(fecha);
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