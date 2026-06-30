package com.duoc.atencionesmedicas.centromedico.controller;

import com.duoc.atencionesmedicas.centromedico.dto.CentroMedicoRequestDTO;
import com.duoc.atencionesmedicas.centromedico.dto.CentroMedicoResponseDTO;
import com.duoc.atencionesmedicas.centromedico.service.CentroMedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/centros-medicos")
@RequiredArgsConstructor
@Tag(name = "Centros Médicos", description = "Operaciones relacionadas con la gestión de centros médicos")
public class CentroMedicoController {

    private final CentroMedicoService centroMedicoService;

    @Operation(summary = "Listar centros médicos", description = "Obtiene todos los centros médicos registrados en el sistema")
    @GetMapping
    public ResponseEntity<?> listarCentros() {
        return ResponseEntity.ok(centroMedicoService.listarCentros());
    }

    @Operation(summary = "Buscar centro médico por ID", description = "Obtiene un centro médico según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<CentroMedicoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(centroMedicoService.buscarPorId(id));
    }

    @Operation(summary = "Buscar centros médicos por comuna", description = "Obtiene centros médicos filtrados según la comuna indicada")
    @GetMapping("/comuna/{comuna}")
    public ResponseEntity<?> buscarPorComuna(@PathVariable String comuna) {
        return ResponseEntity.ok(centroMedicoService.buscarPorComuna(comuna));
    }

    @Operation(summary = "Buscar centros médicos por estado", description = "Obtiene centros médicos filtrados según el estado indicado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(centroMedicoService.buscarPorEstado(estado));
    }

    @Operation(summary = "Buscar centros médicos por nombre", description = "Obtiene centros médicos filtrados según el nombre ingresado")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(centroMedicoService.buscarPorNombre(nombre));
    }

    @Operation(summary = "Crear centro médico", description = "Registra un nuevo centro médico en el sistema")
    @PostMapping
    public ResponseEntity<CentroMedicoResponseDTO> guardarCentro(
            @Valid @RequestBody CentroMedicoRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(centroMedicoService.guardarCentro(dto));
    }

    @Operation(summary = "Actualizar centro médico", description = "Actualiza los datos de un centro médico existente según su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<CentroMedicoResponseDTO> actualizarCentro(
            @PathVariable Integer id,
            @Valid @RequestBody CentroMedicoRequestDTO dto) {

        return ResponseEntity.ok(centroMedicoService.actualizarCentro(id, dto));
    }

    @Operation(summary = "Eliminar centro médico", description = "Elimina un centro médico según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCentro(@PathVariable Integer id) {
        centroMedicoService.eliminarCentro(id);
        return ResponseEntity.noContent().build();
    }
}