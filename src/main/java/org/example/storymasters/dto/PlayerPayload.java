package org.example.storymasters.dto;

import org.example.storymasters.model.Player;

public class PlayerPayload {
    private String name;
    private Integer points;

    public PlayerPayload() {}

    public PlayerPayload(String name, Integer points) {
        this.name = name;
        this.points = points;
    }

    public PlayerPayload(Player player) {
        this.name = player.getName();
        this.points = player.getPoints();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
