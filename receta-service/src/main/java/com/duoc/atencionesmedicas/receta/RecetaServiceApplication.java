package com.duoc.atencionesmedicas.receta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RecetaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecetaServiceApplication.class, args);
    }
	
}