package com.trusteer.interview.dsysme.data;

import com.trusteer.interview.dsysme.utils.IPAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Sharon on 27/12/2016.
 * Data object holding a pair of url and host address (ip) that points to a file
 */
public class HttpFileDescriptor implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(HttpFileDescriptor.class);

    private String url;
    private String ip;

    private boolean isValid;

    public HttpFileDescriptor(String url, String ip)  {
        try {
            this.url = urlValidation(url);
            this.ip = IPAddressValidator.INSTANCE.validate(ip);
            this.isValid = isUrlWithValidFilename(url);
        } catch (Exception e) {
            isValid = false;
        }
    }

    /***
     * http://stackoverflow.com/questions/2230676/how-to-check-for-a-valid-url-in-java
     * @param url
     * @return
     * @throws Exception
     */
    private String urlValidation(String url) throws Exception {
        URI uri;
        try {
            URL u = new URL(url); // this would check for the protocol
            uri = u.toURI(); // does the extra checking required for validation of URI
        } catch (MalformedURLException | URISyntaxException e) {
            logger.error("invalid url "+ url + e.getMessage());
            throw new Exception("invalid url "+ url + e.getMessage());
        }
        return uri.toString();
    }

    private boolean isUrlWithValidFilename(String url) {
        int index = url.lastIndexOf("/");
        if (index== url.length())
            return false;
        String fileName = url.substring(index+1);
        if (fileName.trim().isEmpty())
            return false;
        File f = new File(fileName);
        try {
            f.getCanonicalPath();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getIp() {
        return ip;
    }

    static public HttpFileDescriptor fromString(String descriptor) {
        String [] arr = descriptor.split(" +");
        return new HttpFileDescriptor(arr[0].trim(), arr[1].trim());
    }

    public boolean isValid() {
        return isValid;
    }

    public URL toURLWithIp() throws MalformedURLException {
        String urlString = getUrl().toString();
        urlString.replaceFirst((new URL(url).getHost()), ip);

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
                "url='" + url + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

}
