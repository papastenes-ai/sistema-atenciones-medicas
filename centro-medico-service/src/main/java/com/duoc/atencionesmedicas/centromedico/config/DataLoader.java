package com.duoc.atencionesmedicas.centromedico.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.duoc.atencionesmedicas.centromedico.model.CentroMedico;
import com.duoc.atencionesmedicas.centromedico.repository.CentroMedicoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CentroMedicoRepository centroMedicoRepository;

    @Override
    public void run(String... args) throws Exception {

        if (centroMedicoRepository.count() > 0) {
            log.info(">>Los datos de centros médicos ya existen.");
            return;
        }

        CentroMedico centro1 = new CentroMedico(
                null,
                "Centro Médico Alameda",
                "Av. Libertador Bernardo O'Higgins 1234",
                "Santiago",
                "222345678",
                "08:00 - 18:00",
                "ACTIVO"
        );

        CentroMedico centro2 = new CentroMedico(
                null,
                "Centro Médico Maipú",
                "Av. Pajaritos 456",
                "Maipú",
                "229876543",
                "09:00 - 19:00",
                "ACTIVO"
        );

        CentroMedico centro3 = new CentroMedico(
                null,
                "Centro Médico Providencia",
                "Av. Providencia 789",
                "Providencia",
                "223334444",
                "08:30 - 17:30",
                "ACTIVO"
        );

        CentroMedico centro4 = new CentroMedico(
                null,
                "Centro Médico La Florida",
                "Vicuña Mackenna 321",
                "La Florida",
                "226667777",
                "10:00 - 20:00",
                "INACTIVO"
        );

        centroMedicoRepository.save(centro1);
        centroMedicoRepository.save(centro2);
        centroMedicoRepository.save(centro3);
        centroMedicoRepository.save(centro4);

        log.info(">>Datos de centros médicos cargados correctamente.");
    }
}