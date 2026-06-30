package com.duoc.atencionesmedicas.medicamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class MedicamentoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                MedicamentoServiceApplication.class, args);
    }
}