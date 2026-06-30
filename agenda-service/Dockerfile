# Imagen base con Java 17.
# JRE sirve para ejecutar aplicaciones Java ya compiladas.
FROM eclipse-temurin:17-jre-jammy

# Se declara una variable llamada JAR_FILE.
# Esta variable contiene la ruta del archivo .jar generado por Maven dentro de target/.
ARG JAR_FILE=target/agenda-service-0.0.1-SNAPSHOT.jar

# Copia el archivo .jar desde la carpeta target/ al contenedor.
# Dentro del contenedor se renombra como app_agenda.jar.
COPY ${JAR_FILE} app_agenda.jar

# Expone el puerto que usa agenda-service.
# Este microservicio trabaja en el puerto 8088.
EXPOSE 8088

# Comando que se ejecuta cuando el contenedor inicia.
# Levanta la aplicación usando java -jar.
ENTRYPOINT ["java", "-jar", "app_agenda.jar"]