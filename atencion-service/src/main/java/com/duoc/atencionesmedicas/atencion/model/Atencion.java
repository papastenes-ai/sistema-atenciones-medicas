package com.duoc.atencionesmedicas.atencion.model;

import jakarta.persistence.*;
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
    @Column(name = "id_atencion")
    private Integer idAtencion;

    @Column(nullable = false)
    private String fecha;

    @Column(nullable = false)
    private String hora;

    @Column(name = "motivo_consulta", nullable = false)
    private String motivoConsulta;

    private String observacion;

    @Column(name = "paciente_id", nullable = false)
    private Integer pacienteId;

    @Column(name = "medico_id", nullable = false)
    private Integer medicoId;
}