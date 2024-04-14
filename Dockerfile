FROM openjdk:17-jdk
LABEL maintainer="jariBean"
EXPOSE 8080
COPY app.jar .
ENTRYPOINT ["java", "-DenvFile=~/springboot/.env" ,"-jar","-Xmx512m","-Xms256m","app.jar"]
