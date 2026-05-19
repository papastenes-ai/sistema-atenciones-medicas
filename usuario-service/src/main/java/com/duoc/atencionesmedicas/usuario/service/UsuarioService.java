package com.duoc.atencionesmedicas.usuario.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.duoc.atencionesmedicas.usuario.exception.RecursoNoEncontradoException;
import com.duoc.atencionesmedicas.usuario.model.Usuario;
import com.duoc.atencionesmedicas.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado con id: " + id));
    }

    public Usuario buscarPorUsername(String username) {

        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado: " + username));
    }

    public List<Usuario> buscarPorRol(String rol) {

        List<Usuario> usuarios =
                usuarioRepository.findByRol(rol);

        if (usuarios.isEmpty()) {

            throw new RecursoNoEncontradoException(
                    "No existen usuarios con rol: " + rol);
        }

        return usuarios;
    }

    public List<Usuario> buscarPorEstado(String estado) {

        List<Usuario> usuarios =
                usuarioRepository.findByEstado(estado);

        if (usuarios.isEmpty()) {

            throw new RecursoNoEncontradoException(
                    "No existen usuarios con estado: " + estado);
        }

        return usuarios;
    }

    public Usuario guardarUsuario(Usuario usuario) {

        try {

            return usuarioRepository.save(usuario);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al guardar el usuario: " + e.getMessage());
        }
    }

    public Usuario actualizarUsuario(Integer id,Usuario usuarioActualizado) {

        try {

            Usuario usuario = buscarPorId(id);

            usuario.setUsername(
                    usuarioActualizado.getUsername());

            usuario.setPassword(
                    usuarioActualizado.getPassword());

            usuario.setCorreo(
                    usuarioActualizado.getCorreo());

            usuario.setRol(
                    usuarioActualizado.getRol());

            usuario.setEstado(
                    usuarioActualizado.getEstado());

            return usuarioRepository.save(usuario);

        } catch (RecursoNoEncontradoException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al actualizar el usuario: " + e.getMessage());
        }
    }

    public void eliminarUsuario(Integer id) {

        try {

            Usuario usuario = buscarPorId(id);

            usuarioRepository.delete(usuario);

        } catch (RecursoNoEncontradoException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al eliminar el usuario: " + e.getMessage());
        }
    }
}