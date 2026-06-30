package com.duoc.atencionesmedicas.diagnostico.controller;

import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoRequestDTO;
import com.duoc.atencionesmedicas.diagnostico.dto.DiagnosticoResponseDTO;
import com.duoc.atencionesmedicas.diagnostico.service.DiagnosticoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/diagnosticos")
@RequiredArgsConstructor
@Tag(name = "Diagnósticos", description = "Operaciones relacionadas con la gestión de diagnósticos médicos")
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    @Operation(summary = "Listar diagnósticos",description = "Obtiene todos los diagnósticos médicos registrados en el sistema")
    @GetMapping
    public ResponseEntity<?> listarDiagnosticos() {
        return ResponseEntity.ok(diagnosticoService.listarDiagnosticos());
    }

    @Operation(summary = "Buscar diagnóstico por ID",description = "Obtiene un diagnóstico médico según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(diagnosticoService.buscarPorId(id));
    }

    @Operation(summary = "Buscar diagnósticos por atención",description = "Obtiene los diagnósticos asociados a una atención médica según su identificador")
    @GetMapping("/atencion/{atencionId}")
    public ResponseEntity<?> buscarPorAtencionId(@PathVariable Integer atencionId) {
        return ResponseEntity.ok(diagnosticoService.buscarPorAtencionId(atencionId));
    }

    @Operation(summary = "Obtener detalle de diagnóstico",description = "Obtiene el detalle completo de un diagnóstico, incluyendo información de la atención médica mediante comunicación entre microservicios")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> buscarDetallePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(diagnosticoService.buscarDetallePorId(id));
    }

    @Operation(summary = "Crear diagnóstico",description = "Registra un nuevo diagnóstico asociado a una atención médica existente")
    @PostMapping
    public ResponseEntity<DiagnosticoResponseDTO> guardarDiagnostico(
            @Valid @RequestBody DiagnosticoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(diagnosticoService.guardarDiagnostico(dto));
    }

    @Operation(summary = "Actualizar diagnóstico",description = "Actualiza los datos de un diagnóstico existente según su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<DiagnosticoResponseDTO> actualizarDiagnostico(
            @PathVariable Integer id,
            @Valid @RequestBody DiagnosticoRequestDTO dto) {

        return ResponseEntity.ok(diagnosticoService.actualizarDiagnostico(id, dto));
    }

    @Operation(summary = "Eliminar diagnóstico",description = "Elimina un diagnóstico médico según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDiagnostico(@PathVariable Integer id) {
        diagnosticoService.eliminarDiagnostico(id);
        return ResponseEntity.noContent().build();
    }
}