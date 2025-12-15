package org.example.scrumkahoot;

import org.example.scrumkahoot.controller.GameController;

public class Application {
    public static void main(String[] args) {
        var router = new Router();
        var gameCtl = new GameController();

        gameCtl.register(router);

        router.run(8080);
    }
}
