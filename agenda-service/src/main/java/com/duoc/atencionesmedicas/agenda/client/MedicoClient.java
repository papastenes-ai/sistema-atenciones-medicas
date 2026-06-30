package com.duoc.atencionesmedicas.agenda.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.duoc.atencionesmedicas.agenda.dto.MedicoDTO;

@FeignClient(name = "medico-service")
public interface MedicoClient {

    @GetMapping("/api/medicos/{id}")
    MedicoDTO obtenerMedicoPorId(@PathVariable Integer id);
}
