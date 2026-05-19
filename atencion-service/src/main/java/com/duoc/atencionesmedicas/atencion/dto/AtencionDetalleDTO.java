package com.duoc.atencionesmedicas.atencion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtencionDetalleDTO {

    private Integer idAtencion;
    private String fecha;
    private String hora;
    private String motivoConsulta;
    private String observacion;

    private PacienteDTO paciente;
    private MedicoDTO medico;
}