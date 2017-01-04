package com.trusteer.interview.dsysme.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Data class holding details to serve as a reference for modification. If the tracked object changed, its
 * hash should change as well. By comparing 2 TrackingInfo.hash generated for same object you can tell if the object
 * changed and if so you can look at the TrackingInfo.timeStamp to get the time frame during which the object was
 * modified.
 */
public class TrackingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String hash;
    private Date timeStamp;

    public TrackingInfo(String hash) {
        this.hash = hash;
        this.timeStamp = new Date();
    }

    public String getHash() {
        return hash;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }
}
