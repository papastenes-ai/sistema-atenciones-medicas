package com.duoc.atencionesmedicas.diagnostico.controller;

import com.duoc.atencionesmedicas.diagnostico.model.Diagnostico;
import com.duoc.atencionesmedicas.diagnostico.service.DiagnosticoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diagnosticos")
public class DiagnosticoController {

    @Autowired
    private DiagnosticoService diagnosticoService;

    @GetMapping
    public ResponseEntity<?> listarDiagnosticos() {
        return ResponseEntity.ok(diagnosticoService.listarDiagnosticos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(diagnosticoService.buscarPorId(id));
    }

    @GetMapping("/atencion/{atencionId}")
        public ResponseEntity<?> buscarPorAtencionId(@PathVariable Integer atencionId) {
            return ResponseEntity.ok(diagnosticoService.buscarPorAtencionId(atencionId));
    }

    @GetMapping("/{id}/detalle")
        public ResponseEntity<?> buscarDetallePorId(@PathVariable Integer id) {
            return ResponseEntity.ok(diagnosticoService.buscarDetallePorId(id));
    }

    @PostMapping
    public ResponseEntity<?> guardarDiagnostico(@Valid @RequestBody Diagnostico diagnostico) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(diagnosticoService.guardarDiagnostico(diagnostico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDiagnostico(@PathVariable Integer id,@Valid @RequestBody Diagnostico diagnostico) {
        return ResponseEntity.ok(diagnosticoService.actualizarDiagnostico(id, diagnostico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDiagnostico(@PathVariable Integer id) {
        diagnosticoService.eliminarDiagnostico(id);
        return ResponseEntity.noContent().build();
    }
}