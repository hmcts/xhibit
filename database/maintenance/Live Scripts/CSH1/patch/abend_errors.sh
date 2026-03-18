#!/usr/bin/bash
#
# This script simply monitors the /var/mqsi/common/error directory for
#  abend files and on finding a file, emails it to support and moves it
#  into the archive directory.

. /opt/moj/home/wmbroker/.bash_profile

SMTP_SERVER="SMTPRelayN.dom1.infra.int"
EMAIL_RECIPIENTS="xhibit_support@logica.com"

WORKINGDIR="/opt/moj/home/wmbroker/bin/cron/patch"
ABEND_DIR="/var/mqsi/common/errors"
ARCHIVE_DIR="/var/mqsi/common/errors/archive"
AUDITFILE="$WORKINGDIR/abend.aud"

EMAILCMD="/opt/moj/home/wmbroker/bin/cron/sysmon/sendemail.pl -f xhibitcsh@justice.gov.uk -t $EMAIL_RECIPIENTS -u CSH1 Abend  -s $SMTP_SERVER -m Attached is latest CSH1 abend"

ABEND_FILE_LIST=`ls $ABEND_DIR/*.abend`
echo "File list: $ABEND_FILE_LIST"

if [[ -n $ABEND_FILE_LIST ]]; then
        # there is at least one or more abend files

        for currentRecord in ${ABEND_FILE_LIST}; do
		# get the timestamp of the file
		FILE_TIMESTAMP=`ls -l $currentRecord | awk ' { print $8" on "$7" "$6 } '`

                ${EMAILCMD} -u CSH1 Abend at $FILE_TIMESTAMP -a $currentRecord
		DATETIME=`date '+%Y%m%d %H%M%S'`
		echo "$DATETIME: $currentRecord" >>$AUDITFILE

		/usr/bin/mv -f $currentRecord $ARCHIVE_DIR 
	done
fi
