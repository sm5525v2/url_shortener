package com.example.url_shortener.config;


import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;

@Configuration
public class CassandraConfig {
    public @Bean CqlSession session() {
        return CqlSession.builder().withKeyspace(keyspaceName).build();
    }

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspaceName;

    @Value("${spring.data.cassandra.local-datacenter}")
    private String datacenter;

    @Bean
    public String initializeSchema(CqlSession session) {
        session.execute("CREATE TABLE IF NOT EXISTS url (id bigint PRIMARY KEY, originalUrl text, shortUrl text);");
        // Add more schema initialization statements if needed
        return "Schema initialized successfully";
    }
}
