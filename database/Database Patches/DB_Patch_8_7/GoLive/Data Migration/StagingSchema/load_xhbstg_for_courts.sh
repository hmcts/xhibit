#!/bin/bash

############################################################################################################################
########  Shell  script to loop through the csv files for the requested court and run the respective SQLLDR scripts ########
############################################################################################################################




############################### Set court_id = command line parameter 1 #################################

court_id=$1

############################## Check court id is populated - validated if script run on its own ########################

while [ "$court_id" == "" ]
do
    read -p "Enter Court id XXX from which csv files are to be loaded : " court_id
done

######################### Set log filename ###########################

curr_time=$(date +'%d/%m/%Y %r')
curr_time_string=$(date +'%d%m%Y_%H_%M_%S')
log_filename="court_"$court_id"_sqlldr_log_"$curr_time_string".log"

echo "####################################################################################################" > $log_filename
echo $curr_time"=> Starting SQLLDR for loading csv files from Court with ID : "$court_id >> $log_filename
echo "####################################################################################################" >> $log_filename
echo "" >> $log_filename

############################### Set csv folder #######################

csv_folder="court_"$court_id"_crest_csv_files"
csv_folder_files=$csv_folder"/*.csv"

################# Define Folders for each court ######################

log_folder="court_"$court_id"_sqlldr_log_files"
bad_folder="court_"$court_id"_sqlldr_bad_files"
dsc_folder="court_"$court_id"_sqlldr_dsc_files"
processed_folder="court_"$court_id"_processed_csv_files"

######################################################################

##################################### set CREST Tablenames ###########################


tableName[0]="bw_history"
tableName[1]="case"
tableName[2]="case_note"
tableName[3]="case_opposer"
tableName[4]="case_party_sof"
tableName[5]="case_subject"
tableName[6]="courtroom_location"
tableName[7]="courtroom"
tableName[8]="courtroom_usage"
tableName[9]="disposal"
tableName[10]="home_court"
tableName[11]="judge_ticket"
tableName[12]="judge_usage"
tableName[13]="legal_aid_order"
tableName[14]="non_avail_dates"
tableName[15]="subject"
tableName[16]="committal_charge"
tableName[17]="legal_aid_amendment"
tableName[18]="case_history"
tableName[19]="case_hearing_day"
tableName[20]="case_subject_appearance"
tableName[21]="chambers"
tableName[22]="solicitor_firm"
tableName[23]="subject_history"
tableName[24]="lists"
tableName[25]="courtroom_day"
tableName[26]="charge"
tableName[27]="warned_list_details"
tableName[28]="csu_history"
tableName[29]="release_judge"
tableName[30]="opposer"

######################################################################################


################## Check the csv folder exists and corresponding csv files received #################

