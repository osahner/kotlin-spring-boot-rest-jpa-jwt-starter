# kotlin-spring-boot-rest-jpa-jwt-starter

[![Build Status](https://travis-ci.org/osahner/kotlin-spring-boot-rest-jpa-jwt-starter.svg?branch=master)](https://travis-ci.org/osahner/kotlin-spring-boot-rest-jpa-jwt-starter)

**Features**:
* spring-boot 2.0.0.RELEASE
* kotlin 1.2.30
* JWT Authentication/Authorization with spring-security [inspired by Auth0](https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/)
* JPA mysql
* Junit 5 Tests
* Custom REST Exception Handling (inspired by [Bruno Leite](https://www.toptal.com/java/spring-boot-rest-api-error-handling) and [Eugen Paraschiv](http://www.baeldung.com/exception-handling-for-rest-with-spring))
* Travis CI

### Install

* **create mysql db**
```sql
CREATE DATABASE starterspringkotlin;
GRANT ALL ON starterspringkotlin.* TO starterspringkotlin@localhost IDENTIFIED BY 'starterspringkotlin';
FLUSH PRIVILEGES;
```

* **compile and run**
```bash
mvn clean compile test
mvn spring-boot:run
```

### Test
```bash
curl http://localhost:8080/api/test
```

```bash
curl http://localhost:8080/api/restricted
```

```bash
curl -i -H "Content-Type: application/json" -X POST -d '{ "username": "john.doe", "password": "test1234"}' http://localhost:8080/login
```

```bash
curl  -H "Authorization: Bearer ******"  http://localhost:8080/api/restricted
```

```bash
curl -H "Authorization: Bearer ******" http://localhost:8080/actuator/health
```

### Changelog

* _v0.0.2-SNAPSHOT_: update spring-boot 2.0.0.RELEASE, kotlin 1.2.30, Junit5 5.1.0
