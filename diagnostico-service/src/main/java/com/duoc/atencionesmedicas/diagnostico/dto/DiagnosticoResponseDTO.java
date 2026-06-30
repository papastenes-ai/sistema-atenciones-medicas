package com.duoc.atencionesmedicas.diagnostico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticoResponseDTO {

    private Integer idDiagnostico;
    private String descripcion;
    private String tratamiento;
    private String fechaDiagnostico;
    private Integer atencionId;
}