# Imagen base con Java 17.
# JRE sirve para ejecutar aplicaciones Java ya compiladas.
FROM eclipse-temurin:17-jre-jammy

# Se declara una variable llamada JAR_FILE.
# Esta variable contiene la ruta del archivo .jar generado por Maven dentro de target/.
ARG JAR_FILE=target/examen-service-0.0.1-SNAPSHOT.jar

# Copia el archivo .jar desde la carpeta target/ al contenedor.
# Dentro del contenedor se renombra como app_examen.jar.
COPY ${JAR_FILE} app_examen.jar

# Expone el puerto que usa examen-service.
# Este microservicio trabaja en el puerto 8087.
EXPOSE 8087

# Comando que se ejecuta cuando el contenedor inicia.
# Levanta la aplicación usando java -jar.
ENTRYPOINT ["java", "-jar", "app_examen.jar"]