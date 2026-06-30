package com.duoc.atencionesmedicas.centromedico.service;

import com.duoc.atencionesmedicas.centromedico.dto.CentroMedicoRequestDTO;
import com.duoc.atencionesmedicas.centromedico.dto.CentroMedicoResponseDTO;
import com.duoc.atencionesmedicas.centromedico.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.centromedico.model.CentroMedico;
import com.duoc.atencionesmedicas.centromedico.repository.CentroMedicoRepository;
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
class CentroMedicoServiceTest {

    @Mock
    private CentroMedicoRepository centroMedicoRepository;

    @InjectMocks
    private CentroMedicoService centroMedicoService;

    @Test
    void listarCentros_deberiaRetornarListaDeCentros() {
        // Given
        CentroMedico centro = crearCentroMedico(
                1,
                "Centro Médico Central",
                "Av. Principal 123",
                "Santiago",
                "222222222",
                "08:00 - 18:00",
                "ACTIVO"
        );

        when(centroMedicoRepository.findAll()).thenReturn(List.of(centro));

        // When
        List<CentroMedicoResponseDTO> resultado = centroMedicoService.listarCentros();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdCentro());
        assertEquals("Centro Médico Central", resultado.get(0).getNombre());
        assertEquals("Av. Principal 123", resultado.get(0).getDireccion());
        assertEquals("Santiago", resultado.get(0).getComuna());
        assertEquals("222222222", resultado.get(0).getTelefono());
        assertEquals("08:00 - 18:00", resultado.get(0).getHorario());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(centroMedicoRepository, times(1)).findAll();
    }

    @Test
    void listarCentros_cuandoNoHayCentros_deberiaRetornarListaVacia() {
        // Given
        when(centroMedicoRepository.findAll()).thenReturn(List.of());

        // When
        List<CentroMedicoResponseDTO> resultado = centroMedicoService.listarCentros();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(centroMedicoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarCentro() {
        // Given
        CentroMedico centro = crearCentroMedico(
                1,
                "Clínica Norte",
                "Calle Norte 456",
                "Recoleta",
                "233333333",
                "09:00 - 19:00",
                "ACTIVO"
        );

        when(centroMedicoRepository.findById(1)).thenReturn(Optional.of(centro));

        // When
        CentroMedicoResponseDTO resultado = centroMedicoService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCentro());
        assertEquals("Clínica Norte", resultado.getNombre());
        assertEquals("Calle Norte 456", resultado.getDireccion());
        assertEquals("Recoleta", resultado.getComuna());
        assertEquals("233333333", resultado.getTelefono());
        assertEquals("09:00 - 19:00", resultado.getHorario());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(centroMedicoRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(centroMedicoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> centroMedicoService.buscarPorId(99)
        );

        assertEquals("Centro médico no encontrado con id: 99", exception.getMessage());

        verify(centroMedicoRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorComuna_cuandoExistenCentros_deberiaRetornarLista() {
        // Given
        CentroMedico centro = crearCentroMedico(
                1,
                "Centro Providencia",
                "Av. Providencia 1000",
                "Providencia",
                "244444444",
                "08:30 - 18:30",
                "ACTIVO"
        );

        when(centroMedicoRepository.findByComuna("Providencia")).thenReturn(List.of(centro));

        // When
        List<CentroMedicoResponseDTO> resultado = centroMedicoService.buscarPorComuna("Providencia");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Centro Providencia", resultado.get(0).getNombre());
        assertEquals("Providencia", resultado.get(0).getComuna());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(centroMedicoRepository, times(1)).findByComuna("Providencia");
    }

    @Test
    void buscarPorComuna_cuandoNoExistenCentros_deberiaLanzarExcepcion() {
        // Given
        when(centroMedicoRepository.findByComuna("SinComuna")).thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> centroMedicoService.buscarPorComuna("SinComuna")
        );

        assertEquals("No existen centros médicos en la comuna: SinComuna", exception.getMessage());

        verify(centroMedicoRepository, times(1)).findByComuna("SinComuna");
    }

    @Test
    void buscarPorEstado_cuandoExistenCentros_deberiaRetornarLista() {
        // Given
        CentroMedico centro = crearCentroMedico(
                1,
                "Centro Activo",
                "Av. Salud 111",
                "Maipú",
                "255555555",
                "08:00 - 17:00",
                "ACTIVO"
        );

        when(centroMedicoRepository.findByEstado("ACTIVO")).thenReturn(List.of(centro));

        // When
        List<CentroMedicoResponseDTO> resultado = centroMedicoService.buscarPorEstado("ACTIVO");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Centro Activo", resultado.get(0).getNombre());
        assertEquals("Maipú", resultado.get(0).getComuna());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(centroMedicoRepository, times(1)).findByEstado("ACTIVO");
    }

    @Test
    void buscarPorEstado_cuandoNoExistenCentros_deberiaLanzarExcepcion() {
        // Given
        when(centroMedicoRepository.findByEstado("INACTIVO")).thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> centroMedicoService.buscarPorEstado("INACTIVO")
        );

        assertEquals("No existen centros médicos con estado: INACTIVO", exception.getMessage());

        verify(centroMedicoRepository, times(1)).findByEstado("INACTIVO");
    }

    @Test
    void buscarPorNombre_cuandoExistenCentros_deberiaRetornarLista() {
        // Given
        CentroMedico centro = crearCentroMedico(
                1,
                "Centro Médico Vida",
                "Av. Vida 555",
                "Ñuñoa",
                "266666666",
                "08:00 - 20:00",
                "ACTIVO"
        );

        when(centroMedicoRepository.findByNombreContainingIgnoreCase("vida"))
                .thenReturn(List.of(centro));

        // When
        List<CentroMedicoResponseDTO> resultado = centroMedicoService.buscarPorNombre("vida");

        // Then
        assertEquals(1, resultado.size());
        assertEquals("Centro Médico Vida", resultado.get(0).getNombre());
        assertEquals("Ñuñoa", resultado.get(0).getComuna());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(centroMedicoRepository, times(1))
                .findByNombreContainingIgnoreCase("vida");
    }

    @Test
    void buscarPorNombre_cuandoNoExistenCentros_deberiaLanzarExcepcion() {
        // Given
        when(centroMedicoRepository.findByNombreContainingIgnoreCase("inexistente"))
                .thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> centroMedicoService.buscarPorNombre("inexistente")
        );

        assertEquals("No existen centros médicos con nombre: inexistente", exception.getMessage());

        verify(centroMedicoRepository, times(1))
                .findByNombreContainingIgnoreCase("inexistente");
    }

    @Test
    void guardarCentro_deberiaGuardarYRetornarCentro() {
        // Given
        CentroMedicoRequestDTO dto = crearRequestDTO(
                "Centro Nuevo",
                "Nueva dirección 123",
                "La Florida",
                "277777777",
                "09:00 - 18:00",
                "ACTIVO"
        );

        CentroMedico centroGuardado = crearCentroMedico(
                1,
                dto.getNombre(),
                dto.getDireccion(),
                dto.getComuna(),
                dto.getTelefono(),
                dto.getHorario(),
                dto.getEstado()
        );

        when(centroMedicoRepository.save(any(CentroMedico.class))).thenReturn(centroGuardado);

        // When
        CentroMedicoResponseDTO resultado = centroMedicoService.guardarCentro(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCentro());
        assertEquals("Centro Nuevo", resultado.getNombre());
        assertEquals("Nueva dirección 123", resultado.getDireccion());
        assertEquals("La Florida", resultado.getComuna());
        assertEquals("277777777", resultado.getTelefono());
        assertEquals("09:00 - 18:00", resultado.getHorario());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(centroMedicoRepository, times(1)).save(any(CentroMedico.class));
    }

    @Test
    void actualizarCentro_cuandoExiste_deberiaActualizarYRetornarCentro() {
        // Given
        CentroMedico centroExistente = crearCentroMedico(
                1,
                "Centro Antiguo",
                "Dirección antigua",
                "Santiago",
                "200000000",
                "08:00 - 17:00",
                "ACTIVO"
        );

        CentroMedicoRequestDTO dto = crearRequestDTO(
                "Centro Actualizado",
                "Dirección actualizada",
                "Las Condes",
                "288888888",
                "08:00 - 19:00",
                "INACTIVO"
        );

        CentroMedico centroActualizado = crearCentroMedico(
                1,
                dto.getNombre(),
                dto.getDireccion(),
                dto.getComuna(),
                dto.getTelefono(),
                dto.getHorario(),
                dto.getEstado()
        );

        when(centroMedicoRepository.findById(1)).thenReturn(Optional.of(centroExistente));
        when(centroMedicoRepository.save(any(CentroMedico.class))).thenReturn(centroActualizado);

        // When
        CentroMedicoResponseDTO resultado = centroMedicoService.actualizarCentro(1, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCentro());
        assertEquals("Centro Actualizado", resultado.getNombre());
        assertEquals("Dirección actualizada", resultado.getDireccion());
        assertEquals("Las Condes", resultado.getComuna());
        assertEquals("288888888", resultado.getTelefono());
        assertEquals("08:00 - 19:00", resultado.getHorario());
        assertEquals("INACTIVO", resultado.getEstado());

        verify(centroMedicoRepository, times(1)).findById(1);
        verify(centroMedicoRepository, times(1)).save(any(CentroMedico.class));
    }

    @Test
    void actualizarCentro_cuandoNoExiste_noDebeGuardarYLanzaExcepcion() {
        // Given
        CentroMedicoRequestDTO dto = crearRequestDTO(
                "Centro",
                "Dirección",
                "Comuna",
                "299999999",
                "09:00 - 18:00",
                "ACTIVO"
        );

        when(centroMedicoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> centroMedicoService.actualizarCentro(99, dto)
        );

        assertEquals("Centro médico no encontrado con id: 99", exception.getMessage());

        verify(centroMedicoRepository, times(1)).findById(99);
        verify(centroMedicoRepository, never()).save(any(CentroMedico.class));
    }

    @Test
    void eliminarCentro_cuandoExiste_deberiaEliminarCentro() {
        // Given
        CentroMedico centro = crearCentroMedico(
                1,
                "Centro Eliminar",
                "Dirección",
                "Santiago",
                "211111111",
                "09:00 - 18:00",
                "INACTIVO"
        );

        when(centroMedicoRepository.findById(1)).thenReturn(Optional.of(centro));

        // When
        centroMedicoService.eliminarCentro(1);

        // Then
        verify(centroMedicoRepository, times(1)).findById(1);
        verify(centroMedicoRepository, times(1)).delete(centro);
    }

    @Test
    void eliminarCentro_cuandoNoExiste_noDebeEliminarYLanzaExcepcion() {
        // Given
        when(centroMedicoRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> centroMedicoService.eliminarCentro(99)
        );

        assertEquals("Centro médico no encontrado con id: 99", exception.getMessage());

        verify(centroMedicoRepository, times(1)).findById(99);
        verify(centroMedicoRepository, never()).delete(any(CentroMedico.class));
    }

    private CentroMedico crearCentroMedico(
            Integer id,
            String nombre,
            String direccion,
            String comuna,
            String telefono,
            String horario,
            String estado
    ) {
        CentroMedico centro = new CentroMedico();
        centro.setIdCentro(id);
        centro.setNombre(nombre);
        centro.setDireccion(direccion);
        centro.setComuna(comuna);
        centro.setTelefono(telefono);
        centro.setHorario(horario);
        centro.setEstado(estado);
        return centro;
    }

    private CentroMedicoRequestDTO crearRequestDTO(
            String nombre,
            String direccion,
            String comuna,
            String telefono,
            String horario,
            String estado
    ) {
        CentroMedicoRequestDTO dto = new CentroMedicoRequestDTO();
        dto.setNombre(nombre);
        dto.setDireccion(direccion);
        dto.setComuna(comuna);
        dto.setTelefono(telefono);
        dto.setHorario(horario);
        dto.setEstado(estado);
        return dto;
    }
}