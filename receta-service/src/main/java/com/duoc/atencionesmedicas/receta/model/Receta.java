package com.duoc.atencionesmedicas.receta.model;

import jakarta.persistence.*;
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
    @Column(name = "id_receta")
    private Integer idReceta;

    @Column(nullable = false)
    private String medicamento;

    @Column(nullable = false)
    private String dosis;

    @Column(nullable = false)
    private String indicaciones;

    @Column(name = "fecha_receta", nullable = false)
    private String fechaReceta;

    @Column(name = "diagnostico_id", nullable = false)
    private Integer diagnosticoId;
}