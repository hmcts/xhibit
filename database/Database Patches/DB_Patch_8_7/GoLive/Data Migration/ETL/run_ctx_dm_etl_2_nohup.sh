echo "Enter CREST COURT ID : "
read x_court_id

sqlplus -s data_mig/data_mig@CSDBPRD1 << EOF

column court_name new_val court_name
set linesize 200
set termout off
set feedback off
set verify off
set head off
set echo off

SELECT 'CREST COURT_ID '||$x_court_id||' is Valid',court_name as court_is_valid,court_name from xhibit.xhb_court where crest_court_id = nvl($x_court_id,NULL);

PROMPT "You have selected &court_name , Please confirm in capitals (YES/NO) : "

exit;
EOF

read x_confirm_court
if [ $x_confirm_court == "YES" ]
then
   nohup sqlplus data_mig/data_mig@CSDBPRD1 @CTX_RUN_DM_ETL_2_nohup.sql $x_court_id OFF > /dev/null &
else
   echo "Script cancelled as COURT not confirmed, please try again if needed..."
fi
