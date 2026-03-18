/**
* CGI CREST TO XHIBIT Program
*
* MODULE      : etl_dm_summary_report_csv.sql
*
* DESCRIPTION : This is the SQL which will be run on completion of the Data Migration ETL process. The sql script will produce, for each table being *               migrated, a high level count for the following:
*	        Total number of records in the staging table
*               Count of each status type (updated, inserted, ignored, failed etc)
*               Count of updated and inserted rows in the equivalent XHIBIT table.
*               It will also produce a list of any rows which have not been successfully inserted or updated, listing them with primary key or other *               identifiable fields.
*
*
* INSTRUCTIONS
*               This script is run automatically from C2X_DM_ETL_2.sql straight after the DM ETL process
*               1. Put the script in a directory from where you are running the C2X_DM_ETL_2.sql 
*               2. Login to sqlplus as data_mig/data_mig
*               3. C2X_DM_ETL_2.sql is updated to call this script automatically with court_ids and hence NO user inputs required as parameters
*               4. The script should then run uninterropted to the end.
*               5. A file called DataMig_Summary_Report_XXX_YYMMDD_HHMISS.txt will be generated in the same directory where this script was run from.
*               6. Exit sqlplus by typing in: exit
*
* VERSION HISTORY:
*
* Date          Author           Version    Nature of Change
* ----------    -------------    --------   -----------------------------------------------------------------------
* 06/06/2019    S.Sethuraman     1.0        DM SUMMARY - OVERVIEW report - CSV version written
*
* 27/08/2019    S.Sethuraman     1.1        CASE_PARTY_SOF - Issue query suppressed 
*                                           CASE_SUBJECT - user DATA MIGRATION included
**/


SET PAGESIZE 50000
SET LINESIZE 300
SET FEEDBACK OFF
SET SERVEROUTPUT ON
SET TERMOUT ON

DEFINE stg_crest_court_id = &1
DEFINE xhibit_court_id = &2

COLUMN sysdt new_value sysdt

SELECT '_'||TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') sysdt FROM DUAL;

SET VERIFY OFF

SPOOL DataMig_Summary_Report_&stg_crest_court_id&sysdt..csv

DECLARE
v_dm_stg_step VARCHAR2(2000);
v_dm_xhb_step VARCHAR2(2000);
v_stg_tot_count NUMBER := 0;
v_stg_ins_count NUMBER := 0;
v_stg_upd_count NUMBER := 0;
v_stg_mrg_count NUMBER := 0;
v_stg_not_processed NUMBER := 0;
v_xhb_processed NUMBER := 0;
v_stg_failed_count NUMBER := 0;
v_stg_null_count NUMBER := 0;
v_stg_no_of_recs NUMBER := 0;
v_xhb_no_of_recs NUMBER := 0;
v_stg_orph_count NUMBER := 0;
v_stg_not_orph_count NUMBER := 0;
v_stg_b_u_cases NUMBER := 0;

BEGIN

DBMS_OUTPUT.ENABLE(1000000);

----  populate header

DBMS_OUTPUT.PUT_LINE('DATA MIGRATION STEP,TOTAL RECORDS RETRIEVED FROM CREST,INSERTED,UPDATED,MERGED,NOT PROCESSED,FAILED WITH ERROR,NULL,ORPHANED / OBSOLETE RECORDS IF ANY,NOT ORPHANED/NON OBSOLETE AND NOT PROCESSED COUNT,COUNT OF B / U CASES IF ANY,XHIBIT TABLE THAT WAS CHECKED,NO.RECORDS');

----Now START populating detail records

--========== XHBSTG_CASE_DM  =========================
SELECT
'Total No of CREST CASE records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_CASE records which have been updated as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_DM
WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--==========  XHBSTG_BW_HISTORY_DM  =====================
SELECT
'Total No of CREST BW_HISTORY records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_BW_HISTORY records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
v_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_BW_HISTORY_DM
WHERE CREST_COURT_ID = &stg_crest_court_id;

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--   Bench Warant Disposal records 

