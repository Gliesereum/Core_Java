#Gliesereum platform

### Build images

##### Discovery
```
docker rm discovery-service
docker rmi gls-discovery
docker build -t gls-discovery  ./discovery
```

##### Account
```
docker rm account-service
docker rmi gls-account
docker build -t gls-account  ./account
```

##### Proxy
```
docker rm proxy-service
docker rmi gls-proxy
docker build -t gls-proxy  ./proxy
```

##### Mail
```
docker rm mail-service
docker rmi gls-mail
docker build -t gls-mail  ./mail
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