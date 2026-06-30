package com.duoc.atencionesmedicas.diagnostico.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void run(String... args) throws Exception {

        if (diagnosticoRepository.count() > 0) {
            log.info(">>> Los datos de diagnósticos ya existen.");
            return;
        }

        List<Diagnostico> diagnosticos = List.of(
                new Diagnostico(
                        null,
                        "Dolor torácico leve asociado a estrés",
                        "Reposo, exámenes preventivos y control cardiológico",
                        "2025-11-20",
                        1
                ),
                new Diagnostico(
                        null,
                        "Paciente pediátrico en buen estado general",
                        "Control anual y seguimiento de crecimiento",
                        "2025-11-21",
                        2
                ),
                new Diagnostico(
                        null,
                        "Contractura lumbar por esfuerzo físico",
                        "Reposo relativo y sesiones de kinesiología",
                        "2025-11-22",
                        3
                ),
                new Diagnostico(
                        null,
                        "Dermatitis alérgica leve",
                        "Uso de crema tópica y evitar alérgenos",
                        "2025-11-23",
                        4
                )
        );

        diagnosticoRepository.saveAll(diagnosticos);

        log.info(">>> Datos de diagnósticos cargados correctamente.");
    }
}