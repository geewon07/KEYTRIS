#!/bin/bash

# 파일의 경로
FILE_PATH=$(realpath $0)

# 해당 경로의 디렉토리 이름 추출
DIR_NAME=$(dirname "$FILE_PATH")

# 디렉토리의 마지막 부분만 추출
FOLDER_NAME=$(basename "$DIR_NAME")
 
# Information 
SERVICE_NAME=keytris
PART_NAME=$FOLDER_NAME
# TAG=$(date +%Y%m%d%H%M%S)
TAG=latest

# Docker login
docker info 2>/dev/null | grep "Username:" > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo "### Docker not logged or not installed."

    # Docker 설치 확인
    if ! command -v docker &> /dev/null; then 
        echo "### Docker not found! Installing..."
        sudo apt-get update && sudo apt install -y docker.io

        # Docker Service start!
        sudo systemctl start docker
        sudo systemctl enable docker

        echo "### Docker installed successfully."
    else 
        echo "### Docker is installed but not logged in."
        cat /configure/docker/my_password.txt | sudo docker login --username yimo22 --password-stdin
    fi
else 
    echo "### Docker already logged in."
fi

# Image Build
USER_NAME=$(sudo docker info 2>/dev/null | grep "Username:" | awk '{print $2}')
IMAGE_INFO="$SERVICE_NAME-$PART_NAME:$TAG"
echo "### Image Building Start - $IMAGE_INFO"
docker build -t $IMAGE_INFO .
echo "### Image Build Completed - $IMAGE_INFO"

# Image Tagging
echo "### Image Tagging Start"
UPLOAD_IMAGE="$USER_NAME/$IMAGE_INFO"
docker tag $IMAGE_INFO $UPLOAD_IMAGE
echo "### Image Tagging Completed - $UPLOAD_IMAGE"

# Image pushing
echo "### Image Pushing : $UPLOAD_IMAGE "
docker push $UPLOAD_IMAGE
echo "### Image Pushing Completed - $UPLOAD_IMAGE"

