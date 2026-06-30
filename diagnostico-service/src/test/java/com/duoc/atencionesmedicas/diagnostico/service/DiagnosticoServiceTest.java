package com.duoc.atencionesmedicas.diagnostico.service;

import com.duoc.atencionesmedicas.diagnostico.client.AtencionClient;
import com.duoc.atencionesmedicas.diagnostico.dto.AtencionDetalleDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoDetalleDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoRequestDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoResponseDTO;
import com.duoc.atencionesmedicas.diagnostico.model.Diagnostico;
import com.duoc.atencionesmedicas.diagnostico.repository.DiagnosticoRepository;
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
class DiagnosticoServiceTest {

    @Mock
    private DiagnosticoRepository diagnosticoRepository;

    @Mock
    private AtencionClient atencionClient;

    @InjectMocks
    private DiagnosticoService diagnosticoService;

    @Test
    void listarDiagnosticos_deberiaRetornarListaDeDiagnosticos() {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setIdDiagnostico(1);
        diagnostico.setDescripcion("Faringitis aguda");
        diagnostico.setTratamiento("Reposo e hidratación");
        diagnostico.setFechaDiagnostico("2026-06-24");
        diagnostico.setAtencionId(1);

        when(diagnosticoRepository.findAll()).thenReturn(List.of(diagnostico));

        List<DiagnosticoResponseDTO> resultado = diagnosticoService.listarDiagnosticos();

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdDiagnostico());
        assertEquals("Faringitis aguda", resultado.get(0).getDescripcion());
        assertEquals("Reposo e hidratación", resultado.get(0).getTratamiento());
        assertEquals("2026-06-24", resultado.get(0).getFechaDiagnostico());
        assertEquals(1, resultado.get(0).getAtencionId());

        verify(diagnosticoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarDiagnostico() {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setIdDiagnostico(1);
        diagnostico.setDescripcion("Migraña");
        diagnostico.setTratamiento("Analgésico y reposo");
        diagnostico.setFechaDiagnostico("2026-06-25");
        diagnostico.setAtencionId(2);

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnostico));

