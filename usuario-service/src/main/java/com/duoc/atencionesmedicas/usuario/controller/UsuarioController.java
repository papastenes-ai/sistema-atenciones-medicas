package com.duoc.atencionesmedicas.usuario.controller;

import com.duoc.atencionesmedicas.usuario.dto.UsuarioRequestDTO;
import com.duoc.atencionesmedicas.usuario.dto.UsuarioResponseDTO;
import com.duoc.atencionesmedicas.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados en el sistema")
    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @Operation(summary = "Buscar usuario por ID", description = "Obtiene un usuario según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(summary = "Buscar usuario por nombre de usuario", description = "Obtiene un usuario según el nombre de usuario ingresado")
    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.buscarPorUsername(username));
    }

    @Operation(summary = "Buscar usuarios por rol", description = "Obtiene usuarios filtrados según el rol indicado")
    @GetMapping("/rol/{rol}")
    public ResponseEntity<?> buscarPorRol(@PathVariable String rol) {
        return ResponseEntity.ok(usuarioService.buscarPorRol(rol));
    }

    @Operation(summary = "Buscar usuarios por estado", description = "Obtiene usuarios filtrados según el estado indicado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(usuarioService.buscarPorEstado(estado));
    }

    @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario en el sistema")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> guardarUsuario(
            @Valid @RequestBody UsuarioRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.guardarUsuario(dto));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente según su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequestDTO dto) {

        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, dto));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}