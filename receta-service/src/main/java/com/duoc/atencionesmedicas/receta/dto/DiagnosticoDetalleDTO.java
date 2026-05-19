package com.duoc.atencionesmedicas.receta.dto;

import lombok.Data;

@Data
public class DiagnosticoDetalleDTO {

    private Integer idDiagnostico;
    private String descripcion;
    private String tratamiento;
    private String fechaDiagnostico;

    private AtencionDetalleDTO atencion;
}
