# Gliesereum platform update new Web3 version STELS

### Build jar's

This is a single Gradle multi-project build, driven through the wrapper — no
system Gradle needed, and the Gradle version is pinned by the repo.

```
./gradlew clean build
```

Jars land in `<module>/build/libs/`, which is what the Dockerfiles read.
To build one service and what it depends on:

```
./gradlew :karma:build
```

All third-party versions live in `gradle.properties`; the plugins, the Spring
Cloud BOM and the lombok wiring live in the root `build.gradle`. Modules
declare only their own dependencies.

### Build images

##### Clean docker

``` 

docker stack rm gls 

docker rm $(docker ps -a -q) --force

docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-discovery') 
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-account')   
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-proxy')     
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-mail')      
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-permission')
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-karma')     
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-file')
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-lending-gallery')
#docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-socket')
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-notification')
#docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-payment')
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-curator')
```

##### Build images
```
sudo docker build --rm -t gls-discovery:0.1.4 -f docker/discovery/Dockerfile  ./discovery/build/libs/
sudo docker build --rm -t gls-account:0.1.4 -f docker/account/Dockerfile  ./account/build/libs/
sudo docker build --rm  -t gls-proxy:0.1.4  -f docker/proxy/Dockerfile  ./proxy/build/libs/
sudo docker build --rm  -t gls-mail:0.1.4 -f docker/mail/Dockerfile  ./mail/build/libs/
sudo docker build --rm  -t gls-permission:0.1.4 -f docker/permission/Dockerfile  ./permission/build/libs/
sudo docker build --rm  -t gls-karma:0.1.4 -f docker/karma/Dockerfile  ./karma/build/libs/
sudo docker build --rm  -t gls-file:0.1.4 -f docker/file/Dockerfile  ./file/build/libs/
sudo docker build --rm  -t gls-lending-gallery:0.1.4 -f docker/lending-gallery/Dockerfile  ./lending-gallery/build/libs/
sudo docker build --rm  -t gls-notification:0.1.4 -f docker/notification/Dockerfile  ./notification/build/libs/
#sudo docker build --rm  -t gls-payment:0.1.4 -f docker/payment/Dockerfile  ./payment/build/libs/
sudo docker build --rm  -t gls-curator:0.1.4 -f docker/curator/Dockerfile  ./config/elk/
```

### Run docker stack
```
docker stack deploy -c docker/docker-compose-prod-log.yml gls
```
