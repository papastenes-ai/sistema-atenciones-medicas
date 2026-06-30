package com.duoc.atencionesmedicas.diagnostico.client;

import com.duoc.atencionesmedicas.diagnostico.dto.AtencionDetalleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "atencion-service")
public interface AtencionClient {

    @GetMapping("/api/atenciones/{id}/detalle")
    AtencionDetalleDTO obtenerAtencionPorId(@PathVariable Integer id);
}