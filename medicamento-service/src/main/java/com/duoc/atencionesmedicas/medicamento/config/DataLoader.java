package com.duoc.atencionesmedicas.medicamento.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
    public void run(String... args) throws Exception {

        if (medicamentoRepository.count() > 0) {

            log.info(">>>>Los datos de medicamentos ya existen.");
            return;
        }

        Medicamento medicamento1 = new Medicamento(
                null,
                "Paracetamol",
                "Analgésico y antipirético",
                "Caja 500mg",
                120
        );

        Medicamento medicamento2 = new Medicamento(
                null,
                "Ibuprofeno",
                "Antiinflamatorio",
                "Caja 400mg",
                80
        );

        Medicamento medicamento3 = new Medicamento(
                null,
                "Amoxicilina",
                "Antibiótico",
                "Caja 500mg",
                60
        );

        Medicamento medicamento4 = new Medicamento(
                null,
                "Cetirizina",
                "Antialérgico",
                "Caja 10mg",
                95
        );

        medicamentoRepository.save(medicamento1);
        medicamentoRepository.save(medicamento2);
        medicamentoRepository.save(medicamento3);
        medicamentoRepository.save(medicamento4);

        log.info("D>>>atos de medicamentos cargados correctamente.");
    }
}