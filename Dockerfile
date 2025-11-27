# --- ETAPA 1: Construcción (Build) ---
# Usamos la versión estándar (sin alpine) que sí soporta Mac M1/M2
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# --- ETAPA 2: Ejecución (Run) ---
# Usamos la versión estándar de Java
FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
 
