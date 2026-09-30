FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/BackEnd_Grupo7-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]