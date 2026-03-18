/**
* CGI CREST TO XHIBIT Program
*
* MODULE      : data_migration_report.sql
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
*               5. A file called DataMigReport_XXX_YYMMDD_HHMISS.txt will be generated in the same directory where this script was run from.
*               6. Exit sqlplus by typing in: exit
*
* VERSION HISTORY:
*
* Date          Author           Version    Nature of Change
* ----------    -------------    --------   -----------------------------------------------------------------------
* 15/02/2019    A.Dennis         1.0        First written
*
* 21/02/2019    S.Sethuraman     1.1        updated variables crest_court_id and court_id to stg_crest_court_id and xhibit_court_id
*                                           and referenced with ampersand prefix
*
* 26/12/2019    S.Sethuraman     1.2        updated report out_put filename suffixed with crest_court_id and timestamp
*
* 07/03/2019    S.Sethuraman     1.3        updated to be called from C2X_DM_ETL_2.sql automatically with parameters
*
* 08/03/2019    A Dennis         1.3        Updated to inlcude Brian H comments in CTX-3818
*
* 04/04/2019    S Sethuraman     1.4        Updated to include orphan records
*
* 02/05/2019    S Sethuraman     1.5        Updates as per CTX-4168 
*
* 05/06/2019    S Sethuraman     1.6        Updates as per CTX-4289
*
* 05/07/2019    S Sethuraman     1.7        Updates as per CTX-4289 - Change to CASE_SUB_APPEARANCE NOT ORPHAN count remove SYSDATE
*
* 27/08/2019    S Sethuraman     1.8        CASE_SUBJECT QUERY - include user DATA_MIGRATION
*                                           supress - CASE_PARTY_SOF issue query
**/

SET PAGESIZE 50000

DEFINE stg_crest_court_id = &1
DEFINE xhibit_court_id = &2

COLUMN sysdt new_value sysdt

SELECT '_'||TO_CHAR(SYSDATE,'YYYYMMDD_HH24MISS') sysdt FROM DUAL;

SPOOL DataMigReport_&stg_crest_court_id&sysdt..txt

PROMPT 'Enter XHIBIT Court ID: '&xhibit_court_id
PROMPT 'Enter Crest Court ID: '&stg_crest_court_id

PROMPT

COLUMN XHIBIT_ETL_STATUS FORMAT A17

SET VERIFY OFF

PROMPT ========== XHBSTG_CASE_DM  =========================
PROMPT
PROMPT List of Case numbers for Cases created by Data Migration in XHIBIT:
PROMPT
PROMPT SELECT CASE_TYPE||CASE_NUMBER FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA MIGRATION';;
SELECT CASE_TYPE||CASE_NUMBER FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA MIGRATION';

PROMPT Total number of CREST CASE records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table: 
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_CASE records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT List of rows which have failed to process:
PROMPT
PROMPT SELECT CASE_TYPE, CASE_NO, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CASE_TYPE, CASE_NO, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_BW_HISTORY_DM  =====================
PROMPT
PROMPT Total number of CREST BW_HISTORY records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_BW_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_BW_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_BW_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_BW_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Total number of Bench Warrant Disposal records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_DISPOSAL_DM DISP, XHBSTG_CASE_SUBJECT_DM CSU WHERE DISP.DISPOSAL_CODE LIKE 'BW%' AND DISP.SUB_ID = CSU.SUB_ID AND DISP.CASE_TYPE = CSU.CASE_TYPE AND DISP.CASE_NO = CSU.CASE_NO AND (CSU.BENCH_WARRANT_EXEC_DATE IS NULL OR CSU.BENCH_WARRANT_EXEC_DATE < DISP.OUTCOME_DATE) AND CSU.CREST_COURT_ID = &stg_crest_court_id AND DISP.CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_DISPOSAL_DM DISP, XHBSTG_CASE_SUBJECT_DM CSU WHERE DISP.DISPOSAL_CODE LIKE 'BW%' AND DISP.SUB_ID = CSU.SUB_ID AND DISP.CASE_TYPE = CSU.CASE_TYPE AND DISP.CASE_NO = CSU.CASE_NO AND (CSU.BENCH_WARRANT_EXEC_DATE IS NULL OR CSU.BENCH_WARRANT_EXEC_DATE < DISP.OUTCOME_DATE) AND CSU.CREST_COURT_ID = &stg_crest_court_id AND DISP.CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of Bench Warrant disposal records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), DISP.XHIBIT_ETL_STATUS FROM XHBSTG_DISPOSAL_DM DISP, XHBSTG_CASE_SUBJECT_DM CSU WHERE DISP.DISPOSAL_CODE LIKE 'BW%' AND DISP.SUB_ID = CSU.SUB_ID AND DISP.CASE_TYPE = CSU.CASE_TYPE AND DISP.CASE_NO = CSU.CASE_NO AND (CSU.BENCH_WARRANT_EXEC_DATE IS NULL OR CSU.BENCH_WARRANT_EXEC_DATE < DISP.OUTCOME_DATE) AND CSU.CREST_COURT_ID = &stg_crest_court_id AND DISP.CREST_COURT_ID = &stg_crest_court_id GROUP BY DISP.XHIBIT_ETL_STATUS;;
SELECT COUNT(*), DISP.XHIBIT_ETL_STATUS FROM XHBSTG_DISPOSAL_DM DISP, XHBSTG_CASE_SUBJECT_DM CSU WHERE DISP.DISPOSAL_CODE LIKE 'BW%' AND DISP.SUB_ID = CSU.SUB_ID AND DISP.CASE_TYPE = CSU.CASE_TYPE AND DISP.CASE_NO = CSU.CASE_NO AND (CSU.BENCH_WARRANT_EXEC_DATE IS NULL OR CSU.BENCH_WARRANT_EXEC_DATE < DISP.OUTCOME_DATE) AND CSU.CREST_COURT_ID = &stg_crest_court_id AND DISP.CREST_COURT_ID = &stg_crest_court_id GROUP BY DISP.XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_BW_HISTORY records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_BW_HISTORY WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_BW_HISTORY WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT and are NOT Orphan records:
PROMPT
PROMPT SELECT stg.CASE_TYPE, stg.CASE_NO, stg.SUB_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_BW_HISTORY_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_SUBJECT_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);;
SELECT stg.CASE_TYPE, stg.CASE_NO, stg.SUB_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_BW_HISTORY_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_SUBJECT_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);

