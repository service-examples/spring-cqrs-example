package com.manthanhd.services.edge.productservice.async.dao;

public class CreateProductAsyncRequest {
    private String name;
    private String description;

    public CreateProductAsyncRequest() {
    }

    private CreateProductAsyncRequest(Builder builder) {
        name = builder.name;
        description = builder.description;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "CreateProductAsyncRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static final class Builder {
        private String name;
        private String description;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public CreateProductAsyncRequest build() {
            return new CreateProductAsyncRequest(this);
        }
    }
}
