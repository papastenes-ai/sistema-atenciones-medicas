package com.duoc.atencionesmedicas.paciente.controller;

import com.duoc.atencionesmedicas.paciente.model.Paciente;
import com.duoc.atencionesmedicas.paciente.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<?> listarPacientes() {
        return ResponseEntity.ok(pacienteService.listarPacientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }


    @GetMapping("/rut/{rut}")
    public ResponseEntity<?> buscarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(pacienteService.buscarPorRut(rut));
    }



    @PostMapping
    public ResponseEntity<?> guardarPaciente(@Valid @RequestBody Paciente paciente) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pacienteService.guardarPaciente(paciente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPaciente(@PathVariable Integer id,
                                                @Valid @RequestBody Paciente paciente) {
        return ResponseEntity.ok(pacienteService.actualizarPaciente(id, paciente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPaciente(@PathVariable Integer id) {
        pacienteService.eliminarPaciente(id);
        return ResponseEntity.noContent().build();
    }
}