PROMPT Count the no of Orphan records - XHBSTG_BW_HISTORY :
PROMPT
PROMPT select count(*) from xhbstg_bw_history_dm stg where stg.crest_court_id = &stg_crest_court_id AND NOT EXISTS (select 'X' from xhbstg_case_subject_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);;
select count(*) from xhbstg_bw_history_dm stg where stg.crest_court_id = &stg_crest_court_id AND NOT EXISTS (select 'X' from xhbstg_case_subject_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);

PROMPT List of BW Disposal rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT DIS_ID, DISP.XHIBIT_ETL_STATUS FROM XHBSTG_DISPOSAL_DM DISP, XHBSTG_CASE_SUBJECT_DM CSU WHERE DISP.DISPOSAL_CODE LIKE 'BW%' AND DISP.SUB_ID = CSU.SUB_ID AND DISP.CASE_TYPE = CSU.CASE_TYPE AND DISP.CASE_NO = CSU.CASE_NO AND (CSU.BENCH_WARRANT_EXEC_DATE IS NULL OR CSU.BENCH_WARRANT_EXEC_DATE < DISP.OUTCOME_DATE) AND CSU.CREST_COURT_ID = &stg_crest_court_id AND DISP.CREST_COURT_ID = &stg_crest_court_id AND DISP.XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT DIS_ID, DISP.XHIBIT_ETL_STATUS FROM XHBSTG_DISPOSAL_DM DISP, XHBSTG_CASE_SUBJECT_DM CSU WHERE DISP.DISPOSAL_CODE LIKE 'BW%' AND DISP.SUB_ID = CSU.SUB_ID AND DISP.CASE_TYPE = CSU.CASE_TYPE AND DISP.CASE_NO = CSU.CASE_NO AND (CSU.BENCH_WARRANT_EXEC_DATE IS NULL OR CSU.BENCH_WARRANT_EXEC_DATE < DISP.OUTCOME_DATE) AND CSU.CREST_COURT_ID = &stg_crest_court_id AND DISP.CREST_COURT_ID = &stg_crest_court_id AND DISP.XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_CASE_HEARING_DAY_DM  ======================
PROMPT
PROMPT *****FUTURE FIXTURES*****
PROMPT
PROMPT Total number of CREST CASE_HEARING_DAY records in the staging schema for future fixtures:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_HEARING_DAY_DM WHERE  LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_HEARING_DAY_DM WHERE  LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of these records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_CASE_DIARY_FIXTURE records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_CASE_DIARY_FIXTURE WHERE CASE_LISTING_ENTRY_ID IN (SELECT CASE_LISTING_ENTRY_ID FROM XHIBIT.XHB_CASE_LISTING_ENTRY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) AND CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_CASE_DIARY_FIXTURE WHERE CASE_LISTING_ENTRY_ID IN (SELECT CASE_LISTING_ENTRY_ID FROM XHIBIT.XHB_CASE_LISTING_ENTRY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) AND CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CHD_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CHD_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT *****LISTINGS*****
PROMPT
PROMPT Total number of CREST CASE_HEARING_DAY records in the staging schema for listings:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_HEARING_DAY_DM WHERE  LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_HEARING_DAY_DM WHERE  LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of these records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_CASE_ON_LIST records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_CASE_ON_LIST WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) AND CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_CASE_ON_LIST WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) AND CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT and are NOT Orphan records:
PROMPT
PROMPT SELECT stg.CHD_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id  AND stg.LIST_TYPE != 'X' and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);;
SELECT stg.CHD_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id  AND stg.LIST_TYPE != 'X' and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);

PROMPT Count of non processed records related to B or U case :
PROMPT
PROMPT SELECT stg.CHD_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id  AND stg.LIST_TYPE != 'X' and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND stg.case_type in ('B','U');;
SELECT stg.CHD_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HEARING_DAY_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id  AND stg.LIST_TYPE != 'X' and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND stg.case_type in ('B','U');

