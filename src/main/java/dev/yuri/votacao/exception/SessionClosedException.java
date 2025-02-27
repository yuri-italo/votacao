package dev.yuri.votacao.exception;

public class SessionClosedException extends RuntimeException {
    public SessionClosedException(String message) {
        super(message);
    }
}
