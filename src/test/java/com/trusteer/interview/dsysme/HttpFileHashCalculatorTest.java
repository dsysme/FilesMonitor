package com.trusteer.interview.dsysme;

import com.trusteer.interview.dsysme.utils.HttpFileHashCalculator;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 * Created by Sharon on 27/12/2016.
 */
public class HttpFileHashCalculatorTest {

    @Test
    public void testThatSameHashIsReturnedWhenFileNotChanged() throws Exception {
        // compute SHA-1
        String hashFirstCompute = "";
        String hashSecondCompute = "";

        //URL url = new URL("http://test.talia.net/dl/1mb.pak");
        URL url = new URL("http://test.talia.net/dl/1mb.pak");
        hashFirstCompute = HttpFileHashCalculator.INSTANCE.getHash(url);
        Assert.assertNotNull(hashFirstCompute);
        Assert.assertNotSame("", hashFirstCompute);

        hashSecondCompute = HttpFileHashCalculator.INSTANCE.getHash(url);
        Assert.assertEquals(hashFirstCompute, hashSecondCompute);
    }

    @Test
    public void testThatDiffHashIsReturnedWhenFileChanged() throws Exception {
        // compute SHA-1
        String hashFirstCompute = "";
        String hashSecondCompute = "";
        String hashAfterAppendCompute = "";
        //149.202.220.122
        // URL url = new URL("http://www.ynetnews.com/home/0,7340,L-3083,00.html");
        URL url = new URL("http://149.202.220.122/?file_id=73183456368860968541");
        hashFirstCompute = HttpFileHashCalculator.INSTANCE.getHash(url);
        Assert.assertNotNull(hashFirstCompute);
        Assert.assertNotSame("", hashFirstCompute);
        hashSecondCompute = HttpFileHashCalculator.INSTANCE.getHash(url);
        Assert.assertNotNull(hashSecondCompute);
        Assert.assertNotEquals(hashSecondCompute, hashAfterAppendCompute);
    }

}