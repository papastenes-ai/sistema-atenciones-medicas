package com.duoc.atencionesmedicas.agenda.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaRequestDTO {

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    @NotBlank(message = "La hora es obligatoria")
    private String hora;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "El id del paciente es obligatorio")
    private Integer pacienteId;

    @NotNull(message = "El id del médico es obligatorio")
    private Integer medicoId;
}