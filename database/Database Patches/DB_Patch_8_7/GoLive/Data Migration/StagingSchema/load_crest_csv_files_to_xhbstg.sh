#!/bin/bash


current_time=$(date +'%d/%m/%Y %r')
current_time_string=$(date +'%d%m%Y_%H_%M_%S')
run_log_filename="load_crest_csv_to_xhbstg_process_log_"$current_time_string".log"

echo "##############################################################################################" > $run_log_filename
echo $current_time"=> Starting Processing of loading csv files to XHBSTG area..." >> $run_log_filename
echo "##############################################################################################" >> $run_log_filename
echo "" >> $run_log_filename



#---------   below code loops through file c2x_dm_xhibit_court_ids.lst                                              -----------#
#---------   and  for each court id,  calls the script load_xhbstg_for_courts.sh with court id passed as parameter  -----------#

cat court_ids.lst | while read court; do current_time=$(date +'%d/%m/%Y %r');`echo "" >> $run_log_filename`; `echo $current_time"=> Looking for and Starting Processing on files from Court ID : "$court >> $run_log_filename`;`echo "" >> $run_log_filename`; `./load_xhbstg_for_courts.sh "$court"`; current_time=$(date +'%d/%m/%Y %r');`echo "" >> $run_log_filename`;  `echo $current_time"=> END OF PROCESSING on files from Court ID : "$court >> $run_log_filename`; `echo "" >> $run_log_filename`; done

current_time=$(date +'%d/%m/%Y %r')
echo "" >> $run_log_filename
echo "##############################################################################################" >> $run_log_filename
echo $current_time"=> END OF Processing of loading csv files to XHBSTG area..." >> $run_log_filename
echo "##############################################################################################" >> $run_log_filename
