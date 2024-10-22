# Intellitor

A Spring Boot microservices project for e-learning utilizing spring modules & microservices architecture

## components
The Project consists of 3 main microservices, gateway service, discovery server & a common Java library
* `server` ==> Eureka Discovery server listens to port `8761`
* `user` ==> An Auth & security microservice listens to port `8082`
* `course` ==> A Business logic-related microservice for courses & quiz that listens to port `8083`
* `enrollment` ==> A Business logic-related microservice for enrollments & engagements that listens to port `8084`
* `gateway` ==> A Spring API-Gateway microservice listens to port `8085`
* `common` ==> A Java library for all dao ops, common & shared code by all microservices as well as utilities

## Running the project

Intellitor could be deployed either directly through direct JVM env or Docker container

### Direct JVM deployment
#### Prerequisites
* JDK 17
* Gradle
* MySQL server running (e.g: xampp)

#### 1. build & publish common library to Local .m2
```
gradle clean build; ./gradlew publishToMavenLocal
```

#### 2. build each service of the 5
```
./gradlew clean build --include-build ..\common --console plain
```

#### 3. run each service
on Linux
```
./gradlew bootRun
```
on Windows
```
gradlew.bat bootRun
```

### Important:: Order of Running services
1. server
2. user
3. course
4. enrollment
5. gateway

### For Docker containerization deployment
Check [docker-running-cli](docker-running-cli.md)