#!/bin/bash

if ! which docker; then
    echo '[ERROR] Docker not installed.\n';
    exit 1;
fi

docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml down

# This scripts was extracted to other file to allow all apps to be independent and run on IntelliJ or otherwise
./run-dev-deps.sh
./run-cluster-config.sh

# Build images
docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml build

# Run acceptance tests after docker images builds, and env steup
# Better Practice: change all 4 acceptance tests for cucumber test, like:
#   docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml run cucumber-acceptance-test
# Obs: command 'run' is used rather than 'up' because 'up' lock tty when running at TravisCI.
if [[ $# -eq 0 ]] ; then
    declare -a apps=("checkout_app" "gateway_app")

    for app in "${apps[@]}"
    do
        docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml run $app
    done
else
    docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml run $1
fi

# docker-compose -f docker-compose.base.yml -f docker-compose.ci.yml down
