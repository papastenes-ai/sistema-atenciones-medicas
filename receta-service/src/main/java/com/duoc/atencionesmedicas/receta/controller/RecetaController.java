package com.duoc.atencionesmedicas.receta.controller;

import com.duoc.atencionesmedicas.receta.dto.RecetaRequestDTO;
import com.duoc.atencionesmedicas.receta.dto.RecetaResponseDTO;
import com.duoc.atencionesmedicas.receta.service.RecetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/recetas")
@RequiredArgsConstructor
@Tag(name = "Recetas", description = "Operaciones relacionadas con la gestión de recetas médicas")
public class RecetaController {

    private final RecetaService recetaService;

    @Operation(summary = "Listar recetas",description = "Obtiene todas las recetas médicas registradas en el sistema")
    @GetMapping
    public ResponseEntity<?> listarRecetas() {
        return ResponseEntity.ok(recetaService.listarRecetas());
    }

    @Operation(summary = "Buscar receta por ID",description = "Obtiene una receta médica según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<RecetaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(recetaService.buscarPorId(id));
    }

    @Operation(summary = "Buscar recetas por diagnóstico",description = "Obtiene las recetas médicas asociadas a un diagnóstico según su identificador")
    @GetMapping("/diagnostico/{diagnosticoId}")
    public ResponseEntity<?> buscarPorDiagnosticoId(@PathVariable Integer diagnosticoId) {
        return ResponseEntity.ok(recetaService.buscarPorDiagnosticoId(diagnosticoId));
    }

    @Operation(summary = "Obtener detalle de receta",description = "Obtiene el detalle completo de una receta médica, incluyendo información del diagnóstico mediante comunicación entre microservicios")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> buscarDetallePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(recetaService.buscarDetallePorId(id));
    }

    @Operation(summary = "Crear receta médica",description = "Registra una nueva receta médica asociada a un diagnóstico existente")
    @PostMapping
    public ResponseEntity<RecetaResponseDTO> guardarReceta(
            @Valid @RequestBody RecetaRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recetaService.guardarReceta(dto));
    }

    @Operation(summary = "Actualizar receta médica",description = "Actualiza los datos de una receta médica existente según su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<RecetaResponseDTO> actualizarReceta(
            @PathVariable Integer id,
            @Valid @RequestBody RecetaRequestDTO dto) {

        return ResponseEntity.ok(recetaService.actualizarReceta(id, dto));
    }

    @Operation(summary = "Eliminar receta médica", description = "Elimina una receta médica según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReceta(@PathVariable Integer id) {
        recetaService.eliminarReceta(id);
        return ResponseEntity.noContent().build();
    }
}