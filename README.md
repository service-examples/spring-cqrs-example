# Product Service (CQRS)

[![Build Status](https://travis-ci.org/service-examples/spring-cqrs-example.svg?branch=master)](https://travis-ci.org/service-examples/spring-cqrs-example)

## Building the project

For building the raw jar file artifact, run:

```bash
./gradlew clean build
```

Once you have the artifact you can now build the docker image using:

```bash
./gradlew docker
```

The above task expects docker to be present and installed on the host machine.