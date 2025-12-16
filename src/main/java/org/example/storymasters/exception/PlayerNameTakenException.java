package org.example.storymasters.exception;

public class PlayerNameTakenException extends RuntimeException {
    public PlayerNameTakenException(String message) {
        super(message);
    }
}
