package com.trusteer.interview.dsysme;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Sharon on 27/12/2016.
 * TODO add validation
 */
public class HttpFileDescriptor {

    private URL url;
    private InetAddress ip;

    public HttpFileDescriptor(String url, String ip) throws Exception {
        this.url = new URL(url);
        this.ip = InetAddress.getByName(ip);
    }

    public URL getUrl() {
        return url;
    }

    public InetAddress getIp() {
        return ip;
    }

    static public HttpFileDescriptor fromString(String descriptor) throws Exception{
        String [] arr = descriptor.split(" +");
        return new HttpFileDescriptor(arr[0].trim(), arr[1].trim());
    }


    public URL toURLWithIp() throws MalformedURLException {
        String urlString = getUrl().toString();
        urlString.replaceFirst(getUrl().getHost(), getIp().getHostAddress());

        return new URL(urlString);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpFileDescriptor that = (HttpFileDescriptor) o;

        if (getUrl() != null ? !getUrl().equals(that.getUrl()) : that.getUrl() != null) return false;
        return getIp() != null ? getIp().equals(that.getIp()) : that.getIp() == null;
    }

    @Override
    public int hashCode() {
        int result = getUrl() != null ? getUrl().hashCode() : 0;
        result = 31 * result + (getIp() != null ? getIp().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpFileDescriptor{" +
                "url=" + url +
                ", ip=" + ip +
                '}';
    }
}
