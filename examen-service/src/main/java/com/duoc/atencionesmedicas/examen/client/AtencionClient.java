package com.duoc.atencionesmedicas.examen.client;

import com.duoc.atencionesmedicas.examen.dto.AtencionDetalleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "atencion-service", url = "http://localhost:8083")
public interface AtencionClient {

    @GetMapping("/api/atenciones/{id}/detalle")
    AtencionDetalleDTO obtenerAtencionPorId(@PathVariable Integer id);
}