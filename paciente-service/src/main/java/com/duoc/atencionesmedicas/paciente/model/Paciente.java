package com.duoc.atencionesmedicas.paciente.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPaciente;

    @NotBlank(message = "El rut es obligatorio")
    @Column(nullable = false, unique = true)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private Integer edad;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL)
    private Contacto contacto;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL)
    private Direccion direccion;
}