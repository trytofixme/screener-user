########################
# 1) BUILD (Gradle)
########################
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY gradlew gradle/ ./
RUN chmod +x gradlew
COPY build.gradle settings.gradle gradle.properties* ./
RUN ./gradlew --no-daemon help

COPY src ./src
RUN ./gradlew --no-daemon clean bootJar -x test

########################
# 2) RUNTIME (JRE)
########################
FROM eclipse-temurin:21-jre-jammy

RUN useradd -r -u 1001 app
USER 1001

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENV JAVA_TOOL_OPTIONS="-XX:+UseG1GC -XX:MaxRAMPercentage=70"

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
