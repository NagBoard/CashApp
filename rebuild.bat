@echo off
echo Building JAR without running tests...
call mvnw clean package -DskipTests
echo Build completed!