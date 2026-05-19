package com.duoc.atencionesmedicas.examen.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.duoc.atencionesmedicas.examen.model.Examen;
import com.duoc.atencionesmedicas.examen.repository.ExamenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ExamenRepository examenRepository;

    @Override
    public void run(String... args) throws Exception {

        if (examenRepository.count() > 0) {

            log.info(">>>>Los datos de exámenes ya existen.");
            return;
        }

        Examen examen1 = new Examen(
                null,
                "Hemograma",
                "Valores normales",
                "2025-11-20",
                1
        );

        Examen examen2 = new Examen(
                null,
                "Radiografía lumbar",
                "Leve inflamación muscular",
                "2025-11-21",
                2
        );

        Examen examen3 = new Examen(
                null,
                "Perfil lipídico",
                "Colesterol elevado",
                "2025-11-22",
                3
        );

        Examen examen4 = new Examen(
                null,
                "Test alergológico",
                "Positiva reacción al polen",
                "2025-11-23",
                4
        );

        examenRepository.save(examen1);
        examenRepository.save(examen2);
        examenRepository.save(examen3);
        examenRepository.save(examen4);

        log.info(">>>Datos de exámenes cargados correctamente.");
    }
}