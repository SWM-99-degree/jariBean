FROM openjdk:17-jdk
LABEL maintainer="jariBean"
EXPOSE 8080
ENTRYPOINT ["java", "-DenvFile=~/springboot/.env" ,"-jar","app.jar"]
