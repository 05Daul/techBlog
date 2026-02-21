package daulspring.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
  // RedisConnectionFactory 은 yml파일을 읽고, 호스트(host)와 포트(port) 정보를 현재 파라미터에 넣어줌.


  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
    // 핵심 : Redis는 데이터를 바이트 배열로 저장
    // 현재는 유저: 토큰 -> String, String
    RedisTemplate<String, String> template = new RedisTemplate<>();

    template.setConnectionFactory(connectionFactory);

    template.setKeySerializer(new StringRedisSerializer()); // Key를 저장할 때 일반 문자열로 변환.
    template.setValueSerializer(new StringRedisSerializer()); // Val를 저장할 때 일반 문자열로 변환.


    return template;
  }
}
