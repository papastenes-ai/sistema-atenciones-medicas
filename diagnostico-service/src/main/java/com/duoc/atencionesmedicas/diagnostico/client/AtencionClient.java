package com.duoc.atencionesmedicas.diagnostico.client;

import com.duoc.atencionesmedicas.diagnostico.dto.AtencionDetalleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//feing las usa como contrato para consttruir la peticion 
@FeignClient(name = "atencion-service", url = "http://localhost:8083")
public interface AtencionClient {

    @GetMapping("/api/atenciones/{id}/detalle")
    AtencionDetalleDTO obtenerAtencionPorId(@PathVariable Integer id);
}