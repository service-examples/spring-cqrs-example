package com.manthanhd.services.edge.productservice.utils;

import com.manthanhd.services.edge.productservice.async.dao.CreateProductAsyncRequest;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.UUID;

public class MqMessageSendCli {
    public static void main(String[] args) throws InterruptedException {
        final RabbitTemplate template = new RabbitTemplate(new CachingConnectionFactory());
        final CreateProductAsyncRequest createProductAsyncRequest = CreateProductAsyncRequest.newBuilder().name("awesome-product " + UUID.randomUUID().toString()).description("awesome description").build();
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.convertAndSend("product-service-topic-exchange", "productservice.post", createProductAsyncRequest);
    }
}
