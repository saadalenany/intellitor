# Use a specific version of OpenJDK
FROM eclipse-temurin:17-jdk-jammy
# Set the working directory
WORKDIR /build
# Create JAR File argument
ARG JAR_FILE=build/libs/*.jar
# Copy only the necessary Jars
COPY ${JAR_FILE} intellitor.jar
ENTRYPOINT ["java","-Dspring.profiles.active=docker","-jar","intellitor.jar"]