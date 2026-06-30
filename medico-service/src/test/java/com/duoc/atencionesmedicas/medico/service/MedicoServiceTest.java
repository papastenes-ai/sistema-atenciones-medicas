package com.duoc.atencionesmedicas.medico.service;

import com.duoc.atencionesmedicas.medico.dto.MedicoRequestDTO;
import com.duoc.atencionesmedicas.medico.dto.MedicoResponseDTO;
import com.duoc.atencionesmedicas.medico.model.Especialidad;
import com.duoc.atencionesmedicas.medico.model.Medico;
import com.duoc.atencionesmedicas.medico.repository.EspecialidadRepository;
import com.duoc.atencionesmedicas.medico.repository.MedicoRepository;
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
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private MedicoService medicoService;

    @Test
    void listarMedicos_deberiaRetornarListaDeMedicos() {
        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1);
        especialidad.setNombreEspecialidad("Cardiología");

        Medico medico = new Medico();
        medico.setIdMedico(1);
        medico.setNombre("Juan");
        medico.setApellido("Pérez");
        medico.setRut("12345678-9");
        medico.setCorreo("juan@test.cl");
        medico.setTelefono("912345678");
        medico.setEspecialidad(especialidad);

        when(medicoRepository.findAll()).thenReturn(List.of(medico));

        List<MedicoResponseDTO> resultado = medicoService.listarMedicos();

        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        assertEquals(1, resultado.get(0).getEspecialidadId());
        assertEquals("Cardiología", resultado.get(0).getEspecialidadNombre());

        verify(medicoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarMedico() {
        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1);
        especialidad.setNombreEspecialidad("Pediatría");

        Medico medico = new Medico();
        medico.setIdMedico(1);
        medico.setNombre("Carla");
        medico.setApellido("Gómez");
        medico.setRut("98765432-1");
        medico.setCorreo("carla@test.cl");
        medico.setTelefono("987654321");
        medico.setEspecialidad(especialidad);

        when(medicoRepository.findById(1)).thenReturn(Optional.of(medico));

        MedicoResponseDTO resultado = medicoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Carla", resultado.getNombre());
        assertEquals(1, resultado.getEspecialidadId());
        assertEquals("Pediatría", resultado.getEspecialidadNombre());

        verify(medicoRepository, times(1)).findById(1);
    }

    @Test
    void guardarMedico_deberiaGuardarYRetornarMedico() {
        MedicoRequestDTO dto = new MedicoRequestDTO();
        dto.setNombre("Pedro");
        dto.setApellido("Ramírez");
        dto.setRut("11111111-1");
        dto.setCorreo("pedro@test.cl");
        dto.setTelefono("911111111");
        dto.setEspecialidadId(1);

        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1);
        especialidad.setNombreEspecialidad("Medicina General");

        Medico medicoGuardado = new Medico();
        medicoGuardado.setIdMedico(1);
        medicoGuardado.setNombre(dto.getNombre());
        medicoGuardado.setApellido(dto.getApellido());
        medicoGuardado.setRut(dto.getRut());
        medicoGuardado.setCorreo(dto.getCorreo());
        medicoGuardado.setTelefono(dto.getTelefono());
        medicoGuardado.setEspecialidad(especialidad);

        when(especialidadRepository.findById(1)).thenReturn(Optional.of(especialidad));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoGuardado);

        MedicoResponseDTO resultado = medicoService.guardarMedico(dto);

        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getNombre());
        assertEquals(1, resultado.getEspecialidadId());
        assertEquals("Medicina General", resultado.getEspecialidadNombre());

        verify(especialidadRepository, times(1)).findById(1);
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }

    @Test
    void buscarPorEspecialidad_deberiaRetornarMedicosFiltrados() {
        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1);
        especialidad.setNombreEspecialidad("Cardiología");

        Medico medico = new Medico();
        medico.setIdMedico(1);
        medico.setNombre("Ana");
        medico.setApellido("Torres");
        medico.setRut("22222222-2");
        medico.setCorreo("ana@test.cl");
        medico.setTelefono("922222222");
        medico.setEspecialidad(especialidad);

        when(medicoRepository.findByEspecialidadNombreEspecialidadContainingIgnoreCase("Cardiología"))
                .thenReturn(List.of(medico));

        List<MedicoResponseDTO> resultado = medicoService.buscarPorEspecialidad("Cardiología");

        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombre());
        assertEquals(1, resultado.get(0).getEspecialidadId());
        assertEquals("Cardiología", resultado.get(0).getEspecialidadNombre());

        verify(medicoRepository, times(1))
                .findByEspecialidadNombreEspecialidadContainingIgnoreCase("Cardiología");
    }
}