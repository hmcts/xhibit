SELECT cch.crest_court_id,
           xhc.court_id,
           cch.case_no,
           cch.case_type,
           cch.psd_ct_code,
           cch.comm_date,
           cch.reason_deleted,
           cch.case_title,
           cch.date_archived,
           cch.sent_for_trial_date
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_case_history_dm cch
    where cch.crest_court_id = 453/*p_crest_court_id*/ and
          cch.crest_court_id = xhc.crest_court_id and 
          NVL(cch.xhibit_etl_status,'N') <> 'U' and
          cch.xhibit_enrich_date is NULL  and
          NOT exists 
          (select 'X' from xhibit.xhb_case_history
            where case_number = cch.case_no and 
                  case_type = cch.case_type and
                  court_id = xhc.court_id   and 
                  nvl(obs_ind,'N') != 'Y');
                  
"CREST_COURT_ID"              "COURT_ID"                    "CASE_NO"                     "CASE_TYPE"                   "PSD_CT_CODE"                 "COMM_DATE"                   "REASON_DELETED"              "CASE_TITLE"                  "DATE_ARCHIVED"               "SENT_FOR_TRIAL_DATE"         
"453"                         "81"                          "20147083"                    "T"                           "2725"                        "10-JUL-2018"                 "test deleetion by lisz for qacas""LIZ CASE TO DELETE"          "10-JUL-2018"                 "10-JUL-2018"                 
"453"                         "81"                          "20147072"                    "T"                           "2725"                        "01-MAY-2018"                 "scott asked me to"           "LIZ TODELET"                 "16-MAY-2018"                 "01-MAY-2018"                 

--2 rows returned from the cursor

begin
 dbms_output.enable(10000000);
 dm_process_pkg_ss.upd_xhb_case_history_crest (453);
end;

/*DBMS Output*/
CREST - COURT : 453 - Starting process of inserting rows from CREST CASE_HISTORY table
 
INSERTING XHB_CASE_HISTORY - FOR CREST_COURT_ID = 453
v_case_no : 20147072, v_case_type : T , v_xhibit_court_id 81
 
CTX-2197:XHB_CASE_HISTORY - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2197:XHBSTG_CASE_HISTORY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_CASE_HISTORY - FOR CREST_COURT_ID = 453
v_case_no : 20147083, v_case_type : T , v_xhibit_court_id 81
 
CTX-2197:XHB_CASE_HISTORY - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2197:XHBSTG_CASE_HISTORY_DM - Crest Court : 453 - Updated no of rows : 1
 
CTX-2197:XHBSTG_CASE_HISTORY_DM processed 2 for CREST_COURT_ID : 453 successfully!
 
CTX-2197:XHBSTG_CASE_HISTORY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - 0 for CREST_COURT_ID : 453


--row counts marry up so it has worked

select * from xhbstg_case_history_dm where crest_court_id = 453;
"CASE_TYPE"                   "CASE_NO"                     "PSD_CT_CODE"                 "COMM_DATE"                   "DATE_CLOSED"                 "REASON_DELETED"              "CASE_TITLE"                  "DATE_ARCHIVED"               "SENT_FOR_TRIAL_DATE"         "CREST_COURT_ID"              "XHIBIT_COURT_ID"             "XHIBIT_ETL_STATUS"           "XHIBIT_LOADED_DATE"          "XHIBIT_ENRICH_DATE"          "XHIBIT_ETL_DATE"             "XHIBIT_ETL_ERR_MESSAGE"      
"T"                           "20147072"                    "2725"                        "01-MAY-2018"                 ""                            "scott asked me to"           "LIZ TODELET"                 "16-MAY-2018"                 "01-MAY-2018"                 "453"                         "81"                          "I"                           "14-AUG-2018"                 "03-SEP-2018"                 "03-SEP-2018"                 ""                            
"T"                           "20147083"                    "2725"                        "10-JUL-2018"                 ""                            "test deleetion by lisz for qacas""LIZ CASE TO DELETE"          "10-JUL-2018"                 "10-JUL-2018"                 "453"                         "81"                          "I"                           "14-AUG-2018"                 "03-SEP-2018"                 "03-SEP-2018"                 ""                            

--Staging table updated successfully

SELECT * FROM xhbstg_data_migration_log WHERE action_name = 'upd_xhb_case_history_crest- CTX-2197';
"DATA_MIGRATION_LOG_ID"       "COURT_ID"                    "ACTION_NAME"                 "RUN_TIME"                    "LOG_MESSAGE_TYPE"            "LOG_MESSAGE"                 "ERROR_ROW_COUNT"             "SUCCESS_ROW_COUNT"           "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  
"101571"                      "453"                         "upd_xhb_case_history_crest- CTX-2197""03-SEP-2018"                 "I"                           "XHB_CASE_HISTORY - Starting process of inserting  rows from CREST CASE_HISTORY table"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"101572"                      "453"                         "upd_xhb_case_history_crest- CTX-2197""03-SEP-2018"                 "I"                           "UPDATING XHB_CASE_HISTORY - inserting  case no - 20147072, case_type - T"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"101573"                      "453"                         "upd_xhb_case_history_crest- CTX-2197""03-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_HISTORY_DM - updated case_no 20147072, case_type T row with ETL_STATUS I"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"101574"                      "453"                         "upd_xhb_case_history_crest- CTX-2197""03-SEP-2018"                 "I"                           "UPDATING XHB_CASE_HISTORY - inserting  case no - 20147083, case_type - T"""                            ""                            "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"101575"                      "453"                         "upd_xhb_case_history_crest- CTX-2197""03-SEP-2018"                 "I"                           "UPDATING XHBSTG_CASE_HISTORY_DM - updated case_no 20147083, case_type T row with ETL_STATUS I"""                            "1"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"101576"                      "453"                         "upd_xhb_case_history_crest- CTX-2197""03-SEP-2018"                 "I"                           "XHB_CASE_HISTORY : Processed 2 successfully!"""                            "2"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              
"101577"                      "453"                         "upd_xhb_case_history_crest- CTX-2197""03-SEP-2018"                 "I"                           "XHB_CASE_HISTORY : updating XHBSTG_CASE_HISTORY_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  453"""                            "0"                           "03-SEP-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              


-- all successfull.  