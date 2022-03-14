##MAVEN BUILD
FROM maven:3.6.3-jdk-11-slim AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package -Dmaven.test.skip=true

FROM adoptopenjdk/openjdk11:alpine-jre

EXPOSE 8080

##FROM MAVEN BUILD
ARG JAR_FILE=target/*.jar
ARG JAR_NAME=app.jar
COPY --from=MAVEN_TOOL_CHAIN /tmp/${JAR_FILE} ${JAR_NAME}
ENTRYPOINT ["java","-jar","app.jar"]

##NON BUILD
#COPY target/*.jar app.jar
#ENTRYPOINT ["java","-jar","app.jar"]
