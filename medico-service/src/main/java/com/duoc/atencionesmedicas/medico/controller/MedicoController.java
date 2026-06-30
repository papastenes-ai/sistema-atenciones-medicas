package com.duoc.atencionesmedicas.medico.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.duoc.atencionesmedicas.medico.dto.MedicoRequestDTO;
import com.duoc.atencionesmedicas.medico.dto.MedicoResponseDTO;
import com.duoc.atencionesmedicas.medico.service.MedicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
@Tag(name = "Médicos", description = "Operaciones relacionadas con la gestión de médicos")
public class MedicoController {

    private final MedicoService medicoService;

    @Operation(summary = "Listar médicos", description = "Obtiene todos los médicos registrados")
    @GetMapping
    public ResponseEntity<?> listarMedicos() {
        return ResponseEntity.ok(medicoService.listarMedicos());
    }

    @Operation(summary = "Buscar médico por ID", description = "Obtiene un médico según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @Operation(summary = "Buscar médicos por especialidad",description = "Obtiene el listado de médicos filtrados según el nombre de la especialidad indicada")
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<?> buscarPorEspecialidad(@PathVariable String especialidad) {
        return ResponseEntity.ok(medicoService.buscarPorEspecialidad(especialidad));
    }

    @Operation(summary = "Crear médico", description = "Registra un nuevo médico en el sistema")
    @PostMapping
    public ResponseEntity<MedicoResponseDTO> guardarMedico(@Valid @RequestBody MedicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(medicoService.guardarMedico(dto));
    }

    @Operation(summary = "Actualizar médico", description = "Actualiza los datos de un médico existente")
    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> actualizarMedico(@PathVariable Integer id,@Valid @RequestBody MedicoRequestDTO dto) {
        return ResponseEntity.ok(medicoService.actualizarMedico(id, dto));
    }

    @Operation(summary = "Eliminar médico", description = "Elimina un médico según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedico(@PathVariable Integer id) {medicoService.eliminarMedico(id);
        return ResponseEntity.noContent().build();
    }
}