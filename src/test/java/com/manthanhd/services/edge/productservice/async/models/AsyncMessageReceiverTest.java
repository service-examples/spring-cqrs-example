package com.manthanhd.services.edge.productservice.async.models;

import com.manthanhd.services.edge.productservice.async.receivers.AsyncMessageReceiver;
import com.manthanhd.services.edge.productservice.repository.ProductRepository;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AsyncMessageReceiverTest.SpringConfiguration.class)
public class AsyncMessageReceiverTest {

    @Autowired
    private ProductRepository mockProductRepository;

    @Autowired
    private AsyncMessageReceiver receiver;

    /*@Test
    public void receiveCreateProductRequestSavesProduct() {
        final Product fakeProduct = Product.newBuilder().description("some-description").name("some-name").build();
        final CreateProductAsyncRequest createProductAsyncRequest = CreateProductAsyncRequest.newBuilder().name("some-name").description("some-description").build();

        Mockito.doReturn(fakeProduct).when(mockProductRepository).save(ArgumentMatchers.any());

        receiver.receiveCreateProductRequest(createProductAsyncRequest);

        Mockito.verify(mockProductRepository, times(1)).save(ArgumentMatchers.any());
    }*/

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