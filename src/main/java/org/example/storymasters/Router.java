package org.example.storymasters;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class Router {
    private final Javalin app;

    public Router() {
        app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.allowHost("http://localhost:3000");
                    rule.allowHost("http://localhost:3001");
                    rule.allowHost("http://127.0.0.1:3000");
                    rule.allowHost("http://127.0.0.1:3001");
                    rule.allowHost("https://wiresnchains.com");
                    rule.allowCredentials = true;
                });
            });
        });
    }

    public void run(int port) {
        app.start(port);
    }

    public void get(String path, Handler handler) {
        app.get(path, handler);
    }

    public void post(String path, Handler handler) {
        app.post(path, handler);
    }
}