PROMPT Count the no of Orphan records - XHBSTG_CASE_HEARING_DAY_DM - LISTINGS :
PROMPT
PROMPT select count(*) from xhbstg_case_hearing_day_dm stg where stg.crest_court_id = &stg_crest_court_id AND stg.list_type != 'X' AND stg.case_type NOT IN ('B','U') AND NOT EXISTS (select 'X' from xhbstg_case_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);;
select count(*) from xhbstg_case_hearing_day_dm stg where stg.crest_court_id = &stg_crest_court_id AND stg.list_type != 'X' AND stg.case_type NOT IN ('B','U') AND NOT EXISTS (select 'X' from xhbstg_case_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type); 

PROMPT ==========  XHBSTG_CASE_HISTORY_DM  ======================
PROMPT
PROMPT Total number of CREST CASE_HISTORY records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_CASE_HISTORY records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_CASE_HISTORY WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_CASE_HISTORY WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CASE_TYPE, CASE_NO, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CASE_TYPE, CASE_NO, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_CASE_NOTE  =============================
PROMPT
PROMPT Count of the total number of CREST CASE_NOTE records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_NOTE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_NOTE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_NOTE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_NOTE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_DIARY_NOTE_ENTRY records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_DIARY_NOTE_ENTRY WHERE NOTE_TYPE_ID NOT IN (SELECT REF_LISTING_DATA_ID FROM XHIBIT.XHB_REF_LISTING_DATA WHERE REF_DATA_VALUE = 'DCN') AND (CASE_LISTING_ENTRY_ID IN  (SELECT CASE_LISTING_ENTRY_ID FROM XHIBIT.XHB_CASE_LISTING_ENTRY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) OR COURT_ID = &xhibit_court_id OR case_id in (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_DIARY_NOTE_ENTRY WHERE NOTE_TYPE_ID NOT IN (SELECT REF_LISTING_DATA_ID FROM XHIBIT.XHB_REF_LISTING_DATA WHERE REF_DATA_VALUE = 'DCN') AND (CASE_LISTING_ENTRY_ID IN  (SELECT CASE_LISTING_ENTRY_ID FROM XHIBIT.XHB_CASE_LISTING_ENTRY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) OR COURT_ID = &xhibit_court_id OR case_id in (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT and are NOT Orphan records:
PROMPT
PROMPT SELECT stg.CAN_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_CASE_NOTE_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);;
SELECT stg.CAN_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_CASE_NOTE_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);

PROMPT Count the no of Orphan records - XHBSTG_CASE_NOTE_DM :
PROMPT
PROMPT select count(*) from xhbstg_case_note_dm stg where stg.crest_court_id = &stg_crest_court_id AND stg.case_no is NOT NULL AND NOT EXISTS (select 'X' from xhbstg_case_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);;
select count(*) from xhbstg_case_note_dm stg where stg.crest_court_id = &stg_crest_court_id AND stg.case_no is NOT NULL AND NOT EXISTS (select 'X' from xhbstg_case_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type); 

PROMPT ==========  XHBSTG_CASE_OPPOSER_DM  =======================
PROMPT
PROMPT Total number of CREST CASE_OPPOSER records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_OPPOSER_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_OPPOSER_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_OPPOSER_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_OPPOSER_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_CASE_PROSECUTOR_AGENCY records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT List of any rows which were not updated in XHIBIT where there is an equivalent non obsolete row :
PROMPT
PROMPT SELECT OPP_ID, CASE_NO,CASE_TYPE FROM XHBSTG_CASE_OPPOSER_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY pa WHERE NVL(pa.OBS_IND,'N') != 'Y' AND pa.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE cs WHERE cs.COURT_ID = &xhibit_court_id AND cs.CASE_NUMBER = stg.CASE_NO AND cs.CASE_TYPE = stg.CASE_TYPE) AND pa.REF_PROSECUTOR_AGENCY_ID = (SELECT REF_PROSECUTOR_AGENCY_ID FROM XHIBIT.XHB_REF_PROSECUTOR_AGENCY rpa WHERE rpa.COURT_ID = &xhibit_court_id AND rpa.CREST_OPPOSER_ID = stg.OPP_ID and NVL(rpa.OBS_IND,'N') !='Y'));;
SELECT OPP_ID, CASE_NO,CASE_TYPE FROM XHBSTG_CASE_OPPOSER_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY pa WHERE NVL(pa.OBS_IND,'N') != 'Y' AND pa.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE cs WHERE cs.COURT_ID = &xhibit_court_id AND cs.CASE_NUMBER = stg.CASE_NO AND cs.CASE_TYPE = stg.CASE_TYPE) AND pa.REF_PROSECUTOR_AGENCY_ID = (SELECT REF_PROSECUTOR_AGENCY_ID FROM XHIBIT.XHB_REF_PROSECUTOR_AGENCY rpa WHERE rpa.COURT_ID = &xhibit_court_id AND rpa.CREST_OPPOSER_ID = stg.OPP_ID and NVL(rpa.OBS_IND,'N') !='Y'));

