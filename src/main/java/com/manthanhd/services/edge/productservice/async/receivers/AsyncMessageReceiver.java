package com.manthanhd.services.edge.productservice.async.receivers;

import com.manthanhd.services.edge.productservice.async.dao.CreateProductAsyncRequest;
import com.manthanhd.services.edge.productservice.models.Product;
import com.manthanhd.services.edge.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AsyncMessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncMessageReceiver.class);

    @Autowired
    private ProductRepository repository;

    @RabbitListener(queues = {"product-service"})
    public void receiveCreateProductRequest(CreateProductAsyncRequest request) {
        final Product product = Product.newBuilder().name(request.getName()).description(request.getDescription()).productId(UUID.randomUUID().toString()).build();
        final Product savedProduct = repository.save(product);
        LOGGER.info(String.format("New Product [ASYNC]: %s", savedProduct.getProductId()));
    }

}
