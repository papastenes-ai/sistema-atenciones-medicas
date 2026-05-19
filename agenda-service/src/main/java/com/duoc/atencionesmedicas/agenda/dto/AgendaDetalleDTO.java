package com.duoc.atencionesmedicas.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaDetalleDTO {

    private Integer idAgenda;
    private String fecha;
    private String hora;
    private String estado;

    private PacienteDTO paciente;
    private MedicoDTO medico;
}