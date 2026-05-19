package com.duoc.atencionesmedicas.medicamento.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duoc.atencionesmedicas.medicamento.model.Medicamento;
import com.duoc.atencionesmedicas.medicamento.service.MedicamentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @GetMapping
    public ResponseEntity<?> listarMedicamentos() {

        return ResponseEntity.ok(
                medicamentoService.listarMedicamentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {

        return ResponseEntity.ok(
                medicamentoService.buscarPorId(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> buscarPorNombre(
            @PathVariable String nombre) {

        return ResponseEntity.ok(
                medicamentoService.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<?> guardarMedicamento(
            @Valid @RequestBody Medicamento medicamento) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicamentoService.guardarMedicamento(medicamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMedicamento(
            @PathVariable Integer id,
            @Valid @RequestBody Medicamento medicamento) {

        return ResponseEntity.ok(
                medicamentoService.actualizarMedicamento(id, medicamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedicamento(
            @PathVariable Integer id) {

        medicamentoService.eliminarMedicamento(id);

        return ResponseEntity.noContent().build();
    }
}