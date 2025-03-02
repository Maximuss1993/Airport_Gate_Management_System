Command for building app in local with Jib:
mvn compile jib:dockerBuild

Command for building app in DockerHub with Jib:
mvn compile jib:build

Command for running local composed docker image:
docker-compose up

Command for removing composed docker image:
docker-compose down

