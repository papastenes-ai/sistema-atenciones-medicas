package com.duoc.atencionesmedicas.paciente.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.atencionesmedicas.paciente.model.Contacto;
import com.duoc.atencionesmedicas.paciente.model.Direccion;
import com.duoc.atencionesmedicas.paciente.model.Paciente;
import com.duoc.atencionesmedicas.paciente.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PacienteRepository pacienteRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (pacienteRepository.count() > 0) {
            log.info(">>> Los datos de pacientes ya existen.");
            return;
        }

        Paciente paciente1 = new Paciente(null, "Carlos", "Muñoz", "12345678-9", 34, "carlos@correo.cl", "912345678");
        Paciente paciente2 = new Paciente(null, "Fernanda", "Rojas", "98765432-1", 27, "fernanda@correo.cl", "987654321");
        Paciente paciente3 = new Paciente(null, "Daniela", "Castillo", "15478963-2", 41, "daniela@correo.cl", "956789123");
        Paciente paciente4 = new Paciente(null, "Matías", "Herrera", "20123456-7", 22, "matias@correo.cl", "923456789");

        Contacto contacto1 = new Contacto(null, "912345678", "carlos@correo.cl", paciente1);
        Contacto contacto2 = new Contacto(null, "987654321", "fernanda@correo.cl", paciente2);
        Contacto contacto3 = new Contacto(null, "956789123", "daniela@correo.cl", paciente3);
        Contacto contacto4 = new Contacto(null, "923456789", "matias@correo.cl", paciente4);

        Direccion direccion1 = new Direccion(null, "Los Carrera", "123", "Maipú", "Santiago", paciente1);
        Direccion direccion2 = new Direccion(null, "San Martín", "456", "Providencia", "Santiago", paciente2);
        Direccion direccion3 = new Direccion(null, "O'Higgins", "789", "La Florida", "Santiago", paciente3);
        Direccion direccion4 = new Direccion(null, "Brasil", "321", "Ñuñoa", "Santiago", paciente4);

        paciente1.setContacto(contacto1);
        paciente2.setContacto(contacto2);
        paciente3.setContacto(contacto3);
        paciente4.setContacto(contacto4);

        paciente1.setDireccion(direccion1);
        paciente2.setDireccion(direccion2);
        paciente3.setDireccion(direccion3);
        paciente4.setDireccion(direccion4);

        pacienteRepository.saveAll(List.of(paciente1, paciente2, paciente3, paciente4));

        log.info(">>> Datos de pacientes cargados correctamente.");
    }
}