package org.chocvanilla.weatherapp.io;

import java.io.*;

public class FileSystemHelpers {
    public static BufferedReader getResource(Class relativeTo, String name) throws IOException {
        try {
            return new BufferedReader(new InputStreamReader(relativeTo.getResourceAsStream(name)));            
        } catch (NullPointerException e){
            throw new IOException(e);
        }
    }
}
