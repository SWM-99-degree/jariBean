FROM openjdk:17-jdk
LABEL maintainer="jariBean"
EXPOSE 8080
COPY ./build/libs/jariBean-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-DenvFile=~/springboot/.env" ,"-jar","app.jar"]
