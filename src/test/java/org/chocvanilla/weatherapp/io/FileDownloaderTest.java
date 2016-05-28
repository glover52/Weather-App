package org.chocvanilla.weatherapp.io;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class FileDownloaderTest {
    private FileDownloader fd;
    private static final String testDir = "TestDir";
    private static final String testFile = "TestFile";

    @Before
    public void setUp() {
        fd  = new FileDownloader();
    }

    @AfterClass
    public static void tearDown() {
        try {
            File dir = new File(testDir);
            // Deleted created directories and files
            String[] entry = dir.list();
            for (String s : entry) {
                File file = new File(dir.getPath(), s);
                file.delete();
            }
            dir.delete();
        } catch (Exception e){
            // Ignore
        }
    }

    @Test (expected = UnknownHostException.class)
    public void badUrlTest() throws IOException{
        URL url = new URL("http://www.amadeupurlfortesting.com");
        fd.downloadFile(url ,"", "" );
    }

    @Test (expected = IOException.class)
    public void downloadWithNoWriteAccess() throws IOException{
        URL url = new URL("http://www.google.com");
        fd.downloadFile(url ,"", "" );
    }

    @Test
    public void downloadTest() throws IOException{
        URL url = new URL("http://www.google.com");
        Path p = fd.downloadFile(url , testDir, testFile);

        assertNotNull(p);
        assertEquals(p.getParent().getFileName().toString(), testDir);
        assertEquals(p.getFileName().toString(), testFile);
    }


}
