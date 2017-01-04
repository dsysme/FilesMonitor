package com.trusteer.interview.dsysme;

import com.trusteer.interview.dsysme.data.HttpFileDescriptor;
import com.trusteer.interview.dsysme.data.TrackingInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * Created by Sharon on 01/01/2017.
 */
public enum TrackingInfoReader {

    INSTANCE;

    public Map<HttpFileDescriptor, TrackingInfo> read(File file) throws Exception {
        Map<HttpFileDescriptor, TrackingInfo> result;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            result = (Map<HttpFileDescriptor, TrackingInfo>) ois.readObject();
        }
        return result;
    }
}
