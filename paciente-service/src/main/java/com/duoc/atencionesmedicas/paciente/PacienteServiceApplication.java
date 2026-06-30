package com.duoc.atencionesmedicas.paciente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PacienteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PacienteServiceApplication.class, args);
    }
}