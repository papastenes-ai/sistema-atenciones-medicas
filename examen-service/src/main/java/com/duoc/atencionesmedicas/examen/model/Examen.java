package com.duoc.atencionesmedicas.examen.model;

import jakarta.persistence.*;
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
    @Column(name = "id_examen")
    private Integer idExamen;

    @Column(name = "nombre_examen", nullable = false)
    private String nombreExamen;

    @Column(nullable = false)
    private String resultado;

    @Column(name = "fecha_examen", nullable = false)
    private String fechaExamen;

    @Column(name = "atencion_id", nullable = false)
    private Integer atencionId;
}