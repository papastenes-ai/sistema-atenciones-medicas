package com.duoc.atencionesmedicas.examen.controller;

import com.duoc.atencionesmedicas.examen.dto.ExamenRequestDTO;
import com.duoc.atencionesmedicas.examen.dto.ExamenResponseDTO;
import com.duoc.atencionesmedicas.examen.service.ExamenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/examenes")
@RequiredArgsConstructor
@Tag(name = "Exámenes", description = "Operaciones relacionadas con la gestión de exámenes médicos")
public class ExamenController {

    private final ExamenService examenService;

    @Operation(summary = "Listar exámenes",description = "Obtiene todos los exámenes médicos registrados en el sistema")
    @GetMapping
    public ResponseEntity<?> listarExamenes() {
        return ResponseEntity.ok(examenService.listarExamenes());
    }

    @Operation(summary = "Buscar examen por ID",description = "Obtiene un examen médico según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<ExamenResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(examenService.buscarPorId(id));
    }

    @Operation(summary = "Buscar exámenes por atención",description = "Obtiene los exámenes médicos asociados a una atención médica según su identificador")
    @GetMapping("/atencion/{atencionId}")
    public ResponseEntity<?> buscarPorAtencionId(@PathVariable Integer atencionId) {
        return ResponseEntity.ok(examenService.buscarPorAtencionId(atencionId));
    }


    @Operation(summary = "Buscar exámenes por nombre", description = "Obtiene los exámenes médicos filtrados según el nombre del examen ingresado")
    @GetMapping("/nombre/{nombreExamen}")
    public ResponseEntity<?> buscarPorNombreExamen(@PathVariable String nombreExamen) {
        return ResponseEntity.ok(examenService.buscarPorNombreExamen(nombreExamen));
    }

    @Operation(summary = "Obtener detalle de examen",description = "Obtiene el detalle completo de un examen médico, incluyendo información de la atención mediante comunicación entre microservicios")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> buscarDetallePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(examenService.buscarDetallePorId(id));
    }

    @Operation(summary = "Crear examen médico",description = "Registra un nuevo examen médico asociado a una atención médica existente")
    @PostMapping
    public ResponseEntity<ExamenResponseDTO> guardarExamen(
            @Valid @RequestBody ExamenRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examenService.guardarExamen(dto));
    }

    @Operation(summary = "Actualizar examen médico",description = "Actualiza los datos de un examen médico existente según su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<ExamenResponseDTO> actualizarExamen(
            @PathVariable Integer id,
            @Valid @RequestBody ExamenRequestDTO dto) {

        return ResponseEntity.ok(examenService.actualizarExamen(id, dto));
    }

    @Operation(summary = "Eliminar examen médico",description = "Elimina un examen médico según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarExamen(@PathVariable Integer id) {
        examenService.eliminarExamen(id);
        return ResponseEntity.noContent().build();
    }
}