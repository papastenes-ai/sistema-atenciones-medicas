package com.duoc.atencionesmedicas.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendaResponseDTO {

    private Integer idAgenda;
    private String fecha;
    private String hora;
    private String estado;
    private Integer pacienteId;
    private Integer medicoId;
}