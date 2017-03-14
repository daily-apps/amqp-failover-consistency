#!/bin/bash

if ! which docker; then
    echo '[ERROR] Docker not installed.\n';
    exit 1;
fi

# Up all cluster of rabbits, postgres, mail server and apps
docker-compose -f docker-compose.base.yml up -d
docker-compose -f docker-compose.base.yml logs &
sleep 15