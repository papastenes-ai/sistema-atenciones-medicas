package com.duoc.atencionesmedicas.usuario.service;

import com.duoc.atencionesmedicas.usuario.dto.UsuarioRequestDTO;
import com.duoc.atencionesmedicas.usuario.dto.UsuarioResponseDTO;
import com.duoc.atencionesmedicas.usuario.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.usuario.model.Usuario;
import com.duoc.atencionesmedicas.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private UsuarioResponseDTO mapToResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getCorreo(),
                usuario.getRol(),
                usuario.getEstado()
        );
    }

    private Usuario buscarEntidadPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + id));
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Integer id) {
        Usuario usuario = buscarEntidadPorId(id);
        return mapToResponseDTO(usuario);
    }

    public UsuarioResponseDTO buscarPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado: " + username));

        return mapToResponseDTO(usuario);
    }

    public List<UsuarioResponseDTO> buscarPorRol(String rol) {
        List<Usuario> usuarios = usuarioRepository.findByRol(rol);

        if (usuarios.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen usuarios con rol: " + rol);
        }

        return usuarios.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> buscarPorEstado(String estado) {
        List<Usuario> usuarios = usuarioRepository.findByEstado(estado);

        if (usuarios.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existen usuarios con estado: " + estado);
        }

        return usuarios.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO guardarUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario(
                null,
                dto.getUsername(),
                dto.getPassword(),
                dto.getCorreo(),
                dto.getRol(),
                dto.getEstado()
        );

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        log.info("Usuario guardado correctamente con id: {}",
                usuarioGuardado.getIdUsuario());

        return mapToResponseDTO(usuarioGuardado);
    }

    public UsuarioResponseDTO actualizarUsuario(Integer id, UsuarioRequestDTO dto) {
        Usuario usuario = buscarEntidadPorId(id);

        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
        usuario.setCorreo(dto.getCorreo());
        usuario.setRol(dto.getRol());
        usuario.setEstado(dto.getEstado());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        log.info("Usuario id {} actualizado correctamente.", id);

        return mapToResponseDTO(usuarioActualizado);
    }

    public void eliminarUsuario(Integer id) {
        Usuario usuario = buscarEntidadPorId(id);
        usuarioRepository.delete(usuario);

        log.info("Usuario id {} eliminado correctamente.", id);
    }
}