PROMPT ==========  XHBSTG_CASE_PARTY_SOF  ========================
PROMPT
PROMPT Total number of CREST CASE_PARTY_SOF records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_PARTY_SOF_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_PARTY_SOF_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_PARTY_SOF_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_PARTY_SOF_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_DEF_ON_CASE_REF_SOL_FIRM records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_DEF_ON_CASE_REF_SOL_FIRM WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_DEF_ON_CASE_REF_SOL_FIRM WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT Count of XHIBIT XHB_PROSECUTOR_REF_SOL_FIRM records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_PROSECUTOR_REF_SOL_FIRM WHERE CASE_PROS_AGENCY_ID IN (SELECT CASE_PROS_AGENCY_ID FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_PROSECUTOR_REF_SOL_FIRM WHERE CASE_PROS_AGENCY_ID IN (SELECT CASE_PROS_AGENCY_ID FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';

/*
--PROMPT List of any rows which were not inserted in XHIBIT where there is an equivalent non obsolete row :
--PROMPT
--PROMPT SELECT CPF_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_PARTY_SOF_DM STG WHERE STG.CREST_COURT_ID = &stg_crest_court_id AND STG.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_DEF_ON_CASE_REF_SOL_FIRM DS WHERE NVL(OBS_IND, 'N') != 'Y' AND DEFENDANT_ON_CASE_ID = (SELECT DEFENDANT_oN_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE DOC WHERE DOC.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE CS WHERE CS.COURT_ID = &xhibit_court_id AND CS.CASE_NUMBER = STG.CASE_NO AND CS.CASE_TYPE = STG.CASE_TYPE) AND DOC.DEFENDANT_ID = (SELECT DEFENDANT_ID FROM XHIBIT.XHB_DEFENDANT DEF WHERE DEF.COURT_ID = &xhibit_court_id AND DEF.CREST_DEFENDANT_ID = STG.SUB_OPP_ID)) AND DS.CREST_CPF_ID = STG.CPF_ID)
--PROMPT UNION
--PROMPT SELECT CPF_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_PARTY_SOF_DM STG WHERE STG.CREST_COURT_ID = &stg_crest_court_id AND STG.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_PROSECUTOR_REF_SOL_FIRM PS WHERE NVL(OBS_IND, 'N') != 'Y' AND PS.CASE_PROS_AGENCY_ID = (SELECT CASE_PROS_AGENCY_ID FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY CPA WHERE NVL(CPA.OBS_IND, 'N') != 'Y' AND CPA.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE CS WHERE CS.COURT_ID = &xhibit_court_id AND CS.CASE_NUMBER = STG.CASE_NO AND CS.CASE_TYPE = STG.CASE_TYPE)) AND PS.CREST_CPF_ID = STG.CPF_ID);;
--SELECT CPF_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_PARTY_SOF_DM STG WHERE STG.CREST_COURT_ID = &stg_crest_court_id AND STG.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_DEF_ON_CASE_REF_SOL_FIRM DS WHERE NVL(OBS_IND, 'N') != 'Y' AND DEFENDANT_ON_CASE_ID = (SELECT DEFENDANT_oN_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE DOC WHERE DOC.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE CS WHERE CS.COURT_ID = &xhibit_court_id AND CS.CASE_NUMBER = STG.CASE_NO AND CS.CASE_TYPE = STG.CASE_TYPE) AND DOC.DEFENDANT_ID = (SELECT DEFENDANT_ID FROM XHIBIT.XHB_DEFENDANT DEF WHERE DEF.COURT_ID = &xhibit_court_id AND DEF.CREST_DEFENDANT_ID = STG.SUB_OPP_ID)) AND DS.CREST_CPF_ID = STG.CPF_ID)
--UNION
--SELECT CPF_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_PARTY_SOF_DM STG WHERE STG.CREST_COURT_ID = &stg_crest_court_id AND STG.XHIBIT_ETL_STATUS NOT IN ('I', 'U') AND EXISTS (SELECT 'X' FROM XHIBIT.XHB_PROSECUTOR_REF_SOL_FIRM PS WHERE NVL(OBS_IND, 'N') != 'Y' AND PS.CASE_PROS_AGENCY_ID = (SELECT CASE_PROS_AGENCY_ID FROM XHIBIT.XHB_CASE_PROSECUTOR_AGENCY CPA WHERE NVL(CPA.OBS_IND, 'N') != 'Y' AND CPA.CASE_ID = (SELECT CASE_ID FROM XHIBIT.XHB_CASE CS WHERE CS.COURT_ID = &xhibit_court_id AND CS.CASE_NUMBER = STG.CASE_NO AND CS.CASE_TYPE = STG.CASE_TYPE)) AND PS.CREST_CPF_ID = STG.CPF_ID);

*/

PROMPT ==========  XHBSTG_CASE_SUB_APPEARNACE_DM  ========================
PROMPT
PROMPT *****FUTURE FIXTURES******* 
PROMPT
PROMPT Total number of CREST CASE_SUB_APPEARANCE records in the staging schema for future fixtures:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of these records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_FIXTURE_DEFT_ATTENDING records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_FIXTURE_DEFT_ATTENDING WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) AND CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_FIXTURE_DEFT_ATTENDING WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) AND CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CASE_TYPE, CASE_NO, CHD_ID, SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CASE_TYPE, CASE_NO, CHD_ID, SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE = 'X' AND LIST_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT *****LISTINGS*******
PROMPT
PROMPT Total number of CREST CASE_SUB_APPEARANCE records in the staging schema for listings:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of these records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUB_APPEARANCE_DM WHERE  CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id) AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_DEF_ON_CASE_ON_LIST records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_DEF_ON_CASE_ON_LIST WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) AND CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_DEF_ON_CASE_ON_LIST WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) AND CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT and are NOT Orphan records:
PROMPT
PROMPT SELECT stg.CASE_TYPE, stg.CASE_NO, stg.CHD_ID, stg.SUB_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUB_APPEARANCE_DM stg WHERE stg.CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id) AND stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U')  AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_SUBJECT_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc. case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);;
SELECT stg.CASE_TYPE, stg.CASE_NO, stg.CHD_ID, stg.SUB_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUB_APPEARANCE_DM stg WHERE stg.CHD_ID IN (SELECT CHD_ID FROM XHBSTG_CASE_HEARING_DAY_DM WHERE LIST_TYPE != 'X' AND CREST_COURT_ID = &stg_crest_court_id) AND stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U')  AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_SUBJECT_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc. case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);

