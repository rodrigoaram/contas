FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk maven -y
COPY . .
RUN mvn -DskipTests clean package


FROM openjdk:21-slim

EXPOSE 8080

COPY --from=build /target/contas.jar .
COPY --from=build contas.sqlite .

ENTRYPOINT ["java", "-jar", "contas.jar"]
