package dev.yuri.votacao.exception;

public class SessionNotFinishedException extends RuntimeException {
    public SessionNotFinishedException(String message) {
        super(message);
    }
}
