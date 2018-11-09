package com.p2pdinner.config;

import com.netflix.discovery.converters.Auto;
import com.p2pdinner.domain.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by rajaniy on 1/30/17.
 */
@Configuration
public class RedisConfiguration {

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public Jackson2JsonRedisSerializer<AccessToken> jackson2JsonRedisSerializer() {
        return new Jackson2JsonRedisSerializer<AccessToken>(AccessToken.class);
    }

    @Bean(name = "accessTokenTemplate")
    public RedisTemplate<String, AccessToken> accessTokenTemplate() {
        RedisTemplate<String,AccessToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate<String,AccessToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
        return redisTemplate;
    }
}
