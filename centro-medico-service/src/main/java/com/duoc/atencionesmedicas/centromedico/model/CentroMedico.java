package com.duoc.atencionesmedicas.centromedico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "centros_medicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentroMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCentro;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false)
    private String direccion;

    @NotBlank(message = "La comuna es obligatoria")
    @Column(nullable = false)
    private String comuna;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(nullable = false)
    private String telefono;

    @NotBlank(message = "El horario es obligatorio")
    @Column(nullable = false)
    private String horario;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false)
    private String estado;
}