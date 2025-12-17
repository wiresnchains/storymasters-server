package org.example.storymasters.dto;

public class StartGameRequest {
    private String connectionCode;

    public StartGameRequest() {}

    public StartGameRequest(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    public String getConnectionCode() {
        return connectionCode;
    }

    public void setConnectionCode(String connectionCode) {
        this.connectionCode = connectionCode;
    }
}
