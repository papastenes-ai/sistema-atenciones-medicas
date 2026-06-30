package com.duoc.atencionesmedicas.centromedico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentroMedicoResponseDTO {

    private Integer idCentro;
    private String nombre;
    private String direccion;
    private String comuna;
    private String telefono;
    private String horario;
    private String estado;
}