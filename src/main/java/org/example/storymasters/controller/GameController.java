package org.example.storymasters.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.HttpStatus;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import org.example.storymasters.Router;
import org.example.storymasters.dto.*;
import org.example.storymasters.exception.GameNotFoundException;
import org.example.storymasters.exception.PlayerNameTakenException;
import org.example.storymasters.model.Game;
import org.example.storymasters.model.Player;
import org.example.storymasters.service.GameService;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class GameController implements Controller {
    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final HashMap<String, BiConsumer<Player, WebsocketMessage>> events = new HashMap<>();

    @Override
    public void register(Router router) {
        router.post("/create-game", ctx -> {
            CreateGameResponse res = createGame();
            System.out.println("\nCreating game: " + res.getConnectionCode());
            ctx.status(HttpStatus.OK);
            ctx.json(res);
        });

        router.post("/start-game", ctx -> {
            var request = mapper.readValue(ctx.body(), StartGameRequest.class);
            startGame(request.getConnectionCode());
            ctx.status(HttpStatus.OK);
            ctx.result("{}");
            ctx.contentType("application/json");
        });

        router.ws("/game/{code}/{name}", this::onConnect, this::onMessage, this::onClose);

        addEvent("send-user-story", (player, message) -> {
            var game = player.getGame();
            GenericPayload payload = mapper.convertValue(message.getData(), GenericPayload.class);
            game.addUserStory(player, payload.getMessage());
        });

        addEvent("vote-for", (player, message) -> {
            var game = player.getGame();
            var payload = mapper.convertValue(message.getData(), VoteForPayload.class);
            game.voteFor(player, payload.getUserStoryIndex());
        });
    }

    private CreateGameResponse createGame() {
        GameService gameService = GameService.get();
        Game game = gameService.createGame();
        return new CreateGameResponse(game.getConnectionCode());
    }

    private void startGame(String connectionCode) {
        GameService.get().startGame(connectionCode);
    }

    private void addEvent(String name, BiConsumer<Player, WebsocketMessage> handler) {
        events.put(name, handler);
    }

    private void callEvent(Player player, WebsocketMessage message) throws JsonProcessingException {
        events.get(message.getEvent()).accept(player, message);
    }

    private void onConnect(WsConnectContext ctx) {
        String code = ctx.pathParam("code");
        String name = ctx.pathParam("name");


        try {
            GameService.get().joinGame(name, code, ctx);
            System.out.println(String.format("%s joined game %s", name, code));
        }
        catch (GameNotFoundException | PlayerNameTakenException ex) {
            System.err.println(ex.getMessage());
            ctx.closeSession();
        }
    }

    private void onMessage(WsMessageContext ctx) throws JsonProcessingException {
        if (ctx.message().equals("im-alive")) {
            return;
        }

        WebsocketMessage message = mapper.readValue(ctx.message(), WebsocketMessage.class);

        if (message.isFromHost()) {
            callEvent(null, message);
            return;
        }

        Player player = ctx.attribute("player");

        if (player == null) {
            ctx.session.close();
            return;
        }

        callEvent(player, message);
    }

    private void onClose(WsCloseContext ctx) {
        String code = ctx.pathParam("code");
        String name = ctx.pathParam("name");

        try {
            GameService.get().quitGame(name, code);
        }
        catch (GameNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
