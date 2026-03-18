/*Run initial cursor*/
SELECT xcr.court_room_id
    ,      crudm.am_time_civ_hours 
    ,      crudm.am_time_civ_mins 
    ,      crudm.am_time_hours 
    ,      crudm.am_time_mins
    ,      crudm.pm_time_civ_hours 
    ,      crudm.pm_time_civ_mins 
    ,      crudm.pm_time_hours 
    ,      crudm.pm_time_mins
    ,      crudm.sitting_date
    ,      crudm.courtroom_no
    ,      crudm.crest_court_id
    FROM xhbstg_courtroom_usage_dm crudm
    ,    xhbstg_courtroom_dm crdm
    ,    xhibit.xhb_court_room xcr
    ,    xhibit.xhb_court_site xcs
    ,    xhibit.xhb_court xc
    WHERE crudm.courtroom_no = crdm.courtroom_no
    AND   crudm.ctl_id = crdm.ctl_id
    AND   xcr.court_site_id = xcs.court_site_id
    AND   xcs.court_id = xc.court_id
    AND   xc.crest_court_id = crudm.crest_court_id
    AND   crudm.crest_court_id = :p_crest_court_id
    AND   xcr.crest_court_room_no = crudm.courtroom_no
    AND   nvl(xcr.obs_ind,'N') <> 'Y'
    AND   nvl(xcs.obs_ind,'N') <> 'Y'
    AND   nvl(xc.obs_ind,'N') <> 'Y';
	

"COURT_ROOM_ID"               "AM_TIME_CIV_HOURS"           "AM_TIME_CIV_MINS"            "AM_TIME_HOURS"               "AM_TIME_MINS"                "PM_TIME_CIV_HOURS"           "PM_TIME_CIV_MINS"            "PM_TIME_HOURS"               "PM_TIME_MINS"                "SITTING_DATE"                "COURTROOM_NO"                "CREST_COURT_ID"              
"8112"                        ""                            ""                            "2"                           "30"                          ""                            ""                            "2"                           "15"                          "25-JUN-2018"                 "1"                           "453"                         
"8112"                        ""                            ""                            "1"                           "45"                          ""                            ""                            "2"                           "39"                          "14-JUN-2018"                 "1"                           "453"                         
"8112"                        ""                            ""                            "1"                           "45"                          ""                            ""                            "3"                           "14"                          "13-JUN-2018"                 "1"                           "453"                         
"8112"                        ""                            ""                            "9"                           "30"                          ""                            ""                            "5"                           "0"                           "16-MAR-2009"                 "1"                           "453"                         
"8112"                        ""                            ""                            "9"                           "9"                           ""                            ""                            ""                            ""                            "25-NOV-2008"                 "1"                           "453"                         
	

--5 rows returned.  All with court room no of 1.  After running, there should be 5 rows inserted


--double check the usage staging table
select * from xhbstg_courtroom_usage_dm where crest_court_id = 453;

"CRU_ID"                      "SITTING_DATE"                "PM_TIME_MINS"                "AM_TIME_CIV_HOURS"           "PM_TIME_CIV_MINS"            "SITE_CODE"                   "PM_TIME_CIV_HOURS"           "AM_TIME_CIV_MINS"            "PM_TIME_HOURS"               "AM_TIME_MINS"                "CTL_ID"                      "AM_TIME_HOURS"               "COURTROOM_NO"                "CREST_COURT_ID"              "XHIBIT_COURT_ID"             "XHIBIT_ETL_STATUS"           "XHIBIT_LOADED_DATE"          "XHIBIT_ENRICH_DATE"          "XHIBIT_ETL_DATE"             "XHIBIT_ETL_ERR_MESSAGE"      
"29036"                       "25-NOV-2008"                 ""                            ""                            ""                            "A"                           ""                            ""                            ""                            "9"                           "4"                           "9"                           "1"                           "453"                         ""                            ""                            "14-AUG-2018"                 ""                            ""                            ""                            
"29037"                       "16-MAR-2009"                 "0"                           ""                            ""                            "A"                           ""                            ""                            "5"                           "30"                          "4"                           "9"                           "1"                           "453"                         ""                            ""                            "14-AUG-2018"                 ""                            ""                            ""                            
"29038"                       "13-JUN-2018"                 "14"                          ""                            ""                            "A"                           ""                            ""                            "3"                           "45"                          "4"                           "1"                           "1"                           "453"                         ""                            ""                            "14-AUG-2018"                 ""                            ""                            ""                            
"29039"                       "14-JUN-2018"                 "39"                          ""                            ""                            "A"                           ""                            ""                            "2"                           "45"                          "4"                           "1"                           "1"                           "453"                         ""                            ""                            "14-AUG-2018"                 ""                            ""                            ""                            
"29040"                       "25-JUN-2018"                 "15"                          ""                            ""                            "A"                           ""                            ""                            "2"                           "30"                          "4"                           "2"                           "1"                           "453"                         ""                            ""                            "14-AUG-2018"                 ""                            ""                            ""                            


