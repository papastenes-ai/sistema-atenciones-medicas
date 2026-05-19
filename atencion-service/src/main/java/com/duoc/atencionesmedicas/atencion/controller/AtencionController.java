package com.duoc.atencionesmedicas.atencion.controller;

import com.duoc.atencionesmedicas.atencion.model.Atencion;
import com.duoc.atencionesmedicas.atencion.service.AtencionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    @Autowired
    private AtencionService atencionService;

    @GetMapping
    public ResponseEntity<?> listarAtenciones() {
        return ResponseEntity.ok(atencionService.listarAtenciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(atencionService.buscarPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
        public ResponseEntity<?> buscarPorPacienteId(@PathVariable Integer pacienteId) {
            return ResponseEntity.ok(atencionService.buscarPorPacienteId(pacienteId));
    }

    @GetMapping("/medico/{medicoId}")
        public ResponseEntity<?> buscarPorMedicoId(@PathVariable Integer medicoId) {
            return ResponseEntity.ok(atencionService.buscarPorMedicoId(medicoId));
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<?> buscarDetallePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(atencionService.buscarDetallePorId(id));
    }

    @PostMapping
    public ResponseEntity<?> guardarAtencion(@Valid @RequestBody Atencion atencion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(atencionService.guardarAtencion(atencion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAtencion(@PathVariable Integer id,
                                                @Valid @RequestBody Atencion atencion) {
        return ResponseEntity.ok(atencionService.actualizarAtencion(id, atencion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAtencion(@PathVariable Integer id) {
        atencionService.eliminarAtencion(id);
        return ResponseEntity.noContent().build();
    }
}
