package org.example.storymasters.dto;

public class GenericPayload {
    private String message;

    public GenericPayload() {}

    public GenericPayload(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
