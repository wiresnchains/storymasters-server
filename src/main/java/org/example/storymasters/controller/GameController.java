package org.example.storymasters.controller;

import io.javalin.http.HttpStatus;
import org.example.storymasters.Router;
import org.example.storymasters.dto.CreateGameResponse;
import org.example.storymasters.service.GameService;

public class GameController implements Controller {
    @Override
    public void register(Router router) {
        router.post("/create-game", ctx -> {
            var res = createGame();
            ctx.status(HttpStatus.OK);
            ctx.json(res);
        });
    }

    private CreateGameResponse createGame() {
        var game = GameService.get().createGame();
        return new CreateGameResponse(game.getConnectionCode());
    }
}
