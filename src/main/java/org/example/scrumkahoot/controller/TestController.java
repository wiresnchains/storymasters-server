package org.example.scrumkahoot.controller;

import org.example.scrumkahoot.Router;


public class TestController implements Controller {
    @Override
    public void register(Router router) {
        router.get("/"    , ctx -> ctx.result("Hello World"));
        router.get("/test", ctx -> ctx.result("test"));

//            var game = GameService.get().createGame();
//            var res = new CreateGameResponse(game.getConnectionCode());
//            ctx.json(res);
//        });
    }
}
