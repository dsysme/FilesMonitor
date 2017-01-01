package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Sharon on 28/12/2016.
 */

public class FilesMonitor extends Observable  {

    private static final Logger logger = LoggerFactory.getLogger(FilesMonitor.class);


    private Map<HttpFileDescriptor, HttpFileTrackingInfo> monitoredFiles;

    public FilesMonitor(Map<HttpFileDescriptor, HttpFileTrackingInfo> monitoredFiles) {
        this.monitoredFiles = monitoredFiles;
    }

    public void checkModifications() {
        monitoredFiles.keySet().stream().forEach(monitoredFile -> {
                    logger.info("checking "+monitoredFile.toString());
                    HttpFileTrackingInfo oldTrackingInfo = monitoredFiles.get(monitoredFile);
                    HttpFileTrackingInfo newTrackingInfo = fileChanged(monitoredFile);
                    if (newTrackingInfo != null) {
                        triggerFileChanged(monitoredFile, oldTrackingInfo, newTrackingInfo);
                    }
                }
        );
    }

    private void triggerFileChanged(HttpFileDescriptor fileDescriptor, HttpFileTrackingInfo oldTrackinginfo, HttpFileTrackingInfo newTrackinginfo) {
        monitoredFiles.put(fileDescriptor, newTrackinginfo);
        this.setChanged();
        FileModifiedEvent event = new FileModifiedEvent(fileDescriptor, oldTrackinginfo, newTrackinginfo);
        notifyObservers(event);
    }

    private HttpFileTrackingInfo fileChanged(HttpFileDescriptor monitoredFile) {
        HttpFileTrackingInfo before = monitoredFiles.get(monitoredFile);
        try {
            String currentHash = HttpFileHashCalculator.INSTANCE.getHash(monitoredFile.toURLWithIp());
            if (!currentHash.isEmpty() && !currentHash.equals(before.getHash())) {
                return new HttpFileTrackingInfo(currentHash);
            }
        } catch (MalformedURLException e) {
           logger.error("Failed to calculate hash for "+monitoredFile);
        }
        return null;
    }


    public Map<HttpFileDescriptor, HttpFileTrackingInfo> getMonitoredFiles() {
        return Collections.unmodifiableMap(monitoredFiles);
    }
}
