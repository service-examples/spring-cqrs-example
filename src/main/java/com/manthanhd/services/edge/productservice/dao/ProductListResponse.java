package com.manthanhd.services.edge.productservice.dao;

import java.util.List;

public class ProductListResponse {
    private List<ProductResponse> products;

    private ProductListResponse(Builder builder) {
        products = builder.products;
    }

    public ProductListResponse() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public static final class Builder {
        private List<ProductResponse> products;

        private Builder() {
        }

        public Builder products(List<ProductResponse> val) {
            products = val;
            return this;
        }

        public ProductListResponse build() {
            return new ProductListResponse(this);
        }
    }
}