if [ -d $csv_folder ]; then
  if ls $csv_folder_files 1> /dev/null 2>&1; then
  
      echo "" >> $log_filename 
      curr_time=$(date +'%d/%m/%Y %r')
      echo $curr_time"=> CSV files found for court id : "$court_id" - Starting SQLLDR " >> $log_filename
      echo "" >> $log_filename 


      for i in "${tableName[@]}"  
      do 
         
         #################  check for each table if csv file available and process SQLLDR step for the table ##################

         export crest_tablename=$i
         echo $crest_tablename
         export xhbstg_tablename="xhbstg_"$i"_dm"
	 echo $xhbstg_tablename
         export xhb_in_file=$csv_folder"/court_"$court_id"_"$crest_tablename".csv"
	 echo $xhb_in_file
        

         current_time=$(date +'%d/%m/%Y %r');
         echo "#################################################################################################" >> $log_filename; 
         echo "###  "$current_time"=> LOAD PROCESS - START - Table : "$xhbstg_tablename"  ###" >> $log_filename;
         echo "#################################################################################################" >> $log_filename; 
           
         ################## check ctl file exists and get ctx number from the ctl filename #################

         export ctl_file=$xhbstg_tablename"_ctx_????.ctl"
	echo $ctl_file
       
       if [ -f $ctl_file ]; then
         export ctx_no=`ls $ctl_file | tr -dc [0-9]`
          
         curr_time=$(date +'%d/%m/%Y %r')
         curr_time_string=$(date +'%d%m%Y_%H_%M_%S')
        
         ################# set parameters for the SQLLDR par file ###################
 
         export xhb_ctl_filename=$xhbstg_tablename"_ctx_"$ctx_no".ctl" 
         export xhb_log_filename=$log_folder/"court_"$court_id"_"$xhbstg_tablename"_ctx_"$ctx_no"_"$curr_time_string".log"      
         export xhb_bad_filename=$bad_folder/"court_"$court_id"_"$xhbstg_tablename"_ctx_"$ctx_no"_"$curr_time_string".bad"      
         export xhb_dsc_filename=$dsc_folder/"court_"$court_id"_"$xhbstg_tablename"_ctx_"$ctx_no"_"$curr_time_string".dsc"      


         current_time=$(date +'%d/%m/%Y %r');
         echo "" >> $log_filename; 
         echo $current_time"=> Looking for csv file "$xhb_in_file" from Court ID : "$court_id >> $log_filename;
         echo "" >> $log_filename; 
        

         #################### check if csv file present ################ 
         
         if [ -f $xhb_in_file ]; then

            current_time=$(date +'%d/%m/%Y %r');
            echo "" >> $log_filename; 
            echo $current_time"=> Found csv file "$xhb_in_file" from Court ID : "$court_id >> $log_filename;
            echo "" >> $log_filename; 
           
 
            current_time=$(date +'%d/%m/%Y %r');
            echo "" >> $log_filename; 
            echo $current_time"=> Checking and Creating folders for LOG , BAD and DISCARD files for table "$xhbstg_tablename" - Court ID : "$court_id >> $log_filename;
            echo "" >> $log_filename; 
           
            ################## check and create log, bad, dsc folders ############## 
            
            mkdir -p $log_folder
            mkdir -p $bad_folder
            mkdir -p $dsc_folder
            mkdir -p $processed_folder
      
            curr_time=$(date +'%d/%m/%Y %r')
            echo $curr_time"=> Calling SQLLDR to load "$xhbstg_tablename" table , for court id : "$court_id >> $log_filename
            echo "" >> $log_filename 

            ######## run SQLLDR script with par file where the parameters are set with variables defined above ####


            ################ check PAR file present and run SQLLDR ############

            if [ -f crest_csv_to_xhbstg_table.par ]; then

               sqlldr PARFILE=crest_csv_to_xhbstg_table.par
               
               curr_time=$(date +'%d/%m/%Y %r')
               echo "" >> $log_filename 
               echo $curr_time"=> SQLLDR Loading process complete for table "$xhbstg_tablename" , for court id : "$court_id >> $log_filename
               echo "" >> $log_filename 
               echo $curr_time"=> Moving csv file "$xhb_in_file" to "$processed_folder >> $log_filename
               echo "" >> $log_filename 

               ################### move csv file to processed folder ##################
            
               mv $xhb_in_file $processed_folder

            else

               curr_time=$(date +'%d/%m/%Y %r')
               echo "" >> $log_filename 
               echo $curr_time"=> PAR file crest_csv_to_xhbstg_table.par NOT FOUND for running SQLLDR for table "$xhbstg_tablename" , for court id : "$court_id >> $log_filename
               echo "" >> $log_filename 
               
            fi
         else
        
            ################ No csv file present ##################  
            
            curr_time=$(date +'%d/%m/%Y %r')
            echo $curr_time"=> CSV File "$xhb_in_file" NOT FOUND, skipping SQLLDR processing for table XHBSTG_HOME_COURT_DM table , for court id : "$court_id >> $log_filename
            echo "" >> $log_filename

         fi

      else

         current_time=$(date +'%d/%m/%Y %r');
         echo "" >> $log_filename;  
         echo $current_time"=> NO CTL file found for table "$xhbstg_tablename" to run SQLLDR for Court ID : "$court_id >> $log_filename; 
         echo "" >> $log_filename; 
      
      fi

     done

         current_time=$(date +'%d/%m/%Y %r');
         echo "" >> $log_filename;  
         echo $current_time"=> END OF PROCESSING csv files - from Court ID : "$court_id >> $log_filename; 
         echo "" >> $log_filename; 
  
  else

     ################# No csv files found for court ##########

      curr_time=$(date +'%d/%m/%Y %r')
      echo $curr_time"=> No CSV files found for court id : "$court_id" - Nothing to load to XHBSTG " >> $log_filename
      echo "" >> $log_filename; 

  fi

else
 
      #######################  No csv files FOLDER found for court ####################

      curr_time=$(date +'%d/%m/%Y %r')
      echo $curr_time"=> No CSV files FOLDER found for court id : "$court_id" - Nothing to load to XHBSTG " >> $log_filename
      echo "" >> $log_filename; 

fi

curr_time=$(date +'%d/%m/%Y %r')
echo "####################################################################################################" >> $log_filename
echo $curr_time"=> END of SQLLDR Processing for Court with ID : "$court_id >> $log_filename
echo "####################################################################################################" >> $log_filename
echo "" >> $log_filename
