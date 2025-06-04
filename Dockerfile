FROM eclipse-temurin:17-jdk-jammy AS java-builder

WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=java-builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
