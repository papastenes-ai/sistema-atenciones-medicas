package com.duoc.atencionesmedicas.atencion.client;

import com.duoc.atencionesmedicas.atencion.dto.PacienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "paciente-service", url = "http://localhost:8081")
public interface PacienteClient {

    @GetMapping("/api/pacientes/{id}")
    PacienteDTO obtenerPacientePorId(@PathVariable Integer id);
}