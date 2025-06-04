FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR
COPY target/apex-web-server-*.jar app.jar

# Set timezone
ENV TZ=Europe/Kiev
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Run application with production profile
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app/app.jar"]

EXPOSE 8080