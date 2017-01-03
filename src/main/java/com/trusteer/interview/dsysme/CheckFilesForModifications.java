package com.trusteer.interview.dsysme;

import com.trusteer.interview.dsysme.alerts.AlertHandler;
import com.trusteer.interview.dsysme.alerts.AlertNotifier;
import com.trusteer.interview.dsysme.alerts.EmailAlert;
import com.trusteer.interview.dsysme.alerts.LogAlert;
import com.trusteer.interview.dsysme.data.HttpFileDescriptor;
import com.trusteer.interview.dsysme.data.TrackingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/***
 * config directory should be pass as VM parameter (-Dfile-monitor-config.dir="...") to main
 * TODO no need to run check modification if no previous run tracking saved
 * TODO clean code and externalize configuration such as SEND_TO
 */
public enum CheckFilesForModifications {
    INSTANCE;

    final static Logger logger = LoggerFactory.getLogger(CheckFilesForModifications.class);
    public static final String FILES_MONITOR = "FilesMonitor";
    public static final String SEND_TO = "dsysme.interview@gmail.com";
    private static FilesMonitor filesMonitor;
    private String outputFolder;
    private String logFile;

    public static void main(String[] args) {
        CheckFilesForModifications.INSTANCE.logger.info("CheckFilesForModifications starting...");
        String historyFileName ="";
        try {
            historyFileName = CheckFilesForModifications.INSTANCE.buildFilesMonitorEnv();
        } catch (Exception e) {
            logger.error("Failed to create FilesMonitor", e);
            System.exit(-1);
        }
        try {
            CheckFilesForModifications.INSTANCE.associateFilesMonitorWithAlertHandlers();
        } catch (Exception e) {
            logger.error("Failed to create Alert Notifier", e);
            System.exit(-1);
        }

        // do the actual monitoring
        CheckFilesForModifications.INSTANCE.filesMonitor.checkModifications();
        // save monitoring reference for next run
        CheckFilesForModifications.INSTANCE.persistMonitoringData(historyFileName);
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

    private void associateFilesMonitorWithAlertHandlers() throws Exception {
        AlertHandler alertHandler = new AlertHandler();
        AlertNotifier alertNotifier = new EmailAlert(SEND_TO);
        alertHandler.add(alertNotifier);
        logger.info("Added email alert notifier");
        alertNotifier = new LogAlert(FILES_MONITOR, logFile);
        alertHandler.add(alertNotifier);
        logger.info("Added log file alert notifier "+logFile);
        filesMonitor.addObserver(alertHandler);
    }

    /***
     * config directory should be pass as VM parameter (-Dfile-monitor-config.dir="...") to main
     * @return
     * @throws Exception
     */
    private String buildFilesMonitorEnv() throws Exception {

        outputFolder = System.getProperty("user.home")+File.separator+FilesMonitor.class.getSimpleName();
        logFile = outputFolder +File.separator+"logs"+File.separator+"FileModifications.log";
        //
        // make sure working directory exists (this is where input/output files are kept)
        File workingDirectory = new File(System.getProperty("directory", outputFolder));
        if (!workingDirectory.exists()) {
            workingDirectory.mkdirs();
            logger.info("created "+workingDirectory);
        }
        // check if previously monitoring data was kept. If so construct FilesMonitor from that data, if not build
        // reference monitoring data from configuration files
        File historyFile = new File(workingDirectory, System.getProperty("history", "MonitoredFiles.ser"));
        if (!historyFile.exists()) {
            String configFolderName = System.getProperty("file-monitor-config.dir");
            if (configFolderName == null || configFolderName.isEmpty()) {
                logger.error("Missing parameter 'file-monitor-config.dir'");
                throw new Exception("Missing parameter 'file-monitor-config.dir'");
            }
            File configFolder = new File(configFolderName);
            if (!configFolder.exists()) {
                logger.error(configFolder.getAbsolutePath() + " does not exists.");
                throw new Exception(configFolderName + " does not exists.");
            }
            InputFilesLoader.INSTANCE.load(configFolderName);
            filesMonitor = FilesMonitorFactory.INSTANCE.createNewFilesMonitor(InputFilesLoader.INSTANCE.getHttpFileDescriptors());
        } else {
            Map<HttpFileDescriptor, TrackingInfo> monitoredFiles = new HashMap<>();
            monitoredFiles.putAll(new HashMap<>(TrackingInfoReader.INSTANCE.read(historyFile)));
            filesMonitor = new FilesMonitor(monitoredFiles);
            logger.info("loaded files descriptors from previous run.");
        }
        return historyFile.getAbsolutePath();
    }
}