SELECT 
'Total number of Bench Warrant Disposal records in the staging schema:' ,
count(disp.xhibit_etl_status),
SUM(decode(disp.xhibit_etl_status,'I',1,0)),
SUM(decode(disp.xhibit_etl_status,'U',1,0)),
SUM(decode(disp.xhibit_etl_status,'M',1,0)),
SUM(decode(disp.xhibit_etl_status,'N',1,0)),
SUM(decode(disp.xhibit_etl_status,'X',1,0)),
SUM(decode(disp.xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_BW_HISTORY records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
v_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_DISPOSAL_DM DISP, XHBSTG_CASE_SUBJECT_DM CSU WHERE DISP.DISPOSAL_CODE LIKE 'BW%' AND DISP.SUB_ID = CSU.SUB_ID AND DISP.CASE_TYPE = CSU.CASE_TYPE AND DISP.CASE_NO = CSU.CASE_NO AND (CSU.BENCH_WARRANT_EXEC_DATE IS NULL OR CSU.BENCH_WARRANT_EXEC_DATE < DISP.OUTCOME_DATE) AND CSU.CREST_COURT_ID = &stg_crest_court_id AND DISP.CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_BW_HISTORY WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA_MIGRATION';

select count(*) into v_stg_orph_count from xhbstg_bw_history_dm stg where stg.crest_court_id = &stg_crest_court_id AND NOT EXISTS (select 'X' from xhbstg_case_subject_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);


SELECT count(stg.xhibit_etl_status) into v_stg_not_orph_count FROM XHBSTG_BW_HISTORY_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_SUBJECT_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);

SELECT count(DISP.XHIBIT_ETL_STATUS) into v_stg_not_processed FROM XHBSTG_DISPOSAL_DM DISP, XHBSTG_CASE_SUBJECT_DM CSU WHERE DISP.DISPOSAL_CODE LIKE 'BW%' AND DISP.SUB_ID = CSU.SUB_ID AND DISP.CASE_TYPE = CSU.CASE_TYPE AND DISP.CASE_NO = CSU.CASE_NO AND (CSU.BENCH_WARRANT_EXEC_DATE IS NULL OR CSU.BENCH_WARRANT_EXEC_DATE < DISP.OUTCOME_DATE) AND CSU.CREST_COURT_ID = &stg_crest_court_id AND DISP.CREST_COURT_ID = &stg_crest_court_id AND DISP.XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--==========  XHBSTG_CASE_HEARING_DAY_DM  FUTURE FIXTURES  ======================

SELECT
'Total No of CREST CASE_HEARING_DAY records in the staging schema for future fixtures:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_CASE_DIARY_FIXTURE records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
v_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_HEARING_DAY_DM WHERE  LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) INTO v_xhb_no_of_recs FROM XHIBIT.XHB_CASE_DIARY_FIXTURE WHERE CASE_LISTING_ENTRY_ID IN (SELECT CASE_LISTING_ENTRY_ID FROM XHIBIT.XHB_CASE_LISTING_ENTRY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) AND CREATED_BY = 'DATA_MIGRATION';

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_CASE_HEARING_DAY_DM  LISTINGS ======================

SELECT
'Total No of CREST CASE_HEARING_DAY records in the staging schema for listings:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_CASE_ON_LIST records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
v_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_HEARING_DAY_DM WHERE  LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) INTO v_xhb_no_of_recs FROM XHIBIT.XHB_CASE_ON_LIST WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) AND CREATED_BY = 'DATA_MIGRATION';

SELECT count(stg.XHIBIT_ETL_STATUS) INTO v_stg_not_orph_count FROM XHBSTG_CASE_HEARING_DAY_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id  AND stg.LIST_TYPE != 'X' and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);

