# Imagen base con Java 17.
# JRE sirve para ejecutar aplicaciones Java ya compiladas.
FROM eclipse-temurin:17-jre-jammy

# Se declara una variable llamada JAR_FILE.
# Esta variable contiene la ruta del archivo .jar generado por Maven dentro de target/.
ARG JAR_FILE=target/receta-service-0.0.1-SNAPSHOT.jar

# Copia el archivo .jar desde la carpeta target/ al contenedor.
# Dentro del contenedor se renombra como app_receta.jar.
COPY ${JAR_FILE} app_receta.jar

# Expone el puerto que usa receta-service.
# Este microservicio trabaja en el puerto 8085.
EXPOSE 8085

# Comando que se ejecuta cuando el contenedor inicia.
# Levanta la aplicación usando java -jar.
ENTRYPOINT ["java", "-jar", "app_receta.jar"]