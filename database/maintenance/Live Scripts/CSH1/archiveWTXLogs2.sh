#!/bin/sh

#
#This script moves the current wtxaudit/mqsiarchive and wtxaudit/mqsibackout directories. The resulting archive
#directories will be called mqsiarchive-yyyyMMddHHmmss and mqsibackout-yyyyMMddHHmmss. This stops the directories
#becoming unmanagable.
#

dateTime=`date '+%Y%m%d%H%M%S'`
mv /var/mqsi/logs/wtxaudit/mqsibackout /var/mqsi/logs/wtxaudit/mqsibackout-${dateTime}
echo Moved directories at ${dateTime}
