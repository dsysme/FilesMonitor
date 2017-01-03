package com.trusteer.interview.dsysme;

import com.trusteer.interview.dsysme.data.HttpFileDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class responsibility is to read all configuration files and create a list of all URL,IP pairs that needs
 * to be monitored.
 */
public enum InputFilesLoader {

    INSTANCE;

    final static Logger logger = LoggerFactory.getLogger(InputFilesLoader.class);

    private List<HttpFileDescriptor> httpFileDescriptors;

    public void load(String folderName) {

        try {
            InputFilesCollector.INSTANCE.collectConfigurationFiles(folderName).stream().forEach(file -> loadDescriptors(file));
        } catch (Exception e) {
            logger.error("Failed to load configuration files: " + e.getMessage());
        }
    }

    private void loadDescriptors(String fileName) {
        logger.info("Start loading urls from "+fileName);
        Path path = Paths.get(fileName);
        try (Stream<String> stream = Files.lines(path)) {
            // handle simple pairs <url> <ip>
            httpFileDescriptors = stream.filter(line -> !line.trim().endsWith("*")).map(line -> HttpFileDescriptor.fromString(line)).filter(fileDescriptor -> fileDescriptor.isValid()).collect(Collectors.toList());
            // TODO handle pairs <url> *
            //stream.filter(line -> line.trim().endsWith("*")).
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Done loading urls from "+fileName);
    }

    public List<HttpFileDescriptor> getHttpFileDescriptors() {
        return httpFileDescriptors;
    }
}