begin
 dbms_output.enable(10000000);
 dm_process_pkg_cc.ins_xhb_crtrm_usage_with_crest (453);
end;


CREST - COURT : 453 - Starting process of inserting new rows into XHB_COURT_USAGE for new data from CREST
 
INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 2 am_time_mins 30 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours 2 pm_time_mins 15 sitting_date 25-JUN-2018
 
CTX-2190:XHB_COURT_ROOM_USAGE - INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 2 am_time_mins 30 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours 2 pm_time_mins 15 sitting_date 25-JUN-2018 with ETL_STATUS N - Inserted no of rows : 1
 
CTX-2190:xhbstg_courtroom_usage_dm - Crest Court : 453 - Updated no of rows : 5
CREST - COURT : 453 - Starting process of inserting row in XHB_COURT_ROOM_USAGE for new columns with the required data from CREST
 
INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 1 am_time_mins 45 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours 2 pm_time_mins 39 sitting_date 14-JUN-2018
 
CTX-2190:XHB_COURT_ROOM_USAGE - INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 1 am_time_mins 45 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours 2 pm_time_mins 39 sitting_date 14-JUN-2018 with ETL_STATUS U - Inserted no of rows : 1
 
CTX-2190:xhbstg_courtroom_usage_dm - Crest Court : 453 - Updated no of rows : 5
CREST - COURT : 453 - Starting process of inserting row in XHB_COURT_ROOM_USAGE for new columns with the required data from CREST
 
INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 1 am_time_mins 45 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours 3 pm_time_mins 14 sitting_date 13-JUN-2018
 
CTX-2190:XHB_COURT_ROOM_USAGE - INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 1 am_time_mins 45 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours 3 pm_time_mins 14 sitting_date 13-JUN-2018 with ETL_STATUS U - Inserted no of rows : 1
 
CTX-2190:xhbstg_courtroom_usage_dm - Crest Court : 453 - Updated no of rows : 5
CREST - COURT : 453 - Starting process of inserting row in XHB_COURT_ROOM_USAGE for new columns with the required data from CREST
 
INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 9 am_time_mins 30 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours 5 pm_time_mins 0 sitting_date 16-MAR-2009
 
CTX-2190:XHB_COURT_ROOM_USAGE - INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 9 am_time_mins 30 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours 5 pm_time_mins 0 sitting_date 16-MAR-2009 with ETL_STATUS U - Inserted no of rows : 1
 
CTX-2190:xhbstg_courtroom_usage_dm - Crest Court : 453 - Updated no of rows : 5
CREST - COURT : 453 - Starting process of inserting row in XHB_COURT_ROOM_USAGE for new columns with the required data from CREST
 
INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 9 am_time_mins 9 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours  pm_time_mins  sitting_date 25-NOV-2008
 
CTX-2190:XHB_COURT_ROOM_USAGE - INSERTING into XHB_COURT_ROOM_USAGE - processing crest court id - 453 court_room_id 8112 am_time_civ_hours  am_time_civ_mins  am_time_hours 9 am_time_mins 9 pm_time_civ_hours  pm_time_civ_mins  pm_time_hours  pm_time_mins  sitting_date 25-NOV-2008 with ETL_STATUS U - Inserted no of rows : 1
 
CTX-2190:xhbstg_courtroom_usage_dm - Crest Court : 453 - Updated no of rows : 5
CREST - COURT : 453 - Starting process of inserting row in XHB_COURT_ROOM_USAGE for new columns with the required data from CREST
 
CTX-2190:XHB_COURT_ROOM_USAGE processed 5 for CREST_COURT_ID : 453 successfully!
XHB_COURT_ROOM_USAGE Updated for CREST_COURT_ID : 453 successfully!