select count(*) into v_stg_orph_count from xhbstg_case_hearing_day_dm stg where stg.crest_court_id = &stg_crest_court_id AND stg.list_type != 'X' AND stg.case_type NOT IN ('B','U') AND NOT EXISTS (select 'X' from xhbstg_case_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type); 

SELECT count(stg.XHIBIT_ETL_STATUS) into v_stg_b_u_cases FROM XHBSTG_CASE_HEARING_DAY_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id  AND stg.LIST_TYPE != 'X' and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND stg.case_type in ('B','U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_CASE_HISTORY_DM  ======================

SELECT
'Total No of CREST CASE_HISTORY records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_CASE_HISTORY records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_HISTORY_DM
WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_CASE_HISTORY WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA_MIGRATION';

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--==========  XHBSTG_CASE_NOTE  =============================

SELECT
'Total No of CREST CASE_NOTE records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_DIARY_NOTE_ENTRY records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_NOTE_DM
WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_DIARY_NOTE_ENTRY WHERE NOTE_TYPE_ID NOT IN (SELECT REF_LISTING_DATA_ID FROM XHIBIT.XHB_REF_LISTING_DATA WHERE REF_DATA_VALUE = 'DCN') AND (CASE_LISTING_ENTRY_ID IN  (SELECT CASE_LISTING_ENTRY_ID FROM XHIBIT.XHB_CASE_LISTING_ENTRY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) OR COURT_ID = &xhibit_court_id OR case_id in (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA_MIGRATION';

SELECT count(stg.XHIBIT_ETL_STATUS) into v_stg_not_orph_count FROM XHBSTG_CASE_NOTE_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);

select count(*) into v_stg_orph_count from xhbstg_case_note_dm stg where stg.crest_court_id = &stg_crest_court_id AND stg.case_no is NOT NULL AND NOT EXISTS (select 'X' from xhbstg_case_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type); 

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--==========  XHBSTG_CASE_OPPOSER_DM  =======================

SELECT
'Total No of CREST CASE_OPPOSER records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_CASE_PROSECUTOR_AGENCY records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_OPPOSER_DM
WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and LAST_UPDATED_BY = 'DATA_MIG';

SELECT count(OPP_ID) into v_stg_not_orph_count FROM XHBSTG_CASE_OPPOSER_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY pa WHERE NVL(pa.OBS_IND,'N') != 'Y' AND pa.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE cs WHERE cs.COURT_ID = &xhibit_court_id AND cs.CASE_NUMBER = stg.CASE_NO AND cs.CASE_TYPE = stg.CASE_TYPE) AND pa.REF_PROSECUTOR_AGENCY_ID = (SELECT REF_PROSECUTOR_AGENCY_ID FROM XHIBIT.XHB_REF_PROSECUTOR_AGENCY rpa WHERE rpa.COURT_ID = &xhibit_court_id AND rpa.CREST_OPPOSER_ID = stg.OPP_ID and NVL(rpa.OBS_IND,'N') !='Y'));

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--==========  XHBSTG_CASE_PARTY_SOF  ========================

SELECT
'Total No of CREST CASE_PARTY_SOF records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_DEF_ON_CASE_REF_SOL_FIRM records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_PARTY_SOF_DM
WHERE CREST_COURT_ID = &stg_crest_court_id;


SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_DEF_ON_CASE_REF_SOL_FIRM WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';

