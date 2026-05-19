# Sistema de Atenciones Médicas - Arquitectura de Microservicios

## Descripción General

Proyecto académico desarrollado utilizando arquitectura de microservicios con Spring Boot, JPA, MySQL y OpenFeign.

El sistema permite administrar:

- Pacientes
- Médicos
- Atenciones
- Diagnósticos
- Recetas
- Medicamentos
- Exámenes
- Agenda médica
- Usuarios
- Centros médicos

Cada microservicio funciona de manera independiente y posee:

- Base de datos propia
- Controladores REST
- Servicios
- Repositorios JPA
- DTOs
- ExceptionHandler global
- DataLoader
- Validaciones
- CRUD completo

---

# Tecnologías Utilizadas

- Java 17
- Spring Boot 3.5.14
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- OpenFeign
- Lombok
- Maven
- VS Code
- Postman

---

# Arquitectura del Proyecto

## Cadena principal de microservicios

```text
receta-service
    ↓
diagnostico-service
    ↓
atencion-service
    ↓
paciente-service
medico-service
```

## Otros consumos Feign

```text
agenda-service
 → paciente-service
 → medico-service

examen-service
 → atencion-service
```

---

# Microservicios del Sistema

| Microservicio | Puerto | Base de Datos |
|---|---|---|
| paciente-service | 8081 | paciente_db |
| medico-service | 8082 | medico_db |
| atencion-service | 8083 | atencion_db |
| diagnostico-service | 8084 | diagnostico_db |
| receta-service | 8085 | receta_db |
| medicamento-service | 8086 | medicamento_db |
| examen-service | 8087 | examen_db |
| agenda-service | 8088 | agenda_db |
| usuario-service | 8089 | usuario_db |
| centro-medico-service | 8090 | centro_medico_db |

---

# Creación de Bases de Datos

Ejecutar en HeidiSQL o MySQL:

```sql
DROP DATABASE IF EXISTS paciente_db;
CREATE DATABASE paciente_db;

DROP DATABASE IF EXISTS medico_db;
CREATE DATABASE medico_db;

DROP DATABASE IF EXISTS atencion_db;
CREATE DATABASE atencion_db;

DROP DATABASE IF EXISTS diagnostico_db;
CREATE DATABASE diagnostico_db;

DROP DATABASE IF EXISTS receta_db;
CREATE DATABASE receta_db;

DROP DATABASE IF EXISTS medicamento_db;
CREATE DATABASE medicamento_db;

DROP DATABASE IF EXISTS examen_db;
CREATE DATABASE examen_db;

DROP DATABASE IF EXISTS agenda_db;
CREATE DATABASE agenda_db;

DROP DATABASE IF EXISTS usuario_db;
CREATE DATABASE usuario_db;

DROP DATABASE IF EXISTS centro_medico_db;
CREATE DATABASE centro_medico_db;
```

---

# Orden Correcto de Ejecución

IMPORTANTE:

Levantar los microservicios en este orden para evitar errores Feign.

```text
1. paciente-service
2. medico-service
3. atencion-service
4. diagnostico-service
5. receta-service
6. medicamento-service
7. examen-service
8. agenda-service
9. usuario-service
10. centro-medico-service
```

---

# Configuración application.properties

Ejemplo:

```properties
spring.application.name=paciente-service

server.port=8081

spring.datasource.url=jdbc:mysql://localhost:3306/paciente_db
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

---

# Estructura Base de los Microservicios

```text
src/main/java
 ├── client
 ├── config
 ├── controller
 ├── dto
 ├── exception
 ├── model
 ├── repository
 └── service
```

---

# Explicación de Carpetas

| Carpeta | Función |
|---|---|
| controller | Endpoints REST |
| service | Lógica de negocio |
| repository | Acceso a base de datos |
| model | Entidades JPA |
| dto | Objetos de transferencia |
| client | Comunicación Feign |
| exception | Manejo global de errores |
| config | DataLoader y configuraciones |

---

# Explicación Técnica

## ¿Qué es OpenFeign?

Permite que un microservicio consuma endpoints de otro microservicio mediante interfaces Java.

Ejemplo:

```java
@FeignClient(name = "paciente-service", url = "http://localhost:8081")
```

---

## ¿Por qué cada microservicio tiene su propia base de datos?

Porque los microservicios deben estar desacoplados.

No se usan Foreign Keys reales entre servicios.

Las relaciones se manejan mediante IDs y llamadas HTTP.

---

## ¿Por qué usamos DTO?

Para evitar exponer directamente entidades internas y construir respuestas más limpias.

---

# Endpoint Más Importante del Proyecto

```text
GET http://localhost:8085/api/recetas/1/detalle
```

Este endpoint demuestra:

- Arquitectura distribuida
- OpenFeign
- DTOs
- Comunicación entre microservicios
- Encadenamiento de servicios

Cadena completa:

```text
receta-service
 → diagnostico-service
 → atencion-service
 → paciente-service
 → medico-service
