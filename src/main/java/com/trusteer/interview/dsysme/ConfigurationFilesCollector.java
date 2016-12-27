package com.trusteer.interview.dsysme;

import sun.misc.Launcher;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Based on snippet taken from:
 * http://stackoverflow.com/questions/11012819/how-can-i-get-a-resource-folder-from-inside-my-jar-file/20073154#20073154
 */
public enum  ConfigurationFilesCollector {

    INSTANCE;

    public List<String> collectConfigurationFiles() throws Exception {
        final String path = "config";
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        List<String> result = new ArrayList<>();

        if (jarFile.isFile()) {  // Run with JAR file
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(path + "/")) { //filter according to the path
                    result.add(name);
                }
            }
            jar.close();
        }
        else { // Run with IDE
            final URL url = Launcher.class.getResource("/" + path);
            if (url != null) {
                final File configFolder = new File(url.toURI());
                for (File file : configFolder.listFiles()) {
                    result.add(file.getAbsolutePath());
                }
            }
        }
        return result;
    }
}
