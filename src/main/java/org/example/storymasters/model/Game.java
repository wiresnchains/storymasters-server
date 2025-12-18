package org.example.storymasters.model;

import io.javalin.websocket.WsContext;
import org.example.storymasters.dto.LeaderboardPayload;
import org.example.storymasters.dto.PlayerPayload;
import org.example.storymasters.dto.UserStoryPayload;
import org.example.storymasters.dto.VotingPayload;
import org.example.storymasters.exception.PlayerNameTakenException;
import org.example.storymasters.service.GameService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Game {
    private final int MAX_GAME_ROUND_COUNT = 5;
    private final String connectionCode;
    private final List<Player> players = new ArrayList<Player>();
    private final List<UserStory> activeRoundUserStories = new ArrayList<UserStory>();
    private final HashSet<Player> roundVotes = new HashSet<>();
    private boolean started;
    private int roundsPlayed;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Game(String connectionCode) {
        this.connectionCode = connectionCode;
        this.started = false;
        this.roundsPlayed = 0;

        scheduler.schedule(() -> {
            if (players.isEmpty()) {
                System.out.println("Closing game " + connectionCode + " (idle)");
                GameService.get().closeGame(this);
            }
            else {
                GameService.get().startGame(connectionCode);
            }
        }, 30, TimeUnit.SECONDS);
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

        scheduler.shutdownNow();
    }

    public void broadcast(String eventName, Object data) {
        for (var player : players) {
            player.broadcast(eventName, data);
        }
    }

    public void showVotingStage() {
        var storyPayloads = IntStream.range(0, activeRoundUserStories.size())
            .mapToObj(i -> new UserStoryPayload(activeRoundUserStories.get(i), i))
            .toList();

        broadcast("show-voting", new VotingPayload(storyPayloads, false));

        System.out.println("Game " + connectionCode + " entered voting stage");
    }

    public void showVoteResults() {
        var storyPayloads = IntStream.range(0, activeRoundUserStories.size())
                .mapToObj(i -> new UserStoryPayload(activeRoundUserStories.get(i), i))
                .toList();

        broadcast("show-voting", new VotingPayload(storyPayloads, true));

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
        startRound();
    }

    private ArrayList<String> themes = new ArrayList<String>(List.of(
        // "App die kerstman helpt met zijn taken",
        "App die kerstelven helpen cadeautjes in te pakken",
        "Systeem om te het rendier-verzorgprocess te waarborgen",
        "Kerstman en hulptroepen helpen alle cadeautjes rond te brengen op kerstavond",
        "Meta: (spel)website welke je helpt oefenen met userstories schrijven"
    ));

    public void startRound() {
        int index = (int) Math.floor(Math.random() * themes.size());
        String theme = this.themes.get(index);
        System.out.println("Theme: " + theme);

        themes.remove(index);

        broadcast("start-round", theme);

        scheduler.schedule(this::endRound, 60, TimeUnit.SECONDS);

        System.out.println("[" + connectionCode + "] Round started");
    }

    public void endRound() {
        System.out.println("[" + connectionCode + "] Ending round...");

        showVotingStage();

        scheduler.schedule(() -> {
            showVoteResults();

            scheduler.schedule(() -> {
                var winner = getCurrentRoundWinner();

                if (winner != null) {
                    winner.getOwner().setPoints(winner.getOwner().getPoints() + 5);
                }

                showLeaderboard();

                scheduler.schedule(() -> {
                    this.roundsPlayed++;

                    activeRoundUserStories.clear();
                    roundVotes.clear();

                    System.out.println("Game " + connectionCode + " round ended");

                    int rounds = Math.min(this.themes.size(), MAX_GAME_ROUND_COUNT);
                    if (this.roundsPlayed >= rounds) {
                        System.out.println(String.format("Closing game %s (%s rounds played)", connectionCode, rounds));
                        GameService.get().closeGame(this);
                        return;
                    }

                    startRound();
                }, 5, TimeUnit.SECONDS);
            }, 5, TimeUnit.SECONDS);
        }, 10, TimeUnit.SECONDS);
    }

    public void addUserStory(Player player, String story) {
        System.out.println("[" + connectionCode + "] Got user story " + story + " from " + player.getName());
        activeRoundUserStories.add(new UserStory(story, player));
    }

    public void voteFor(Player player, Integer userStoryIndex) {
        if (roundVotes.contains(player)) {
            return; // player has already voted this round
        }

        var userStory = activeRoundUserStories.get(userStoryIndex);
        userStory.setVotes(userStory.getVotes() + 1);
        roundVotes.add(player);
    }

    public UserStory getCurrentRoundWinner() {
        UserStory winningUs = null;

        for (var userStory : activeRoundUserStories) {
            if (winningUs == null) {
                winningUs = userStory;
                continue;
            }

            if (winningUs.getVotes() >= userStory.getVotes()) {
                continue;
            }

            winningUs = userStory;
        }

        return winningUs;
    }
}
