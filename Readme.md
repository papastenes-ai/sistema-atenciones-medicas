# Sistema de Atenciones Médicas - Arquitectura de Microservicios

## Descripción del proyecto

Sistema de gestión de atenciones médicas desarrollado bajo una arquitectura distribuida basada en microservicios.

El proyecto permite administrar pacientes, médicos, centros médicos, atenciones, diagnósticos, recetas, medicamentos, exámenes, agenda y usuarios, separando cada responsabilidad funcional en un servicio independiente.

El objetivo principal del sistema es representar un ecosistema de atención médica donde cada microservicio administra su propio dominio, su propia base de datos y se comunica con otros servicios mediante REST usando Feign Client. Además, se centraliza el acceso mediante un API Gateway y se utiliza Eureka Server para el registro y descubrimiento de servicios.

Este proyecto fue desarrollado como parte de la Evaluación Parcial 3 de la asignatura Desarrollo FullStack 1, donde se solicita mantener al menos 10 microservicios, implementar comunicación REST, pruebas unitarias, documentación Swagger/OpenAPI, API Gateway, configuración YAML y despliegue local o remoto. La pauta también solicita que el repositorio incluya README, microservicios, rutas Gateway, enlaces Swagger e instrucciones de ejecución local/remota. :contentReference[oaicite:0]{index=0}

---

## Integrantes

- Pablo Patenes


## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Spring Cloud OpenFeign
- Spring Cloud Gateway
- Eureka Server
- JUnit 5
- Mockito
- Swagger / OpenAPI
- Maven
- Docker
- Render
- Git / GitHub

---

## Arquitectura del sistema

El sistema está compuesto por microservicios independientes. Cada microservicio sigue el patrón CSR:

- Controller: recibe las solicitudes HTTP y expone los endpoints REST.
- Service: contiene la lógica de negocio, validaciones y coordinación con otros servicios.
- Repository: administra el acceso a datos mediante JPA.
- Model: representa las entidades persistidas en la base de datos.
- DTO: permite transportar datos sin exponer directamente las entidades internas.

Esta separación permite mantener una arquitectura ordenada, con responsabilidades claras y menor acoplamiento entre capas.

---

## Microservicios implementados

| Microservicio | Puerto local | Responsabilidad |
|---|---:|---|
| eureka-server | 8761 | Registro y descubrimiento de microservicios |
| api-gateway | 8080 | Punto central de entrada y enrutamiento |
| paciente-service | 8081 | Gestión de pacientes |
| medico-service | 8082 | Gestión de médicos |
| atencion-service | 8083 | Gestión de atenciones médicas |
| diagnostico-service | 8084 | Gestión de diagnósticos |
| receta-service | 8085 | Gestión de recetas |
| medicamento-service | 8086 | Gestión de medicamentos |
| examen-service | 8087 | Gestión de exámenes médicos |
| agenda-service | 8088 | Gestión de agenda médica |
| usuario-service | 8089 | Gestión de usuarios |
| centro-medico-service | 8090 | Gestión de centros médicos |

---

## Comunicación entre microservicios

La comunicación entre servicios se realiza mediante Feign Client.

Ejemplos de integración:

- `atencion-service` consulta a `paciente-service` para validar y obtener información de pacientes.
- `atencion-service` consulta a `medico-service` para validar y obtener información de médicos.
- `examen-service` consulta a `atencion-service` para validar que una atención exista antes de registrar un examen.
- Los servicios relacionados al dominio médico utilizan comunicación REST para mantener consistencia entre entidades relacionadas.

Las URL de los servicios se configuran mediante variables de entorno para permitir ejecución local y despliegue remoto.

Ejemplo:

