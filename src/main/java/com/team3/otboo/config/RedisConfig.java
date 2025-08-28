package com.team3.otboo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team3.otboo.domain.dm.service.SubscribeService;
import com.team3.otboo.domain.notification.service.RedisSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


	@Value("${spring.data.redis.host}")
	private String host;
	@Value("${spring.data.redis.port}")
	private int port;

	@Bean
	@Qualifier("chatPubSub")
	public RedisConnectionFactory chatPubSubFactory() {
		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
		configuration.setHostName(host);
		configuration.setPort(port);

		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(configuration);
		// Pub/Sub 과 일반 명령어의 충돌을 막기 위해 커넥션 공유를 비활성화
		lettuceConnectionFactory.setShareNativeConnection(false);
		return lettuceConnectionFactory;
	}

	// publish 객체 .
	@Bean
	@Qualifier("chatPubSub")
	public RedisTemplate<String, Object> redisTemplate(
		@Qualifier("chatPubSub") RedisConnectionFactory redisConnectionFactory,
		ObjectMapper objectMapper) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

		return redisTemplate;
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(
		@Qualifier("chatPubSub") RedisConnectionFactory redisConnectionFactory,
		MessageListenerAdapter dmListenerAdapter,
		RedisSubscriber redisSubscriber
	) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		container.addMessageListener(dmListenerAdapter, new ChannelTopic("direct-messages"));
		container.addMessageListener(redisSubscriber, new ChannelTopic("notification-channel"));
		return container;
	}

	@Bean
	public MessageListenerAdapter dmListenerAdapter(SubscribeService subscribeService) {
		return new MessageListenerAdapter(subscribeService, "onMessage");
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(redisConnectionFactory);

		// Redis key와 value 직렬화 방식 설정
		stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
		stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
		stringRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
		stringRedisTemplate.setHashValueSerializer(new StringRedisSerializer());

		return stringRedisTemplate;
	}
}
