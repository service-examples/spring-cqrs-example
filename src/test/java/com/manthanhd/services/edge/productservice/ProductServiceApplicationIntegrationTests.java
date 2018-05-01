package com.manthanhd.services.edge.productservice;

import com.manthanhd.services.edge.productservice.models.Product;
import com.manthanhd.services.edge.productservice.repository.ProductRepository;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProductServiceApplication.class})
@AutoConfigureMockMvc
@ContextConfiguration(classes = {ProductServiceApplicationIntegrationTests.UnitTestConfig.class})
public class ProductServiceApplicationIntegrationTests {

    @MockBean
    private ProductRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void ableToListProducts() throws Exception {
        final Product fakeProduct1 = Product.newBuilder().productId(UUID.randomUUID().toString()).name("some-product").description("some-description").build();
        final Product fakeProduct2 = Product.newBuilder().productId(UUID.randomUUID().toString()).name("some-product2").description("some-description2").build();

        final Iterator<Product> mockIterator = Lists.newArrayList(fakeProduct1, fakeProduct2).iterator();
        final Iterable<Product> mockIterable = Mockito.mock(Iterable.class);
        Mockito.doReturn(mockIterator).when(mockIterable).iterator();
        Mockito.doReturn(mockIterable).when(repository).findAll();

        mockMvc.perform(get("/product")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.products", hasSize(2)))
                .andExpect(jsonPath("$.products[0].productId", is(fakeProduct1.getProductId())))
                .andExpect(jsonPath("$.products[0].name", is(fakeProduct1.getName())))
                .andExpect(jsonPath("$.products[0].description", is(fakeProduct1.getDescription())))
                .andExpect(jsonPath("$.products[1].productId", is(fakeProduct2.getProductId())))
                .andExpect(jsonPath("$.products[1].name", is(fakeProduct2.getName())))
                .andExpect(jsonPath("$.products[1].description", is(fakeProduct2.getDescription())));
    }

    @Test
    public void ableToGetSpecificProduct() throws Exception {
        final Product fakeProduct1 = Product.newBuilder().productId(UUID.randomUUID().toString()).name("some-product").description("some-description").build();

        final Optional<Product> fakeOptionalProduct = Optional.of(fakeProduct1);
        Mockito.doReturn(fakeOptionalProduct).when(repository).findById(eq(fakeProduct1.getProductId()));

        mockMvc.perform(get("/product/" + fakeProduct1.getProductId())
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.productId", is(fakeProduct1.getProductId())))
                .andExpect(jsonPath("$.name", is(fakeProduct1.getName())))
                .andExpect(jsonPath("$.description", is(fakeProduct1.getDescription())));
    }

    @Test
    public void ableToReturn404WhenProductIsNotFound() throws Exception {
        final String fakeId = "abc123";
        final Optional<Product> fakeOptionalProduct = Optional.ofNullable(null);
        Mockito.doReturn(fakeOptionalProduct).when(repository).findById(eq(fakeId));

        mockMvc.perform(get("/product/abc123")
                .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(isEmptyOrNullString()));
    }

    @Configuration
    public static class UnitTestConfig {
        @Bean
        public ProductRepository productRepository() {
            return Mockito.mock(ProductRepository.class);
        }
    }
}
