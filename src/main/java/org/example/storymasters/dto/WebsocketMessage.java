package org.example.storymasters.dto;

public class WebsocketMessage {
    private String event;
    private Object data;
    private boolean fromHost;

    public WebsocketMessage() {}

    public WebsocketMessage(String event, Object data, boolean fromHost) {
        this.event = event;
        this.data = data;
        this.fromHost = fromHost;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isFromHost() {
        return fromHost;
    }

    public void setFromHost(boolean fromHost) {
        this.fromHost = fromHost;
    }
}
