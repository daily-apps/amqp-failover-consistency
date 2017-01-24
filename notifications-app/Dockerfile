FROM registry.gitlab.com/guide-apps/amqp-failover-consistency:spring-builds-jdk8

COPY . /app
RUN mvn clean package

CMD ['java', '-jar', 'target/notifications-app.jar']
