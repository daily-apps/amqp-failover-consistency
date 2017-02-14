# ROUTES

[CRUD] /authorize
CRUD operations for Authorizations Model/Entity

[CRUD] /capture
CRUD operations for Captures Model/Entity

# REQUIREMENTS

1. Test units, integration and end-to-end
2. Env variables to be prepared to docker
3. JMX runner to simulate authorization and capture scenarios

# BUILDS

mvn -Dtest=**.acceptance.**.*Test clean package -Drun.profiles=ci
mvn -Dtest=**.integration.**.*Test clean package -Drun.profiles=test
mvn -Dtest=**.unit.**.*Test clean package -Drun.profiles=test