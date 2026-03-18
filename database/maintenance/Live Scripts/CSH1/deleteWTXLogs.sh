#!/bin/bash

## First find all logs older than 14 days
## Start in the /opt/moj/csh1/applogs folder
## Log files can be *.log or *.aud
cd /opt/moj/csh1/applogs

rm /opt/moj/csh1/applogs/audLogFilesToDelete.txt /opt/moj/csh1/applogs/logFilesToDelete.txt
find . -name "*.aud" -mtime +2 > /opt/moj/csh1/applogs/audLogFilesToDelete.txt
find . -name "*.log" -mtime +2 > /opt/moj/csh1/applogs/logFilesToDelete.txt
date
echo Have now found all the log files to delete


## Now loop round file generated and delete .aud files
while read fileToDelete
do
  rm $fileToDelete
done < <(cat /opt/moj/csh1/applogs/audLogFilesToDelete.txt)


## Now loop round file generated and delete .log files
while read fileToDelete
do
  rm $fileToDelete
done < <(cat /opt/moj/csh1/applogs/logFilesToDelete.txt)
date
echo Done...
