package com.example.url_shortener.repository;


import com.example.url_shortener.model.Url;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends CassandraRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
}

