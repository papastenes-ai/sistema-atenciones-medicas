        package com.duoc.atencionesmedicas.usuario.config;
        import org.springframework.boot.CommandLineRunner;
        import org.springframework.stereotype.Component;

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
        public void run(String... args) throws Exception {

                if (usuarioRepository.count() > 0) {
                log.info(">>>Los datos de usuarios ya existen.");
                return;
                }

                Usuario usuario1 = new Usuario(
                        null,
                        "admin",
                        "admin123",
                        "admin@clinica.cl",
                        "ADMIN",
                        "ACTIVO"
                );

                Usuario usuario2 = new Usuario(
                        null,
                        "recepcion",
                        "recepcion123",
                        "recepcion@clinica.cl",
                        "RECEPCIONISTA",
                        "ACTIVO"
                );

                Usuario usuario3 = new Usuario(
                        null,
                        "medico",
                        "medico123",
                        "medico@clinica.cl",
                        "MEDICO",
                        "ACTIVO"
                );

                Usuario usuario4 = new Usuario(
                        null,
                        "auditor",
                        "auditor123",
                        "auditor@clinica.cl",
                        "AUDITOR",
                        "INACTIVO"
                );

                usuarioRepository.save(usuario1);
                usuarioRepository.save(usuario2);
                usuarioRepository.save(usuario3);
                usuarioRepository.save(usuario4);

                log.info(">>>Datos de usuarios cargados correctamente.");
        }
        }