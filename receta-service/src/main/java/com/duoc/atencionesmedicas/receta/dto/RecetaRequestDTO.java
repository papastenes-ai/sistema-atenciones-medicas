package com.duoc.atencionesmedicas.receta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaRequestDTO {

    @NotBlank(message = "El medicamento es obligatorio")
    private String medicamento;

    @NotBlank(message = "La dosis es obligatoria")
    private String dosis;

    @NotBlank(message = "Las indicaciones son obligatorias")
    private String indicaciones;

    @NotBlank(message = "La fecha de la receta es obligatoria")
    private String fechaReceta;

    @NotNull(message = "El id del diagnóstico es obligatorio")
    private Integer diagnosticoId;
}