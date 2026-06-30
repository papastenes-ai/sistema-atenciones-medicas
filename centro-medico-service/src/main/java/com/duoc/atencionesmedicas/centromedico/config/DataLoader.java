package com.duoc.atencionesmedicas.centromedico.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void run(String... args) throws Exception {

        if (centroMedicoRepository.count() > 0) {
            log.info(">>> Los datos de centros médicos ya existen.");
            return;
        }

        List<CentroMedico> centros = List.of(
                new CentroMedico(
                        null,
                        "Centro Médico Alameda",
                        "Av. Libertador Bernardo O'Higgins 1234",
                        "Santiago",
                        "222345678",
                        "08:00 - 18:00",
                        "ACTIVO"
                ),
                new CentroMedico(
                        null,
                        "Centro Médico Maipú",
                        "Av. Pajaritos 456",
                        "Maipú",
                        "229876543",
                        "09:00 - 19:00",
                        "ACTIVO"
                ),
                new CentroMedico(
                        null,
                        "Centro Médico Providencia",
                        "Av. Providencia 789",
                        "Providencia",
                        "223334444",
                        "08:30 - 17:30",
                        "ACTIVO"
                ),
                new CentroMedico(
                        null,
                        "Centro Médico La Florida",
                        "Vicuña Mackenna 321",
                        "La Florida",
                        "226667777",
                        "10:00 - 20:00",
                        "INACTIVO"
                )
        );

        centroMedicoRepository.saveAll(centros);

        log.info(">>> Datos de centros médicos cargados correctamente.");
    }
}