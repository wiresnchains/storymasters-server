package org.example.scrumkahoot;

import org.example.scrumkahoot.controller.*;

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
