package com.duoc.atencionesmedicas.examen.dto;

import lombok.Data;

@Data
public class AtencionDetalleDTO {

    private Integer idAtencion;
    private String fecha;
    private String hora;
    private String motivoConsulta;
    private String observacion;
    private PacienteDTO paciente;
    private MedicoDTO medico;
}