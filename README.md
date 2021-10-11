# Skill Matrix

[![CI](https://github.com/marckoch/skillmatrix/actions/workflows/main.yml/badge.svg)](https://github.com/marckoch/skillmatrix/actions/workflows/main.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=marckoch_skillmatrix&metric=alert_status)](https://sonarcloud.io/dashboard?id=marckoch_skillmatrix)

A refactoring of the beloved [Spring Petclinic](https://github.com/spring-projects/spring-petclinic) to a skill matrix project.

## What is a skill matrix?

Here developers of a company can enter and rate their skills. Other people can search for skills and see which person has these skills.

## Running it

You can run it from Maven directly using the Spring Boot Maven plugin.

```
./mvnw spring-boot:run
```

You can then access it here: http://localhost:8080/

## Tech Stack
- Java 17
- Maven 3.8.2
- Spring Boot 2.5.5
- Thymeleaf
- Bootstrap 5
- Tokeninput: https://loopj.com/jquery-tokeninput/ (Demos: https://loopj.com/jquery-tokeninput/demo.html)