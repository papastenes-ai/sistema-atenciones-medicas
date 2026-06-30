package com.duoc.atencionesmedicas.diagnostico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticoRequestDTO {

    @NotBlank(message = "La descripción del diagnóstico es obligatoria")
    private String descripcion;

    @NotBlank(message = "El tratamiento es obligatorio")
    private String tratamiento;

    @NotBlank(message = "La fecha del diagnóstico es obligatoria")
    private String fechaDiagnostico;

    @NotNull(message = "El id de atención es obligatorio")
    private Integer atencionId;
}