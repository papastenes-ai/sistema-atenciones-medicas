package com.duoc.atencionesmedicas.agenda.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.atencionesmedicas.agenda.model.Agenda;
import com.duoc.atencionesmedicas.agenda.repository.AgendaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AgendaRepository agendaRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (agendaRepository.count() > 0) {
            log.info(">>> Los datos de agenda ya existen.");
            return;
        }

        List<Agenda> agendas = List.of(
                new Agenda(null, "2025-11-25", "09:00", "PROGRAMADA", 1, 1),
                new Agenda(null, "2025-11-25", "10:30", "PROGRAMADA", 2, 2),
                new Agenda(null, "2025-11-26", "12:00", "CONFIRMADA", 3, 3),
                new Agenda(null, "2025-11-27", "15:30", "CANCELADA", 4, 4)
        );

        agendaRepository.saveAll(agendas);

        log.info(">>> Datos de agenda cargados correctamente.");
    }
}