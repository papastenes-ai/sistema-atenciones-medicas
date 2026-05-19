package com.duoc.atencionesmedicas.diagnostico.dto;

import lombok.Data;

@Data
public class MedicoDTO {

    private Integer idMedico;
    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
}