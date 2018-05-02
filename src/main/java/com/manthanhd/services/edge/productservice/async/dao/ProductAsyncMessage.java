package com.manthanhd.services.edge.productservice.async.dao;

public class ProductAsyncMessage {
    private String request;
    private String expires;
    private AsyncRequestType requestType;

    private ProductAsyncMessage(Builder builder) {
        request = builder.request;
        expires = builder.expires;
        requestType = builder.requestType;
    }

    public ProductAsyncMessage() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getRequest() {
        return request;
    }

    public String getExpires() {
        return expires;
    }

    public AsyncRequestType getRequestType() {
        return requestType;
    }

    public static final class Builder {
        private String request;
        private String expires;
        private AsyncRequestType requestType;

        private Builder() {
        }

        public Builder request(String val) {
            request = val;
            return this;
        }

        public Builder expires(String val) {
            expires = val;
            return this;
        }

        public Builder requestType(AsyncRequestType val) {
            requestType = val;
            return this;
        }

        public ProductAsyncMessage build() {
            return new ProductAsyncMessage(this);
        }
    }
}
