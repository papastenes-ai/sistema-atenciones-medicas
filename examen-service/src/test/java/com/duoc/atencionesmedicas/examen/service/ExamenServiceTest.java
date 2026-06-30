package com.duoc.atencionesmedicas.examen.service;

import com.duoc.atencionesmedicas.examen.client.AtencionClient;
import com.duoc.atencionesmedicas.examen.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenDetalleDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenRequestDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenResponseDTO;
import com.duoc.atencionesmedicas.examen.model.Examen;
import com.duoc.atencionesmedicas.examen.repository.ExamenRepository;
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
class ExamenServiceTest {

    @Mock
    private ExamenRepository examenRepository;

    @Mock
    private AtencionClient atencionClient;

    @InjectMocks
    private ExamenService examenService;

    @Test
    void listarExamenes_deberiaRetornarListaDeExamenes() {
        Examen examen = new Examen();
        examen.setIdExamen(1);
        examen.setNombreExamen("Hemograma");
        examen.setResultado("Normal");
        examen.setFechaExamen("2026-06-24");
        examen.setAtencionId(1);

        when(examenRepository.findAll()).thenReturn(List.of(examen));

        List<ExamenResponseDTO> resultado = examenService.listarExamenes();

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
        Examen examen = new Examen();
        examen.setIdExamen(1);
        examen.setNombreExamen("Radiografía");
        examen.setResultado("Sin hallazgos");
        examen.setFechaExamen("2026-06-25");
        examen.setAtencionId(2);

        when(examenRepository.findById(1)).thenReturn(Optional.of(examen));

        ExamenResponseDTO resultado = examenService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdExamen());
        assertEquals("Radiografía", resultado.getNombreExamen());
        assertEquals("Sin hallazgos", resultado.getResultado());
        assertEquals("2026-06-25", resultado.getFechaExamen());
        assertEquals(2, resultado.getAtencionId());

        verify(examenRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorAtencionId_deberiaRetornarExamenesFiltrados() {
        Examen examen = new Examen();
        examen.setIdExamen(1);
        examen.setNombreExamen("Perfil lipídico");
        examen.setResultado("Colesterol elevado");
        examen.setFechaExamen("2026-06-26");
        examen.setAtencionId(5);

        when(examenRepository.findByAtencionId(5)).thenReturn(List.of(examen));

        List<ExamenResponseDTO> resultado = examenService.buscarPorAtencionId(5);

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getAtencionId());
        assertEquals("Perfil lipídico", resultado.get(0).getNombreExamen());

        verify(examenRepository, times(1)).findByAtencionId(5);
    }

    @Test
    void buscarPorNombreExamen_deberiaRetornarExamenesFiltrados() {
        Examen examen = new Examen();
        examen.setIdExamen(1);
        examen.setNombreExamen("Hemograma completo");
        examen.setResultado("Normal");
        examen.setFechaExamen("2026-06-26");
        examen.setAtencionId(1);

        when(examenRepository.findByNombreExamenContainingIgnoreCase("Hemo"))
                .thenReturn(List.of(examen));

        List<ExamenResponseDTO> resultado = examenService.buscarPorNombreExamen("Hemo");

        assertEquals(1, resultado.size());
        assertEquals("Hemograma completo", resultado.get(0).getNombreExamen());
        assertEquals("Normal", resultado.get(0).getResultado());

        verify(examenRepository, times(1))
                .findByNombreExamenContainingIgnoreCase("Hemo");
    }

    @Test
    void guardarExamen_deberiaValidarAtencionGuardarYRetornarExamen() {
        ExamenRequestDTO dto = new ExamenRequestDTO();
        dto.setNombreExamen("Examen de orina");
        dto.setResultado("Normal");
        dto.setFechaExamen("2026-06-27");
        dto.setAtencionId(1);

        AtencionDetalleDTO atencionDetalleDTO = mock(AtencionDetalleDTO.class);

        Examen examenGuardado = new Examen();
        examenGuardado.setIdExamen(1);
        examenGuardado.setNombreExamen(dto.getNombreExamen());
        examenGuardado.setResultado(dto.getResultado());
        examenGuardado.setFechaExamen(dto.getFechaExamen());
        examenGuardado.setAtencionId(dto.getAtencionId());

        when(atencionClient.obtenerAtencionPorId(1)).thenReturn(atencionDetalleDTO);
        when(examenRepository.save(any(Examen.class))).thenReturn(examenGuardado);

        ExamenResponseDTO resultado = examenService.guardarExamen(dto);

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
    void actualizarExamen_deberiaActualizarYRetornarExamen() {
        Examen examenExistente = new Examen();
        examenExistente.setIdExamen(1);
        examenExistente.setNombreExamen("Examen antiguo");
        examenExistente.setResultado("Resultado antiguo");
        examenExistente.setFechaExamen("2026-06-20");
        examenExistente.setAtencionId(1);

        ExamenRequestDTO dto = new ExamenRequestDTO();
        dto.setNombreExamen("Examen actualizado");
        dto.setResultado("Resultado actualizado");
        dto.setFechaExamen("2026-06-28");
        dto.setAtencionId(2);

        AtencionDetalleDTO atencionDetalleDTO = mock(AtencionDetalleDTO.class);

        Examen examenActualizado = new Examen();
        examenActualizado.setIdExamen(1);
        examenActualizado.setNombreExamen(dto.getNombreExamen());
        examenActualizado.setResultado(dto.getResultado());
        examenActualizado.setFechaExamen(dto.getFechaExamen());
        examenActualizado.setAtencionId(dto.getAtencionId());

        when(examenRepository.findById(1)).thenReturn(Optional.of(examenExistente));
        when(atencionClient.obtenerAtencionPorId(2)).thenReturn(atencionDetalleDTO);
        when(examenRepository.save(any(Examen.class))).thenReturn(examenActualizado);

        ExamenResponseDTO resultado = examenService.actualizarExamen(1, dto);

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
    void eliminarExamen_deberiaEliminarExamenCuandoExiste() {
        Examen examen = new Examen();
        examen.setIdExamen(1);
        examen.setNombreExamen("Examen a eliminar");
        examen.setResultado("Resultado");
        examen.setFechaExamen("2026-06-29");
        examen.setAtencionId(1);

        when(examenRepository.findById(1)).thenReturn(Optional.of(examen));

        examenService.eliminarExamen(1);

        verify(examenRepository, times(1)).findById(1);
        verify(examenRepository, times(1)).delete(examen);
    }

    @Test
    void buscarDetallePorId_deberiaRetornarDetalleExamen() {
        Examen examen = new Examen();
        examen.setIdExamen(1);
        examen.setNombreExamen("Hemograma");
        examen.setResultado("Normal");
        examen.setFechaExamen("2026-06-30");
        examen.setAtencionId(3);

        AtencionDetalleDTO atencionDetalleDTO = mock(AtencionDetalleDTO.class);

        when(examenRepository.findById(1)).thenReturn(Optional.of(examen));
        when(atencionClient.obtenerAtencionPorId(3)).thenReturn(atencionDetalleDTO);

        ExamenDetalleDTO resultado = examenService.buscarDetallePorId(1);

        assertNotNull(resultado);

        verify(examenRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(3);
    }
}