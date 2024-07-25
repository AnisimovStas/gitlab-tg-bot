FROM eclipse-temurin:21
EXPOSE 8080
WORKDIR /opt/app
COPY build/libs/tgbot.gitlab-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar" ]