package com.springReact.DocumentManager.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;


@Configuration
public class CacheConfig {

    @Bean(name = "userLoginCache")
    public CacheStore<String,Integer> userCache()
    {
        //The cache will be available only for 900 secs /15 mins.
        return new CacheStore<>(900, TimeUnit.SECONDS);
    }

    @Bean(name = "registrationCache")
    public CacheStore<String,Integer> anotherCache()
    {
        return new CacheStore<>(900, TimeUnit.SECONDS);
    }
}



















