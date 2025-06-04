FROM openjdk:17-jdk-slim

WORKDIR /app

# Install PostgreSQL driver
RUN apt-get update && apt-get install -y wget
RUN wget https://jdbc.postgresql.org/download/postgresql-42.7.2.jar -O /app/postgresql.jar

# Copy your JAR
COPY target/apex-web-server-*.jar app.jar

# Set timezone
ENV TZ=Europe/Kiev
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Database connection
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://database-apex.czoc4gse0qtr.eu-north-1.rds.amazonaws.com:5432/postgres
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=apexroot

# Run with the PostgreSQL driver included in classpath
ENTRYPOINT ["java", "-cp", "/app/postgresql.jar:/app/app.jar", "org.springframework.boot.loader.launch.JarLauncher"]

EXPOSE 8080