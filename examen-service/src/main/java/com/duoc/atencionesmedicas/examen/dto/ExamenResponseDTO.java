package com.duoc.atencionesmedicas.examen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamenResponseDTO {

    private Integer idExamen;
    private String nombreExamen;
    private String resultado;
    private String fechaExamen;
    private Integer atencionId;
}