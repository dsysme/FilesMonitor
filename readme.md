**Context:**

We need to constantly monitor some important public files/pages to make sure their content doesn't change (unless we meant to).


**Mission:**

Read a list of URLs (http or https) from a configuration file. Some URLs are hosted by multiple servers, and we want to check all of them. The configuration file has pairs of URL+IP, where IP can be * (which means perform normal DNS query to obtain the IP). An example for a configuration file:

http://somehost.trusteer.com/file1.txt  10.0.0.1
http://somehost.trusteer.com/file1.txt  10.0.0.2
http://somehost.trusteer.com/file1.txt  10.0.0.3
http://somehost.trusteer.com/file2.txt  10.0.0.1
https://somehost.trusteer.com/file2.txt  10.0.0.1
http://somehost.trusteer.com/file2.txt  10.0.0.2
https://somehost.trusteer.com/file2.txt  10.0.0.2
http://somehost.trusteer.com/file2.txt  10.0.0.3
https://somehost.trusteer.com/file2.txt  10.0.0.3
http://another.trusteer.com/file3.txt  *
https://another.trusteer.com/file3.txt  *
http://another.trusteer.com/file4.txt  *
https://another.trusteer.com/file4.txt  *


For each URL, download the page and calculate a SHA1 hash of the HTTP response body. Whenever the hash value changes, send an email alert once. The email should include the affected URL and IP.

This check should occur every 10 minutes.

**Implementation requirements:**

- The program may be a combination of programs/scripts.
- Use any programming language(s). You may use any standard libraries or unix tools as part of the solution.
- The program should write a log of its operations to a log file.

**Implementation and installation**
There are 3 logs you can follow:
1. log for the scheduled runs in /tmp/FilesMonitorScheduler.log
2. log for the FilesMonitor program FilesMonitor.log
3. log holds only the file modification alerts: FileModifications.log

If I had more time, I would have added a build distribution task, but for now preparing the run environment needs to be manual. Here are the instruction:

**Prerequisites:**
1. java 8 jdk insallation
2. gradle installation 

**Preparing the development environment:**
1. extract the zip into a folder of your choice: e.g. /home/dsysme/dev
2. compile the code using gradle: e.g. gradle build jar

**Preparing the run environment:**
1. create a folder for the input and output files: e.g.'mkdir /home/dsysme/FilesMonitor' after first run a file named MonitoredFiles.ser will be created. You need to delete it whenever you want to change configuration files
2. 'cd  /home/dsysme/FilesMonitor'
3. create a sub folder for the logs: e.g. 'mkdir /home/dsysme/FilesMonitor/logs'
4. create a sub folder named config for configuration files (pairs of urls and ips): e.g. 'mkdir /home/dsysme/FilesMonitor/config'
5. copy the configuration files: e.g. 'cp ~/dev/FilesMonitor/build/resources/main/config/* ./config'
6. copy the jar file to that folder: e.g. 'cp ~/dev/FilesMonitor/build/libs/dsysme-files-monitor-0.1.0.jar .'
7. copy the shall script: e.g. 'cp ~/dev/FilesMonitor/src/main/FilesMonitor.sh .'
8. make sure the FilesMonitor.sh had run permissions (if not 'chmod 777 FilesMonitor.sh)
9. copy the logback.xml configuration file: e.g. 'cp ~/dev/FilesMonitor/src/main/java/logback.xml .
10. edit ~/dev/FilesMonitor/src/main/java/logback.xml to set the position of the log file FilesMonitor.log to the logs folder you created 
11. add to crontab a scheduled run of the script you copied:
12. crontab -e
    - in the file that opened add the following line: 
        */10 * * * * /home/dsysme/FilesMonitor.sh /home/dsysme/FilesMonitor /home/dsysme/FilesMonitor.sh /home/dsysme/FilesMonitor (you need of course to fix the path to your path)
    - use 'tail -f /tmp/FilesMonitorScheduler.log' to make sure the script is running on schedule 
13. after setting the crontab the program should run on hh:10, hh:20, hh:30, hh:40, hh:50, hh:00. The logs are being appended on each run.
14. you can check the you receive emails by logging in  to dsysme.interview@gmail.com (password 'dontpanic')

**TODO**
- add more unit tests (especially test cases for exceptions)
- performance consideration
- support distribution task in build.gradle
- extract to configuration files some hardcoded parameters such as the gmail account used for sending the email alerts
- add more url ip pairs to configuration file
- install a small web server on my computer to get the full control of the files.
- support dependency injection
- add mock framework such as mockito
