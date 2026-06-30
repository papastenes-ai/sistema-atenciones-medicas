package com.duoc.atencionesmedicas.receta.service;

import com.duoc.atencionesmedicas.receta.client.DiagnosticoClient;
import com.duoc.atencionesmedicas.receta.dto.DiagnosticoDetalleDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaDetalleDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaRequestDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaResponseDTO;
import com.duoc.atencionesmedicas.receta.model.Receta;
import com.duoc.atencionesmedicas.receta.repository.RecetaRepository;
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
class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private DiagnosticoClient diagnosticoClient;

    @InjectMocks
    private RecetaService recetaService;

    @Test
    void listarRecetas_deberiaRetornarListaDeRecetas() {
        Receta receta = new Receta();
        receta.setIdReceta(1);
        receta.setMedicamento("Paracetamol");
        receta.setDosis("500 mg cada 8 horas");
        receta.setIndicaciones("Tomar después de las comidas");
        receta.setFechaReceta("2026-06-24");
        receta.setDiagnosticoId(1);

        when(recetaRepository.findAll()).thenReturn(List.of(receta));

        List<RecetaResponseDTO> resultado = recetaService.listarRecetas();

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdReceta());
        assertEquals("Paracetamol", resultado.get(0).getMedicamento());
        assertEquals("500 mg cada 8 horas", resultado.get(0).getDosis());
        assertEquals("Tomar después de las comidas", resultado.get(0).getIndicaciones());
        assertEquals("2026-06-24", resultado.get(0).getFechaReceta());
        assertEquals(1, resultado.get(0).getDiagnosticoId());

        verify(recetaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarReceta() {
        Receta receta = new Receta();
        receta.setIdReceta(1);
        receta.setMedicamento("Ibuprofeno");
        receta.setDosis("400 mg cada 12 horas");
        receta.setIndicaciones("Tomar con abundante agua");
        receta.setFechaReceta("2026-06-25");
        receta.setDiagnosticoId(2);

        when(recetaRepository.findById(1)).thenReturn(Optional.of(receta));

        RecetaResponseDTO resultado = recetaService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReceta());
        assertEquals("Ibuprofeno", resultado.getMedicamento());
        assertEquals("400 mg cada 12 horas", resultado.getDosis());
        assertEquals("Tomar con abundante agua", resultado.getIndicaciones());
        assertEquals("2026-06-25", resultado.getFechaReceta());
        assertEquals(2, resultado.getDiagnosticoId());

        verify(recetaRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorDiagnosticoId_deberiaRetornarRecetasFiltradas() {
        Receta receta = new Receta();
        receta.setIdReceta(1);
        receta.setMedicamento("Amoxicilina");
        receta.setDosis("500 mg cada 8 horas");
        receta.setIndicaciones("Completar tratamiento por 7 días");
        receta.setFechaReceta("2026-06-26");
        receta.setDiagnosticoId(5);

        when(recetaRepository.findByDiagnosticoId(5)).thenReturn(List.of(receta));

        List<RecetaResponseDTO> resultado = recetaService.buscarPorDiagnosticoId(5);

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getDiagnosticoId());
        assertEquals("Amoxicilina", resultado.get(0).getMedicamento());

        verify(recetaRepository, times(1)).findByDiagnosticoId(5);
    }

    @Test
    void guardarReceta_deberiaValidarDiagnosticoGuardarYRetornarReceta() {
        RecetaRequestDTO dto = new RecetaRequestDTO();
        dto.setMedicamento("Loratadina");
        dto.setDosis("10 mg al día");
        dto.setIndicaciones("Tomar en la mañana");
        dto.setFechaReceta("2026-06-27");
        dto.setDiagnosticoId(1);

        DiagnosticoDetalleDTO diagnosticoDetalleDTO = mock(DiagnosticoDetalleDTO.class);

        Receta recetaGuardada = new Receta();
        recetaGuardada.setIdReceta(1);
        recetaGuardada.setMedicamento(dto.getMedicamento());
        recetaGuardada.setDosis(dto.getDosis());
        recetaGuardada.setIndicaciones(dto.getIndicaciones());
        recetaGuardada.setFechaReceta(dto.getFechaReceta());
        recetaGuardada.setDiagnosticoId(dto.getDiagnosticoId());

        when(diagnosticoClient.obtenerDiagnosticoPorId(1)).thenReturn(diagnosticoDetalleDTO);
        when(recetaRepository.save(any(Receta.class))).thenReturn(recetaGuardada);

        RecetaResponseDTO resultado = recetaService.guardarReceta(dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReceta());
        assertEquals("Loratadina", resultado.getMedicamento());
        assertEquals("10 mg al día", resultado.getDosis());
        assertEquals("Tomar en la mañana", resultado.getIndicaciones());
        assertEquals("2026-06-27", resultado.getFechaReceta());
        assertEquals(1, resultado.getDiagnosticoId());

        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(1);
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    void actualizarReceta_deberiaActualizarYRetornarReceta() {
        Receta recetaExistente = new Receta();
        recetaExistente.setIdReceta(1);
        recetaExistente.setMedicamento("Medicamento antiguo");
        recetaExistente.setDosis("Dosis antigua");
        recetaExistente.setIndicaciones("Indicaciones antiguas");
        recetaExistente.setFechaReceta("2026-06-20");
        recetaExistente.setDiagnosticoId(1);

        RecetaRequestDTO dto = new RecetaRequestDTO();
        dto.setMedicamento("Medicamento actualizado");
        dto.setDosis("Dosis actualizada");
        dto.setIndicaciones("Indicaciones actualizadas");
        dto.setFechaReceta("2026-06-28");
        dto.setDiagnosticoId(2);

        DiagnosticoDetalleDTO diagnosticoDetalleDTO = mock(DiagnosticoDetalleDTO.class);

        Receta recetaActualizada = new Receta();
        recetaActualizada.setIdReceta(1);
        recetaActualizada.setMedicamento(dto.getMedicamento());
        recetaActualizada.setDosis(dto.getDosis());
        recetaActualizada.setIndicaciones(dto.getIndicaciones());
        recetaActualizada.setFechaReceta(dto.getFechaReceta());
        recetaActualizada.setDiagnosticoId(dto.getDiagnosticoId());

        when(recetaRepository.findById(1)).thenReturn(Optional.of(recetaExistente));
        when(diagnosticoClient.obtenerDiagnosticoPorId(2)).thenReturn(diagnosticoDetalleDTO);
        when(recetaRepository.save(any(Receta.class))).thenReturn(recetaActualizada);

        RecetaResponseDTO resultado = recetaService.actualizarReceta(1, dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdReceta());
        assertEquals("Medicamento actualizado", resultado.getMedicamento());
        assertEquals("Dosis actualizada", resultado.getDosis());
        assertEquals("Indicaciones actualizadas", resultado.getIndicaciones());
        assertEquals("2026-06-28", resultado.getFechaReceta());
        assertEquals(2, resultado.getDiagnosticoId());

        verify(recetaRepository, times(1)).findById(1);
        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(2);
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    void eliminarReceta_deberiaEliminarRecetaCuandoExiste() {
        Receta receta = new Receta();
        receta.setIdReceta(1);
        receta.setMedicamento("Receta a eliminar");
        receta.setDosis("1 comprimido");
        receta.setIndicaciones("Eliminar prueba");
        receta.setFechaReceta("2026-06-29");
        receta.setDiagnosticoId(1);

        when(recetaRepository.findById(1)).thenReturn(Optional.of(receta));

        recetaService.eliminarReceta(1);

        verify(recetaRepository, times(1)).findById(1);
        verify(recetaRepository, times(1)).delete(receta);
    }

    @Test
    void buscarDetallePorId_deberiaRetornarDetalleReceta() {
        Receta receta = new Receta();
        receta.setIdReceta(1);
        receta.setMedicamento("Paracetamol");
        receta.setDosis("500 mg");
        receta.setIndicaciones("Cada 8 horas");
        receta.setFechaReceta("2026-06-30");
        receta.setDiagnosticoId(3);

        DiagnosticoDetalleDTO diagnosticoDetalleDTO = mock(DiagnosticoDetalleDTO.class);

        when(recetaRepository.findById(1)).thenReturn(Optional.of(receta));
        when(diagnosticoClient.obtenerDiagnosticoPorId(3)).thenReturn(diagnosticoDetalleDTO);

        RecetaDetalleDTO resultado = recetaService.buscarDetallePorId(1);

        assertNotNull(resultado);

        verify(recetaRepository, times(1)).findById(1);
        verify(diagnosticoClient, times(1)).obtenerDiagnosticoPorId(3);
    }
}