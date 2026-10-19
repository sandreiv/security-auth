# --- ETAPA 1: Compilación ---
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# 1. Copiamos los archivos de estructura de Gradle primero
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# 2. CRLF de Windows rompe el shebang en Alpine ("not found")
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
RUN ./gradlew --version

# 3. Copiamos el código fuente y compilamos
COPY src ./src
RUN ./gradlew clean build -x test

# --- ETAPA 2: Imagen Final ---
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

#EXPOSE 8171
EXPOSE 9000
ENTRYPOINT ["java", "-jar", "app.jar"]