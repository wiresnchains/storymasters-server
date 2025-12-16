package org.example.storymasters.dto;

public class CreateGameResponse {
    private String code;

    public CreateGameResponse(String connectionCode) {
        this.code = connectionCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String connectionCode) {
        this.code = connectionCode;
    }
}
