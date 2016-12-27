**Context:**

We need to constantly monitor some important public files/pages to make sure their content doesn't change (unless we meant to).


**Mission:**

Read a list of URLs (http or https) from a configuration file. Some URLs are hosted by multiple servers, and we want to check all of them. The configuration file has pairs of URL+IP, where IP can be * (which means perform normal DNS query to obtain the IP). An example for a configuration file:

<br>
http://somehost.trusteer.com/file1.txt  10.0.0.1
</br>
<br>
http://somehost.trusteer.com/file1.txt  10.0.0.2
</br>
<br>
http://somehost.trusteer.com/file1.txt  10.0.0.3
</br>
<br>
http://somehost.trusteer.com/file2.txt  10.0.0.1
</br>
<br>
https://somehost.trusteer.com/file2.txt  10.0.0.1
</br>
<br>
http://somehost.trusteer.com/file2.txt  10.0.0.2
</br>
<br>
https://somehost.trusteer.com/file2.txt  10.0.0.2
</br>
<br>
http://somehost.trusteer.com/file2.txt  10.0.0.3
</br>
<br>
https://somehost.trusteer.com/file2.txt  10.0.0.3
</br>
<br>
http://another.trusteer.com/file3.txt  *
</br>
<br>
https://another.trusteer.com/file3.txt  *
</br>
<br>
http://another.trusteer.com/file4.txt  *
</br>
<br>
https://another.trusteer.com/file4.txt  *
</br>
<br></br>
<br></br>
<p>
For each URL, download the page and calculate a SHA1 hash of the HTTP response body. Whenever the hash value changes, send an email alert once. The email should include the affected URL and IP.
This check should occur every 10 minutes.
</p>

**Implementation requirements:**

- The program may be a combination of programs/scripts.
- Use any programming language(s). You may use any standard libraries or unix tools as part of the solution.
- The program should write a log of its operations to a log file.