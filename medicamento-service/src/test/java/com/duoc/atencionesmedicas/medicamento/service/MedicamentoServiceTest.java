package com.duoc.atencionesmedicas.medicamento.service;

import com.duoc.atencionesmedicas.medicamento.dto.MedicamentoRequestDTO;
import com.duoc.atencionesmedicas.medicamento.dto.MedicamentoResponseDTO;
import com.duoc.atencionesmedicas.medicamento.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.medicamento.model.Medicamento;
import com.duoc.atencionesmedicas.medicamento.repository.MedicamentoRepository;
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
class MedicamentoServiceTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @InjectMocks
    private MedicamentoService medicamentoService;

    @Test
    void listarMedicamentos_deberiaRetornarListaDeMedicamentos() {
        // Given
        Medicamento medicamento = crearMedicamento(
                1,
                "Paracetamol",
                "Analgésico y antipirético",
                "Laboratorio Chile",
                100
        );

        when(medicamentoRepository.findAll()).thenReturn(List.of(medicamento));

        // When
        List<MedicamentoResponseDTO> resultado = medicamentoService.listarMedicamentos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdMedicamento());
        assertEquals("Paracetamol", resultado.get(0).getNombreMedicamento());
        assertEquals("Analgésico y antipirético", resultado.get(0).getDescripcion());
        assertEquals("Laboratorio Chile", resultado.get(0).getLaboratorio());
        assertEquals(100, resultado.get(0).getStock());

        verify(medicamentoRepository, times(1)).findAll();
    }

    @Test
    void listarMedicamentos_cuandoNoHayMedicamentos_deberiaRetornarListaVacia() {
        // Given
        when(medicamentoRepository.findAll()).thenReturn(List.of());

        // When
        List<MedicamentoResponseDTO> resultado = medicamentoService.listarMedicamentos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(medicamentoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarMedicamento() {
        // Given
        Medicamento medicamento = crearMedicamento(
                1,
                "Ibuprofeno",
                "Antiinflamatorio",
                "Lab Farma",
                50
        );

        when(medicamentoRepository.findById(1)).thenReturn(Optional.of(medicamento));

        // When
        MedicamentoResponseDTO resultado = medicamentoService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdMedicamento());
        assertEquals("Ibuprofeno", resultado.getNombreMedicamento());
        assertEquals("Antiinflamatorio", resultado.getDescripcion());
        assertEquals("Lab Farma", resultado.getLaboratorio());
        assertEquals(50, resultado.getStock());

        verify(medicamentoRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(medicamentoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicamentoService.buscarPorId(99)
        );

        assertEquals("Medicamento no encontrado con id: 99", exception.getMessage());

        verify(medicamentoRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorNombreMedicamento_cuandoExistenCoincidencias_deberiaRetornarLista() {
        // Given
        Medicamento medicamento = crearMedicamento(
                1,
                "Paracetamol 500 mg",
                "Analgésico",
                "Laboratorio Chile",
                80
        );

        when(medicamentoRepository.findByNombreMedicamentoContainingIgnoreCase("para"))
                .thenReturn(List.of(medicamento));

        // When
        List<MedicamentoResponseDTO> resultado =
                medicamentoService.buscarPorNombreMedicamento("para");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Paracetamol 500 mg", resultado.get(0).getNombreMedicamento());
        assertEquals("Analgésico", resultado.get(0).getDescripcion());
        assertEquals("Laboratorio Chile", resultado.get(0).getLaboratorio());
        assertEquals(80, resultado.get(0).getStock());

        verify(medicamentoRepository, times(1))
                .findByNombreMedicamentoContainingIgnoreCase("para");
    }

    @Test
    void buscarPorNombreMedicamento_cuandoNoHayCoincidencias_deberiaLanzarExcepcion() {
        // Given
        when(medicamentoRepository.findByNombreMedicamentoContainingIgnoreCase("inexistente"))
                .thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicamentoService.buscarPorNombreMedicamento("inexistente")
        );

        assertEquals("No existen medicamentos con nombre: inexistente", exception.getMessage());

        verify(medicamentoRepository, times(1))
                .findByNombreMedicamentoContainingIgnoreCase("inexistente");
    }

    @Test
    void guardarMedicamento_deberiaGuardarYRetornarMedicamento() {
        // Given
        MedicamentoRequestDTO dto = crearRequestDTO(
                "Loratadina",
                "Antialérgico",
                "Pharma Test",
                40
        );

        Medicamento medicamentoGuardado = crearMedicamento(
                1,
                dto.getNombreMedicamento(),
                dto.getDescripcion(),
                dto.getLaboratorio(),
                dto.getStock()
        );

        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(medicamentoGuardado);

        // When
        MedicamentoResponseDTO resultado = medicamentoService.guardarMedicamento(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdMedicamento());
        assertEquals("Loratadina", resultado.getNombreMedicamento());
        assertEquals("Antialérgico", resultado.getDescripcion());
        assertEquals("Pharma Test", resultado.getLaboratorio());
        assertEquals(40, resultado.getStock());

        verify(medicamentoRepository, times(1)).save(any(Medicamento.class));
    }

    @Test
    void actualizarMedicamento_cuandoExiste_deberiaActualizarYRetornarMedicamento() {
        // Given
        Medicamento medicamentoExistente = crearMedicamento(
                1,
                "Medicamento antiguo",
                "Descripción antigua",
                "Laboratorio antiguo",
                10
        );

        MedicamentoRequestDTO dto = crearRequestDTO(
                "Medicamento actualizado",
                "Descripción actualizada",
                "Laboratorio actualizado",
                120
        );

        Medicamento medicamentoActualizado = crearMedicamento(
                1,
                dto.getNombreMedicamento(),
                dto.getDescripcion(),
                dto.getLaboratorio(),
                dto.getStock()
        );

        when(medicamentoRepository.findById(1)).thenReturn(Optional.of(medicamentoExistente));
        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(medicamentoActualizado);

        // When
        MedicamentoResponseDTO resultado = medicamentoService.actualizarMedicamento(1, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdMedicamento());
        assertEquals("Medicamento actualizado", resultado.getNombreMedicamento());
        assertEquals("Descripción actualizada", resultado.getDescripcion());
        assertEquals("Laboratorio actualizado", resultado.getLaboratorio());
        assertEquals(120, resultado.getStock());

        verify(medicamentoRepository, times(1)).findById(1);
        verify(medicamentoRepository, times(1)).save(any(Medicamento.class));
    }

    @Test
    void actualizarMedicamento_cuandoNoExiste_noDebeGuardarYLanzaExcepcion() {
        // Given
        MedicamentoRequestDTO dto = crearRequestDTO(
                "Medicamento",
                "Descripción",
                "Laboratorio",
                20
        );

        when(medicamentoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicamentoService.actualizarMedicamento(99, dto)
        );

        assertEquals("Medicamento no encontrado con id: 99", exception.getMessage());

        verify(medicamentoRepository, times(1)).findById(99);
        verify(medicamentoRepository, never()).save(any(Medicamento.class));
    }

    @Test
    void eliminarMedicamento_cuandoExiste_deberiaEliminarMedicamento() {
        // Given
        Medicamento medicamento = crearMedicamento(
                1,
                "Medicamento eliminar",
                "Descripción",
                "Laboratorio",
                15
        );

        when(medicamentoRepository.findById(1)).thenReturn(Optional.of(medicamento));

        // When
        medicamentoService.eliminarMedicamento(1);

        // Then
        verify(medicamentoRepository, times(1)).findById(1);
        verify(medicamentoRepository, times(1)).delete(medicamento);
    }

    @Test
    void eliminarMedicamento_cuandoNoExiste_noDebeEliminarYLanzaExcepcion() {
        // Given
        when(medicamentoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicamentoService.eliminarMedicamento(99)
        );

        assertEquals("Medicamento no encontrado con id: 99", exception.getMessage());

        verify(medicamentoRepository, times(1)).findById(99);
        verify(medicamentoRepository, never()).delete(any(Medicamento.class));
    }

    private Medicamento crearMedicamento(
            Integer id,
            String nombreMedicamento,
            String descripcion,
            String laboratorio,
            Integer stock
    ) {
        Medicamento medicamento = new Medicamento();
        medicamento.setIdMedicamento(id);
        medicamento.setNombreMedicamento(nombreMedicamento);
        medicamento.setDescripcion(descripcion);
        medicamento.setLaboratorio(laboratorio);
        medicamento.setStock(stock);
        return medicamento;
    }

    private MedicamentoRequestDTO crearRequestDTO(
            String nombreMedicamento,
            String descripcion,
            String laboratorio,
            Integer stock
    ) {
        MedicamentoRequestDTO dto = new MedicamentoRequestDTO();
        dto.setNombreMedicamento(nombreMedicamento);
        dto.setDescripcion(descripcion);
        dto.setLaboratorio(laboratorio);
        dto.setStock(stock);
        return dto;
    }
}