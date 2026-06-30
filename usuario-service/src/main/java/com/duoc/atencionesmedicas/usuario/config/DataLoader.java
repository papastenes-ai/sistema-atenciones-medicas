package com.duoc.atencionesmedicas.usuario.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.atencionesmedicas.usuario.model.Usuario;
import com.duoc.atencionesmedicas.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (usuarioRepository.count() > 0) {
            log.info(">>> Los datos de usuarios ya existen.");
            return;
        }

        List<Usuario> usuarios = List.of(
                new Usuario(
                        null,
                        "admin",
                        "admin123",
                        "admin@clinica.cl",
                        "ADMIN",
                        "ACTIVO"
                ),
                new Usuario(
                        null,
                        "recepcion",
                        "recepcion123",
                        "recepcion@clinica.cl",
                        "RECEPCIONISTA",
                        "ACTIVO"
                ),
                new Usuario(
                        null,
                        "medico",
                        "medico123",
                        "medico@clinica.cl",
                        "MEDICO",
                        "ACTIVO"
                ),
                new Usuario(
                        null,
                        "auditor",
                        "auditor123",
                        "auditor@clinica.cl",
                        "AUDITOR",
                        "INACTIVO"
                )
        );

        usuarioRepository.saveAll(usuarios);

        log.info(">>> Datos de usuarios cargados correctamente.");
    }
}