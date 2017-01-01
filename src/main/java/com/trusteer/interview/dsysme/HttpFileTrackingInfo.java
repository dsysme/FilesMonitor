package com.trusteer.interview.dsysme;

import java.util.Date;

/**
 * Created by Sharon on 28/12/2016.
 */
public class HttpFileTrackingInfo {
    private String hash;
    private Date timeStamp;

    public HttpFileTrackingInfo(String hash) {
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
