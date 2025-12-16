package org.example.storymasters.model;

public class Game {
    private final String connectionCode;

    public Game(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    public String getConnectionCode() {
        return connectionCode;
    }
}
