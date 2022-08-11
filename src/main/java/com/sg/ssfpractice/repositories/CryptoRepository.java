package com.sg.ssfpractice.repositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class CryptoRepository {

    @Value("${crypto.cache.duration}")
    private Long cacheTime; // last price check will last for 1 minute.

    @Autowired
    @Qualifier("redislab")
    private RedisTemplate<String, String> redisTemplate;

    public void save(String coin, String currency, String payload ) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        valueOp.set(coin.toUpperCase(), payload, Duration.ofMinutes(cacheTime));

    }
    
    public Optional<String> get (String coin, String currency) {
        ValueOperations<String, String> valueOp = redisTemplate.opsForValue();
        String value = valueOp.get(coin.toUpperCase());
        
        if (null == value)
            return Optional.empty();
        return Optional.of(value); 
    
    }
}
