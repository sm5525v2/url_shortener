# URL Shortener Implementation

## Overview
- **Frontend:** React
- **Backend:** Spring Boot
- **Cache:** Redis
- **Database:** Cassandra

## How to Run

### Step 1: Set Up Cassandra and Redis Using Docker

1. **Cassandra:**

    Pull the latest Cassandra Docker image and run it:
    ```sh
    docker run --name cassandra -d -p 9042:9042 cassandra:latest
    docker exec -it cassandra cqlsh
    ```

    Create the keyspace and table:
    ```sql
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

    -- Add index to originalUrl for duplicate check
    CREATE INDEX ON url_shortener.url (originalUrl);
    ```

2. **Redis:**

    Pull the latest Redis Docker image and run it:
    ```sh
    docker run -d --name=redis -p 6379:6379 redis
    ```

### Step 2: Build the Backend

Navigate to the backend root folder and build the jar file:
```sh
mvn clean install
```
### Step 3: Run the Application

Use Docker Compose to build and run the application:

```sh
docker-compose up --build
```

### Step 4: Run Kafka

1. **Run Kafka:**
Use Docker Compose to build and run the application:

```sh
cd kafka
docker-compose up --build
```
2. **Create Kafka Topic:**

```sh
docker exec -it "kafka_container" /bin/sh
cd /opt/kafka/bin
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic url_received
```

### Access the Application
- Frontend: http://localhost:3030
- Backend: http://localhost:8080<br>
Your URL Shortener application should now be up and running!

