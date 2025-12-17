package org.example.storymasters.service;

import io.javalin.websocket.WsContext;
import org.example.storymasters.exception.GameNotFoundException;
import org.example.storymasters.exception.PlayerNameTakenException;
import org.example.storymasters.model.Game;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameService {
    private final List<Game> games = new ArrayList<>();
    private final Map<String, Game> gamesMap = new LinkedHashMap<String, Game>();

    public Game createGame() {
        var game = new Game(generateConnectionCode());

        games.add(game);
        gamesMap.put(game.getConnectionCode(), game);

        System.out.println("Created game " + game.getConnectionCode());

        return game;
    }

    public void startGame(String connectionCode) throws GameNotFoundException {
        var game = getGame(connectionCode);
        game.start();
    }

    public Game getGame(String connectionCode) throws GameNotFoundException {
        Game game = this.gamesMap.get(connectionCode);
        // for (var game : games) {
        //     if (game.getConnectionCode().equals(connectionCode)) {
        //         return game;
        //     }
        // }

        if (game != null) return game;
        throw new GameNotFoundException("Game met koppelcode " + connectionCode + " bestaat niet");
    }

    public void joinGame(String name, String connectionCode, WsContext ctx) throws GameNotFoundException, PlayerNameTakenException {
        var game = getGame(connectionCode);

        if (game.isStarted()) {
            ctx.closeSession();
        }

        game.addPlayer(name, ctx);
    }

    public void quitGame(String name, String connectionCode) throws GameNotFoundException {
        getGame(connectionCode).removePlayer(name);
    }

    public void closeGame(Game game) {
        game.cleanup();
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
