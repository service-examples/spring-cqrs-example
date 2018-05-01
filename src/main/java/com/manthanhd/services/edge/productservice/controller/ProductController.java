package com.manthanhd.services.edge.productservice.controller;

import com.manthanhd.services.edge.productservice.dao.ProductListResponse;
import com.manthanhd.services.edge.productservice.dao.ProductResponse;
import com.manthanhd.services.edge.productservice.models.Product;
import com.manthanhd.services.edge.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController {

    private ProductRepository repository;

    @Autowired
    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(path = "/product", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ProductListResponse> listProducts() {
        final List<ProductResponse> productList = new LinkedList<>();
        final Iterable<Product> products = repository.findAll();
        for (Product product : products) {
            productList.add(ProductResponse.newBuilder()
                    .productId(product.getProductId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .build());
        }
        final ProductListResponse productListResponse = ProductListResponse.newBuilder().products(productList).build();
        return ResponseEntity.ok(productListResponse);
    }

    @RequestMapping(path = "/product/{productId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ProductResponse> listProducts(@PathVariable("productId") String productId) {
        final Optional<Product> optionalProduct = repository.findById(productId);
        final Product product = optionalProduct.orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        final ProductResponse productResponse = ProductResponse.newBuilder().productId(product.getProductId()).name(product.getName()).description(product.getDescription()).build();
        return ResponseEntity.ok(productResponse);
    }
}
