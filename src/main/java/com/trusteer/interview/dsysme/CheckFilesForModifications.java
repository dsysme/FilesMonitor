package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CheckFilesForModifications {
    final static Logger logger = LoggerFactory.getLogger(CheckFilesForModifications.class);

    public static void main(String[] args) {
        logger.info("CheckFilesForModifications starting...");
        File history = new File(TrackingInfoWriter.INSTANCE.getFileName());
        FilesMonitor filesMonitor = null;
        try {
            if (!history.exists()) {
                ConfigurationLoader.INSTANCE.load();
                filesMonitor = FilesMonitorFactory.INSTANCE.createNewFilesMonitor(ConfigurationLoader.INSTANCE.getHttpFileDescriptors());
            } else {
                Map<HttpFileDescriptor, HttpFileTrackingInfo> monitoredFiles = new HashMap<>();
                monitoredFiles.putAll(new HashMap<>(TrackingInfoReader.INSTANCE.read(history)));
                filesMonitor = new FilesMonitor(monitoredFiles);
            }
        } catch (Exception e) {
            logger.error("Failed to create FilesMonitor", e);
            System.exit(-1);
        }
        AlertHandler alertHandler = new AlertHandler();
        AlertNotifier alertNotifier = new AlertByEmail("sharon.shmorak@gamil.com");
        alertHandler.add(alertNotifier);
        alertHandler.add(new LogAlert());
        filesMonitor.addObserver(alertHandler);
        filesMonitor.checkModifications();
        try {
            String fileName = TrackingInfoWriter.INSTANCE.write(filesMonitor.getMonitoredFiles());
            logger.info("Monitored files status was saved to " + fileName);
        } catch (Exception e) {
            logger.error("Failed to write monitored files status. " + e);
        }
    }
}