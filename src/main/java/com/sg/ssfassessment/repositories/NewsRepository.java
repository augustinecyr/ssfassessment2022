package com.sg.ssfassessment.repositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class NewsRepository {

    @Value("${news.cache.duration}")
    private Long newsCacheTime;

    @Autowired
    @Qualifier("redislab")
    private RedisTemplate<String, String> redisTemplate;

    public void saveArticle(String categories, String payloadArticle) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        valueOp.set(categories.toUpperCase(), payloadArticle, Duration.ofMinutes(newsCacheTime));

    }

    public Optional<String> getArticle (String categories) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        String value = valueOp.get(categories.toUpperCase());
        
        if (null == value)
            return Optional.empty();
        return Optional.of(value); 
    }

}
