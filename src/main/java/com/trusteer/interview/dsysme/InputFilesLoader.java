package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class responsibility is to read all configuration files and create a list of all URL,IP pairs that needs
 * to be monitored.
 */
public enum InputFilesLoader {

    INSTANCE;

    final static Logger logger = LoggerFactory.getLogger(InputFilesLoader.class);

    private List<HttpFileDescriptor> httpFileDescriptors;

    InputFilesLoader() {
        httpFileDescriptors = new ArrayList<>();
    }

    public void load(String folderName) {

        try {
            InputFilesCollector.INSTANCE.collectConfigurationFiles(folderName).stream().forEach(file -> loadDescriptors(file));
        } catch (Exception e) {
            logger.error("Failed to load configuration files: " + e.getMessage());
        }
    }

    private void loadDescriptors(String fileName) {
        logger.info("Loading urls from "+fileName);
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> {
                HttpFileDescriptor fd;
                try {
                    if (!line.trim().endsWith("*")) {
                        fd = HttpFileDescriptor.fromString(line);
                        httpFileDescriptors.add(fd);
                    }
                    //TODO support *
                } catch (Exception e) {
                    logger.error("Failed to create descriptor for: " + line);
                }
            });
            logger.info("Done loading from "+fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<HttpFileDescriptor> getHttpFileDescriptors() {
        return httpFileDescriptors;
    }
}