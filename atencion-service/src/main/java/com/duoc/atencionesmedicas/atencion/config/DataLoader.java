package com.duoc.atencionesmedicas.atencion.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.duoc.atencionesmedicas.atencion.model.Atencion;
import com.duoc.atencionesmedicas.atencion.repository.AtencionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AtencionRepository atencionRepository;

    @Override
    public void run(String... args) throws Exception {

        if (atencionRepository.count() > 0) {
            log.info(">>>Los datos de atenciones ya existen.");
                return;
        }

        Atencion atencion1 = new Atencion(
                null,
                "2025-11-20",
                "09:30",
                "Dolor torácico",
                "Paciente presenta molestias leves",
                1,
                1
        );

        Atencion atencion2 = new Atencion(
                null,
                "2025-11-21",
                "10:15",
                "Control pediátrico",
                "Control anual sin complicaciones",
                2,
                2
        );

        Atencion atencion3 = new Atencion(
                null,
                "2025-11-22",
                "11:45",
                "Dolor lumbar",
                "Paciente refiere dolor por esfuerzo físico",
                3,
                3
        );

        Atencion atencion4 = new Atencion(
                null,
                "2025-11-23",
                "15:00",
                "Alergia en la piel",
                "Posible dermatitis alérgica",
                4,
                4
        );

        atencionRepository.save(atencion1);
        atencionRepository.save(atencion2);
        atencionRepository.save(atencion3);
        atencionRepository.save(atencion4);

        log.info(">>>Datos de atenciones cargados correctamente.");

        }
}