# Airport Gate Management System

## Technical Information
- **Framework**: Spring Boot 3
- **JVM**: Java 17
- **Database**: PostgreSQL
- **Protocol**: REST API
- **Authentication**: (if applicable) JWT, OAuth 2.0, Basic Authentication

## Features

List the key features of the application. For example:
- Search for flights by destination, date, and time
- Book flights with confirmation
- View booking history
- Admin panel for managing flights

## Installation and Setup

### Prerequisites

Before setting up the application, ensure you have the following installed:

- Java 11+
- Maven
- Docker
- Docker Compose
- PostgreSQL (if not using Docker Compose)

### Cloning the Repository

First, clone the repository from GitHub:
git clone https://github.com/Maximuss1993/Airport_Gate_Management_System.git

Maven command for building app in local with Jib:
mvn compile jib:dockerBuild

Command for running local composed docker image:
docker-compose up

Command for removing composed docker image:
docker-compose down

