package com.duoc.atencionesmedicas.diagnostico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosticoDetalleDTO {

    private Integer idDiagnostico;
    private String descripcion;
    private String tratamiento;
    private String fechaDiagnostico;

    private AtencionDetalleDTO atencion;
}