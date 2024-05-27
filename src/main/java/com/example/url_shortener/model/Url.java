package com.example.url_shortener.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Url {

    @PrimaryKey
    private long id;
    private String originalUrl;
    private String shortUrl;

}
