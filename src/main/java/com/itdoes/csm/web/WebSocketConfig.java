package com.itdoes.csm.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.itdoes.common.core.jackson.JavaTimeObjectMapper;

/**
 * @author Jalen Zhong
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		LOGGER.info("registerStompEndpoints: " + registry);
		registry.addEndpoint("/ws").withSockJS();
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		LOGGER.info("configureWebSocketTransport: " + registry);
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		LOGGER.info("configureClientInboundChannel: " + registration);
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		LOGGER.info("configureClientOutboundChannel: " + registration);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		LOGGER.info("addArgumentResolvers: " + argumentResolvers);
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
		LOGGER.info("addReturnValueHandlers: " + returnValueHandlers);
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		LOGGER.info("configureMessageConverters: " + messageConverters);
		final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(new JavaTimeObjectMapper());
		messageConverters.add(converter);
		return true;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		LOGGER.info("configureMessageBroker: " + registry);
		registry.setApplicationDestinationPrefixes("/app");
		registry.setUserDestinationPrefix("/user");
		registry.enableSimpleBroker("/topic");
	}
}
