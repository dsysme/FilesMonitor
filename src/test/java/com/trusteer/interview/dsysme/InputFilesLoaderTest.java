package com.trusteer.interview.dsysme;

import com.trusteer.interview.dsysme.data.HttpFileDescriptor;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * Created by Sharon on 03/01/2017.
 */
public class InputFilesLoaderTest {
    @Test
    @Ignore
    public void loadDescriptors() throws Exception {
        // TODO needs to be able to run anywhere
        InputFilesLoader.INSTANCE.loadDescriptors("C:\\Users\\Sharon\\IdeaProjects\\FilesMonitor\\src\\main\\resources\\config\\c.config");
        List<HttpFileDescriptor> descriptors = InputFilesLoader.INSTANCE.getHttpFileDescriptors();
        Assert.assertTrue(descriptors.size()==3);
    }

    @Test
    public void getAllIps() throws Exception {

        Set<HttpFileDescriptor> result = InputFilesLoader.INSTANCE.getAllIps("http://www.ynetnews.com/home/0,7340,L-3083,00.html *");
        Assert.assertFalse(result.isEmpty());
        for (HttpFileDescriptor fileDescriptor : result) {
            Assert.assertTrue(fileDescriptor.isValid());
        }
    }

}