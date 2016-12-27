package com.trusteer.interview.dsysme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public enum ConfigurationLoader {

    INSTANCE;

    private List<HttpFileDescriptor> httpFileDescriptors;

    ConfigurationLoader() {
        httpFileDescriptors = new ArrayList<>();
    }

    static public class HttpFileDescriptor {
        URL url;
        InetAddress ip;

        public HttpFileDescriptor(String url, String ip) throws Exception {
            this.url = new URL(url);
            this.ip = InetAddress.getByName(ip);
        }

        public URL getUrl() {
            return url;
        }

        public InetAddress getIp() {
            return ip;
        }

        static public HttpFileDescriptor fromString(String descriptor) throws Exception{
            String [] arr = descriptor.split(" +");
            return new HttpFileDescriptor(arr[0].trim(), arr[1].trim());
        }


        @Override
        public String toString() {
            return "HttpFileDescriptor{" +
                    "url=" + url +
                    ", ip=" + ip +
                    '}';
        }
    }

    public void load() {

        try {
            ConfigurationFilesCollector.INSTANCE.collectConfigurationFiles().stream().forEach(file -> loadDescriptors(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        httpFileDescriptors.stream().forEach(System.out::println);
    }

    private void loadDescriptors(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> {
                HttpFileDescriptor fd;
                try {
                    if (!line.trim().endsWith("*")) {
                        fd = HttpFileDescriptor.fromString(line);
                        httpFileDescriptors.add(fd);
                    }
                } catch (Exception e) {
                    //TODO log
                    System.out.println("Failed to create descriptor for: " + line);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}