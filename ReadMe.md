Create Local MYSQL db 

`docker-compose down && docker-compose --profile dev up -d && ./gradlew bootRun`

Use production db 

`docker-compose down && docker-compose --profile prod up -d`