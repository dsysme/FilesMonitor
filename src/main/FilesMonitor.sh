#!/bin/bash
# based on http://stackoverflow.com/questions/29816328/schedule-cron-entries-to-run-script-only-when-not-already-running

export FILES_MONITOR_HOME=$1

pID=$(pgrep -n "dsysme-files-monitor-0.1.0.jar")

# Check if jarfile is running
if [[ ! -z $pID ]]
then
    #log something to syslog
    echo "already running. not restarting: $(date)" >> /tmp/FilesMonitorScheduler.log
else
    # start jar file
     echo "Starting FilesMonitor: $(date)" >> /tmp/FilesMonitorScheduler.log
    /usr/bin/java -jar -Dfile-monitor-config.dir=$FILES_MONITOR_HOME/config -Dlogback.configurationFile=$FILES_MONITOR_HOME/logback.xml $FILES_MONITOR_HOME/dsysme-files-monitor-0.1.0.jar
fi