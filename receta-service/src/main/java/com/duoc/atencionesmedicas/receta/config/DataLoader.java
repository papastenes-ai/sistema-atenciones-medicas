package com.duoc.atencionesmedicas.receta.config;

import java.util.List;

import com.duoc.atencionesmedicas.receta.model.Receta;
import com.duoc.atencionesmedicas.receta.repository.RecetaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RecetaRepository recetaRepository;

    @Override
    @Transactional
    public void run(String... args) {

        if (recetaRepository.count() > 0) {
            log.info(">>> Los datos de recetas ya existen.");
            return;
        }

        List<Receta> recetas = List.of(
                new Receta(
                        null,
                        "Paracetamol",
                        "500mg cada 8 horas",
                        "Tomar después de las comidas",
                        "2025-11-20",
                        1
                ),
                new Receta(
                        null,
                        "Ibuprofeno",
                        "400mg cada 12 horas",
                        "No consumir con el estómago vacío",
                        "2025-11-21",
                        2
                ),
                new Receta(
                        null,
                        "Diclofenaco",
                        "50mg cada 8 horas",
                        "Uso por 5 días",
                        "2025-11-22",
                        3
                ),
                new Receta(
                        null,
                        "Cetirizina",
                        "10mg diaria",
                        "Tomar preferentemente de noche",
                        "2025-11-23",
                        4
                )
        );

        recetaRepository.saveAll(recetas);

        log.info(">>> Datos de recetas cargados correctamente.");
    }
}