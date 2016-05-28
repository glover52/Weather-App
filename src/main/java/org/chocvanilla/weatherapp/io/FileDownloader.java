package org.chocvanilla.weatherapp.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

public class FileDownloader {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public Path downloadFile(URL url, String target, String filename) throws IOException {
        Path path = Paths.get(target, filename);
        Paths.get(target).toFile().mkdirs();
        try (InputStream in = url.openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            return path;
        }
    }
}
