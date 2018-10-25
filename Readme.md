#Gliesereum platform

### Build jar's

#####gradle
```
    gradle clean -b=account/build.gradle
    gradle build -b=account/build.gradle
    
    gradle clean -b=discovery/build.gradle
    gradle build -b=discovery/build.gradle
    
    gradle clean -b=proxy/build.gradle
    gradle build -b=proxy/build.gradle
    
    gradle clean -b=mail/build.gradle
    gradle build -b=mail/build.gradle
```

##### wrapper
```
    ./gradlew clean -b=account/build.gradle
    ./gradlew build -b=account/build.gradle
    
    ./gradlew clean -b=discovery/build.gradle
    ./gradlew build -b=discovery/build.gradle
    
    ./gradlew clean -b=proxy/build.gradle
    ./gradlew build -b=proxy/build.gradle
    
    ./gradlew clean -b=mail/build.gradle
    ./gradlew build -b=mail/build.gradle
```

### Build images

##### Clean docker

``` 
docker rm $(docker ps -a -q) --force
```

##### Discovery
```
docker rm discovery-service
docker rmi gls-discovery
docker build -t gls-discovery -f docker/discovery/Dockerfile  .
```

##### Account
```
docker rm account-service
docker rmi gls-account
docker build -t gls-account -f docker/account/Dockerfile  .
```

##### Proxy
```
docker rm proxy-service
docker rmi gls-proxy
docker build -t gls-proxy  -f docker/proxy/Dockerfile  .
```

##### Mail
```
docker rm mail-service
docker rmi gls-mail
docker build -t gls-mail -f docker/mail/Dockerfile  .
```


### Run stack
```
docker stack deploy -c docker/docker-compose-dev.yml gls
```

### Run container

##### Discovery
```
docker rm discovery-service
docker run -i -t -p 8761:8761 --name=discovery-service -e PORT=8761 -e PROFILE=docker -e HOST=localhost gls-discovery
```

##### Account
```
docker rm account-service
docker run -i -t -p 8210:8210 --name=account-service -e PORT=8210 -e PROFILE=docker -e DB_HOST=127.0.0.1 -e DB_PORT=5432 -e DB_USERNAME=postgres -e DB_PASSWORD=postgres -e REDIS_HOST=localhost -e REDIS_PORT=6379 -e REDIS_PASSWORD= -e EUREKA_HOST=localhost -e EUREKA_PORT=8761 gls-account
```

##### Proxy
```
docker rm proxy-service
docker run -i -t -p 8200:8200 --name=proxy-service -e PORT=8200 -e PROFILE=docker -e EUREKA_HOST=localhost -e EUREKA_PORT=8761 gls-proxy
```

##### Mail
```
docker rm account-service
docker run -i -t -p 8220:8220 --name=mail-service -e PORT=8220 -e PROFILE=docker -e REDIS_HOST=localhost -e REDIS_PORT=6379 -e REDIS_PASSWORD= -e EUREKA_HOST=localhost -e EUREKA_PORT=8761 gls-mail
```