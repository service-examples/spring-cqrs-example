package com.manthanhd.services.edge.productservice.async.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
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
public class AsyncConfiguration implements RabbitListenerConfigurer {

    @Value("${async.queue.name}")
    private String queueName;

    @Value(("${async.routing.key}"))
    private String routingKey;

    @Value("${async.topic.exchangeName")
    private String topicExchangeName;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private MessageHandlerMethodFactory messageHandlerMethodFactory;

    @Bean
    Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(routingKey);
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerFactory() {
        final DefaultMessageHandlerMethodFactory defaultMessageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        defaultMessageHandlerMethodFactory.setMessageConverter(messageConverter);
        return defaultMessageHandlerMethodFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
    }

//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        final MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, LISTENER_METHOD);
//        messageListenerAdapter.setMessageConverter(new Jackson2JsonMessageConverter());
//        return messageListenerAdapter;
//    }
//
//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
//        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//        container.setQueueNames(queueName);
//        container.setMessageListener(listenerAdapter);
//        container.setMessageConverter(new Jackson2JsonMessageConverter());
//        return container;
//    }
}