PROMPT Count the no of Orphan records - XHBSTG_CASE_SUB_APPEARANCE_DM - LISTINGS  :
PROMPT
PROMPT select count(*) from xhbstg_case_SUB_APPEARANCE_dm stg where stg.crest_court_id = &stg_crest_court_id AND stg.chd_id in (select chd.chd_id from xhbstg_case_hearing_day_dm chd where chd.crest_court_id = stg.crest_court_id and chd.list_type != 'X') AND NOT EXISTS (select 'X' from xhbstg_case_subject_dm xc where xc. crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);;
select count(*) from xhbstg_case_SUB_APPEARANCE_dm stg where stg.crest_court_id = &stg_crest_court_id AND stg.chd_id in (select chd.chd_id from xhbstg_case_hearing_day_dm chd where chd.crest_court_id = stg.crest_court_id and chd.list_type != 'X') AND NOT EXISTS (select 'X' from xhbstg_case_subject_dm xc where xc. crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type and xc.sub_id = stg.sub_id);

PROMPT ==========  XHBSTG_CASE_SUBJECT_DM  =================================
PROMPT
PROMPT Total number of CREST CASE_SUBJECT records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CASE_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CASE_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_DEFENDANT_ON_CASE records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and LAST_UPDATED_BY in ( 'DATA_MIG','DATA MIGRATION');;
SELECT COUNT(*) FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and LAST_UPDATED_BY in ('DATA_MIG','DATA MIGRATION');

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CASE_TYPE, CASE_NO, SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CASE_TYPE, CASE_NO, SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CASE_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_CHAMBERS_DM  =========================
PROMPT
PROMPT Total number of CREST CHAMBERS records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CHAMBERS_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CHAMBERS_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CHAMBERS_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CHAMBERS_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_REF_CHAMBER records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_REF_CHAMBER WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_REF_CHAMBER WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT Count of XHIBIT XHB_REF_CHAMBER records in court which are not obsolete:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_REF_CHAMBER WHERE COURT_ID = &xhibit_court_id and NVL(OBS_IND,'N') != 'Y';;
SELECT COUNT(*) FROM XHIBIT.XHB_REF_CHAMBER WHERE COURT_ID = &xhibit_court_id and NVL(OBS_IND,'N') != 'Y';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CHA_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CHAMBERS_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CHA_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CHAMBERS_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_CHARGE_DM  =================================
PROMPT
PROMPT Total number of CREST CHARGE records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CHARGE_DM WHERE CASE_TYPE = 'A' AND CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CHARGE_DM WHERE CASE_TYPE = 'A' AND CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CHARGE_DM WHERE CASE_TYPE = 'A' AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CHARGE_DM WHERE CASE_TYPE = 'A' AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_OFFENCE records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_OFFENCE WHERE CHARGE_ID IN (SELECT CHARGE_ID FROM XHIBIT.XHB_CHARGE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_OFFENCE WHERE CHARGE_ID IN (SELECT CHARGE_ID FROM XHIBIT.XHB_CHARGE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CHG_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CHARGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id AND CASE_TYPE = 'A' AND XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CHG_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CHARGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id AND CASE_TYPE = 'A' AND XHIBIT_ETL_STATUS NOT IN ('I', 'U');


PROMPT ==========  XHBSTG_COMMITTAL_CHARGE_DM  =========================
PROMPT
PROMPT Total number of CREST COMMITTAL_CHARGE records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_COMMITTAL_CHARGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_COMMITTAL_CHARGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COMMITTAL_CHARGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COMMITTAL_CHARGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_CHARGES_LOG records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_CHARGES_LOG WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_CHARGES_LOG WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT and are NOT Orphan records:
PROMPT
PROMPT SELECT stg.CCH_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_COMMITTAL_CHARGE_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U')  AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);;
SELECT stg.CCH_ID, stg.XHIBIT_ETL_STATUS FROM XHBSTG_COMMITTAL_CHARGE_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.XHIBIT_ETL_STATUS NOT IN ('I', 'U')  AND EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM xc WHERE xc.CREST_COURT_ID = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);

PROMPT Count the no of Orphan records - XHBSTG_COMMITTAL_CHARGE_DM - LISTINGS  :
PROMPT
PROMPT select count(*) from XHBSTG_COMMITTAL_CHARGE_DM stg where stg.crest_court_id = &stg_crest_court_id AND NOT EXISTS (select 'X' from xhbstg_case_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type);;
select count(*) from XHBSTG_COMMITTAL_CHARGE_DM stg where stg.crest_court_id = &stg_crest_court_id AND NOT EXISTS (select 'X' from xhbstg_case_dm xc where xc.crest_court_id = stg.crest_court_id and xc.case_no = stg.case_no and xc.case_type = stg.case_type); 

PROMPT ========== XHBSTG_COURTROOM_DAY_DM  ========================
PROMPT
PROMPT Total number of CREST COURTROOM_DAY records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_COURTROOM_DAY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_COURTROOM_DAY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_DAY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_DAY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_SITTING_ON_LIST records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_SITTING_ON_LIST WHERE COURT_SITE_ID IN (SELECT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_SITTING_ON_LIST WHERE COURT_SITE_ID IN (SELECT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CTD_ID, XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_DAY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U', 'D');;
SELECT CTD_ID, XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_DAY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U', 'D');

PROMPT ==========  XHBSTG_COURTROOM_DM  ======================
PROMPT
PROMPT Total number of CREST COURTROOM records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_COURTROOM_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_COURTROOM_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_COURT_ROOM records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_COURT_ROOM WHERE COURT_SITE_ID IN (SELECT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id) and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_COURT_ROOM WHERE COURT_SITE_ID IN (SELECT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id) and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT COURTROOM_NO, CTL_ID, XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT COURTROOM_NO, CTL_ID, XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_COURTROOM_LOCATION_DM  =====================
PROMPT
PROMPT Total number of CREST COURTROOM_LOCATION records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_COURTROOM_LOCATION_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_COURTROOM_LOCATION_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_LOCATION_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_LOCATION_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_CCOURT_SITE records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CTL_ID, XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_LOCATION_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CTL_ID, XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_LOCATION_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_COURTROOM_USAGE_DM  ========================
PROMPT
PROMPT Total number of CREST COURTROOM_USAGE records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_COURTROOM_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_COURTROOM_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_COURT_ROOM_USAGE records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_COURT_ROOM_USAGE WHERE COURT_ROOM_ID IN (SELECT COURT_ROOM_ID FROM XHIBIT.XHB_COURT_ROOM WHERE COURT_SITE_ID IN (SELECT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_COURT_ROOM_USAGE WHERE COURT_ROOM_ID IN (SELECT COURT_ROOM_ID FROM XHIBIT.XHB_COURT_ROOM WHERE COURT_SITE_ID IN (SELECT COURT_SITE_ID FROM XHIBIT.XHB_COURT_SITE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CRU_ID, XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CRU_ID, XHIBIT_ETL_STATUS FROM XHBSTG_COURTROOM_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ===========  XHBSTG_CSU_HISTORY_DM  ========================
PROMPT
PROMPT Total number of CREST CSU_HISTORY records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_CSU_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_CSU_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CSU_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_CSU_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_DEFENDANT_ON_CASE_HISTORY records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_DEFENDANT_ON_CASE_HISTORY WHERE CASE_HISTORY_ID IN (SELECT CASE_HISTORY_ID FROM XHIBIT.XHB_CASE_HISTORY WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_DEFENDANT_ON_CASE_HISTORY WHERE CASE_HISTORY_ID IN (SELECT CASE_HISTORY_ID FROM XHIBIT.XHB_CASE_HISTORY WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT CASE_TYPE, CASE_NO, SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CSU_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT CASE_TYPE, CASE_NO, SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_CSU_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_DISPOSAL_DM  ======================
PROMPT
PROMPT Total number of CREST DISPOSAL records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_DISPOSAL_DM WHERE DISPOSAL_CODE = 'NMO' AND CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_DISPOSAL_DM WHERE DISPOSAL_CODE = 'NMO' AND CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_DISPOSAL_DM WHERE DISPOSAL_CODE = 'NMO' AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_DISPOSAL_DM WHERE DISPOSAL_CODE = 'NMO' AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_MONETARY_ORDER_TRACKING records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_MONETARY_ORDER_TRACKING WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_MONETARY_ORDER_TRACKING WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT DIS_ID, XHIBIT_ETL_STATUS FROM XHBSTG_DISPOSAL_DM WHERE DISPOSAL_CODE = 'NMO' AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT DIS_ID, XHIBIT_ETL_STATUS FROM XHBSTG_DISPOSAL_DM WHERE DISPOSAL_CODE = 'NMO' AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_HOME_COURT_DM  ========================
PROMPT
PROMPT Total number of CREST HOME_COURT records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_HOME_COURT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_HOME_COURT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_HOME_COURT_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_HOME_COURT_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_COURT records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_COURT WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_COURT WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT NAME, XHIBIT_ETL_STATUS FROM XHBSTG_HOME_COURT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT NAME, XHIBIT_ETL_STATUS FROM XHBSTG_HOME_COURT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_JUDGE_TICKET_DM  ========================
PROMPT
PROMPT Total number of CREST JUDGE_TICKET records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_JUDGE_TICKET_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_JUDGE_TICKET_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_JUDGE_TICKET_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_JUDGE_TICKET_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_REF_JUDGE_TICKET records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_REF_JUDGE_TICKET WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_REF_JUDGE_TICKET WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT JUD_ID, TICKET_TYPE, XHIBIT_ETL_STATUS FROM XHBSTG_JUDGE_TICKET_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT JUD_ID, TICKET_TYPE, XHIBIT_ETL_STATUS FROM XHBSTG_JUDGE_TICKET_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_JUDGE_USAGE_DM  =========================
PROMPT
PROMPT Total number of CREST JUDGE_USAGE records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_JUDGE_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_JUDGE_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_JUDGE_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_JUDGE_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_JUDGE_USAGE records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_JUDGE_USAGE WHERE REF_JUDGE_ID IN (SELECT REF_JUDGE_ID FROM XHIBIT.XHB_REF_JUDGE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_JUDGE_USAGE WHERE REF_JUDGE_ID IN (SELECT REF_JUDGE_ID FROM XHIBIT.XHB_REF_JUDGE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT JUU_ID, XHIBIT_ETL_STATUS FROM XHBSTG_JUDGE_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT JUU_ID, XHIBIT_ETL_STATUS FROM XHBSTG_JUDGE_USAGE_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_LEGAL_AID_AMENDMENT_DM  =====================
PROMPT
PROMPT Total number of CREST LEGAL_AID_AMENDMENT records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_LEGAL_AID_AMENDMENT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_LEGAL_AID_AMENDMENT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_LEGAL_AID_AMENDMENT_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_LEGAL_AID_AMENDMENT_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_LEGAL_AID_AMENDMENT records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_LEGAL_AID_AMENDMENT WHERE LEGAL_AID_ORDER_ID IN (SELECT LEGAL_AID_ORDER_ID FROM XHIBIT.XHB_LEGAL_AID_ORDER WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id))) and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_LEGAL_AID_AMENDMENT WHERE LEGAL_AID_ORDER_ID IN (SELECT LEGAL_AID_ORDER_ID FROM XHIBIT.XHB_LEGAL_AID_ORDER WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id))) and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT SEQ_NO, LEO_ID, XHIBIT_ETL_STATUS FROM XHBSTG_LEGAL_AID_AMENDMENT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT SEQ_NO, LEO_ID, XHIBIT_ETL_STATUS FROM XHBSTG_LEGAL_AID_AMENDMENT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');
  
PROMPT ==========  XHBSTG_LEGAL_AID_ORDER_DM  =====================
PROMPT
PROMPT Total number of CREST LEGAL_AID_ORDER records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_LEGAL_AID_ORDER_DM WHERE REFUSAL_DATE IS NULL AND CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_LEGAL_AID_ORDER_DM WHERE REFUSAL_DATE IS NULL AND CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_LEGAL_AID_ORDER_DM WHERE REFUSAL_DATE IS NULL AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_LEGAL_AID_ORDER_DM WHERE REFUSAL_DATE IS NULL AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_LEGAL_AID_ORDER records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_LEGAL_AID_ORDER WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_LEGAL_AID_ORDER WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and CREATED_BY = 'DATA MIGRATION';

PROMPT Count of XHIBIT XHB_LEGAL_AID_ORDER records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_LEGAL_AID_ORDER WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_LEGAL_AID_ORDER WHERE DEFENDANT_ON_CASE_ID IN (SELECT DEFENDANT_ON_CASE_ID FROM XHIBIT.XHB_DEFENDANT_ON_CASE WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id)) and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT LEO_ID, XHIBIT_ETL_STATUS FROM XHBSTG_LEGAL_AID_ORDER_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT LEO_ID, XHIBIT_ETL_STATUS FROM XHBSTG_LEGAL_AID_ORDER_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ========== XHBSTG_LISTS_DM  ======================
PROMPT
PROMPT Total number of CREST LISTS records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_LISTS_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_LISTS_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_LISTS_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_LISTS_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_LIST records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_LIST WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_LIST WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT LST_ID, XHIBIT_ETL_STATUS FROM XHBSTG_LISTS_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT LST_ID, XHIBIT_ETL_STATUS FROM XHBSTG_LISTS_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_NON_AVAIL_DATES_DM  =========================
PROMPT
PROMPT Total number of CREST NON_AVAIL_DATES records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_NON_AVAIL_DATES_DM WHERE END_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_NON_AVAIL_DATES_DM WHERE END_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_NON_AVAIL_DATES_DM WHERE END_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_NON_AVAIL_DATES_DM WHERE END_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_CASE_NON_AVAIL_DAYS records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_CASE_NON_AVAIL_DAYS WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_CASE_NON_AVAIL_DAYS WHERE CASE_ID IN (SELECT CASE_ID FROM XHIBIT.XHB_CASE WHERE COURT_ID = &xhibit_court_id) and CREATED_BY = 'DATA MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT NAD_ID, XHIBIT_ETL_STATUS FROM XHBSTG_NON_AVAIL_DATES_DM WHERE END_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT NAD_ID, XHIBIT_ETL_STATUS FROM XHBSTG_NON_AVAIL_DATES_DM WHERE END_DATE > SYSDATE AND CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT Count the no of Orphan records - XHBSTG_NON_AVAIL_DATES_DM:
PROMPT
PROMPT SELECT count(*) FROM XHBSTG_NON_AVAIL_DATES_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.END_DATE > SYSDATE AND NOT EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM XC WHERE XC.CREST_COURT_ID = STG.CREST_COURT_ID AND XC.CASE_NO = STG.CASE_NO AND XC.CASE_TYPE = XC.CASE_TYPE);;
SELECT count(*) FROM XHBSTG_NON_AVAIL_DATES_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and stg.END_DATE > SYSDATE AND NOT EXISTS (SELECT 'X' FROM XHBSTG_CASE_DM XC WHERE XC.CREST_COURT_ID = STG.CREST_COURT_ID AND XC.CASE_NO = STG.CASE_NO AND XC.CASE_TYPE = XC.CASE_TYPE);

PROMPT ==========  XHBSTG_SOLICITOR_FIRM_DM  ==========================
PROMPT
PROMPT Total number of CREST SOLICITOR_FIRM records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_SOLICITOR_FIRM_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_SOLICITOR_FIRM_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_SOLICITOR_FIRM_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_SOLICITOR_FIRM_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_REF_SOLICITOR_FIRM records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_REF_SOLICITOR_FIRM WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_REF_SOLICITOR_FIRM WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT Count of XHIBIT XHB_REF_SOLICITOR_FIRM records in court which are not obsolete:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_REF_SOLICITOR_FIRM WHERE COURT_ID = &xhibit_court_id and NVL(OBS_IND,'N') != 'Y';;
SELECT COUNT(*) FROM XHIBIT.XHB_REF_SOLICITOR_FIRM WHERE COURT_ID = &xhibit_court_id and NVL(OBS_IND,'N') != 'Y';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT SOF_ID, XHIBIT_ETL_STATUS FROM XHBSTG_SOLICITOR_FIRM_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT SOF_ID, XHIBIT_ETL_STATUS FROM XHBSTG_SOLICITOR_FIRM_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT ==========  XHBSTG_SUBJECT_DM  ============================
PROMPT
PROMPT Total number of CREST SUBJECT records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_DEFENDANT records which have been updated as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_DEFENDANT WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';;
SELECT COUNT(*) FROM XHIBIT.XHB_DEFENDANT WHERE COURT_ID = &xhibit_court_id and LAST_UPDATED_BY = 'DATA_MIG';

PROMPT list of any rows which have failed to process:
PROMPT
PROMPT SELECT SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_SUBJECT_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

PROMPT Count the no of records not linked to a case - XHBSTG_SUBJECT_DM:
PROMPT
PROMPT SELECT count(*) FROM XHBSTG_SUBJECT_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and NOT EXISTS (SELECT 'X' FROM XHBSTG_CASE_SUBJECT_DM XC WHERE XC. CREST_COURT_ID = stg.CREST_COURT_ID and xc.sub_id = stg.sub_id);;
SELECT count(*) FROM XHBSTG_SUBJECT_DM stg WHERE stg.CREST_COURT_ID = &stg_crest_court_id and NOT EXISTS (SELECT 'X' FROM XHBSTG_CASE_SUBJECT_DM XC WHERE XC. CREST_COURT_ID = stg.CREST_COURT_ID and xc.sub_id = stg.sub_id);

PROMPT ==========  XHBSTG_SUBJECT_HISTORY_DM  ======================
PROMPT
PROMPT Total number of CREST SUBJECT_HISTORY records in the staging schema:
PROMPT
PROMPT SELECT COUNT(*) FROM XHBSTG_SUBJECT_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;;
SELECT COUNT(*) FROM XHBSTG_SUBJECT_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id;

PROMPT Break down of the different status values of records in the staging table:
PROMPT
PROMPT SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_SUBJECT_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;;
SELECT COUNT(*), XHIBIT_ETL_STATUS FROM XHBSTG_SUBJECT_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id GROUP BY XHIBIT_ETL_STATUS;

PROMPT Count of XHIBIT XHB_DEFENDANT_HISTORY records which have been inserted as part of data migration:
PROMPT
PROMPT SELECT COUNT(*) FROM XHIBIT.XHB_DEFENDANT_HISTORY WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA_MIGRATION';;
SELECT COUNT(*) FROM XHIBIT.XHB_DEFENDANT_HISTORY WHERE COURT_ID = &xhibit_court_id and CREATED_BY = 'DATA_MIGRATION';

PROMPT List of any rows which were not inserted or updated in XHIBIT:
PROMPT
PROMPT SELECT SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_SUBJECT_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');;
SELECT SUB_ID, XHIBIT_ETL_STATUS FROM XHBSTG_SUBJECT_HISTORY_DM WHERE CREST_COURT_ID = &stg_crest_court_id and XHIBIT_ETL_STATUS NOT IN ('I', 'U');

SET VERIFY ON

SPOOL OFF
