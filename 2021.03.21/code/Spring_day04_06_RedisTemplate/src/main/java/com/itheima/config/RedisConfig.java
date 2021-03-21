package com.itheima.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@PropertySource("redis.properties")
public class RedisConfig {

    @Value("${redis.host}")
    private String hostName;

    @Value("${redis.port}")
    private Integer port;

//    @Value("${redis.password}")
//    private String password;

    @Value("${redis.maxActive}")
    private Integer maxActive;
    @Value("${redis.minIdle}")
    private Integer minIdle;
    @Value("${redis.maxIdle}")
    private Integer maxIdle;
    @Value("${redis.maxWait}")
    private Integer maxWait;



    @Bean
    //配置RedisTemplate
    public RedisTemplate createRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        //1.创建对象
        RedisTemplate redisTemplate = new RedisTemplate();
        //2.设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //3.设置redis生成的key的序列化器，对key编码进行处理
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        //4.返回
        return redisTemplate;
    }

    @Bean
    //配置Redis连接工厂
    public RedisConnectionFactory createRedisConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration,GenericObjectPoolConfig genericObjectPoolConfig){
        //1.创建配置构建器，它是基于池的思想管理Jedis连接的
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder builder = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        //2.设置池的配置信息对象
        builder.poolConfig(genericObjectPoolConfig);
        //3.创建Jedis连接工厂
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,builder.build());
        //4.返回
        return jedisConnectionFactory;
    }

    @Bean
    //配置spring提供的Redis连接池信息
    public GenericObjectPoolConfig createGenericObjectPoolConfig(){
        //1.创建Jedis连接池的配置对象
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        //2.设置连接池信息
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);
        //3.返回
        return genericObjectPoolConfig;
    }


    @Bean
    //配置Redis标准连接配置对象
    public RedisStandaloneConfiguration createRedisStandaloneConfiguration(){
        //1.创建Redis服务器配置信息对象
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        //2.设置Redis服务器地址，端口和密码（如果有密码的话）
        redisStandaloneConfiguration.setHostName(hostName);
        redisStandaloneConfiguration.setPort(port);
//        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        //3.返回
        return redisStandaloneConfiguration;
    }

}