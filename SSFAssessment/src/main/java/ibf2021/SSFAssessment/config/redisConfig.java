package ibf2021.SSFAssessment.config;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class redisConfig {

    private final Logger logger = Logger.getLogger(redisConfig.class.getName());

    @Autowired
    Environment env;

    @Bean("Redis_Config")
    public RedisTemplate<String, Object> createRedisTemplate() {
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setDatabase(Integer.parseInt(env.getProperty("spring.redis.database")));
        config.setHostName(env.getProperty("spring.redis.host"));
        config.setPort(Integer.parseInt(env.getProperty("spring.redis.port")));

        final String redisPW = System.getenv("REDIS_PASSWORD");
        if (redisPW != null) {
            logger.info("Redis PW exists in Sys Environment and we are now setting redisPW");
            config.setPassword(redisPW);
        }
        final JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

        final JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisFac);
        template.setKeySerializer(new StringRedisSerializer());
        RedisSerializer<Object> serialiser = new JdkSerializationRedisSerializer(getClass().getClassLoader());
        template.setValueSerializer(serialiser);

        return template;
    }

}
