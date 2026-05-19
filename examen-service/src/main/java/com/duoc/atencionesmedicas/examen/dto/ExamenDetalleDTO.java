package com.duoc.atencionesmedicas.examen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamenDetalleDTO {

    private Integer idExamen;
    private String nombreExamen;
    private String resultado;
    private String fechaExamen;

    private AtencionDetalleDTO atencion;
}