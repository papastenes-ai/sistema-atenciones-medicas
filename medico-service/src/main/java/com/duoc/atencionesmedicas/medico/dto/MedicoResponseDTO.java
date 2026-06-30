package com.duoc.atencionesmedicas.medico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponseDTO {

    private Integer idMedico;
    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private Integer especialidadId;
    private String especialidadNombre;
}