#!/bin/bash
# based on http://stackoverflow.com/questions/29816328/schedule-cron-entries-to-run-script-only-when-not-already-running

export FILES_MONITOR_HOME=$1

pID=$(pgrep -n "dsysme-files-monitor-0.1.0.jar")

# Check if jarfile is running
if $pID > /dev/null
then
    #log something to syslog
    logger  $pID "already running. not restarting."
else
    # start jar file
    logger $0 "Running FilesMonitor"
    /usr/bin/java -jar -Dfile-monitor-config.dir=$FILES_MONITOR_HOME/config -Dlogback.configurationFile=$FILES_MONITOR_HOME/logback.xml $FILES_MONITOR_HOME/dsysme-files-monitor-0.1.0.jar
fi