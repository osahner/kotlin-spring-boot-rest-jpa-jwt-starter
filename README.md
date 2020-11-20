# kotlin-spring-boot-rest-jpa-jwt-starter

[![Build Status](https://travis-ci.org/osahner/kotlin-spring-boot-rest-jpa-jwt-starter.svg?branch=develop)](https://travis-ci.org/osahner/kotlin-spring-boot-rest-jpa-jwt-starter)
[![codecov](https://codecov.io/gh/osahner/kotlin-spring-boot-rest-jpa-jwt-starter/branch/develop/graph/badge.svg)](https://codecov.io/gh/osahner/kotlin-spring-boot-rest-jpa-jwt-starter/branch/develop/)


**Features**:
* spring-boot 2.4.x
* kotlin 1.4.x
* JWT Authentication/Authorization with spring-security [inspired by Auth0](https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/)
* JPA mysql / OpenCVS / POI
* Travis CI / codecov

### Install & play

* **create a mysql db**
```sql
CREATE DATABASE starterspringkotlin;
GRANT ALL ON starterspringkotlin.* TO starterspringkotlin@localhost IDENTIFIED BY 'starterspringkotlin';
FLUSH PRIVILEGES;
```

* **compile & integration tests**
```sh
mvn -Ddockerfile.skip clean compile test
```

* **run app**
```sh
mvn spring-boot:run
```

* **some CLI tests**
```sh
curl http://localhost:4080/starter-test/api/v1/test
# result: Pong!%

curl http://localhost:4080/starter-test/api/v1/restricted
# result {"timestamp":"***","status":403,"error":"Forbidden","message":"Access Denied","path":"/starter-test/api/v1/restricted"}%

curl -s -i -H "Content-Type: application/json" -X POST -d '{ "username": "john.doe", "password": "test1234"}' http://localhost:4080/starter-test/login | grep Authorization
# result: Authorization: Bearer ***

curl  -H "Authorization: Bearer ***"  http://localhost:4080/starter-test/api/v1/restricted
# result: Pong!%
```

### Docker

```sh
mvn clean package -Dmaven.test.skip=true
docker run -it -p 8888:8888 --rm osahner/kotlin-spring-boot-rest-jpa-jwt-starter:latest

curl http://localhost:8888/starter-test/api/v1/test
# result: Pong!%
```

### Why

This is my tiny backend cookbook. I need and use it on regular basis for different small to midsized projects.
* Like it -> use it.
* Found an error -> please tell me.

### Changelog
* _v0.7.1-SNAPSHOT_: spring-boot 2.4.0
* _v0.6.6-SNAPSHOT_: spring-boot 2.3.4, kotlin 1.4.10, update docker build
* _v0.6.5-SNAPSHOT_: spring-boot 2.3.2, kotlin 1.3.72, fix JPA uneccessary creation of hibernate_sequence and join tables without primary key, enhanced PoiExportService
* _v0.6.4-SNAPSHOT_: spring-boot 2.2.4, kotlin 1.3.70, fix REST API naming convention
* _v0.6.1-SNAPSHOT_: add Docker
* _v0.6.0-SNAPSHOT_: update spring-boot 2.2.0.RELEASE, add address controller with csv import an xls export
* _v0.5.0-SNAPSHOT_: spring-boot 2.1.9, and kotlin 1.3.50
* _v0.4.1-SNAPSHOT_: spring-boot 2.1.3 and kotlin 1.3.21, add codecov, fixed code style, fix tests, add coverage
* _v0.3.1-SNAPSHOT_: update jdk11, spring-boot 2.1.2 and kotlin 1.3.20
* _v0.1.0-SNAPSHOT_: switch to jar packaging standalone app, update kotlin 1.2.61, jwt 0.10.5
* _v0.0.5-SNAPSHOT_: update spring-boot 2.0.4.RELEASE, kotlin 1.2.60, jwt 0.10.1

## LICENCE

MIT © [Oliver Sahner](https://osahner.github.io)
