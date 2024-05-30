# URL Shortener implmentation

## Overview
Frontend: React<br>
Backend: Spring Boot<br>
Cache: Redis<br>
Database: Cassandra<br>

## How to run
Pull Cassandra, Redis docker images and Run in docker
```
Cassandra

docker run --name cassandra -d -p 9042:9042 cassandra:latest
docker exec -it cassandra cqlsh
-- Create the keyspace
CREATE KEYSPACE IF NOT EXISTS url_shortener WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
-- Use the keyspace
USE url_shortener;

-- Create the url table
CREATE TABLE IF NOT EXISTS url (
    id bigint PRIMARY KEY,
    originalUrl text,
    shortUrl text
);

-- add index to original URL for duplicate check
CREATE INDEX ON url_shortener.url (originalUrl);
```
```
Redis
docker run -d --name=redis -p 6379:6379 redis
```


Build jar file in backend root folder
```
mvn clean install
```

Run docker compose command
```
docker-compose up --build
```

Frontend will run on localhost:3030
Backend will run on localhost:8080

