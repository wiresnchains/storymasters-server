package org.example.storymasters;

import org.example.storymasters.controller.*;

public class Application {
    public static void main(String[] args) {
        var router = new Router();
        var gameCtl = new GameController();
        TestController testController = new TestController();
        testController.register(router);

        gameCtl.register(router);

        router.run(8080);
    }
}