        DiagnosticoResponseDTO resultado = diagnosticoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDiagnostico());
        assertEquals("Migraña", resultado.getDescripcion());
        assertEquals("Analgésico y reposo", resultado.getTratamiento());
        assertEquals("2026-06-25", resultado.getFechaDiagnostico());
        assertEquals(2, resultado.getAtencionId());

        verify(diagnosticoRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorAtencionId_deberiaRetornarDiagnosticosFiltrados() {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setIdDiagnostico(1);
        diagnostico.setDescripcion("Dolor abdominal");
        diagnostico.setTratamiento("Solicitar exámenes");
        diagnostico.setFechaDiagnostico("2026-06-26");
        diagnostico.setAtencionId(5);

        when(diagnosticoRepository.findByAtencionId(5)).thenReturn(List.of(diagnostico));

        List<DiagnosticoResponseDTO> resultado = diagnosticoService.buscarPorAtencionId(5);

        assertEquals(1, resultado.size());
        assertEquals(5, resultado.get(0).getAtencionId());
        assertEquals("Dolor abdominal", resultado.get(0).getDescripcion());

        verify(diagnosticoRepository, times(1)).findByAtencionId(5);
    }

    @Test
    void guardarDiagnostico_deberiaValidarAtencionGuardarYRetornarDiagnostico() {
        DiagnosticoRequestDTO dto = new DiagnosticoRequestDTO();
        dto.setDescripcion("Bronquitis");
        dto.setTratamiento("Antibiótico y reposo");
        dto.setFechaDiagnostico("2026-06-27");
        dto.setAtencionId(1);

        AtencionDetalleDTO atencionDetalleDTO = mock(AtencionDetalleDTO.class);

        Diagnostico diagnosticoGuardado = new Diagnostico();
        diagnosticoGuardado.setIdDiagnostico(1);
        diagnosticoGuardado.setDescripcion(dto.getDescripcion());
        diagnosticoGuardado.setTratamiento(dto.getTratamiento());
        diagnosticoGuardado.setFechaDiagnostico(dto.getFechaDiagnostico());
        diagnosticoGuardado.setAtencionId(dto.getAtencionId());

        when(atencionClient.obtenerAtencionPorId(1)).thenReturn(atencionDetalleDTO);
        when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnosticoGuardado);

        DiagnosticoResponseDTO resultado = diagnosticoService.guardarDiagnostico(dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDiagnostico());
        assertEquals("Bronquitis", resultado.getDescripcion());
        assertEquals("Antibiótico y reposo", resultado.getTratamiento());
        assertEquals("2026-06-27", resultado.getFechaDiagnostico());
        assertEquals(1, resultado.getAtencionId());

        verify(atencionClient, times(1)).obtenerAtencionPorId(1);
        verify(diagnosticoRepository, times(1)).save(any(Diagnostico.class));
    }

    @Test
    void actualizarDiagnostico_deberiaActualizarYRetornarDiagnostico() {
        Diagnostico diagnosticoExistente = new Diagnostico();
        diagnosticoExistente.setIdDiagnostico(1);
        diagnosticoExistente.setDescripcion("Diagnóstico antiguo");
        diagnosticoExistente.setTratamiento("Tratamiento antiguo");
        diagnosticoExistente.setFechaDiagnostico("2026-06-20");
        diagnosticoExistente.setAtencionId(1);

        DiagnosticoRequestDTO dto = new DiagnosticoRequestDTO();
        dto.setDescripcion("Diagnóstico actualizado");
        dto.setTratamiento("Tratamiento actualizado");
        dto.setFechaDiagnostico("2026-06-28");
        dto.setAtencionId(2);

        AtencionDetalleDTO atencionDetalleDTO = mock(AtencionDetalleDTO.class);

        Diagnostico diagnosticoActualizado = new Diagnostico();
        diagnosticoActualizado.setIdDiagnostico(1);
        diagnosticoActualizado.setDescripcion(dto.getDescripcion());
        diagnosticoActualizado.setTratamiento(dto.getTratamiento());
        diagnosticoActualizado.setFechaDiagnostico(dto.getFechaDiagnostico());
        diagnosticoActualizado.setAtencionId(dto.getAtencionId());

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnosticoExistente));
        when(atencionClient.obtenerAtencionPorId(2)).thenReturn(atencionDetalleDTO);
        when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnosticoActualizado);

        DiagnosticoResponseDTO resultado = diagnosticoService.actualizarDiagnostico(1, dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDiagnostico());
        assertEquals("Diagnóstico actualizado", resultado.getDescripcion());
        assertEquals("Tratamiento actualizado", resultado.getTratamiento());
        assertEquals("2026-06-28", resultado.getFechaDiagnostico());
        assertEquals(2, resultado.getAtencionId());

        verify(diagnosticoRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(2);
        verify(diagnosticoRepository, times(1)).save(any(Diagnostico.class));
    }

    @Test
    void eliminarDiagnostico_deberiaEliminarDiagnosticoCuandoExiste() {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setIdDiagnostico(1);
        diagnostico.setDescripcion("Diagnóstico a eliminar");
        diagnostico.setTratamiento("Tratamiento");
        diagnostico.setFechaDiagnostico("2026-06-29");
        diagnostico.setAtencionId(1);

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnostico));

        diagnosticoService.eliminarDiagnostico(1);

        verify(diagnosticoRepository, times(1)).findById(1);
        verify(diagnosticoRepository, times(1)).delete(diagnostico);
    }

    @Test
    void buscarDetallePorId_deberiaRetornarDetalleDiagnostico() {
        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setIdDiagnostico(1);
        diagnostico.setDescripcion("Lumbalgia");
        diagnostico.setTratamiento("Kinesiología");
        diagnostico.setFechaDiagnostico("2026-06-30");
        diagnostico.setAtencionId(3);

        AtencionDetalleDTO atencionDetalleDTO = mock(AtencionDetalleDTO.class);

        when(diagnosticoRepository.findById(1)).thenReturn(Optional.of(diagnostico));
        when(atencionClient.obtenerAtencionPorId(3)).thenReturn(atencionDetalleDTO);

        DiagnosticoDetalleDTO resultado = diagnosticoService.buscarDetallePorId(1);

        assertNotNull(resultado);

        verify(diagnosticoRepository, times(1)).findById(1);
        verify(atencionClient, times(1)).obtenerAtencionPorId(3);
    }
}