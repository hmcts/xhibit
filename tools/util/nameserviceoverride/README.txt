Purpose
=======

The purpose of the nameserviceoverride.jar is to override the default DNS lookups with a custom
name space which we define in a file.  For example if you want XDEVXPG to resolve to 127.0.0.1 you
can do this by supplying a custom DNS names mapping file.  This can be done per process, so you could
have several Xhibit thick client applications running, each of which resolves XDEVXPG into a different
IP address.

Instructions for use
====================

To use the name service override, edit your xhibit.bat file and add the following 4 lines:

  SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dsun.net.spi.nameservice.provider.1=dns,LocalManagedDNS
  SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dsun.net.spi.nameservice.provider.2=dns,sun
  SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dsun.net.inetaddr.ttl=0
  SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dsun.net.inetaddr.negative.ttl=0

then edit the XHIBIT_CLASSPATH setting and add the nameserviceoverride.jar file to 
it (use a full explicit file path if you need to), e.g.

  SET XHIBIT_CLASSPATH=F:\Projects\XHIBIT\XHIBIT\tools\util\nameserviceoverride\dist\nameserviceoverride.jar;..\properties;......

Then copy the hosts file into a file called jhosts.txt:

  cp C:\WINDOWS\system32\drivers\etc\hosts C:\Documents and Settings\hewittm\jhosts.txt

(Replace hewittm with your username). Now run xhibit from the batch file.  
Any DNS lookups will be done from the jhosts.txt file.

Alternatively if you wish to use a different filename for the name lookups edit the xhibit.bat 
file again and add the following line:

  SET JAVA_OPTIONS=%JAVA_OPTIONS% -Dnameserviceoverride.hosts.filename="C:\Documents and Settings\hewittm\jhosts2.txt"

which will override the default filename.  Using this method you can have many batch files, each
of which uses a different file for name resolution.

