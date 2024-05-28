package com.example.url_shortener.service;

import com.example.url_shortener.model.Url;
import com.example.url_shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String URL_CACHE_PREFIX = "URL_";
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = BASE62.length();

    public String shortenUrl(String originalUrl) {

        //check redis cache first
        String shortUrl = (String) redisTemplate.opsForValue().get(URL_CACHE_PREFIX + originalUrl);
        if(shortUrl == null) {
            System.out.println("not in cache");

            //check cassandra DB
            Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);

            if (!existingUrl.isPresent()) {
                //TODO: get id from snowflake
                long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
                String newShortUrl = encode(id);
                Url Url = new Url(id, originalUrl, newShortUrl);
                urlRepository.save(Url);
                redisTemplate.opsForValue().set(URL_CACHE_PREFIX + newShortUrl, originalUrl, Duration.ofMinutes(60));
                redisTemplate.opsForValue().set(URL_CACHE_PREFIX + originalUrl, newShortUrl, Duration.ofMinutes(60));
                return newShortUrl;
            }
            shortUrl = existingUrl.get().getShortUrl();
        }
        redisTemplate.opsForValue().set(URL_CACHE_PREFIX + shortUrl, originalUrl, Duration.ofMinutes(60));
        redisTemplate.opsForValue().set(URL_CACHE_PREFIX + originalUrl, shortUrl, Duration.ofMinutes(60));
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        String originalUrl = (String) redisTemplate.opsForValue().get(URL_CACHE_PREFIX + shortUrl);
        if(originalUrl == null) {
            long id = decode(shortUrl);
            originalUrl = urlRepository.findById(id).orElseThrow().getOriginalUrl();
            // store in redis cache
            redisTemplate.opsForValue().set(URL_CACHE_PREFIX + shortUrl, originalUrl, Duration.ofMinutes(60));
            redisTemplate.opsForValue().set(URL_CACHE_PREFIX + originalUrl, shortUrl, Duration.ofMinutes(60));
        }
        return originalUrl;
    }

    // Encode the unique ID to a short URL
    String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return sb.reverse().toString();
    }

    // Decode the short URL to get the unique ID
    long decode(String shortUrl) {
        long num = 0;
        for (int i = 0; i < shortUrl.length(); i++) {
            num = num * BASE + BASE62.indexOf(shortUrl.charAt(i));
        }
        return num;
    }
}
