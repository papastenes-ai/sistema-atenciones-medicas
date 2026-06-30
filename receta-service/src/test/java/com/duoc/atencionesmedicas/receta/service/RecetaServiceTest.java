package com.duoc.atencionesmedicas.receta.service;

import com.duoc.atencionesmedicas.receta.client.DiagnosticoClient;
import com.duoc.atencionesmedicas.receta.dto.DiagnosticoDetalleDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaDetalleDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaRequestDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaResponseDTO;
import com.duoc.atencionesmedicas.receta.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.receta.exception.ReglaNegocioException;
import com.duoc.atencionesmedicas.receta.model.Receta;
import com.duoc.atencionesmedicas.receta.repository.RecetaRepository;
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
class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private DiagnosticoClient diagnosticoClient;

    @InjectMocks
    private RecetaService recetaService;

    @Test
    void listarRecetas_deberiaRetornarListaDeRecetas() {
        // Given
        Receta receta = crearReceta(
                1,
                "Paracetamol",
                "500 mg cada 8 horas",
                "Tomar con agua después de comer",
                "2026-06-30",
                10
        );

        when(recetaRepository.findAll()).thenReturn(List.of(receta));

        // When
        List<RecetaResponseDTO> resultado = recetaService.listarRecetas();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdReceta());
        assertEquals("Paracetamol", resultado.get(0).getMedicamento());
        assertEquals("500 mg cada 8 horas", resultado.get(0).getDosis());
        assertEquals("Tomar con agua después de comer", resultado.get(0).getIndicaciones());
        assertEquals("2026-06-30", resultado.get(0).getFechaReceta());
        assertEquals(10, resultado.get(0).getDiagnosticoId());

        verify(recetaRepository, times(1)).findAll();
    }

    @Test
    void listarRecetas_cuandoNoHayRecetas_deberiaRetornarListaVacia() {
        // Given
        when(recetaRepository.findAll()).thenReturn(List.of());

        // When
        List<RecetaResponseDTO> resultado = recetaService.listarRecetas();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(recetaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarReceta() {
        // Given
        Receta receta = crearReceta(
                1,
                "Ibuprofeno",
                "400 mg cada 12 horas",
                "No tomar en ayunas",
                "2026-06-29",
                5
        );

        when(recetaRepository.findById(1)).thenReturn(Optional.of(receta));

        // When
        RecetaResponseDTO resultado = recetaService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReceta());
        assertEquals("Ibuprofeno", resultado.getMedicamento());
        assertEquals("400 mg cada 12 horas", resultado.getDosis());
        assertEquals("No tomar en ayunas", resultado.getIndicaciones());
        assertEquals("2026-06-29", resultado.getFechaReceta());
        assertEquals(5, resultado.getDiagnosticoId());

        verify(recetaRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(recetaRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> recetaService.buscarPorId(99)
        );

        assertEquals("Receta no encontrada con id: 99", exception.getMessage());

        verify(recetaRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorDiagnosticoId_cuandoExistenRecetas_deberiaRetornarLista() {
        // Given
        Receta receta = crearReceta(
                1,
                "Amoxicilina",
                "875 mg cada 12 horas",
                "Tomar por 7 días",
                "2026-06-28",
                7
        );

        when(recetaRepository.findByDiagnosticoId(7)).thenReturn(List.of(receta));

        // When
        List<RecetaResponseDTO> resultado = recetaService.buscarPorDiagnosticoId(7);

        // Then
        assertEquals(1, resultado.size());
        assertEquals(7, resultado.get(0).getDiagnosticoId());
        assertEquals("Amoxicilina", resultado.get(0).getMedicamento());
        assertEquals("875 mg cada 12 horas", resultado.get(0).getDosis());

        verify(recetaRepository, times(1)).findByDiagnosticoId(7);
    }

    @Test
    void buscarPorDiagnosticoId_cuandoNoExistenRecetas_deberiaLanzarExcepcion() {
        // Given
        when(recetaRepository.findByDiagnosticoId(999)).thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> recetaService.buscarPorDiagnosticoId(999)
        );

        assertEquals("No existen recetas para el diagnóstico id: 999", exception.getMessage());

        verify(recetaRepository, times(1)).findByDiagnosticoId(999);
    }

    @Test
    void guardarReceta_cuandoDiagnosticoExiste_deberiaGuardarYRetornarReceta() {
        // Given
        RecetaRequestDTO dto = crearRequestDTO(
                "Loratadina",
                "10 mg cada 24 horas",
                "Tomar por la mañana",
                "2026-06-27",
                3
        );

        DiagnosticoDetalleDTO diagnosticoDetalle = crearDiagnosticoDetalle(3);

        Receta recetaGuardada = crearReceta(
                1,
                dto.getMedicamento(),
                dto.getDosis(),
                dto.getIndicaciones(),
                dto.getFechaReceta(),
                dto.getDiagnosticoId()
        );

        when(diagnosticoClient.obtenerDiagnosticoPorId(3)).thenReturn(diagnosticoDetalle);
        when(recetaRepository.save(any(Receta.class))).thenReturn(recetaGuardada);

        // When
        RecetaResponseDTO resultado = recetaService.guardarReceta(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReceta());
        assertEquals("Loratadina", resultado.getMedicamento());
        assertEquals("10 mg cada 24 horas", resultado.getDosis());
        assertEquals("Tomar por la mañana", resultado.getIndicaciones());
        assertEquals("2026-06-27", resultado.getFechaReceta());
        assertEquals(3, resultado.getDiagnosticoId());

        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(3);
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    void guardarReceta_cuandoDiagnosticoNoExiste_noDebeGuardar() {
        // Given
        RecetaRequestDTO dto = crearRequestDTO(
                "Medicamento prueba",
                "Dosis prueba",
                "Indicaciones prueba",
                "2026-06-27",
                999
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/diagnosticos/999/detalle",
                "Diagnóstico no encontrado"
        );

        when(diagnosticoClient.obtenerDiagnosticoPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> recetaService.guardarReceta(dto)
        );

        assertNotNull(exception);
        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(999);
        verify(recetaRepository, never()).save(any(Receta.class));
    }

    @Test
    void guardarReceta_cuandoDiagnosticoServiceFalla_noDebeGuardar() {
        // Given
        RecetaRequestDTO dto = crearRequestDTO(
                "Medicamento remoto",
                "Dosis remota",
                "Indicaciones remotas",
                "2026-06-27",
                2
        );

        FeignException errorRemoto = crearFeignInternalServerError(
                "/api/diagnosticos/2/detalle",
                "Servicio diagnóstico no disponible"
        );

        when(diagnosticoClient.obtenerDiagnosticoPorId(2)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> recetaService.guardarReceta(dto)
        );

        assertNotNull(exception);
        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(2);
        verify(recetaRepository, never()).save(any(Receta.class));
    }

    @Test
    void actualizarReceta_cuandoExisteYDiagnosticoExiste_deberiaActualizarYRetornarReceta() {
        // Given
        Receta recetaExistente = crearReceta(
                1,
                "Medicamento antiguo",
                "Dosis antigua",
                "Indicaciones antiguas",
                "2026-06-20",
                1
        );

        RecetaRequestDTO dto = crearRequestDTO(
                "Medicamento actualizado",
                "Dosis actualizada",
                "Indicaciones actualizadas",
                "2026-06-30",
                4
        );

        DiagnosticoDetalleDTO diagnosticoDetalle = crearDiagnosticoDetalle(4);

        Receta recetaActualizada = crearReceta(
                1,
                dto.getMedicamento(),
                dto.getDosis(),
                dto.getIndicaciones(),
                dto.getFechaReceta(),
                dto.getDiagnosticoId()
        );

        when(recetaRepository.findById(1)).thenReturn(Optional.of(recetaExistente));
        when(diagnosticoClient.obtenerDiagnosticoPorId(4)).thenReturn(diagnosticoDetalle);
        when(recetaRepository.save(any(Receta.class))).thenReturn(recetaActualizada);

        // When
        RecetaResponseDTO resultado = recetaService.actualizarReceta(1, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReceta());
        assertEquals("Medicamento actualizado", resultado.getMedicamento());
        assertEquals("Dosis actualizada", resultado.getDosis());
        assertEquals("Indicaciones actualizadas", resultado.getIndicaciones());
        assertEquals("2026-06-30", resultado.getFechaReceta());
        assertEquals(4, resultado.getDiagnosticoId());

        verify(recetaRepository, times(1)).findById(1);
        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(4);
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    void actualizarReceta_cuandoRecetaNoExiste_noDebeValidarDiagnosticoNiGuardar() {
        // Given
        RecetaRequestDTO dto = crearRequestDTO(
                "Medicamento",
                "Dosis",
                "Indicaciones",
                "2026-06-30",
                1
        );

        when(recetaRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> recetaService.actualizarReceta(99, dto)
        );

        assertEquals("Receta no encontrada con id: 99", exception.getMessage());

        verify(recetaRepository, times(1)).findById(99);
        verify(diagnosticoClient, never()).obtenerDiagnosticoPorId(anyInt());
        verify(recetaRepository, never()).save(any(Receta.class));
    }

    @Test
    void actualizarReceta_cuandoDiagnosticoNoExiste_noDebeGuardar() {
        // Given
        Receta recetaExistente = crearReceta(
                1,
                "Medicamento antiguo",
                "Dosis antigua",
                "Indicaciones antiguas",
                "2026-06-20",
                1
        );

        RecetaRequestDTO dto = crearRequestDTO(
                "Medicamento actualizado",
                "Dosis actualizada",
                "Indicaciones actualizadas",
                "2026-06-30",
                999
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/diagnosticos/999/detalle",
                "Diagnóstico no encontrado"
        );

        when(recetaRepository.findById(1)).thenReturn(Optional.of(recetaExistente));
        when(diagnosticoClient.obtenerDiagnosticoPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> recetaService.actualizarReceta(1, dto)
        );

        assertNotNull(exception);
        verify(recetaRepository, times(1)).findById(1);
        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(999);
        verify(recetaRepository, never()).save(any(Receta.class));
    }

    @Test
    void eliminarReceta_cuandoExiste_deberiaEliminarReceta() {
        // Given
        Receta receta = crearReceta(
                1,
                "Receta a eliminar",
                "Dosis",
                "Indicaciones",
                "2026-06-29",
                1
        );

        when(recetaRepository.findById(1)).thenReturn(Optional.of(receta));

        // When
        recetaService.eliminarReceta(1);

        // Then
        verify(recetaRepository, times(1)).findById(1);
        verify(recetaRepository, times(1)).delete(receta);
    }

    @Test
    void eliminarReceta_cuandoNoExiste_noDebeEliminarYLanzaExcepcion() {
        // Given
        when(recetaRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> recetaService.eliminarReceta(99)
        );

        assertEquals("Receta no encontrada con id: 99", exception.getMessage());

        verify(recetaRepository, times(1)).findById(99);
        verify(recetaRepository, never()).delete(any(Receta.class));
    }

    @Test
    void buscarDetallePorId_cuandoExisteRecetaYDiagnostico_deberiaRetornarDetalle() {
        // Given
        Receta receta = crearReceta(
                1,
                "Salbutamol",
                "2 puff cada 8 horas",
                "Usar con aerocámara",
                "2026-06-30",
                8
        );

        DiagnosticoDetalleDTO diagnosticoDetalle = crearDiagnosticoDetalle(8);

        when(recetaRepository.findById(1)).thenReturn(Optional.of(receta));
        when(diagnosticoClient.obtenerDiagnosticoPorId(8)).thenReturn(diagnosticoDetalle);

        // When
        RecetaDetalleDTO resultado = recetaService.buscarDetallePorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReceta());
        assertEquals("Salbutamol", resultado.getMedicamento());
        assertEquals("2 puff cada 8 horas", resultado.getDosis());
        assertEquals("Usar con aerocámara", resultado.getIndicaciones());
        assertEquals("2026-06-30", resultado.getFechaReceta());
        assertNotNull(resultado.getDiagnostico());
        assertEquals(8, resultado.getDiagnostico().getIdDiagnostico());

        verify(recetaRepository, times(1)).findById(1);
        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(8);
    }

    @Test
    void buscarDetallePorId_cuandoRecetaNoExiste_noDebeConsultarDiagnostico() {
        // Given
        when(recetaRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> recetaService.buscarDetallePorId(99)
        );

        assertEquals("Receta no encontrada con id: 99", exception.getMessage());

        verify(recetaRepository, times(1)).findById(99);
        verify(diagnosticoClient, never()).obtenerDiagnosticoPorId(anyInt());
    }

    @Test
    void buscarDetallePorId_cuandoDiagnosticoAsociadoNoExiste_deberiaLanzarReglaNegocioException() {
        // Given
        Receta receta = crearReceta(
                1,
                "Receta con diagnóstico inexistente",
                "Dosis",
                "Indicaciones",
                "2026-06-30",
                999
        );

        FeignException.NotFound notFound = crearFeignNotFound(
                "/api/diagnosticos/999/detalle",
                "Diagnóstico no encontrado"
        );

        when(recetaRepository.findById(1)).thenReturn(Optional.of(receta));
        when(diagnosticoClient.obtenerDiagnosticoPorId(999)).thenThrow(notFound);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> recetaService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(recetaRepository, times(1)).findById(1);
        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(999);
    }

    @Test
    void buscarDetallePorId_cuandoDiagnosticoServiceFalla_deberiaLanzarReglaNegocioException() {
        // Given
        Receta receta = crearReceta(
                1,
                "Receta remota",
                "Dosis remota",
                "Indicaciones remotas",
                "2026-06-30",
                2
        );

        FeignException errorRemoto = crearFeignInternalServerError(
                "/api/diagnosticos/2/detalle",
                "Servicio diagnóstico no disponible"
        );

        when(recetaRepository.findById(1)).thenReturn(Optional.of(receta));
        when(diagnosticoClient.obtenerDiagnosticoPorId(2)).thenThrow(errorRemoto);

        // When - Then
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> recetaService.buscarDetallePorId(1)
        );

        assertNotNull(exception);
        verify(recetaRepository, times(1)).findById(1);
        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(2);
    }

    private Receta crearReceta(
            Integer id,
            String medicamento,
            String dosis,
            String indicaciones,
            String fechaReceta,
            Integer diagnosticoId
    ) {
        Receta receta = new Receta();
        receta.setIdReceta(id);
        receta.setMedicamento(medicamento);
        receta.setDosis(dosis);
        receta.setIndicaciones(indicaciones);
        receta.setFechaReceta(fechaReceta);
        receta.setDiagnosticoId(diagnosticoId);
        return receta;
    }

    private RecetaRequestDTO crearRequestDTO(
            String medicamento,
            String dosis,
            String indicaciones,
            String fechaReceta,
            Integer diagnosticoId
    ) {
        RecetaRequestDTO dto = new RecetaRequestDTO();
        dto.setMedicamento(medicamento);
        dto.setDosis(dosis);
        dto.setIndicaciones(indicaciones);
        dto.setFechaReceta(fechaReceta);
        dto.setDiagnosticoId(diagnosticoId);
        return dto;
    }

    private DiagnosticoDetalleDTO crearDiagnosticoDetalle(Integer id) {
        DiagnosticoDetalleDTO diagnostico = new DiagnosticoDetalleDTO();
        diagnostico.setIdDiagnostico(id);
        diagnostico.setDescripcion("Diagnóstico de prueba");
        diagnostico.setTratamiento("Tratamiento de prueba");
        diagnostico.setFechaDiagnostico("2026-06-30");
        return diagnostico;
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