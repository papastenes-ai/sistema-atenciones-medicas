package com.duoc.atencionesmedicas.medico.service;

import com.duoc.atencionesmedicas.medico.dto.MedicoRequestDTO;
import com.duoc.atencionesmedicas.medico.dto.MedicoResponseDTO;
import com.duoc.atencionesmedicas.medico.exception.RecursoNoEncontradoException;
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
        // Given
        Especialidad especialidad = crearEspecialidad(1, "Medicina General", "Atención primaria");

        Medico medico = crearMedico(
                1,
                "11111111-1",
                "Ana",
                "González",
                "ana@test.cl",
                "911111111",
                especialidad
        );

        when(medicoRepository.findAll()).thenReturn(List.of(medico));

        // When
        List<MedicoResponseDTO> resultado = medicoService.listarMedicos();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdMedico());
        assertEquals("11111111-1", resultado.get(0).getRut());
        assertEquals("Ana", resultado.get(0).getNombre());
        assertEquals("González", resultado.get(0).getApellido());
        assertEquals("ana@test.cl", resultado.get(0).getCorreo());
        assertEquals("911111111", resultado.get(0).getTelefono());
        assertEquals(1, resultado.get(0).getEspecialidadId());
        assertEquals("Medicina General", resultado.get(0).getEspecialidadNombre());

        verify(medicoRepository, times(1)).findAll();
    }

    @Test
    void listarMedicos_cuandoNoHayMedicos_deberiaRetornarListaVacia() {
        // Given
        when(medicoRepository.findAll()).thenReturn(List.of());

        // When
        List<MedicoResponseDTO> resultado = medicoService.listarMedicos();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(medicoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarMedico() {
        // Given
        Especialidad especialidad = crearEspecialidad(2, "Cardiología", "Especialidad del corazón");

        Medico medico = crearMedico(
                1,
                "22222222-2",
                "Pedro",
                "Ramírez",
                "pedro@test.cl",
                "922222222",
                especialidad
        );

        when(medicoRepository.findById(1)).thenReturn(Optional.of(medico));

        // When
        MedicoResponseDTO resultado = medicoService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdMedico());
        assertEquals("22222222-2", resultado.getRut());
        assertEquals("Pedro", resultado.getNombre());
        assertEquals("Ramírez", resultado.getApellido());
        assertEquals("pedro@test.cl", resultado.getCorreo());
        assertEquals("922222222", resultado.getTelefono());
        assertEquals(2, resultado.getEspecialidadId());
        assertEquals("Cardiología", resultado.getEspecialidadNombre());

        verify(medicoRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(medicoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicoService.buscarPorId(99)
        );

        assertEquals("Médico no encontrado con id: 99", exception.getMessage());

        verify(medicoRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorEspecialidad_cuandoExistenMedicos_deberiaRetornarListaFiltrada() {
        // Given
        Especialidad especialidad = crearEspecialidad(3, "Pediatría", "Atención infantil");

        Medico medico = crearMedico(
                1,
                "33333333-3",
                "María",
                "López",
                "maria@test.cl",
                "933333333",
                especialidad
        );

        when(medicoRepository.findByEspecialidadNombreEspecialidadContainingIgnoreCase("pedia"))
                .thenReturn(List.of(medico));

        // When
        List<MedicoResponseDTO> resultado = medicoService.buscarPorEspecialidad("pedia");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("María", resultado.get(0).getNombre());
        assertEquals("López", resultado.get(0).getApellido());
        assertEquals(3, resultado.get(0).getEspecialidadId());
        assertEquals("Pediatría", resultado.get(0).getEspecialidadNombre());

        verify(medicoRepository, times(1))
                .findByEspecialidadNombreEspecialidadContainingIgnoreCase("pedia");
    }

    @Test
    void buscarPorEspecialidad_cuandoNoExistenMedicos_deberiaLanzarExcepcion() {
        // Given
        when(medicoRepository.findByEspecialidadNombreEspecialidadContainingIgnoreCase("trauma"))
                .thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicoService.buscarPorEspecialidad("trauma")
        );

        assertEquals("No existen médicos con especialidad: trauma", exception.getMessage());

        verify(medicoRepository, times(1))
                .findByEspecialidadNombreEspecialidadContainingIgnoreCase("trauma");
    }

    @Test
    void guardarMedico_cuandoEspecialidadExiste_deberiaGuardarYRetornarMedico() {
        // Given
        Especialidad especialidad = crearEspecialidad(1, "Medicina General", "Atención primaria");

        MedicoRequestDTO dto = crearRequestDTO(
                "44444444-4",
                "Camila",
                "Torres",
                "camila@test.cl",
                "944444444",
                1
        );

        Medico medicoGuardado = crearMedico(
                1,
                dto.getRut(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getTelefono(),
                especialidad
        );

        when(especialidadRepository.findById(1)).thenReturn(Optional.of(especialidad));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoGuardado);

        // When
        MedicoResponseDTO resultado = medicoService.guardarMedico(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdMedico());
        assertEquals("44444444-4", resultado.getRut());
        assertEquals("Camila", resultado.getNombre());
        assertEquals("Torres", resultado.getApellido());
        assertEquals("camila@test.cl", resultado.getCorreo());
        assertEquals("944444444", resultado.getTelefono());
        assertEquals(1, resultado.getEspecialidadId());
        assertEquals("Medicina General", resultado.getEspecialidadNombre());

        verify(especialidadRepository, times(1)).findById(1);
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }

    @Test
    void guardarMedico_cuandoEspecialidadNoExiste_noDebeGuardarYLanzaExcepcion() {
        // Given
        MedicoRequestDTO dto = crearRequestDTO(
                "55555555-5",
                "Diego",
                "Pérez",
                "diego@test.cl",
                "955555555",
                99
        );

        when(especialidadRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicoService.guardarMedico(dto)
        );

        assertEquals("Especialidad no encontrada con id: 99", exception.getMessage());

        verify(especialidadRepository, times(1)).findById(99);
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    void actualizarMedico_cuandoExisteYEspecialidadExiste_deberiaActualizarYRetornarMedico() {
        // Given
        Especialidad especialidadAntigua = crearEspecialidad(1, "Medicina General", "Atención primaria");
        Especialidad especialidadNueva = crearEspecialidad(2, "Neurología", "Sistema nervioso");

        Medico medicoExistente = crearMedico(
                1,
                "66666666-6",
                "Nombre antiguo",
                "Apellido antiguo",
                "antiguo@test.cl",
                "966666666",
                especialidadAntigua
        );

        MedicoRequestDTO dto = crearRequestDTO(
                "66666666-6",
                "Nombre actualizado",
                "Apellido actualizado",
                "actualizado@test.cl",
                "977777777",
                2
        );

        Medico medicoActualizado = crearMedico(
                1,
                dto.getRut(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getCorreo(),
                dto.getTelefono(),
                especialidadNueva
        );

        when(medicoRepository.findById(1)).thenReturn(Optional.of(medicoExistente));
        when(especialidadRepository.findById(2)).thenReturn(Optional.of(especialidadNueva));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoActualizado);

        // When
        MedicoResponseDTO resultado = medicoService.actualizarMedico(1, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdMedico());
        assertEquals("66666666-6", resultado.getRut());
        assertEquals("Nombre actualizado", resultado.getNombre());
        assertEquals("Apellido actualizado", resultado.getApellido());
        assertEquals("actualizado@test.cl", resultado.getCorreo());
        assertEquals("977777777", resultado.getTelefono());
        assertEquals(2, resultado.getEspecialidadId());
        assertEquals("Neurología", resultado.getEspecialidadNombre());

        verify(medicoRepository, times(1)).findById(1);
        verify(especialidadRepository, times(1)).findById(2);
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }

    @Test
    void actualizarMedico_cuandoMedicoNoExiste_noDebeBuscarEspecialidadNiGuardar() {
        // Given
        MedicoRequestDTO dto = crearRequestDTO(
                "77777777-7",
                "Nombre",
                "Apellido",
                "correo@test.cl",
                "977777777",
                1
        );

        when(medicoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicoService.actualizarMedico(99, dto)
        );

        assertEquals("Médico no encontrado con id: 99", exception.getMessage());

        verify(medicoRepository, times(1)).findById(99);
        verify(especialidadRepository, never()).findById(anyInt());
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    void actualizarMedico_cuandoEspecialidadNoExiste_noDebeGuardar() {
        // Given
        Especialidad especialidadAntigua = crearEspecialidad(1, "Medicina General", "Atención primaria");

        Medico medicoExistente = crearMedico(
                1,
                "88888888-8",
                "Rodrigo",
                "Silva",
                "rodrigo@test.cl",
                "988888888",
                especialidadAntigua
        );

        MedicoRequestDTO dto = crearRequestDTO(
                "88888888-8",
                "Rodrigo",
                "Silva",
                "rodrigo@test.cl",
                "988888888",
                99
        );

        when(medicoRepository.findById(1)).thenReturn(Optional.of(medicoExistente));
        when(especialidadRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicoService.actualizarMedico(1, dto)
        );

        assertEquals("Especialidad no encontrada con id: 99", exception.getMessage());

        verify(medicoRepository, times(1)).findById(1);
        verify(especialidadRepository, times(1)).findById(99);
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    void eliminarMedico_cuandoExiste_deberiaEliminarMedico() {
        // Given
        Especialidad especialidad = crearEspecialidad(1, "Medicina General", "Atención primaria");

        Medico medico = crearMedico(
                1,
                "99999999-9",
                "Médico",
                "Eliminar",
                "eliminar@test.cl",
                "999999999",
                especialidad
        );

        when(medicoRepository.findById(1)).thenReturn(Optional.of(medico));

        // When
        medicoService.eliminarMedico(1);

        // Then
        verify(medicoRepository, times(1)).findById(1);
        verify(medicoRepository, times(1)).delete(medico);
    }

    @Test
    void eliminarMedico_cuandoNoExiste_noDebeEliminarYLanzaExcepcion() {
        // Given
        when(medicoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> medicoService.eliminarMedico(99)
        );

        assertEquals("Médico no encontrado con id: 99", exception.getMessage());

        verify(medicoRepository, times(1)).findById(99);
        verify(medicoRepository, never()).delete(any(Medico.class));
    }

    private Especialidad crearEspecialidad(Integer id, String nombre, String descripcion) {
        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(id);
        especialidad.setNombreEspecialidad(nombre);
        especialidad.setDescripcion(descripcion);
        return especialidad;
    }

    private Medico crearMedico(
            Integer id,
            String rut,
            String nombre,
            String apellido,
            String correo,
            String telefono,
            Especialidad especialidad
    ) {
        Medico medico = new Medico();
        medico.setIdMedico(id);
        medico.setRut(rut);
        medico.setNombre(nombre);
        medico.setApellido(apellido);
        medico.setCorreo(correo);
        medico.setTelefono(telefono);
        medico.setEspecialidad(especialidad);
        return medico;
    }

    private MedicoRequestDTO crearRequestDTO(
            String rut,
            String nombre,
            String apellido,
            String correo,
            String telefono,
            Integer especialidadId
    ) {
        MedicoRequestDTO dto = new MedicoRequestDTO();
        dto.setRut(rut);
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setCorreo(correo);
        dto.setTelefono(telefono);
        dto.setEspecialidadId(especialidadId);
        return dto;
    }
}