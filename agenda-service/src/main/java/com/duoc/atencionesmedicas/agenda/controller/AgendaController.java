package com.duoc.atencionesmedicas.agenda.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.atencionesmedicas.agenda.dto.AgendaRequestDTO;
import com.duoc.atencionesmedicas.agenda.dto.AgendaResponseDTO;
import com.duoc.atencionesmedicas.agenda.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
@Tag(name = "Agendas", description = "Operaciones relacionadas con la gestión de agendas médicas")
public class AgendaController {

    private final AgendaService agendaService;


    @Operation(summary = "Listar agendas", description = "Obtiene todas las agendas médicas registradas en el sistema")
    @GetMapping
    public ResponseEntity<?> listarAgendas() {
        return ResponseEntity.ok(agendaService.listarAgendas());
    }

    @Operation(summary = "Buscar agenda por ID", description = "Obtiene una agenda médica según su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(agendaService.buscarPorId(id));
    }

    @Operation(summary = "Buscar agendas por paciente", description = "Obtiene las agendas médicas asociadas a un paciente según su identificador")
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<?> buscarPorPacienteId(@PathVariable Integer pacienteId) {
        return ResponseEntity.ok(agendaService.buscarPorPacienteId(pacienteId));
    }

    @Operation(summary = "Buscar agendas por médico", description = "Obtiene las agendas médicas asociadas a un médico según su identificador")
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<?> buscarPorMedicoId(@PathVariable Integer medicoId) {
        return ResponseEntity.ok(agendaService.buscarPorMedicoId(medicoId));
    }

    @Operation(summary = "Buscar agendas por estado", description = "Obtiene las agendas médicas filtradas según el estado indicado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(agendaService.buscarPorEstado(estado));
    }

    @Operation(summary = "Obtener detalle de agenda", description = "Obtiene el detalle completo de una agenda médica, incluyendo información del paciente y del médico mediante comunicación entre microservicios")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> buscarDetallePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(agendaService.buscarDetallePorId(id));
    }

    @Operation(summary = "Crear agenda médica", description = "Registra una nueva agenda médica asociada a un paciente y un médico existentes")
    @PostMapping
    public ResponseEntity<AgendaResponseDTO> guardarAgenda(
            @Valid @RequestBody AgendaRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(agendaService.guardarAgenda(dto));
    }

    @Operation(summary = "Actualizar agenda médica", description = "Actualiza los datos de una agenda médica existente según su identificador")
    @PutMapping("/{id}")
    public ResponseEntity<AgendaResponseDTO> actualizarAgenda(
            @PathVariable Integer id,
            @Valid @RequestBody AgendaRequestDTO dto) {

        return ResponseEntity.ok(agendaService.actualizarAgenda(id, dto));
    }

    @Operation(summary = "Eliminar agenda médica", description = "Elimina una agenda médica según su identificador")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAgenda(@PathVariable Integer id) {
        agendaService.eliminarAgenda(id);
        return ResponseEntity.noContent().build();
    }
}