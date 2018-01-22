```sql
CREATE USER 'startertest'@'localhost' IDENTIFIED BY 'startertest';
GRANT ALL ON `startertest`.* TO `startertest`@`localhost`;
```

```bash
curl -i -H "Content-Type: application/json" -X POST -d '{ "username": "john.doe", "password": "test1234"}' http://localhost:8080/login
```

```bash
curl http://localhost:8080/api/test
```

```bash
curl http://localhost:8080/api/restricted
curl  -H "Authorization: Bearer ******"  http://localhost:8080/api/restricted
```


```bash
curl -H "Authorization: Bearer ******" http://localhost:8080/actuator/healt
```
