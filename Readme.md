# Gliesereum platform

### Build jar's

##### gradle
```
    gradle clean build -b=account/build.gradle
    
    gradle clean build -b=discovery/build.gradle
    
    gradle clean build -b=proxy/build.gradle
    
    gradle clean build -b=mail/build.gradle
    
    gradle clean build -b=permission/build.gradle
    
    gradle clean build -b=karma/build.gradle
```

##### wrapper
```
    ./gradlew clean build -b=account/build.gradle
    
    ./gradlew clean build -b=discovery/build.gradle
    
    ./gradlew clean build -b=proxy/build.gradle
    
    ./gradlew clean build -b=mail/build.gradle
    
    ./gradlew clean build -b=permission/build.gradle

    ./gradlew clean build -b=karma/build.gradle
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
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-discovery')
docker build -t gls-discovery:0.0.1 -f docker/discovery/Dockerfile  .
```

##### Account
```
docker rm account-service
docker rmi gls-account
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-account')
docker build -t gls-account:0.0.1 -f docker/account/Dockerfile  .
```

##### Proxy
```
docker rm proxy-service
docker rmi gls-proxy
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-proxy')
docker build -t gls-proxy:0.0.1  -f docker/proxy/Dockerfile  .
```

##### Mail
```
docker rm mail-service
docker rmi gls-mail
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-mail')
docker build -t gls-mail:0.0.1 -f docker/mail/Dockerfile  .
```

##### Permission
```
docker rm permission-service
docker rmi gls-permission
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-permission')
docker build -t gls-permission:0.0.1 -f docker/permission/Dockerfile  .
```

##### Curator
```
docker rm curator-service
docker rmi gls-curator
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-curator')
docker build -t gls-curator:0.0.1 -f docker/curator/Dockerfile  .
```

##### Karma
```
docker rm karma-service
docker rmi gls-karma
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-karma')
docker build -t gls-karma:0.0.1 -f docker/karma/Dockerfile  .
```

### Run stack
```
docker stack deploy -c docker/docker-compose-dev.yml gls
docker stack deploy -c docker/docker-compose-dev-log.yml gls
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

##### Permission
```
docker rm permission-service
docker run -i -t -p 8230:8230 --name=permission-service -e PORT=8230 -e PROFILE=docker -e DB_HOST=127.0.0.1 -e DB_PORT=5432 -e DB_USERNAME=postgres -e DB_PASSWORD=postgres -e EUREKA_HOST=localhost -e EUREKA_PORT=8761 gls-permission
```

##### Karma
```
docker rm karma-service
docker run -i -t -p 8240:8240 --name=karma-service -e PORT=8240 -e PROFILE=docker -e DB_HOST=127.0.0.1 -e DB_PORT=5432 -e DB_USERNAME=postgres -e DB_PASSWORD=postgres -e EUREKA_HOST=localhost -e EUREKA_PORT=8761 gls-karma
```