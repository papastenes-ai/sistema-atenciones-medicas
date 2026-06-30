package com.duoc.atencionesmedicas.receta.client;

import com.duoc.atencionesmedicas.receta.dto.DiagnosticoDetalleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "diagnostico-service")
public interface DiagnosticoClient {

    @GetMapping("/api/diagnosticos/{id}/detalle")
    DiagnosticoDetalleDTO obtenerDiagnosticoPorId(@PathVariable Integer id);
}