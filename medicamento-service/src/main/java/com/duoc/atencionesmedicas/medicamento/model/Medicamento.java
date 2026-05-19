package com.duoc.atencionesmedicas.medicamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Integer idMedicamento;

    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @NotBlank(message = "La presentación es obligatoria")
    @Column(nullable = false)
    private String presentacion;

    @NotNull(message = "El stock es obligatorio")
    @Column(nullable = false)
    private Integer stock;
}