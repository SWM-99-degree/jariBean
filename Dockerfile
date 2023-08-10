FROM openjdk:17-jdk
LABEL maintainer="jariBean"
EXPOSE 8080
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} jaribean.jar
ENTRYPOINT ["java", "-DenvFile=~/springboot/.env" ,"-jar","/jaribean.jar"]
