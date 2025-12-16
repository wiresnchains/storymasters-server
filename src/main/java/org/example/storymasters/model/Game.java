package org.example.storymasters.model;

import org.example.storymasters.exception.PlayerNameTakenException;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final String connectionCode;
    private final List<Player> players = new ArrayList<>();

    public Game(String connectionCode) {
        this.connectionCode = connectionCode;
    }

    public String getConnectionCode() {
        return connectionCode;
    }

    public Player addPlayer(String name) throws PlayerNameTakenException {
        for (var player : players) {
            if (player.getName().equals(name)) {
                throw new PlayerNameTakenException("Player met de naam " + name + " speelt al");
            }
        }
        var player = new Player(name);
        this.players.add(player);
        return player;
    }

    public void removePlayer(String name) {
        for (int i = 0; i < players.size(); i++) {
            var player = players.get(i);

            if (player.getName().equals(name)) {
                players.remove(i);
                break;
            }
        }
    }
}
