package org.example.storymasters;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageHandler;

import java.util.function.Function;

public class Router {
    private final Javalin app;

    public Router() {
        app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(rule -> {
                    rule.allowHost("http://localhost:3000");
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

    public void ws(String path, WsConnectHandler onConnect, WsMessageHandler onMessage, WsCloseHandler onClose) {
        app.ws(path, ws -> {
            ws.onConnect(onConnect);
            ws.onMessage(onMessage);
            ws.onClose(onClose);
        });
    }
}
