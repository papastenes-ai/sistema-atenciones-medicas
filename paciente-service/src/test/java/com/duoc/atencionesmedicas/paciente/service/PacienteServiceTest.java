package com.duoc.atencionesmedicas.paciente.service;

import com.duoc.atencionesmedicas.paciente.dto.PacienteRequestDTO;
import com.duoc.atencionesmedicas.paciente.dto.PacienteResponseDTO;
import com.duoc.atencionesmedicas.paciente.exception.RecursoNoEncontradoException;
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
        // Given
        Paciente paciente = crearPaciente(
                1,
                "Carlos",
                "Muñoz",
                "12345678-9",
                34,
                "carlos@test.cl",
                "912345678"
        );

        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));

        // When
        List<PacienteResponseDTO> resultado = pacienteService.listarPacientes();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdPaciente());
        assertEquals("Carlos", resultado.get(0).getNombre());
        assertEquals("Muñoz", resultado.get(0).getApellido());
        assertEquals("12345678-9", resultado.get(0).getRut());
        assertEquals(34, resultado.get(0).getEdad());
        assertEquals("carlos@test.cl", resultado.get(0).getCorreo());
        assertEquals("912345678", resultado.get(0).getTelefono());

        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void listarPacientes_cuandoNoHayPacientes_deberiaRetornarListaVacia() {
        // Given
        when(pacienteRepository.findAll()).thenReturn(List.of());

        // When
        List<PacienteResponseDTO> resultado = pacienteService.listarPacientes();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarPaciente() {
        // Given
        Paciente paciente = crearPaciente(
                1,
                "Fernanda",
                "Rojas",
                "98765432-1",
                27,
                "fernanda@test.cl",
                "987654321"
        );

        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        // When
        PacienteResponseDTO resultado = pacienteService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdPaciente());
        assertEquals("Fernanda", resultado.getNombre());
        assertEquals("Rojas", resultado.getApellido());
        assertEquals("98765432-1", resultado.getRut());
        assertEquals(27, resultado.getEdad());
        assertEquals("fernanda@test.cl", resultado.getCorreo());
        assertEquals("987654321", resultado.getTelefono());

        verify(pacienteRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(pacienteRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> pacienteService.buscarPorId(99)
        );

        assertEquals("Paciente no encontrado con id: 99", exception.getMessage());

        verify(pacienteRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorRut_cuandoExiste_deberiaRetornarPaciente() {
        // Given
        Paciente paciente = crearPaciente(
                2,
                "María",
                "López",
                "11111111-1",
                40,
                "maria@test.cl",
                "911111111"
        );

        when(pacienteRepository.findByRut("11111111-1")).thenReturn(Optional.of(paciente));

        // When
        PacienteResponseDTO resultado = pacienteService.buscarPorRut("11111111-1");

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getIdPaciente());
        assertEquals("María", resultado.getNombre());
        assertEquals("López", resultado.getApellido());
        assertEquals("11111111-1", resultado.getRut());
        assertEquals(40, resultado.getEdad());
        assertEquals("maria@test.cl", resultado.getCorreo());
        assertEquals("911111111", resultado.getTelefono());

        verify(pacienteRepository, times(1)).findByRut("11111111-1");
    }

    @Test
    void buscarPorRut_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(pacienteRepository.findByRut("99999999-9")).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> pacienteService.buscarPorRut("99999999-9")
        );

        assertEquals("Paciente no encontrado con rut: 99999999-9", exception.getMessage());

        verify(pacienteRepository, times(1)).findByRut("99999999-9");
    }

    @Test
    void guardarPaciente_deberiaGuardarYRetornarPaciente() {
        // Given
        PacienteRequestDTO dto = crearRequestDTO(
                "Pablo",
                "González",
                "22222222-2",
                30,
                "pablo@test.cl",
                "922222222"
        );

        Paciente pacienteGuardado = crearPaciente(
                1,
                dto.getNombre(),
                dto.getApellido(),
                dto.getRut(),
                dto.getEdad(),
                dto.getCorreo(),
                dto.getTelefono()
        );

        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteGuardado);

        // When
        PacienteResponseDTO resultado = pacienteService.guardarPaciente(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdPaciente());
        assertEquals("Pablo", resultado.getNombre());
        assertEquals("González", resultado.getApellido());
        assertEquals("22222222-2", resultado.getRut());
        assertEquals(30, resultado.getEdad());
        assertEquals("pablo@test.cl", resultado.getCorreo());
        assertEquals("922222222", resultado.getTelefono());

        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void actualizarPaciente_cuandoExiste_deberiaActualizarYRetornarPaciente() {
        // Given
        Paciente pacienteExistente = crearPaciente(
                1,
                "Nombre antiguo",
                "Apellido antiguo",
                "12345678-9",
                20,
                "antiguo@test.cl",
                "900000000"
        );

        PacienteRequestDTO dto = crearRequestDTO(
                "Nombre actualizado",
                "Apellido actualizado",
                "12345678-9",
                25,
                "actualizado@test.cl",
                "955555555"
        );

        Paciente pacienteActualizado = crearPaciente(
                1,
                dto.getNombre(),
                dto.getApellido(),
                dto.getRut(),
                dto.getEdad(),
                dto.getCorreo(),
                dto.getTelefono()
        );

        when(pacienteRepository.findById(1)).thenReturn(Optional.of(pacienteExistente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteActualizado);

        // When
        PacienteResponseDTO resultado = pacienteService.actualizarPaciente(1, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdPaciente());
        assertEquals("Nombre actualizado", resultado.getNombre());
        assertEquals("Apellido actualizado", resultado.getApellido());
        assertEquals("12345678-9", resultado.getRut());
        assertEquals(25, resultado.getEdad());
        assertEquals("actualizado@test.cl", resultado.getCorreo());
        assertEquals("955555555", resultado.getTelefono());

        verify(pacienteRepository, times(1)).findById(1);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    void actualizarPaciente_cuandoNoExiste_noDebeGuardarYLanzaExcepcion() {
        // Given
        PacienteRequestDTO dto = crearRequestDTO(
                "Nombre",
                "Apellido",
                "12345678-9",
                25,
                "correo@test.cl",
                "955555555"
        );

        when(pacienteRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> pacienteService.actualizarPaciente(99, dto)
        );

        assertEquals("Paciente no encontrado con id: 99", exception.getMessage());

        verify(pacienteRepository, times(1)).findById(99);
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void eliminarPaciente_cuandoExiste_deberiaEliminarPaciente() {
        // Given
        Paciente paciente = crearPaciente(
                1,
                "Paciente",
                "Eliminar",
                "33333333-3",
                50,
                "eliminar@test.cl",
                "933333333"
        );

        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        // When
        pacienteService.eliminarPaciente(1);

        // Then
        verify(pacienteRepository, times(1)).findById(1);
        verify(pacienteRepository, times(1)).delete(paciente);
    }

    @Test
    void eliminarPaciente_cuandoNoExiste_noDebeEliminarYLanzaExcepcion() {
        // Given
        when(pacienteRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> pacienteService.eliminarPaciente(99)
        );

        assertEquals("Paciente no encontrado con id: 99", exception.getMessage());

        verify(pacienteRepository, times(1)).findById(99);
        verify(pacienteRepository, never()).delete(any(Paciente.class));
    }

    private Paciente crearPaciente(
            Integer id,
            String nombre,
            String apellido,
            String rut,
            Integer edad,
            String correo,
            String telefono
    ) {
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(id);
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setRut(rut);
        paciente.setEdad(edad);
        paciente.setCorreo(correo);
        paciente.setTelefono(telefono);
        return paciente;
    }

    private PacienteRequestDTO crearRequestDTO(
            String nombre,
            String apellido,
            String rut,
            Integer edad,
            String correo,
            String telefono
    ) {
        PacienteRequestDTO dto = new PacienteRequestDTO();
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setRut(rut);
        dto.setEdad(edad);
        dto.setCorreo(correo);
        dto.setTelefono(telefono);
        return dto;
    }
}