# FROM openjdk:17-jdk
# LABEL maintainer="jariBean"
# EXPOSE 8080
# COPY app.jar .
# ENTRYPOINT ["java", "-DenvFile=~/springboot/.env" ,"-jar","app.jar"]


FROM openjdk:17-jdk
LABEL maintainer="jariBean"
EXPOSE 8080

# dd-java-agent.jar 파일을 다운로드하고 원하는 디렉토리에 저장
RUN curl -L -O https://github.com/DataDog/dd-trace-java/releases/download/download-latest/dd-java-agent.jar

# dd-java-agent.jar 파일을 다운로드한 뒤에 Java 애플리케이션 실행
COPY app.jar .
ENTRYPOINT ["java", "-javaagent:/dd-java-agent.jar", "-DenvFile=~/springboot/.env", "-jar", "app.jar"]
