FROM openjdk:17-jdk-slim
LABEL authors="greengorilla7"
LABEL contact="waboleonel@gmail.com"
COPY target/todo-api.jar todo-api.jar

ENTRYPOINT ["java", "-jar", "todo-api"]