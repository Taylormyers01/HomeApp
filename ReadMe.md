Create Local MYSQL db 
```bash
docker-compose down && docker-compose --profile dev up -d && ./gradlew bootRun

```
Use production db 
- Generate new .jar
```bash
SPRING_PROFILES_ACTIVE=prod ./gradlew build 
```
- Create new image 
```bash
docker-compose down && docker-compose --profile prod up -d
```
- Create tagged version from docker image
```bash
docker tag homeapp-myapp taylormyers01/homeapp
```
- Push image to docker hub
```bash
docker push taylormyers01/homeapp
```

