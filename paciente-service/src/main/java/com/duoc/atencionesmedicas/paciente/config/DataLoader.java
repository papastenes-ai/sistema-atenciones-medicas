package com.duoc.atencionesmedicas.paciente.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info(">>> DataLoader desactivado para evitar carga inicial automática en Render.");
    }
}