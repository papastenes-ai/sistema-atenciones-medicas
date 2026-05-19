package com.duoc.atencionesmedicas.medico.controller;

import com.duoc.atencionesmedicas.medico.model.Medico;
import com.duoc.atencionesmedicas.medico.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<?> listarMedicos() {
        return ResponseEntity.ok(medicoService.listarMedicos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }


    @GetMapping("/rut/{rut}")
        public ResponseEntity<?> buscarPorRut(@PathVariable String rut) {
            return ResponseEntity.ok(medicoService.buscarPorRut(rut));
    }

    @GetMapping("/apellido/{apellido}")
        public ResponseEntity<?> buscarPorApellido(@PathVariable String apellido) {
            return ResponseEntity.ok(medicoService.buscarPorApellido(apellido));
    }



    @PostMapping
    public ResponseEntity<?> guardarMedico(@Valid @RequestBody Medico medico) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicoService.guardarMedico(medico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMedico(@PathVariable Integer id,
                                            @Valid @RequestBody Medico medico) {
        return ResponseEntity.ok(medicoService.actualizarMedico(id, medico));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMedico(@PathVariable Integer id) {
        medicoService.eliminarMedico(id);
        return ResponseEntity.noContent().build();
    }
}