package com.duoc.atencionesmedicas.atencion.controller;

import com.duoc.atencionesmedicas.atencion.dto.AtencionRequestDTO;
import com.duoc.atencionesmedicas.atencion.dto.AtencionResponseDTO;
import com.duoc.atencionesmedicas.atencion.service.AtencionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/atenciones")
@RequiredArgsConstructor
@Tag(name = "Atenciones", description = "Operaciones relacionadas con la gestión de atenciones médicas")
public class AtencionController {

    private final AtencionService atencionService;

    @Operation(summary = "Listar atenciones",description = "Obtiene todas las atenciones médicas registradas en el sistema")
    @GetMapping
    public ResponseEntity<?> listarAtenciones() {
        return ResponseEntity.ok(atencionService.listarAtenciones());
    }

    @Operation(summary = "Buscar atención por ID",description = "Obtiene una atención médica según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<AtencionResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(atencionService.buscarPorId(id));
    }

    @Operation(summary = "Buscar atenciones por paciente",description = "Obtiene todas las atenciones médicas asociadas a un paciente según su identificador")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<?> buscarPorPacienteId(@PathVariable Integer pacienteId) {
        return ResponseEntity.ok(atencionService.buscarPorPacienteId(pacienteId));
    }

    @Operation(summary = "Buscar atenciones por médico",description = "Obtiene todas las atenciones médicas asociadas a un médico según su identificador")
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<?> buscarPorMedicoId(@PathVariable Integer medicoId) {
        return ResponseEntity.ok(atencionService.buscarPorMedicoId(medicoId));
    }

    @Operation(summary = "Obtener detalle de atención",description = "Obtiene el detalle completo de una atención médica, incluyendo información del paciente y del médico mediante comunicación entre microservicios")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> buscarDetallePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(atencionService.buscarDetallePorId(id));
    }

    @Operation(summary = "Crear atención médica",description = "Registra una nueva atención médica asociada a un paciente y un médico existentes")
    @PostMapping
    public ResponseEntity<AtencionResponseDTO> guardarAtencion(@Valid @RequestBody AtencionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atencionService.guardarAtencion(dto));
    }

    @Operation(summary = "Actualizar atención médica",description = "Actualiza los datos de una atención médica existente según su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<AtencionResponseDTO> actualizarAtencion(@PathVariable Integer id,@Valid @RequestBody AtencionRequestDTO dto) {
        return ResponseEntity.ok(atencionService.actualizarAtencion(id, dto));
    }

    @Operation(summary = "Eliminar atención médica",description = "Elimina una atención médica según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAtencion(@PathVariable Integer id) {atencionService.eliminarAtencion(id);
        return ResponseEntity.noContent().build();
    }
}