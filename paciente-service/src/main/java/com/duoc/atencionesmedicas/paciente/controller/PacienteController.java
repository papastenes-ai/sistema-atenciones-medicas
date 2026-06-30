package com.duoc.atencionesmedicas.paciente.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import com.duoc.atencionesmedicas.paciente.dto.PacienteRequestDTO;
import com.duoc.atencionesmedicas.paciente.dto.PacienteResponseDTO;
import com.duoc.atencionesmedicas.paciente.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Operaciones relacionadas con la gestión de pacientes")
public class PacienteController {

    private final PacienteService pacienteService;
    @Operation(summary = "Listar pacientes", description = "Obtiene el listado completo de pacientes registrados")
    @GetMapping
    public ResponseEntity<?> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }
    
    @Operation(summary = "Buscar paciente por ID", description = "Obtiene los datos de un paciente según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @Operation(summary = "Buscar paciente por RUT",description = "Obtiene los datos de un paciente según el RUT ingresado")
    @GetMapping("/rut/{rut}")
    public ResponseEntity<PacienteResponseDTO> buscarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(pacienteService.buscarPorRut(rut));
    }
    @Operation(summary = "Crear paciente", description = "Registra un nuevo paciente en el sistema")
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> guardarPaciente(@Valid @RequestBody PacienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pacienteService.guardarPaciente(dto));
    }

    @Operation(summary = "Actualizar paciente", description = "Actualiza los datos de un paciente existente")
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> actualizarPaciente(@PathVariable Integer id,@Valid @RequestBody PacienteRequestDTO dto) {
        return ResponseEntity.ok(pacienteService.actualizarPaciente(id, dto));
    }
     
    @Operation(summary = "Eliminar paciente", description = "Elimina un paciente según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Integer id) {pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }
}