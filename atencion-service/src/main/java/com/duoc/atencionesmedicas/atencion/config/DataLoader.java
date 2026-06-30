package com.duoc.atencionesmedicas.atencion.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void run(String... args) throws Exception {

        if (atencionRepository.count() > 0) {
            log.info(">>> Los datos de atenciones ya existen.");
            return;
        }

        List<Atencion> atenciones = List.of(
                new Atencion(
                        null,
                        "2025-11-20",
                        "09:30",
                        "Dolor torácico",
                        "Paciente presenta molestias leves",
                        1,
                        1
                ),
                new Atencion(
                        null,
                        "2025-11-21",
                        "10:15",
                        "Control pediátrico",
                        "Control anual sin complicaciones",
                        2,
                        2
                ),
                new Atencion(
                        null,
                        "2025-11-22",
                        "11:45",
                        "Dolor lumbar",
                        "Paciente refiere dolor por esfuerzo físico",
                        3,
                        3
                ),
                new Atencion(
                        null,
                        "2025-11-23",
                        "15:00",
                        "Alergia en la piel",
                        "Posible dermatitis alérgica",
                        4,
                        4
                )
        );

        atencionRepository.saveAll(atenciones);

        log.info(">>> Datos de atenciones cargados correctamente.");
    }
}