--check data has gone in
select * from xhbstg_courtroom_usage_dm where crest_court_id = 453;
"CRU_ID"                      "SITTING_DATE"                "PM_TIME_MINS"                "AM_TIME_CIV_HOURS"           "PM_TIME_CIV_MINS"            "SITE_CODE"                   "PM_TIME_CIV_HOURS"           "AM_TIME_CIV_MINS"            "PM_TIME_HOURS"               "AM_TIME_MINS"                "CTL_ID"                      "AM_TIME_HOURS"               "COURTROOM_NO"                "CREST_COURT_ID"              "XHIBIT_COURT_ID"             "XHIBIT_ETL_STATUS"           "XHIBIT_LOADED_DATE"          "XHIBIT_ENRICH_DATE"          "XHIBIT_ETL_DATE"             "XHIBIT_ETL_ERR_MESSAGE"      
"29036"                       "25-NOV-2008"                 ""                            ""                            ""                            "A"                           ""                            ""                            ""                            "9"                           "4"                           "9"                           "1"                           "453"                         "81"                          "U"                           "14-AUG-2018"                 "29-AUG-2018"                 "29-AUG-2018"                 ""                            
"29037"                       "16-MAR-2009"                 "0"                           ""                            ""                            "A"                           ""                            ""                            "5"                           "30"                          "4"                           "9"                           "1"                           "453"                         "81"                          "U"                           "14-AUG-2018"                 "29-AUG-2018"                 "29-AUG-2018"                 ""                            
"29038"                       "13-JUN-2018"                 "14"                          ""                            ""                            "A"                           ""                            ""                            "3"                           "45"                          "4"                           "1"                           "1"                           "453"                         "81"                          "U"                           "14-AUG-2018"                 "29-AUG-2018"                 "29-AUG-2018"                 ""                            
"29039"                       "14-JUN-2018"                 "39"                          ""                            ""                            "A"                           ""                            ""                            "2"                           "45"                          "4"                           "1"                           "1"                           "453"                         "81"                          "U"                           "14-AUG-2018"                 "29-AUG-2018"                 "29-AUG-2018"                 ""                            
"29040"                       "25-JUN-2018"                 "15"                          ""                            ""                            "A"                           ""                            ""                            "2"                           "30"                          "4"                           "2"                           "1"                           "453"                         "81"                          "U"                           "14-AUG-2018"                 "29-AUG-2018"                 "29-AUG-2018"                 ""                            


SELECT * FROM xhbstg_data_migration_log WHERE action_name = 'ins_xhb_crtrm_usage_with_crest- CTX-2190';
"DATA_MIGRATION_LOG_ID"       "COURT_ID"                    "ACTION_NAME"                 "RUN_TIME"                    "LOG_MESSAGE_TYPE"            "LOG_MESSAGE"                 "ERROR_ROW_COUNT"             "SUCCESS_ROW_COUNT"           "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  
"74006"                       "453"                         "ins_xhb_crtrm_usage_with_crest- CTX-2190""29-AUG-2018"                 "E"                           "ins_xhb_crtrm_usage_with_crest - Error processing"""                            "0"                           "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"74007"                       "453"                         "ins_xhb_crtrm_usage_with_crest- CTX-2190""29-AUG-2018"                 "E"                           "ins_xhb_crtrm_usage_with_crest - Error processing"""                            "0"                           "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"74008"                       "453"                         "ins_xhb_crtrm_usage_with_crest- CTX-2190""29-AUG-2018"                 "E"                           "ins_xhb_crtrm_usage_with_crest - Error processing"""                            "0"                           "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"74009"                       "453"                         "ins_xhb_crtrm_usage_with_crest- CTX-2190""29-AUG-2018"                 "I"                           "XHB_COURT_ROOM_USAGE - Starting process of inserting new rows into XHB_COURT_USAGE for new data from CREST"""                            ""                            "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"74020"                       "453"                         "ins_xhb_crtrm_usage_with_crest- CTX-2190""29-AUG-2018"                 "I"                           "XHB_COURT_ROOM_USAGE : Processed 5 successfully!"""                            "5"                           "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"74021"                       "453"                         "ins_xhb_crtrm_usage_with_crest- CTX-2190""29-AUG-2018"                 "I"                           "XHB_COURT_ROOM_USAGE : updating xhbstg_courtroom_usage_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  453"""                            "0"                           "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              

--Check the xhibit usage table for the new records
select * from xhibit.xhb_court_room_usage where last_updated_by = 'DATA MIGRATION';

"COURT_ROOM_USAGE_ID"         "COURT_ROOM_ID"               "AM_TIME_CIV_HOURS"           "AM_TIME_CIV_MINS"            "AM_TIME_HOURS"               "AM_TIME_MINS"                "PM_TIME_CIV_HOURS"           "PM_TIME_CIV_MINS"            "PM_TIME_HOURS"               "PM_TIME_MINS"                "SITTING_DATE"                "LAST_UPDATE_DATE"            "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  "VERSION"                     "OBS_IND"                     
"6"                           "8112"                        ""                            ""                            "2"                           "30"                          ""                            ""                            "2"                           "15"                          "25-JUN-2018"                 "29-AUG-2018"                 "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              "1"                           "N"                           
"7"                           "8112"                        ""                            ""                            "1"                           "45"                          ""                            ""                            "2"                           "39"                          "14-JUN-2018"                 "29-AUG-2018"                 "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              "1"                           "N"                           
"8"                           "8112"                        ""                            ""                            "1"                           "45"                          ""                            ""                            "3"                           "14"                          "13-JUN-2018"                 "29-AUG-2018"                 "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              "1"                           "N"                           
"9"                           "8112"                        ""                            ""                            "9"                           "30"                          ""                            ""                            "5"                           "0"                           "16-MAR-2009"                 "29-AUG-2018"                 "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              "1"                           "N"                           
"10"                          "8112"                        ""                            ""                            "9"                           "9"                           ""                            ""                            ""                            ""                            "25-NOV-2008"                 "29-AUG-2018"                 "29-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              "1"                           "N"                           


rollback;	
	