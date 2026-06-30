package com.duoc.atencionesmedicas.medicamento.controller;

import com.duoc.atencionesmedicas.medicamento.dto.MedicamentoRequestDTO;
import com.duoc.atencionesmedicas.medicamento.dto.MedicamentoResponseDTO;
import com.duoc.atencionesmedicas.medicamento.service.MedicamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
@Tag(name = "Medicamentos", description = "Operaciones relacionadas con la gestión de medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @Operation(summary = "Listar medicamentos", description = "Obtiene todos los medicamentos registrados en el sistema")
    @GetMapping
    public ResponseEntity<?> listarMedicamentos() {
        return ResponseEntity.ok(medicamentoService.listarMedicamentos());
    }

    @Operation(summary = "Buscar medicamento por ID", description = "Obtiene un medicamento según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<MedicamentoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(medicamentoService.buscarPorId(id));
    }


    @Operation(summary = "Buscar medicamentos por nombre", description = "Obtiene medicamentos filtrados según el nombre del medicamento ingresado")
    @GetMapping("/nombre/{nombreMedicamento}")
    public ResponseEntity<?> buscarPorNombreMedicamento(@PathVariable String nombreMedicamento) {
        return ResponseEntity.ok(medicamentoService.buscarPorNombreMedicamento(nombreMedicamento));
    }

    @Operation(summary = "Crear medicamento", description = "Registra un nuevo medicamento en el sistema")
    @PostMapping
    public ResponseEntity<MedicamentoResponseDTO> guardarMedicamento(
            @Valid @RequestBody MedicamentoRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicamentoService.guardarMedicamento(dto));
    }

    @Operation(summary = "Actualizar medicamento", description = "Actualiza los datos de un medicamento existente según su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoResponseDTO> actualizarMedicamento(
            @PathVariable Integer id,
            @Valid @RequestBody MedicamentoRequestDTO dto) {

        return ResponseEntity.ok(medicamentoService.actualizarMedicamento(id, dto));
    }

    @Operation(summary = "Eliminar medicamento", description = "Elimina un medicamento según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedicamento(@PathVariable Integer id) {
        medicamentoService.eliminarMedicamento(id);
        return ResponseEntity.noContent().build();
    }
}