```java
@FeignClient(
        name = "paciente-service",
        url = "${PACIENTE_SERVICE_URL:http://localhost:8081}"
)

Esto permite que localmente el servicio use localhost, pero en Render pueda consumir la URL pública del microservicio correspondiente.

API Gateway

El API Gateway centraliza el acceso a los microservicios y permite enrutar las solicitudes desde un único punto de entrada.

URL local del Gateway:

http://localhost:8080
Rutas principales del Gateway
Servicio	Ruta Gateway
paciente-service	/api/pacientes/**
medico-service	/api/medicos/**
atencion-service	/api/atenciones/**
diagnostico-service	/api/diagnosticos/**
receta-service	/api/recetas/**
medicamento-service	/api/medicamentos/**
examen-service	/api/examenes/**
agenda-service	/api/agendas/**
usuario-service	/api/usuarios/**
centro-medico-service	/api/centros-medicos/**

Ejemplo de consumo local mediante Gateway:

http://localhost:8080/api/pacientes
http://localhost:8080/api/medicos
http://localhost:8080/api/atenciones
http://localhost:8080/api/examenes
Eureka Server

Eureka Server permite registrar los microservicios y visualizar cuáles se encuentran activos.

URL local:

http://localhost:8761

Cada microservicio posee configuración para registrarse en Eureka:

eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:http://localhost:8761/eureka/}
    register-with-eureka: true
    fetch-registry: true
Swagger / OpenAPI

Cada microservicio expone documentación técnica mediante Swagger.

Formato general:

http://localhost:PUERTO/swagger-ui.html
Enlaces Swagger locales
Servicio	Swagger
paciente-service	http://localhost:8081/swagger-ui.html
medico-service	http://localhost:8082/swagger-ui.html
atencion-service	http://localhost:8083/swagger-ui.html
diagnostico-service	http://localhost:8084/swagger-ui.html
receta-service	http://localhost:8085/swagger-ui.html
medicamento-service	http://localhost:8086/swagger-ui.html
examen-service	http://localhost:8087/swagger-ui.html
agenda-service	http://localhost:8088/swagger-ui.html
usuario-service	http://localhost:8089/swagger-ui.html
centro-medico-service	http://localhost:8090/swagger-ui.html

También se puede acceder a la especificación OpenAPI con:

http://localhost:PUERTO/api-docs
Configuración YAML

Cada microservicio utiliza archivos application.yml para administrar configuración de entorno, puertos, conexión a base de datos, Eureka y Swagger.

Ejemplo de configuración:

server:
  port: ${PORT:8087}

spring:
  application:
    name: examen-service

  datasource:
    url: ${DB_URL:jdbc:mysql://localhost:3306/examen_db}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:}
    driver-class-name: com.mysql.cj.jdbc.Driver

eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:http://localhost:8761/eureka/}
    register-with-eureka: true
    fetch-registry: true

Esta configuración permite que el mismo código funcione localmente y también en un entorno remoto como Render.

Variables de entorno

Para ejecución remota se utilizan variables de entorno.

Variables generales por microservicio:

DB_URL
DB_USERNAME
DB_PASSWORD
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE

Variables adicionales según dependencias entre servicios:

PACIENTE_SERVICE_URL
MEDICO_SERVICE_URL
ATENCION_SERVICE_URL
DIAGNOSTICO_SERVICE_URL
RECETA_SERVICE_URL
MEDICAMENTO_SERVICE_URL
EXAMEN_SERVICE_URL
AGENDA_SERVICE_URL
USUARIO_SERVICE_URL
CENTRO_MEDICO_SERVICE_URL

Ejemplo para examen-service:

DB_URL=jdbc:mysql://host-remoto:3306/examen_db
DB_USERNAME=usuario
DB_PASSWORD=clave
ATENCION_SERVICE_URL=https://atencion-service.onrender.com
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=https://eureka-server.onrender.com/eureka/
Ejecución local
1. Clonar el repositorio
git clone https://github.com/papastenes-ai/sistema-atenciones-medicas.git
cd sistema-atenciones-medicas
2. Crear bases de datos MySQL

Crear una base de datos por microservicio:

CREATE DATABASE paciente_db;
CREATE DATABASE medico_db;
CREATE DATABASE atencion_db;
CREATE DATABASE diagnostico_db;
CREATE DATABASE receta_db;
CREATE DATABASE medicamento_db;
CREATE DATABASE examen_db;
CREATE DATABASE agenda_db;
CREATE DATABASE usuario_db;
CREATE DATABASE centro_medico_db;
3. Levantar Eureka Server
cd eureka-server
mvn spring-boot:run

URL:

http://localhost:8761
4. Levantar microservicios base

En terminales separadas:

cd paciente-service
mvn spring-boot:run
cd medico-service
mvn spring-boot:run
5. Levantar servicios dependientes
cd atencion-service
mvn spring-boot:run
cd examen-service
mvn spring-boot:run

Luego levantar los demás servicios según corresponda:

cd diagnostico-service
mvn spring-boot:run
cd receta-service
mvn spring-boot:run
cd medicamento-service
mvn spring-boot:run
cd agenda-service
mvn spring-boot:run
cd usuario-service
mvn spring-boot:run
cd centro-medico-service
mvn spring-boot:run
6. Levantar API Gateway
cd api-gateway
mvn spring-boot:run

URL Gateway:

http://localhost:8080
Ejecución de pruebas unitarias

Las pruebas unitarias se encuentran en:

src/test/java

Se utilizan:

JUnit 5
Mockito
Mocks de repositorios
Mocks de Feign Client
Asserts para validar resultados
Casos exitosos y casos de error

Ejecutar pruebas de un microservicio:

cd atencion-service
mvn test
cd examen-service
mvn test

En Windows PowerShell:

cd atencion-service
.\mvnw.cmd test
cd examen-service
.\mvnw.cmd test
Pruebas unitarias destacadas
atencion-service

Se valida la lógica de negocio asociada a una atención médica:

Listar atenciones.
Buscar atención por ID.
Buscar atenciones por paciente.
Buscar atenciones por médico.
Guardar atención validando paciente y médico mediante Feign.
Actualizar atención validando paciente y médico.
Eliminar atención.
Construir detalle de atención consultando paciente-service y medico-service.
Manejar errores remotos cuando paciente o médico no existen.
Evitar guardar datos inconsistentes si falla una validación remota.
examen-service

Se valida la lógica de negocio asociada a exámenes médicos:

Listar exámenes.
Buscar examen por ID.
Buscar exámenes por atención.
Buscar exámenes por nombre.
Guardar examen validando previamente que exista la atención.
Actualizar examen validando atención asociada.
Eliminar examen.
Construir detalle del examen consultando atencion-service.
Manejar errores remotos cuando la atención no existe.
Evitar guardar el examen si falla la validación con atencion-service.
Docker

Cada microservicio posee un Dockerfile para construir y ejecutar el servicio en contenedor.

Ejemplo de Dockerfile utilizado:

FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

Para construir una imagen:

cd examen-service
docker build -t examen-service .

Para ejecutar:

docker run -p 8087:8087 examen-service
Despliegue en Render

El proyecto está preparado para despliegue remoto usando Render.

La configuración para Render se basa en:

Uso de variable ${PORT} en cada microservicio.
Variables de entorno para credenciales de base de datos.
Variables de entorno para las URL de servicios remotos.
Dockerfile por microservicio.
Gateway como punto de entrada centralizado.
Eureka Server para registro de servicios.
Servicios configurables en Render
api-gateway
eureka-server
paciente-service
medico-service
atencion-service
examen-service
Resto de microservicios según disponibilidad de despliegue
Variables ejemplo para Render
DB_URL=jdbc:mysql://host-remoto:3306/nombre_db
DB_USERNAME=usuario
DB_PASSWORD=clave
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=https://eureka-server.onrender.com/eureka/

Ejemplo para servicios con Feign:

PACIENTE_SERVICE_URL=https://paciente-service.onrender.com
MEDICO_SERVICE_URL=https://medico-service.onrender.com
ATENCION_SERVICE_URL=https://atencion-service.onrender.com
Orden recomendado de ejecución

Para evitar errores de comunicación entre servicios, se recomienda levantar en este orden:

eureka-server
paciente-service
medico-service
atencion-service
diagnostico-service
receta-service
medicamento-service
examen-service
agenda-service
usuario-service
centro-medico-service
api-gateway
Endpoints principales
Pacientes
GET    /api/pacientes
GET    /api/pacientes/{id}
POST   /api/pacientes
PUT    /api/pacientes/{id}
DELETE /api/pacientes/{id}
Médicos
GET    /api/medicos
GET    /api/medicos/{id}
POST   /api/medicos
PUT    /api/medicos/{id}
DELETE /api/medicos/{id}
Atenciones
GET    /api/atenciones
GET    /api/atenciones/{id}
GET    /api/atenciones/{id}/detalle
GET    /api/atenciones/paciente/{pacienteId}
GET    /api/atenciones/medico/{medicoId}
POST   /api/atenciones
PUT    /api/atenciones/{id}
DELETE /api/atenciones/{id}
Exámenes
GET    /api/examenes
GET    /api/examenes/{id}
GET    /api/examenes/{id}/detalle
GET    /api/examenes/atencion/{atencionId}
GET    /api/examenes/nombre/{nombreExamen}
POST   /api/examenes
PUT    /api/examenes/{id}
DELETE /api/examenes/{id}
Ejemplo de flujo funcional
Registro de una atención
El usuario envía una solicitud a atencion-service.
atencion-service recibe pacienteId y medicoId.
Antes de guardar, consulta mediante Feign a:
paciente-service
medico-service
Si ambos existen, se guarda la atención.
Si alguno no existe, se lanza una excepción de regla de negocio y no se guarda información inconsistente.
Consulta de detalle de atención
El usuario consulta el detalle de una atención.
atencion-service busca la atención en su base de datos local.
Luego consulta los datos del paciente en paciente-service.
También consulta los datos del médico en medico-service.
Finalmente construye un AtencionDetalleDTO con la atención, paciente y médico.
Registro de un examen
El usuario envía una solicitud a examen-service.
examen-service recibe atencionId.
Antes de guardar, consulta mediante Feign a atencion-service.
Si la atención existe, se guarda el examen.
Si la atención no existe o el servicio remoto falla, se lanza una excepción y no se guarda el examen.
Control de versiones

El proyecto se mantiene en GitHub con commits técnicos y progresivos.

Ejemplos de commits realizados:

Configura despliegue Render y Dockerfiles
Refuerza pruebas unitarias de atencion-service
Refuerza pruebas unitarias de examen-service
Ajusta comunicacion Feign entre microservicios
Actualiza documentacion tecnica del proyecto
Consideraciones finales

Este proyecto demuestra una arquitectura distribuida basada en microservicios, integrando comunicación REST, separación por capas, persistencia independiente, documentación técnica con Swagger, pruebas unitarias con Mockito, configuración mediante YAML, API Gateway, Eureka Server y preparación para despliegue remoto en Render.

La solución permite validar reglas de negocio críticas del dominio médico, especialmente la consistencia entre atenciones, pacientes, médicos y exámenes.


Después de pegarlo y guardar, ejecuta desde la raíz:

```powershell
git status
git add README.md
git commit -m "Actualiza documentacion tecnica del proyecto"
git push origin main