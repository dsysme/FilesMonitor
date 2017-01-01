package com.trusteer.interview.dsysme;

/**
 * Created by Sharon on 28/12/2016.
 * TODO use builder pattern
 */
public class FileModifiedEvent {

    private HttpFileDescriptor fileDescriptor;
    private HttpFileTrackingInfo before;
    private HttpFileTrackingInfo after;

    public FileModifiedEvent(HttpFileDescriptor fileDescriptor, HttpFileTrackingInfo before, HttpFileTrackingInfo after) {
        this.fileDescriptor = fileDescriptor;
        this.before = before;
        this.after = after;
    }

    public HttpFileDescriptor getFileDescriptor() {
        return fileDescriptor;
    }

    public HttpFileTrackingInfo getBefore() {
        return before;
    }

    public HttpFileTrackingInfo getAfter() {
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
