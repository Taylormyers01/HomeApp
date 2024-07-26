Create Local MYSQL db 
```bash
docker-compose down && docker-compose --profile dev up -d && SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun

```
Use production db 
- Generate new .jar
```bash
SPRING_PROFILES_ACTIVE=prod ./gradlew build 
```
- Create new container
```bash
docker-compose down && docker-compose --profile prod up -d
```
- Create image from container
```bash
docker commit homeapp-myapp-1 taylormyers01/homeapp
```
- Push image to docker hub
```bash
docker push taylormyers01/homeapp
```

