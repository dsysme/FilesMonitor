package com.trusteer.interview.dsysme;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Created by Sharon on 01/01/2017.
 */
public enum TrackingInfoReader {

    INSTANCE;

    public Map<HttpFileDescriptor, HttpFileTrackingInfo> read(File file) throws Exception {
        Map<HttpFileDescriptor, HttpFileTrackingInfo> result = Collections.EMPTY_MAP;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            result = (Map<HttpFileDescriptor, HttpFileTrackingInfo>) ois.readObject();
        }
        return result;
    }
}
