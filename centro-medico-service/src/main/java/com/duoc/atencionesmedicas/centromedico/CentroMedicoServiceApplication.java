package com.duoc.atencionesmedicas.centromedico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



@EnableDiscoveryClient
@SpringBootApplication
public class CentroMedicoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CentroMedicoServiceApplication.class, args);
    }
}