/*
select count(*) into v_stg_not_orph_count from 
(SELECT CPF_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_PARTY_SOF_DM STG WHERE STG.CREST_COURT_ID = &stg_crest_court_id AND STG.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_DEF_ON_CASE_REF_SOL_FIRM DS WHERE NVL(OBS_IND, 'N') != 'Y' AND DEFENDANT_ON_CASE_ID = (SELECT DEFENDANT_oN_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE DOC WHERE DOC.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE CS WHERE CS.COURT_ID = &xhibit_court_id AND CS.CASE_NUMBER = STG.CASE_NO AND CS.CASE_TYPE = STG.CASE_TYPE) AND DOC.DEFENDANT_ID = (SELECT DEFENDANT_ID FROM XHIBIT.XHB_DEFENDANT DEF WHERE DEF.COURT_ID = &xhibit_court_id AND DEF.CREST_DEFENDANT_ID = STG.SUB_OPP_ID)) AND DS.CREST_CPF_ID = STG.CPF_ID)
UNION
SELECT CPF_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_PARTY_SOF_DM STG WHERE STG.CREST_COURT_ID = &stg_crest_court_id AND STG.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_PROSECUTOR_REF_SOL_FIRM PS WHERE NVL(OBS_IND, 'N') != 'Y' AND PS.CASE_PROS_AGENCY_ID = (SELECT CASE_PROS_AGENCY_ID FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY CPA WHERE NVL(CPA.OBS_IND, 'N') != 'Y' AND CPA.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE CS WHERE CS.COURT_ID = &xhibit_court_id AND CS.CASE_NUMBER = STG.CASE_NO AND CS.CASE_TYPE = STG.CASE_TYPE)) AND PS.CREST_CPF_ID = STG.CPF_ID));
*/

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

select ' ',0,0,0,0,0,0,0,0,0,0,'Count of XHIBIT XHB_PROSECUTOR_REF_SOL_FIRM records which have been inserted as part of data migration:',0 
into 
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_dm_xhb_step,
v_xhb_no_of_recs
from dual;


SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_PROSECUTOR_REF_SOL_FIRM WHERE CASE_PROS_AGENCY_ID IN (SELECT CASE_PROS_AGENCY_ID FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';


DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_CASE_SUB_APPEARNACE_DM  future fixtures ========================

SELECT
'Total No of CREST CASE_SUB_APPEARANCE records in the staging schema for future fixtures:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_FIXTURE_DEFT_ATTENDING records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
v_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) INTO v_xhb_no_of_recs FROM XHIBIT.XHB_FIXTURE_DEFT_ATTENDING WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) AND CREATED_BY = 'DATA_MIGRATION';

SELECT count(*) into v_stg_not_orph_count FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_CASE_SUB_APPEARANCE  LISTINGS ======================

SELECT
'Total No of CREST CASE_SUB_APPEARANCE records in the staging schema for listings:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_DEF_ON_CASE_ON_LIST records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
v_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id;


SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_DEF_ON_CASE_ON_LIST WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) AND CREATED_BY = 'DATA_MIGRATION';

SELECT count(stg.CASE_NO) into v_stg_not_orph_count FROM XHBSTG_CASE_SUB_APPEARANCE_DM stg WHERE stg.CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id) AND stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U')  AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_SUBJECT_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc. case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);

select count(*) into v_stg_orph_count from xhbstg_case_SUB_APPEARANCE_dm stg where stg.crest_court_id = &stg_crest_court_id AND stg.chd_id in (select chd.chd_id from xhbstg_case_hearing_day_dm chd where chd.crest_court_id = stg.crest_court_id and chd.list_type != 'X') AND NOT EXISTS (select 'X' from xhbstg_case_subject_dm xc where xc. crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);



--==========  XHBSTG_CASE_SUBJECT_DM  =================================

SELECT
'Total No of CREST CASE_SUBJECT records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_DEFENDANT_ON_CASE records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CASE_SUBJECT_DM
WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and LAST_UPDATED_BY in ( 'DATA_MIG','DATA MIGRATION');


SELECT count(OPP_ID) into v_stg_not_orph_count FROM XHBSTG_CASE_OPPOSER_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY pa WHERE NVL(pa.OBS_IND,'N') != 'Y' AND pa.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE cs WHERE cs.COURT_ID = &xhibit_court_id AND cs.CASE_NUMBER = stg.CASE_NO AND cs.CASE_TYPE = stg.CASE_TYPE) AND pa.REF_PROSECUTOR_AGENCY_ID = (SELECT REF_PROSECUTOR_AGENCY_ID FROM XHIBIT.XHB_REF_PROSECUTOR_AGENCY rpa WHERE rpa.COURT_ID = &xhibit_court_id AND rpa.CREST_OPPOSER_ID = stg.OPP_ID and NVL(rpa.OBS_IND,'N') !='Y'));

