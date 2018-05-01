package com.manthanhd.services.edge.productservice.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Product {
    @Id
    private String productId;
    @NotNull
    private String name;
    private String description;

    private Product(Builder builder) {
        productId = builder.productId;
        name = builder.name;
        description = builder.description;
    }

    public Product() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static final class Builder {
        private String productId;
        private String name;
        private String description;

        private Builder() {
        }

        public Builder productId(String val) {
            productId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
