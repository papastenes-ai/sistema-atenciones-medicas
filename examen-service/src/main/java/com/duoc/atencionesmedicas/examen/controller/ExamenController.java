package com.duoc.atencionesmedicas.examen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duoc.atencionesmedicas.examen.model.Examen;
import com.duoc.atencionesmedicas.examen.service.ExamenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/examenes")
@RequiredArgsConstructor
public class ExamenController {

    private final ExamenService examenService;

    @GetMapping
    public ResponseEntity<?> listarExamenes() {

        return ResponseEntity.ok(
                examenService.listarExamenes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {

        return ResponseEntity.ok(
                examenService.buscarPorId(id));
    }

    @GetMapping("/atencion/{atencionId}")
    public ResponseEntity<?> buscarPorAtencionId(
            @PathVariable Integer atencionId) {

        return ResponseEntity.ok(
                examenService.buscarPorAtencionId(atencionId));
    }

    @GetMapping("/nombre/{nombreExamen}")
    public ResponseEntity<?> buscarPorNombreExamen(
            @PathVariable String nombreExamen) {

        return ResponseEntity.ok(
                examenService.buscarPorNombreExamen(nombreExamen));
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> buscarDetallePorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                examenService.buscarDetallePorId(id));
    }

    @PostMapping
    public ResponseEntity<?> guardarExamen(
            @Valid @RequestBody Examen examen) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(examenService.guardarExamen(examen));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarExamen(
            @PathVariable Integer id,
            @Valid @RequestBody Examen examen) {

        return ResponseEntity.ok(
                examenService.actualizarExamen(id, examen));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarExamen(
            @PathVariable Integer id) {

        examenService.eliminarExamen(id);

        return ResponseEntity.noContent().build();
    }
}