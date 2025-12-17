package org.example.storymasters.service;

import io.javalin.websocket.WsContext;
import org.example.storymasters.exception.GameNotFoundException;
import org.example.storymasters.exception.PlayerNameTakenException;
import org.example.storymasters.model.Game;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private final List<Game> games = new ArrayList<>();

    public Game createGame() {
        var game = new Game(generateConnectionCode());

        games.add(game);

        System.out.println("Created game " + game.getConnectionCode());

        return game;
    }

    public Game getGame(String connectionCode) throws GameNotFoundException {
        for (var game : games) {
            if (game.getConnectionCode().equals(connectionCode)) {
                return game;
            }
        }

        throw new GameNotFoundException("Game met koppel code " + connectionCode + " bestaat niet");
    }

    public void joinGame(String name, String connectionCode, WsContext ctx) throws GameNotFoundException, PlayerNameTakenException {
        var game = getGame(connectionCode);
        game.addPlayer(name, ctx);
    }

    public void quitGame(String name, String connectionCode) throws GameNotFoundException {
        getGame(connectionCode).removePlayer(name);
    }

    public void closeGame(Game game) {
        games.remove(game);
        System.out.println("Closed game " + game.getConnectionCode());
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
