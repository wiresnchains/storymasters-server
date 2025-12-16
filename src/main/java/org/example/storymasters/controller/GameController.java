package org.example.storymasters.controller;

import io.javalin.http.HttpStatus;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import org.example.storymasters.Router;
import org.example.storymasters.dto.CreateGameResponse;
import org.example.storymasters.exception.GameNotFoundException;
import org.example.storymasters.exception.PlayerNameTakenException;
import org.example.storymasters.service.GameService;

public class GameController implements Controller {
    @Override
    public void register(Router router) {
        router.post("/create-game", ctx -> {
            var res = createGame();
            ctx.status(HttpStatus.OK);
            ctx.json(res);
        });

        router.post("/join-game", ctx -> {
            var res = createGame();
            ctx.status(HttpStatus.OK);
            ctx.json(res);
        });

        router.ws("/game/{code}/{name}", this::onConnect, this::onMessage, this::onClose);
    }

    private CreateGameResponse createGame() {
        var game = GameService.get().createGame();
        return new CreateGameResponse(game.getConnectionCode());
    }

    private void onConnect(WsConnectContext ctx) {
        String code = ctx.pathParam("code");
        String name = ctx.pathParam("name");

        try {
            GameService.get().joinGame(code, name);
        }
        catch (GameNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void onMessage(WsMessageContext ctx) {
        System.out.println(ctx.message());
    }

    private void onClose(WsCloseContext ctx) {
        String code = ctx.pathParam("code");
        String name = ctx.pathParam("name");

        try {
            GameService.get().quitGame(name, code);
        }
        catch (GameNotFoundException | PlayerNameTakenException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
