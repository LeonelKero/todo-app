FROM openjdk:17-jdk-slim
LABEL authors="greengorilla7"
COPY target/todo-api.jar todo-api.jar

ENTRYPOINT ["java", "-jar", "todo-api"]