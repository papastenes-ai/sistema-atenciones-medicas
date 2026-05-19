package com.duoc.atencionesmedicas.diagnostico.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.duoc.atencionesmedicas.diagnostico.model.Diagnostico;
import com.duoc.atencionesmedicas.diagnostico.repository.DiagnosticoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final DiagnosticoRepository diagnosticoRepository;

    @Override
    public void run(String... args) throws Exception {

        if (diagnosticoRepository.count() > 0) {
            log.info(">>>Los datos de diagnósticos ya existen.");
            return;
        }

        Diagnostico diagnostico1 = new Diagnostico(
                null,
                "Dolor torácico leve asociado a estrés",
                "Reposo, exámenes preventivos y control cardiológico",
                "2025-11-20",
                1
        );

        Diagnostico diagnostico2 = new Diagnostico(
                null,
                "Paciente pediátrico en buen estado general",
                "Control anual y seguimiento de crecimiento",
                "2025-11-21",
                2
        );

        Diagnostico diagnostico3 = new Diagnostico(
                null,
                "Contractura lumbar por esfuerzo físico",
                "Reposo relativo y sesiones de kinesiología",
                "2025-11-22",
                3
        );

        Diagnostico diagnostico4 = new Diagnostico(
                null,
                "Dermatitis alérgica leve",
                "Uso de crema tópica y evitar alérgenos",
                "2025-11-23",
                4
        );

        diagnosticoRepository.save(diagnostico1);
        diagnosticoRepository.save(diagnostico2);
        diagnosticoRepository.save(diagnostico3);
        diagnosticoRepository.save(diagnostico4);

        log.info(">>>Datos de diagnósticos cargados correctamente.");
    }
}