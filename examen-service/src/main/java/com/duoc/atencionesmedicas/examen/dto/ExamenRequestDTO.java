package com.duoc.atencionesmedicas.examen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamenRequestDTO {

    @NotBlank(message = "El nombre del examen es obligatorio")
    private String nombreExamen;

    @NotBlank(message = "El resultado del examen es obligatorio")
    private String resultado;

    @NotBlank(message = "La fecha del examen es obligatoria")
    private String fechaExamen;

    @NotNull(message = "El id de atención es obligatorio")
    private Integer atencionId;
}