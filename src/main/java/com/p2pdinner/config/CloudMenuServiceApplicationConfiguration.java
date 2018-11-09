package com.p2pdinner.config;

import javax.sql.DataSource;

import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by rajaniy on 6/25/16.
 */
@Configuration
@Profile("cloud")
public class CloudMenuServiceApplicationConfiguration {
    @Bean
    public Cloud cloud() {
        return new CloudFactory().getCloud();
    }

    @Bean
    public DataSource dataSource(){
        return cloud().getSingletonServiceConnector(DataSource.class, null);
    }


    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        ServiceInfo serviceInfo = cloud().getServiceInfo("REDIS");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        if (serviceInfo instanceof RedisServiceInfo) {
            RedisServiceInfo redisServiceInfo = (RedisServiceInfo) serviceInfo;
            jedisConnectionFactory.setHostName(redisServiceInfo.getHost());
            jedisConnectionFactory.setPassword(redisServiceInfo.getPassword());
            jedisConnectionFactory.setPort(redisServiceInfo.getPort());
            jedisConnectionFactory.setPoolConfig(poolConfig);
            jedisConnectionFactory.setUsePool(true);
        }
        return jedisConnectionFactory;
    }
}
