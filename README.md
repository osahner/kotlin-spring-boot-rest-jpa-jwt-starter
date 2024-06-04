# kotlin-spring-boot-rest-jpa-jwt-starter

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=osahner_kotlin-spring-boot-rest-jpa-jwt-starter)](https://sonarcloud.io/summary/new_code?id=osahner_kotlin-spring-boot-rest-jpa-jwt-starter)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=osahner_kotlin-spring-boot-rest-jpa-jwt-starter&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=osahner_kotlin-spring-boot-rest-jpa-jwt-starter)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=osahner_kotlin-spring-boot-rest-jpa-jwt-starter&metric=bugs)](https://sonarcloud.io/summary/new_code?id=osahner_kotlin-spring-boot-rest-jpa-jwt-starter)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=osahner_kotlin-spring-boot-rest-jpa-jwt-starter&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=osahner_kotlin-spring-boot-rest-jpa-jwt-starter)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=osahner_kotlin-spring-boot-rest-jpa-jwt-starter&metric=coverage)](https://sonarcloud.io/summary/new_code?id=osahner_kotlin-spring-boot-rest-jpa-jwt-starter)

**Features**:
* spring-boot 3.2.x
* kotlin 1.9.x
* JWT Authentication/Authorization with spring-security [inspired by Auth0](https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/)
* 2FA with TOTP (Google Authenticator)
* JPA mysql / OpenCVS / POI

### Install & play

* **create a mysql db**
```sql
-- for mysql 5.7
CREATE DATABASE starterspringkotlin;
GRANT ALL ON starterspringkotlin.* TO starterspringkotlin@localhost IDENTIFIED BY 'starterspringkotlin';
FLUSH PRIVILEGES;
-- for mysql 8
CREATE DATABASE starterspringkotlin;
CREATE USER 'starterspringkotlin'@'localhost' IDENTIFIED BY 'starterspringkotlin';
GRANT ALL PRIVILEGES ON starterspringkotlin.* TO 'starterspringkotlin'@'localhost';
FLUSH PRIVILEGES;
```
> check `src/main/resources/application.yaml` for mysql 5.7 or 8 support (keys are `spring.jpa.database-platform` and `spring.datasource.url`)

* **compile & integration tests**
```shell
mvn clean compile test
```

* **run app**
```shell
mvn spring-boot:run
```

* **some CLI tests**
```shell
curl http://localhost:4080/starter-test/api/v1/test
# result: Pong!%

curl http://localhost:4080/starter-test/api/v1/restricted
# result {"timestamp":"***","status":403,"error":"Forbidden","message":"Access Denied","path":"/starter-test/api/v1/restricted"}%

curl -s -i -H "Content-Type: application/json" -X POST -d '{ "username": "john.doe", "password": "test1234"}' http://localhost:4080/starter-test/login | grep Authorization
# result: Authorization: Bearer ***

curl  -H "Authorization: Bearer ***"  http://localhost:4080/starter-test/api/v1/restricted
# result: Pong!%
```
some more test can be found in [address.http](contributed/requests/address.http) if you are using intelli j.

### Docker

```shell
./contributed/buildDocker.sh (-p) # see below
docker run -it -p 8888:8888 --rm osahner/kotlin-spring-boot-rest-jpa-jwt-starter:0.11.0-SNAPSHOT

curl http://localhost:8888/starter-test/api/v1/test
# result: Pong!%
```

:exclamation: If you develop on Apple Silicon (like me) you can use the simple script `contributed/buildDocker.sh`. Option `-p` is for **production** build (`--platform=linux/amd64` instead of `--platform=linux/arm64/v8` without)

Modify `Dockerfile` to your needs. 

### Why

This is my little backend cookbook. I need and use it regularly for various small to medium-sized projects.
* Like it -> use it.
* Found an error -> please [report](https://github.com/osahner/kotlin-spring-boot-rest-jpa-jwt-starter/issues).

### Changelog
* _v0.11.2-SNAPSHOT_: spring-boot 3.3.0, kotlin 2.0.0
* _v0.11.1-SNAPSHOT_: fix token validity time
* _v0.11.0-SNAPSHOT_: spring-boot 3.2.x, kotlin 1.9.x, java 21
* _v0.10.0-SNAPSHOT_: spring-boot 3.1.x, add 2FA, cleanup 
* _v0.9.1-SNAPSHOT_: spring-boot 3.0.x, kotlin 1.8.x, [migrated to SEQ tables](#migrate-to-seq-tables) 
* _v0.8.3-SNAPSHOT_: spring-boot 2.7.x, java 17
* _v0.8.1-SNAPSHOT_: spring-boot 2.6.x
* _v0.8.0-SNAPSHOT_: [renamed default branch to main](#rename-local-master-branch-to-main), spring-boot 2.5.x, kotlin 1.4.10
* _v0.7.1-SNAPSHOT_: spring-boot 2.4.0
* _v0.6.6-SNAPSHOT_: spring-boot 2.3.4, kotlin 1.4.10, update docker build
* _v0.6.5-SNAPSHOT_: spring-boot 2.3.2, kotlin 1.3.72, fix JPA uneccessary creation of hibernate_sequence and join tables without primary key, enhanced PoiExportService
* _v0.6.4-SNAPSHOT_: spring-boot 2.2.4, kotlin 1.3.70, fix REST API naming convention
* _v0.6.1-SNAPSHOT_: add Docker
* _v0.6.0-SNAPSHOT_: update spring-boot 2.2.0.RELEASE, add address controller with csv import and xls export
* _v0.5.0-SNAPSHOT_: spring-boot 2.1.9, and kotlin 1.3.50
* _v0.4.1-SNAPSHOT_: spring-boot 2.1.3 and kotlin 1.3.21, add codecov, fixed code style, fix tests, add coverage
* _v0.3.1-SNAPSHOT_: update jdk11, spring-boot 2.1.2 and kotlin 1.3.20
* _v0.1.0-SNAPSHOT_: switch to jar packaging standalone app, update kotlin 1.2.61, jwt 0.10.5
* _v0.0.5-SNAPSHOT_: update spring-boot 2.0.4.RELEASE, kotlin 1.2.60, jwt 0.10.1

#### Rename local master branch to main
```shell
git branch -m master main
git fetch origin
git branch -u origin/main main
```

#### Migrate to SEQ tables
```sql
-- migrate existing autoincrement tables to SEQ tables after table update
SET FOREIGN_KEY_CHECKS = 0;
USE starterspringkotlin;

UPDATE app_user_SEQ SET next_val = (SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'starterspringkotlin' AND TABLE_NAME = 'app_user') + 1;
ALTER TABLE app_user MODIFY id INT NOT NULL;

UPDATE app_role_SEQ SET next_val = (SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'starterspringkotlin' AND TABLE_NAME = 'app_role') + 1;
ALTER TABLE app_role MODIFY id INT NOT NULL;

UPDATE address_SEQ SET next_val = (SELECT AUTO_INCREMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'starterspringkotlin' AND TABLE_NAME = 'address') + 1;
ALTER TABLE address MODIFY id INT NOT NULL;

SET FOREIGN_KEY_CHECKS = 1;
```

## LICENCE

MIT © [Oliver Sahner](https://osahner.github.io)
