#!/bin/bash
SERVICE_NAME=keytris
PART_NAME=frontend
TAG=$(date +%Y%m%d%H%M%S)

# Docker login
sudo docker info 2>/dev/null | grep "Username:" > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "Docker not logged or not installed."

    # Docker 설치 확인
    if ! command -v docker &> /dev/null; then 
        echo "Docker not found! Installing..."
        sudo apt-get update && install -y docker.io

        # Docker Service start!
        sudo systemctl start docker
        sudo systemctl enable docker

        echo "Docker installed successfully."
    else 
        echo "Docker is installed but not logged in."
        cat ~/configure/docker/my_password.txt | sudo docker login --username yimo22 --password-stdin
    fi
else 
    echo "Docker already logged in."
fi
# Image Build
USER_NAME= sudo docker info 2>/dev/null | grep "Username:" | awk '{print $2}'
sudo docker build -t $USER_NAME/$SERVICE_NAME_$PART_NAME:$TAG .

# Image push
sudo docker push $USER_NAME/$SERVICE_NAME_$PART_NAME:$TAG