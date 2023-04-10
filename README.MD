# Java Template Service

This is a simple java service that contains a few implementations of the most common behaviours that exist in our services.

## Motivation

The main objective of this project is to provide a template that contains the minimum requirements to spin up a java service with less effort. 

## Architecture 

Please, grab a coffee and read the chosen [architecture](ARCHITECTURE.MD) for this project.

## Functionalities and Technologies

- Rest endpoints (Vertx + OpenApi 3)
- Stream messages reading and writing (Kafka)
- Logs (Log4j / Slf4j)
- Metrics (Micrometer / Prometheus)
- Config (Typesafe)
- DI (Guice)
- Tests (Junit5 + Mockito)
- Test coverage (Jacoco)

## How to add or remove functionalities

### Dependencies
Take a look at the [dependencies](dependencies_versions.gradle) file and add or remove the necessary library.
Try to keep an organized list by the implementation/tests and alphabetical order (Intellij : Edit -> Sort lines).

### Modules
For each technology that needs to implement an in/out port or provide some infrastructure needs a module to kept separated
from the business logic.

The current modules are:

1. [application-main](application-main) 
2. [business-logic](business-logic)
3. [guice](guice)
4. [kafka](kafka)
5. [micrometer](micrometer)
6. [vertx](vertx)

Each module has its own dependencies.gradle file which contains only the dependencies needed for that module. 
For more info about gradle multiple module support [read this doc](https://docs.gradle.org/current/userguide/multi_project_builds.html)

### Creating Use Cases

1. Every UseCase needs an interface to be declared, with tha said, go to [input ports dir](business-logic/src/main/java/com/viafoura/template/microservice/business/port/input) and create a new interface.
2. If the interface needs domain objects, please create them under [this structure](business-logic/src/main/java/com/viafoura/template/microservice/business/domain)
3. Implement the UseCase under the [service package](business-logic/src/main/java/com/viafoura/template/microservice/business/service).
4. Go to the [UseCase Guice Module file](guice/src/main/java/com/viafoura/template/microservice/di/module/UseCaseModule.java) to point the implementation 
5. If the service requires external needs you might need to add output ports and use them.

### Adding rest endpoints

1. Change the [api spec file](vertx/src/main/resources/api-spec.yaml) under the vertx module according to your needs.
2. Go to the [api package](vertx/src/main/java/com/viafoura/template/microservice/vertx/api) and create the interface with the actions needed.
3. If you are adding an endpoint that requires requests or response objects you might need to create a new package under vertx/api dir.
4. Change the [EventBusType class](vertx/src/main/java/com/viafoura/template/microservice/vertx/event/EventBusType.java) to add the newer operations.
5. Go to the [event package](vertx/src/main/java/com/viafoura/template/microservice/vertx/event/consumer) and implement your api consumer. 
6. Don't forget to add your consumer as implementation in the [guice module](guice/src/main/java/com/viafoura/template/microservice/di/module/VertxModule.java)
7. The Api interface created at the 2nd step needs to be implemented inside the [adapter web dir](vertx/src/main/java/com/viafoura/template/microservice/vertx/adapter)
8. Obviously, the above adapter needs to call an [useCase class](#Creating Use Cases) and be tested.

### Kafka events 

The [reference.conf](vertx/src/main/resources/reference.conf) file covers lists of incoming and outgoing topics under 
the `kafka.topic.incoming` and `kafka.topic.outgoing` properties respectively.

#### Consumption

The events consumption are already being made by [EventTopologySupplier](kafka/src/main/java/com/viafoura/template/microservice/stream/runner/consumer/EventTopologySupplier.java) and [EventsConsumerAdapter](kafka/src/main/java/com/viafoura/template/microservice/stream/adapter/EventsConsumerAdapter.java) classes.
Maybe you only need to change EventsConsumerAdapter logic to call different use cases. But this use-case is enough to consume all topics

#### Publishing

Publishing is also being done by [EventPublisher](kafka/src/main/java/com/viafoura/template/microservice/stream/runner/producer/EventPublisher.java) and [EventsConsumerAdapter](kafka/src/main/java/com/viafoura/template/microservice/stream/adapter/MessagePublisherAdapter.java) classes.
Change the output port and implementation according to your needs.

### Output Ports - External needs

Every external call must be implemented by creating its port and then the output adapter into the corresponding module as mentioned on [modules](#Modules) section.

1. Create the port interface under the [output port dir](business-logic/src/main/java/com/viafoura/template/microservice/business/port/output)
2. Then implement the port interface under the adapter module.
3. Don't forget to tell guice about the implementation in the [AdapterModule file](guice/src/main/java/com/viafoura/template/microservice/di/module/AdapterModule.java).

### Config

1. We use TypesafeConfig library to load the settings, if a module needs to read external configs be sure to add the key/value 
within the vertx [reference.conf](vertx/src/main/resources/reference.conf) file
2. Create an interface called `{Module-name}Config.java` under the config dir placed in the new module.
3. Then, create the above interface implementation class receiving Config as input.
4. Finally, tell [guice](guice) about the new implementation at your module creating or using an existing Guice module class.

See [kafka module](kafka/src/main/java/com/viafoura/template/microservice/stream/config) as an example

### Metrics

Application metrics output adapter is already provided by [this class](micrometer/src/main/java/com/viafoura/template/microservice/metrics/adapter/ServiceMetricsAdapter.java).
You only need to add the custom names into [MetricNames class](business-logic/src/main/java/com/viafoura/template/microservice/business/infrastructure/metric/MetricNames.java) 
and add the functions inside [the port](business-logic/src/main/java/com/viafoura/template/microservice/business/port/output/metric/ApplicationMetricsPort.java).

Finally, you'll be able to use the port and its functions in all services you need. 

### Removing functionalities

Feel free to change your project by removing unnecessary code from this structure, remember to decouple from guice,
then remove the unused libraries and compile the project.

## Building/Running

To run the service locally, install docker and first authenticate to our AWS container service

```
aws --profile {DEV_OR_TEST_PROFILE} ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 218894879100.dkr.ecr.us-east-1.amazonaws.com
```

In order to run a container built from the source code, execute the following:
1. Build. 
   Jar and fat-jar will be generated under build/libs folder
```
./gradlew clean build
```

2. Running Locally
    1. Using docker-compose.dependencies.yaml and running from IDE (Intellij)
       After running this command you should start the main verticle from the IDE.
   ```
   docker-compose -f docker-compose.dependencies.yaml up --build
   ```
   Note: When running from IDE,  Create a new _Run/Debug Configuration_ in IntelliJ with `com.viafoura.template.microservice.Main` as Main class and add the following VM options:
   ```
   -Dconfig.override_with_env_vars=true -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory
   ```

    2. Using docker-compose.local.yaml - simplest way
   ```
   VERSION=* docker-compose -f docker-compose.local.yaml up --build
   ```


## Tests and Coverage

### Unit tests

Nothing new at the unit test scenarios unless the coverage criteria we have when using JaCoCo test coverage plugin. 
So pay attention to the rates and packages that don't need to be covered by tests.

To run both integration and load tests you need to [build the application locally](#buildingrunning) first.

### Integration tests

We have 2 flavors for integration tests (Vertx execution and Postman collections). Different projects requires different approaches. 
Keep in mind that would be great having IT running on CI. With that said, choosing between then will help the team with the quality stages. 

#### Vertx

The Vertx Integration tests are located [here](application-main/src/intTest). Write your tests and use Gradle Wrapper to run them:

`./gradlew integrationTests`

#### Postman 

Postman/Newman was adopted to serve as rest integration tests. [There is a sample collection file](docker/postman/postman_collection.json) which accepts variables and can be used by running:

`VERSION=* docker-compose -f docker-compose.test.postman.yml up --build --abort-on-container-exit | grep postmanTests`

### Load tests

K6 implements intelligent load tests for this project. [The sample file](docker/k6/healthy.js) covers the Healthy endpoint, and you can run the test running:

`VERSION=* docker-compose -f docker-compose.test.k6.yml up --build --abort-on-container-exit | grep k6Tests`

## Misc

### Testing kafka messages consumer-producer
   
1. Run the local app [throughout docker-compose.local](#buildingrunning).
2. Open another terminal and type: `docker-compose -f docker-compose.local.yml exec kafka bash`. It will allow you to enter bash into kafka docker image
3. In the opened terminal type: `$KAFKA_HOME/bin/kafka-console-producer.sh --topic {TOPIC_NAME} --broker-list localhost:9092`. 
   Where {TOPIC_NAME} is the name of the topic you want to send messages
4. Send a message to the above topic e.g. ```{"hello":"viafoura"}```
5. If the consumption was succeeded you will see in the logs: _"Consuming event with key: 'null' and value: '{hello=viafoura}'"_
6. To check consumptions, open another terminal and type `docker-compose -f docker-compose.local.yml exec kafka bash`
7. Then log the consumption for the output topic `$KAFKA_HOME/bin/kafka-console-producer.sh --topic {TOPIC_NAME} --broker-list localhost:9092`
   Where {TOPIC_NAME} is the topic you want to track
8. With the template scenario, the ouput topics might return as ```{"values":{"hello":"viafoura","verified":true}}```

_Sometimes kafka on docker fails when managing its volumes, if it happens, try to use `docker system prune` in order to clean your entire docker env._ 

## What else can we add here

Some improvements/changes are welcome in this project such as:

- New naming conventions
- New adapters implementations (Database/jooq)
- Security layer for vertx
- Minikube running
- CircuitBreaker / fault tolerance  

Just keep in mind that this is a template service, we must try to keep the minimal and most used functionalities to serve as a quick startup way for a new java service.

## Code that inspired this work

- [polls-service](https://github.com/viafoura/polls-service) - This was our first service using Ports+Adapter, and we had good experiences such as the easy way to maintain and test. You can find real good tests there using TestContainers as well.
- [realtime-event-feed-service](https://github.com/viafoura/realtime-event-feed-service) - REF applied a good architecture especially taking the infrastructure code apart from the business-logic.
