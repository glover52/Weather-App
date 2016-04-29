package org.chocvanilla.weatherapp.io;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class FileSystemHelpers {
    public static BufferedReader getResource(Class relativeTo, String name) {
        return new BufferedReader(new InputStreamReader(relativeTo.getResourceAsStream(name)));
    }
}
