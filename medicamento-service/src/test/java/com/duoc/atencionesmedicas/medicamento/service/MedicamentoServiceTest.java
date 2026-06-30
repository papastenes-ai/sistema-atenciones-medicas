package com.duoc.atencionesmedicas.medicamento.service;

import com.duoc.atencionesmedicas.medicamento.dto.MedicamentoRequestDTO;
import com.duoc.atencionesmedicas.medicamento.dto.MedicamentoResponseDTO;
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
        Medicamento medicamento = new Medicamento();
        medicamento.setIdMedicamento(1);
        medicamento.setNombreMedicamento("Paracetamol");
        medicamento.setDescripcion("Analgésico");
        medicamento.setLaboratorio("Laboratorio Chile");
        medicamento.setStock(50);

        when(medicamentoRepository.findAll()).thenReturn(List.of(medicamento));

        List<MedicamentoResponseDTO> resultado = medicamentoService.listarMedicamentos();

        assertEquals(1, resultado.size());
        assertEquals("Paracetamol", resultado.get(0).getNombreMedicamento());
        assertEquals("Laboratorio Chile", resultado.get(0).getLaboratorio());
        assertEquals(50, resultado.get(0).getStock());

        verify(medicamentoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarMedicamento() {
        Medicamento medicamento = new Medicamento();
        medicamento.setIdMedicamento(1);
        medicamento.setNombreMedicamento("Ibuprofeno");
        medicamento.setDescripcion("Antiinflamatorio");
        medicamento.setLaboratorio("Lab Test");
        medicamento.setStock(30);

        when(medicamentoRepository.findById(1)).thenReturn(Optional.of(medicamento));

        MedicamentoResponseDTO resultado = medicamentoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Ibuprofeno", resultado.getNombreMedicamento());
        assertEquals("Lab Test", resultado.getLaboratorio());

        verify(medicamentoRepository, times(1)).findById(1);
    }

    @Test
    void guardarMedicamento_deberiaGuardarYRetornarMedicamento() {
        MedicamentoRequestDTO dto = new MedicamentoRequestDTO();
        dto.setNombreMedicamento("Amoxicilina");
        dto.setDescripcion("Antibiótico");
        dto.setLaboratorio("Laboratorio Test");
        dto.setStock(100);

        Medicamento medicamentoGuardado = new Medicamento();
        medicamentoGuardado.setIdMedicamento(1);
        medicamentoGuardado.setNombreMedicamento(dto.getNombreMedicamento());
        medicamentoGuardado.setDescripcion(dto.getDescripcion());
        medicamentoGuardado.setLaboratorio(dto.getLaboratorio());
        medicamentoGuardado.setStock(dto.getStock());

        when(medicamentoRepository.save(any(Medicamento.class))).thenReturn(medicamentoGuardado);

        MedicamentoResponseDTO resultado = medicamentoService.guardarMedicamento(dto);

        assertNotNull(resultado);
        assertEquals("Amoxicilina", resultado.getNombreMedicamento());
        assertEquals(100, resultado.getStock());

        verify(medicamentoRepository, times(1)).save(any(Medicamento.class));
    }

    @Test
    void buscarPorNombreMedicamento_deberiaRetornarMedicamentosFiltrados() {
        Medicamento medicamento = new Medicamento();
        medicamento.setIdMedicamento(1);
        medicamento.setNombreMedicamento("Paracetamol");
        medicamento.setDescripcion("Analgésico");
        medicamento.setLaboratorio("Laboratorio Chile");
        medicamento.setStock(50);

        when(medicamentoRepository.findByNombreMedicamentoContainingIgnoreCase("Paracetamol"))
                .thenReturn(List.of(medicamento));

        List<MedicamentoResponseDTO> resultado = medicamentoService.buscarPorNombreMedicamento("Paracetamol");

        assertEquals(1, resultado.size());
        assertEquals("Paracetamol", resultado.get(0).getNombreMedicamento());

        verify(medicamentoRepository, times(1))
                .findByNombreMedicamentoContainingIgnoreCase("Paracetamol");
    }
}