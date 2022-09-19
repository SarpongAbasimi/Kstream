## Project

- This is an http server that accepts greetings from users.
- The greeting are then published to a source topic using Kafka
- The data from the source topic will then be processed by a kafka streams app


## Stack

- Http4s
- Cats-Effects
- Kafka
- Fs2 Streams
- Cats
- Ciris

# Note
- There are no unit tests in this project.


# How to Start Server

- `docker compose up` to start containers
- sbt `run` to start server

