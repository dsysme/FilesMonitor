package com.trusteer.interview.dsysme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class responsibility is to return a list of input file names.
 * config directory should be pass as named argument (file-monitor-config.dir) to main
 *
 * TODO: Consider optimizing by keeping past result and recollecting only on explicit demand
 *
 */
public enum InputFilesCollector {

    INSTANCE;

    final static Logger logger = LoggerFactory.getLogger(InputFilesCollector.class);

    public List<String> collectConfigurationFiles(String folderName) throws Exception {
        File folder = new File(folderName);
        if (folder == null || !folder.isDirectory() || !folder.exists()) {
            logger.error("missing or bad 'file-monitor-config.dir' named argument");
            throw new Exception("missing or bad 'file-monitor-config.dir' named argument");
        }
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null || listOfFiles.length == 0) {
            logger.error("No configuration files were found in "+folderName);
            throw new Exception("No configuration files were found in "+folderName);
        }
        List<String> result = Arrays.stream(listOfFiles).filter(file -> file.isFile()).map(file -> file.getAbsolutePath()).collect(Collectors.toList());
        if (result == null || result.isEmpty()) {
            logger.error("No configuration files were found in "+folderName);
            throw new Exception("No configuration files were found in "+folderName);
        }
        return result;
    }
}
