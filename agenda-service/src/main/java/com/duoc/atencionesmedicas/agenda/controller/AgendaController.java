package com.duoc.atencionesmedicas.agenda.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duoc.atencionesmedicas.agenda.model.Agenda;
import com.duoc.atencionesmedicas.agenda.service.AgendaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping
    public ResponseEntity<?> listarAgendas() {
        return ResponseEntity.ok(agendaService.listarAgendas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(agendaService.buscarPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<?> buscarPorPacienteId(@PathVariable Integer pacienteId) {
        return ResponseEntity.ok(agendaService.buscarPorPacienteId(pacienteId));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<?> buscarPorMedicoId(@PathVariable Integer medicoId) {
        return ResponseEntity.ok(agendaService.buscarPorMedicoId(medicoId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(agendaService.buscarPorEstado(estado));
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> buscarDetallePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(agendaService.buscarDetallePorId(id));
    }

    @PostMapping
    public ResponseEntity<?> guardarAgenda(@Valid @RequestBody Agenda agenda) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(agendaService.guardarAgenda(agenda));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAgenda(@PathVariable Integer id,@Valid @RequestBody Agenda agenda) {
        return ResponseEntity.ok(agendaService.actualizarAgenda(id, agenda));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAgenda(@PathVariable Integer id) {
        agendaService.eliminarAgenda(id);
        return ResponseEntity.noContent().build();
    }
}