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
    ,     stgcd.courtroom_no
    ,     stgcd.ctd_id
    ,     stgcd.list_date
    ,     stgcd.site_code
    ,     xc.crest_court_id
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
    AND   xl.list_type_id = xrld.ref_listing_data_id
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
    AND   nvl(xl.obs_ind,'N') <> 'Y'
    AND   nvl(xrld.obs_ind,'N') <> 'Y'
    AND   NVL(stgcd.xhibit_etl_status,'N') not in ('I', 'U') 
    AND   stgcd.xhibit_enrich_date is NULL 
    AND   NOT EXISTS (SELECT 'x'
                      FROM xhibit.xhb_sitting_on_list xsol
                      WHERE xsol.list_id = xl.list_id
                      AND   NVL(xsol.obs_ind,'N') <> 'Y')
    ;

/*1 row returned*/


--150 rows so after running there should be 151 rows
select count(*)
from XHIBIT.XHB_SITTING_ON_LIST 
;

--After running, there should be 151 rows
DECLARE
 BEGIN dm_process_pkg_cc.upd_xhb_sit_on_list_crest(p_crest_court_id => 453);
END;

/*DBMS Output*/

CREST - COURT : 453 - Starting process of inserting rows from CREST COURTROOM_DAY table
 
INSERTING XHB_SITTING_ON_LIST - FOR CREST_COURT_ID = 453
v_list_id : 21 ,v_sitting_number : 1, courtroom No : 1 court site code A, v_xhibit_court_id 81
 
CTX-2403:XHB_SITTING_ON_LIST - Crest Court : 453 - Inserted no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : 453 - Updated no of rows : 1
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM processed 1 for CREST_COURT_ID : 453 successfully!
 
CTX-2403:XHBSTG_COURTOOM_DAY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - 13992 for CREST_COURT_ID : 453



--run another count
select count(*)
from XHIBIT.XHB_SITTING_ON_LIST 
;
--151 rows as expected

select *
FROM data_mig.xhbstg_data_migration_log
WHERE action_name like '%upd_xhb_sit_on_list_crest%'
and log_message like '%NOT updated%';


select *
FROM xhbstg_courtroom_day_dm
where trunc(xhibit_enrich_date) = trunc(sysdate) 
and crest_court_id = 453 and xhibit_etl_status = 'I';

"CTD_ID"                      "JP1_NAME"                    "JUD_ID"                      "JP2_NAME"                    "JP4_NAME"                    "SITTING_NOTE"                "LIST_TYPE"                   "COMMIT_FLAG"                 "START_TIME"                  "OLD_CTD_ID"                  "JUD_SEQ_NO"                  "JUD_SIT_TYPE"                "JP3_NAME"                    "LIST_DATE"                   "COURTROOM_NO"                "SITE_CODE"                   "JUD_SIT_IND"                 "CREST_COURT_ID"              "XHIBIT_COURT_ID"             "XHIBIT_ETL_STATUS"           "XHIBIT_LOADED_DATE"          "XHIBIT_ENRICH_DATE"          "XHIBIT_ETL_DATE"             "XHIBIT_ETL_ERR_MESSAGE"      
"54556"                       ""                            "100"                         ""                            ""                            ""                            "D"                           "Y"                           "10:30 am"                    ""                            "1"                           ""                            ""                            "05-APR-2018"                 "1"                           "A"                           ""                            "453"                         "81"                          "I"                           "06-SEP-2018"                 "12-SEP-2018"                 "12-SEP-2018"                 ""                            


select *
FROM xhbstg_courtroom_day_dm
where trunc(xhibit_enrich_date) = trunc(sysdate) 
and crest_court_id = 453 and xhibit_etl_status = 'N'
and xhibit_etl_err_message IS NOT NULL;

--no rows as expected

SELECT *
FROM xhibit.xhb_sitting_on_list
WHERE last_updated_by LIKE 'DATA%'; --1 rows

"SITTING_ON_LIST_ID"          "SITTING_NUMBER"              "LIST_ID"                     "TIME_MARKING_ID"             "TIME_LISTED"                 "JUDGE_REF_ID"                "JP1"                         "JP2"                         "JP3"                         "JP4"                         "LIST_NOTE_PRE_DEFINED_ID"    "LIST_NOTE_TEXT"              "PRE_DEF_NOTE_CLASSIFICATION_ID""FREE_TEXT_NOTE_CLASS_ID"     "OBS_IND"                     "LAST_UPDATE_DATE"            "CREATION_DATE"               "LAST_UPDATED_BY"             "CREATED_BY"                  "VERSION"                     "COURT_ROOM_ID"               "COURT_SITE_ID"               
"2577"                        "1"                           "21"                          "111386"                      "05-APR-2018"                 "34372"                       ""                            ""                            ""                            ""                            ""                            ""                            ""                            ""                            "N"                           "12-SEP-2018"                 "12-SEP-2018"                 "DATA_MIGRATION"              "DATA_MIGRATION"              "1"                           "8112"                        "1610"                        


rollback;