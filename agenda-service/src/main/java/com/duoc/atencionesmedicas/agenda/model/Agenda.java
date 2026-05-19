package com.duoc.atencionesmedicas.agenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "agendas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAgenda;

    @Column(nullable = false)
    private String fecha;

    @Column(nullable = false)
    private String hora;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "El id del paciente es obligatorio")
    @Column(nullable = false)
    private Integer pacienteId;

    @NotNull(message = "El id del médico es obligatorio")
    @Column(nullable = false)
    private Integer medicoId;
}