package com.manthanhd.services.edge.productservice.async.dao;

public enum AsyncRequestType {
    CREATE("create"), UPDATE("update");

    private final String type;

    AsyncRequestType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
