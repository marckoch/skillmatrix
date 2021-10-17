# Skill Matrix

[![CI](https://github.com/marckoch/skillmatrix/actions/workflows/main.yml/badge.svg)](https://github.com/marckoch/skillmatrix/actions/workflows/main.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=marckoch_skillmatrix&metric=alert_status)](https://sonarcloud.io/dashboard?id=marckoch_skillmatrix)

A refactoring of the beloved [Spring Petclinic](https://github.com/spring-projects/spring-petclinic) to a skill matrix project.

## What is a skill matrix?

Here developers of a company can enter and rate their skills. Other people can search for skills and see which person has these skills.

## Running it with H2

You can run it from Maven directly using the Spring Boot Maven plugin. Without any profile we use a H2 database.

```
./mvnw spring-boot:run
```

You can then access it here: http://localhost:8080/

## Running it with postgres dB

First start the database
```shell
docker run -p 5432:5432 --name skillmatrix-postgres -e POSTGRES_USER=sm_user -e POSTGRES_PASSWORD=sm_pw -e POSTGRES_DB=skillmatrix-db postgres
```
then start the app
```
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Tech Stack
- Java 17
- Maven 3.8.2
- Spring Boot 2.5.5
- Thymeleaf
- Bootstrap 5
- Tokeninput: https://loopj.com/jquery-tokeninput/ (Demos: https://loopj.com/jquery-tokeninput/demo.html)
- darkmode from https://blog.shhdharmen.me/toggle-light-and-dark-themes-in-bootstrap and https://bootswatch.com/cyborg/

## postgres db

Run postgres DB in docker container:
```
docker run -p 5432:5432 --name skillmatrix-postgres -e POSTGRES_USER=sm_user -e POSTGRES_PASSWORD=sm_pw -e POSTGRES_DB=skillmatrix-db postgres
```
- `skillmatrix-postgres` is the container name
- `skillmatrix-db` is the name of the database
- `postgres` is the image name pulled from docker
