package com.duoc.atencionesmedicas.paciente.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contactos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idContacto;

    @Column(nullable = false)
    private String telefono;

    @Email(message = "Correo inválido")
    @Column(nullable = false)
    private String correo;

    @OneToOne
    @JoinColumn(name = "id_paciente")
    @JsonIgnore
    private Paciente paciente;
}