package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CheckFilesForModifications {
    final static Logger logger = LoggerFactory.getLogger(CheckFilesForModifications.class);
    private static FilesMonitor filesMonitor;

    public static void main(String[] args) {
        logger.info("CheckFilesForModifications starting...");
        String historyFileName ="";
        try {
            historyFileName = buildFilesMonitorEnv();
        } catch (Exception e) {
            logger.error("Failed to create FilesMonitor", e);
            System.exit(-1);
        }
        associateFilesMonitorWithAlertHandlers();
        // do the actual monitoring
        filesMonitor.checkModifications();
        // save monitoring reference for next run
        persistMonitoringData(historyFileName);
    }

    private static void persistMonitoringData(String historyFileName) {
        // TODO rename old file
        try {
            TrackingInfoWriter.INSTANCE.write(filesMonitor.getMonitoredFiles(), historyFileName);
            logger.info("Monitored files status was saved to " + historyFileName);
        } catch (Exception e) {
            logger.error("Failed to write monitored files status. " + e);
        }
    }

    private static void associateFilesMonitorWithAlertHandlers() {
        AlertHandler alertHandler = new AlertHandler();
        AlertNotifier alertNotifier = new AlertByEmail("sharon.shmorak@gamil.com");
        alertHandler.add(alertNotifier);
        logger.info("Added email alert handler");
        alertHandler.add(new LogAlert());
        logger.info("Added log alert handler");
        filesMonitor.addObserver(alertHandler);
    }

    private static String buildFilesMonitorEnv() throws Exception {
        // make sure working directory exists (this is where input/output files are kept)
        File workingDirectory = new File(System.getProperty("directory", System.getProperty("user.home")+File.separator+FilesMonitor.class.getSimpleName()));
        if (!workingDirectory.exists()) {
            workingDirectory.mkdirs();
            logger.info("created "+workingDirectory);
        }
        // check if previously monitoring data was kept. If so construct FilesMonitor from that data, if not build
        // reference monitoring data from configuration files
        File historyFile = new File(workingDirectory, System.getProperty("history", "MonitoredFiles.ser"));
        if (!historyFile.exists()) {
            ConfigurationLoader.INSTANCE.load();
            filesMonitor = FilesMonitorFactory.INSTANCE.createNewFilesMonitor(ConfigurationLoader.INSTANCE.getHttpFileDescriptors());
        } else {
            Map<HttpFileDescriptor, HttpFileTrackingInfo> monitoredFiles = new HashMap<>();
            monitoredFiles.putAll(new HashMap<>(TrackingInfoReader.INSTANCE.read(historyFile)));
            filesMonitor = new FilesMonitor(monitoredFiles);
            logger.info("loaded files descriptors from previous run.");
        }
        return historyFile.getAbsolutePath();
    }
}