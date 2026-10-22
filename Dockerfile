#
# Mvn Build
#
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests
#
# Jar Package
#
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /home/app/target/*.jar /usr/local/lib/lipputoimisto.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/lipputoimisto.jar"]
