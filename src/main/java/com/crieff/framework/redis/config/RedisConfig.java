package com.crieff.framework.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @description:
 * @author: aKuang
 * @time: 10/25/22 4:06 PM
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//        //我们为了开发方便，直接使用<String, Object>类型
//        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//        /**
//         * 1. 方式一：创建Json的序列化方式
//         *      -->配置Redis的序列化解析方式是Json
//         *      -->即：通过Json去解析任何传进来的实体类对象，
//         *      而不是使用原来的JDK去解析。
//         */
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        //1.1 Json的序列化方式，我们需要通过ObjectMapper进行转义
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        //1.2 转义完就可以使用Json的序列化方式了
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        /**
//         * 2.方式二：创建String类型的序列化方式
//         *         -->配置Redis的序列化解析方式是Json
//         *         -->即：通过Json去解析任何传进来的实体类对象，
//         *         而不是使用原来的JDK去解析。
//         */
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        /**
//         * 3. 上面创建好两种序列化方式后，将两种序列化方式
//         * 分别应用到(set)Redis的不同数据类型里面去使用
//         *
//         */
//        template.setConnectionFactory(factory);
//        // 所有String类型的key都采用String的序列化方式
//        template.setKeySerializer(stringRedisSerializer);
//        // 所有hash类型的key也采用String的序列化方式
//        template.setHashKeySerializer(stringRedisSerializer);
//        // 所有的value类型的序列化方式采用jackson
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        // 如果是hash的value序列化方式采用jackson
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        //上面设置好每一种类型对应的序列化方式后，将所有的properties设置set进去。
//        template.afterPropertiesSet();
//        return template;

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Key 序列化：String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value 序列化：JSON
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

}
