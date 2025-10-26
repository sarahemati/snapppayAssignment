# === Build stage ===
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# === Run stage ===
FROM eclipse-temurin:21-jre
ENV TZ=Asia/Tehran \
    SPRING_PROFILES_ACTIVE=docker \
    JAVA_OPTS=""
WORKDIR /opt/app
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
EXPOSE 8080
USER 1000
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
