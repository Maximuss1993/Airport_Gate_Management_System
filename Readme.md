# **Airport Gate Management System**

> **Description of the task:**
> 
> Create a REST service that manages available gates for planes to park at an airport. 
> The REST service should accept a flight number and return the assigned gate. 
> The service appropriate response should be returned from the REST service.

> **Technical Requirements**
> - The gates and flights should be managed in a relational database.
>- Any database errors should be handled gracefully.
> - The solution should use spring and hibernate frameworks.
> - It should be thread safe and able to handle multiple concurrent requests.
> - The code should be written so that it is as simple as possible for 
> administrators and other developers to follow and debug any issues.

> Extra Challenge
*Update the solution so that certain gates are only available between certain times. If a
request for a gate arrives to the REST service at a specific time, the service should check
what gates are available at the current time as well as whether they are currently
in use.*

## Technical Information
- **Framework**: Spring Boot 3
- **JVM**: Java 17
- **Database**: PostgreSQL
- **Protocol**: REST API
- **Authentication**: (if applicable) JWT, OAuth 2.0, Basic Authentication 
//?? Da li ubaciti autentifikaciju?

## Features
> List the key features of the application.
- Search for flights by destination, date, and time
- Book flights with confirmation
- View booking history
- Admin panel for managing flights
//PROVERI SVE!

## Installation and Setup

### Prerequisites
> Before setting up the application, ensure you have the following installed:
- Maven
- Docker
- Docker Compose

### Cloning the Repository

First, clone the repository from GitHub:
> `git clone https://github.com/Maximuss1993/Airport_Gate_Management_System.git`

Maven command for building app in local with Jib:
> `mvn compile jib:dockerBuild`

Command for running local composed docker image:
> `docker-compose up`

Command for removing composed docker image:
> `docker-compose down`