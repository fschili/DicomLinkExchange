# ---- Build Stage ----
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /build
COPY pom.xml .
COPY dicomLinkExchange.yaml .
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Runtime Stage ----
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /build/target/dicom-link-exchange.jar app.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "app.jar"]
