package com.duoc.atencionesmedicas.atencion;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients
public class AtencionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtencionServiceApplication.class, args);
    }
}