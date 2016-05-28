package org.chocvanilla.weatherapp.io;

public class MissingAPIKeyException extends RuntimeException {
    public MissingAPIKeyException(String message) {
        super(message);
    }
}