SELECT count(*) into v_stg_not_orph_count from XHBSTG_CASE_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_CASE_CHAMBERS_DM  =================================

SELECT
'Total No of CREST CASE_CHAMBERS records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_REF_CHAMBER records which have been updated as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CHAMBERS_DM
WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_REF_CHAMBER WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

SELECT COUNT(*) into v_stg_not_orph_count FROM XHIBIT.XHB_REF_CHAMBER WHERE COURT_ID = &xhibit_court_id and NVL(OBS_IND,'N') != 'Y';


DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_CHARGE_DM  =================================


SELECT
'Total No of CREST CHARGE records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_OFFENCE records which have been updated as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CHARGE_DM WHERE CASE_TYPE = 'A' AND CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_OFFENCE WHERE CHARGE_ID IN (SELECT CHARGE_ID FROM XHIBIT.XHB_CHARGE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';

SELECT count(*) into v_stg_not_orph_count from XHBSTG_CHARGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id AND CASE_TYPE = 'A' AND XHIBIT_ETL_STATUS NOT IN ('I', 'U');


DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_COMMITTAL_CHARGE_DM  =========================

SELECT
'Total No of CREST COMMITTAL CHARGE records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_CHARGES_LOG records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_COMMITTAL_CHARGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_CHARGES_LOG WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';


SELECT count(stg.CCH_ID) into v_stg_not_orph_count from XHBSTG_COMMITTAL_CHARGE_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U')  AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);

select count(*) into v_stg_orph_count from XHBSTG_COMMITTAL_CHARGE_DM stg where stg.crest_court_id = &stg_crest_court_id AND NOT EXISTS (select 'X' from xhbstg_case_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type); 

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--========== XHBSTG_COURTROOM_DAY_DM  ========================

SELECT
'Total No of CREST COURTROOM_DAY records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_SITTING_ON_LIST records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_COURTROOM_DAY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_SITTING_ON_LIST WHERE COURT_SITE_ID IN (SELECT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';

SELECT count(CTD_ID) into v_stg_not_orph_count FROM XHBSTG_COURTROOM_DAY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U', 'D');


DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_COURTROOM_DM  ======================

SELECT
'Total No of CREST COURTROOM records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_COURT_ROOM records which have been updated as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_COURTROOM_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_COURT_ROOM WHERE COURT_SITE_ID IN (SELECT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id) and LAST_UPDATED_BY = 'DATA_MIG';

SELECT count(COURTROOM_NO) into v_stg_not_orph_count FROM XHBSTG_COURTROOM_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');


DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_COURTROOM_LOCATION_DM  =====================


SELECT
'Total No of CREST COURTROOM_LOCATION records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_COURT_SITE records which have been updated as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_COURTROOM_LOCATION_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

SELECT count(CTL_ID) into v_stg_not_orph_count FROM XHBSTG_COURTROOM_LOCATION_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_COURTROOM_USAGE_DM  ========================

SELECT
'Total No of CREST COURTROOM_USAGE records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_COURT_ROOM_USAGE records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_COURTROOM_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_COURT_ROOM_USAGE WHERE COURT_ROOM_ID IN (SELECT COURT_ROOM_ID FROM XHIBIT.XHB_COURT_ROOM WHERE COURT_SITE_ID IN (SELECT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA_MIGRATION';

SELECT count(CRU_ID) into v_stg_not_orph_count FROM XHBSTG_COURTROOM_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--===========  XHBSTG_CSU_HISTORY_DM  ========================

SELECT
'Total No of CREST CSU_HISTORY records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_DEFENDANT_ON_CASE_HISTORY records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_CSU_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*)into v_xhb_no_of_recs  FROM XHIBIT.XHB_DEFENDANT_ON_CASE_HISTORY WHERE CASE_HISTORY_ID IN (SELECT CASE_HISTORY_ID FROM XHIBIT.XHB_CASE_HISTORY WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';

