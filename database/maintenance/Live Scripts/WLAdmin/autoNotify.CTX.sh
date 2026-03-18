#!/bin/ksh
SCRIPT_DIR=/opt/customer/bea/user_projects/domains/XHIBIT_LIVE/common/publicDisplayNotifier/
cd $SCRIPT_DIR

NOTIFY_SCRIPT=$SCRIPT_DIR/notify.sh
NOTIFY_DATE=`date +\%d-\%b-\%y`
TIME=`date +%H_%M_%S`

if [ ! -x $NOTIFY_SCRIPT ]
 then
  echo "$NOTIFY_SCRIPT NOT FOUND "
  exit
fi

LOGDIR=$SCRIPT_DIR/logs
LOGFILE=${LOGDIR}/notifiy.${NOTIFY_DATE}_${TIME}

[ ! -d $LOGDIR ]  && mkdir -p $LOGDIR

COURT_ID_OUTFILE=$SCRIPT_DIR/court_id.txt.ctx


#---------------------------------------------
function notify_run {

#ssh oracle@x.y.70.193 '/opt/moj/home/oracle/xhibitsupport/scripts/countScript.sh' > $COURT_ID_OUTFILE


#----------------------------
if [ -r $COURT_ID_OUTFILE ]
then
 for id in `cat $COURT_ID_OUTFILE`
  do
  
 echo "here" 
 echo "$NOTIFY_SCRIPT $id $NOTIFY_DATE"
    $NOTIFY_SCRIPT $id $NOTIFY_DATE
 done
else
 echo "$COURT_ID_OUTFILE NOT FOUND"
fi
}

#####################
notify_run | tee -a $LOGFILE

