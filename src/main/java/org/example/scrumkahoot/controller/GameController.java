package org.example.scrumkahoot.controller;

import org.example.scrumkahoot.Router;
import org.example.scrumkahoot.dto.CreateGameResponse;
import org.example.scrumkahoot.service.GameService;

public class GameController implements Controller {
    @Override
    public void register(Router router) {
        router.post("/create-game", ctx -> {
            var game = GameService.get().createGame();
            var res = new CreateGameResponse(game.getConnectionCode());
            ctx.json(res);
        });
    }
}
