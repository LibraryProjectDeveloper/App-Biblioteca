FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY target/AppBiblioteca-0.0.1-SNAPSHOT.jar .

EXPOSE 8080


ENTRYPOINT ["java", "-jar", "AppBiblioteca-0.0.1-SNAPSHOT.jar"]