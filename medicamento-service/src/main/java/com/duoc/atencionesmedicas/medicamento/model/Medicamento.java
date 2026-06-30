package com.duoc.atencionesmedicas.medicamento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medicamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medicamento")
    private Integer idMedicamento;

    @Column(name = "nombre_medicamento", nullable = false)
    private String nombreMedicamento;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String laboratorio;

    @Column(nullable = false)
    private Integer stock;
}