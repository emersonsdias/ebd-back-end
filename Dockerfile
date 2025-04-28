# Build stage
FROM maven:3.9.6-eclipse-temurin-11 AS maven
COPY pom.xml /home/app
COPY src /home/app/src
RUN mvn -f /home/app/pom.xml clean package

# Runtime stage
FROM eclipse-temurin:21-jdk
COPY --from=maven /home/app/target/ebd-0.0.1-SNAPSHOT.jar /usr/local/lib/ebd.jar
VOLUME /tmp
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/ebd.jar"]
