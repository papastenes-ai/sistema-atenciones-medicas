package com.duoc.atencionesmedicas.receta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recetas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReceta;

    @NotBlank(message = "El medicamento es obligatorio")
    @Column(nullable = false)
    private String medicamento;

    @NotBlank(message = "La dosis es obligatoria")
    @Column(nullable = false)
    private String dosis;

    @Column(nullable = false)
    private String indicaciones;

    @Column(nullable = false)
    private String fechaReceta;

    @NotNull(message = "El id del diagnóstico es obligatorio")
    @Column(nullable = false)
    private Integer diagnosticoId;
}