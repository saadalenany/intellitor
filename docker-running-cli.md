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

## Obsolete method of running each service independently

### Run a MySQL Docker image with empty Password for `dao` microservice
```
docker run --name mysql -e MYSQL_ALLOW_EMPTY_PASSWORD=1 -d mysql:latest
```
for accessing the docker shell
```
docker exec -it mysql mysql -uroot -p
```

### Build
```
docker build -t intellitor .
docker build -t dao .
docker build -t user .
docker build -t course .
docker build -t enrollment .
```

### Run
```
docker run --name intellitor -dp 127.0.0.1:8761:8761 intellitor:latest
docker run --name dao --link mysql:latest --link intellitor:latest -dp 127.0.0.1:8081:8081 dao:latest
docker run --name user --link dao:latest --link intellitor:latest -dp 127.0.0.1:8082:8082 user:latest
docker run --name course --link intellitor:latest -dp 127.0.0.1:8083:8083 course:latest
docker run --name enrollment --link intellitor:latest -dp 127.0.0.1:8084:8084 enrollment:latest
```
