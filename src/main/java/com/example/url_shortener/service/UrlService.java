package com.example.url_shortener.service;

import com.example.url_shortener.model.Url;
import com.example.url_shortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    private static final String URL_CACHE_PREFIX = "URL_";
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = BASE62.length();

    public String shortenUrl(String originalUrl) {
        if(urlRepository.existsByOriginalUrl(originalUrl)) {
            String shortUrl = urlRepository.findByOriginalUrl(originalUrl).orElseThrow().getShortUrl();
            return shortUrl;
        }
        //TODO: get id from snowflake
        long id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        String shortUrl = encode(id);
        Url Url = new Url(id, originalUrl, shortUrl);
        urlRepository.save(Url);
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        long id = decode(shortUrl);
        String originalUrl = urlRepository.findById(id).orElseThrow().getOriginalUrl();

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
