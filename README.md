This project aims to validate AMQP protocol failover capability using a microservice architecture with Docker Compose, RabbitMQ, PostgreSQL and Springboot.

To validate failover capability, we will simulate a `product purchase` (products checkout) flow at one e-commerce website. A purchase represents a click at `buy button` Front-End that call some APIs representing Checkout flow, Payments Rules, Delivery Strategies, Reports and Notifications about product status.

To simulate a purchase scenarios, this project contains 4 apps running Jetty webservers and RabbitMQ clients:
- Checkout App: simulate a simple purchase process and notify event oriented architecture about it
- Delivery App: catch purchase event, and throw delivery process at distribution center
- Reports App: store info about purchase to generate business reports
- Notifications App: sent email to users about purchase status (bought, dispatched, delivered)

Use [Docker Compose](https://docs.docker.com/engine/installation/) to run all apps and third dependencies. Compose file will up 4 apps, 1 PostgreSQL database and 1 cluster of Rabbitmq AMQP servers:

```shell
# Will up
docker-compose up
```

At bottom of this page, exists and Architecture Diagram about communications with all parts of system.

**How to test and simulate failover scenarios**

Validating if everything is up and running:

```shell
for i in {1..4};
do
    curl -s "http://localhost:900$i/status"
done

# if it displays 4 lines with 'health' text, it works
```

Fine! Lets test some schenarios:

1. All works fine:
2. RabbitMQ slave die:
3. RabbitMQ master die:
4. Nothing works:

**Architecture Diagram**:

![Architecture Diagram](https://gitlab.com/guide-apps/amqp-failover-consistency/raw/master/doc/architecture-diagram.png)

Some improvements, features or some tips to this project? Send me an [e-mail](gabrielmassote@gmail.com) or message.
