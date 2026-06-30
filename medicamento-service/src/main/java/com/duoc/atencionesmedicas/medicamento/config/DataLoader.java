package com.duoc.atencionesmedicas.medicamento.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.atencionesmedicas.medicamento.model.Medicamento;
import com.duoc.atencionesmedicas.medicamento.repository.MedicamentoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final MedicamentoRepository medicamentoRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (medicamentoRepository.count() > 0) {
            log.info(">>> Los datos de medicamentos ya existen.");
            return;
        }

        List<Medicamento> medicamentos = List.of(
                new Medicamento(
                        null,
                        "Paracetamol",
                        "Analgésico y antipirético",
                        "Caja 500mg",
                        120
                ),
                new Medicamento(
                        null,
                        "Ibuprofeno",
                        "Antiinflamatorio",
                        "Caja 400mg",
                        80
                ),
                new Medicamento(
                        null,
                        "Amoxicilina",
                        "Antibiótico",
                        "Caja 500mg",
                        60
                ),
                new Medicamento(
                        null,
                        "Cetirizina",
                        "Antialérgico",
                        "Caja 10mg",
                        95
                )
        );

        medicamentoRepository.saveAll(medicamentos);

        log.info(">>> Datos de medicamentos cargados correctamente.");
    }
}