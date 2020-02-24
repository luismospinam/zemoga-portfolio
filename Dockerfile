FROM maven:3.5-jdk-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /usr/src/app/target/zemoga-portfolio-0.0.1-SNAPSHOT.war /usr/app/zemoga-portfolio.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/zemoga-portfolio.jar"]