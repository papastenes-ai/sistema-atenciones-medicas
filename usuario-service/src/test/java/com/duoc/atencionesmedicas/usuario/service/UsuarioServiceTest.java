package com.duoc.atencionesmedicas.usuario.service;

import com.duoc.atencionesmedicas.usuario.dto.UsuarioRequestDTO;
import com.duoc.atencionesmedicas.usuario.dto.UsuarioResponseDTO;
import com.duoc.atencionesmedicas.usuario.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.usuario.model.Usuario;
import com.duoc.atencionesmedicas.usuario.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void listarUsuarios_deberiaRetornarListaDeUsuarios() {
        // Given
        Usuario usuario = crearUsuario(
                1,
                "admin",
                "123456",
                "admin@test.cl",
                "ADMIN",
                "ACTIVO"
        );

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        // When
        List<UsuarioResponseDTO> resultado = usuarioService.listarUsuarios();

        // Then
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getIdUsuario());
        assertEquals("admin", resultado.get(0).getUsername());
        assertEquals("admin@test.cl", resultado.get(0).getCorreo());
        assertEquals("ADMIN", resultado.get(0).getRol());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void listarUsuarios_cuandoNoHayUsuarios_deberiaRetornarListaVacia() {
        // Given
        when(usuarioRepository.findAll()).thenReturn(List.of());

        // When
        List<UsuarioResponseDTO> resultado = usuarioService.listarUsuarios();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarUsuario() {
        // Given
        Usuario usuario = crearUsuario(
                1,
                "pablo",
                "clave123",
                "pablo@test.cl",
                "ADMIN",
                "ACTIVO"
        );

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        // When
        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        assertEquals("pablo", resultado.getUsername());
        assertEquals("pablo@test.cl", resultado.getCorreo());
        assertEquals("ADMIN", resultado.getRol());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.buscarPorId(99)
        );

        assertEquals("Usuario no encontrado con id: 99", exception.getMessage());

        verify(usuarioRepository, times(1)).findById(99);
    }

    @Test
    void buscarPorUsername_cuandoExiste_deberiaRetornarUsuario() {
        // Given
        Usuario usuario = crearUsuario(
                2,
                "doctor01",
                "claveDoctor",
                "doctor01@test.cl",
                "MEDICO",
                "ACTIVO"
        );

        when(usuarioRepository.findByUsername("doctor01")).thenReturn(Optional.of(usuario));

        // When
        UsuarioResponseDTO resultado = usuarioService.buscarPorUsername("doctor01");

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.getIdUsuario());
        assertEquals("doctor01", resultado.getUsername());
        assertEquals("doctor01@test.cl", resultado.getCorreo());
        assertEquals("MEDICO", resultado.getRol());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(usuarioRepository, times(1)).findByUsername("doctor01");
    }

    @Test
    void buscarPorUsername_cuandoNoExiste_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.buscarPorUsername("noexiste")
        );

        assertEquals("Usuario no encontrado: noexiste", exception.getMessage());

        verify(usuarioRepository, times(1)).findByUsername("noexiste");
    }

    @Test
    void buscarPorRol_cuandoExistenUsuarios_deberiaRetornarLista() {
        // Given
        Usuario usuario = crearUsuario(
                3,
                "recepcion01",
                "claveRecepcion",
                "recepcion@test.cl",
                "RECEPCION",
                "ACTIVO"
        );

        when(usuarioRepository.findByRol("RECEPCION")).thenReturn(List.of(usuario));

        // When
        List<UsuarioResponseDTO> resultado = usuarioService.buscarPorRol("RECEPCION");

        // Then
        assertEquals(1, resultado.size());
        assertEquals(3, resultado.get(0).getIdUsuario());
        assertEquals("recepcion01", resultado.get(0).getUsername());
        assertEquals("RECEPCION", resultado.get(0).getRol());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(usuarioRepository, times(1)).findByRol("RECEPCION");
    }

    @Test
    void buscarPorRol_cuandoNoExistenUsuarios_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(usuarioRepository.findByRol("SIN_ROL")).thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.buscarPorRol("SIN_ROL")
        );

        assertEquals("No existen usuarios con rol: SIN_ROL", exception.getMessage());

        verify(usuarioRepository, times(1)).findByRol("SIN_ROL");
    }

    @Test
    void buscarPorEstado_cuandoExistenUsuarios_deberiaRetornarLista() {
        // Given
        Usuario usuario = crearUsuario(
                4,
                "usuarioInactivo",
                "clave",
                "inactivo@test.cl",
                "PACIENTE",
                "INACTIVO"
        );

        when(usuarioRepository.findByEstado("INACTIVO")).thenReturn(List.of(usuario));

        // When
        List<UsuarioResponseDTO> resultado = usuarioService.buscarPorEstado("INACTIVO");

        // Then
        assertEquals(1, resultado.size());
        assertEquals(4, resultado.get(0).getIdUsuario());
        assertEquals("usuarioInactivo", resultado.get(0).getUsername());
        assertEquals("PACIENTE", resultado.get(0).getRol());
        assertEquals("INACTIVO", resultado.get(0).getEstado());

        verify(usuarioRepository, times(1)).findByEstado("INACTIVO");
    }

    @Test
    void buscarPorEstado_cuandoNoExistenUsuarios_deberiaLanzarRecursoNoEncontradoException() {
        // Given
        when(usuarioRepository.findByEstado("BLOQUEADO")).thenReturn(List.of());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.buscarPorEstado("BLOQUEADO")
        );

        assertEquals("No existen usuarios con estado: BLOQUEADO", exception.getMessage());

        verify(usuarioRepository, times(1)).findByEstado("BLOQUEADO");
    }

    @Test
    void guardarUsuario_deberiaGuardarYRetornarUsuarioSinExponerPassword() {
        // Given
        UsuarioRequestDTO dto = crearRequestDTO(
                "nuevoUsuario",
                "claveSegura",
                "nuevo@test.cl",
                "ADMIN",
                "ACTIVO"
        );

        Usuario usuarioGuardado = crearUsuario(
                1,
                dto.getUsername(),
                dto.getPassword(),
                dto.getCorreo(),
                dto.getRol(),
                dto.getEstado()
        );

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // When
        UsuarioResponseDTO resultado = usuarioService.guardarUsuario(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        assertEquals("nuevoUsuario", resultado.getUsername());
        assertEquals("nuevo@test.cl", resultado.getCorreo());
        assertEquals("ADMIN", resultado.getRol());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_cuandoExiste_deberiaActualizarYRetornarUsuario() {
        // Given
        Usuario usuarioExistente = crearUsuario(
                1,
                "usuarioAntiguo",
                "claveAntigua",
                "antiguo@test.cl",
                "PACIENTE",
                "ACTIVO"
        );

        UsuarioRequestDTO dto = crearRequestDTO(
                "usuarioActualizado",
                "claveNueva",
                "actualizado@test.cl",
                "ADMIN",
                "INACTIVO"
        );

        Usuario usuarioActualizado = crearUsuario(
                1,
                dto.getUsername(),
                dto.getPassword(),
                dto.getCorreo(),
                dto.getRol(),
                dto.getEstado()
        );

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // When
        UsuarioResponseDTO resultado = usuarioService.actualizarUsuario(1, dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        assertEquals("usuarioActualizado", resultado.getUsername());
        assertEquals("actualizado@test.cl", resultado.getCorreo());
        assertEquals("ADMIN", resultado.getRol());
        assertEquals("INACTIVO", resultado.getEstado());

        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_cuandoNoExiste_noDebeGuardarYLanzaExcepcion() {
        // Given
        UsuarioRequestDTO dto = crearRequestDTO(
                "usuario",
                "clave",
                "usuario@test.cl",
                "ADMIN",
                "ACTIVO"
        );

        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.actualizarUsuario(99, dto)
        );

        assertEquals("Usuario no encontrado con id: 99", exception.getMessage());

        verify(usuarioRepository, times(1)).findById(99);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void eliminarUsuario_cuandoExiste_deberiaEliminarUsuario() {
        // Given
        Usuario usuario = crearUsuario(
                1,
                "usuarioEliminar",
                "clave",
                "eliminar@test.cl",
                "PACIENTE",
                "INACTIVO"
        );

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        // When
        usuarioService.eliminarUsuario(1);

        // Then
        verify(usuarioRepository, times(1)).findById(1);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void eliminarUsuario_cuandoNoExiste_noDebeEliminarYLanzaExcepcion() {
        // Given
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // When - Then
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> usuarioService.eliminarUsuario(99)
        );

        assertEquals("Usuario no encontrado con id: 99", exception.getMessage());

        verify(usuarioRepository, times(1)).findById(99);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }

    private Usuario crearUsuario(
            Integer id,
            String username,
            String password,
            String correo,
            String rol,
            String estado
    ) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setCorreo(correo);
        usuario.setRol(rol);
        usuario.setEstado(estado);
        return usuario;
    }

    private UsuarioRequestDTO crearRequestDTO(
            String username,
            String password,
            String correo,
            String rol,
            String estado
    ) {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setUsername(username);
        dto.setPassword(password);
        dto.setCorreo(correo);
        dto.setRol(rol);
        dto.setEstado(estado);
        return dto;
    }
}