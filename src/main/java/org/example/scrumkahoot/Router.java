package org.example.scrumkahoot;

import io.javalin.Javalin;
import io.javalin.http.Handler;

public class Router {
    private final Javalin app;

    public Router() {
        app = Javalin.create();
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
