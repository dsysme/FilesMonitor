package com.trusteer.interview.dsysme;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Created by Sharon on 01/01/2017.
 */
public enum TrackingInfoWriter {

    INSTANCE;

    public String write(Map<HttpFileDescriptor, HttpFileTrackingInfo> monitoredFiles, String fileName) throws Exception {
        //TODO configuration
        //TODO handle existing file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(monitoredFiles);
        }
        return fileName;
    }

}
