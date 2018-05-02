package com.manthanhd.services.edge.productservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manthanhd.services.edge.productservice.async.dao.AsyncRequestType;
import com.manthanhd.services.edge.productservice.async.dao.CreateProductAsyncRequest;
import com.manthanhd.services.edge.productservice.async.dao.ProductAsyncMessage;
import com.manthanhd.services.edge.productservice.async.dao.UpdateProductAsyncRequest;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.UUID;

public class MqMessageSendCli {
    public static void main(String[] args) throws JsonProcessingException {
        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guestpassword");

        final ObjectMapper objectMapper = new ObjectMapper();

        final RabbitTemplate template = new RabbitTemplate(connectionFactory);

        template.setMessageConverter(new Jackson2JsonMessageConverter());

        final CreateProductAsyncRequest createProductAsyncRequest = CreateProductAsyncRequest.newBuilder()
                .name("new awesome-product " + UUID.randomUUID().toString())
                .description("create description")
                .build();

        final ProductAsyncMessage createAsyncMessage = ProductAsyncMessage.newBuilder()
                .expires("sooon!")
                .request(objectMapper.writeValueAsString(createProductAsyncRequest))
                .requestType(AsyncRequestType.CREATE)
                .build();
        template.convertAndSend("product-service-topic-exchange", "productservice.post", createAsyncMessage);

        final UpdateProductAsyncRequest updateProductAsyncRequest = UpdateProductAsyncRequest.newBuilder()
                .productId("abc123")
                .name("product-to-update")
                .description("Updated description")
                .build();

        final ProductAsyncMessage updateAsyncMessage = ProductAsyncMessage.newBuilder()
                .expires("sooon!")
                .request(objectMapper.writeValueAsString(updateProductAsyncRequest))
                .requestType(AsyncRequestType.UPDATE)
                .build();
        template.convertAndSend("product-service-topic-exchange", "productservice.put", updateAsyncMessage);
    }
}
