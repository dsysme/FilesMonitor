package com.trusteer.interview.dsysme;

import com.trusteer.interview.dsysme.data.HttpFileDescriptor;
import com.trusteer.interview.dsysme.data.TrackingInfo;
import com.trusteer.interview.dsysme.utils.HttpFileHashCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for construction of a FileMonitor from a list of files that should be monitored.
 * For each monitored file it creates the TrackingInfo that will serve as a baseline for checking modifications
 * Created by Sharon on 28/12/2016.
 */
public enum FilesMonitorFactory {

    INSTANCE;

    final static Logger logger = LoggerFactory.getLogger(FilesMonitorFactory.class);

    public FilesMonitor createNewFilesMonitor(List<HttpFileDescriptor> filesToMonitor) {
        Map<HttpFileDescriptor, TrackingInfo> trackingInfoMap = new HashMap<>();
        for (HttpFileDescriptor file : filesToMonitor) {
            try {
                String hash = HttpFileHashCalculator.INSTANCE.getHash(file.toURLWithIp());
                if (hash.isEmpty()) {
                    logger.error("Failed to get reference hash for file "+ file);
                }
                trackingInfoMap.put(file, new TrackingInfo(hash));
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return  new FilesMonitor(trackingInfoMap);
    }

}
