package com.duoc.atencionesmedicas.receta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaResponseDTO {

    private Integer idReceta;
    private String medicamento;
    private String dosis;
    private String indicaciones;
    private String fechaReceta;
    private Integer diagnosticoId;
}