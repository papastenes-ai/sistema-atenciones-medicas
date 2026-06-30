package com.duoc.atencionesmedicas.usuario.service;

import com.duoc.atencionesmedicas.usuario.dto.UsuarioRequestDTO;
import com.duoc.atencionesmedicas.usuario.dto.UsuarioResponseDTO;
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
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsername("Pablo");
        usuario.setCorreo("pablo@test.cl");
        usuario.setPassword("123456");
        usuario.setRol("ADMIN");
        usuario.setEstado("ACTIVO");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.listarUsuarios();

        assertEquals(1, resultado.size());
        assertEquals("Pablo", resultado.get(0).getUsername());
        assertEquals("pablo@test.cl", resultado.get(0).getCorreo());
        assertEquals("ADMIN", resultado.get(0).getRol());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_deberiaRetornarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsername("Juan");
        usuario.setCorreo("juan@test.cl");
        usuario.setPassword("123456");
        usuario.setRol("MEDICO");
        usuario.setEstado("ACTIVO");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getUsername());
        assertEquals("juan@test.cl", resultado.getCorreo());
        assertEquals("MEDICO", resultado.getRol());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    void guardarUsuario_deberiaGuardarYRetornarUsuario() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setUsername("Ana");
        dto.setCorreo("ana@test.cl");
        dto.setPassword("123456");
        dto.setRol("RECEPCIONISTA");
        dto.setEstado("ACTIVO");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setIdUsuario(1);
        usuarioGuardado.setUsername(dto.getUsername());
        usuarioGuardado.setCorreo(dto.getCorreo());
        usuarioGuardado.setPassword(dto.getPassword());
        usuarioGuardado.setRol(dto.getRol());
        usuarioGuardado.setEstado(dto.getEstado());

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        UsuarioResponseDTO resultado = usuarioService.guardarUsuario(dto);

        assertNotNull(resultado);
        assertEquals("Ana", resultado.getUsername());
        assertEquals("ana@test.cl", resultado.getCorreo());
        assertEquals("RECEPCIONISTA", resultado.getRol());
        assertEquals("ACTIVO", resultado.getEstado());

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void buscarPorRol_deberiaRetornarUsuariosFiltrados() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsername("Carlos");
        usuario.setCorreo("carlos@test.cl");
        usuario.setPassword("123456");
        usuario.setRol("ADMIN");
        usuario.setEstado("ACTIVO");

        when(usuarioRepository.findByRol("ADMIN")).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.buscarPorRol("ADMIN");

        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getUsername());
        assertEquals("ADMIN", resultado.get(0).getRol());

        verify(usuarioRepository, times(1)).findByRol("ADMIN");
    }

    @Test
    void buscarPorEstado_deberiaRetornarUsuariosFiltrados() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsername("Fernanda");
        usuario.setCorreo("fernanda@test.cl");
        usuario.setPassword("123456");
        usuario.setRol("ADMIN");
        usuario.setEstado("ACTIVO");

        when(usuarioRepository.findByEstado("ACTIVO")).thenReturn(List.of(usuario));

        List<UsuarioResponseDTO> resultado = usuarioService.buscarPorEstado("ACTIVO");

        assertEquals(1, resultado.size());
        assertEquals("Fernanda", resultado.get(0).getUsername());
        assertEquals("ACTIVO", resultado.get(0).getEstado());

        verify(usuarioRepository, times(1)).findByEstado("ACTIVO");
    }
}