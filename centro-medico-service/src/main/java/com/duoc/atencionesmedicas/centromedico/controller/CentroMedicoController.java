package com.duoc.atencionesmedicas.centromedico.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duoc.atencionesmedicas.centromedico.model.CentroMedico;
import com.duoc.atencionesmedicas.centromedico.service.CentroMedicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/centros-medicos")
@RequiredArgsConstructor
public class CentroMedicoController {

    private final CentroMedicoService centroMedicoService;

    @GetMapping
    public ResponseEntity<?> listarCentros() {
        return ResponseEntity.ok(centroMedicoService.listarCentros());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(centroMedicoService.buscarPorId(id));
    }

    @GetMapping("/comuna/{comuna}")
    public ResponseEntity<?> buscarPorComuna(@PathVariable String comuna) {
        return ResponseEntity.ok(centroMedicoService.buscarPorComuna(comuna));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(centroMedicoService.buscarPorEstado(estado));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(centroMedicoService.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<?> guardarCentro(@Valid @RequestBody CentroMedico centroMedico) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(centroMedicoService.guardarCentro(centroMedico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCentro(@PathVariable Integer id,@Valid @RequestBody CentroMedico centroMedico) {
        return ResponseEntity.ok(centroMedicoService.actualizarCentro(id, centroMedico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCentro(@PathVariable Integer id) {
        centroMedicoService.eliminarCentro(id);
        return ResponseEntity.noContent().build();
    }
}