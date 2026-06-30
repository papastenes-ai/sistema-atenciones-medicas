FROM eclipse-temurin:17-jre-jammy

ARG JAR_FILE=target/atencion-service-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app_atencion.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app_atencion.jar"]