SELECT count(XHIBIT_ETL_STATUS) into v_stg_not_orph_count FROM XHBSTG_CSU_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--==========  XHBSTG_DISPOSAL_DM  ======================

SELECT
'Total No of CREST DISPOSAL records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_MONETRAY_ORDER_TRACKING records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_DISPOSAL_DM WHERE DISPOSAL_CODE = 'NMO' AND CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_MONETARY_ORDER_TRACKING WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';

SELECT count(DIS_ID) into v_stg_not_orph_count FROM XHBSTG_DISPOSAL_DM WHERE DISPOSAL_CODE = 'NMO' AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_HOME_COURT_DM  ========================

SELECT
'Total No of CREST HOME_COURT records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_COURT records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_HOME_COURT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_COURT WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

SELECT count(XHIBIT_ETL_STATUS) into v_stg_not_orph_count FROM XHBSTG_HOME_COURT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_JUDGE_TICKET_DM  ========================

SELECT
'Total No of CREST JUDGE_TICKET records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_REF_JUDGE_TICKET records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_JUDGE_TICKET_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_REF_JUDGE_TICKET WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA_MIGRATION';

SELECT count(JUD_ID) into v_stg_not_orph_count FROM XHBSTG_JUDGE_TICKET_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_JUDGE_USAGE_DM  =========================

SELECT
'Total No of CREST JUDGE_USAGE records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_JUDGE_USAGE records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_JUDGE_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_JUDGE_USAGE WHERE REF_JUDGE_ID IN (SELECT REF_JUDGE_ID FROM XHIBIT.XHB_REF_JUDGE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';

SELECT count(XHIBIT_ETL_STATUS) into v_stg_not_orph_count FROM XHBSTG_JUDGE_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--==========  XHBSTG_LEGAL_AID_AMENDMENT_DM  =====================


SELECT
'Total No of CREST LEGAL records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_LEGAL_AID_AMENDMENT records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_LEGAL_AID_AMENDMENT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_LEGAL_AID_AMENDMENT WHERE LEGAL_AID_ORDER_ID IN (SELECT LEGAL_AID_ORDER_ID FROM XHIBIT.XHB_LEGAL_AID_ORDER WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id))) and CREATED_BY = 'DATA_MIGRATION';

SELECT count(SEQ_NO) into v_stg_not_orph_count FROM XHBSTG_LEGAL_AID_AMENDMENT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_LEGAL_AID_ORDER_DM  =====================

SELECT
'Total No of CREST LEGAL_AID_ORDER records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_LEGAL_AID_ORDER records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_LEGAL_AID_ORDER_DM WHERE CREST_COURT_ID = &stg_crest_court_id and REFUSAL_DATE is NULL;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_LEGAL_AID_ORDER WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA MIGRATION';

SELECT count(LEO_ID) into v_stg_not_orph_count FROM XHBSTG_LEGAL_AID_ORDER_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

select ' ',0,0,0,0,0,0,0,0,0,0,0,'Count of XHIBIT XHB_LEGAL_AID_ORDER records which have been updated as part of data migration:',0 
into 
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_dm_xhb_step,
v_xhb_no_of_recs
from dual;

SELECT COUNT(*),'Count of XHIBIT XHB_LEGAL_AID_ORDER records which have been updated as part of data migration:' into v_xhb_no_of_recs,v_dm_xhb_step FROM XHIBIT.XHB_LEGAL_AID_ORDER WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--========== XHBSTG_LISTS_DM  ======================

SELECT
'Total No of CREST LISTS records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_LIST records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_LISTS_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_LIST WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA MIGRATION';

SELECT count(LST_ID) into v_stg_not_orph_count FROM XHBSTG_LISTS_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_NON_AVAIL_DATES_DM  =========================