```

---

# Endpoints Principales

## Pacientes

```text
GET http://localhost:8081/api/pacientes
GET http://localhost:8081/api/pacientes/1
GET http://localhost:8081/api/pacientes/rut/12345678-9
```

---

## Médicos

```text
GET http://localhost:8082/api/medicos
GET http://localhost:8082/api/medicos/1
GET http://localhost:8082/api/medicos/rut/11111111-1
GET http://localhost:8082/api/medicos/apellido/Pérez
```

---

## Atenciones

```text
GET http://localhost:8083/api/atenciones
GET http://localhost:8083/api/atenciones/1/detalle
GET http://localhost:8083/api/atenciones/paciente/1
GET http://localhost:8083/api/atenciones/medico/1
```

---

## Diagnósticos

```text
GET http://localhost:8084/api/diagnosticos
GET http://localhost:8084/api/diagnosticos/1/detalle
GET http://localhost:8084/api/diagnosticos/atencion/1
```

---

## Recetas

```text
GET http://localhost:8085/api/recetas
GET http://localhost:8085/api/recetas/1/detalle
GET http://localhost:8085/api/recetas/diagnostico/1
```

---

## Exámenes

```text
GET http://localhost:8087/api/examenes
GET http://localhost:8087/api/examenes/1/detalle
```

---

## Agenda

```text
GET http://localhost:8088/api/agendas
GET http://localhost:8088/api/agendas/1/detalle
```

---

# Pruebas Recomendadas en Postman

## 1. Probar CRUD básico

Ejemplo paciente:

### Crear paciente

```http
POST http://localhost:8081/api/pacientes
```

Body:

```json
{
  "rut": "12345678-9",
  "nombre": "Juan",
  "apellido": "Pérez",
  "edad": 30
}
```

---

### Listar pacientes

```http
GET http://localhost:8081/api/pacientes
```

---

### Actualizar paciente

```http
PUT http://localhost:8081/api/pacientes/1
```

---

### Eliminar paciente

```http
DELETE http://localhost:8081/api/pacientes/1
```

---

# Pruebas Feign Importantes

## Receta + Diagnóstico + Atención + Paciente + Médico

```http
GET http://localhost:8085/api/recetas/1/detalle
```

---

## Examen + Atención

```http
GET http://localhost:8087/api/examenes/1/detalle
```

---

## Agenda + Paciente + Médico

```http
GET http://localhost:8088/api/agendas/1/detalle
```

---

# Validaciones

Ejemplo JSON incorrecto:

```json
{
  "nombre": ""
}
```

Respuesta:

```json
{
  "nombre": "El nombre es obligatorio"
}
```

Tecnologías usadas:

- @Valid
- jakarta.validation
- GlobalExceptionHandler

---

# Manejo de Errores

Ejemplo:

```http
GET /api/pacientes/999
```

Respuesta:

```json
{
  "error": "Paciente no encontrado con id: 999"
}
```

---

# Problemas Frecuentes y Soluciones

## Error: Port already in use

```text
Port 8081 was already in use
```

### Solución

- cerrar terminales anteriores
- reiniciar VS Code

---

## Error: package does not exist

### Solución

1. cerrar VS Code
2. abrir nuevamente
3. ejecutar:

```text
Java: Clean Java Language Server Workspace
```

4. reiniciar VS Code

---

## Error Build Failed / ClassNotFoundException

### Solución

Eliminar carpeta:

```text
target
```

Luego ejecutar:

```bash
mvn install
```

---

# Comandos Maven Utilizados

## Limpiar proyecto

```bash
mvn clean
```

---

## Compilar proyecto

```bash
mvn install
```

---

# Observaciones Técnicas

Durante el desarrollo se utilizó:

```properties
spring.jpa.hibernate.ddl-auto=create
```

En producción podría reemplazarse por:

- Flyway
- Liquibase

para manejar migraciones versionadas.

---

# Características Implementadas

- Arquitectura de microservicios
- CRUD completo
- OpenFeign
- DTOs
- JPA + Hibernate
- Validaciones
- ExceptionHandler
- Bases de datos independientes
- DataLoader
- ResponseEntity
- Búsquedas personalizadas
- Arquitectura por capas

---

# Autor

Proyecto académico desarrollado para evaluación de Microservicios y Desarrollo Backend.
