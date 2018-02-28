# kotlin-spring-boot-rest-jpa-jwt-starter

[![Build Status](https://travis-ci.org/osahner/kotlin-spring-boot-rest-jpa-jwt-starter.svg?branch=master)](https://travis-ci.org/osahner/kotlin-spring-boot-rest-jpa-jwt-starter)

**Features**:
* spring-boot 2.0.0.RC2
* kotlin 1.2.21
* JWT Authentication/Authorization with spring-security [inspired by Auth0](https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/)
* JPA mysql
* Junit 5 Test
* Custom REST Exception Handling
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
