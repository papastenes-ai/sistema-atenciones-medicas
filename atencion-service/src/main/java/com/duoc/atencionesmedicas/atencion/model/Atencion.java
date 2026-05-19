package com.duoc.atencionesmedicas.atencion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "atenciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAtencion;

    @Column(nullable = false)
    private String fecha;

    @Column(nullable = false)
    private String hora;

    @NotBlank(message = "El motivo de consulta es obligatorio")
    @Column(nullable = false)
    private String motivoConsulta;

    private String observacion;

    @NotNull(message = "El id del paciente es obligatorio")
    @Column(nullable = false)
    private Integer pacienteId;

    @NotNull(message = "El id del médico es obligatorio")
    @Column(nullable = false)
    private Integer medicoId;
}