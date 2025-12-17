package org.example.storymasters.model;

import io.javalin.websocket.WsContext;
import org.example.storymasters.dto.WebsocketMessage;

public class Player {
    private final String name;
    private Integer points;
    private final WsContext socketCtx;

    public Player(String name, WsContext socketCtx) {
        this.name = name;
        this.points = 0;
        this.socketCtx = socketCtx;
        socketCtx.attribute("player", this);
    }

    public String getName() {
        return name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void broadcast(String event, Object data) {
        if (!socketCtx.session.isOpen()) {
            return;
        }

        socketCtx.send(new WebsocketMessage(event, data, false));
    }
}
