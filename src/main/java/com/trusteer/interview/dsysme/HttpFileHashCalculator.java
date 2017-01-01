package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * This class calculate hash string for given inputStream using SHA-1 algorithm
 * Created by Sharon on 27/12/2016.
 */
public enum HttpFileHashCalculator {

    INSTANCE;

    final static Logger logger = LoggerFactory.getLogger(HttpFileHashCalculator.class);

    public String getHash(URL url) {

        HttpURLConnection conn = null;
        String result = "";
        try {
            conn = (HttpURLConnection) url.openConnection();
            try (InputStream is = conn.getInputStream()) {
                result = getHash(is);
            }
        } catch (IOException e) {
            // we need to fully consume the error stream in order for the underline socket connection to be reused
            // http://docs.oracle.com/javase/6/docs/technotes/guides/net/http-keepalive.html
            try {
                StringBuilder sb = new StringBuilder();
                try (InputStream es = conn.getErrorStream()) {
                    try (BufferedInputStream bis = new BufferedInputStream(es)) {
                        while (bis.available() > 0) {
                            sb.append(bis.read());
                        }
                    }
                }
                logger.error(sb.toString());
            } catch (IOException ex) {
                logger.error(ex.toString());
            }
        } finally {
            // needed for the underline socket connection to be reused
            if (conn != null)
                conn.disconnect();
        }
        return result;
    }

    /**
     * Based on code from http://www.javacreed.com/how-to-generate-sha1-hash-value-of-file/
     */
    private String getHash(InputStream inputStream) {
        String result = "";
        try {
            try (DigestInputStream in = new DigestInputStream(new BufferedInputStream(inputStream),
                    MessageDigest.getInstance("SHA-1"))) {
                // Read the stream and do nothing with it
                while (in.read() != -1) {
                }

                // Get the digest and finialise the computation
                final MessageDigest md = in.getMessageDigest();
                final byte[] digest = md.digest();

                // Format as HEX
                try (Formatter formatter = new Formatter()) {
                    for (final byte b : digest) {
                        formatter.format("%02x", b);
                    }

                    result = formatter.toString();
                }
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            logger.error(e.toString());
        }
        return result;
    }
}
