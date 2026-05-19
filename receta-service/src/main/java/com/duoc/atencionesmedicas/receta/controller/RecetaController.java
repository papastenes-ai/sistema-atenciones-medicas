package com.duoc.atencionesmedicas.receta.controller;

import com.duoc.atencionesmedicas.receta.model.Receta;
import com.duoc.atencionesmedicas.receta.service.RecetaService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public ResponseEntity<?> listarRecetas() {
        return ResponseEntity.ok(recetaService.listarRecetas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(recetaService.buscarPorId(id));
    }


    @GetMapping("/diagnostico/{diagnosticoId}")
        public ResponseEntity<?> buscarPorDiagnosticoId(@PathVariable Integer diagnosticoId) {
            return ResponseEntity.ok(recetaService.buscarPorDiagnosticoId(diagnosticoId));
    }


    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> buscarDetallePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(recetaService.buscarDetallePorId(id));
    }

    @PostMapping
    public ResponseEntity<?> guardarReceta(@Valid @RequestBody Receta receta) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recetaService.guardarReceta(receta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarReceta(@PathVariable Integer id,@Valid @RequestBody Receta receta) {

        return ResponseEntity.ok(recetaService.actualizarReceta(id, receta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarReceta(@PathVariable Integer id) {

        recetaService.eliminarReceta(id);

        return ResponseEntity.noContent().build();
    }
}