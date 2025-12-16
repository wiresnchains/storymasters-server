package org.example.storymasters.model;

import java.util.Random;

public class Game {
    private String connectionCode = "NIEUWE CONNECTION CODE";;

    public Game() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) stringBuilder.append(random.nextInt(10));
        // System.out.println("Code: " + stringBuilder);
        connectionCode = stringBuilder.toString();
    }

    public String getCode() {
        return connectionCode;
    }
}