SELECT
'Total No of CREST NON_AVAIL_DATES records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_NON_AVAIL_DAYS records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_NON_AVAIL_DATES_DM WHERE END_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_CASE_NON_AVAIL_DAYS WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA MIGRATION';

SELECT count(NAD_ID) into v_stg_not_orph_count FROM XHBSTG_NON_AVAIL_DATES_DM WHERE END_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

SELECT count(*) into v_stg_orph_count FROM XHBSTG_NON_AVAIL_DATES_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.END_DATE > SYSDATE AND NOT EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM XC WHERE XC.CREST_COURT_ID = STG.CREST_COURT_ID AND XC.CASE_NO = STG.CASE_NO AND XC.CASE_TYPE = XC.CASE_TYPE);

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_SOLICITOR_FIRM_DM  ==========================

SELECT
'Total No of CREST SOLICITOR_FIRM records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_REF_SOLICITOR_FIRM records which have been updated as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_SOLICITOR_FIRM_DM WHERE CREST_COURT_ID = &stg_crest_court_id ;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_REF_SOLICITOR_FIRM WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

SELECT COUNT(*) into v_stg_not_orph_count FROM XHIBIT.XHB_REF_SOLICITOR_FIRM WHERE COURT_ID = &xhibit_court_id and NVL(OBS_IND,'N') != 'Y';

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);

--==========  XHBSTG_SUBJECT_DM  ============================

SELECT
'Total No of CREST SUBJECT records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_DEFENDANT records which have been updated as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id ;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_DEFENDANT WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

SELECT count(SUB_ID) into v_stg_not_orph_count FROM XHBSTG_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

SELECT count(*) into v_stg_orph_count FROM XHBSTG_SUBJECT_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and NOT EXISTS (SELECT 'X' FROM XHBSTG_CASE_SUBJECT_DM XC WHERE XC. CREST_COURT_ID = stg.CREST_COURT_ID and xc.sub_id = stg.sub_id);

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


--==========  XHBSTG_SUBJECT_HISTORY_DM  ======================

SELECT
'Total No of CREST SUBJECT_HISTORY records in the staging schema:' ,
count(xhibit_etl_status),
SUM(decode(xhibit_etl_status,'I',1,0)),
SUM(decode(xhibit_etl_status,'U',1,0)),
SUM(decode(xhibit_etl_status,'M',1,0)),
SUM(decode(xhibit_etl_status,'N',1,0)),
SUM(decode(xhibit_etl_status,'X',1,0)),
SUM(decode(xhibit_etl_status,NULL,1,0)),
'Count of XHIBIT XHB_DEFENDANT_HISTORY records which have been inserted as part of data migration:',
0,
0,
0,
0 into
v_dm_stg_step,
v_stg_tot_count,
v_stg_ins_count,
v_stg_upd_count,
V_stg_mrg_count,
v_stg_not_processed,
v_stg_failed_count,
v_stg_null_count,
v_dm_xhb_step,
v_stg_orph_count,
v_stg_not_orph_count,
v_stg_b_u_cases,
v_xhb_no_of_recs
FROM XHBSTG_SUBJECT_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id ;

SELECT COUNT(*) into v_xhb_no_of_recs FROM XHIBIT.XHB_DEFENDANT_HISTORY WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA_MIGRATION';

SELECT count(SUB_ID) into v_stg_not_orph_count FROM XHBSTG_SUBJECT_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

DBMS_OUTPUT.PUT_LINE(v_dm_stg_step||','||v_stg_tot_count||','||v_stg_ins_count||','||v_stg_upd_count||','||v_stg_mrg_count||','||v_stg_not_processed||','||v_stg_failed_count||','||v_stg_null_count||','||v_stg_orph_count||','||v_stg_not_orph_count||','||v_stg_b_u_cases||','||v_dm_xhb_step||','||v_xhb_no_of_recs);


END;
/

SET VERIFY ON

SPOOL OFF

