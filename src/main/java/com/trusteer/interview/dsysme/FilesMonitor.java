package com.trusteer.interview.dsysme;

import com.trusteer.interview.dsysme.alerts.FileModifiedEvent;
import com.trusteer.interview.dsysme.data.HttpFileDescriptor;
import com.trusteer.interview.dsysme.data.TrackingInfo;
import com.trusteer.interview.dsysme.utils.HttpFileHashCalculator;
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

    private Map<HttpFileDescriptor, TrackingInfo> monitoredFiles;

    public FilesMonitor(Map<HttpFileDescriptor, TrackingInfo> monitoredFiles) {
        this.monitoredFiles = monitoredFiles;
    }

    public void checkModifications() {
        logger.info("checking modifications");
        monitoredFiles.keySet().stream().forEach(monitoredFile -> {
                    logger.info("checking "+monitoredFile.toString());
                    TrackingInfo oldTrackingInfo = monitoredFiles.get(monitoredFile);
                    TrackingInfo newTrackingInfo = fileChanged(monitoredFile);
                    if (newTrackingInfo != null) {
                        triggerFileChanged(monitoredFile, oldTrackingInfo, newTrackingInfo);
                    }
                }
        );
    }

    private void triggerFileChanged(HttpFileDescriptor fileDescriptor, TrackingInfo oldTrackinginfo, TrackingInfo newTrackinginfo) {
        monitoredFiles.put(fileDescriptor, newTrackinginfo);
        this.setChanged();
        FileModifiedEvent event = new FileModifiedEvent(fileDescriptor, oldTrackinginfo, newTrackinginfo);
        notifyObservers(event);
    }

    private TrackingInfo fileChanged(HttpFileDescriptor monitoredFile) {
        TrackingInfo before = monitoredFiles.get(monitoredFile);
        try {
            String currentHash = HttpFileHashCalculator.INSTANCE.getHash(monitoredFile.toURLWithIp());
            if (!currentHash.isEmpty() && !currentHash.equals(before.getHash())) {
                return new TrackingInfo(currentHash);
            }
        } catch (MalformedURLException e) {
           logger.error("Failed to calculate hash for "+monitoredFile);
        }
        return null;
    }

    public Map<HttpFileDescriptor, TrackingInfo> getMonitoredFiles() {
        return Collections.unmodifiableMap(monitoredFiles);
    }
}
