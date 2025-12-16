package org.example.storymasters.service;

import org.example.storymasters.model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private final List<Game> games = new ArrayList<>();

    public Game createGame() {
        Game game = new Game();

        games.add(game);

        return game;
    }

    public List<Game> getGames() {
        return games;
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
