package org.example.storymasters;

import org.example.storymasters.controller.GameController;

public class Application {
    public static void main(String[] args) {
        var router = new Router();
        var gameCtl = new GameController();

        gameCtl.register(router);

        router.run(8080);
    }
}
