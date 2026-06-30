
FROM eclipse-temurin:17-jre-jammy


ARG JAR_FILE=target/paciente-service-0.0.1-SNAPSHOT.jar


COPY ${JAR_FILE} app_paciente.jar


EXPOSE 8081


ENTRYPOINT ["java", "-jar", "app_paciente.jar"]