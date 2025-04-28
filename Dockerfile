FROM eclipse-temurin:11-jdk AS maven
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:21
COPY --from=maven /home/app/target/ebd-0.0.1-SNAPSHOT.jar /usr/local/lib/ebd.jar
VOLUME /tmp
EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/local/lib/ebd.jar"]