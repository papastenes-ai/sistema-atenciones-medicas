package com.duoc.atencionesmedicas.paciente.service;

import com.duoc.atencionesmedicas.paciente.dto.PacienteRequestDTO;
import com.duoc.atencionesmedicas.paciente.dto.PacienteResponseDTO;
import com.duoc.atencionesmedicas.paciente.model.Paciente;
import com.duoc.atencionesmedicas.paciente.repository.PacienteRepository;
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
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    void listarPacientes_deberiaRetornarListaDePacientes() {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(1);
        paciente.setNombre("Carlos");
        paciente.setApellido("Muñoz");
        paciente.setRut("12345678-9");
        paciente.setEdad(34);
        paciente.setCorreo("carlos@test.cl");
        paciente.setTelefono("912345678");

        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));

        List<PacienteResponseDTO> resultado = pacienteService.listarPacientes();

        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNombre());
        assertEquals("12345678-9", resultado.get(0).getRut());

        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarPaciente() {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(1);
        paciente.setNombre("Fernanda");
        paciente.setApellido("Rojas");
        paciente.setRut("98765432-1");
        paciente.setEdad(27);
        paciente.setCorreo("fernanda@test.cl");
        paciente.setTelefono("987654321");

        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        PacienteResponseDTO resultado = pacienteService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Fernanda", resultado.getNombre());
        assertEquals("98765432-1", resultado.getRut());

        verify(pacienteRepository, times(1)).findById(1);
    }

    @Test
    void guardarPaciente_deberiaGuardarYRetornarPaciente() {
        PacienteRequestDTO dto = new PacienteRequestDTO();
        dto.setNombre("Pablo");
        dto.setApellido("González");
        dto.setRut("11111111-1");
        dto.setEdad(30);
        dto.setCorreo("pablo@test.cl");
        dto.setTelefono("912345678");

        Paciente pacienteGuardado = new Paciente();
        pacienteGuardado.setIdPaciente(1);
        pacienteGuardado.setNombre(dto.getNombre());
        pacienteGuardado.setApellido(dto.getApellido());
        pacienteGuardado.setRut(dto.getRut());
        pacienteGuardado.setEdad(dto.getEdad());
        pacienteGuardado.setCorreo(dto.getCorreo());
        pacienteGuardado.setTelefono(dto.getTelefono());

        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteGuardado);

        PacienteResponseDTO resultado = pacienteService.guardarPaciente(dto);

        assertNotNull(resultado);
        assertEquals("Pablo", resultado.getNombre());
        assertEquals("11111111-1", resultado.getRut());

        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }
}