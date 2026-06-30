FROM eclipse-temurin:17-jre-jammy

ARG JAR_FILE=target/medico-service-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app_medico.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app_medico.jar"]

