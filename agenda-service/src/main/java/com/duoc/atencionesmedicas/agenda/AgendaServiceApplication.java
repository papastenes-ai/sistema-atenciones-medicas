package com.duoc.atencionesmedicas.agenda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AgendaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgendaServiceApplication.class, args);
    }
}