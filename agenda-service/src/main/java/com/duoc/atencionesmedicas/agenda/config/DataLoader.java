package com.duoc.atencionesmedicas.agenda.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
    public void run(String... args) throws Exception {

        if (agendaRepository.count() > 0) {
            log.info(">>>Los datos de agenda ya existen.");
            return;
        }

        Agenda agenda1 = new Agenda(null, "2025-11-25", "09:00", "PROGRAMADA", 1, 1);
        Agenda agenda2 = new Agenda(null, "2025-11-25", "10:30", "PROGRAMADA", 2, 2);
        Agenda agenda3 = new Agenda(null, "2025-11-26", "12:00", "CONFIRMADA", 3, 3);
        Agenda agenda4 = new Agenda(null, "2025-11-27", "15:30", "CANCELADA", 4, 4);

        agendaRepository.save(agenda1);
        agendaRepository.save(agenda2);
        agendaRepository.save(agenda3);
        agendaRepository.save(agenda4);

        log.info(">>>Datos de agenda cargados correctamente.");
    }
}