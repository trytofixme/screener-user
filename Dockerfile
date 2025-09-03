# === 1) BUILD (Gradle wrapper) ===
ARG MODULE=server
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY gradlew gradle/ ./
RUN chmod +x gradlew
COPY settings.gradle* build.gradle* gradle.properties* ./

COPY ${MODULE}/build.gradle* ${MODULE}/
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon :${MODULE}:dependencies

COPY ${MODULE}/ ${MODULE}/
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon :${MODULE}:clean :${MODULE}:bootJar -x test

# === 2) RUNTIME (JRE only) ===
FROM eclipse-temurin:21-jre-jammy
ARG MODULE=server
WORKDIR /app

RUN useradd -r -u 1001 app && chown -R 1001:0 /app
USER 1001

COPY --from=build /app/${MODULE}/build/libs/*.jar /app/app.jar

ENV JAVA_TOOL_OPTIONS="-XX:+UseG1GC -XX:MaxRAMPercentage=70"

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
