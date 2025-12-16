package org.example.storymasters.controller;

import org.example.storymasters.Router;

public class TestController implements Controller {
    @Override
    public void register(Router router) {
        router.get("./"    , ctx -> ctx.result("Hello World"));
        router.get("./test", ctx -> ctx.result("test"));
    }
}
