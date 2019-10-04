#!/bin/bash
PROJECT_HOME="/usr/gliesereum"
cd $PROJECT_HOME
pwd
whoami

echo 'Git pull' 
sudo git pull

echo 'Gradle build'                               
sudo gradle clean build -b=account/build.gradle --no-daemon
sudo gradle clean build -b=discovery/build.gradle --no-daemon
sudo gradle clean build -b=proxy/build.gradle --no-daemon
sudo gradle clean build -b=mail/build.gradle --no-daemon
sudo gradle clean build -b=permission/build.gradle --no-daemon
sudo gradle clean build -b=karma/build.gradle --no-daemon
sudo gradle clean build -b=file/build.gradle --no-daemon
sudo gradle clean build -b=lending-gallery/build.gradle --no-daemon
#sudo gradle clean build -b=socket/build.gradle --no-daemon
sudo gradle clean build -b=notification/build.gradle --no-daemon
sudo gradle clean build -b=language/build.gradle --no-daemon
#sudo gradle clean build -b=payment/build.gradle --no-daemon

echo 'Docker stop containers'       
docker stack rm gls                 
sleep 30                            
docker rm $(docker ps -a -q) --force

echo 'Docker clean images'   
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
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-language')
#docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-payment')
#docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-curator')

echo 'Docker build images'
sudo docker build --rm -t gls-discovery:0.1.4 -f docker/discovery/Dockerfile  ./discovery/build/libs/
sudo docker build --rm -t gls-account:0.1.4 -f docker/account/Dockerfile  ./account/build/libs/
sudo docker build --rm  -t gls-proxy:0.1.4  -f docker/proxy/Dockerfile  ./proxy/build/libs/
sudo docker build --rm  -t gls-mail:0.1.4 -f docker/mail/Dockerfile  ./mail/build/libs/
sudo docker build --rm  -t gls-permission:0.1.4 -f docker/permission/Dockerfile  ./permission/build/libs/
sudo docker build --rm  -t gls-karma:0.1.4 -f docker/karma/Dockerfile  ./karma/build/libs/
sudo docker build --rm  -t gls-file:0.1.4 -f docker/file/Dockerfile  ./file/build/libs/
sudo docker build --rm  -t gls-lending-gallery:0.1.4 -f docker/lending-gallery/Dockerfile  ./lending-gallery/build/libs/
sudo docker build --rm  -t gls-notification:0.1.4 -f docker/notification/Dockerfile  ./notification/build/libs/
sudo docker build --rm  -t gls-language:0.1.4 -f docker/language/Dockerfile  ./language/build/libs/
#sudo docker build --rm  -t gls-payment:0.1.4 -f docker/payment/Dockerfile  ./payment/build/libs/
#sudo docker build --rm  -t gls-curator:0.1.4 -f docker/curator/Dockerfile  ./config/elk/

echo 'Docker deploy'                            
docker stack deploy -c docker/docker-compose-dev-log.yml gls
docker ps 