package com.duoc.atencionesmedicas.examen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExamenServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                ExamenServiceApplication.class, args);
    }
}