package com.trusteer.interview.dsysme;

import com.trusteer.interview.dsysme.data.HttpFileDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

    InputFilesLoader() {
        httpFileDescriptors = new ArrayList<>();
    }

    /***
     *
     * @param folderName
     * @return false if load failed, true otherwise
     */
    public boolean load(String folderName) {

        boolean result = true;
        try {
            InputFilesCollector.INSTANCE.collectConfigurationFiles(folderName).stream().forEach(file -> loadDescriptors(file));
        } catch (Exception e) {
            logger.error("Failed to load configuration files: " + e.getMessage());
            return false;
        }
        return result;
    }

    public void loadDescriptors(String fileName) {
        logger.info("Start loading urls from "+fileName);
        Path path = Paths.get(fileName);
        try {
            // handle simple pairs <url> <ip>
            try (Stream<String> stream = Files.lines(path)) {
                httpFileDescriptors.addAll(stream.filter(line -> !line.trim().endsWith("*"))
                        .map(line -> HttpFileDescriptor.fromString(line)).filter(fileDescriptor -> fileDescriptor.isValid())
                        .collect(Collectors.toList()));
            }
            // handle <url> *
            try (Stream<String> stream = Files.lines(path)) {
                httpFileDescriptors.addAll(stream.filter(line -> line.trim().endsWith("*")).map(line -> getAllIps(line))
                        .flatMap(x -> x.stream()).filter(fileDescriptor -> fileDescriptor.isValid())
                        .collect(Collectors.toList()));
            }
            // log bad lines
            try (Stream<String> stream = Files.lines(path)) {
                httpFileDescriptors.stream().filter(fileDescriptor -> !fileDescriptor.isValid())
                        .forEach(fileDescriptor -> logger.error("malformed line in " + fileName + " " + fileDescriptor.toString()));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        logger.info("Done loading urls from "+fileName);
    }

    public Set<HttpFileDescriptor> getAllIps(String line) {
        HttpFileDescriptor fileDescriptor = HttpFileDescriptor.fromString(line);
        URL url = null;
        Set<HttpFileDescriptor> result = new HashSet<>();
        String host;
        try {
            url = new URL(fileDescriptor.getUrl());
        } catch (Exception e) {
            logger.error("invalid record "+ line);
            return Collections.EMPTY_SET;
        }
        try {
            host = url.getHost();
            if (host == null || host.isEmpty()) {
                logger.error("could not get host"+ url);
                return Collections.EMPTY_SET;
            }
            for (InetAddress addr : InetAddress.getAllByName(host))
                result.add(new HttpFileDescriptor(fileDescriptor.getUrl(), addr.getHostAddress()));
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    public List<HttpFileDescriptor> getHttpFileDescriptors() {
        return httpFileDescriptors;
    }
}