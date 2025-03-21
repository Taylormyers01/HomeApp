# Create Local MYSQL db 
```bash
docker-compose down && docker-compose --profile dev up -d && SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun
```
# Use production db 
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
- Multi platform build 
    ```bash
    docker buildx build --build-arg VAADIN=true --build-arg PROFILE=prod -t taylormyers01/homeapp --platform linux/amd64,linux/arm64 .
  ```
- Push image to docker hub
    ```bash
    docker push taylormyers01/homeapp
    ```

# On Linux server
> Documenting here since I forget every time 

### General commands for docker 

- List all containers
    ```bash
    docker ps -a
    ```
- Stop and remove container
    ```bash
    docker stop container_id
    docker rm container_id
    ```
- Pull updated docker image
    ```bash
    docker pull taylormyes01/homeapp
    ```
- Create new container off of updated image 
    ```bash
    docker run -d -p 55000:8080 taylormyers01/homeapp
    ```
    > Ports for my setup were set to 55000 but should be able to be updated

- Remove all dependencies since vaadin loves to break
    ```bash
    rm -rf $HOME/.gradle/caches/
    ./gradlew build --refresh-dependencies
    ```

