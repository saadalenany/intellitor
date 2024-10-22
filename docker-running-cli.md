# Steps for containerizing the microservices

## Prerequisites
* JDK 17
* Docker
* Gradle

### Before anything build & publish `common` library to Local .m2
```
gradle clean build; ./gradlew publishToMavenLocal
```

### then build each service
```
./gradlew clean build --include-build ..\common --console plain
```

## Run Docker `compose.yaml` file
```
docker compose up -d
```