package com.duoc.atencionesmedicas.paciente.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponseDTO {

    private Integer idPaciente;
    private String nombre;
    private String apellido;
    private String rut;
    private Integer edad;
    private String correo;
    private String telefono;
}