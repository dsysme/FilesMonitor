package com.trusteer.interview.dsysme.alerts;

import com.trusteer.interview.dsysme.data.HttpFileDescriptor;
import com.trusteer.interview.dsysme.data.TrackingInfo;

/**
 * Data object holding the details of file modified event
 * TODO use builder pattern
 */
public class FileModifiedEvent {

    private HttpFileDescriptor fileDescriptor;
    private TrackingInfo before;
    private TrackingInfo after;

    public FileModifiedEvent(HttpFileDescriptor fileDescriptor, TrackingInfo before, TrackingInfo after) {
        this.fileDescriptor = fileDescriptor;
        this.before = before;
        this.after = after;
    }

    public HttpFileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    public TrackingInfo getBefore() {
        return before;
    }

    public TrackingInfo getAfter() {
        return after;
    }

    @Override
    public String toString() {
        return "File " +
                fileDescriptor.getUrl() + " changed on " + fileDescriptor.getIp() +
                " between " + before.getTimeStamp() +
                " and " + after.getTimeStamp();
    }
}
