package org.chocvanilla.weatherapp.io;

public class MissingAPIKeyException extends Exception {
    public MissingAPIKeyException(String message) {
        super(message);
    }
}
