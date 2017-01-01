package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class responsibility is to return a list of configuration file names.
 * All files under resource/config are assumed to be configuration files.
 *
 * TODO: Consider optimizing by keeping past result and recollecting only on explicit demand
 *
 * Based on snippet taken from:
 * http://stackoverflow.com/questions/11012819/how-can-i-get-a-resource-folder-from-inside-my-jar-file/20073154#20073154
 */
public enum  ConfigurationFilesCollector {

    INSTANCE;

    final static Logger logger = LoggerFactory.getLogger(ConfigurationFilesCollector.class);

    public List<String> collectConfigurationFiles() throws Exception {
        final String path = "config";
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        List<String> result = new ArrayList<>();

        if (jarFile.isFile()) {  // Run with JAR file
            logger.info("extracting configuration files from jar");
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(path + "/")) { //filter according to the path
                    result.add(name);
                    logger.info("found configuration file "+name);
                }
            }
            jar.close();
        }
        else { // Run with IDE
            logger.info("extracting configuration files from resources folder");
            final URL url = getClass().getClassLoader().getResource(path);
            if (url != null) {
                final File configFolder = new File(url.toURI());
                for (File file : configFolder.listFiles()) {
                    result.add(file.getAbsolutePath());
                    logger.info("found configuration file "+file.getAbsolutePath());
                }
            }
        }
        return result;
    }
}
