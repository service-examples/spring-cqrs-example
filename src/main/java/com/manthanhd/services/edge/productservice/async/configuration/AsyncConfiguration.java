package com.manthanhd.services.edge.productservice.async.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
@EnableRabbit
public class AsyncConfiguration implements RabbitListenerConfigurer {

    @Value("${async.queue.name}")
    private String queueName;

    @Value(("${async.routing.key}"))
    private String routingKey;

    @Value("${async.topic.exchangeName")
    private String topicExchangeName;

    @Value("${async.mq.server.hostname}")
    private String mqHostname;

    @Value("${async.mq.authentication.username}")
    private String mqAuthenticationUsername;

    @Value("${async.mq.authentication.password}")
    private String mqAuthenticationPassword;

    @Value("${async.mq.server.maxConnections}")
    private int mqMaxConnections;

    @Autowired
    private MessageHandlerMethodFactory messageHandlerMethodFactory;

    @Bean
    Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerFactory(MessageConverter messageConverter) {
        final DefaultMessageHandlerMethodFactory defaultMessageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        defaultMessageHandlerMethodFactory.setMessageConverter(messageConverter);
        return defaultMessageHandlerMethodFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(mqHostname);
        connectionFactory.setUsername(mqAuthenticationUsername);
        connectionFactory.setPassword(mqAuthenticationPassword);
        connectionFactory.setConnectionLimit(mqMaxConnections);
        return connectionFactory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
    }
}
