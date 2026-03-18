SELECT stgcd.jud_seq_no AS SITTING_NUMBER
    ,      xl.list_id
    ,      CASE
            WHEN stgcd.start_time IS NOT NULL THEN (SELECT ref_system_code_id
                                                    FROM xhibit.xhb_ref_system_code 
                                                    WHERE TRIM(code_type) = 'EXHIBIT_TIME_FORMAT' 
                                                    AND TRIM(de_code) = 'SITTING AT'
                                                    AND court_id = :v_xhibit_court_id
                                                    AND nvl(obs_ind,'N') <> 'Y'
                                                    )
            ELSE NULL
            END TIME_MARKING_ID
    ,     to_date(to_char(stgcd.list_date,'DD-MON-YYYY')||' '||stgcd.start_time,'DD-MON-YYYY HH:MI AM') TIME_LISTED
    ,     xrj.ref_judge_id JUDGE_REF_ID
    ,     stgcd.jp1_name JP1
    ,     stgcd.jp2_name JP2
    ,     stgcd.jp3_name JP3
    ,     stgcd.jp4_name JP4
    ,     NULL AS list_note_pre_defined_id
    ,     stgcd.sitting_note AS LIST_NOTE_TEXT
    ,     NULL AS PRE_DEF_NOTE_CLASSIFICATION_ID
    ,     NULL FREE_TEXT_NOTE_CLASS_ID
    ,     'N' AS OBS_IND
    ,     xcr.court_room_id
    ,     xcs.court_site_id 
    ,     xc.crest_court_id
    ,     stgcd.ctd_id
    FROM xhbstg_courtroom_day_dm stgcd
    ,    xhibit.xhb_list xl
    ,    xhibit.xhb_ref_listing_data xrld
    ,    xhibit.xhb_ref_judge xrj
    ,    xhibit.xhb_court xc
    ,    xhibit.xhb_court_site xcs
    ,    xhibit.xhb_court_room xcr
    WHERE trunc(stgcd.list_date) = trunc(xl.list_start_date)
    AND   xrld.ref_data_type = 'LIST_TYPE'
    AND   DECODE(stgcd.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
    AND   stgcd.crest_court_id = :p_crest_court_id
    AND   nvl(xl.obs_ind,'N') <> 'Y'
    AND   stgcd.jud_id = xrj.crest_judge_id
    AND   nvl(xrj.obs_ind,'N') <> 'Y'
    AND   stgcd.crest_court_id = xc.crest_court_id
    AND   xc.court_id = xcs.court_id
    AND   xcs.court_site_id = xcr.court_site_id
    AND   xcr.CREST_COURT_ROOM_NO = stgcd.courtroom_no
    AND   xcs.court_site_code = stgcd.site_code
    AND   xrj.court_id = xc.court_id
    AND   nvl(xc.obs_ind,'N') <> 'Y'
    AND   nvl(xcs.obs_ind,'N') <> 'Y'
    AND   nvl(xcr.obs_ind,'N') <> 'Y'
    AND   nvl(xcr.obs_ind,'N') <> 'Y'
    AND   NVL(stgcd.xhibit_etl_status,'N') not in ('I', 'U') 
    AND   stgcd.xhibit_enrich_date is NULL 
    AND   NOT EXISTS (SELECT 'x'
                      FROM xhibit.xhb_sitting_on_list xsol
                      WHERE xsol.list_id = xl.list_id
                      AND   NVL(xsol.obs_ind,'N') <> 'Y')
    ; 

/* RESULT 33 rows to be inserted
"SITTING_NUMBER"              "LIST_ID"                     "TIME_MARKING_ID"             "TIME_LISTED"                 "JUDGE_REF_ID"                "JP1"                         "JP2"                         "JP3"                         "JP4"                         "LIST_NOTE_PRE_DEFINED_ID"    "LIST_NOTE_TEXT"              "PRE_DEF_NOTE_CLASSIFICATION_ID""FREE_TEXT_NOTE_CLASS_ID"     "OBS_IND"                     "COURT_ROOM_ID"               "COURT_SITE_ID"               "CREST_COURT_ID"              "CTD_ID"                      
"1"                           "31"                          "111386"                      "14-MAY-2018"                 "34372"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "55019"                       
"1"                           "62"                          "111386"                      "23-MAY-2018"                 "34614"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "55166"                       
"1"                           "102"                         "111386"                      "06-AUG-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56616"                       
"1"                           "102"                         "111386"                      "06-AUG-2018"                 "34471"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8113"                        "1610"                        "453"                         "56617"                       
"1"                           "102"                         "111386"                      "06-AUG-2018"                 "34433"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8114"                        "1610"                        "453"                         "56618"                       
"1"                           "90"                          "111386"                      "12-JUL-2018"                 "34812"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56133"                       
"1"                           "96"                          "111386"                      "20-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56343"                       
"1"                           "96"                          "111386"                      "20-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8113"                        "1610"                        "453"                         "56344"                       
"1"                           "96"                          "111386"                      "20-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8114"                        "1610"                        "453"                         "56345"                       
"1"                           "96"                          "111386"                      "20-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56364"                       
"1"                           "96"                          "111386"                      "20-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8113"                        "1610"                        "453"                         "56365"                       
"1"                           "96"                          "111386"                      "20-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8114"                        "1610"                        "453"                         "56366"                       
"1"                           "100"                         "111386"                      "23-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56385"                       
"1"                           "100"                         "111386"                      "23-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8113"                        "1610"                        "453"                         "56386"                       
"1"                           "100"                         "111386"                      "23-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8114"                        "1610"                        "453"                         "56387"                       
"1"                           "100"                         "111386"                      "23-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56406"                       
"1"                           "100"                         "111386"                      "23-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8113"                        "1610"                        "453"                         "56407"                       
"1"                           "100"                         "111386"                      "23-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8114"                        "1610"                        "453"                         "56408"                       
"1"                           "21"                          "111386"                      "05-APR-2018"                 "34372"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "54556"                       
"1"                           "53"                          "111386"                      "21-MAY-2018"                 "34614"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "55124"                       
"1"                           "58"                          "111386"                      "22-MAY-2018"                 "34614"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "55145"                       
"1"                           "95"                          "111386"                      "20-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56343"                       
"1"                           "95"                          "111386"                      "20-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8113"                        "1610"                        "453"                         "56344"                       
"1"                           "95"                          "111386"                      "20-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8114"                        "1610"                        "453"                         "56345"                       
"1"                           "95"                          "111386"                      "20-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56364"                       
"1"                           "95"                          "111386"                      "20-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8113"                        "1610"                        "453"                         "56365"                       
"1"                           "95"                          "111386"                      "20-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8114"                        "1610"                        "453"                         "56366"                       
"1"                           "97"                          "111386"                      "23-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56385"                       
"1"                           "97"                          "111386"                      "23-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8113"                        "1610"                        "453"                         "56386"                       
"1"                           "97"                          "111386"                      "23-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8114"                        "1610"                        "453"                         "56387"                       
"1"                           "97"                          "111386"                      "23-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56406"                       
"1"                           "97"                          "111386"                      "23-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8113"                        "1610"                        "453"                         "56407"                       
"1"                           "97"                          "111386"                      "23-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8114"                        "1610"                        "453"                         "56408"                       
                          "102"                         "111386"                      "06-AUG-2018 10:15 am"        "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "8112"                        "1610"                        "453"                         "56616"                       

*/

--150 rows
select count(*)
from XHIBIT.XHB_SITTING_ON_LIST 
;

--After running, there should be 183 rows
DECLARE
 BEGIN dm_process_pkg_cc.upd_xhb_sit_on_list_crest(p_crest_court_id => 453);
END;

/*DBMS Output*/
CREST - COURT : 453 - Starting process of inserting rows from CREST COURTROOM_DAY table
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 31 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 62 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 102 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 102 ,v_sitting_number : 1, courtroom ID : 8113 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 102 ,v_sitting_number : 1, courtroom ID : 8114 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 90 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 96 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 96 ,v_sitting_number : 1, courtroom ID : 8113 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 96 ,v_sitting_number : 1, courtroom ID : 8114 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 96 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 96 ,v_sitting_number : 1, courtroom ID : 8113 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 96 ,v_sitting_number : 1, courtroom ID : 8114 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 100 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 100 ,v_sitting_number : 1, courtroom ID : 8113 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 100 ,v_sitting_number : 1, courtroom ID : 8114 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 100 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 100 ,v_sitting_number : 1, courtroom ID : 8113 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 100 ,v_sitting_number : 1, courtroom ID : 8114 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 21 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 53 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 58 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 95 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 95 ,v_sitting_number : 1, courtroom ID : 8113 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 95 ,v_sitting_number : 1, courtroom ID : 8114 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 95 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 95 ,v_sitting_number : 1, courtroom ID : 8113 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 95 ,v_sitting_number : 1, courtroom ID : 8114 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 97 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 97 ,v_sitting_number : 1, courtroom ID : 8113 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 97 ,v_sitting_number : 1, courtroom ID : 8114 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 97 ,v_sitting_number : 1, courtroom ID : 8112 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 97 ,v_sitting_number : 1, courtroom ID : 8113 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 97 ,v_sitting_number : 1, courtroom ID : 8114 court site ID 1610, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM processed 33 for CREST_COURT_ID : 453 successfully!
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - 13972 for CREST_COURT_ID : 453

--run another count
select count(*)
from XHIBIT.XHB_SITTING_ON_LIST 
;
--183 rows as expected

select *
FROM data_mig.xhbstg_data_migration_log
WHERE action_name like '%upd_xhb_sit_on_list_crest%'
and log_message like '%NOT updated%';


select *
FROM xhbstg_courtroom_day_dm
where trunc(xhibit_enrich_date) = trunc(sysdate) 
and crest_court_id = 453 and xhibit_etl_status = 'I';


select *
FROM xhbstg_courtroom_day_dm
where trunc(xhibit_enrich_date) = trunc(sysdate) 
and crest_court_id = 453 and xhibit_etl_status = 'N'
and xhibit_etl_err_message IS NOT NULL;



SELECT *
FROM xhibit.xhb_sitting_on_list
WHERE last_updated_by LIKE 'DATA%'; --33 rows

"SITTING_ON_LIST_ID"          "SITTING_NUMBER"              "LIST_ID"                     "TIME_MARKING_ID"             "TIME_LISTED"                 "JUDGE_REF_ID"                "JP1"                         "JP2"                         "JP3"                         "JP4"                         "LIST_NOTE_PRE_DEFINED_ID"    "LIST_NOTE_TEXT"              "PRE_DEF_NOTE_CLASSIFICATION_ID""FREE_TEXT_NOTE_CLASS_ID"     "OBS_IND"                     "LAST_UPDATE_DATE"            "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  "VERSION"                     "COURT_ROOM_ID"               "COURT_SITE_ID"               
"772"                         "1"                           "31"                          "111386"                      "14-MAY-2018"                 "34372"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"773"                         "1"                           "62"                          "111386"                      "23-MAY-2018"                 "34614"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"774"                         "1"                           "102"                         "111386"                      "06-AUG-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"775"                         "1"                           "102"                         "111386"                      "06-AUG-2018"                 "34471"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8113"                        "1610"                        
"776"                         "1"                           "102"                         "111386"                      "06-AUG-2018"                 "34433"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8114"                        "1610"                        
"777"                         "1"                           "90"                          "111386"                      "12-JUL-2018"                 "34812"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"778"                         "1"                           "96"                          "111386"                      "20-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"779"                         "1"                           "96"                          "111386"                      "20-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8113"                        "1610"                        
"780"                         "1"                           "96"                          "111386"                      "20-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8114"                        "1610"                        
"781"                         "1"                           "96"                          "111386"                      "20-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"782"                         "1"                           "96"                          "111386"                      "20-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8113"                        "1610"                        
"783"                         "1"                           "96"                          "111386"                      "20-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8114"                        "1610"                        
"784"                         "1"                           "100"                         "111386"                      "23-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"785"                         "1"                           "100"                         "111386"                      "23-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8113"                        "1610"                        
"786"                         "1"                           "100"                         "111386"                      "23-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8114"                        "1610"                        
"787"                         "1"                           "100"                         "111386"                      "23-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"788"                         "1"                           "100"                         "111386"                      "23-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8113"                        "1610"                        
"789"                         "1"                           "100"                         "111386"                      "23-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8114"                        "1610"                        
"790"                         "1"                           "21"                          "111386"                      "05-APR-2018"                 "34372"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"791"                         "1"                           "53"                          "111386"                      "21-MAY-2018"                 "34614"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"792"                         "1"                           "58"                          "111386"                      "22-MAY-2018"                 "34614"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"793"                         "1"                           "95"                          "111386"                      "20-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"794"                         "1"                           "95"                          "111386"                      "20-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8113"                        "1610"                        
"795"                         "1"                           "95"                          "111386"                      "20-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8114"                        "1610"                        
"796"                         "1"                           "95"                          "111386"                      "20-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"797"                         "1"                           "95"                          "111386"                      "20-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8113"                        "1610"                        
"798"                         "1"                           "95"                          "111386"                      "20-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8114"                        "1610"                        
"799"                         "1"                           "97"                          "111386"                      "23-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"800"                         "1"                           "97"                          "111386"                      "23-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8113"                        "1610"                        
"801"                         "1"                           "97"                          "111386"                      "23-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8114"                        "1610"                        
"802"                         "1"                           "97"                          "111386"                      "23-JUL-2018"                 "34850"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        
"803"                         "1"                           "97"                          "111386"                      "23-JUL-2018"                 "34398"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8113"                        "1610"                        
"804"                         "1"                           "97"                          "111386"                      "23-JUL-2018"                 "34650"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "10-SEP-2018"                 "10-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8114"                        "1610"                        


rollback;