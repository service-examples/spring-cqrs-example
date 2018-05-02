package com.manthanhd.services.edge.productservice.async.receivers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manthanhd.services.edge.productservice.async.dao.CreateProductAsyncRequest;
import com.manthanhd.services.edge.productservice.async.dao.ProductAsyncMessage;
import com.manthanhd.services.edge.productservice.async.dao.UpdateProductAsyncRequest;
import com.manthanhd.services.edge.productservice.models.Product;
import com.manthanhd.services.edge.productservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AsyncMessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncMessageReceiver.class);
    private final ObjectMapper objectMapper;

    @Autowired
    private ProductRepository repository;

    public AsyncMessageReceiver() {
        objectMapper = new ObjectMapper();
    }

    @RabbitListener(queues = {"product-service"})
    public void receiveMessage(ProductAsyncMessage message) {
        try {
            switch (message.getRequestType()) {
                case CREATE:
                    createProduct(objectMapper.readValue(message.getRequest(), CreateProductAsyncRequest.class));
                    break;
                case UPDATE:
                    updateProduct(objectMapper.readValue(message.getRequest(), UpdateProductAsyncRequest.class));
                    break;
                default:
                    LOGGER.error("Unknown message type %s", message.getRequestType());
                    break;
            }
        } catch (Throwable e) {
            LOGGER.error("Unexpected exception encountered when processing async request", e);
        }
    }

    private void createProduct(CreateProductAsyncRequest request) {
        LOGGER.info(String.format("New Product [ASYNC] ACK: %s", request));
        final Product product = Product.newBuilder().name(request.getName()).description(request.getDescription()).productId(UUID.randomUUID().toString()).build();
        final Product savedProduct = repository.save(product);
        LOGGER.info(String.format("New Product [ASYNC] PAS: %s", savedProduct.getProductId()));
    }

    private void updateProduct(UpdateProductAsyncRequest request) {
        if (null == request.getProductId()) {
            LOGGER.error("Update Product [ASYNC] ERR: Id is null");
            return;
        }

        LOGGER.info(String.format("Update Product [ASYNC] ACK: %s", request.getProductId()));
        final Optional<Product> productOptional = repository.findById(request.getProductId());
        if (!productOptional.isPresent()) {
            LOGGER.error(String.format("Update Product [ASYNC] ERR: %s", request.getProductId()));
            return;
        }

        final Product product = productOptional.get();
        product.setName(request.getName());
        product.setDescription(request.getDescription());

        final Product savedProduct = repository.save(product);
        LOGGER.info("Update Product [ASYNC] PAS: " + savedProduct.getProductId());
    }
}
