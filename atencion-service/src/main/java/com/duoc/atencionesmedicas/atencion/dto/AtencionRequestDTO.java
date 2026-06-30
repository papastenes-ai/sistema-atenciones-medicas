package com.duoc.atencionesmedicas.atencion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtencionRequestDTO {

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    @NotBlank(message = "La hora es obligatoria")
    private String hora;

    @NotBlank(message = "El motivo de consulta es obligatorio")
    private String motivoConsulta;

    private String observacion;

    @NotNull(message = "El id del paciente es obligatorio")
    private Integer pacienteId;

    @NotNull(message = "El id del médico es obligatorio")
    private Integer medicoId;
}