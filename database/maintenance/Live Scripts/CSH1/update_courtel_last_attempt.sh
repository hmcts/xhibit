#!/usr/bin/bash

##############################################################################
#
# To fix a bug in Courtel where records do not process if XHB_COURTEL_LIST.LAST_ATTEMPT_DATETIME is null
#  - Correct fix is to update trigger on XHB_XML_DOCUMENT
#  - This is a workaround till that change can be done
#
# Created : 29/07/2019 - Scott Atwell
#
##############################################################################

SCRIPT_HOME=/opt/moj/home/wmbroker/bin/cron
. $SCRIPT_HOME/setEnv.sh


OUTPUT=`sqlplus -s <username>/<password>@csdbprd1 @$SCRIPT_HOME/sql/update_courtel_last_attempt.sql`
echo $OUTPUT
OUTPUT=`echo $OUTPUT|tr -s ' '|cut -f1,4 -d ' '|tr -s ' ' '2'`
echo $OUTPUT

