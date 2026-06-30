package com.duoc.atencionesmedicas.medicamento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoResponseDTO {

    private Integer idMedicamento;
    private String nombreMedicamento;
    private String descripcion;
    private String laboratorio;
    private Integer stock;
}