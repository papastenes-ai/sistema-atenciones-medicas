package com.duoc.atencionesmedicas.examen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "examenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExamen;

    @NotBlank(message = "El nombre del examen es obligatorio")
    @Column(nullable = false)
    private String nombreExamen;

    @Column(nullable = false)
    private String resultado;

    @Column(nullable = false)
    private String fechaExamen;

    @NotNull(message = "El id de atención es obligatorio")
    @Column(nullable = false)
    private Integer atencionId;
}