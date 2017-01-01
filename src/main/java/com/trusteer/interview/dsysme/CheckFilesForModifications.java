package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckFilesForModifications {
    final static Logger logger = LoggerFactory.getLogger(CheckFilesForModifications.class);

    public static void main(String[] args) {
        logger.info("CheckFilesForModifications starting...");
        ConfigurationLoader.INSTANCE.load();
        FilesMonitor filesMonitor = FilesMonitorFactory.INSTANCE.createNewFilesMonitor(ConfigurationLoader.INSTANCE.getHttpFileDescriptors());
        AlertHandler alertHandler = new AlertHandler();
        AlertNotifier alertNotifier = new AlertByEmail("sharon.shmorak@gamil.com");
        alertHandler.add(alertNotifier);
        alertHandler.add(new LogAlert());
        filesMonitor.addObserver(alertHandler);
        for (int i = 0; i < 5; ++i) {
            filesMonitor.checkModifications();
//            try {
//                Thread.sleep(1000*60*2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}