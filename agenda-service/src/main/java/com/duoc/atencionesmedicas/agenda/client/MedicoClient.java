package com.duoc.atencionesmedicas.agenda.client;

import com.duoc.atencionesmedicas.agenda.dto.MedicoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "medico-service", url = "http://localhost:8082")
public interface MedicoClient {

    @GetMapping("/api/medicos/{id}")
    MedicoDTO obtenerMedicoPorId(@PathVariable Integer id);
}