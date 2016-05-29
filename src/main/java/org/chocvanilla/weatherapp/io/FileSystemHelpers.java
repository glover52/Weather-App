package org.chocvanilla.weatherapp.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileSystemHelpers {
    public static BufferedReader getResource(Class relativeTo, String name) throws IOException {
        try {
            return new BufferedReader(new InputStreamReader(relativeTo.getResourceAsStream(name)));
        } catch (NullPointerException e) {
            throw new IOException(e);
        }
    }
}
