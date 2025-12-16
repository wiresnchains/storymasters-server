package org.example.storymasters.dto;

public class CreateGameResponse {
    private String connectionCode;

    public CreateGameResponse(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    public String getConnectionCode() {
        return connectionCode;
    }

    public void setConnectionCode(String connectionCode) {
        this.connectionCode = connectionCode;
    }
}
