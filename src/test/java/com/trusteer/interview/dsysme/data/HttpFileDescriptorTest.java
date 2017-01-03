package com.trusteer.interview.dsysme.data;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Sharon on 03/01/2017.
 */
public class HttpFileDescriptorTest {
    @Test
    public void fromString() throws Exception {

    }

    @Test
    public void isValidForValidCase() throws Exception {
        String url = "http://somehost.trusteer.com/file2.txt";
        String ip = "10.0.0.3";
        HttpFileDescriptor fileDescriptor = new HttpFileDescriptor(url, ip);
        Assert.assertTrue(fileDescriptor.isValid());
    }

    @Test
    public void isValidForMissingProtocol() throws Exception {
        String url = "/somehost.trusteer.com/file2.txt";
        String ip = "10.0.0.3";
        HttpFileDescriptor fileDescriptor = new HttpFileDescriptor(url, ip);
        Assert.assertFalse(fileDescriptor.isValid());
    }

    @Test
    public void isValidForEmptyUrl() throws Exception {
        String url = "";
        String ip = "10.0.0.3";
        HttpFileDescriptor fileDescriptor = new HttpFileDescriptor(url, ip);
        Assert.assertFalse(fileDescriptor.isValid());
    }

    @Test
    public void isValidForMissingHost() throws Exception {
        String url = "http://";
        String ip = "10.0.0.3";
        HttpFileDescriptor fileDescriptor = new HttpFileDescriptor(url, ip);
        Assert.assertFalse(fileDescriptor.isValid());
    }

    @Test
    public void isValidForMissingFileName() throws Exception {
        String url = "http://somehost.trusteer.com/";
        String ip = "10.0.0.3";
        HttpFileDescriptor fileDescriptor = new HttpFileDescriptor(url, ip);
        Assert.assertFalse(fileDescriptor.isValid());
    }

    @Test
    public void toURLWithIp() throws Exception {

    }

}