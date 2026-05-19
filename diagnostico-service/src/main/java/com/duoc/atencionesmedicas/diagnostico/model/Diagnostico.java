package com.duoc.atencionesmedicas.diagnostico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diagnosticos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDiagnostico;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String tratamiento;

    @Column(nullable = false)
    private String fechaDiagnostico;

    @NotNull(message = "El id de atención es obligatorio")
    @Column(nullable = false)
    private Integer atencionId;
}