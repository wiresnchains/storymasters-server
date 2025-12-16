package org.example.storymasters.service;

import org.example.storymasters.model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private final List<Game> games = new ArrayList<>();

    public Game createGame() {
        var game = new Game(generateConnectionCode());

        games.add(game);

        return game;
    }

    public List<Game> getGames() {
        return games;
    }

    private String generateConnectionCode() {
        while (true) {
            int codeInt = (int) (Math.random() * 1_000_000);
            String code = String.format("%06d", codeInt);

            boolean exists = false;
            for (var game : games) {
                if (game.getConnectionCode().equals(code)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                return code;
            }
        }
    }

    // Singleton
    private static GameService instance;

    protected GameService() {}

    public static GameService get() {
        if (instance == null) {
            instance = new GameService();
        }

        return instance;
    }
}
