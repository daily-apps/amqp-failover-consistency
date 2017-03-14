#!/bin/bash

if ! which docker; then
    echo '[ERROR] Docker not installed.\n';
    exit 1;
fi

docker-compose -f docker-compose.base.yml -f docker-compose.dev.yml down

# This scripts was extracted to other file to allow all apps to be independent and run on IntelliJ or otherwise
./run-dev-deps.sh
./run-cluster-config.sh

# Build images
docker-compose -f docker-compose.base.yml -f docker-compose.dev.yml build

if [[ $# -eq 0 ]] ; then
    docker-compose -f docker-compose.base.yml -f docker-compose.dev.yml up --no-recreate
else
    docker-compose -f docker-compose.base.yml -f docker-compose.dev.yml up --no-recreate $1
fi
