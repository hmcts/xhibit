--Initial cursor
SELECT  xc.case_id
    ,       naddm.start_date
    ,       naddm.end_date
    ,       naddm.reason
    FROM XHBSTG_NON_AVAIL_DATES_DM naddm
    ,    xhibit.xhb_case xc
    WHERE naddm.case_no = xc.case_number
    AND   naddm.case_type = xc.case_type
    AND   xc.court_id = 81--v_xhibit_court_id
    AND   trunc(naddm.end_date) > trunc(SYSDATE)
    AND   naddm.crest_court_id = 453--p_crest_court_id
    ;  
    
/*There are no rows being returned in the cursor because there are no end_dates > sysdate in the data set
  For testing, I have removed the clause 'AND   trunc(naddm.end_date) > trunc(SYSDATE)' to return a row
  This was put back in into the procedure after the test was run
*/

SELECT  xc.case_id
    ,       naddm.start_date
    ,       naddm.end_date
    ,       naddm.reason
    FROM XHBSTG_NON_AVAIL_DATES_DM naddm
    ,    xhibit.xhb_case xc
    WHERE naddm.case_no = xc.case_number
    AND   naddm.case_type = xc.case_type
    AND   xc.court_id = 81--v_xhibit_court_id
   --AND   trunc(naddm.end_date) > trunc(SYSDATE)
    AND   naddm.crest_court_id = 453--p_crest_court_id
    ; 

"CASE_ID"                     "START_DATE"                  "END_DATE"                    "REASON"                      
"621476"                      "01-AUG-2008"                 "01-AUG-2008"                 "annual leave"                


/*Run the procdure.  1 row should be inserted into the xhibit table*/
 BEGIN
 dbms_output.enable(10000000);
 dm_process_pkg_cc.upd_xhb_case_nad_with_crest(p_crest_court_id => 453);
 END;
 
 /*dbms output*/
CREST - COURT : 453 - Starting process of inserting data IN XHB_CASE_NON_AVAIL_DAYS with the required data from CREST
 
Insert into XHB_CASE_NON_AVAIL_DAYS - non avail days data into XHB_CASE_NON_AVAIL_DAYS with values :  case_id : 621476 start_date : 01-AUG-2008 end_date : 01-AUG-2008 reason : annual leave
 
CTX-2202:XHB_CASE_NON_AVAIL_DAYS - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2202:XHBSTG_NON_AVAIL_DATES_DM - Crest Court : 453 - Updated no of rows : 1
 
CTX-2202:XHB_CASE_NON_AVAIL_DAYS processed 1 for CREST_COURT_ID : 453 successfully!
XHB_CASE_NON_AVAIL_DAYS Updated for CREST_COURT_ID : 453 successfully!


 
CTX-2202:XHB_CASE_NON_AVAIL_DAYS processed 1 for CREST_COURT_ID : 453 successfully!
XHB_CASE_NON_AVAIL_DAYS Updated for CREST_COURT_ID : 453 successfully!

/*Check results*/
SELECT * FROM xhibit.xhb_case_non_avail_days order by 1 desc;
"NAD_ID"                      "CASE_ID"                     "START_DATE"                  "END_DATE"                    "REASON"                      "OBS_IND"                     "LAST_UPDATE_DATE"            "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  "VERSION"                     
"353"                         "621476"                      "01-AUG-2008"                 "01-AUG-2008"                 "annual leave"                "N"                           "31-AUG-2018"                 "31-AUG-2018"                 "DATA MIGRATION"              "DATA MIGRATION"              "1"                           


/*Check the staging table*/
SELECT distinct xhibit_enrich_date
, xhibit_etl_date
, xhibit_etl_err_message
FROM XHBSTG_NON_AVAIL_DATES_DM;

"XHIBIT_ENRICH_DATE"          "XHIBIT_ETL_DATE"             "XHIBIT_ETL_ERR_MESSAGE"      
"31-AUG-2018"                 "31-AUG-2018"                 ""                            

rollback;

