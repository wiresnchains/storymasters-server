package org.example.storymasters.dto;

import org.example.storymasters.model.Player;

import java.util.List;

public class LeaderboardPayload {
    private List<PlayerPayload> players;

    public LeaderboardPayload() {}

    public LeaderboardPayload(List<PlayerPayload> players) {
        this.players = players;
    }

    public List<PlayerPayload> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerPayload> players) {
        this.players = players;
    }
}
