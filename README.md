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

## Performance Testing and Improvements

To ensure the reliability and performance of our URL Shortener Service, I conducted extensive performance testing using JMeter.

### Initial Testing

I tested the URL shortening functionality with 10,000 requests / sec using JMeter. The results showed that approximately 7% of the requests resulted in errors. These errors were primarily due to the response time of Cassandra, the primary database.

### Performance Issue

- **Issue:** Approximately 7% error rate during 10,000 requests / sec performance test.
- **Root Cause:** Cassandra response time.

### Solution: Redis Cache Implementation

To mitigate the performance issues, I implemented Redis as a caching layer. This cache stores the frequently accessed data, significantly reducing the load on Cassandra and improving the response times.

### Results After Implementing Redis Cache

After applying Redis as a caching solution, I re-ran the performance tests. The results were impressive:

- **Error Rate:** 0% during the 10,000 request performance test.
- **Conclusion:** Implementing Redis cache eliminated the errors caused by Cassandra's response times.


