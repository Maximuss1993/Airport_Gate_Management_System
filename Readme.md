# **Airport Gate Management System**

> **Description of the task:**
>
> Create a REST service that manages available gates for planes to park at an
> airport. The REST service should accept a flight number and return the
> assigned gate. The service appropriate response should be returned from
> the REST service.

> **Technical Requirements**
> - The gates and flights should be managed in a relational database.
> - Any database errors should be handled gracefully.
> - The solution should use spring and hibernate frameworks.
> - It should be thread safe and able to handle multiple concurrent requests.
> - The code should be written so that it is as simple as possible for
    > administrators and other developers to follow and debug any issues.

> Extra Challenge
> - Update the solution so that certain gates are only available between certain
    times. If a request for a gate arrives to the REST service at a specific
    time,
    the service should check what gates are available at the current time as
    well
    as whether they are currently in use.

## Technical Information

- **Framework**: Spring Boot 3
- **JVM**: Java 17
- **Database**: PostgreSQL
- **ORM framework**: Hibernate
- **Protocol**: REST API
- **Authentication**: JWT
- **Containerization platform**: Docker
- **Building Docker image tool**: Jib
- **API tool**: Swagger
- **Testing frameworks**: JUnit, Mockito

## Features

- Three models are implemented in the application:

1. Airport,
2. Flight and
3. Gate.

> For all entities you can CREATE, UPDATE, DELETE or SEARCH by different
> parameters.

> The main function for accepting a flight number and returning the assigned
> gate is in the **GateController** class.

## Package by Feature

- In this project structure, packages contain all classes that are required
  for a feature.
- The use of a class by a class in another package is eliminated with this
  structure. Also, the classes within the packages are closely related to each
  other. Thus, there is high cohesion within packages and low coupling between
  packages. In addition, this structure provides higher modularity.

> Advantages of Package by Feature:
> - Package by Feature has packages with high cohesion, low
    coupling and high modularity.
> - Package by Feature allows some classes to set their access modifier
    package-private instead of public, so it increases encapsulation. On the
    other
    hand, Package by Layer forces you to set nearly all classes public.
> - Package by Feature reduces the need to navigate between packages since all
    classes needed for a feature are in the same package.
> - Package by Feature is like microservice architecture. Each package is
    limited to classes related to a particular feature. On the other hand,
    Package
    By Layer is monolithic. As an application grows in size, the number of
    classes
    in each package will increase without bound.
*****

## Installation and Setup

### Prerequisites

Before setting up the application, ensure you have the following installed:

- Maven
- Docker
- Docker Compose

### Cloning the Repository

First, clone the repository from GitHub:
> `git clone https://github.com/Maximuss1993/Airport_Gate_Management_System.git`

Maven command for building app in local with Jib:
> `mvn compile jib:dockerBuild`

Command for running composed docker image in local (on Docker Daemon):
> `docker-compose up`

The **Swagger UI** page will then be available at

- http://server:port/context-path/swagger-ui.html

and the OpenAPI description will be available at the following

- url for json format: http://server:port/context-path/v3/api-docs

> - server: The server name or IP
> - port: The server port
> - context-path: The context path of the application

You can change port in the application.properties file.

Default port in this application is 8181:
> `http://localhost:8181/swagger-ui/index.html`

On the end you can use the fallowing command to delete all docker images:
> `docker-compose down`