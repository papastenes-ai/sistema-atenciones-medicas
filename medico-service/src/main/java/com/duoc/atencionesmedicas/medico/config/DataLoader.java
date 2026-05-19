package com.duoc.atencionesmedicas.medico.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.duoc.atencionesmedicas.medico.model.Especialidad;
import com.duoc.atencionesmedicas.medico.model.Medico;
import com.duoc.atencionesmedicas.medico.repository.EspecialidadRepository;
import com.duoc.atencionesmedicas.medico.repository.MedicoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;

    @Override
    public void run(String... args) throws Exception {

        if (medicoRepository.count() > 0) {
            log.info(">>>Los datos de médicos ya existen.");
            return;
        }

        Especialidad especialidad1 =
        new Especialidad(null, "Cardiología", "Especialidad enfocada en enfermedades del corazón");

        Especialidad especialidad2 =
        new Especialidad(null, "Pediatría", "Atención médica infantil");

        Especialidad especialidad3 =
        new Especialidad(null, "Traumatología", "Tratamiento de lesiones y fracturas");

        Especialidad especialidad4 =
        new Especialidad(null, "Dermatología", "Tratamiento de enfermedades de la piel");

        



        especialidadRepository.save(especialidad1);
        especialidadRepository.save(especialidad2);
        especialidadRepository.save(especialidad3);
        especialidadRepository.save(especialidad4);

        Medico medico1 = new Medico(
                null,
                "11111111-1",
                "Ricardo",
                "Pérez",
                "ricardo.perez@clinica.cl",
                "912345678",
                especialidad1
        );

        Medico medico2 = new Medico(
                null,
                "22222222-2",
                "Carolina",
                "Muñoz",
                "carolina.munoz@clinica.cl",
                "923456789",
                especialidad2
        );

        Medico medico3 = new Medico(
                null,
                "33333333-3",
                "Felipe",
                "Rojas",
                "felipe.rojas@clinica.cl",
                "934567891",
                especialidad3
        );

        Medico medico4 = new Medico(
                null,
                "44444444-4",
                "Valentina",
                "Silva",
                "valentina.silva@clinica.cl",
                "945678912",
                especialidad4
        );

        medicoRepository.save(medico1);
        medicoRepository.save(medico2);
        medicoRepository.save(medico3);
        medicoRepository.save(medico4);

        log.info(">>>Datos de médicos cargados correctamente.");
    }

}