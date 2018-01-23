# kotlin-spring-boot-rest-jpa-jwt-starter

**Features**:
* spring-boot 2.0.0.M7
* kotlin 1.2.20
* JWT Authentication/Authorization with spring-security
* JPA mysql
* Rest Exception handling

### Install

* **create mysql db**
```sql
CREATE DATABASE starterspringkotlin;
GRANT ALL ON starterspringkotlin.* TO starterspringkotlin@localhost IDENTIFIED BY 'starterspringkotlin';
FLUSH PRIVILEGES;
```

* **compile and run**
```bash
mvn install
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
