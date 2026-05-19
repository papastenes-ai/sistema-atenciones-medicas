package com.duoc.atencionesmedicas.paciente.repository;

import com.duoc.atencionesmedicas.paciente.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
}