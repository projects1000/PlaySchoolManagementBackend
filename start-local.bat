@echo off
echo Starting PlaySchool Management Backend in Local Development Mode...
echo.
echo Environment: LOCAL
echo Database: Cloud PostgreSQL (shared for dev/prod)
echo CORS: Allowing localhost + production domains
echo.
mvn spring-boot:run
