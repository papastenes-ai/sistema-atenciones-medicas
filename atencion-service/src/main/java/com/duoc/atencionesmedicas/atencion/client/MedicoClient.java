package com.duoc.atencionesmedicas.atencion.client;

import com.duoc.atencionesmedicas.atencion.dto.MedicoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "medico-service",
        url = "${MEDICO_SERVICE_URL:http://localhost:8082}"
)
public interface MedicoClient {

    @GetMapping("/api/medicos/{id}")
    MedicoDTO obtenerMedicoPorId(@PathVariable("id") Integer id);
}