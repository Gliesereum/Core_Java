#!/bin/bash
PROJECT_HOME="/usr/gliesereum"
cd $PROJECT_HOME
pwd
whoami

echo 'Git pull' 
git pull

echo 'Gradle build'                               
sudo gradle clean build -b=account/build.gradle    
sudo gradle clean build -b=discovery/build.gradle  
sudo gradle clean build -b=proxy/build.gradle      
sudo gradle clean build -b=mail/build.gradle       
sudo gradle clean build -b=permission/build.gradle 
sudo gradle clean build -b=karma/build.gradle      
sudo gradle clean build -b=media/build.gradle

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
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-media')     
docker rmi $(docker images --format '{{.Repository}}:{{.Tag}}' | grep 'gls-curator')

echo 'Docker build images'
sudo docker build -t gls-discovery:0.0.1 -f docker/discovery/Dockerfile  .  
sudo docker build -t gls-account:0.0.1 -f docker/account/Dockerfile  .      
sudo docker build -t gls-proxy:0.0.1  -f docker/proxy/Dockerfile  .         
sudo docker build -t gls-mail:0.0.1 -f docker/mail/Dockerfile  .            
sudo docker build -t gls-permission:0.0.1 -f docker/permission/Dockerfile  .
sudo docker build -t gls-karma:0.0.1 -f docker/karma/Dockerfile  .          
sudo docker build -t gls-media:0.0.1 -f docker/media/Dockerfile  .          
sudo docker build -t gls-curator:0.0.1 -f docker/curator/Dockerfile  .   

echo 'Docker deploy'                            
docker stack deploy -c docker/docker-compose-dev-log.yml gls
docker ps 