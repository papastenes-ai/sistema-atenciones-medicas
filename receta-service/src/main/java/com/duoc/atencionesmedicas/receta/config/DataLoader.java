package com.duoc.atencionesmedicas.receta.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.duoc.atencionesmedicas.receta.model.Receta;
import com.duoc.atencionesmedicas.receta.repository.RecetaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RecetaRepository recetaRepository;

    @Override
    public void run(String... args) throws Exception {

        if (recetaRepository.count() > 0) {
                log.info(">>>Los datos de recetas ya existen.");
                        return;
        }

        Receta receta1 = new Receta(
                null,
                "Paracetamol",
                "500mg cada 8 horas",
                "Tomar después de las comidas",
                "2025-11-20",
                1
        );

        Receta receta2 = new Receta(
                null,
                "Ibuprofeno",
                "400mg cada 12 horas",
                "No consumir con el estómago vacío",
                "2025-11-21",
                2
        );

        Receta receta3 = new Receta(
                null,
                "Diclofenaco",
                "50mg cada 8 horas",
                "Uso por 5 días",
                "2025-11-22",
                3
        );

        Receta receta4 = new Receta(
                null,
                "Cetirizina",
                "10mg diaria",
                "Tomar preferentemente de noche",
                "2025-11-23",
                4
        );

        recetaRepository.save(receta1);
        recetaRepository.save(receta2);
        recetaRepository.save(receta3);
        recetaRepository.save(receta4);

        log.info(">>>Datos de recetas cargados correctamente.");
        }
}