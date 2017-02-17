#!/bin/bash

if ! which docker; then
    echo '[ERROR] Docker not installed.\n';
    exit 1;
fi

# After docker images builds, run acceptance tests
docker-compose -f docker-compose.yml up -d --no-recreate
docker-compose logs &
sleep 15

# Config cluster mode for slave
docker-compose exec rabbitmq_slave /bin/bash -c "rabbitmqctl stop_app; rabbitmqctl join_cluster rabbit@rabbitmq_master; rabbitmqctl start_app"
docker-compose exec rabbitmq_master rabbitmqctl set_policy ha-all "^payment\-" '{"ha-mode":"all", "ha-sync-mode":"automatic"}'

# Enable plugin to migrate messagens between queues
docker-compose exec rabbitmq_master rabbitmq-plugins enable rabbitmq_shovel rabbitmq_shovel_management

# Run acceptance tests after docker images builds, and env steup
# Better Practice: change all 4 acceptance tests for cucumber test, like:
#   docker-compose -f docker-compose.yml -f docker-compose.ci.yml run cucumber-acceptance-test
if [[ $# -eq 0 ]] ; then
    docker-compose -f docker-compose.yml -f docker-compose.ci.yml run checkout_app
    docker-compose -f docker-compose.yml -f docker-compose.ci.yml run gateway_app
    docker-compose -f docker-compose.yml -f docker-compose.ci.yml run notifications_app
    docker-compose -f docker-compose.yml -f docker-compose.ci.yml run reports_app
else
    docker-compose -f docker-compose.yml -f docker-compose.ci.yml run $1
fi

docker-compose down
