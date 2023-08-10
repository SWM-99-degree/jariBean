FROM openjdk:17-jdk
LABEL maintainer="jariBean"
EXPOSE 8080
COPY *.jar app.jar
ENTRYPOINT ["java", "-DenvFile=~/springboot/.env" ,"-jar","app.jar"]
