FROM openjdk:11.0.12

WORKDIR $APP_HOME

COPY . .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./build/libs/community-0.0.1-SNAPSHOT.jar"]