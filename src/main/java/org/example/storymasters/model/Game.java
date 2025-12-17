package org.example.storymasters.model;

import io.javalin.websocket.WsContext;
import org.example.storymasters.dto.LeaderboardPayload;
import org.example.storymasters.dto.PlayerPayload;
import org.example.storymasters.dto.UserStoryPayload;
import org.example.storymasters.dto.VotingPayload;
import org.example.storymasters.exception.PlayerNameTakenException;
import org.example.storymasters.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final String connectionCode;
    private final List<Player> players = new ArrayList<>();
    private final List<UserStory> activeRoundUserStories = new ArrayList<>();
    private boolean started;

    public Game(String connectionCode) {
        this.connectionCode = connectionCode;
        this.started = false;
    }

    public String getConnectionCode() {
        return connectionCode;
    }

    public Player addPlayer(String name, WsContext ctx) throws PlayerNameTakenException {
        for (var player : players) {
            if (player.getName().equals(name)) {
                throw new PlayerNameTakenException("Player met de naam " + name + " speelt al");
            }
        }

        var player = new Player(name, ctx, this);
        this.players.add(player);

        for (var playerIt : players) {
            player.broadcast("player-joined", playerIt.getName());

            if (playerIt != player) {
                playerIt.broadcast("player-joined", name);
            }
        }

        System.out.println("Player " + name + " has joined game " + connectionCode);

        return player;
    }

    public void removePlayer(String name) {
        for (int i = 0; i < players.size(); i++) {
            var player = players.get(i);

            if (player.getName().equals(name)) {
                players.remove(i);
                broadcast("player-removed", player.getName());
                break;
            }
        }

        System.out.println("Player " + name + " has left game " + connectionCode);

        if (players.isEmpty()) {
            System.out.println("Closing game " + connectionCode + " (last player left)");
            GameService.get().closeGame(this);
        }
    }

    public void cleanup() {
        for (var player : players) {
            player.disconnect();
        }
    }

    public void broadcast(String eventName, Object data) {
        for (var player : players) {
            player.broadcast(eventName, data);
        }
    }

    public void showVotingStage() {
        broadcast("show-voting", new VotingPayload(activeRoundUserStories.stream().map(UserStoryPayload::new).toList(), false));

        System.out.println("Game " + connectionCode + " entered voting stage");
    }

    public void showVoteResults() {
        broadcast("show-voting", new VotingPayload(activeRoundUserStories.stream().map(UserStoryPayload::new).toList(), true));

        System.out.println("Game " + connectionCode + " entered vote results stage");
    }

    public void showLeaderboard() {
        broadcast("show-leaderboard", new LeaderboardPayload(players.stream().map(PlayerPayload::new).toList()));

        System.out.println("Game " + connectionCode + " entered leaderboard stage");
    }

    public boolean isStarted() {
        return started;
    }

    public void start() {
        this.started = true;
        startRound("random thema idk");
    }

    public void startRound(String theme) {
        broadcast("start-round", theme);
    }

    public void endRound() {
        activeRoundUserStories.clear();
    }

    public void addUserStory(Player player, String story) {
        activeRoundUserStories.add(new UserStory(story, player));
    }
}
