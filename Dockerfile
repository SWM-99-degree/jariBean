FROM openjdk:17-jdk
LABEL maintainer="jariBean"
EXPOSE 8080
COPY build/libs/*.jar /jaribean.jar
ENTRYPOINT ["java", "-DenvFile=~/springboot/.env" ,"-jar","/jaribean.jar"]
