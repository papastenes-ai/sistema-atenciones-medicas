package com.duoc.atencionesmedicas.atencion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtencionResponseDTO {

    private Integer idAtencion;
    private String fecha;
    private String hora;
    private String motivoConsulta;
    private String observacion;
    private Integer pacienteId;
    private Integer medicoId;
}