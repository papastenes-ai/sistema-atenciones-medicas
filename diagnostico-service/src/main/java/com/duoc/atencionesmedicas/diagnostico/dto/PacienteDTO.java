package com.duoc.atencionesmedicas.diagnostico.dto;

import lombok.Data;

@Data
public class PacienteDTO {

    private Integer idPaciente;
    private String rut;
    private String nombre;
    private String apellido;
    private Integer edad;
}