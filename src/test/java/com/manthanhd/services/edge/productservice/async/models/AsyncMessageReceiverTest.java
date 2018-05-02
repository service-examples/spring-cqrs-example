package com.manthanhd.services.edge.productservice.async.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manthanhd.services.edge.productservice.async.dao.AsyncRequestType;
import com.manthanhd.services.edge.productservice.async.dao.CreateProductAsyncRequest;
import com.manthanhd.services.edge.productservice.async.dao.ProductAsyncMessage;
import com.manthanhd.services.edge.productservice.async.dao.UpdateProductAsyncRequest;
import com.manthanhd.services.edge.productservice.async.receivers.AsyncMessageReceiver;
import com.manthanhd.services.edge.productservice.models.Product;
import com.manthanhd.services.edge.productservice.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AsyncMessageReceiverTest.SpringConfiguration.class)
public class AsyncMessageReceiverTest {

    @Autowired
    private ProductRepository mockProductRepository;

    @Autowired
    private AsyncMessageReceiver subject;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void receiveMessageProcessesCreateProductAsyncRequest() throws Exception {
        final CreateProductAsyncRequest createProductAsyncRequest = CreateProductAsyncRequest.newBuilder()
                .name("awesome new product")
                .description("epic description")
                .build();

        final String createProductAsyncRequestJsonString = objectMapper.writeValueAsString(createProductAsyncRequest);

        final ProductAsyncMessage message = ProductAsyncMessage.newBuilder()
                .requestType(AsyncRequestType.CREATE)
                .request(createProductAsyncRequestJsonString)
                .expires("1000")
                .build();

        final Product fakeProduct = Product.newBuilder()
                .name("some-product")
                .description("some-description")
                .productId("abc123")
                .build();
        ;
        Mockito.doReturn(fakeProduct).when(mockProductRepository).save(any(Product.class));

        subject.receiveMessage(message);

        Mockito.verify(mockProductRepository).save(any());
    }

    @Test
    public void receiveMessageProcessesUpdateProductAsyncRequest() throws Exception {
        final UpdateProductAsyncRequest createProductAsyncRequest = UpdateProductAsyncRequest.newBuilder()
                .name("awesome new product")
                .description("epic description")
                .productId("abc123")
                .build();

        final String updateProductAsyncRequestJsonString = objectMapper.writeValueAsString(createProductAsyncRequest);

        final ProductAsyncMessage message = ProductAsyncMessage.newBuilder()
                .requestType(AsyncRequestType.UPDATE)
                .request(updateProductAsyncRequestJsonString)
                .expires("1000")
                .build();

        final Product fakeProduct = Product.newBuilder()
                .name("some-product")
                .description("some-description")
                .productId("abc123")
                .build();

        final Optional<Product> fakeOptionalProduct = Optional.of(fakeProduct);
        ;
        Mockito.doReturn(fakeOptionalProduct).when(mockProductRepository).findById(eq("abc123"));

        subject.receiveMessage(message);

        Mockito.verify(mockProductRepository).findById(eq("abc123"));
    }


    @Configuration
    public static class SpringConfiguration {
        @Bean
        ProductRepository mockProductRepository() {
            return Mockito.mock(ProductRepository.class);
        }

        @Bean
        AsyncMessageReceiver receiver() {
            return new AsyncMessageReceiver();
        }
    }
}