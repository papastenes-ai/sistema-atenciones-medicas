package com.duoc.atencionesmedicas.receta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecetaDetalleDTO {

    private Integer idReceta;
    private String medicamento;
    private String dosis;
    private String indicaciones;
    private String fechaReceta;

    private DiagnosticoDetalleDTO diagnostico;
}