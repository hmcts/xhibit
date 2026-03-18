create or replace package body xhb_data_migration_process_pkg as

/**
* CGI CREST TO XHIBIT Program
*
* MODULE      : xhb_data_migration_process_pkg
*
* DESCRIPTION : The package body to load and enrich data taken from CREST database and then load into the XHIBIT database
*
*               To run procedures in this package 
*               1. login to sqlplus as data_mig/data_mig
*               2. SPOOL log_<procedure_name>.log
*               3. set serveroutput on size 1000000
*                    begin
*                    <package_name>.<procedure_name>(<court id>);
*                    end;
*                    /
*               5. SPOOL OFF
*
* VERSION HISTORY:
*
* Date          Author (s)                           Version    Nature of Change
* ----------    --------------------------------     --------   -----------------------------------------------------------------------
* 18/09/2018    C.Cash. S.Sethuraman. A.Dennis       1.0        Code is a merge of dm_process_pkg_abe, dm_process_pkg_cc, dm_process_ss
*                                                               for the data migration or crest to xhibit.
* 21/11/2018    A Dennis, Barry Jones                1.1        Include a constant for the BULK COLLECT LIMIT so we can change from 1000 to another value. Uncomment COMMITs
*                                                               CTX-3020: Amended upd_xhb_croom_usage_crest to select cru_id in the Cursor so that it can be used to identify a unique row to update in XHBSTG_COURTROOM_USAGE_DM since it was setting etl_status of more than 1 row even though only 1 row was processed thus preventing those rows from being picked to be migrated. 
*                                                               CTX-2985: Amended upd_xhb_c_diary_fixture_crest to update XHBSTG_CASE_HEARING_DAY_DM table for only the list type X rows NOT processed 
*                                                               CTX-2991: Amended upd_xhb_c_list_entry_crest by changing the cursor SELECT so when doing the data migration from xhbstg_case_dm into XHB_CASE_LISTING_ENTRY, populate the JUDGE_ID field if the case_no and the case_type is in xhbstg_release_judge_dm and the req_jud_ind is Q. So if the case we are migrating from xhbstg_case_dm is not in xhbstg_release_judge_dm then put NULL in XHB_CASE_LISTING_ENTRY.JUDGE_ID.
*                                                               CTX-2992: Amended upd_xhb_mon_ord_track_crest by changing cursor to fetch OUTCOME_DATE instead of DOCUMENT_DATE from xhbstg.disposal_dm
*                                                               CTX-2987 and 3009: Amended upd_xhb_defendant_with_crest to process base on unique SELECT of sub_id, crest_court_id, case_no and case_type
*                                                               CTX-3006: Amended upd_xhb_charges_log_crest to add line feed to xhbstg_committal_charge_dm.charge_line when selected and length of it is less than 80
*                                                               CTX-2989: Amended upd_xhb_defendant_with_crest to fetch current_prison_status
*                                                               CTX-2988: Amended update_xhb_court_with_crest to format court_start_time appropriately
*                                                               CTX-2983: Amended update_xhb_case_with_crest to select xhbstg_case_dm.lc_conv_date for xhibit.xhb_case.mag_conviction_date for A cases
*                                                               CTX-3024: Amended upd_xhb_sit_on_list_crest so the Cursor SELECT to fetch xhbstg_courtroom_day_dm.list_date BETWEEN  xhb_list.list_start_date AND xhb_list.list_end_date
* 29/11/2018     A Dennis                            1.2        CTX-3206: Amended upd_xhb_c_diary_fixture_crest in the Cursor SELECT added: NVL(cc.fix_req_ind,'N') fix_req_ind   
*                                                               CTX-2985: Amended upd_xhb_case_on_list_crest in CURSOR cur_crest_col_details_1 to call a function get_next_list_seq_no to calculate incoming seq no if it is NULL based on incrementing the MAX list_sequence by 1 for the rows with that same CTD_ID in the table xhbstg_case_hearing_day_dm
*                                                               CTX-3211: Amended upd_xhb_legal_aid_amend_crest to update the correct row processed in xhbstg_legal_aid_amendment_dm v_crest_leo_id was not being populated. Also to update xhbstg_legal_aid_amendment_dm instead of xhbstg_legal_aid_order_dm for rows NOT processed. Also changed what logging and output messages.
*                                                               CTX-3214: 2 things done:
*                                                                         Amended upd_xhb_defendant_with_crest not to update the table xhbstg_case_party_sof_dm for rows NOT processed at the end of the procedure.
*                                                                         Amended update_xhb_prsf_with_crest the Cursor ref_sol_firm_cur to remove check for rows that have not undergone ETL so they can be processed again.
*                                                               CTX-3216: 2 things done:
*                                                                         Amended upd_xhb_c_diary_fixture_crest not to update the table XHBSTG_CASE_SUB_APPEARANCE_DM for rows NOT processed at the end of the procedure.
*                                                                         Amended upd_xhb_doc_on_list_crest the Cursor docol_cur to remove check for rows that have not undergone ETL so they can be processed again.
*                                                               CTX-3208: Amended every EXCEPTION block in procedures in this package where we call insert_dm_log for the input parameter p_log_msg to concatenate the Oracle Exception error message with something like:- 'Error: '||SUBSTR(v_err_message,1,150)
*                                                               CTX-2991: Amended upd_xhb_c_list_entry_crest to migrate all required data, as specified by the 2nd way in the FS, which means from XHBSTG_CASE_NOTE_DM to XHB_DIARY_NOTE_ENTRY
*                Barry Jones                                    CTX-3009: Amended upd_xhb_defendant_with_crest to concatenate the 4 Crest columns: cacd_result1, cacd_result2, cacd_result3, cacd_result4 and put in 1 XHIBIT column cacd_appeal_result
*
*                A Dennis                                       CTX-3210: Amended update_xhb_case_with_crest to populate CCC_TRANS_FROM_REF_COURT_ID and DATE_TRANS_FROM
*
* 04/12/2018     D Kinder                           1.3         CTX-3275: XHB_CASE_ON_LIST - Missing Time marking - Amended de_code column comparison as per FS Change v0.18        
*
* 04/12/2018     S Sethuraman                                   CTX-3007 - New Procedure added : Update XHB_REF_PROSECUTOR_AGENCY - EMAIL  in XHB_CONTACT_DETAIL - Sec 4.3.2.35 req [4975.DM.037]
* 05/12/2018     S Sethuraman                                   CTX-3005 - XHB_LEGAL_AID_ORDER - FS changes implemented
* 05/12/2018     D Kinder                                       CTX-3277 Fixed XHB_DIARY_NOTE_ENTRY court_id
* 06/12/2018     S Sethuraman                                   CTX-3274 - upd_xhb_case_on_list : SIT_ON_LIST record selection amended as per FS V0.18 changes
* 06/12/2018     S Sethuraman                                   CTX-3273 - SIT_ON_LIST record whould be selected even if REF_JUDGE_ID is NULL or doesn;t match XHIBIT xhb_ref_judge
* 07/12/2018     S Sethuraman                       1.4         CTX-3330 - UPD_XHB_CASE_ON_LIST - fixed XHBSTG_CASE_HEARING_DAY_DM - LIST_DATE SPANNING BETWEEN DATES NOT PICKED UP
* 11/12/2018     S Sethuraman                                   CTX-3367 - XHB_DEFENDANT_ON_CASE - CAO_STATUS mapping amended - as per FS V0.20
* 11/12/2018     S Sethuraman                                   CTX-3366 - XHB_CASE_ON_LIST - TIME_MARKING_ID NULL value issue FIXED
* 11/12/2018     S Sethuraman                                   CTX-3252 - Exception handling in all procedures - log error details and continue processing instead rollback
* 11/12/2018     S Sethuraman                                   CTX-3032 - - fx v0.20 MAPPING CORRECTION FOR REVOC_REASON
* 04/01/2019     S Sethuraman                       1.5         CTX-3375 - UPD_XHB_C_LIST_ENTRY_CREST - restrict diary_note entry for NON NULL default list notes only
* 10/01/2019     S Sethuraman                       1.6         CTX-3509 - UPD_XHB_CASE_ON_LIST - FS v0.22 changes for mapping corrections for SITTING_ON_LIST record updated
* 18/01/2019     S Sethuraman                       1.7         CTX-3575 - Pre-Migration - DM Staging data - Parsing XHBSTG_COURTROOM_DAY_DM and adding missing XHBSTG_LISTS_DM records
* 23/01/2019     S Sethuraman                       1.8         CTX-3577 - UPD_XHB_SIT_ON_LIST_CREST - DEFAULT TO COURT_SITE_CODE = 'X' if COURT_SITE_CODE NOT FOUND IN XHIBIT
* 29/01/2019     S Sethuraman                       1.9         CTX-3514 - UPD_XHB_CASE-ON_LIST_CREST - Set default TIME_MARKING to "SITTING_AT "  is none present in CREST
* 31/01/2019     S Sethuraman                       1.10        CTX-3643 - UPD_XHB_SIT_ON_LIST - Update back XHBSTG_COURTROOM_DAY_DM for default court sites as well
* 04/02/2019     S Sethuraman                       1.11        CTX-3644 - parse_xhbstg_courtroom_day_dm - Filter Condition for crest_court_id added in CURSOR SQL when when XHBSTG_LISTS
* 05/02/2019     S Sethuraman                                   CTX-3654 - UPD_XHB_CROOM_USAGE_CREST - FIX incorrect data_type assignments to v_ctl_id,v_courtroom_no
* 13/02/2019     S Sethuraman                       1.12        CTX-3705 - parse_xhbstg_courtoom_day_dm - add missing courtroom_day_dm records from xhbstg_case_hearing_day_dm
* 14/02/2019     S Sethuraman                                   CTX-3715 - get_next_list_seq_no function - Add CREST_COURT_ID parameter along with CTD_ID
*                                                                          and add crest_court_id to ctd_id when referenced as index
*                                                                          UPD_XHB_CASE_ON_LIST - HANDLE LIST_SEQUENCE NULL FOR BOTH CTD_ID IS NULL AND IS NOT NULL
* 20/02/2019     S Sethuraman                       1.13        CTX-3731 - parse_xhbstg_courtoom_day_dm - add missing crest_court_id in subquery 
*                                                                          in the newly added cursor for adding missing courtroom_day_dm records from xhbstg_case_hearing_day_dm
* 21/02/2019     S Sethuraman                                   CTX-3729 - UPD_XHB_CASE_ON_LIST - Time_marking mapping NULL ONLY to NULL and if time set default to SITTING AT
* 21/02/2019     S Sethuraman                                   CTX-3745 - UPD_XHB_DOC_HISTORY_CREST CURSOR SQL - NOT EXISTS clause incorrectly refers to staging DEFENDANT_NO instead of XHIBIT DOC HISTORY.DEFENDANT_NUMBER
* 21/02/2019     S Sethuraman                                   CTX-3743 - Added New procedure UPD_XHB_OUT_BW_HISTORY_CREST - t add  Outstanding Bench Waranty from CREST
* 26/02/2019     S Sethuraman                       1.14        CTX-3771 - UPD_XHB_C_LIST_ENTRY_CREST - add CREST_COURT_ID  mapping in SUB QUERY in BOTH CURSORS
* 26/02/2019     S Sethuraman                                   CTX-3773 - UPD_XHB_C_DIARY_FIXTURE_CREST - EXTRACT 35 CHARS ONLY OF REASON_REMOVED TO MATCH XHIBIT FIELD LENGTH
* 26/02/2019     S Sethuraman                                   CTX-3776 - UPD_XHB_CASE_NAD_WITH_CREST - Map NULL REASON with '-'
* 26/02/2019     S Sethuraman                                   CTX-3764 - UPD_XHB_DEFENDANT_WITH_CREST - RESULTS VERIFIED if no non hearing disposal entered - NO change to existing RESULTS VERIFIED field in XHIBIT
* 26/02/2019     S Sethuraman                                   CTX-3778 - ignore records with NULL LIST DATES when adding missing CASE_HEARING_DAY_DM
* 26/02/2019     S Sethuraman                                   CTX-3734 - UPD_XHB_CASE_ON_LIST - CRACKED EFFECTIVE IND - to populate XHB_SCHEDULED_HEARING
* 27/02/2019     D Kinder                                       CTX-3772 - parse_xhbstg_courtroom_day_dm -  Data Cleanse START_TIME column in XHBSTG_COURTROOM_DAY_DM
* 28/02/2019     S Sethuraman                                   CTX-3779 - ADD courtroom_day_dm records where case_hearing_day.ctd_id is NULL and also add missing lists records where courtroom_day list_type is other than D as well
* 06/03/2019     S Sethuraman                                   CTX-3810 - UPD_XHB_CASE_ON_LIST - Dry run data issues handled - LIST_dATE NOT NULL included in SQL and TIME_MARKING transformation handled
*                                                                        - parse_xhbstg_courtroom_day procedure - added code to cleanse TIME_MARKING data in CASE_HEARING_DAY_DM
* 06/03/2019     S Sethuraman                                   CTX-3812 - UPD_XHB_LEGAL_AID_AMEND_CREST - Fixed for missing court_id
* 06/03/2019     S Sethuraman                                   CTX-3814 - parse_xhbstg_courtroom_day_dm - START_TIME Time before mignight to be handled i.e. 00:15 am tobe set as 12:15 am 
* 06/03/2019     S Sethuraman                                   CTX-3821 - XHB_CASE.CREATION_DATE updated to CASE.DATE_INPUT  in UPDATE_XHB_cASE_WITH_CREST and MERGE_CREST_CASES_INTO_XHIBIT
* 07/03/2019     S Sethuraman                                   CTX-3813 - UPD_XHB_DOC_ON_LIST_CREST - inculde mapping with SITTING_ON_LIST to avoid duplicates
* 07/03/2019     S Sethuraman                                   CTX-3823 - UPD_XHB_COURT_ROOM_WITH_CREST - Link COURTROOM+LOCATION 
* 07/03/2019     S Sethuraman                                   CTX-3822 - UPD_XHB_CASE_ON_LIST - Cracked_ineffective_IND - update UPD_SCHEDULED_HEARING where multiple HEARING_IDs are fetched - UPDATE ALL
* 07/03/2019     S Sethuraman                                   CTX-3829 - Reports LODR issue - FS changes v0.31 - UPD_DIARY_NE_NO_CASE_CREST and UPD_XHB_C_LIST_ENTRY_CREST updated
* 12/03/2019     S Sethuraman                                   CTX-3844 - UPD_XHB_mon_ord_track_CREST, XHB_UPD_OUT_BW_HISTORY - Update Staging DISPOSAL_DM table back for updat status with disp_id filter included - both CURSORS include LIST_DATE > SYSDATE to migrate FURTURE FIXTURES ONLY of list_type = 'X'
* 12/03/2019     S Sethuraman                                   CTX-3845 - UPD_XHB_C_DIARY_FIXTURE_CREST - both CURSORS include LIST_DATE > SYSDATE to migrate FURTURE FIXTURES ONLY of list_type = 'X'
* 14/03/2019     S Sethuraman                                   CTX-3855 -  parse_xhbstg_courtroom_day and upd_xhb_sitting_on_list procedures updated to address SITTING_ON_LIST / CASE_ON_LIST Duplicates issue - 
* 14/03/2019     S Sethuraman                                   CTX-3856 - DOC_ON_LIST - Floater Cases issue fixed
* 14/03/2019     S Sethuraman                                   CTX-3848 - Staging records U status update does not tally with XHIBIT migrated record count
* 15/03/2019     A Dennis                           1.15        CTX-3846 - UPD_XHB_DEFENDANT_WITH_CREST - When there is more than 1 defendant for the same case number and type the same staging record in XHBSTG_CASE_PARTY_SOF_DM can be updated twice, firstly to the correct status of U and later to N when encountered for different Xhibit courts
* 21/03/2019     A Dennis                                       CTX-3887 - UPD_XHB_DOC_ON_LIST_CREST Fix to migrate defendants on Warned Lists
* 26/03/2019     S Sethuraman                       1.16        CTX-3894 - parse_xhbstg_courtroom_day updated to cater for more data cleansing of START_TIME column in COURTROOM_DAY_DM 
* 27/03/2019     S Sethuraman                                   CTX-3895 - UPD_XHB_CASE_WITH_CREST - XHB_CASE.MAG_CONVICTION_DATE updated for case_type = 'S'
* 27/03/2019     S Sethuraman                                   CTX-3909 - UPD_XHB_CASE_ON_LIST_WITH_CREST - Cracked_ineffective_id - new definition updated
* 28/03/2019     S Sethuraman                                   CTX-3913 - PARSE_XHBSTG_COURTROOM_DAY_DM updated to mark duplicate COURTROOM_DAY_DM CREST entries
* 28/03/2019     A Dennis                                       CTX-3908: Procedure to be called to update, as in link, XHB_CASE_ON_LIST.CASE_DIARY_FIXTURE_ID with its equivalent XHB_CASE_DIARY_FIXTURE.CASE_DIARY_FIXTURE_ID after both tables have been populated during the data migration run for a court.
* 01/04/2019     S Sethuraman                       1.17        CTX-3934 : PARSE_XHBSTG_COURTROOM_DAY_DM - START_TIME - DOUBLE OCCURANCES OF SPECIAL CHARACTERS CAUSING ISSUE IN TRANSFORMAtion - set to NULL
* 23/04/2019     S Sethuraman                       1.18        CTX-4138 : parse_xhbstg_courtroom_day -  birmingham issues - START_TIME needs to be TIMMED in all usages when transforming data
* 23/04/2019     S Sethuraman                                   CTX-4120 : courtroom_usage - rsit report issues -default 0 set to HOURS / MINS if NULL
* 23/04/2019     S Sethuraman                                   CTX-4119 : XHB_CASE_ON_LIST - Cracked effectve ind set only if list type is D
* 30/04/2019     S Sethuraman                       1.19        CTX-4163 : XHB_SIT_ON_LIST / XHB_CASE_ON_LIST - Missing COURTROOM for valid CREST COURT SITE - ASSUME DEFAULT DUMMY COURT SITE 'X' / COURT ROOM  1
* 30/04/2019     S Sethuraman                                   CTX-4166 : Parse_XHBSTG_COURTROOM_DAY - Fix incoorect setting of jud_seq_no when 1 to 9 already exists - assume default 1
* 01/05/2019     S Sethuraman                                   CTX-4167 : Legal_aid_order - REVOC_DATE IS NOT NULL - Condition to be removed and record migrated if NOT already existis in XHIBIT
* 02/05/2019     S Sethuraman                                   CTX-4179 : Extra rows on def_on_case_on_list - where multiple hearing/sittings on same case same day - choos 1 case_on_list record
* 14/05/2019     S Sethuraman                       1.20        CTX-     :  Parse procedure - duplicate marking process for dummy sites to include missing courtrooms check (ctx-4163 change)
* 15/05/2019     S Sethuraman                                   CTX-     :  UPD_XHB_SIT_ON_LIST - update statement for marking 'I' / 'N' - NULL handled in site code and court room no
* 16/05/2019     S Sethuraman                                   CTX-4213 :  UPD_XHB_CASE_ON_LIST procedure - if dummy site exclude matching jud_seq_no as it is defaulted to 1 and the crest value may be different
* 22/05/2019     S Sethuraman                                   CTX-4213-Update : UPD_XHB_cASE_ON_LIST - Add decode statement on both sides
* 28/05/2019     S Sethuraman                       1.21        CTX-  : BRISTOL DR run issue - PARSE_XHB_COURTROOM_DAY_DM : CASE_ON_LIST "Hour must be between 1 and 12" - 
* 29/05/2019     S Sethuraman                                   CTX-  : BRistol run - v_seqno data_type reference changed to XHIBIT table in get_next_list_seq_no function.
* 04/06/2019     S Sethuraman                       1.22        CTX-4284 - DO NOT MAP if ALL of MO_COSTS,MO_FINE AND MO_COMPENSATION are NULL or 0
* 10/06/2019     S Sethuraman                       1.23        Mold C_LIST_ENTRY Duplicate in XHBSTG_RELEASE_JUDGE_DM - fix
* 11/06/2019     S Sethuraman                                   Case_on_list Performance issue - change XCS / XCR as in-line view
* 13/06/2019     S Sethuraman                                   - CTX-     : re-run etl1 issue - include xhibit_status is NULL condition when marking duplicates in COURTROOM_DAY       
* 13/06/2019     S Sethuraman                                   - CTX -   UPD_XHB_DOC_ON_LIST_CREST --  Treat those records as Floater cases where priority is R and List type is F and CTD_ID is null
* 16/06/2019     S Sethuraman                                   Case_on_list Performance issue Duplicates issue fixed
* 17/06/2019     S Sethuraman                                   CTX-4326 : Legal AID ORDER - OBS_IND added to migration
* 25/06/2019     S Sethuraman                                   CTX-CASE_NOTE Ordering issue - CAN_ID added to systimestamp and appended to note_date
* 05/07/2019     S Sethuraman                                   CTX-4217 - DEFENDANT_ON_CASE.CTL_APPLIES mapping added
* 16/07/2019     S Sethuraman                       1.24        CTS_4409 merge_crest_cases_into_xhibit,update_xhb_case_with_crest
**/

    g_bulk_limit    NUMBER := 1; -- This will determine how many rows to process before proactively commiting
    TYPE g_list_sequence_nos IS TABLE OF NUMBER INDEX BY VARCHAR2(30); -- To hold list_sequence  values when processing Case On Lists
    g_list_sequence_list g_list_sequence_nos;

/**
  * NAME       : insert_dm_log
  * DESCRIPTION: Inserts log messages, into table during data migration  
  * PARAMETERS : p_court_id     - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE insert_dm_log  (p_crest_court_id    IN xhbstg_data_migration_log.court_id%type 
                         ,p_action_name       IN xhbstg_data_migration_log.action_name%type
                         ,p_run_time          IN xhbstg_data_migration_log.run_time%type
                         ,p_log_msg_type      IN xhbstg_data_migration_log.log_message_type%type
                         ,p_log_msg           IN xhbstg_data_migration_log.log_message%type
                         ,p_err_row_count     IN xhbstg_data_migration_log.error_row_count%type
                         ,p_success_row_count IN xhbstg_data_migration_log.success_row_count%type
                         ,p_last_updated_by   IN xhbstg_data_migration_log.last_updated_by%type DEFAULT 'DATA MIGRATION'
                         ,p_created_by        IN xhbstg_data_migration_log.created_by%type DEFAULT 'DATA MIGRATION'
                         )
IS
 BEGIN
 
  INSERT INTO xhbstg_data_migration_log (data_migration_log_id
                                        ,court_id
                                        ,action_name
                                        ,run_time
                                        ,log_message_type
                                        ,log_message
                                        ,error_row_count
                                        ,success_row_count
                                        ,creation_date 
                                        ,last_updated_by
                                        ,created_by)
  VALUES (xhbstg_data_migration_log_seq.nextval 
         ,p_crest_court_id
         ,p_action_name
         ,p_run_time 
         ,p_log_msg_type
         ,p_log_msg
         ,p_err_row_count
         ,p_success_row_count
         ,SYSDATE
         ,p_last_updated_by
         ,p_created_by
         );
 
 
  EXCEPTION
    WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('!!! AN ERROR OCCURRED INSERTING LOGGING DATA FOR: '||p_crest_court_id||' ERROR: '||SUBSTR(SQLERRM,1,110));
 
 END insert_dm_log;

/**
  * NAME       : get_xhb_court_from_crest_court
  * DESCRIPTION: Function to be called to get the corresponding Xhibit court id from the Crest court id passed in.
  * PARAMETERS : p_crest_court_id         - The CREST Court ID being processed will be passed to this function
**/
FUNCTION get_xhb_court_from_crest_court(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE)
RETURN NUMBER

AS

    v_court_id  xhibit.xhb_court.court_id%TYPE;
    
BEGIN

    BEGIN
    
        SELECT xco.court_id
        INTO   v_court_id
        FROM   xhibit.xhb_court xco
        WHERE  xco.crest_court_id = p_crest_court_id
        AND    NVL(xco.obs_ind,'N') != 'Y';
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('!!! THE CREST COURT ID: '||p_crest_court_id||' PASSED IN DID NOT MATCH AN XHIBIT COURT ID');
        WHEN TOO_MANY_ROWS THEN
            DBMS_OUTPUT.PUT_LINE('!!! THE CREST COURT ID: '||p_crest_court_id||' PASSED IN MATCHED MORE THAN 1 XHIBIT COURT ID');
    END;
    
    RETURN v_court_id;
    
EXCEPTION
    WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('!!! AN ERROR OCCURRED LOOKING FOR XHIBIT COURT ID FOR CREST COURT ID: '||p_crest_court_id||' ERROR: '||SUBSTR(SQLERRM,1,110));
    
END get_xhb_court_from_crest_court;


/**
  * NAME       : get_next_list_seq_no
  * DESCRIPTION: Function to be called to calculate the next sequence no for the row being processed based on the CTD_ID in xhbstg_case_hearing_day_dm passed in.
  * PARAMETERS : p_ctd_id   - The CTD_ID that will be passed to this function
  *  14/02/2019 - S Sethuraman - CTX-3715 - Add CREST_COURT_ID parameter along with CTD_ID
  *                                         and add crest_court_id to ctd_id when referenced as index
  * 29/05/2019 - S Sethuraman - BRISTOL Issue - v_seq_no data_type reference changed to XHIBIT table
**/
FUNCTION get_next_list_seq_no (p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE, p_ctd_id    IN xhbstg_case_hearing_day_dm.ctd_id%TYPE)
RETURN  NUMBER
AS 

    v_seq_no xhibit.xhb_case_on_list.seq_no%TYPE := 0; -- CTX_3715 Set to 0 instead of NULL

BEGIN

    -- For rows, belonging to a CTD_ID, that is processed and has at least 1 NULL list_sequence store the seq no calculated globally in order to increment as we process remaining rows
    IF NOT g_list_sequence_list.EXISTS(p_crest_court_id||TO_CHAR(p_ctd_id)) THEN    
        
        SELECT  MAX(NVL(xchd.list_sequence,0)) +1
        INTO v_seq_no
        FROM xhbstg_case_hearing_day_dm xchd
        WHERE xchd.crest_court_id = p_crest_court_id and
              xchd.ctd_id = p_ctd_id;
    
        g_list_sequence_list(p_crest_court_id||TO_CHAR(p_ctd_id)) := v_seq_no;
     
    ELSE -- the CTD_ID has been processed before so increment the previous value stored by 1 to get the seq no
    
        g_list_sequence_list(p_crest_court_id||TO_CHAR(p_ctd_id)) := g_list_sequence_list(p_crest_court_id||TO_CHAR(p_ctd_id)) + 1;

    END IF; 
    
  RETURN TO_NUMBER(g_list_sequence_list(p_crest_court_id||TO_CHAR(p_ctd_id)));
  
END get_next_list_seq_no;


/**
  * NAME       : list_crest_cases_not_in_xhibit
  * DESCRIPTION: CTX-2173: Procedure to be called to find and list all Cases that are in CREST but not yet in XHIBIT.  This list will 
  *              later be cross check with another task, to be done under CTX-2174, that will insert thes Cases as new rows into XHIBIT XHB_CASE table.
  * PARAMETERS : p_crest_court_id         - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE list_crest_cases_not_in_xhibit(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE)
AS

    TYPE xhbstg_case_dm_rec IS RECORD
    (
      case_no          xhbstg_case_dm.case_no%TYPE
    , case_type        xhbstg_case_dm.case_type%TYPE
    );

    TYPE xhbstg_case_dm_type IS TABLE OF xhbstg_case_dm_rec;
    xhbstg_case_dm_tt  xhbstg_case_dm_type;
    
    CURSOR cur_list_cases IS
    SELECT xcd.case_no
         , xcd.case_type
    FROM  xhbstg_case_dm xcd
    WHERE xcd.crest_court_id = p_crest_court_id
    AND   NVL(xcd.xhibit_etl_status,'N') NOT IN ('U','I')
    AND   xcd.xhibit_enrich_date  IS NULL
    MINUS 
    SELECT xc.case_number
         , xc.case_type
    FROM  xhibit.xhb_case xc
        , xhibit.xhb_court xco
    WHERE xc.court_id           = xco.court_id
    AND   xco.crest_court_id    = p_crest_court_id
    AND   NVL(xco.obs_ind,'N') != 'Y'
    ORDER BY 1; 
    
    v_total_cases  NUMBER := 0;
    
BEGIN

    -- Log the Crest Court, procedure name, Jira ticket and the Xhibit table
    insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                  , p_action_name        => 'list_crest_cases_not_in_xhibit- CTX-2173'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            =>  'XHB_CASE - Starting listing of Cases that are in CREST but not yet in XHIBIT'
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  =>  NULL
                  , p_created_by         => 'DATA MIGRATION'              
                  , p_last_updated_by    => 'DATA MIGRATION'        
                  );
                  
    DBMS_OUTPUT.PUT_LINE('LIST OF CASES THAT ARE IN CREST BUT NOT YET IN XHIBIT FOR CREST COURT ID = '||p_crest_court_id);
    DBMS_OUTPUT.PUT_LINE('CASE NO , CASE TYPE');

    -- NOTES
    -- This procedure is only listing Cases so we are not commiting into XHIBIT tables
    -- We are using a Cursor to gracefully handle when no data found for the list when processing certain court ids that may have cases all already in XHIBIT
    -- We BULK COLLECT LIMIT just in case we find too many rows that will blow process memory. This way we process up to 1000 a time then fetch the next up to 1000
    OPEN cur_list_cases;
    LOOP
    FETCH cur_list_cases BULK COLLECT INTO xhbstg_case_dm_tt LIMIT g_bulk_limit;

        IF xhbstg_case_dm_tt IS NOT NULL AND xhbstg_case_dm_tt.COUNT > 0 THEN  -- prevents numeric or value error later if nothing in the array
        
            v_total_cases := v_total_cases + xhbstg_case_dm_tt.COUNT;  -- we want to count number of cases found as we collect them
            
            FOR i IN xhbstg_case_dm_tt.FIRST .. xhbstg_case_dm_tt.LAST LOOP
            
                -- List the Cases found
                DBMS_OUTPUT.PUT_LINE(xhbstg_case_dm_tt(i).case_no||','||xhbstg_case_dm_tt(i).case_type);
                insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                              , p_action_name        => 'list_crest_cases_not_in_xhibit- CTX-2173'
                              , p_run_time           => SYSDATE
                              , p_log_msg_type       => 'I' -- Information
                              , p_log_msg            => 'XHB_CASE - Found case_no = '||xhbstg_case_dm_tt(i).case_no||', case_type = '||xhbstg_case_dm_tt(i).case_type
                              , p_err_row_count      => NULL
                              , p_success_row_count  => NULL
                              , p_created_by         => 'DATA MIGRATION'              
                              , p_last_updated_by    => 'DATA MIGRATION'        
                              );
                  
            END LOOP;

        END IF;
        
    EXIT WHEN cur_list_cases%NOTFOUND;
    
    END LOOP;
    CLOSE cur_list_cases;
    
    DBMS_OUTPUT.PUT_LINE('TOTAL NUMBER OF CASES FOUND = '||v_total_cases);
    insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                  , p_action_name        => 'list_crest_cases_not_in_xhibit- CTX-2173'
                  , p_run_time           => SYSDATE
                  , p_log_msg_type       => 'I' -- Information
                  , p_log_msg            => 'XHB_CASE - TOTAL NUMBER OF CASES FOUND = '||v_total_cases
                  , p_err_row_count      => NULL
                  , p_success_row_count  => v_total_cases
                  , p_created_by         => 'DATA MIGRATION'              
                  , p_last_updated_by    => 'DATA MIGRATION'        
                  );
                  
        COMMIT;  --- Uncomment when running for real

--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
    
        IF cur_list_cases%ISOPEN THEN
            CLOSE cur_list_cases;
        END IF;
        
        ROLLBACK;
        
        DBMS_OUTPUT.PUT_LINE('!! AN ERROR HAS OCCURRED. ERROR: '||SUBSTR(SQLERRM,180));
        -- Log the error
        insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                      , p_action_name        => 'list_crest_cases_not_in_xhibit- CTX-2173'
                      , p_run_time           =>  SYSDATE
                      , p_log_msg_type       =>  'E' -- Error
                      , p_log_msg            => 'XHB_CASE - !! AN ERROR HAS OCCURRED. ERROR: '||SUBSTR(SQLERRM,180)
                      , p_err_row_count      =>  1
                      , p_success_row_count  =>  NULL
                      , p_created_by         => 'DATA MIGRATION'              
                      , p_last_updated_by    => 'DATA MIGRATION'        
                      );
                      
        COMMIT;  --- Uncomment when running for real

END list_crest_cases_not_in_xhibit;


/**
  * NAME       : update_xhbstg_case_dm
  * DESCRIPTION: CTX-2174 This function when called will update the relevant row, based on the parametere passed, in XHBSTG_CASE_DM table to show
  *              that the row has been processed and merged into XHIBIT XHB_CASE table. It will then return a value of zero because it is 
  *              being called from the VALUES part of a MERGE statement, since one does not have direct access to rows being processed in 
  *              a MERGE statement so we call this function to update the rows being processed in XHBSTG_CASE_DM and then return zero to add,
  *              without any effect, to the new CASE_ID being inserted into XHB_CASE table.
  *              
  * PARAMETERS : p_crest_court_id         - The CREST Court ID being processed will be passed to this procedure
  *            : p_case_no                - The Case number
  *            : p_case_type              - The Case Type
  *            : p_xhibit_court_id        - The XHIBIT Court ID
**/
FUNCTION update_xhbstg_case_dm( p_crest_court_id  IN xhbstg_case_dm.crest_court_id%TYPE
                              , p_case_no         IN xhbstg_case_dm.case_no%TYPE
                              , p_case_type       IN xhbstg_case_dm.case_type%TYPE
                              , p_xhibit_court_id IN xhbstg_case_dm.xhibit_court_id%TYPE)
RETURN NUMBER
AS

    v_etl_date    xhbstg_case_dm.xhibit_etl_date%TYPE;
    v_etl_status  xhbstg_case_dm.xhibit_etl_status%TYPE;
    v_err_message xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

    -- Update XHIBIT_ETL_STATUS, XHIBIT_ETL_DATE and XHIBIT_COURT_ID in the staging table to show that the row has been merged into the XHB_CASE table
    -- Here we will not set the XHIBIT_ENRICH_DATE, will be set when we Update the rows we have Merged in a later task.
    UPDATE xhbstg_case_dm xcd
    SET    xcd.xhibit_etl_status  = 'M'  -- Merged into XHIBIT tsble
         , xcd.xhibit_etl_date    = SYSDATE
         , xcd.xhibit_court_id    = p_xhibit_court_id
    WHERE xcd.crest_court_id      = p_crest_court_id
    AND   xcd.case_no             = p_case_no
    AND   xcd.case_type           = p_case_type
    RETURNING xcd.xhibit_etl_status
            , xcd.xhibit_etl_date
    INTO      v_etl_status
            , v_etl_date;

    -- Log the fact that we have processed the row fetched for merge
    insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                  , p_action_name        => 'merge_crest_cases_into_xhibit- CTX-2174'  -- still part the of the calling procedure merge_crest_cases_into_xhibit
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            => 'XHB_CASE merged case_no = '||p_case_no||' case_type = '||p_case_type
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  => 1
                  , p_created_by         => 'DATA MIGRATION'              
                  , p_last_updated_by    => 'DATA MIGRATION'        
                  );
    
    -- Comment or Uncomment to capture this information 
    --DBMS_OUTPUT.PUT_LINE('COURT_ID = '||p_crest_court_id||' CASE_NO= '||p_case_no||' CASE_TYPE= '||p_case_type||' ETL_DATE = '||TO_CHAR(v_etl_date,'DD/MM/YYYY')||' STATUS = '||v_etl_status);

    -- NOTE in this function commit will be handled by the calling procedure
    
    RETURN 0;
       
EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SUBSTR(SQLERRM,1,500);
        
        -- NOTE in this function rollback will be handled by the calling procedure
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhbstg_case_dm for case_no= '||p_case_no||' case_type= '||p_case_type||' ERROR: '||SUBSTR(v_err_message,1,110));
        
        -- Update the row in the CREST table that caused the failure
        UPDATE xhbstg_case_dm xcd
        SET    xcd.xhibit_etl_err_message  =  v_err_message
             , xcd.xhibit_etl_status       =  'X'   -- Error
             , xcd.xhibit_enrich_date      =  SYSDATE
        WHERE  xcd.case_no                 =  p_case_no
        AND    xcd.case_type               =  p_case_type
        AND    xcd.crest_court_id          =  p_crest_court_id; 
        
        -- Log the error with the failing row details and the Oracle Exception
        insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                      , p_action_name        => 'merge_crest_cases_into_xhibit- CTX-2174'
                      , p_run_time           =>  SYSDATE
                      , p_log_msg_type       =>  'E' -- Error
                      , p_log_msg            => 'XHB_CASE - ERROR when merging case_no = '||p_case_no||' case_type = '||p_case_type||' ERROR: '||SUBSTR(v_err_message,1,180)
                      , p_err_row_count      =>  1
                      , p_success_row_count  =>  NULL
                      , p_created_by         => 'DATA MIGRATION'              
                      , p_last_updated_by    => 'DATA MIGRATION'        
                      );

     COMMIT; -- Commit when running for real 
    
END update_xhbstg_case_dm;


/**
  * NAME       : merge_crest_cases_into_xhibit
  * DESCRIPTION: CTX-2174: Procedure to be called to find all Cases that are in CREST but not yet in XHIBIT and then insert 
  *              those Cases in the XHB_CASE table in XHIBIT. Before this procedure is called to perform the MERGE the script called
  *              errorlog_xhbstg_case_dm.sql should have been run already to use the Oracle feature DBMS_ERRLOG.CREATE_ERROR_LOG
  *              to create the table err$_xhbstg_case_dm
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
  * 06/03/2019     S Sethuraman      - CTX-3821 - XHB_CASE.CREATION_DATE updated to CASE.DATE_INPUT  in UPDATE_XHB_cASE_WITH_CREST and MERGE_CREST_CASES_INTO_XHIBIT
  * 16/07/2019     S Sethuraman       - CTX-4409 - Remove OBS_IND check for REF_COURT_ID mapping
**/
PROCEDURE merge_crest_cases_into_xhibit(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
    v_count_crest_merged_rows NUMBER := 0;
    
BEGIN

    -- Log the Crest Court, procedure name, Jira ticket and the Xhibit table
    insert_dm_log ( p_crest_court_id     =>  p_crest_court_id 
                  , p_action_name        =>  'merge_crest_cases_into_xhibit- CTX-2174'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            =>  'XHB_CASE - Starting merging into XHIBIT cases that exist in CREST but not yet in XHIBIT'
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  =>  NULL
                  , p_created_by         =>  'DATA MIGRATION'              
                  , p_last_updated_by    =>  'DATA MIGRATION'        
                  );

    DBMS_OUTPUT.PUT_LINE('START Mreging Cases that exist in CREST Court '||p_crest_court_id||' but not yet in XHIBIT');
    DBMS_OUTPUT.PUT_LINE('So these Cases will be inserted as new rows in XHB_CASE table in XHIBIT');
    
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
    
    /* This MERGE statement will find the Cases that are in CREST (XHBSTG_CASE_DM) but not yet in XHIBIT (XHB_CASE) and then Insert them as new rows
       in XHB_CASE table. For this MERGE, when creating rows only the columns identified in the Data Migration Functional Spec will be
       popuplated so that it will trigger the existing Broker map process to load associated case level data.
       When this procedure is called to perform the MERGE, any exceptions will be put into the table err$_xhbstg_case_dm and the MERGE will run to the end.
       That is, an exception should not stop it from carrying on to the end, since they will be logged. So at the end, to find any errors after merging say for
       Crest Court Id 453 you can query the table like this:
*               SELECT ora_err_number$
*                    , ora_err_mesg$
*               FROM   err$_xhbstg_case_dm
*               WHERE  ora_err_tag$ = 'MERGE_453';
    */
    MERGE INTO xhibit.xhb_case xc
    USING (SELECT xcd.case_no
                , xcd.case_type
                , CASE 
                     WHEN xcd.case_type = 'A' AND xcd.appeal_type IS NOT NULL THEN xcd.appeal_type
                     WHEN xcd.case_type = 'A' AND xcd.appeal_type IS NULL THEN 'C'
                     ELSE NULL
                  END app_typ
                , (SELECT xrc.ref_court_id
                   FROM xhibit.xhb_ref_court xrc
                   WHERE xrc.crest_code        = xcd.psd_ct_code
                   AND   xrc.court_id          = v_xhibit_court_id
                   AND rownum = 1 -- CTX-4409
                   --AND   NVL(xrc.obs_ind,'N') != 'Y' -- CTX-4409
                  ) rf_crt
                , xcd.psd_reference
                , xcd.date_input -- CTX-3821
           FROM  xhbstg_case_dm xcd
           WHERE xcd.crest_court_id        = p_crest_court_id
           AND   xcd.xhibit_etl_status    IS NULL   -- we are merging rows that are untouched
           AND   xcd.xhibit_enrich_date   IS NULL
          ) t
    ON (     xc.court_id     = v_xhibit_court_id
         AND t.case_no       = xc.case_number
         AND t.case_type     = xc.case_type
       )
    WHEN NOT MATCHED THEN
    INSERT ( case_id
           , case_number
           , case_type
           , case_sub_type
           , ref_court_id
           , court_id
           , charge_import_indicator
           , magistrates_case_ref
           , case_listed
           , created_by
           , last_updated_by
           , last_update_date
           , creation_date
           )
    VALUES ( xhibit.xhb_case_seq.nextval + update_xhbstg_case_dm( TO_CHAR(p_crest_court_id)
                                                                          , t.case_no
                                                                          , t.case_type
                                                                          , v_xhibit_court_id
                                                                          )  
           , t.case_no
           , t.case_type
           , t.app_typ
           , t.rf_crt
           , v_xhibit_court_id
           , 'R'
           , t.psd_reference
           , 'N'
           , 'DATA MIGRATION'
           , 'DATA MIGRATION'
           , SYSDATE
           , t.date_input -- CTX-3821
           )
    LOG ERRORS INTO err$_xhbstg_case_dm('MERGE_'||p_crest_court_id) REJECT LIMIT UNLIMITED;

    DBMS_OUTPUT.PUT_LINE('   Total number of Cases found and inserted into XHB_CASE = '||SQL%ROWCOUNT);    

    -- Log the number of Cases merged into XHB_CASE
    insert_dm_log ( p_crest_court_id     =>  p_crest_court_id 
                  , p_action_name        =>  'merge_crest_cases_into_xhibit- CTX-2174'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            =>  'XHB_CASE - FINISHED. Total number of Cases found and inserted into XHB_CASE = '||SQL%ROWCOUNT
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  =>  SQL%ROWCOUNT
                  , p_created_by         =>  'DATA MIGRATION'              
                  , p_last_updated_by    =>  'DATA MIGRATION'        
                  );
                  
    SELECT COUNT(*)
    INTO  v_count_crest_merged_rows
    FROM  xhbstg_case_dm xcd
    WHERE xcd.crest_court_id = p_crest_court_id
    AND   xcd.xhibit_etl_status = 'M';
    
    DBMS_OUTPUT.PUT_LINE('The number of rows updated to ETL status of M in XHBSTG_CASE_DM after Merge = '||v_count_crest_merged_rows);
    DBMS_OUTPUT.PUT_LINE('NOTE: if both numbers are equal then it shows the number of rows that were merged successfully. Now check the Log table err$_xhbstg_case_dm for any failures.');
    DBMS_OUTPUT.PUT_LINE('FINISHED Finding and inserting Cases that exist in CREST but not yet in XHIBIT');
                  
    COMMIT; -- Commit when running for real    
    
--ROLLBACK;   ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION 
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('!!!!! ERROR HAS OCCURRED IN merge_crest_cases_into_xhibit: '||SUBSTR(SQLERRM,1,150));
        DBMS_OUTPUT.PUT_LINE('PLEASE check the table ERR$XHBSTG_CASE_DM for more details');

        -- Log the error with details and the Oracle Exception
        insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                      , p_action_name        => 'merge_crest_cases_into_xhibit- CTX-2174'
                      , p_run_time           =>  SYSDATE
                      , p_log_msg_type       =>  'E' -- Error
                      , p_log_msg            => 'XHB_CASE - ERROR HAS OCCURRED IN merge_crest_cases_into_xhibit: '||SUBSTR(SQLERRM,1,150)
                      , p_err_row_count      =>  NULL
                      , p_success_row_count  =>  NULL
                      , p_created_by         => 'DATA MIGRATION'              
                      , p_last_updated_by    => 'DATA MIGRATION'        
                      );

     COMMIT; -- Commit when running for real
    
END merge_crest_cases_into_xhibit;


/**
  * NAME       : get_court_id_receiving_site
  * DESCRIPTION: Function to return the XHIBIT COURT_ID_RECEIVING_SITE for the CREST Court ID and CTL ID passed in
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
  *              p_ctl_id           - The CTL_ID in the XHBSTG_CASE_DM table
**/
FUNCTION get_court_id_receiving_site( p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE
                                    , p_ctl_id         IN xhbstg_case_dm.ctl_id%TYPE) 
RETURN NUMBER
AS

    v_court_site_id    xhibit.xhb_court_site.court_site_id%TYPE;
    v_site_code        xhbstg_courtroom_location_dm.site_code%TYPE;
    
BEGIN

    IF p_ctl_id IS NULL THEN
    
        v_site_code := 'A';
        
    ELSE
    
        BEGIN
        
            SELECT xcrl.site_code
            INTO  v_site_code
            FROM  xhbstg_courtroom_location_dm xcrl
            WHERE xcrl.crest_court_id = p_crest_court_id
            AND   xcrl.ctl_id          = p_ctl_id;
            
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                DBMS_OUTPUT.PUT_LINE('!! THE CTL_ID: '||p_ctl_id||' PASSED IN DID NOT MATCH ANY CREST COURTROOM LOCATION SITE CODE');
           WHEN TOO_MANY_ROWS THEN
                DBMS_OUTPUT.PUT_LINE('!! THE CTL_ID: '||p_ctl_id||' PASSED IN MATCHED MORE THAN 1 CREST COURTROOM LOCATION SITE CODE');
        END;
        
    END IF;
    
    BEGIN
    
        SELECT xcs.court_site_id
        INTO   v_court_site_id
        FROM   xhibit.xhb_court_site xcs
        WHERE  xcs.court_site_code   = v_site_code
        AND    xcs.court_id          = xhb_data_migration_process_pkg.get_xhb_court_from_crest_court(p_crest_court_id)
        AND    NVL(xcs.obs_ind,'N') != 'Y';
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('!! THE CREST COURTROOM LOCATION SITE CODE: '||v_site_code||' DID NOT MATCH AN XHIBIT COURT SITE ID');
       WHEN TOO_MANY_ROWS THEN
            DBMS_OUTPUT.PUT_LINE('!! THE CREST COURTROOM LOCATION SITE CODE: '||v_site_code||' MATCHED MORE THAN 1 XHIBIT COURT SITE ID');
    END;

    RETURN v_court_site_id;
    
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN get_court_id_receiving_site: '||SUBSTR(SQLERRM,1,150));

END get_court_id_receiving_site;


/**
  * NAME       : update_xhb_case_with_crest
  * DESCRIPTION: CTX-2178, 2478, 2584, 2862, 2983: Procedure to be called to update existing rows in XHB_CASE with data from CREST but not yet put in XHIBIT 
  *              So this will be done after the MERGE with CREST data and the Broker process has been run. Then we call this 
  *              procedure to do this update for the rest of the new columns in XHB_CASE that have not yet been populated with CREST data
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
  *              
  * S Sethuraman 10/12/2018 - CTX-3252 - Exception Handling block implemented within loop to continue processing byjust logging errors for failures
  * 06/03/2019     S Sethuraman  - CTX-3821 - XHB_CASE.CREATION_DATE updated to CASE.DATE_INPUT  in UPDATE_XHB_cASE_WITH_CREST and MERGE_CREST_CASES_INTO_XHIBIT
  * 27/03/2019   S Sethuraman - CTX-3895 - XHB_CASE.MAG_CONVICTION_DATE updated for case_type = 'S'
  * 16/07/2019     S Sethuraman       - CTX-4409 - Remove OBS_IND check for REF_COURT_ID mapping  
**/
PROCEDURE update_xhb_case_with_crest(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;

    TYPE xhb_case_rec IS RECORD
    (
      case_id                      xhibit.xhb_case.case_id%TYPE
    , case_number                  xhibit.xhb_case.case_number%TYPE
    , case_type                    xhibit.xhb_case.case_type%TYPE
    , transferred_case             xhibit.xhb_case.transferred_case%TYPE
    , date_trans_to                xhibit.xhb_case.date_trans_to%TYPE
    , case_listed                  xhibit.xhb_case.case_listed%TYPE
    , monitoring_category_id       xhibit.xhb_case.monitoring_category_id%TYPE
    , appeal_lodged_date           xhibit.xhb_case.appeal_lodged_date%TYPE
    , received_date                xhibit.xhb_case.received_date%TYPE
    , either_way_type              xhibit.xhb_case.either_way_type%TYPE
    , ticket_required              xhibit.xhb_case.ticket_required%TYPE
    , ticket_type_code             xhibit.xhb_case.ticket_type_code%TYPE
    , court_id_receiving_site      xhibit.xhb_case.court_id_receiving_site%TYPE
    , committal_date               xhibit.xhb_case.committal_date%TYPE
    , sent_for_trial_date          xhibit.xhb_case.sent_for_trial_date%TYPE
    , no_defendants_for_case       xhibit.xhb_case.no_defendants_for_case%TYPE
    , secure_court                 xhibit.xhb_case.secure_court%TYPE
    , preliminary_date_of_hearing  xhibit.xhb_case.preliminary_date_of_hearing%TYPE
    , original_jps_1               xhibit.xhb_case.original_jps_1%TYPE
    , original_jps_2               xhibit.xhb_case.original_jps_2%TYPE
    , original_jps_3               xhibit.xhb_case.original_jps_3%TYPE
    , original_jps_4               xhibit.xhb_case.original_jps_4%TYPE
    , police_force_code            xhibit.xhb_case.police_force_code%TYPE
    , magcourt_hearingtype_ref_id  xhibit.xhb_case.magcourt_hearingtype_ref_id%TYPE
    , orig_body_decision_date      xhibit.xhb_case.orig_body_decision_date%TYPE
    , case_group_number            xhibit.xhb_case.case_group_number%TYPE
    , video_link_required          xhibit.xhb_case.video_link_required%TYPE
    , default_hearing_type         xhibit.xhb_case.default_hearing_type%TYPE
    , pub_running_list_id          xhibit.xhb_case.pub_running_list_id%TYPE
    , date_ctl_reminder_printed    xhibit.xhb_case.date_ctl_reminder_printed%TYPE
    , date_trans_recorded_to       xhibit.xhb_case.date_trans_recorded_to%TYPE
    , case_title                   xhibit.xhb_case.case_title%TYPE                  -- CTX2862 on 13/10/2018
    , ccc_trans_to_ref_court_id1   xhibit.xhb_case.ccc_trans_to_ref_court_id%TYPE    -- CTX2862 on 13/10/2018
    , ccc_trans_to_ref_court_id2   xhibit.xhb_case.ccc_trans_to_ref_court_id%TYPE    -- CTX2862 on 13/10/2018
    , mag_conviction_date          xhibit.xhb_case.mag_conviction_date%TYPE
    , ccc_trans_from_ref_court_id  xhibit.xhb_case.ccc_trans_from_ref_court_id%TYPE  -- CTX-3210
    , date_trans_from              xhibit.xhb_case.date_trans_from%TYPE              -- CTX-3210
    , date_input                   xhbstg_case_dm.date_input%TYPE              -- CTX-3821
    );

    TYPE xhb_case_type IS TABLE OF xhb_case_rec;
    xhb_case_tt  xhb_case_type;

    -- Cursor to fetch data to be used to update the XHIBIT.XHB_CASE table
    CURSOR cur_crest_case_details IS  
    SELECT xc.case_id
         , xcd.case_no
         , xcd.case_type
         , NVL2(xcd.ccc_trans_from,'Y',NULL)
         , xcd.date_trans_to
         , DECODE(xc.case_listed,'N','N','Y')
         , (SELECT xrmc.ref_monitoring_category_id
            FROM   xhibit.xhb_ref_monitoring_category xrmc
            WHERE  xrmc.monitoring_category_code = xcd.crime_monitoring_category
           )
         , DECODE(xcd.case_type,'A',xcd.comm_date,NULL)
         , xcd.date_received
         , xcd.either_way_type
         , xcd.ticket_reqd
         , (SELECT xrs.ref_system_code_id
            FROM   xhibit.xhb_ref_system_code xrs
            WHERE  xrs.code_type = 'JUDGE_TICKET'
            AND    xrs.code      = xcd.ticket_type
            AND    xrs.court_id  = v_xhibit_court_id
            AND    NVL(xrs.obs_ind,'N') != 'Y'
           )
         , get_court_id_receiving_site( p_crest_court_id
                                      , xcd.ctl_id
                                      )
         , xcd.comm_date
         , xcd.sent_for_trial_date
         , xcd.total_defts
         , xcd.security_ind
         , xcd.plea_direction_date
         , xcd.orig_jp1_name
         , xcd.orig_jp2_name
         , xcd.orig_jp3_name
         , xcd.orig_jp4_name
         , (SELECT xrsc.ref_system_code_id
            FROM  xhibit.xhb_ref_system_code xrsc
            WHERE xrsc.code_type         = 'HO_POL_FORCE'
            AND   xrsc.code              = xcd.ho_pol_force
            AND   xrsc.court_id          = v_xhibit_court_id
            AND   NVL(xrsc.obs_ind,'N') != 'Y'
           )
         , (SELECT xrsc.ref_system_code_id
            FROM  xhibit.xhb_ref_system_code xrsc
            WHERE xrsc.code_type         = 'HO_PSD_HRG_TYPE'
            AND   xrsc.code              = xcd.ho_psd_hrg_type
            AND   xrsc.court_id          = v_xhibit_court_id
            AND   NVL(xrsc.obs_ind,'N') != 'Y'
           )
         , xcd.lc_conv_date
         , xcd.case_group
         , xcd.video_ind
         , (SELECT xrht.ref_hearing_type_id
            FROM  xhibit.xhb_ref_hearing_type xrht
            WHERE xrht.hearing_type_code = xcd.init_hrg_type
            AND   xrht.category          = 'X'
            AND   xrht.court_id          = v_xhibit_court_id
            AND   NVL(xrht.obs_ind,'N') != 'Y'   
           )
         , (CASE xcd.prlis_job_id  
              WHEN NULL THEN NULL
              ELSE (SELECT xprl.pub_running_list_id
                    FROM   xhibit.xhb_pub_running_list xprl
                    WHERE  xprl.court_id = v_xhibit_court_id
                    AND    xprl.obs_ind = 'Y'  -- this will stop it from showing up in the XHIBIT running screen
                   )
           END 
           )
          , xcd.date_ctl_reminder_printed
          , xcd.date_trans_to_recorded
          , NVL2(xc.case_title, xc.case_title, xcd.case_title)  -- CTX2862 on 13/10/2018
          , (SELECT xrc.ref_court_id                             -- CTX2862 on 13/10/2018
             FROM  xhibit.xhb_ref_court xrc
             WHERE xrc.crest_code        = xcd.ccc_trans_to
             AND   xrc.court_id          = v_xhibit_court_id
             AND rownum = 1 -- CTX-4409             
           --  AND   NVL(xrc.obs_ind,'N') != 'Y'   -- CTX-4409                 
            )
          , TO_NUMBER(xcd.ccc_trans_to)                         -- CTX2862 on 13/10/2018
          , (CASE
               WHEN xcd.case_type = 'A' AND xc.case_sub_type != 'O' THEN xcd.lc_conv_date
               WHEN xcd.case_type = 'S' THEN xcd.comm_date -- CTX-3895
               ELSE NULL
            END
            )
          , (SELECT xrc.ref_court_id                             -- CTX-3210
             FROM  xhibit.xhb_ref_court xrc
             WHERE xrc.crest_code        = xcd.ccc_trans_from
             AND   xrc.court_id          = v_xhibit_court_id
             AND rownum = 1 -- CTX-4409
           --  AND   NVL(xrc.obs_ind,'N') != 'Y'       -- CTX-4409            
            )
          , xcd.date_trans_from                                   -- CTX-3210
          , xcd.date_input -- CTX-3821
    FROM  xhbstg_case_dm   xcd
        , xhibit.xhb_case  xc             
    WHERE xcd.crest_court_id               =  p_crest_court_id   
    AND   xcd.case_no                      =  xc.case_number 
    AND   xcd.case_type                    =  xc.case_type
    AND   NVL(xcd.xhibit_etl_status,'N')   <> 'U'
    AND   xcd.xhibit_enrich_date           IS  NULL
    AND   xc.court_id                      =  v_xhibit_court_id;
     
    v_count_number_of_rows         NUMBER := 0;
    v_case_no                      xhbstg_case_dm.case_no%TYPE;
    v_case_type                    xhbstg_case_dm.case_type%TYPE;
    v_case_id                      xhibit.xhb_case.case_id%TYPE;
    v_err_message                  xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_count_crest_merged_rows      NUMBER := 0;
    v_xcd_upd_rows                 NUMBER := 0;
    v_etl_status                   VARCHAR2(1);
    v_count_xcd_not_processed_row  NUMBER := 0;
    
BEGIN

    -- Log the Crest Court, procedure name, Jira ticket and the Xhibit table
    insert_dm_log ( p_crest_court_id     =>  p_crest_court_id 
                  , p_action_name        => 'update_xhb_case_with_crest- CTX-2178'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            =>  'XHB_CASE - STARTING UPDATING existing rows in XHB_CASE with new columns with their required data from CREST'
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  =>  NULL
                  , p_created_by         =>  'DATA MIGRATION'              
                  , p_last_updated_by    =>  'DATA MIGRATION'        
                  );

    DBMS_OUTPUT.PUT_LINE('START UPDATING existing rows in XHB_CASE with new columns with their required data from CREST');

    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id); 

    -- Here we are going to update the rest of the columns in the rows in XHB_CASE table that have not yet been populated with CREST data
    OPEN cur_crest_case_details;
    LOOP
    FETCH cur_crest_case_details BULK COLLECT INTO xhb_case_tt LIMIT g_bulk_limit;
 
    IF xhb_case_tt IS NOT NULL AND xhb_case_tt.COUNT > 0 THEN  -- prevents numeric or value error later if nothing in the array
    
        FOR i IN xhb_case_tt.FIRST .. xhb_case_tt.LAST LOOP        
          BEGIN -- CTX-3252 
           
           -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_case_no   := xhb_case_tt(i).case_number;
            v_case_type := xhb_case_tt(i).case_type;
            v_case_id   := xhb_case_tt(i).case_id;
            
            -- Update the rows in XHB_CASE with data from CREST
            UPDATE  xhibit.xhb_case xc
            SET     xc.transferred_case             = xhb_case_tt(i).transferred_case           
                  , xc.date_trans_to                = xhb_case_tt(i).date_trans_to           
                  , xc.case_listed                  = xhb_case_tt(i).case_listed               
                  , xc.monitoring_category_id       = xhb_case_tt(i).monitoring_category_id        
                  , xc.appeal_lodged_date           = xhb_case_tt(i).appeal_lodged_date     
                  , xc.received_date                = xhb_case_tt(i).received_date          
                  , xc.either_way_type              = xhb_case_tt(i).either_way_type     
                  , xc.ticket_required              = xhb_case_tt(i).ticket_required        
                  , xc.ticket_type_code             = xhb_case_tt(i).ticket_type_code   
                  , xc.court_id_receiving_site      = xhb_case_tt(i).court_id_receiving_site   
                  , xc.committal_date                = xhb_case_tt(i).committal_date     
                  , xc.sent_for_trial_date          = xhb_case_tt(i).sent_for_trial_date  
                  , xc.no_defendants_for_case       = xhb_case_tt(i).no_defendants_for_case 
                  , xc.secure_court                 = xhb_case_tt(i).secure_court
                  , xc.preliminary_date_of_hearing  = xhb_case_tt(i).preliminary_date_of_hearing
                  , xc.original_jps_1               = xhb_case_tt(i).original_jps_1
                  , xc.original_jps_2               = xhb_case_tt(i).original_jps_2
                  , xc.original_jps_3               = xhb_case_tt(i).original_jps_3
                  , xc.original_jps_4               = xhb_case_tt(i).original_jps_4
                  , xc.police_force_code            = xhb_case_tt(i).police_force_code
                  , xc.magcourt_hearingtype_ref_id  = xhb_case_tt(i).magcourt_hearingtype_ref_id
                  , xc.orig_body_decision_date      = xhb_case_tt(i).orig_body_decision_date
                  , xc.case_group_number            = xhb_case_tt(i).case_group_number
                  , xc.video_link_required          = NVL(xhb_case_tt(i).video_link_required,'N') --Not null, has table DEFAULT of N so set here 
                  , xc.default_hearing_type         = xhb_case_tt(i).default_hearing_type
                  , xc.pub_running_list_id          = xhb_case_tt(i).pub_running_list_id
                  , xc.date_ctl_reminder_printed    = xhb_case_tt(i).date_ctl_reminder_printed
                  , xc.date_trans_recorded_to       = xhb_case_tt(i).date_trans_recorded_to
                  , xc.case_title                   = xhb_case_tt(i).case_title      -- CTX2862 on 13/10/2018 and the nect one below
                  , xc.ccc_trans_to_ref_court_id    = NVL2(xhb_case_tt(i).ccc_trans_to_ref_court_id1, xhb_case_tt(i).ccc_trans_to_ref_court_id1, xhb_case_tt(i).ccc_trans_to_ref_court_id2)
                  , xc.mag_conviction_date          = xhb_case_tt(i).mag_conviction_date
                  , xc.ccc_trans_from_ref_court_id  = xhb_case_tt(i).ccc_trans_from_ref_court_id  -- CTX-3210
                  , xc.date_trans_from              = xhb_case_tt(i).date_trans_from              -- CTX-3210
                  , xc.last_updated_by              = 'DATA MIGRATION'
                  , xc.last_update_date             = SYSDATE
                  , xc.creation_date     = xhb_case_tt(i).date_input -- CTX-3821
            WHERE   xc.case_id           =  v_case_id;
            
            v_xcd_upd_rows := SQL%ROWCOUNT;
            
            -- Log the fact that we have processed the row fetched for update
            insert_dm_log ( p_crest_court_id     =>  p_crest_court_id 
                          , p_action_name        =>  'update_xhb_case_with_crest- CTX-2178'
                          , p_run_time           =>  SYSDATE
                          , p_log_msg_type       =>  'I' -- Information
                          , p_log_msg            =>  'XHB_CASE - updated case_id - '||v_case_id||' case_number = '||v_case_no||' case_type = '||v_case_type
                          , p_err_row_count      =>   NULL
                          , p_success_row_count  =>  1
                          , p_created_by         =>  'DATA MIGRATION'              
                          , p_last_updated_by    =>  'DATA MIGRATION'        
                          );
                          
            -- Update XHBSTG_CASE_DM table for the rows processed. Set etl_status to U if the row was Updated, however, if the row was
            -- not updated but it is a merged row then leave the status as Merged else set as Not processd      
            UPDATE xhbstg_case_dm xcd
            SET    xcd.xhibit_etl_date      =  NVL(xcd.xhibit_etl_date, SYSDATE) -- The rows that were Merged will have a date in them so preserve that Merged date
                 , xcd.xhibit_etl_status    =  CASE
                                                  WHEN  v_xcd_upd_rows  > 0 THEN 'U'
                                                  WHEN  xhibit_etl_status IS NOT NULL AND xhibit_etl_status = 'M' THEN 'M'
                                                  ELSE 'N'
                                               END 
                 , xcd.xhibit_enrich_date   =  SYSDATE
                 , xcd.xhibit_court_id      =  v_xhibit_court_id
            WHERE  xcd.case_no              =  xhb_case_tt(i).case_number
            AND    xcd.case_type            =  xhb_case_tt(i).case_type
            AND    xcd.crest_court_id       =  p_crest_court_id
            RETURNING xhibit_etl_status INTO v_etl_status;         

            DBMS_OUTPUT.PUT_LINE('CASE_ID= '||v_case_id||' CASE_NO= '||v_case_no||' CASE_TYPE= '||v_case_type||' ETL_STATUS = '||v_etl_status);
          EXCEPTION
              WHEN OTHERS THEN
                      v_err_message := SUBSTR(SQLERRM,1,500);
        
               --ROLLBACK; -- CTX-3252  Don't ROLLBACK -  continue processing with logging the error
               DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_case_with_crest: '||SUBSTR(v_err_message,1,150));
               DBMS_OUTPUT.PUT_LINE('The number of rows updated and committed in XHB_CASE before Error = '||v_count_number_of_rows);
        
              -- If the failure occurred during Update then update the row in the CREST table that caused the failure
              UPDATE xhbstg_case_dm xcd
              SET    xcd.xhibit_etl_err_message  = v_err_message
                   , xcd.xhibit_etl_status       =  'X'   -- Error
                   , xcd.xhibit_enrich_date      =  SYSDATE
                   , xcd.xhibit_court_id         = v_xhibit_court_id
              WHERE  xcd.case_no                 =  v_case_no
                AND    xcd.case_type               =  v_case_type
                AND    xcd.crest_court_id          =  p_crest_court_id; 
        
              -- Log the error with the failing row details and the Oracle Exception
              insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                         , p_action_name        => 'update_xhb_case_with_crest- CTX-2178'
                         , p_run_time           =>  SYSDATE
                         , p_log_msg_type       =>  'E' -- Error
                         , p_log_msg            => 'XHB_CASE - ERROR processing case_id = '||v_case_id||' case_no = '||v_case_no||' case_type = '||v_case_type||' ERROR: '||SUBSTR(v_err_message,1,200)
                         , p_err_row_count      =>  1
                         , p_success_row_count  =>  NULL
                         , p_created_by         => 'DATA MIGRATION'              
                         , p_last_updated_by    => 'DATA MIGRATION'        
                        );
        
              END; -- CTX-3252
        END LOOP;
        
        COMMIT; ---Commit after every successful processing BULK COLLECT of rows 
        v_count_number_of_rows := v_count_number_of_rows + xhb_case_tt.COUNT;
        
    END IF;
    
    EXIT WHEN cur_crest_case_details%NOTFOUND;
    
    END LOOP;
    CLOSE cur_crest_case_details;

    DBMS_OUTPUT.PUT_LINE('The number of rows updated in XHB_CASE = '||v_count_number_of_rows);
    
    SELECT COUNT(*)
    INTO  v_count_crest_merged_rows
    FROM  xhbstg_case_dm xcd
    WHERE xcd.crest_court_id = p_crest_court_id
    AND   xcd.xhibit_etl_status = 'U';
    
    DBMS_OUTPUT.PUT_LINE('The number of rows updated to ETL status of U in XHBSTG_CASE_DM after Merge = '||v_count_crest_merged_rows);
    DBMS_OUTPUT.PUT_LINE('NOTE: if both numbers are equal then it shows the number of rows that were updated successfully. Now check for any errors in the output file');

    -- Log the total number of rows processed for the update
    insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                  , p_action_name        => 'update_xhb_case_with_crest- CTX-2178'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            => 'XHB_CASE - The number of rows updated in XHB_CASE = '||v_count_number_of_rows
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  => v_count_number_of_rows
                  , p_created_by         => 'DATA MIGRATION'              
                  , p_last_updated_by    => 'DATA MIGRATION'        
                  ); 
                  
    -- Update XHBSTG_CASE table for the rows NOT processed
    UPDATE xhbstg_case_dm xcd
    SET    xcd.xhibit_etl_date      =  SYSDATE
         , xcd.xhibit_etl_status    =  'N'
         , xcd.xhibit_enrich_date   =  SYSDATE
         , xcd.xhibit_court_id      =  v_xhibit_court_id
    WHERE  xcd.crest_court_id       =  p_crest_court_id
    AND  xcd.xhibit_etl_status     IS NULL 
    AND  xcd.xhibit_enrich_date    IS NULL
    AND  xcd.xhibit_etl_date       IS NULL
    AND  xcd.xhibit_court_id       IS NULL; 
   
    v_count_xcd_not_processed_row := SQL%ROWCOUNT;
   
    -- Log the update of XHBSTG_CASE table for the rows NOT processed
    insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                  , p_action_name        => 'update_xhb_case_with_crest- CTX-2178'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            => 'XHB_CASE - finished. In XHBSTG_CASE updated '||v_count_xcd_not_processed_row||' rows to ETL_STATUS = N as rows NOT updated for CREST Court id = '||p_crest_court_id
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  => v_count_xcd_not_processed_row
                  , p_created_by         => 'DATA MIGRATION'              
                  , p_last_updated_by    => 'DATA MIGRATION'        
                  ); 
                  
    DBMS_OUTPUT.PUT_LINE('FINISHED UPDATING existing rows in XHB_CASE with new columns with their required data from CREST');

    COMMIT;  --- Uncomment when running for real  

--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE
    
EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SUBSTR(SQLERRM,1,500);
        IF cur_crest_case_details%ISOPEN THEN
            CLOSE cur_crest_case_details;
        END IF;
        
        --ROLLBACK; -- CTX-3252 - log the error message and commit;
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_case_with_crest: '||SUBSTR(v_err_message,1,150));
        DBMS_OUTPUT.PUT_LINE('The number of rows updated and committed in XHB_CASE before Error = '||v_count_number_of_rows);
        
        -- Log the error with the failing row details and the Oracle Exception
        insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                      , p_action_name        => 'update_xhb_case_with_crest- CTX-2178'
                      , p_run_time           =>  SYSDATE
                      , p_log_msg_type       =>  'E' -- Error
                      , p_log_msg            => 'XHB_CASE - ERROR processing case_id = '||v_case_id||' case_no = '||v_case_no||' case_type = '||v_case_type||' ERROR: '||SUBSTR(v_err_message,1,200)
                      , p_err_row_count      =>  1
                      , p_success_row_count  =>  NULL
                      , p_created_by         => 'DATA MIGRATION'              
                      , p_last_updated_by    => 'DATA MIGRATION'        
                      );
        
        COMMIT;  --- Uncomment when running for real
--ROLLBACK;   ---???????????????????????????????? FOR TESTING ONLY SO REMOVE         
        
END update_xhb_case_with_crest;


/**
  * NAME       : update_xhb_cpa_with_crest [DM.009]
  * DESCRIPTION: CTX-2183: Procedure to be called to update existing rows in XHB_CASE_PROSECUTOR_AGENCY with data from CREST but not yet put in XHIBIT 
  *              CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE update_xhb_cpa_with_crest(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
    
    TYPE xhb_cpa_rec IS RECORD
    ( 
      opposer_type         xhbstg_case_opposer_dm.opposer_type%TYPE
    , opp_id               xhbstg_case_opposer_dm.opp_id%TYPE
    , case_no              xhbstg_case_opposer_dm.case_no%TYPE
    , case_type            xhbstg_case_opposer_dm.case_type%TYPE
    , case_pros_agency_id  xhibit.xhb_case_prosecutor_agency.case_pros_agency_id%TYPE
    );
    
    TYPE xhb_cpa_type IS TABLE OF xhb_cpa_rec;
    xhb_cpa_tt  xhb_cpa_type;

    CURSOR cur_c_p_a_details IS
    SELECT xcod.opposer_type 
         , xcod.opp_id
         , xcod.case_no
         , xcod.case_type
         , xcpa.case_pros_agency_id        
    FROM   xhbstg_case_opposer_dm             xcod
         , xhibit.xhb_ref_prosecutor_agency   xrpa
         , xhibit.xhb_case_prosecutor_agency  xcpa
         , xhibit.xhb_case                    xc
    WHERE xcod.crest_court_id               = p_crest_court_id
    AND   NVL(xcod.xhibit_etl_status,'N')   <> 'U'
    AND   xcod.xhibit_enrich_date           IS NULL   
    AND   xcod.case_no                      = xc.case_number 
    AND   xcod.case_type                    = xc.case_type  
    AND   xc.court_id                       = v_xhibit_court_id
    AND   xc.case_id                        = xcpa.case_id
    AND   xcpa.ref_prosecutor_agency_id     = xrpa.ref_prosecutor_agency_id
    AND   NVL(xcpa.obs_ind,'N')            != 'Y'
    AND   TO_NUMBER(xrpa.crest_opposer_id)  = xcod.opp_id
    AND   xrpa.court_id                     = v_xhibit_court_id
    AND   NVL(xrpa.obs_ind,'N')            != 'Y';

    v_case_pros_agency_id           xhibit.xhb_case_prosecutor_agency.case_pros_agency_id%TYPE;
    v_opp_id                        xhbstg_case_opposer_dm.opp_id%TYPE;
    v_case_no                       xhbstg_case_opposer_dm.case_no%TYPE;
    v_case_type                     xhbstg_case_opposer_dm.case_type%TYPE;
    v_count_number_of_rows          NUMBER := 0;
    v_xcpa_upd_rows                 NUMBER := 0;
    v_xcpa_upd_status               VARCHAR2(1) := 'N';
    v_err_message                   xhbstg_case_opposer_dm.xhibit_etl_err_message%TYPE;
    v_count_xcod_updated_rows       NUMBER := 0;
    v_count_xcod_not_processed_row  NUMBER := 0;
    
BEGIN

    -- Log the Crest Court, procedure name, Jira ticket and the Xhibit table
    insert_dm_log ( p_crest_court_id     =>  p_crest_court_id 
                  , p_action_name        => 'upd_xhb_cpa_with_crest- CTX-2183'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            =>  'XHB_CASE_PROSECUTOR_AGENCY - Starting process of updating existing rows for new columns with the required data from CREST'
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  =>  NULL
                  , p_created_by         =>  'DATA MIGRATION'              
                  , p_last_updated_by    =>  'DATA MIGRATION'        
                  );

    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing rows in XHB_CASE_PROSECUTOR_AGENCY for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);    
    
    -- Here we are going to update the rest of the columns in the rows in XHB_CASE_PROSECUTOR_AGENCY table that have not yet been populated with CREST data
    OPEN cur_c_p_a_details;
    LOOP
    FETCH cur_c_p_a_details BULK COLLECT INTO xhb_cpa_tt LIMIT g_bulk_limit;
 
    IF xhb_cpa_tt IS NOT NULL AND xhb_cpa_tt.COUNT > 0 THEN -- prevents numeric or value error later if nothing in the array

        FOR i IN xhb_cpa_tt.FIRST .. xhb_cpa_tt.LAST LOOP        
        BEGIN -- CTX3252 - BEGIN BLOCK for handling exception for each record and to continue processing 
        
            -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_case_pros_agency_id  := xhb_cpa_tt(i).case_pros_agency_id;
            v_opp_id               := xhb_cpa_tt(i).opp_id;
            v_case_no              := xhb_cpa_tt(i).case_no;
            v_case_type            := xhb_cpa_tt(i).case_type;        
        
            -- We are updating based on primary key at a time so only up to 1 row will be update at a time
            UPDATE xhibit.xhb_case_prosecutor_agency xcpa
            SET    xcpa.respondent_status    = xhb_cpa_tt(i).opposer_type
            WHERE  xcpa.case_pros_agency_id  = v_case_pros_agency_id
            AND    NVL(xcpa.obs_ind,'N')    != 'Y' ;           
             
            v_xcpa_upd_rows := SQL%ROWCOUNT;
            IF v_xcpa_upd_rows > 0 THEN 
               v_xcpa_upd_status := 'U'; -- XHIBIT TABLE updated
            ELSE
               v_xcpa_upd_status := 'N'; -- No Action Performed on XHIBIT table
            END IF;  
            
            -- Log the fact that we have processed the row fetched for update
            insert_dm_log ( p_crest_court_id     =>  p_crest_court_id 
                          , p_action_name        =>  'upd_xhb_cpa_with_crest- CTX-2183'
                          , p_run_time           =>  SYSDATE
                          , p_log_msg_type       =>  'I' -- Information
                          , p_log_msg            =>  'XHB_CASE_PROSECUTOR_AGENCY - updated case_pros_agency_id - '||v_case_pros_agency_id||' using CREST opp_id = '||xhb_cpa_tt(i).opp_id||' case_no = '||xhb_cpa_tt(i).case_no||' case_type = '||xhb_cpa_tt(i).case_type
                          , p_err_row_count      =>   NULL
                          , p_success_row_count  =>  v_xcpa_upd_rows
                          , p_created_by         =>  'DATA MIGRATION'              
                          , p_last_updated_by    =>  'DATA MIGRATION'        
                          );              
            
            -- Update XHBSTG_CASE_OPPOSER_DM table for the row processed
            UPDATE xhbstg_case_opposer_dm xcod
            SET    xcod.xhibit_etl_date      =  SYSDATE
                 , xcod.xhibit_court_id      =  v_xhibit_court_id                 
                 , xcod.xhibit_enrich_date   =  SYSDATE
                 , xcod.xhibit_etl_status    =  v_xcpa_upd_status                 
            WHERE  xcod.opp_id               =  v_opp_id
            AND    xcod.case_no              =  v_case_no
            AND    xcod.case_type            =  v_case_type
            AND    xcod.crest_court_id       =  p_crest_court_id; 

            DBMS_OUTPUT.PUT_LINE('Updated case_pros_agency_id = '||v_case_pros_agency_id||' using CREST opp_id = '||v_opp_id||' case_no = '||v_case_no||' case_type = '||v_case_type||' etl_status = '||v_xcpa_upd_status);
        
        EXCEPTION
          WHEN OTHERS THEN
              v_err_message := SUBSTR(SQLERRM,1,500);
              DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_cpa_with_crest: '||SUBSTR(v_err_message,1,150));
              DBMS_OUTPUT.PUT_LINE('The number of rows updated and committed in XHB_CASE_PROSECUTOR_AGENCY before Error = '||v_count_number_of_rows);
             
              -- If the failure occurred during Update then update the row in the CREST table that caused the failure
              UPDATE xhbstg_case_opposer_dm xcod
              SET    xcod.xhibit_etl_err_message  = v_err_message
                   , xcod.xhibit_etl_status       =  'X'   -- Error
                   , xcod.xhibit_enrich_date      =  SYSDATE
                   , xcod.xhibit_court_id         = v_xhibit_court_id
              WHERE  xcod.opp_id                  =  v_opp_id
              AND    xcod.case_no                 =  v_case_no
              AND    xcod.case_type               =  v_case_type
              AND    xcod.crest_court_id          =  p_crest_court_id;  
      
              -- Log the error with the failing row details and the Oracle Exception
              insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                            , p_action_name        => 'upd_xhb_cpa_with_crest- CTX-2183'
                            , p_run_time           =>  SYSDATE
                            , p_log_msg_type       =>  'E' -- Error
                            , p_log_msg            => 'XHB_CASE_PROSECUTOR_AGENCY - ERROR processing case_pros_agency_id = '||v_case_pros_agency_id||' opp_id = '||v_opp_id||' case_no = '||v_case_no||' case_type = '||v_case_type||' ERROR: '||SUBSTR(v_err_message,1,200)
                            , p_err_row_count      =>  1
                            , p_success_row_count  =>  NULL
                            , p_created_by         => 'DATA MIGRATION'              
                            , p_last_updated_by    => 'DATA MIGRATION'        
                            );
        
        END; -- CTX3252 - END BLOCK for handling exception for each record and to continue processing         
        END LOOP;  
        
      COMMIT; ---Commit after every successful processing BULK COLLECT of rows 
        v_count_number_of_rows := v_count_number_of_rows + xhb_cpa_tt.COUNT;
        
    END IF;
  
    EXIT WHEN cur_c_p_a_details%NOTFOUND;
    
    END LOOP;
    
    CLOSE cur_c_p_a_details;
    
    DBMS_OUTPUT.PUT_LINE('The number of rows updated in XHB_CASE_PROSECUTOR_AGENCY = '||v_count_number_of_rows);

    SELECT COUNT(*)
    INTO  v_count_xcod_updated_rows
    FROM  xhbstg_case_opposer_dm xcod
    WHERE xcod.crest_court_id = p_crest_court_id
    AND   xcod.xhibit_etl_status = 'U';
    
    DBMS_OUTPUT.PUT_LINE('The number of rows updated to ETL status of U in XHBSTG_CASE_OPPSER_DM = '||v_count_xcod_updated_rows);
    
    -- Log the total number of rows processed for the update
    insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                  , p_action_name        => 'upd_xhb_cpa_with_crest- CTX-2183'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            => 'XHB_CASE_PROSECUTOR_AGENCY - Processed '||v_count_number_of_rows||' successfully'
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  => v_count_number_of_rows
                  , p_created_by         => 'DATA MIGRATION'              
                  , p_last_updated_by    => 'DATA MIGRATION'        
                  ); 

    -- Update XHBSTG_CASE_OPPOSER_DM table for the rows NOT processed
    UPDATE xhbstg_case_opposer_dm xcod
    SET    xcod.xhibit_etl_date      =  SYSDATE
         , xcod.xhibit_etl_status    =  'N'
         , xcod.xhibit_enrich_date   =  SYSDATE
         , xcod.xhibit_court_id      = v_xhibit_court_id
    WHERE  xcod.crest_court_id       =  p_crest_court_id
    AND  xcod.xhibit_etl_status     IS NULL 
    AND  xcod.xhibit_enrich_date    IS NULL
    AND  xcod.xhibit_etl_date       IS NULL
    AND  xcod.xhibit_court_id       IS NULL;

    v_count_xcod_not_processed_row := SQL%ROWCOUNT;
    
    -- Log the update of XHBSTG_CASE_OPPOSER_DM table for the rows NOT processed
    insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                  , p_action_name        => 'upd_xhb_cpa_with_crest- CTX-2183'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            => 'XHB_CASE_PROSECUTOR_AGENCY - finished. In XHBSTG_CASE_OPPOSER_DM updated '||v_count_xcod_not_processed_row||' rows to ETL_STATUS = N as rows NOT updated for CREST Court id = '||p_crest_court_id
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  => v_count_xcod_not_processed_row
                  , p_created_by         => 'DATA MIGRATION'              
                  , p_last_updated_by    => 'DATA MIGRATION'        
                  );    

     DBMS_OUTPUT.PUT_LINE('The number of rows NOT processed and hence updated to ETL status of N in XHBSTG_CASE_OPPSER_DM = '||v_count_xcod_not_processed_row);
     
    COMMIT;  --- Uncomment when running for real  

--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE 

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SUBSTR(SQLERRM,1,500);
        IF cur_c_p_a_details%ISOPEN THEN
            CLOSE cur_c_p_a_details;
        END IF;
        
        --ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_cpa_with_crest: '||SUBSTR(v_err_message,1,150));
        DBMS_OUTPUT.PUT_LINE('The number of rows updated and committed in XHB_CASE_PROSECUTOR_AGENCY before Error = '||v_count_number_of_rows);
       
        -- Log the error with the failing row details and the Oracle Exception
        insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                      , p_action_name        => 'upd_xhb_cpa_with_crest- CTX-2183'
                      , p_run_time           =>  SYSDATE
                      , p_log_msg_type       =>  'E' -- Error
                      , p_log_msg            => 'XHB_CASE_PROSECUTOR_AGENCY - ERROR processing case_pros_agency_id = '||v_case_pros_agency_id||' opp_id = '||v_opp_id||' case_no = '||v_case_no||' case_type = '||v_case_type||' ERROR: '||SUBSTR(v_err_message,1,200)
                      , p_err_row_count      =>  1
                      , p_success_row_count  =>  NULL
                      , p_created_by         => 'DATA MIGRATION'              
                      , p_last_updated_by    => 'DATA MIGRATION'        
                      );
                      
        COMMIT;  --- Uncomment when running for real
--ROLLBACK;   ---???????????????????????????????? FOR TESTING ONLY SO REMOVE            
    
END update_xhb_cpa_with_crest;

/**
  * NAME       : update_xhb_court_with_crest
  * DESCRIPTION: CTX-2179. New CTX fields for XHB_COURT - Sec 4.3.2.2 req [4975.DM.005]
  *              Update existing rows in XHIBIT XHB_COURT table to add new data elements from CREST
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE update_xhb_court_with_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
    v_err_message VARCHAR2(2000);
  
    CURSOR cur_crest_home_court_details IS
    SELECT xhc.crest_court_id,
       (SELECT xrsc.ref_system_code_id
            FROM  xhibit.xhb_ref_system_code xrsc
            WHERE xrsc.code_type         = 'HO_POL_FORCE'
            AND   xrsc.code              = xhc.ho_pol_force_code
            AND   xrsc.court_id          = v_xhibit_court_id
            AND   NVL(xrsc.obs_ind,'N') != 'Y'
           ) ho_pol_force_code, 
       xhc.cr_fl_order ,
       to_char(to_date(xhc.court_start_time,'HH12:MI AM'),'HH24:MI')  as court_start_time ,
       wld.wl_rep_sort ,
       wld.wl_rep_period ,
       wld.wl_rep_time ,
       wld.wl_text1||wld.wl_text2||wld.wl_text3  warned_list_details ,
       xhc.tier,
       xhc.county_loc_code,
       xhc.dx_ref 
    FROM  xhbstg_home_court_dm   xhc,
          xhbstg_warned_list_details_dm wld
    WHERE xhc.crest_court_id = p_crest_court_id
      AND wld.crest_court_id = xhc.crest_court_id
      AND wld.cc_ind = 'CR';

    xhb_court_tt  cur_crest_home_court_details%ROWTYPE;
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing row in XHB_COURT for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_with_crest- CTX-2179'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_COURT - Starting process of updating existing rows for new columns with the required data from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
    OPEN cur_crest_home_court_details;
    
    FETCH cur_crest_home_court_details INTO xhb_court_tt;

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('UPDATING XHB_COURT - FOR CREST_COURT_ID = '||xhb_court_tt.crest_court_id);
    
            -- Update the rows in XHB_COURT with data from CREST
            UPDATE  xhibit.xhb_court xc
            SET     xc.police_force_code             = xhb_court_tt.ho_pol_force_code
                  , xc.fl_rep_sort                   = xhb_court_tt.cr_fl_order
                  , xc.court_start_time              = xhb_court_tt.court_start_time
                  , xc.wl_rep_sort                   = xhb_court_tt.wl_rep_sort
                  , xc.wl_rep_period                 = xhb_court_tt.wl_rep_period
                  , xc.wl_rep_time                   = xhb_court_tt.wl_rep_time
                  , xc.wl_free_text                  = xhb_court_tt.warned_list_details
                  , xc.tier                          = xhb_court_tt.tier
                  , xc.county_loc_code               = xhb_court_tt.county_loc_code
                  , xc.dx_ref                        = xhb_court_tt.dx_ref
                  , xc.last_update_date              = SYSDATE
                  , xc.last_updated_by               = 'DATA MIGRATION'
            WHERE  xc.crest_court_id  =  xhb_court_tt.crest_court_id
              AND  NVL(xc.obs_ind,'N')!='Y';

             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_with_crest- CTX-2179'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_COURT - 1 row Updated successfully!'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => SQL%ROWCOUNT
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
    

            -- Update XHBSTG_HOME_COURT_DM, XHBSTG_WARNED_LIST_DETAILS_DM tables for the rows processed
            UPDATE xhbstg_home_court_dm xhc
            SET    xhc.xhibit_etl_date   = SYSDATE
                  ,xhc.xhibit_court_id   = v_xhibit_court_id
                  ,xhc.xhibit_enrich_date = SYSDATE
                  ,xhc.xhibit_etl_status = 'U' --Updated
            WHERE  xhc.crest_court_id   =  p_crest_court_id;

            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_with_crest- CTX-2179'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHBSTG_HOME_COURT_DM - 1 row Updated successfully!'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => SQL%ROWCOUNT
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
            UPDATE xhbstg_warned_list_details_dm wld
            SET    wld.xhibit_etl_date   = SYSDATE
                  ,wld.xhibit_court_id   = v_xhibit_court_id
                  ,wld.xhibit_enrich_date = SYSDATE
                  ,wld.xhibit_etl_status = 'U' -- Updated
            WHERE  wld.crest_court_id   =  p_crest_court_id;

            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_with_crest- CTX-2179'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHBSTG_WARNED_LIST_DETAILS_DM - 1 row Updated successfully!'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => SQL%ROWCOUNT
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


      COMMIT; ----????????????????????????????????????
        

    CLOSE cur_crest_home_court_details;

    DBMS_OUTPUT.PUT_LINE('XHB_COURT Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_court_with_crest for CREST_COURT_ID : '||p_crest_court_id||' - '||SUBSTR(v_err_message,1,110));
         
         -- Update XHBSTG_HOME_COURT_DM, XHBSTG_WARNED_LIST_DETAILS_DM tables with error status/messages
         BEGIN
            UPDATE xhbstg_home_court_dm xhc
            SET    xhc.xhibit_etl_date   = SYSDATE
                  ,xhc.xhibit_court_id   = v_xhibit_court_id
                  ,xhc.xhibit_enrich_date = SYSDATE
                  ,xhc.xhibit_etl_status = 'X' -- Error
                  ,xhc.xhibit_etl_err_message = v_err_message
            WHERE  xhc.crest_court_id   =  p_crest_court_id;

            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_with_crest- CTX-2179'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'XHBSTG_HOME_COURT_DM - 1 row Updated with Error : '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => SQL%ROWCOUNT
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

            UPDATE xhbstg_warned_list_details_dm wld
            SET    wld.xhibit_etl_date   = SYSDATE
                  ,wld.xhibit_court_id   = v_xhibit_court_id
                  ,wld.xhibit_enrich_date = SYSDATE
                  ,wld.xhibit_etl_status = 'X' -- Error
                  ,wld.xhibit_etl_err_message = v_err_message
            WHERE  wld.crest_court_id   =  p_crest_court_id;

            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_with_crest- CTX-2179'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'XHBSTG_WARNED_LIST_DETAILS_DM - 1 row Updated with Error :'||v_err_message
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => SQL%ROWCOUNT
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
 

           COMMIT;
          END;
          
END update_xhb_court_with_crest;

/**
  * NAME       : upd_xhb_defendant_with_crest
  * DESCRIPTION: CTX-2180. New CTX fields for XHB_DEFENDANT - Sec 4.3.2.3 req [4975.DM.006]
  *              CTX-2181. New CTX fields for XHB_DEFENDANT_ON_CASE - Sec 4.3.2.4 req [4975.DM.007]
  *              CTX-2182. New CTX fields for XHB_DEF_ON_CASE_REF_SOL_FIRM - Sec 4.3.2.5 req [4975.DM.008]
  *              CTX-2987, 3009, 3214
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * S Sethuraman 10/12/2018 - CTX-3252 - Exception Handling block implemented within loop to continue processing byjust logging errors for failures
  * S Sethuraman 11/12/2018 - CTX-3367 - XHB_DEFENDANT_ON_CASE - CAO_STATUS mapping amended - as per FS V0.20
  * S Sethuraman 26/02/2019 - CTX-3764 - UPD_XHB_DEFENDANT_WITH_CREST - RESULTS VERIFIED if no non hearing disposal entered - NO change to existing RESULTS VERIFIED field in XHIBIT
  * A Dennis     15/03/2019 - CTX-3846 - UPD_XHB_DEFENDANT_WITH_CREST - When there is more than 1 defendant for the same case number and type the same staging record in XHBSTG_CASE_PARTY_SOF_DM can be updated twice, firstly to the correct status of U and later to N when encountered for different Xhibit courts
  * S Sethuraman 05/07/2019 - CTX-4217 - DEFENDANT_ON_CASE.CTL_APPLIES mapping added
**/
PROCEDURE upd_xhb_defendant_with_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
  TYPE xhb_def_case_rec IS RECORD
      ( 
        crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
      , case_no                      data_mig.xhbstg_case_subject_dm.case_no%TYPE  
      , case_type                    data_mig.xhbstg_case_subject_dm.case_type%TYPE
      , case_id                      xhibit.xhb_case.case_id%TYPE
      , guardian_name                xhibit.xhb_defendant.parent_guardian_name%TYPE
      , ethnic_appearance            xhibit.xhb_defendant.ethnic_appearance_code%TYPE
      , ethnic_classification        xhibit.xhb_defendant.ethnicity_self_defined%TYPE
      , current_prison_status        xhibit.xhb_defendant.current_prison_status%TYPE
      , sub_id                       data_mig.xhbstg_subject_dm.sub_id%TYPE
      , sodpa_date                   data_mig.xhbstg_case_subject_dm.sodpa_date%TYPE
      , first_mag_hrg_date           data_mig.xhbstg_case_subject_dm.first_mag_hrg_date%TYPE
      , last_mag_hrg_date            data_mig.xhbstg_case_subject_dm.last_mag_hrg_date%TYPE
      , ctl_expiry_date              data_mig.xhbstg_case_subject_dm.ctl_expiry_date%TYPE
      , cacd_forms_date              data_mig.xhbstg_case_subject_dm.cacd_forms_date%TYPE
      , form_ng_sent_date            data_mig.xhbstg_case_subject_dm.form_ng_sent_date%TYPE
      , cacd_appeal_result_date      data_mig.xhbstg_case_subject_dm.cacd_appeal_result_date%TYPE
      , cacd_result1                 data_mig.xhbstg_case_subject_dm.cacd_result1%TYPE
      , cacd_result2                 data_mig.xhbstg_case_subject_dm.cacd_result2%TYPE   -- CTX-3009
      , cacd_result3                 data_mig.xhbstg_case_subject_dm.cacd_result3%TYPE   -- CTX-3009
      , cacd_result4                 data_mig.xhbstg_case_subject_dm.cacd_result4%TYPE   -- CTX-3009
      , coa_status                   xhibit.xhb_defendant_on_case.coa_status%TYPE -- CTX-3367
      , results_verified             xhibit.xhb_defendant_on_case.results_verified%TYPE
      , date_exported                Xhibit.Xhb_Defendant_On_Case.date_exported%TYPE
      , ctl_applies                  xhibit.xhb_defendant_on_case.ctl_applies%TYPE -- CTX-4217
      );
 
      TYPE xhb_def_case_type IS TABLE OF xhb_def_case_rec;
      xhb_def_case_tt  xhb_def_case_type;

    TYPE xhb_doc_ref_sol_firm_rec IS RECORD
     ( 
      cpf_id                       data_mig.xhbstg_case_party_sof_dm.cpf_id%TYPE
    , sol_ref                      data_mig.xhbstg_case_party_sof_dm.sol_ref%TYPE );
    
     TYPE xhb_doc_sol_firm_type IS TABLE OF xhb_doc_ref_sol_firm_rec;
    xhb_doc_sol_firm_tt  xhb_doc_sol_firm_type;
         
    CURSOR cur_crest_def_case_details  IS
    SELECT xsb.crest_court_id,
           xcsb.case_no,
           xcsb.case_type,
           xc.case_id,          
           xsb.guardian_name,
           NVL(xcsb.ethnic_appearance,'Not known / Not Recorded') ethinic_appearance,
           NVL(xcsb.ethnic_classification,'Not Stated / Unknown') ethnic_classification, 
           xsb.current_prison_status,
           xsb.sub_id,
           xcsb.sodpa_date,
           first_mag_hrg_date,
           last_mag_hrg_date,
           ctl_expiry_date,
           cacd_forms_date,
           form_ng_sent_date,
           cacd_appeal_result_date,
           cacd_result1,
           cacd_result2,      --CTX-3009
           cacd_result3,      --CTX-3009
           cacd_result4,      --CTX-3009
           decode(cacd_status,'C','S','F','F',NULL) as coa_status, -- CTX-3367
           CASE
            WHEN xcsb.date_non_hrg_disp IS NOT NULL THEN 'E' 
            ELSE NULL 
          END results_verified,
          xcsb.date_non_hrg_disp AS date_exported,
          decode(xcsb.ctl_applies,NULL,NULL,'Y','Y','N','N',NULL) as ctl_applies -- CTX-4217
    FROM  data_mig.xhbstg_subject_dm   xsb
        , data_mig.xhbstg_case_subject_dm   xcsb
        , xhibit.xhb_case xc
    WHERE xsb.crest_court_id = p_crest_court_id
      AND xsb.crest_court_id = xcsb.crest_court_id       
      AND xsb.sub_id         = xcsb.sub_id            
      AND xcsb.case_no       = xc.case_number
      AND xcsb.case_type     = xc.case_type
      AND xc.court_id        = get_xhb_court_from_crest_court(p_crest_court_id)
      ORDER BY xc.case_id;

    CURSOR cur_crest_dref_sol_firm_det 
          ( l_court_id              IN    xhibit.xhb_court.crest_court_id%TYPE,
            l_def_on_case_id        IN    xhibit.xhb_defendant_on_case.defendant_on_case_id%TYPE) IS
    SELECT xcps.cpf_id,
           xcps.sol_ref
    FROM  data_mig.xhbstg_case_subject_dm   xcsb
        , xhibit.xhb_defendant xd
        , xhibit.xhb_defendant_on_case xdc
        , data_mig.xhbstg_case_party_sof_dm xcps
    WHERE xcsb.crest_court_id = l_court_id
      AND xcps.crest_court_id = xcsb.crest_court_id
      AND xdc.defendant_on_case_id = l_def_on_case_id
      AND xd.defendant_id = xdc.defendant_id
      AND xd.crest_defendant_id = xcsb.sub_id 
      AND xd.court_id = get_xhb_court_from_crest_court(p_crest_court_id) -- CTX-3846 Added to filter on XHIBIT court
      AND xcps.case_no = xcsb.case_no 
      AND xcps.case_type = xcsb.case_type;

    v_count_number_of_rows   NUMBER := 0;
    v_count_doc_rows         NUMBER := 0;
    v_count_dsf_rows         NUMBER := 0;
    v_def_upd_rows           NUMBER := 0;
    v_doc_upd_rows           NUMBER := 0;
    v_dsf_upd_rows           NUMBER := 0;
    v_def_upd_status         CHAR(1) := 'N';
    v_doc_upd_status         CHAR(1) := 'N';
    v_dsf_upd_status         CHAR(1) := 'N';
    v_sub_id                 xhbstg_subject_dm.sub_id%TYPE := NULL;
    v_def_id                 xhibit.xhb_defendant.defendant_id%TYPE := NULL;
    v_case_id                xhibit.xhb_case.case_id%TYPE := NULL;
    v_cpf_id                 xhbstg_case_party_sof_dm.cpf_id%TYPE := NULL;
    v_def_on_case_id         xhbstg_case_subject_dm.case_no%TYPE := NULL;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE := '-';
    
BEGIN

     insert_dm_log (p_crest_court_id     => p_crest_court_id 
                   ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                   ,p_run_time           =>  sysdate
                   ,p_log_msg_type       =>  'I' -- Information
                   ,p_log_msg            =>  'XHB_DEFENDANT - Starting process of updating existing rows for new columns with the required data from CREST'
                   ,p_err_row_count      =>  NULL
                   ,p_success_row_count  => NULL
                   ,p_last_updated_by    => 'DATA MIGRATION'
                   ,p_created_by         => 'DATA MIGRATION'
                   );

    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing rows in XHB_DEFENDANT for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

      -- Here we are going to update the rest of the columns the rows in XHB_DEFENDANT table that have not yet been populated with CREST data
    OPEN cur_crest_def_case_details;
    LOOP
    FETCH cur_crest_def_case_details BULK COLLECT INTO xhb_def_case_tt LIMIT g_bulk_limit;
    IF xhb_def_case_tt IS NOT NULL AND xhb_def_case_tt.COUNT > 0 THEN
    
        FOR i IN xhb_def_case_tt.FIRST .. xhb_def_case_tt.LAST LOOP
        
        BEGIN -- CTX-3252
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_DEFENDANT - processing sub id - '||xhb_def_case_tt(i).sub_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
 --   DBMS_OUTPUT.PUT_LINE(' ');
 --   DBMS_OUTPUT.PUT_LINE('UPDATING XHB_DEFENDANT - FOR CREST_COURT_ID = '||xhb_def_case_tt(i).crest_court_id);
    
        -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
        v_sub_id   := xhb_def_case_tt(i).sub_id;
        v_case_id  := xhb_def_case_tt(i).case_id;
                  
         -- Update the rows in XHB_DEFENDANT with data from CREST  
         UPDATE  xhibit.xhb_defendant xd
            SET   xd.parent_guardian_name           = xhb_def_case_tt(i).guardian_name
                  , xd.ethnic_appearance_code       = xhb_def_case_tt(i).ethnic_appearance
                  , xd.ethnicity_self_defined       = xhb_def_case_tt(i).ethnic_classification
                  , xd.current_prison_status        = xhb_def_case_tt(i).current_prison_status
                  , xd.last_update_date             = SYSDATE
                  , xd.last_updated_by              = 'DATA MIGRATION'
            WHERE  xd.court_id                      = v_xhibit_court_id
              AND  xd.crest_defendant_id            = xhb_def_case_tt(i).sub_id
        RETURNING xd.defendant_id into v_def_id;
    
    v_def_upd_rows := SQL%ROWCOUNT;
 --   DBMS_OUTPUT.PUT_LINE(' ');
 --   DBMS_OUTPUT.PUT_LINE('CTX-2180:XHB_DEFENDANT - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_def_upd_rows);
    
               -- Update XHBSTG_SUBJECT_DM table for the row processed
         IF v_def_upd_rows > 0 THEN 
            v_def_upd_status := 'U'; -- XHIBIT TABLE updated
         ELSIF v_def_upd_rows = 0 THEN
            v_def_upd_status := 'N'; -- No Action Performed on XHIBIT table  
         END IF;  

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_DEFENDANT - updated crest_def_id '||xhb_def_case_tt(i).sub_id||' row with ETL_STATUS '||v_def_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_def_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         

           -- Update XHBSTG_SUBJECT_DM table for the rows processed
            UPDATE xhbstg_subject_dm xs
            SET    xs.xhibit_etl_date   = SYSDATE
                  ,xs.xhibit_court_id   = v_xhibit_court_id
                  ,xs.xhibit_enrich_date = SYSDATE
                  ,xs.xhibit_etl_status = v_def_upd_status
            WHERE  xs.crest_court_id   =  p_crest_court_id
              AND  xs.sub_id = xhb_def_case_tt(i).sub_id;     

     --           DBMS_OUTPUT.PUT_LINE(' ');
     --           DBMS_OUTPUT.PUT_LINE('CTX-2180:XHBSTG_SUBJECT_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
              
         -- Update the row in XHB_DEFENDANT_ON_CASE with data from CREST  
         UPDATE  xhibit.xhb_defendant_on_case xdc
            SET   xdc.driving_disq_suspended_date    = xhb_def_case_tt(i).sodpa_date
                  , xdc.mag_court_first_hearing_date = xhb_def_case_tt(i).first_mag_hrg_date
                  , xdc.mag_court_final_hearing_date = xhb_def_case_tt(i).last_mag_hrg_date
                  , xdc.custody_time_limit           = xhb_def_case_tt(i).ctl_expiry_date
                  , xdc.date_rcpt_notice_appeal      = xhb_def_case_tt(i).cacd_forms_date
                  , xdc.form_ng_sent_date            = xhb_def_case_tt(i).form_ng_sent_date
                  , xdc.cacd_appeal_result_date      = xhb_def_case_tt(i).cacd_appeal_result_date
                  , xdc.cacd_appeal_result           = xhb_def_case_tt(i).cacd_result1 ||
                                                       xhb_def_case_tt(i).cacd_result2 ||        --CTX-3009
                                                       xhb_def_case_tt(i).cacd_result3 ||        --CTX-3009
                                                       xhb_def_case_tt(i).cacd_result4           --CTX-3009
                  , xdc.coa_status                   = xhb_def_case_tt(i).coa_status
                  -- CTX-3764
                  , xdc.results_verified             = CASE
                                                        WHEN xhb_def_case_tt(i).results_verified = 'E' THEN xhb_def_case_tt(i).results_verified
                                                        ELSE xdc.results_verified
                                                        END
                  -- CTX-3764                                      
                  , xdc.date_exported                = CASE
                                                        WHEN xdc.results_verified = 'E' THEN xdc.date_exported
                                                        ELSE xhb_def_case_tt(i).date_exported
                                                        END
                  , xdc.last_update_date             = SYSDATE
                  , xdc.last_updated_by              = 'DATA MIGRATION'
                  , xdc.ctl_applies                  = xhb_def_case_tt(i).ctl_applies -- CTX-4217
            WHERE  xdc.defendant_id                  = v_def_id
              AND xdc.case_id = xhb_def_case_tt(i).case_id
              AND nvl(xdc.obs_ind,'N') != 'Y'
            RETURNING xdc.defendant_on_case_id into v_def_on_case_id;

     v_doc_upd_rows := SQL%ROWCOUNT;
 --   DBMS_OUTPUT.PUT_LINE(' ');
 --   DBMS_OUTPUT.PUT_LINE('CTX-2181:XHB_DEFENDANT_ON_CASE - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_doc_upd_rows);

         -- Update XHBSTG_CASE_SUBJECT_DM table for the rows processed
        IF v_doc_upd_rows > 0 THEN 
            v_doc_upd_status := 'U'; -- XHIBIT TABLE updated
         ELSIF v_doc_upd_rows = 0 THEN
            v_doc_upd_status := 'N'; -- No Action Performed on XHIBIT table  
         END IF; 
  
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_DEFENDANT_ON_CASE - updated crest_def_id '||xhb_def_case_tt(i).sub_id||', case_id '||xhb_def_case_tt(i).case_id||' row with ETL_STATUS '||v_doc_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_doc_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

         -- Update XHBSTG_CASE_SUBJECT_DM table for the rows processed         
            UPDATE xhbstg_case_subject_dm xcs
            SET    xcs.xhibit_etl_date   = SYSDATE
                  ,xcs.xhibit_court_id   = v_xhibit_court_id
                  ,xcs.xhibit_enrich_date = SYSDATE
                  ,xcs.xhibit_etl_status = v_doc_upd_status
            WHERE  xcs.crest_court_id   =  p_crest_court_id
              AND  xcs.sub_id     = xhb_def_case_tt(i).sub_id
              AND  xcs.case_no    = xhb_def_case_tt(i).case_no
              AND  xcs.case_type  = xhb_def_case_tt(i).case_type;

    --            DBMS_OUTPUT.PUT_LINE(' ');
    --            DBMS_OUTPUT.PUT_LINE('CTX-2181:XHBSTG_CASE_SUBJECT_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);     

       OPEN cur_crest_dref_sol_firm_det(p_crest_court_id,v_def_on_case_id);
         LOOP
         FETCH cur_crest_dref_sol_firm_det BULK COLLECT INTO xhb_doc_sol_firm_tt LIMIT g_bulk_limit;
 
           IF xhb_doc_sol_firm_tt IS NOT NULL AND xhb_doc_sol_firm_tt.COUNT > 0 THEN
    
             FOR k IN xhb_doc_sol_firm_tt.FIRST .. xhb_doc_sol_firm_tt.LAST LOOP
  
              BEGIN -- CTX-3252
              
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_cpf_id   := xhb_doc_sol_firm_tt(k).cpf_id;
 
                 --  Update XHB_DEF_ON_CASE_REF_SOL_FIRM
                 UPDATE xhibit.xhb_def_on_case_ref_sol_firm xdcsf
                    SET xdcsf.solicitor_ref   = xhb_doc_sol_firm_tt(k).sol_ref
                  WHERE xdcsf.defendant_on_case_id   =  v_def_on_case_id
                    AND  xdcsf.crest_cpf_id = xhb_doc_sol_firm_tt(k).cpf_id
                    AND nvl(xdcsf.obs_ind,'N') != 'Y';

                 v_dsf_upd_rows := SQL%ROWCOUNT;
     --            DBMS_OUTPUT.PUT_LINE(' ');
     --            DBMS_OUTPUT.PUT_LINE('CTX-2182:XHB_DEF_ON_CASE_REF_SOL_FIRM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_dsf_upd_rows);

               -- Update XHB_DEF_ON_CASE_REF_SOL_FIRM table for the rows processed
                IF v_dsf_upd_rows > 0 THEN 
                   v_dsf_upd_status := 'U'; -- XHIBIT TABLE updated
                ELSIF v_dsf_upd_rows = 0 THEN
                   v_dsf_upd_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_DEF_ON_CASE_REF_SOL_FIRM - updated crest_cpf_id '||xhb_doc_sol_firm_tt(k).cpf_id||', def_on_case_id '||v_def_on_case_id||' row with ETL_STATUS '||v_dsf_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_dsf_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
          -- CTX-3846: In XHBSTG_CASE_PARTY_SOF_DM, IF a row has already been set to U then it should not be changed to N. Also see the
          -- comments futher below for CTX-3214 about the later call to update_xhb_prsf_with_crest that will set to N.
          IF v_dsf_upd_status = 'U' THEN                         
                 -- Update XHBSTG_CASE_PARTY_SOF_DM table for the rows processed
                 UPDATE xhbstg_case_party_sof_dm xps
                    SET    xps.xhibit_etl_date   = SYSDATE
                          ,xps.xhibit_court_id   = v_xhibit_court_id
                          ,xps.xhibit_enrich_date = SYSDATE
                          ,xps.xhibit_etl_status = v_dsf_upd_status
                  WHERE  xps.crest_court_id   =  p_crest_court_id
                    AND  xps.cpf_id = xhb_doc_sol_firm_tt(k).cpf_id;

           END IF;
                    
          --      DBMS_OUTPUT.PUT_LINE(' ');
          --      DBMS_OUTPUT.PUT_LINE('CTX-2182:XHBSTG_CASE_PARTY_SOF_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
       EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; -- CTX-3252 - Don;t ROLLBACK, log error and continue processing
        DBMS_OUTPUT.PUT_LINE('!!! ERROR OCCURRED IN update_xhb_def_with_crest for CREST_COURT: '||p_crest_court_id||',sub_id: '||v_sub_id||', case_id: '||v_case_id||', def_on_case_id: '||v_def_on_case_id||' , crest_cpf_id: '||v_cpf_id||' - '||SUBSTR(v_err_message,1,55));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_defendant_with_crest - XHBSTG_CASE_PARTY_SOF_DM - Error processing crest_def_id : '||v_sub_id||' , case_id : '||v_case_id||' , def_on_case_id : '||v_def_on_case_id||' , crest_cpf_id : '||v_cpf_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_CASE_PARTY_SOF_DM
         -- tables with error status/messages
         BEGIN

            UPDATE xhbstg_case_party_sof_dm xcps
            SET    xcps.xhibit_etl_date   = SYSDATE
                  ,xcps.xhibit_court_id   = v_xhibit_court_id
                  ,xcps.xhibit_enrich_date = SYSDATE
                  ,xcps.xhibit_etl_status = 'X' -- Error
                  ,xcps.xhibit_etl_err_message = v_err_message
            WHERE  xcps.crest_court_id   =  p_crest_court_id
              AND  xcps.cpf_id = v_cpf_id; 
            
          END;
        
         END; -- CTX-3252  
             END LOOP;
             
             v_count_dsf_rows := v_count_dsf_rows + xhb_doc_sol_firm_tt.COUNT;

           END IF;
    
           EXIT WHEN cur_crest_dref_sol_firm_det%NOTFOUND;
    
          END LOOP;
        CLOSE cur_crest_dref_sol_firm_det;              
         
        EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; -- CTX-3252 - Don;t ROLLBACK, log error and continue processing
        DBMS_OUTPUT.PUT_LINE('!!! ERROR OCCURRED IN update_xhb_def_with_crest for CREST_COURT: '||p_crest_court_id||',sub_id: '||v_sub_id||', case_id: '||v_case_id||', def_on_case_id: '||v_def_on_case_id||' , crest_cpf_id: '||v_cpf_id||' - '||SUBSTR(v_err_message,1,55));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_defendant_with_crest - Error processing crest_def_id : '||v_sub_id||' , case_id : '||v_case_id||' , def_on_case_id : '||v_def_on_case_id||' , crest_cpf_id : '||v_cpf_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_SUBJECT_DM, XHBSTG_CASE_SUBJECT_DM
         -- tables with error status/messages
         BEGIN
            UPDATE xhbstg_subject_dm xs
            SET    xs.xhibit_etl_date   = SYSDATE
                  ,xs.xhibit_court_id   = v_xhibit_court_id
                  ,xs.xhibit_enrich_date = SYSDATE
                  ,xs.xhibit_etl_status = 'X' -- Error
                  ,xs.xhibit_etl_err_message = v_err_message
            WHERE  xs.crest_court_id   =  p_crest_court_id
              AND  xs.sub_id = v_sub_id;

            UPDATE xhbstg_case_subject_dm xcs
            SET    xcs.xhibit_etl_date   = SYSDATE
                  ,xcs.xhibit_court_id   = v_xhibit_court_id
                  ,xcs.xhibit_enrich_date = SYSDATE
                  ,xcs.xhibit_etl_status = 'X' -- Error
                  ,xcs.xhibit_etl_err_message = v_err_message
            WHERE  xcs.crest_court_id   =  p_crest_court_id
              AND  xcs.sub_id = v_sub_id
              AND  xcs.case_no = v_case_id; 
            
          END;
        
         END; -- CTX-3252  
         END LOOP;
         
       COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
       v_count_doc_rows := v_count_doc_rows + xhb_def_case_tt.COUNT;

       
       END IF;
    
       EXIT WHEN cur_crest_def_case_details%NOTFOUND;
    
    END LOOP;
    CLOSE cur_crest_def_case_details;

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DEFENDANT : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DEFENDANT_ON_CASE : Processed '||v_count_doc_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_doc_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DEF_ON_CASE_REF_SOL_FIRM : Processed '||v_count_dsf_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_dsf_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
  
            -- Update XHBSTG_SUBJECT_DM table for the rows NOT processed
            UPDATE xhbstg_subject_dm xcd
            SET    xcd.xhibit_etl_date   = SYSDATE
                  ,xcd.xhibit_court_id   = v_xhibit_court_id
                  ,xcd.xhibit_enrich_date = SYSDATE
                  ,xcd.xhibit_etl_status = 'N' -- Not processed
            WHERE  xcd.crest_court_id   =  p_crest_court_id
              AND  xcd.xhibit_etl_status is NULL 
              AND  xcd.xhibit_enrich_date  is NULL
              AND  xcd.xhibit_etl_date  is NULL
              AND  xcd.xhibit_court_id is NULL;
           
    
            -- Update XHBSTG_CASE_SUBJECT_DM table for the rows NOT processed
            UPDATE xhbstg_case_subject_dm xcsd
            SET    xcsd.xhibit_etl_date   = SYSDATE
                  ,xcsd.xhibit_court_id   = v_xhibit_court_id
                  ,xcsd.xhibit_enrich_date = SYSDATE
                  ,xcsd.xhibit_etl_status = 'N' -- Not processed
            WHERE  xcsd.crest_court_id   =  p_crest_court_id
              AND  xcsd.xhibit_etl_status is NULL 
              AND  xcsd.xhibit_enrich_date  is NULL
              AND  xcsd.xhibit_etl_date  is NULL
              AND  xcsd.xhibit_court_id is NULL;          
        
        /*  CTX-3214: Commented this out since it was stopping the table xhbstg_case_party_sof_dm from being processed again when the 
                      procedure update_xhb_prsf_with_crest was called later on during the data migration run            
            -- Update xhbstg_case_party_sof_dm_ table for the rows NOT processed
            UPDATE xhbstg_case_party_sof_dm xcsfd
            SET    xcsfd.xhibit_etl_date   = SYSDATE
                  ,xcsfd.xhibit_court_id   = v_xhibit_court_id
                  ,xcsfd.xhibit_enrich_date = SYSDATE
                  ,xcsfd.xhibit_etl_status = 'N' -- Not processed
            WHERE  xcsfd.crest_court_id   =  p_crest_court_id
              AND  xcsfd.xhibit_etl_status is NULL 
              AND  xcsfd.xhibit_enrich_date  is NULL
              AND  xcsfd.xhibit_etl_date  is NULL
              AND  xcsfd.xhibit_court_id is NULL;     
          */                                                                   
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2180:XHB_DEFENDANT and CTX-2181:XHB_DEFENDANT_ON_CASE processed '||v_count_doc_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE('CTX-2182:XHB_DEF_ON_CASE_REF_SOL_FIRM processed '||v_count_dsf_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

   COMMIT;    
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        -- ROLLBACK; -- CTX-3252 Don't rollback JUST log error
        DBMS_OUTPUT.PUT_LINE('!!! ERROR OCCURRED IN update_xhb_def_with_crest for CREST_COURT: '||p_crest_court_id||',sub_id: '||v_sub_id||', case_id: '||v_case_id||', def_on_case_id: '||v_def_on_case_id||' , crest_cpf_id: '||v_cpf_id||' - '||SUBSTR(v_err_message,1,55));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_defendant_with_crest- CTX-2180,CTX-2181,CTX-2182'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_defendant_with_crest - Error processing crest_def_id : '||v_sub_id||' , case_id : '||v_case_id||' , def_on_case_id : '||v_def_on_case_id||' , crest_cpf_id : '||v_cpf_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
            COMMIT;
          
END upd_xhb_defendant_with_crest;


/**
  * NAME       : upd_xhb_legal_aid_order_crest
  * DESCRIPTION: CTX-2185. New CTX fields for XHB_LEGAL_AID_ORDER - Sec 4.3.2.9 req [4975.DM.012]
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * 
  * 05/12/2018 : CTX-3005 : Implementing FS changes v0.18 to include revocation data
  * S Sethuraman 05/12/2018 - CTX-3252 - Exception Handling block implemented within loop to continue processing byjust logging errors for failures
  * S Sethuraman 11/12/2018 - CTX-3032 - fx v0.20 MAPPING CORRECTION FOR REVOC_REASON
  * S Sethuraman 01/05/2019 - CTX-4167 : Legal_aid_order - REVOC_DATE IS NOT NULL - Condition to be removed and record migrated if NOT already existis in XHIBIT
  * S Sethuraman 17/06/2019 - CTX-4326 : Legal AID ORDER - OBS_IND added to migration
**/
PROCEDURE upd_xhb_legal_aid_order_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;

 TYPE xhb_lao_rec IS RECORD
    (
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , leo_id                       data_mig.xhbstg_legal_aid_order_dm.leo_id%TYPE
    , def_on_case_id               xhibit.xhb_defendant_on_case.defendant_on_case_id%TYPE    
    , case_pros_agency_id          xhibit.xhb_legal_aid_order.case_pros_agency_id%TYPE
    , la_date                      data_mig.xhbstg_legal_aid_order_dm.la_date%TYPE
    , granted_by                   xhibit.xhb_legal_aid_order.granted_by%TYPE
    , psd_la_ref                   data_mig.xhbstg_legal_aid_order_dm.psd_la_ref%TYPE
    , no_of_counsel                data_mig.xhbstg_legal_aid_order_dm.no_of_counsel%TYPE
    , no_of_qc                     data_mig.xhbstg_legal_aid_order_dm.no_of_qc%TYPE
    , revoc_date                   data_mig.xhbstg_legal_aid_order_dm.revoc_date%TYPE
    , reason_for_revocation_id     data_mig.xhbstg_legal_aid_order_dm.revoc_reason%TYPE
    , obs_ind                      xhibit.xhb_legal_aid_order.obs_ind%TYPE
    );

    TYPE xhb_lao_type IS TABLE OF xhb_lao_rec;
    xhb_lao_tt  xhb_lao_type;
    default_xhb_lao_tt  xhb_lao_type;
    
    -- CTX-3005 - UPD_CUR for exisiting XHIBIT records that need to be updated
    -- CTX-3005 - NON NULL REFUSAL DATE records ignored as they dont need to be updated in XHIBIT
    CURSOR upd_cur_crest_lao_details IS
    SELECT clao.crest_court_id,
           clao.leo_id,
           xdc.defendant_on_case_id, -- CTX-3005
           NULL as case_pros_agency_id, -- CTX-3005
           clao.la_date  ORDER_DATE,
           decode(clao.psd_crown_ct_ind,'M','LA','C','CC') GRANTED_BY,
           clao.psd_la_ref psd_ro_ref,
           clao.no_of_counsel number_of_advocates,
           clao.no_of_qc number_of_qcs,
           clao.revoc_date date_of_revocation,
           decode(clao.revoc_reason,'CON',1,'SBR',2,'SLR',3,NULL) as reason_for_revocation_id,-- CTX-3032
           decode(clao.revoc_date,NULL,'N','Y') as obs_ind-- CTX-4326
 /*          (SELECT xrs.ref_system_code_id
            FROM   xhibit.xhb_ref_system_code xrs
            WHERE  xrs.code_type = 'LA_INV_REASON'
            AND    xrs.code      = clao.revoc_reason
            AND    xrs.court_id  = xhc.court_id
            AND    NVL(xrs.obs_ind,'N') != 'Y'
           ) as reason_for_revocation_id -- CTX-3005 */
    from xhibit.xhb_legal_aid_order xlao,
         xhibit.xhb_defendant_on_case xdc,
         xhibit.xhb_court xhc,
         xhibit.xhb_defendant xd,
         xhibit.xhb_case xc,
         data_mig.xhbstg_legal_aid_order_dm clao
    where clao.crest_court_id = p_crest_court_id and
          clao.crest_court_id = xhc.crest_court_id and
          xhc.court_id = xc.court_id and
          xd.court_id = xc.court_id and
          xlao.crest_leo_id = clao.leo_id and
          clao.case_no = xc.case_number and
          clao.case_type = xc.case_type and
          xd.defendant_id = xdc.defendant_id and
          xdc.case_id = xc.case_id and
          xd.crest_defendant_id = clao.sub_opp_id and -- CTX-3005
          xlao.defendant_on_case_id = xdc.defendant_on_case_id and
          clao.refusal_date is NULL and -- CTX-3005 - NON NULL REFUSAL date should NOT be imported to XHIBIT
          nvl(xlao.obs_ind,'N') != 'Y' and
          NVL(clao.xhibit_etl_status,'N') not in ('I', 'U') and
          clao.xhibit_enrich_date is NULL;
   
    -- CTX-3005 - Insert in XHIBIT all NON-Matching rows except where refusal date is populated
    CURSOR ins_cur_crest_lao_details IS
    SELECT clao.crest_court_id,
           clao.leo_id,
           xdc.defendant_on_case_id, -- CTX-3005
           NULL as case_pros_agency_id, -- CTX-3005
           clao.la_date  ORDER_DATE,
           decode(clao.psd_crown_ct_ind,'M','LA','C','CC') GRANTED_BY,
           clao.psd_la_ref psd_ro_ref,
           clao.no_of_counsel number_of_advocates,
           clao.no_of_qc number_of_qcs,
           clao.revoc_date date_of_revocation,
           decode(clao.revoc_reason,'CON',1,'SBR',2,'SLR',3,NULL) as reason_for_revocation_id, -- CTX-3032
           decode(clao.revoc_date,NULL,'N','Y') as obs_ind-- CTX-4326
/*           (SELECT xrs.ref_system_code_id
            FROM   xhibit.xhb_ref_system_code xrs
            WHERE  xrs.code_type = 'LA_INV_REASON'
            AND    xrs.code      = clao.revoc_reason
            AND    xrs.court_id  = xhc.court_id
            AND    NVL(xrs.obs_ind,'N') != 'Y'
           ) as reason_for_revocation_id -- CTX-3005 */
    from xhibit.xhb_defendant_on_case xdc,
         xhibit.xhb_court xhc,
         xhibit.xhb_defendant xd,
         xhibit.xhb_case xc,
         data_mig.xhbstg_legal_aid_order_dm clao
    where clao.crest_court_id = p_crest_court_id and
          clao.crest_court_id = xhc.crest_court_id and
          xhc.court_id = xc.court_id and
          xd.court_id = xc.court_id and
          clao.case_no = xc.case_number and
          clao.case_type = xc.case_type and
          xd.defendant_id = xdc.defendant_id and
          xdc.case_id = xc.case_id and
          xd.crest_defendant_id = clao.sub_opp_id and -- CTX-3005
          clao.refusal_date is NULL and -- CTX-3005 - NON NULL REFUSAL date should NOT be imported to XHIBIT
          NVL(clao.xhibit_etl_status,'N') not in ('I', 'U') and
          clao.xhibit_enrich_date is NULL and
          -- CTX-4167 Remove REVOC DATE IS NOT NULL condition and migrate if record NOT exists in XHIBIT
      --    clao.revoc_date is NOT NULL and
          NOT EXISTS (select 'X' from xhibit.xhb_legal_aid_order xlao
                        where xlao.crest_leo_id = clao.leo_id and
                              xlao.defendant_on_case_id = xdc.defendant_on_case_id and
                              NVL(xlao.obs_ind,'N') != 'Y');

    v_count_number_of_rows   NUMBER := 0;
    v_lao_upd_rows           NUMBER := 0;
    v_lao_np_rows           NUMBER := 0;
    v_lao_upd_status         CHAR(1) := 'N';
    v_lao_ins_rows           NUMBER := 0;
    v_lao_ins_status         CHAR(1) := 'N';
    v_crest_leo_id           xhbstg_legal_aid_order_dm.leo_id%TYPE;
    v_def_on_case_id         xhibit.xhb_defendant_on_case.defendant_on_case_id%TYPE;
    v_case_id                xhibit.xhb_case.case_id%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;

BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_LEGAL_AID_ORDER - Starting process of updating existing rows for new columns with the required data from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing rows in XHB_LEGAL_AID_ORDER for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

      -- Here we are going to update the rest of the columns the rows in XHB_LEGAL_AID_ORDER table that have not yet been populated with CREST data
    OPEN upd_cur_crest_lao_details;
    LOOP
    FETCH upd_cur_crest_lao_details BULK COLLECT INTO xhb_lao_tt LIMIT g_bulk_limit;

    IF xhb_lao_tt IS NOT NULL AND xhb_lao_tt.COUNT > 0 THEN

        FOR i IN xhb_lao_tt.FIRST .. xhb_lao_tt.LAST LOOP
        BEGIN -- CTX-3252
           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_LEGAL_AID_ORDER - processing leo id - '||xhb_lao_tt(i).leo_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('UPDATING XHB_LEGAL_AID_ORDER - FOR CREST_COURT_ID = '||xhb_lao_tt(i).crest_court_id);

 -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_crest_leo_id     := xhb_lao_tt(i).leo_id;
            v_def_on_case_id   := xhb_lao_tt(i).def_on_case_id;
             DBMS_OUTPUT.PUT_LINE('v_crest_leo_id : '||v_crest_leo_id||' , v_def_on_case_id : '||v_def_on_case_id||' , v_xhibit_court_id '||v_xhibit_court_id);
            -- Update the rows in XHB_LEGAL_AID_ORDER with data from CREST
         UPDATE  xhibit.xhb_legal_aid_order xlao
            SET    xlao.case_pros_agency_id          = xhb_lao_tt(i).case_pros_agency_id
                  , xlao.order_date                   = xhb_lao_tt(i).la_date
                  , xlao.granted_by                   = xhb_lao_tt(i).granted_by
                  , xlao.psd_ro_ref                   = xhb_lao_tt(i).psd_la_ref
                  , xlao.number_of_advocates          = xhb_lao_tt(i).no_of_counsel
                  , xlao.number_of_qcs                = xhb_lao_tt(i).no_of_qc
                  , xlao.date_of_revocation           = xhb_lao_tt(i).revoc_date
                  , xlao.reason_for_revocation_id     = xhb_lao_tt(i).reason_for_revocation_id
                  , xlao.obs_ind                      = xhb_lao_tt(i).obs_ind
                  , xlao.last_update_date             = SYSDATE
                  , xlao.last_updated_by              = 'DATA MIGRATION'
            WHERE  xlao.crest_leo_id                  = xhb_lao_tt(i).leo_id
              AND  xlao.defendant_on_case_id          = xhb_lao_tt(i).def_on_case_id
              AND  NVL(xlao.obs_ind,'N') != 'Y';

    v_lao_upd_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2185:XHB_LEGAL_AID_ORDER - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_lao_upd_rows);

               -- Update XHBSTG_SUBJECT_DM table for the rows processed
         IF v_lao_upd_rows > 0 THEN
            v_lao_upd_status := 'U'; -- XHIBIT TABLE updated
         ELSIF v_lao_upd_rows = 0 THEN
            v_lao_upd_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;

    insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_LEGAL_AID_ORDER - updated crest_leo_id '||xhb_lao_tt(i).leo_id||
                                                   ', def_on_case_id '||v_def_on_case_id||
                                                   ' row with ETL_STATUS '||v_lao_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_lao_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


           -- Update XHBSTG_LEGAL_AID_ORDER_DM table for the rows processed
            UPDATE xhbstg_legal_aid_order_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = v_lao_upd_status
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.leo_id = xhb_lao_tt(i).leo_id;

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2185:XHBSTG_LEGAL_AID_ORDER_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

     EXCEPTION  -- CTX-3252
       WHEN OTHERS THEN
        v_err_message := SQLERRM;
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_legal_aid_order_crest/amendment for CREST_COURT : '||p_crest_court_id||',crest_leo_id : '||v_crest_leo_id||' , v_def_on_case_id : '||v_def_on_case_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_with_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_legal_aid_order_crest- Error processing crest_leo_id : '||v_crest_leo_id||
                                                   ' , case_id : '||v_case_id||
                                                   ' , def_on_case_id : '||v_def_on_case_id||
                                                   ' ERROR :'||substr(v_err_message,1,200)
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_LEGAL_AID_ORDER_DM
         -- tables with error status/messages
         BEGIN
            UPDATE xhbstg_legal_aid_order_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = 'X' -- Error
                  ,xl.xhibit_etl_err_message = v_err_message
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.leo_id = v_crest_leo_id;
          END;
       END; -- CTX-3252
     END LOOP;

      COMMIT;
      v_count_number_of_rows := v_count_number_of_rows + xhb_lao_tt.COUNT;

     END IF;

    EXIT WHEN upd_cur_crest_lao_details%NOTFOUND;

    END LOOP;


    CLOSE upd_cur_crest_lao_details;

         -- CTX-3005 - Insert NON-Matching CREST rows into XHIBIT
          
          xhb_lao_tt := default_xhb_lao_tt; -- re-initialise table
          
                      insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_LEGAL_AID_ORDER - Starting process of inserting records that dont exists already in XHIBIT'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


         -- Here we are going to insert the rows that dont exist already in XHB_LEGAL_AID_ORDER table that have not yet been populated with CREST data
         OPEN ins_cur_crest_lao_details;
         LOOP
         FETCH ins_cur_crest_lao_details BULK COLLECT INTO xhb_lao_tt LIMIT g_bulk_limit;

         IF xhb_lao_tt IS NOT NULL AND xhb_lao_tt.COUNT > 0 THEN

         FOR i IN xhb_lao_tt.FIRST .. xhb_lao_tt.LAST LOOP
           BEGIN -- CTX-3252
             insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTING into XHB_LEGAL_AID_ORDER - processing leo id - '||xhb_lao_tt(i).leo_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
          DBMS_OUTPUT.PUT_LINE(' ');
          DBMS_OUTPUT.PUT_LINE('INSERTING XHB_LEGAL_AID_ORDER - FOR CREST_COURT_ID = '||xhb_lao_tt(i).crest_court_id);

          -- Should the insert fail we want to capture the failing row in the exception block so hold them here before update
             v_crest_leo_id     := xhb_lao_tt(i).leo_id;
             v_def_on_case_id   := xhb_lao_tt(i).def_on_case_id;
             DBMS_OUTPUT.PUT_LINE('v_crest_leo_id : '||v_crest_leo_id||' , v_def_on_case_id : '||v_def_on_case_id||' , v_xhibit_court_id '||v_xhibit_court_id);
            -- INSERT  the rows in XHB_LEGAL_AID_ORDER with data from CREST
         INSERT into  xhibit.xhb_legal_aid_order
                (
                 legal_aid_order_id,
                 crest_leo_id,
                 defendant_on_case_id,
                 case_pros_agency_id,
                 order_date,
                 granted_by,
                 psd_ro_ref,
                 number_of_advocates,
                 number_of_qcs,
                 date_of_revocation,
                 reason_for_revocation_id,
                 creation_date,
                 created_by,
                 last_update_date,
                 last_updated_by,
                 obs_ind
                 )
                VALUES
                (   xhibit.xhb_legal_aid_order_seq.nextval
                  , xhb_lao_tt(i).leo_id
                  , xhb_lao_tt(i).def_on_case_id
                  , xhb_lao_tt(i).case_pros_agency_id
                  , xhb_lao_tt(i).la_date
                  , xhb_lao_tt(i).granted_by
                  , xhb_lao_tt(i).psd_la_ref
                  , xhb_lao_tt(i).no_of_counsel
                  , xhb_lao_tt(i).no_of_qc
                  , xhb_lao_tt(i).revoc_date
                  , xhb_lao_tt(i).reason_for_revocation_id
                  , SYSDATE
                  , 'DATA MIGRATION'
                  , SYSDATE
                  , 'DATA MIGRATION'
                  , xhb_lao_tt(i).obs_ind
                );
                
  
           v_lao_ins_rows := SQL%ROWCOUNT;
           DBMS_OUTPUT.PUT_LINE(' ');
           DBMS_OUTPUT.PUT_LINE('CTX-2185:XHB_LEGAL_AID_ORDER - Crest Court : '||p_crest_court_id||' - inserted no of rows : '||v_lao_ins_rows);

           IF v_lao_ins_rows > 0 THEN
              v_lao_ins_status := 'I'; -- XHIBIT TABLE inserted
           ELSIF v_lao_ins_rows = 0 THEN
              v_lao_ins_status := 'N'; -- No Action Performed on XHIBIT table
           END IF;

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_LEGAL_AID_ORDER - inserted crest_leo_id '||xhb_lao_tt(i).leo_id||
                                                   ', def_on_case_id '||v_def_on_case_id||
                                                   ' row with ETL_STATUS '||v_lao_ins_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_lao_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


           -- Update XHBSTG_LEGAL_AID_ORDER_DM table for the rows processed
            UPDATE xhbstg_legal_aid_order_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = v_lao_ins_status
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.leo_id = xhb_lao_tt(i).leo_id;

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2185:XHBSTG_LEGAL_AID_ORDER_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
         
      EXCEPTION -- CTX-3252
       WHEN OTHERS THEN
        v_err_message := SQLERRM;
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_legal_aid_order_crest/amendment for CREST_COURT : '||p_crest_court_id||',crest_leo_id : '||v_crest_leo_id||' , v_def_on_case_id : '||v_def_on_case_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_with_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_legal_aid_order_crest- Error processing crest_leo_id : '||v_crest_leo_id||
                                                   ' , case_id : '||v_case_id||
                                                   ' , def_on_case_id : '||v_def_on_case_id||
                                                   ' ERROR :'||substr(v_err_message,1,200)
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_LEGAL_AID_ORDER_DM
         -- tables with error status/messages
         BEGIN
            UPDATE xhbstg_legal_aid_order_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = 'X' -- Error
                  ,xl.xhibit_etl_err_message = v_err_message
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.leo_id = v_crest_leo_id;

          END;
         END; -- CTX-3252

         END LOOP;

         COMMIT;
         v_count_number_of_rows := v_count_number_of_rows + xhb_lao_tt.COUNT;

       END IF;

      EXIT WHEN ins_cur_crest_lao_details%NOTFOUND;

    END LOOP;
 
    CLOSE ins_cur_crest_lao_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_LEGAL_AID_ORDER : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
           
               ------ Update rest of the Unprocessed rows in XHBSTG_LEGAL_AID_ORDER_DM
               ------ for given CREST COURT ID to 'N' i.e. NOT processed

                       -- Update XHBSTG_LEGAL_AID_ORDER_DM table for the rows NOT processed
            UPDATE xhbstg_legal_aid_order_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = 'N' -- Not processed
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.xhibit_etl_status is NULL
              AND  xl.xhibit_enrich_date  is NULL
              AND  xl.xhibit_etl_date  is NULL
              AND  xl.xhibit_court_id is NULL;

            v_lao_np_rows := SQL%ROWCOUNT;

             insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_LEGAL_AID_ORDER : updating XHBSTG_LEGAL_AID_ORDER with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_lao_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2185:XHB_LEGAL_AID_ORDER processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2185:XHBSTG_LEGAL_AID_ORDER_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_lao_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);

    COMMIT;
 --ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_legal_aid_order_crest/amendment for CREST_COURT : '||p_crest_court_id||',crest_leo_id : '||v_crest_leo_id||' , v_def_on_case_id : '||v_def_on_case_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_legal_aid_order_with_crest- CTX-2185'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_legal_aid_order_crest- Error processing crest_leo_id : '||v_crest_leo_id||
                                                   ' , case_id : '||v_case_id||
                                                   ' , def_on_case_id : '||v_def_on_case_id||
                                                   ' ERROR :'||substr(v_err_message,1,200)
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
        COMMIT;
END upd_xhb_legal_aid_order_crest;

/**
  * NAME       : upd_xhb_legal_aid_amend_crest
  * DESCRIPTION: CTX-2610. New CTX fields for XHB_LEGAL_AID_AMENDMENT - Sec 4.3.2.10 req [4975.DM.013]
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * S Sethuraman 10/12/2018 - CTX-3252 - Exception Handling block implemented within loop to continue processing byjust logging errors for failures
  * S Sethuraman 06/03/2019 - CTX-3812 - UPD_XHB_LEGAL_AID_AMEND_CREST - Fixed for missing court_id link
**/
PROCEDURE upd_xhb_legal_aid_amend_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_laa_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , leo_id                       data_mig.xhbstg_legal_aid_order_dm.leo_id%TYPE
    , change_type                  xhibit.xhb_legal_aid_amendment.amendment_type%TYPE
    , amendment_date               data_mig.xhbstg_legal_aid_order_dm.la_date%TYPE
    , lao_id                       xhibit.xhb_legal_aid_order.legal_aid_order_id%TYPE 
    );

    TYPE xhb_laa_type IS TABLE OF xhb_laa_rec;
    xhb_laa_tt  xhb_laa_type;
     
    CURSOR cur_crest_laa_details IS
    SELECT claa.crest_court_id,
           claa.leo_id,
           claa.change_type,
           claa.amendment_date,
           xlao.legal_aid_order_id
    FROM data_mig.xhbstg_legal_aid_amendment_dm claa
    ,    xhibit.xhb_legal_aid_order xlao
    ,   xhibit.xhb_defendant_on_case xdoc -- CTX-3812
    ,   xhibit.xhb_court xc  -- CTX-3812
    ,   xhibit.xhb_case xcase     -- CTX-3812
    WHERE claa.crest_court_id = p_crest_court_id
      AND xc.crest_court_id = claa.crest_court_id  -- CTX-3812   
      AND claa.leo_id = xlao.crest_leo_id
      AND xlao.defendant_on_case_id = xdoc.defendant_on_case_id -- CTX-3812
      AND xdoc.case_id = xcase.case_id -- CTX-3812
      AND xcase.court_id = xc.court_id       -- CTX-3812
      AND NVL(claa.xhibit_etl_status,'N') <> 'U'
      AND claa.xhibit_enrich_date is NULL
      /*Even though this is a new table, make sure duplicate rows are not created*/
      AND NOT EXISTS (SELECT 'x'
                      FROM xhibit.xhb_legal_aid_amendment 
                      WHERE legal_aid_order_id = xlao.legal_aid_order_id
                      AND   amendment_date = claa.amendment_date
                      AND   amendment_type = claa.change_type)
      ;    


    v_count_number_of_rows   NUMBER := 0;
--    v_count_laa_rows         NUMBER := 0;
    v_laa_np_rows            NUMBER := 0;
    v_laa_upd_rows           NUMBER := 0;
    v_laa_upd_status         CHAR(1) := 'N';
    v_crest_leo_id           xhbstg_legal_aid_amendment_dm.leo_id%TYPE;
    v_lao_id                 xhibit.xhb_legal_aid_amendment.legal_aid_order_id%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

            OPEN cur_crest_laa_details;
            LOOP
            FETCH cur_crest_laa_details BULK COLLECT INTO xhb_laa_tt LIMIT g_bulk_limit;
   
            IF xhb_laa_tt IS NOT NULL AND xhb_laa_tt.COUNT > 0 THEN
   
               FOR j IN xhb_laa_tt.FIRST .. xhb_laa_tt.LAST LOOP
 
                  BEGIN -- CTX-3252
                      -- Should  the update fail we want to capture the failing row in the exception block so hold them here before update
                     v_lao_id       := xhb_laa_tt(j).lao_id;
                     v_crest_leo_id := xhb_laa_tt(j).leo_id;
                     
                     insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                   ,p_action_name        => 'upd_xhb_legal_aid_amend_crest- CTX-2610'
                                   ,p_run_time           =>  sysdate
                                   ,p_log_msg_type       =>  'I' -- Information
                                   ,p_log_msg            =>  'INSERTING into XHB_LEGAL_AID_AMENDMENT - processing crest_leo_id - '||v_crest_leo_id||' , xhb_leo_id : '||xhb_laa_tt(j).leo_id
                                   ,p_err_row_count      =>  NULL
                                   ,p_success_row_count  => NULL
                                   ,p_last_updated_by    => 'DATA MIGRATION'
                                   ,p_created_by         => 'DATA MIGRATION'
                                   );
                     DBMS_OUTPUT.PUT_LINE(' ');
                     DBMS_OUTPUT.PUT_LINE('INSERT INTO into XHB_LEGAL_AID_AMENDMENT - FOR CREST_COURT_ID = '||xhb_laa_tt(j).crest_court_id||' , crest_leo_id : '||v_crest_leo_id);
    
                     DBMS_OUTPUT.PUT_LINE('v_lao_id : '||xhb_laa_tt(j).lao_id||' , v_xhibit_court_id '||v_xhibit_court_id);       
                   
                 -- INSERT and populate this NEW table XHB_LEGAL_AID_AMENDMENT with data from CREST  
                     INSERT INTO  xhibit.xhb_legal_aid_amendment 
                                  ( legal_aid_amendment_id
                                  , legal_aid_order_id 
                                  , amendment_date   
                                  , amendment_type 
                                  , creation_date
                                  , created_by  
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (xhibit.xhb_legal_aid_amendment_seq.nextval
                                  ,xhb_laa_tt(j).lao_id
                                  ,xhb_laa_tt(j).amendment_date
                                  ,xhb_laa_tt(j).change_type
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');     
                                                      
                    v_laa_upd_rows := SQL%ROWCOUNT;
                    DBMS_OUTPUT.PUT_LINE(' ');
                    DBMS_OUTPUT.PUT_LINE('CTX-2610:XHB_LEGAL_AID_AMENDMENT - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_laa_upd_rows);
    
                    -- Update XHBSTG_LEGAL_AID_AMENDMENT_DM table for the rows processed
                    IF v_laa_upd_rows > 0 THEN 
                           v_laa_upd_status := 'I'; -- XHIBIT TABLE Inserted
                    ELSIF v_laa_upd_rows = 0 THEN
                          v_laa_upd_status := 'N'; -- No Action Performed on XHIBIT table
                    END IF;  

                    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                  ,p_action_name        => 'upd_xhb_legal_aid_amendment_crest- CTX-2610'
                                  ,p_run_time           =>  sysdate
                                  ,p_log_msg_type       =>  'I' -- Information
                                  ,p_log_msg            =>  'INSERTING into XHB_LEGAL_AID_AMENDMENT - inserted xhb_lao_id '||v_lao_id||', crest_leo_id '||v_crest_leo_id||' row with ETL_STATUS '||v_laa_upd_status
                                  ,p_err_row_count      =>  NULL
                                  ,p_success_row_count  => v_laa_upd_rows
                                  ,p_last_updated_by    => 'DATA MIGRATION'
                                  ,p_created_by         => 'DATA MIGRATION'
                                 );
                         

                    -- Update XHBSTG_LEGAL_AID_AMENDMENT_DM table for the rows processed
                    UPDATE xhbstg_legal_aid_amendment_dm xla
                       SET    xla.xhibit_etl_date    = SYSDATE
                             ,xla.xhibit_court_id    = v_xhibit_court_id
                             ,xla.xhibit_enrich_date = SYSDATE
                             ,xla.xhibit_etl_status  = v_laa_upd_status
                     WHERE  xla.crest_court_id   = p_crest_court_id
                       AND  xla.leo_id           = v_crest_leo_id;     

                    DBMS_OUTPUT.PUT_LINE(' ');
                    DBMS_OUTPUT.PUT_LINE('CTX-2610:XHBSTG_LEGAL_AID_AMENDMENT_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
 EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; -- CTX-3252 - Dont commit log error and continue processing
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_legal_aid_amendment for CREST_COURT: '||p_crest_court_id||',crest_leo_id: '||v_crest_leo_id||'-'||SUBSTR(v_err_message,1,110));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_legal_aid_amed_with_crest- CTX-2185/2610'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_legal_aid_amed_with_crest - Error processing crest_leo_id : '||v_crest_leo_id||', ERROR :'||substr(v_err_message,1,200)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_LEGAL_AID_AMENDMENT_DM table with error status/messages

            UPDATE xhbstg_legal_aid_amendment_dm xla
            SET    xla.xhibit_etl_date   = SYSDATE
                  ,xla.xhibit_court_id   = v_xhibit_court_id
                  ,xla.xhibit_enrich_date = SYSDATE
                  ,xla.xhibit_etl_status = 'X' -- Error
                  ,xla.xhibit_etl_err_message = v_err_message
            WHERE  xla.crest_court_id   =  p_crest_court_id
              AND  xla.leo_id = v_crest_leo_id;

             END; -- CTX-3252 
             END LOOP;
        
        COMMIT;
        v_count_number_of_rows := v_count_number_of_rows + xhb_laa_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_laa_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_laa_details;  
 
  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_legal_aid_amendment_crest- CTX-2610'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_LEGAL_AID_AMENDMENT : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_LEGAL_AID_AMENDMENT_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
               
            -- Update XHBSTG_LEGAL_AID_AMENDMENT_DM table for the rows NOT processed
            UPDATE xhbstg_legal_aid_amendment_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = 'N' -- Not processed
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.xhibit_etl_status is NULL 
              AND  xl.xhibit_enrich_date  is NULL
              AND  xl.xhibit_etl_date  is NULL
              AND  xl.xhibit_court_id is NULL;
           
            v_laa_np_rows := SQL%ROWCOUNT;
            
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_legal_aid_amendment_crest- CTX-2610'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_LEGAL_AID_AMENDMENT : updating XHBSTG_LEGAL_AID_AMENDMENT with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_laa_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );                   

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2610:XHB_LEGAL_AID_AMENDMENT processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
 
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2610:XHBSTG_LEGAL_AID_AMENDMENT_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_laa_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; -- CTX-3252 Log error and Commit
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_legal_aid_amendment for CREST_COURT: '||p_crest_court_id||',crest_leo_id: '||v_crest_leo_id||'-'||SUBSTR(v_err_message,1,110));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_legal_aid_amed_with_crest- CTX-2185/2610'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_legal_aid_amed_with_crest - Error processing crest_leo_id : '||v_crest_leo_id||', ERROR :'||substr(v_err_message,1,200)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

             COMMIT;
END upd_xhb_legal_aid_amend_crest;

/**
  * NAME       : upd_xhb_ref_chamber_crest
  * DESCRIPTION: CTX-2186. New CTX fields for XHB_REF_CHAMBER - Sec 4.3.2.18 req [4975.DM.023]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_ref_chamber_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_chb_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , crest_chamber_id             data_mig.xhbstg_chambers_dm.cha_id%TYPE
    , clerk_name                   data_mig.xhbstg_chambers_dm.clerk_name%TYPE
    , ref_chamber_id               xhibit.xhb_ref_chamber.ref_chamber_id%TYPE
    );

    TYPE xhb_chb_type IS TABLE OF xhb_chb_rec;
    xhb_chb_tt  xhb_chb_type;
      
    CURSOR cur_crest_chb_details IS
    SELECT cchb.crest_court_id,
           xhc.court_id,
           cchb.cha_id crest_chamber_id,
           cchb.clerk_name,
           xchb.ref_chamber_id
    from xhibit.xhb_ref_chamber xchb,
         xhibit.xhb_court xhc,
         data_mig.xhbstg_chambers_dm cchb
    where cchb.crest_court_id = p_crest_court_id and
          cchb.crest_court_id = xhc.crest_court_id and 
          xchb.court_id = xhc.court_id and
          xchb.crest_chamber_id = cchb.cha_id and
          nvl(xchb.obs_ind,'N') != 'Y' and
          NVL(cchb.xhibit_etl_status,'N') <> 'U' and
          cchb.xhibit_enrich_date is NULL;

    v_count_number_of_rows   NUMBER := 0;
    v_chb_upd_rows           NUMBER := 0;
    v_chb_upd_status         CHAR(1) := 'N';
    v_chb_np_rows            NUMBER := 0;
    v_crest_chamber_id       xhbstg_legal_aid_order_dm.leo_id%TYPE;
    v_chamber_id             xhibit.xhb_legal_aid_amendment.legal_aid_order_id%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_ref_chamber_crest- CTX-2186'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_REF_CHAMBER - Starting process of updating existing rows for new columns with the required data from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing rows in XHB_REF_CHAMBER for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to update the rest of the columns the rows in XHB_REF_CHAMBER table that have not yet been populated with CREST data
    OPEN cur_crest_chb_details;
    LOOP
    FETCH cur_crest_chb_details BULK COLLECT INTO xhb_chb_tt LIMIT g_bulk_limit;
   
    IF xhb_chb_tt IS NOT NULL AND xhb_chb_tt.COUNT > 0 THEN
   
        FOR i IN xhb_chb_tt.FIRST .. xhb_chb_tt.LAST LOOP

          BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_ref_chamber_crest- CTX-2186'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_REF_CHAMBER - processing crest_chamber id - '||xhb_chb_tt(i).crest_chamber_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('UPDATING XHB_REF_CHAMBER - FOR CREST_COURT_ID = '||xhb_chb_tt(i).crest_court_id);
    
            -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_crest_chamber_id     := xhb_chb_tt(i).crest_chamber_id;
            v_chamber_id  := xhb_chb_tt(i).ref_chamber_id;
             DBMS_OUTPUT.PUT_LINE('v_crest_chamber_id : '||v_crest_chamber_id||' , v_chamber_id : '||v_chamber_id||' , v_xhibit_court_id '||v_xhibit_court_id);       
            
            -- Update the rows in XHB_REF_CHAMBER with data from CREST  
           UPDATE  xhibit.xhb_ref_chamber xrc
              SET     xrc.clerk_name                   = xhb_chb_tt(i).clerk_name
                    , xrc.last_update_date             = SYSDATE
                    , xrc.last_updated_by              = 'DATA MIGRATION'
              WHERE  xrc.court_id                          = xhb_chb_tt(i).court_id 
                AND  xrc.crest_chamber_id                  = xhb_chb_tt(i).crest_chamber_id
                AND  xrc.ref_chamber_id                    = xhb_chb_tt(i).ref_chamber_id
                AND  NVL(xrc.obs_ind,'N') != 'Y';
  
  
      
            v_chb_upd_rows := SQL%ROWCOUNT;
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('CTX-2186:XHB_REF_CHAMBER - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_chb_upd_rows);
      
            -- Update XHBSTG_CHAMBERS_DM table for the rows processed
            IF v_chb_upd_rows > 0 THEN 
              v_chb_upd_status := 'U'; -- XHIBIT TABLE updated
            ELSIF v_chb_upd_rows = 0 THEN
              v_chb_upd_status := 'N'; -- No Action Performed on XHIBIT table
            END IF;  
  
            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                 ,p_action_name        => 'upd_xhb_ref_chamber_crest- CTX-2186'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'I' -- Information
                                 ,p_log_msg            =>  'UPDATING XHB_REF_CHAMBER - updated crest_chamber_id '||xhb_chb_tt(i).crest_chamber_id||', chamber_id '||v_chamber_id||' row with ETL_STATUS '||v_chb_upd_status
                                 ,p_err_row_count      =>  NULL
                                 ,p_success_row_count  => v_chb_upd_rows
                                 ,p_last_updated_by    => 'DATA MIGRATION'
                                 ,p_created_by         => 'DATA MIGRATION'
                                 );
                           
  
            -- Update XHBSTG_CHAMBERS_DM table for the rows processed
            UPDATE xhbstg_chambers_dm xcd
            SET    xcd.xhibit_etl_date   = SYSDATE
                  ,xcd.xhibit_court_id   = v_xhibit_court_id
                  ,xcd.xhibit_enrich_date = SYSDATE
                  ,xcd.xhibit_etl_status = v_chb_upd_status
            WHERE  xcd.crest_court_id   =  p_crest_court_id
              AND  xcd.cha_id = xhb_chb_tt(i).crest_chamber_id;     

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2186:XHBSTG_CHAMBERS_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
 
        EXCEPTION
        WHEN OTHERS THEN
            v_err_message := SQLERRM;

            DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_ref_chamber_crest for CREST_COURT : '||p_crest_court_id||',crest_chamber_id : '||v_crest_chamber_id||' , v_chamber_id : '||v_chamber_id||'-'||SUBSTR(v_err_message,1,100));
    
               insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_ref_chamber_with_crest - CTX-2186'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'E' -- Error
                             ,p_log_msg            =>  'upd_xhb_ref_chamber_crest - Error processing crest_chamber_id : '||v_crest_chamber_id||' , chamber_id : '||v_chamber_id||' Error: '||SUBSTR(v_err_message,1,150)
                             ,p_err_row_count      => NULL 
                             ,p_success_row_count  => v_count_number_of_rows
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
             -- Update XHBSTG_CHAMBERS_DM
             -- tables with error status/messages
             BEGIN
                UPDATE xhbstg_chambers_dm xcd
                SET    xcd.xhibit_etl_date   = SYSDATE
                      ,xcd.xhibit_court_id   = v_xhibit_court_id
                      ,xcd.xhibit_enrich_date = SYSDATE
                      ,xcd.xhibit_etl_status = 'X' -- Error
                      ,xcd.xhibit_etl_err_message = v_err_message
                WHERE  xcd.crest_court_id   =  p_crest_court_id
                  AND  xcd.cha_id = v_crest_chamber_id;
    
              END;
 
        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_chb_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_chb_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_chb_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_ref_chamber_crest- CTX-2186'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_REF_CHAMBER : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_CHAMBERS_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
               
                       -- Update XHBSTG_CHAMBERS_DM table for the rows NOT processed
            UPDATE xhbstg_chambers_dm xcd
            SET    xcd.xhibit_etl_date   = SYSDATE
                  ,xcd.xhibit_court_id   = v_xhibit_court_id
                  ,xcd.xhibit_enrich_date = SYSDATE
                  ,xcd.xhibit_etl_status = 'N' -- Not processed
            WHERE  xcd.crest_court_id   =  p_crest_court_id
              AND  xcd.xhibit_etl_status is NULL 
              AND  xcd.xhibit_enrich_date  is NULL
              AND  xcd.xhibit_etl_date  is NULL
              AND  xcd.xhibit_court_id is NULL;
           
              v_chb_np_rows := SQL%ROWCOUNT;
                         
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_ref_chamber_crest- CTX-2186'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_REF_CHAMBER : updating XHBSTG_CHAMBERS_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_chb_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2186:XHBSTG_CHAMBERS_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2186:XHBSTG_CHAMBERS_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_chb_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_ref_chamber_crest for CREST_COURT : '||p_crest_court_id||',crest_chamber_id : '||v_crest_chamber_id||' , v_chamber_id : '||v_chamber_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_ref_chamber_with_crest - CTX-2186'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_ref_chamber_crest - Error processing crest_chamber_id : '||v_crest_chamber_id||' , chamber_id : '||v_chamber_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

            COMMIT;          
                     
END upd_xhb_ref_chamber_crest;

/**
  * NAME       : upd_xhb_charges_log_crest
  * DESCRIPTION: CTX-2189 New CTX fields for XHB_CHARGES_LOG - Sec 4.3.2.8 req [4975.DM.011]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_charges_log_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;

 TYPE xhb_chg_rec IS RECORD
    (
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , case_no                      data_mig.xhbstg_committal_charge_dm.case_no%TYPE
    , case_type                    data_mig.xhbstg_committal_charge_dm.case_type%TYPE
    , case_id                      xhibit.xhb_case.case_id%TYPE
    , sequence_no                  xhibit.xhb_charges_log.sequence_no%TYPE
    , charges_info                 xhibit.xhb_charges_log.charges_info%TYPE
    );

    TYPE xhb_chg_type IS TABLE OF xhb_chg_rec;
    xhb_chg_tt  xhb_chg_type;

    CURSOR cur_crest_chg_details IS
    SELECT cchg.crest_court_id,
           xhc.court_id,
           cchg.case_no,
           cchg.case_type,
           xc.case_id,
           -- below statement to re-order the cch_id to transform it to sequence no starting from 1
           row_number() over (partition by cchg.case_no,cchg.case_type order by cchg.cch_id) sequence_no,
           cchg.charge_line
    from xhibit.xhb_court xhc,
         xhibit.xhb_case xc,
         data_mig.xhbstg_committal_charge_dm cchg
    where cchg.crest_court_id = p_crest_court_id and
          cchg.crest_court_id = xhc.crest_court_id and
          xc.court_id = xhc.court_id and
          xc.case_number = cchg.case_no and
          xc.case_type = cchg.case_type and
          NVL(cchg.xhibit_etl_status,'N') not in ('I', 'U') and
          cchg.xhibit_enrich_date is NULL  and
          NOT exists
          (select 'X' from xhibit.xhb_charges_log
            where case_id = xc.case_id and
                  nvl(obs_ind,'N') != 'Y');

    v_count_number_of_rows   NUMBER := 0;
    v_chg_upd_rows           NUMBER := 0;
    v_chg_upd_status         CHAR(1) := 'N';
    v_chg_np_rows            NUMBER := 0;
    v_case_id                xhibit.xhb_case.case_id%TYPE;
    v_case_no                data_mig.xhbstg_committal_charge_dm.case_no%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;

BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_charges_log_crest- CTX-2196'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CHARGES_LOG - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_CHARGES_LOG with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

      -- Here we are going to populate the rows in XHB_CHARGES_LOG table that have not yet been populated with CREST data
    OPEN cur_crest_chg_details;
    LOOP
    FETCH cur_crest_chg_details BULK COLLECT INTO xhb_chg_tt LIMIT g_bulk_limit;

    IF xhb_chg_tt IS NOT NULL AND xhb_chg_tt.COUNT > 0 THEN

        FOR i IN xhb_chg_tt.FIRST .. xhb_chg_tt.LAST LOOP
          BEGIN -- CTX3252 - BEGIN BLOCK for handling exception for each record and to continue processing

             insert_dm_log (p_crest_court_id     => p_crest_court_id
                           ,p_action_name        => 'upd_xhb_charges_log_crest- CTX-2196'
                           ,p_run_time           =>  sysdate
                           ,p_log_msg_type       =>  'I' -- Information
                           ,p_log_msg            =>  'UPDATING XHB_CHARGES_LOG - inserting  case id - '||xhb_chg_tt(i).case_id
                           ,p_err_row_count      =>  NULL
                           ,p_success_row_count  => NULL
                           ,p_last_updated_by    => 'DATA MIGRATION'
                           ,p_created_by         => 'DATA MIGRATION'
                           );
      DBMS_OUTPUT.PUT_LINE(' ');
      DBMS_OUTPUT.PUT_LINE('INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = '||xhb_chg_tt(i).crest_court_id);
  
              -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
              v_case_id     := xhb_chg_tt(i).case_id;
              v_case_no     := xhb_chg_tt(i).case_no;
               DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||', v_case_id : '||v_case_id||' , v_xhibit_court_id '||v_xhibit_court_id);
  
              IF LENGTH(xhb_chg_tt(i).charges_info)<80 THEN
                xhb_chg_tt(i).charges_info := xhb_chg_tt(i).charges_info || chr(10);
              END IF;
                   -- INSERT and populate this NEW table XHB_CHARGES_LOG with data from CREST
                               INSERT INTO  xhibit.xhb_charges_log
                                            ( charges_log_id
                                            , case_id
                                            , sequence_no
                                            , charges_info
                                            , creation_date
                                            , created_by
                                            , last_update_date
                                            , last_updated_by )
                                     VALUES
                                            (xhibit.xhb_charges_log_seq.nextval
                                            ,xhb_chg_tt(i).case_id
                                            ,xhb_chg_tt(i).sequence_no
                                            ,xhb_chg_tt(i).charges_info
                                            ,SYSDATE
                                            ,'DATA_MIGRATION'
                                            ,SYSDATE
                                            ,'DATA_MIGRATION');
          
          
          
          
              v_chg_upd_rows := SQL%ROWCOUNT;
              DBMS_OUTPUT.PUT_LINE(' ');
              DBMS_OUTPUT.PUT_LINE('CTX-2196:XHB_CHARGES_LOG - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_chg_upd_rows);
          
                         -- Update XHBSTG_COMMITTAL_CHARGE_DM table for the rows processed
                   IF v_chg_upd_rows > 0 THEN
                      v_chg_upd_status := 'I'; -- XHIBIT TABLE inserted
                   ELSIF v_chg_upd_rows = 0 THEN
                      v_chg_upd_status := 'N'; -- No Action Performed on XHIBIT table
                   END IF;
          
              insert_dm_log (p_crest_court_id     => p_crest_court_id
                                   ,p_action_name        => 'upd_xhb_charges_log_crest- CTX-2196'
                                   ,p_run_time           =>  sysdate
                                   ,p_log_msg_type       =>  'I' -- Information
                                   ,p_log_msg            =>  'UPDATING XHBSTG_COMMITTAL_CHARGE_DM - updated case_no '||xhb_chg_tt(i).case_no||', case_type '||xhb_chg_tt(i).case_type||' row with ETL_STATUS '||v_chg_upd_status
                                   ,p_err_row_count      =>  NULL
                                   ,p_success_row_count  => v_chg_upd_rows
                                   ,p_last_updated_by    => 'DATA MIGRATION'
                                   ,p_created_by         => 'DATA MIGRATION'
                                   );
          
          
             -- Update XHBSTG_COMMITTAL_CHARGE_DM table for the rows processed
              UPDATE xhbstg_committal_charge_dm xcg
              SET    xcg.xhibit_etl_date   = SYSDATE
                    ,xcg.xhibit_court_id   = v_xhibit_court_id
                    ,xcg.xhibit_enrich_date = SYSDATE
                    ,xcg.xhibit_etl_status = v_chg_upd_status
              WHERE  xcg.crest_court_id   =  p_crest_court_id
                AND  xcg.case_no = xhb_chg_tt(i).case_no;
  
                  DBMS_OUTPUT.PUT_LINE(' ');
                  DBMS_OUTPUT.PUT_LINE('CTX-2196:XHBSTG_COMMITTAL_CHARGE_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

              EXCEPTION
                  WHEN OTHERS THEN
                      v_err_message := SQLERRM;

                      DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_charges_log_crest for CREST_COURT : '||p_crest_court_id||',case_id : '||v_case_id||' , crest_case_no : '||v_case_no||'-'||SUBSTR(v_err_message,1,100));
              
                         insert_dm_log (p_crest_court_id     => p_crest_court_id
                                       ,p_action_name        => 'upd_xhb_charges_log_with_crest - CTX-2196'
                                       ,p_run_time           =>  sysdate
                                       ,p_log_msg_type       =>  'E' -- Error
                                       ,p_log_msg            =>  'upd_xhb_charges_log_crest - Error processing crest_case_no : '||v_case_no||' , case_id : '||v_case_id||' Error: '||SUBSTR(v_err_message,1,150)
                                       ,p_err_row_count      => NULL
                                       ,p_success_row_count  => v_count_number_of_rows
                                       ,p_last_updated_by    => 'DATA MIGRATION'
                                       ,p_created_by         => 'DATA MIGRATION'
                                       );
                       -- Update XHBSTG_CHARGES_LOG_DM
                       -- tables with error status/messages
                       BEGIN
                          UPDATE xhbstg_committal_charge_dm xcg
                          SET    xcg.xhibit_etl_date   = SYSDATE
                                ,xcg.xhibit_court_id   = v_xhibit_court_id
                                ,xcg.xhibit_enrich_date = SYSDATE
                                ,xcg.xhibit_etl_status = 'X' -- Error
                                ,xcg.xhibit_etl_err_message = v_err_message
                          WHERE  xcg.crest_court_id   =  p_crest_court_id
                            AND  xcg.case_no = v_case_no;
              
                        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing

        END;
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_chg_tt.COUNT;

    END IF;

    EXIT WHEN cur_crest_chg_details%NOTFOUND;

    END LOOP;


    CLOSE cur_crest_chg_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_charges_log_crest- CTX-2196'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CHARGES_LOG : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_COMMITTAL_CHARGE_DM
               ------ for given CREST COURT ID to 'N' i.e. NOT processed

                       -- Update XHBSTG_COMMITTAL_CHARGE_DM table for the rows NOT processed
            UPDATE xhbstg_committal_charge_dm xcg
            SET    xcg.xhibit_etl_date   = SYSDATE
                  ,xcg.xhibit_court_id   = v_xhibit_court_id
                  ,xcg.xhibit_enrich_date = SYSDATE
                  ,xcg.xhibit_etl_status = 'N' -- Not processed
            WHERE  xcg.crest_court_id   =  p_crest_court_id
              AND  xcg.xhibit_etl_status is NULL
              AND  xcg.xhibit_enrich_date  is NULL
              AND  xcg.xhibit_etl_date  is NULL
              AND  xcg.xhibit_court_id is NULL;

              v_chg_np_rows := SQL%ROWCOUNT;

             insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_charges_log_crest- CTX-2196'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CHARGES_LOG : updating XHBSTG_COMMITTAL_CHARGE_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_chg_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2196:XHBSTG_COMMITTAL_CHARGE_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2196:XHBSTG_COMMITTAL_CHARGE_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_chg_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
   
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; --CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_charges_log_crest for CREST_COURT : '||p_crest_court_id||',case_id : '||v_case_id||' , crest_case_no : '||v_case_no||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_charges_log_with_crest - CTX-2196'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_charges_log_crest - Error processing crest_case_no : '||v_case_no||' , case_id : '||v_case_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

            COMMIT;


END upd_xhb_charges_log_crest;


/**
  * NAME       : upd_xhb_case_history_crest
  * DESCRIPTION: CTX-2197 New CTX fields for XHB_CASE_HISTORY - Sec 4.3.2.11 req [4975.DM.014]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_case_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_cch_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , case_no                      data_mig.xhbstg_case_history_dm.case_no%TYPE
    , case_type                    data_mig.xhbstg_case_history_dm.case_type%TYPE
    , psd_ct_code                  data_mig.xhbstg_case_history_dm.psd_ct_code%TYPE
    , committal_date               data_mig.xhbstg_case_history_dm.comm_date%TYPE
    , reason_deleted               data_mig.xhbstg_case_history_dm.reason_deleted%TYPE
    , case_title                   data_mig.xhbstg_case_history_dm.case_title%TYPE
    , date_archived                data_mig.xhbstg_case_history_dm.date_archived%TYPE
    , sent_for_trial_date          data_mig.xhbstg_case_history_dm.sent_for_trial_date%TYPE
    );

    TYPE xhb_cch_type IS TABLE OF xhb_cch_rec;
    xhb_cch_tt  xhb_cch_type;
      
    CURSOR cur_crest_cch_details IS
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
    where cch.crest_court_id = p_crest_court_id and
          cch.crest_court_id = xhc.crest_court_id and 
          NVL(cch.xhibit_etl_status,'N') not in ('I', 'U') and
          cch.xhibit_enrich_date is NULL  and
          NOT exists 
          (select 'X' from xhibit.xhb_case_history
            where case_number = cch.case_no and 
                  case_type = cch.case_type and
                  court_id = xhc.court_id   and 
                  nvl(obs_ind,'N') != 'Y');  

    v_count_number_of_rows   NUMBER := 0;
    v_cch_upd_rows           NUMBER := 0;
    v_cch_upd_status         CHAR(1) := 'N';
    v_cch_np_rows            NUMBER := 0;
    v_case_no                data_mig.xhbstg_committal_charge_dm.case_no%TYPE;
    v_case_type              data_mig.xhbstg_committal_charge_dm.case_type%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_history_crest- CTX-2197'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_HISTORY - Starting process of inserting  rows from CREST CASE_HISTORY table'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting rows from CREST CASE_HISTORY table');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_CASE_HISTORY table that have not yet been populated with CREST data
    OPEN cur_crest_cch_details;
    LOOP
    FETCH cur_crest_cch_details BULK COLLECT INTO xhb_cch_tt LIMIT g_bulk_limit;
   
    IF xhb_cch_tt IS NOT NULL AND xhb_cch_tt.COUNT > 0 THEN
   
        FOR i IN xhb_cch_tt.FIRST .. xhb_cch_tt.LAST LOOP
    
          BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_history_crest- CTX-2197'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_CASE_HISTORY - inserting  case no - '||xhb_cch_tt(i).case_no||', case_type - '||xhb_cch_tt(i).case_type
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('INSERTING XHB_CASE_HISTORY - FOR CREST_COURT_ID = '||xhb_cch_tt(i).crest_court_id);
    
 -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_case_no     := xhb_cch_tt(i).case_no;
            v_case_type     := xhb_cch_tt(i).case_type;
             DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||', v_case_type : '||v_case_type||' , v_xhibit_court_id '||v_xhibit_court_id);         
            
         -- INSERT and populate this NEW table XHB_CASE_HISTORY with data from CREST  
                     INSERT INTO  xhibit.xhb_case_history
                                  ( case_history_id
                                  , case_number 
                                  , case_type   
                                  , psd_ct_code
                                  , committal_date
                                  , reason_deleted
                                  , case_title
                                  , date_archived
                                  , sent_for_trial_date
                                  , court_id   
                                  , creation_date 
                                  , created_by
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (xhibit.xhb_case_history_seq.nextval
                                  ,xhb_cch_tt(i).case_no
                                  ,xhb_cch_tt(i).case_type
                                  ,xhb_cch_tt(i).psd_ct_code
                                  ,xhb_cch_tt(i).committal_date
                                  ,xhb_cch_tt(i).reason_deleted
                                  ,xhb_cch_tt(i).case_title
                                  ,xhb_cch_tt(i).date_archived
                                  ,xhb_cch_tt(i).sent_for_trial_date
                                  ,xhb_cch_tt(i).court_id
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');      
                  


    
    v_cch_upd_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2197:XHB_CASE_HISTORY - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cch_upd_rows);
    
               -- Update XHBSTG_CASE_HISTORY_DM table for the rows processed
         IF v_cch_upd_rows > 0 THEN 
            v_cch_upd_status := 'I'; -- XHIBIT TABLE inserted
         ELSIF v_cch_upd_rows = 0 THEN
            v_cch_upd_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;  

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_history_crest- CTX-2197'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_CASE_HISTORY_DM - updated case_no '||xhb_cch_tt(i).case_no||', case_type '||xhb_cch_tt(i).case_type||' row with ETL_STATUS '||v_cch_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_cch_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         

           -- Update XHBSTG_CASE_HISTORY_DM table for the rows processed
            UPDATE xhbstg_case_history_dm xcg
            SET    xcg.xhibit_etl_date   = SYSDATE
                  ,xcg.xhibit_court_id   = v_xhibit_court_id
                  ,xcg.xhibit_enrich_date = SYSDATE
                  ,xcg.xhibit_etl_status = v_cch_upd_status
            WHERE  xcg.crest_court_id   =  p_crest_court_id
              AND  xcg.case_no = xhb_cch_tt(i).case_no
              AND  xcg.case_type = xhb_cch_tt(i).case_type;     

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2197:XHBSTG_CASE_HISTORY_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

        EXCEPTION
            WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_case_history_crest for CREST_COURT : '||p_crest_court_id||',case_no : '||v_case_no||' , crest_case_type : '||v_case_type||'-'||SUBSTR(v_err_message,1,100));
        
                   insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                 ,p_action_name        => 'upd_xhb_case_history_with_crest - CTX-2197'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'upd_xhb_case_history_crest - Error processing crest_case_no : '||v_case_no||' , case_type : '||' Error: '||SUBSTR(v_err_message,1,150)
                                 ,p_err_row_count      => NULL 
                                 ,p_success_row_count  => v_count_number_of_rows
                                 ,p_last_updated_by    => 'DATA MIGRATION'
                                 ,p_created_by         => 'DATA MIGRATION'
                                 );
                 -- Update XHBSTG_CASE_HISTORY_DM
                 -- tables with error status/messages
                 BEGIN
                    UPDATE xhbstg_case_history_dm xcg
                    SET    xcg.xhibit_etl_date   = SYSDATE
                          ,xcg.xhibit_court_id   = v_xhibit_court_id
                          ,xcg.xhibit_enrich_date = SYSDATE
                          ,xcg.xhibit_etl_status = 'X' -- Error
                          ,xcg.xhibit_etl_err_message = v_err_message
                    WHERE  xcg.crest_court_id   =  p_crest_court_id
                      AND  xcg.case_no = v_case_no
                      AND  xcg.case_type = v_case_type;
        
                  END;
                    

        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_cch_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_cch_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_cch_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_history_crest- CTX-2197'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_HISTORY : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_CASE_HISTORY_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
               
                       -- Update XHBSTG_CASE_HISTORY_DM table for the rows NOT processed
            UPDATE xhbstg_case_history_dm xcg
            SET    xcg.xhibit_etl_date   = SYSDATE
                  ,xcg.xhibit_court_id   = v_xhibit_court_id
                  ,xcg.xhibit_enrich_date = SYSDATE
                  ,xcg.xhibit_etl_status = 'N' -- Not processed
            WHERE  xcg.crest_court_id   =  p_crest_court_id
              AND  xcg.xhibit_etl_status is NULL 
              AND  xcg.xhibit_enrich_date  is NULL
              AND  xcg.xhibit_etl_date  is NULL
              AND  xcg.xhibit_court_id is NULL;
           
              v_cch_np_rows := SQL%ROWCOUNT;
                         
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_history_crest- CTX-2197'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_HISTORY : updating XHBSTG_CASE_HISTORY_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_cch_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2197:XHBSTG_CASE_HISTORY_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2197:XHBSTG_CASE_HISTORY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_cch_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK;-- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_case_history_crest for CREST_COURT : '||p_crest_court_id||',case_no : '||v_case_no||' , crest_case_type : '||v_case_type||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_history_with_crest - CTX-2197'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_case_history_crest - Error processing crest_case_no : '||v_case_no||' , case_type : '||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

        COMMIT;

          
                     
END upd_xhb_case_history_crest;

/**
  * NAME       : upd_xhb_def_history_crest
  * DESCRIPTION: CTX-2580 New CTX fields for XHB_DEFENDANT_HISTORY - Sec 4.3.2.27 req [4975.DM.030]
  *              CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_def_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_cdh_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , defendant_id                 xhibit.xhb_defendant.defendant_id%TYPE
    , crest_defendant_id           data_mig.xhbstg_subject_history_dm.sub_id%TYPE
    , surname                      data_mig.xhbstg_subject_history_dm.surname%TYPE
    , first_name                   data_mig.xhbstg_subject_history_dm.forename1%TYPE
    , middle_name                  data_mig.xhbstg_subject_history_dm.forename2%TYPE
    , date_of_birth                data_mig.xhbstg_subject_history_dm.dob%TYPE
    , gender                       data_mig.xhbstg_subject_history_dm.sex%TYPE
    , reason_deleted               xhibit.xhb_defendant_history.reason_deleted%TYPE
    , date_archived                xhibit.xhb_defendant_history.date_archived%TYPE
    );

    TYPE xhb_cdh_type IS TABLE OF xhb_cdh_rec;
    xhb_cdh_tt  xhb_cdh_type;
      
    CURSOR cur_crest_cdh_details IS
   SELECT cdh.crest_court_id,
           xhc.court_id,
           cdh.sub_id as defendant_id,
           cdh.sub_id as crest_defendant_id,
           cdh.surname,
           cdh.forename1,
           cdh.forename2,
           cdh.dob,
           decode(cdh.sex,'M',1,'F',2,'C',0,9) sex,
           'DATA_MIGRATION-CREST DATA' as reason_deleted,
           SYSDATE as date_archived
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_subject_history_dm cdh
    where cdh.crest_court_id = p_crest_court_id and
          cdh.crest_court_id = xhc.crest_court_id and 
          NVL(cdh.xhibit_etl_status,'N') not in ('I', 'U') and
          cdh.xhibit_enrich_date is NULL  and
          NOT exists 
          (select 'X' from xhibit.xhb_defendant_history
            where crest_defendant_id = cdh.sub_id and 
                  court_id = xhc.court_id   and 
                  nvl(obs_ind,'N') != 'Y');  

    v_count_number_of_rows   NUMBER := 0;
    v_cdh_upd_rows           NUMBER := 0;
    v_cdh_upd_status         CHAR(1) := 'N';
    v_cdh_np_rows            NUMBER := 0;
    v_sub_id                 data_mig.xhbstg_subject_history_dm.sub_id%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_def_history_crest- CTX-2580'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DEFENDANT_HISTORY - Starting process of inserting  rows from CREST SUBJECT_HISTORY table'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting rows from CREST SUBJECT_HISTORY table');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_DEFENDANT_HISTORY table that have not yet been populated with CREST data
    OPEN cur_crest_cdh_details;
    LOOP
    FETCH cur_crest_cdh_details BULK COLLECT INTO xhb_cdh_tt LIMIT g_bulk_limit;
   
    IF xhb_cdh_tt IS NOT NULL AND xhb_cdh_tt.COUNT > 0 THEN
   
        FOR i IN xhb_cdh_tt.FIRST .. xhb_cdh_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_def_history_crest- CTX-2580'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'UPDATING XHB_DEFENDANT_HISTORY - inserting  crest sub_id - '||xhb_cdh_tt(i).defendant_id
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
                             
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_DEFENDANT_HISTORY - FOR CREST_COURT_ID = '||xhb_cdh_tt(i).crest_court_id);
    
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_sub_id     := xhb_cdh_tt(i).defendant_id;
                
                DBMS_OUTPUT.PUT_LINE('v_sub_id : '||v_sub_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
                
                -- INSERT and populate this NEW table XHB_DEFENDANT_HISTORY with data from CREST  
                INSERT INTO  xhibit.xhb_defendant_history
                            ( defendant_history_id
                            , defendant_id 
                            , crest_defendant_id   
                            , court_id
                            , surname
                            , first_name
                            , middle_name
                            , date_of_birth
                            , gender 
                            , reason_deleted
                            , date_archived
                            , creation_date 
                            , created_by
                            , last_update_date 
                            , last_updated_by )
                     VALUES 
                            (xhibit.xhb_defendant_history_seq.nextval
                            ,xhb_cdh_tt(i).defendant_id
                            ,xhb_cdh_tt(i).crest_defendant_id
                            ,xhb_cdh_tt(i).court_id                                  
                            ,xhb_cdh_tt(i).surname
                            ,xhb_cdh_tt(i).first_name
                            ,xhb_cdh_tt(i).middle_name
                            ,xhb_cdh_tt(i).date_of_birth
                            ,xhb_cdh_tt(i).gender    
                            ,xhb_cdh_tt(i).reason_deleted                                                                
                            ,xhb_cdh_tt(i).date_archived
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,'DATA_MIGRATION');      
    
                v_cdh_upd_rows := SQL%ROWCOUNT;
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2580:XHB_DEFENDANT_HISTORY - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cdh_upd_rows);
                
                -- Update XHBSTG_SUBJECT_HISTORY_DM table for the rows processed
                IF v_cdh_upd_rows > 0 THEN 
                  v_cdh_upd_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_cdh_upd_rows = 0 THEN
                  v_cdh_upd_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_def_history_crest- CTX-2580'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_SUBJECT_HISTORY_DM - updated sub_id '||xhb_cdh_tt(i).defendant_id||' row with ETL_STATUS '||v_cdh_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_cdh_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
                
                -- Update XHBSTG_SUBJECT_HISTORY_DM table for the rows processed
                UPDATE xhbstg_subject_history_dm xsh
                SET    xsh.xhibit_etl_date   = SYSDATE
                      ,xsh.xhibit_court_id   = v_xhibit_court_id
                      ,xsh.xhibit_enrich_date = SYSDATE
                      ,xsh.xhibit_etl_status = v_cdh_upd_status
                WHERE  xsh.crest_court_id   =  p_crest_court_id
                AND  xsh.sub_id = xhb_cdh_tt(i).defendant_id;
                                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2580:XHBSTG_SUBJECT_HISTORY_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

        EXCEPTION
            WHEN OTHERS THEN

                v_err_message := SQLERRM;
                
                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_def_history_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||'-'||SUBSTR(v_err_message,1,120));
                
                 insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'upd_xhb_def_history_with_crest - CTX-2580'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'E' -- Error
                               ,p_log_msg            =>  'upd_xhb_def_history_crest - Error processing crest sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                               ,p_err_row_count      => NULL 
                               ,p_success_row_count  => v_count_number_of_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
                               
                -- Update XHBSTG_SUBJECT_HISTORY_DM
                -- tables with error status/messages
                BEGIN
                  UPDATE xhbstg_subject_history_dm xsh
                  SET    xsh.xhibit_etl_date   = SYSDATE
                        ,xsh.xhibit_court_id   = v_xhibit_court_id
                        ,xsh.xhibit_enrich_date = SYSDATE
                        ,xsh.xhibit_etl_status = 'X' -- Error
                        ,xsh.xhibit_etl_err_message = v_err_message
                  WHERE  xsh.crest_court_id   =  p_crest_court_id
                    AND  xsh.sub_id = v_sub_id;
                END;


        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_cdh_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_cdh_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_cdh_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_def_history_crest- CTX-2580'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DEFENDANT_HISTORY : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_SUBJECT_HISTORY_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
               
                       -- Update XHBSTG_SUBJECT_HISTORY_DM table for the rows NOT processed
            UPDATE xhbstg_subject_history_dm xsh
            SET    xsh.xhibit_etl_date   = SYSDATE
                  ,xsh.xhibit_court_id   = v_xhibit_court_id
                  ,xsh.xhibit_enrich_date = SYSDATE
                  ,xsh.xhibit_etl_status = 'N' -- Not processed
            WHERE  xsh.crest_court_id   =  p_crest_court_id
              AND  xsh.xhibit_etl_status is NULL 
              AND  xsh.xhibit_enrich_date  is NULL
              AND  xsh.xhibit_etl_date  is NULL
              AND  xsh.xhibit_court_id is NULL;
           
              v_cdh_np_rows := SQL%ROWCOUNT;
                         
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_def_history_crest- CTX-2580'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DEFENDANT_HISTORY : updating XHBSTG_SUBJECT_HISTORY_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_cdh_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2580:XHBSTG_SUBJECT_HISTORY_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2580:XHBSTG_SUBJECT_HISTORY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_cdh_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
    
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN

        v_err_message := SQLERRM;

        --ROLLBACK; --CTX-3252

        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_def_history_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||'-'||SUBSTR(v_err_message,1,120));
        
        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_def_history_with_crest - CTX-2580'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_def_history_crest - Error processing crest sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_count_number_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
        
        COMMIT;
                     
END upd_xhb_def_history_crest;

/**
  * NAME       : upd_xhb_c_list_entry_crest
  * DESCRIPTION: CTX-2198 New CTX fields for XHB_CASE_LISTING_ENTRY - 
  *        Cases newly migrated from CREST - Sec 4.3.2.12 req [4975.DM.015]
  *        Cases already existing in XHIBIT and not in CASE_LISTING_ENTRY - Sec 4.3.2.12 req [4975.DM.016]
  * DESCRIPTION: CTX-2201 New CTX fields for XHB_DIARY_NOTE_ENTRY - 
  *        Default List Notes from CASE table newly migrated from CREST - Sec 4.3.2.16 req [4975.DM.020]
  *        CTX-2991 Other Notes from CASE_NOTE table - Sec 4.3.2.16 req [4975.DM.021]
  *         req [4975.DM.21A] trigger modification included in deployment
  *        CTX-3277 Fixed XHB_DIARY_NOTE_ENTRY court_id
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * 11/12/2018 : S Sethuraman - CTX-3252 - Exception Handling moved inside loop to continue processing without RollBack
  * 04/01/2019 : S Sethuraman - CTX-3375 - RESTRICT DIARY_NOTE_ENTRY creation for NON-NULL DEFAULT_LIST_NOTES only
  * 26/02/2019     S Sethuraman : CTX-3771 - UPD_XHB_C_LIST_ENTRY_CREST - add CREST_COURT_ID  mapping in SUB QUERY in BOTH CURSORS
  * 07/03/2019 : S Sethuraman : CTX-3829 - LODR Reports - update DIARY_NOTE_ENTRY as per FS V0.31 changes
  * 10/06/2019  : S Sethuraman : Mold Duplicate CREST XHBSTG_RELEASE_JUDGE_DM - add DISTINCT in sub query when referring
  * 25/06/2019  : S Sethuraman : CTX-CASE_NOTE Ordering issue - CAN_ID added to systimestamp and appended to note_date
**/
PROCEDURE upd_xhb_c_list_entry_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_cle_ins_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , case_id                      xhibit.xhb_case.case_id%TYPE
    , case_no                      xhibit.xhb_case.case_number%TYPE
    , case_type                    xhibit.xhb_case.case_type%TYPE
    , ref_judge_type_id            xhibit.xhb_case_listing_entry.ref_judge_type_id%TYPE
    , court_site_id                xhibit.xhb_case_listing_entry.court_site_id%TYPE
    , judge_id                     xhibit.xhb_case_listing_entry.judge_id%TYPE
    , note_type_id                 xhibit.xhb_diary_note_entry.note_type_id%TYPE
    , note_classification_id       xhibit.xhb_diary_note_entry.note_classification_id%TYPE
    , diary_note_text              xhibit.xhb_diary_note_entry.diary_note_text%TYPE
    , diary_note_pre_defined_id    xhibit.xhb_diary_note_entry.diary_note_pre_defined_id%TYPE
    , diary_date                   xhibit.xhb_diary_note_entry.diary_date%TYPE
    );

 TYPE xhb_ccn_ins_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , can_id                       xhbstg_case_note_dm.can_id%TYPE
    , case_id                      xhibit.xhb_case.case_id%TYPE
    , case_listing_entry_id        xhibit.xhb_case_listing_entry.case_listing_entry_id%TYPE
    , case_no                      xhibit.xhb_case.case_number%TYPE
    , case_type                    xhibit.xhb_case.case_type%TYPE
    , note_type_id                 xhibit.xhb_diary_note_entry.note_type_id%TYPE
    , note_classification_id       xhibit.xhb_diary_note_entry.note_classification_id%TYPE
    , diary_note_text              xhibit.xhb_diary_note_entry.diary_note_text%TYPE
    , diary_note_pre_defined_id    xhibit.xhb_diary_note_entry.diary_note_pre_defined_id%TYPE
    , diary_date                   xhibit.xhb_diary_note_entry.diary_date%TYPE
    , creation_date                xhibit.xhb_diary_note_entry.creation_date%TYPE -- CTX-CASE_NOTE ORDERING ISSUE
    );

    TYPE xhb_cle_ins_type IS TABLE OF xhb_cle_ins_rec;
    xhb_cle_ins_tt  xhb_cle_ins_type;
      
   TYPE xhb_cle_xins_type IS TABLE OF xhb_cle_ins_rec;
    xhb_cle_xins_tt  xhb_cle_xins_type;
   
   TYPE xhb_den_ins_type IS TABLE OF xhb_ccn_ins_rec;
    xhb_ccn_xins_tt    xhb_den_ins_type;
    
   -- CTX-2991 Create a list to hold case_no and case_type processed to be used later to fetch data to insert into XHB_DIARY_NOTE_ENTRY in a single loop
   -- when processing data taken from the XHBSTG_CASE_NOTE_DM table.
   TYPE v_case_details_rec IS RECORD
   ( 
     case_no            xhibit.xhb_case.case_number%TYPE
   , case_type          xhibit.xhb_case.case_type%TYPE
   );
   TYPE v_case_details_type IS TABLE OF v_case_details_rec;
   v_case_details_tt  v_case_details_type;
    
    -- Select active cases from XHB_CASE  for the CREST_COURT_ID  where there is no
    -- entry existing in XHB_CASE_LISTING_ENTRY table
    -- this includes both data migrated from CREST and also existing CASES
    -- these have to be INSERTED
    --ALSO to note xhbstg_case_dm id referred for ref_judge_type ONLY (which is read only) and
    -- INCLUDES ETL SUCCESS ROWS only as checking for sucessfully migrated CREST CASES
    -- AND no updates to XHBSTG_CASE_DM  required for ETL UPDATE STATUS
    --  below query for data migrated from CREST
   CURSOR cur_crest_cle_ins_details IS
   SELECT  p_crest_court_id crest_court_id,
           xhc.court_id,
           xc.case_id,
           xcc.case_no,
           xcc.case_type,
           (SELECT xrs.ref_system_code_id
            FROM   xhibit.xhb_ref_system_code xrs
            WHERE  xrs.code_type = 'JUDGE_TYPE'
            AND    xrs.code      = xcc.req_judge_type
            AND    xrs.court_id  = xhc.court_id
            AND    NVL(xrs.obs_ind,'N') != 'Y'
           ) as ref_judge_type_id,
           NULL as court_site_id,
           (SELECT DISTINCT xrj.ref_judge_id 
            FROM xhbstg_release_judge_dm rj 
               , xhibit.xhb_ref_judge xrj 
            WHERE rj.crest_court_id = xcc.crest_court_id -- CTX-3771
            AND  rj.req_jud_ind = 'Q' 
            AND rj.case_no = xc.case_number  
            AND rj.case_type = xc.case_type  
            AND rj.jud_id = xrj.crest_judge_id  
            AND xrj.court_id = xc.court_id  
            AND NVL(xrj.obs_ind,'N') != 'Y'
           ) as judge_id,
           (SELECT xrl.ref_listing_data_id
            FROM   xhibit.xhb_ref_listing_data xrl
            WHERE  xrl.ref_data_type = 'NOTE_TYPE'
            AND    xrl.ref_data_value = 'DCN'
            AND    NVL(xrl.obs_ind,'N') != 'Y'
           ) as note_type_id,
           NULL as note_classification_id,
           xcc.default_list_note diary_note_text,
           NULL as diary_note_pre_defined_id,
           NULL as diary_date
    from xhibit.xhb_court xhc,
         xhibit.xhb_case xc,
         xhbstg_case_dm xcc
    where xcc.crest_court_id = p_crest_court_id and 
          xhc.crest_court_id = xcc.crest_court_id and
          xhc.court_id = xc.court_id and
          xcc.case_no = xc.case_number and
          xcc.case_type = xc.case_type and 
          NVL(xcc.xhibit_etl_status,'N') in ('M', 'U') and -- MIGRATED CREST CASES ONLY SELECTED
          xcc.xhibit_enrich_date is NOT NULL 
          AND NOT exists 
                      (select 'X' from xhibit.xhb_case_listing_entry
                        where court_id = xc.court_id and 
                              case_id = xc.case_id)
          ;

          
        --  below query for data already EXISTING in XHB_CASE but NOT in XHB_CASE_LISTING_ENTRY              
    CURSOR cur_xhibit_cle_ins_details IS
    SELECT  p_crest_court_id crest_court_id,
           xhc.court_id,
           xc.case_id,
           xc.case_number,
           xc.case_type,
           NULL as ref_judge_type_id,
           xc.court_id_receiving_site as court_site_id,
          (SELECT distinct xrj.ref_judge_id
           FROM  xhbstg_release_judge_dm rj 
               , xhibit.xhb_ref_judge xrj 
           WHERE rj.crest_court_id = xhc.crest_court_id -- CTX-3771
           AND   rj.req_jud_ind = 'Q' 
           AND rj.case_no = xc.case_number 
           AND rj.case_type = xc.case_type 
           AND rj.jud_id = xrj.crest_judge_id 
           AND xrj.court_id = xc.court_id  
           AND NVL(xrj.obs_ind,'N') != 'Y' ) as judge_id,
           NULL as note_type_id,
           NULL as note_classification_id,
           NULL as diary_note_text,
           NULL as diary_note_pre_defined_id,
           NULL as diary_date
    from xhibit.xhb_court xhc,
         xhibit.xhb_case xc
    where xhc.crest_court_id = p_crest_court_id and 
          xhc.court_id = xc.court_id 
         AND NOT exists (select 'X' 
                        from xhibit.xhb_case_listing_entry
                        where court_id = xc.court_id 
                        and case_id = xc.case_id)
        ;  


     -- CTX-2201 - DIARY_NOTE_ENTRY - FROM OTHER NOTES I.E. XHBSTG_CASE_NOTE_DM Migration
     -- CTX-3829 - UPDATE OBS_IND as N, populate XHIBIT_COURT_ID when NOTE_TYPE = 'D'
     -- CTX-3829 - UPDATE C_LIST_ENRTY_ID ONLY when CASE_NOTE.NOTE_TYPE = 'H'
     -- CTX-3829 - POPULATE CASE_ID WHEN CASE_NO IS NOT NULL AND NOTE_TYPE IN (D,C)
    CURSOR cur_crest_case_note_det (l_court_id  IN xhibit.xhb_court.crest_court_id%TYPE,
                                    l_case_no   IN xhibit.xhb_Case.case_number%TYPE,
                                    l_case_type IN xhibit.xhb_Case.case_type%TYPE) 
      IS
      SELECT xcn.crest_court_id,
             decode(upper(xcn.note_type),'D',xhc.court_id,NULL) court_id, -- CTX-3829
             xcn.can_id,
             xc.case_id,
             decode(upper(xcn.note_type),'H',xcle.case_listing_entry_id,NULL) case_listing_entry_id, -- CTX-3829
             xcn.case_no,
             xcn.case_type,
             (SELECT xrl.ref_listing_data_id
                FROM   xhibit.xhb_ref_listing_data xrl
                WHERE  xrl.ref_data_type = 'NOTE_TYPE'
                  AND    xrl.ref_data_value = decode(UPPER(xcn.note_type),'C','CN','H','HN','D','GDN') --cc10102018 included the UPPER
                  AND    NVL(xrl.obs_ind,'N') != 'Y'
               ) as note_type_id,
              (SELECT xrl.ref_listing_data_id
                FROM   xhibit.xhb_ref_listing_data xrl
                WHERE  xrl.ref_data_type = 'NOTE_CLASSIFICATION'
                  AND    xrl.ref_data_value = decode(UPPER(xcn.note_print_ind), 'S', 'Standard', 'P', 'Priority', 'R', 'Restricted') --cc10102018 included the UPPER
                  AND    NVL(xrl.obs_ind,'N') != 'Y'
               ) as note_classification_id,
              xcn.note as diary_note_text,
               NULL as diary_note_pre_defined_id,
              xcn.diary_date as diary_date,
           --   xcn.note_date -- CTX-CASE_NOTE Issue 
        xcn.note_date+numtodsinterval(row_number() over( partition by note_date order by can_id),'MINUTE') as creation_date           
         FROM data_mig.xhbstg_case_note_dm   xcn,
               xhibit.xhb_case xc,
               xhibit.xhb_court xhc,
               xhibit.xhb_case_listing_entry xcle
        WHERE xcn.crest_court_id = l_court_id -- crest_court_id matched
          AND xcn.crest_court_id = xhc.crest_court_id
          AND xhc.court_id = xc.court_id
          AND xcle.court_id = xc.court_id
          AND xcle.case_id = xc.case_id
          AND xcn.case_no = xc.case_number 
          AND xcn.case_type = xc.case_type
          AND xcn.case_no = l_case_no
          AND xcn.case_type = l_case_type
          AND NVL(xcn.xhibit_etl_status,'N') not in ('I','U' )
          AND xcn.xhibit_enrich_date is NULL
          ;
          
          
    v_count_number_of_rows   NUMBER := 0;  -- MIGRATED COUNT
    v_count_ccn_no_of_rows   NUMBER := 0;  -- CASE NOTES CREST MIGRATED COUNT
    v_xcount_ccn_no_of_rows   NUMBER := 0;  -- CASE NOTES CREST MIGRATED COUNT
    v_xcount_number_of_rows   NUMBER := 0; -- NON-MIGRATED COUNT
    v_cle_ins_rows           NUMBER := 0;
    v_ccn_ins_rows           NUMBER := 0;
    v_cle_ins_status         CHAR(1) := 'N';
    v_ccn_ins_status         CHAR(1) := 'N';
    v_ccn_np_rows            NUMBER := 0;
    v_case_no                xhbstg_case_dm.case_no%TYPE;
    v_c_list_id              xhibit.xhb_case_listing_entry.case_listing_entry_id%TYPE;
    v_case_type              xhbstg_case_dm.case_type%TYPE;
    v_case_id                xhibit.xhb_Case.case_id%TYPE;
    v_can_id                 xhbstg_case_note_dm.can_id%TYPE := NULL;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

    v_case_details_tt := v_case_details_type();
    
  -- FIRST INSERT CREST MIGRATED CASES WHICH ARE NOT IN XHB_CASE_LISTING_ENTRY TABLE
  
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_LISTING_ENTRY - Starting process of inserting  rows for every active case in XHB_CASE - CREST MIGRATED data First'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting  rows for every active case in XHB_CASE - CREST MIGRATED data First');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_CASE_LISTING_ENTRY table
      -- that have not yet been populated with CREST data
    OPEN cur_crest_cle_ins_details;
    LOOP
    FETCH cur_crest_cle_ins_details BULK COLLECT INTO xhb_cle_ins_tt LIMIT g_bulk_limit;
   
    IF xhb_cle_ins_tt IS NOT NULL AND xhb_cle_ins_tt.COUNT > 0 THEN
  
        FOR i IN xhb_cle_ins_tt.FIRST .. xhb_cle_ins_tt.LAST LOOP

         BEGIN -- CTX-3252
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'MIGRATED XHB_CASE_LISTING_ENTRY - inserting  crest case_no - '||xhb_cle_ins_tt(i).case_no||', case_type : '||xhb_cle_ins_tt(i).case_type
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         ); 
  --  DBMS_OUTPUT.PUT_LINE(' ');
  --  DBMS_OUTPUT.PUT_LINE('INSERTING XHB_CASE_LISTING_ENTRY - FOR CREST_COURT_ID = '||xhb_cle_ins_tt(i).crest_court_id);
    
            -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_case_no     := xhb_cle_ins_tt(i).case_no;
            v_case_type   := xhb_cle_ins_tt(i).case_type;
            v_case_id     := xhb_cle_ins_tt(i).case_id;
             DBMS_OUTPUT.PUT_LINE('v_case_id : '||v_case_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||' , v_xhibit_court_id '||v_xhibit_court_id);         
           
 
           begin
               select xhibit.xhb_case_listing_entry_seq.nextval into v_c_list_id from dual;
           end; 
         -- INSERT and populate this NEW table XHB_CASE_LISTING_ENTRY with data from CREST  
                     INSERT INTO  xhibit.xhb_case_listing_entry
                                  ( case_listing_entry_id
                                  , case_id 
                                  , ref_judge_type_id   
                                  , court_site_id
                                  , court_id
                                  , judge_id
                                  , creation_date 
                                  , created_by
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (v_c_list_id
                                  ,xhb_cle_ins_tt(i).case_id
                                  ,xhb_cle_ins_tt(i).ref_judge_type_id
                                  ,xhb_cle_ins_tt(i).court_site_id                                  
                                  ,xhb_cle_ins_tt(i).court_id
                                  ,xhb_cle_ins_tt(i).judge_id
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');      

    
    v_cle_ins_rows := SQL%ROWCOUNT;
 --   DBMS_OUTPUT.PUT_LINE(' ');
 --   DBMS_OUTPUT.PUT_LINE('CTX-2198:XHB_CASE_LISTING_ENTRY - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cle_ins_rows);
    
    -- UPDATE to XHB_CASE_DM table NOT REQUIRED as the REF_JUDGE_TYPE column only is referred
    -- AND XHB_CASE ETL Procedure whould have updated the ACTUAL CASE ETL status
         IF v_cle_ins_rows > 0 THEN 
            v_cle_ins_status := 'I'; -- XHIBIT TABLE inserted
         ELSIF v_cle_ins_rows = 0 THEN
            v_cle_ins_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;  

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'MIGRATED - NO OF RECORDS PROCESSED FROM XHBSTG_CASE_DM - case_no '||xhb_cle_ins_tt(i).case_no||', case_type '||xhb_cle_ins_tt(i).case_type||' row with ETL_STATUS '||v_cle_ins_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_cle_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                             

       --         DBMS_OUTPUT.PUT_LINE(' ');
       --         DBMS_OUTPUT.PUT_LINE('CTX-2198:XHBSTG_CASE_DM - Crest Court : '||p_crest_court_id||' - Processed no of rows : '||SQL%ROWCOUNT);
 

       -- CTX_3375 - Restrict diary_note_entry population only where XHBSTG_CASE_DM.default_list_note is populated     

             IF  xhb_cle_ins_tt(i).diary_note_text is NOT NULL THEN     -- CTX-3375     

       --           DBMS_OUTPUT.PUT_LINE('CTX-2201:XHB_DIARY_NOTE_ENTRY - Inserting v_Case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_c_list_id : '||v_c_list_id||', v_xhibit_court_id '||v_xhibit_court_id);                

           -- INSERT and populate this NEW table XHB_DIARY_ENTRY_NOTE with data from CREST XHBSTG_CASE_DM table
                     INSERT INTO  xhibit.xhb_diary_note_entry
                                  ( diary_note_entry_id
                                  , case_listing_entry_id
                                  , note_type_id 
                                  , note_classification_id   
                                  , diary_note_text
                                  , diary_note_pre_defined_id
                                  , diary_date
                                  , court_id
                                  , creation_date 
                                  , created_by
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (xhibit.xhb_diary_note_entry_seq.nextval
                                  ,v_c_list_id
                                  ,xhb_cle_ins_tt(i).note_type_id
                                  ,xhb_cle_ins_tt(i).note_classification_id                                  
                                  ,xhb_cle_ins_tt(i).diary_note_text
                                  ,xhb_cle_ins_tt(i).diary_note_pre_defined_id
                                  ,xhb_cle_ins_tt(i).diary_date                                                                                                      
                                  ,null
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');       

                 v_ccn_ins_rows := SQL%ROWCOUNT;
       --          DBMS_OUTPUT.PUT_LINE(' ');
       --          DBMS_OUTPUT.PUT_LINE('CTX-2201:XHB_DIARY_NOTE_ENTRY - DEFAULT CASE NOTES Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_ccn_ins_rows);

                 -- set rows processed status
                IF v_ccn_ins_rows > 0 THEN 
                   v_ccn_ins_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_ccn_ins_rows = 0 THEN
                   v_ccn_ins_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTED INTO XHB_DIARY_NOTE_ENTRY - DEFAULT CASE NOTES - crest_case_no '||v_case_no||', case_type '||v_case_type||', v_c_list_id : '||v_c_list_id||' row with ETL_STATUS '||v_ccn_ins_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_ccn_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
           
          END IF;  -- CTX_3375
          
           -- Store the case_no and case_type to be used later to insert into xhibit.xhb_diary_note_entry for data being migrated from xhbstg_case_note_dm table          
           v_case_details_tt.EXTEND; 
           v_case_details_tt(v_case_details_tt.LAST).case_no   := v_case_no;
           v_case_details_tt(v_case_details_tt.LAST).case_type := v_case_type;
                  
             v_count_ccn_no_of_rows := v_count_ccn_no_of_rows + v_ccn_ins_rows;
  EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
      --  ROLLBACK; CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_c_listing_entry_crest for CREST_COURT : '||p_crest_court_id||',case_no : '||v_case_no||',case_id : '||v_case_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_with_crest - CTX-2198,2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_c_list_entry_crest - Error processing case_id : '||v_case_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows+v_xcount_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
          END; -- CTX-3252    
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_cle_ins_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_cle_ins_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_cle_ins_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_LISTING_ENTRY : Inserted CREST MIGRATED ROWS - '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2198:XHB_CASE_LISTING_ENTRY inserted CREST MIGRATED RECORDS - '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
  
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_LISTING_ENTRY - Starting process of inserting  rows for every active case in XHB_CASE - ALREADY EXISTING IN XHB_CASE and NOT MIGRATED NOW'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting  rows for every active case in XHB_CASE - ALREADY EXISTING IN XHB_CASE and NOT MIGRATED NOW');

      -- Here we are going to populate the rows in XHB_CASE_LISTING_ENTRY table
      -- that have not yet been populated  - EXISTING CASES for this COURT
    OPEN cur_xhibit_cle_ins_details;
    LOOP
    FETCH cur_xhibit_cle_ins_details BULK COLLECT INTO xhb_cle_xins_tt LIMIT g_bulk_limit;
   
    IF xhb_cle_xins_tt IS NOT NULL AND xhb_cle_xins_tt.COUNT > 0 THEN
      
        FOR i IN xhb_cle_xins_tt.FIRST .. xhb_cle_xins_tt.LAST LOOP
         BEGIN -- CTX-3252
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'NON-MIGRATED XHB_CASE_LISTING_ENTRY - inserting  crest case_no - '||xhb_cle_xins_tt(i).case_no||', case_type : '||xhb_cle_xins_tt(i).case_type
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         ); 
 --   DBMS_OUTPUT.PUT_LINE(' ');
 --   DBMS_OUTPUT.PUT_LINE('INSERTING XHB_CASE_LISTING_ENTRY - FOR CREST_COURT_ID = '||xhb_cle_xins_tt(i).crest_court_id);
    
 -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_case_no     := xhb_cle_xins_tt(i).case_no;
            v_case_type   := xhb_cle_xins_tt(i).case_type;
            v_case_id     := xhb_cle_xins_tt(i).case_id;
             DBMS_OUTPUT.PUT_LINE('v_case_id : '||v_case_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||' , v_xhibit_court_id '||v_xhibit_court_id);         
            
         -- INSERT and populate this NEW table XHB_CASE_LISTING_ENTRY with data from CREST  
                     INSERT INTO  xhibit.xhb_case_listing_entry
                                  ( case_listing_entry_id
                                  , case_id 
                                  , ref_judge_type_id   
                                  , court_site_id
                                  , court_id
                                  , judge_id
                                  , creation_date 
                                  , created_by
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (xhibit.xhb_case_listing_entry_seq.nextval
                                  ,xhb_cle_xins_tt(i).case_id
                                  ,xhb_cle_xins_tt(i).ref_judge_type_id
                                  ,xhb_cle_xins_tt(i).court_site_id                                  
                                  ,xhb_cle_xins_tt(i).court_id
                                  ,xhb_cle_xins_tt(i).judge_id
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');      
                  


    
    v_cle_ins_rows := SQL%ROWCOUNT;

 --   DBMS_OUTPUT.PUT_LINE(' ');
 --   DBMS_OUTPUT.PUT_LINE('CTX-2198:XHB_CASE_LISTING_ENTRY - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cle_ins_rows);
    
    -- UPDATE to XHB_CASE_DM table NOT REQUIRED as the REF_JUDGE_TYPE column only is referred
    -- AND XHB_CASE ETL Procedure whould have updated the ACTUAL CASE ETL status
               -- Update INFO LOG TABLE for XHBSTG_CASE_HISTORY_DM table update for the rows processed
         IF v_cle_ins_rows > 0 THEN 
            v_cle_ins_status := 'I'; -- XHIBIT TABLE inserted
         ELSIF v_cle_ins_rows = 0 THEN
            v_cle_ins_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;  

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                  ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                  ,p_run_time           =>  sysdate
                  ,p_log_msg_type       =>  'I' -- Information
                  ,p_log_msg            =>  'NON-MIGRATED - NO OF RECORDS PROCESSED FROM XHB_CASE - case_no '||xhb_cle_xins_tt(i).case_no||', case_type '||xhb_cle_xins_tt(i).case_type||' row with ETL_STATUS '||v_cle_ins_status
                  ,p_err_row_count      =>  NULL
                  ,p_success_row_count  => v_cle_ins_rows
                  ,p_last_updated_by    => 'DATA MIGRATION'
                  ,p_created_by         => 'DATA MIGRATION'
                  );
                             
     --      DBMS_OUTPUT.PUT_LINE('CTX-2198:XHB_CASE_LISTING_ENTRY - Crest Court : '||p_crest_court_id||' - Processed no of rows : '||SQL%ROWCOUNT);

           -- Store the case_no and case_type to be used later to insert into xhibit.xhb_diary_note_entry for data being migrated from xhbstg_case_note_dm table          
           v_case_details_tt.EXTEND; 
           v_case_details_tt(v_case_details_tt.LAST).case_no   := v_case_no;
           v_case_details_tt(v_case_details_tt.LAST).case_type := v_case_type;        
 EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
       -- ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_c_listing_entry_crest for CREST_COURT : '||p_crest_court_id||',case_no : '||v_case_no||',case_id : '||v_case_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_with_crest - CTX-2198,2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_c_list_entry_crest - Error processing case_id : '||v_case_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows+v_xcount_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

        END; -- CTX-3252    
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   
        v_xcount_number_of_rows := v_xcount_number_of_rows + xhb_cle_xins_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_xhibit_cle_ins_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_xhibit_cle_ins_details; --case_listing_entry_populated and connected diary_note_entry


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_LISTING_ENTRY : Inserted NON MIGRATED ROWS - '||v_xcount_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_xcount_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2198:XHB_CASE_LISTING_ENTRY inserted NON MIGRATED RECORDS - '||v_xcount_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

    -- ========== Migrate data to insert into xhibit.XHB_DIARY_NOTE_ENTRY for data taken from XHBSTG_CASE_NOTE_DM table ================
    IF v_case_details_tt IS NOT NULL AND v_case_details_tt.COUNT > 0 THEN
    
        FOR x IN v_case_details_tt.FIRST .. v_case_details_tt.LAST LOOP
        
           OPEN cur_crest_case_note_det(p_crest_court_id, v_case_details_tt(x).case_no, v_case_details_tt(x).case_type);
             LOOP
             FETCH cur_crest_case_note_det BULK COLLECT INTO xhb_ccn_xins_tt LIMIT g_bulk_limit;
     
               IF xhb_ccn_xins_tt IS NOT NULL AND xhb_ccn_xins_tt.COUNT > 0 THEN
                
                 FOR k IN xhb_ccn_xins_tt.FIRST .. xhb_ccn_xins_tt.LAST LOOP

                    BEGIN -- CTX-3252
                    -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                    v_case_no     := xhb_ccn_xins_tt(k).case_no;
                    v_case_type   := xhb_ccn_xins_tt(k).case_type;
                    v_can_id      := xhb_ccn_xins_tt(k).can_id;
                    v_c_list_id   := xhb_ccn_xins_tt(k).case_listing_entry_id;
                     DBMS_OUTPUT.PUT_LINE('v_Case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_can_id = '||v_can_id||', v_c_list_id : '||v_c_list_id||', v_xhibit_court_id '||v_xhibit_court_id);                
    
    --DBMS_OUTPUT.PUT_LINE('DOING xhibit.xhb_diary_note_entry');

               -- INSERT and populate this NEW table XHB_DIARY_ENTRY_NOTE with data from CREST  XHBSTG_CASE_NOTE_DM table
                         INSERT INTO  xhibit.xhb_diary_note_entry
                                      ( diary_note_entry_id
                                      , case_listing_entry_id
                                      , note_type_id 
                                      , note_classification_id   
                                      , diary_note_text
                                      , diary_note_pre_defined_id
                                      , diary_date
                                      , court_id
                                      , obs_ind -- CTX-3829
                                      , case_id -- CTX-3829
                                      , creation_date 
                                      , created_by
                                      , last_update_date 
                                      , last_updated_by )
                               VALUES 
                                      (xhibit.xhb_diary_note_entry_seq.nextval
                                      ,xhb_ccn_xins_tt(k).case_listing_entry_id
                                      ,xhb_ccn_xins_tt(k).note_type_id
                                      ,xhb_ccn_xins_tt(k).note_classification_id                                  
                                      ,xhb_ccn_xins_tt(k).diary_note_text
                                      ,xhb_ccn_xins_tt(k).diary_note_pre_defined_id
                                      ,xhb_ccn_xins_tt(k).diary_date                                                                                                      
                                     -- ,decode(xhb_ccn_xins_tt(k).case_no, null,xhb_ccn_xins_tt(k).court_id,null)
                                      ,xhb_ccn_xins_tt(k).court_id -- CTX-3829
                                      , 'N' -- CTX-3829
                                      , xhb_ccn_xins_tt(k).case_id -- CTX-3829
                                      ,xhb_ccn_xins_tt(k).creation_date
                                      ,'DATA_MIGRATION'
                                      ,SYSDATE
                                      ,'DATA_MIGRATION');       
    
                     v_ccn_ins_rows := SQL%ROWCOUNT;
          --           DBMS_OUTPUT.PUT_LINE(' ');
          --           DBMS_OUTPUT.PUT_LINE('CTX-2201:XHB_DIARY_NOTE_ENTRY - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_ccn_ins_rows);
    
                   -- Update XHBSTG_CASE_NOTE_DM table for the rows processed
                    IF v_ccn_ins_rows > 0 THEN 
                       v_ccn_ins_status := 'I'; -- XHIBIT TABLE inserted
                    ELSIF v_ccn_ins_rows = 0 THEN
                       v_ccn_ins_status := 'N'; -- No Action Performed on XHIBIT table
                    END IF;
    
               insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'INSERTING XHB_DIARY_NOTE_ENTRY - inserted crest_case_no '||v_case_no||', case_type '||v_case_type||', v_c_list_id : '||v_c_list_id||' row with ETL_STATUS '||v_ccn_ins_status
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => v_ccn_ins_rows
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
                             
                     -- Update XHBSTG_CASE_NOTE_DM table for the rows processed
                     UPDATE xhbstg_case_note_dm xcn
                        SET    xcn.xhibit_etl_date   = SYSDATE
                              ,xcn.xhibit_court_id   = v_xhibit_court_id
                              ,xcn.xhibit_enrich_date = SYSDATE
                              ,xcn.xhibit_etl_status = v_ccn_ins_status
                      WHERE  xcn.crest_court_id   =  p_crest_court_id
                        AND  xcn.can_id  = xhb_ccn_xins_tt(k).can_id
                        AND  xcn.case_no  = xhb_ccn_xins_tt(k).case_no
                        AND  xcn.case_type = xhb_ccn_xins_tt(k).case_type;
                        
            --        DBMS_OUTPUT.PUT_LINE(' ');
            --        DBMS_OUTPUT.PUT_LINE('CTX-2201:XHBSTG_CASE_NOTE - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
  
 EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        -- ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_c_listing_entry_crest for CREST_COURT : '||p_crest_court_id||',case_no : '||v_case_no||',case_id : '||v_case_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_with_crest - CTX-2198,2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_c_list_entry_crest - Error processing case_id : '||v_case_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows+v_xcount_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

      -- Update XHBSTG_CASE_NOTE_DM
         -- tables with error status/messages

            UPDATE xhbstg_case_note_dm xch
            SET    xch.xhibit_etl_date   = SYSDATE
                  ,xch.xhibit_court_id   = v_xhibit_court_id
                  ,xch.xhibit_enrich_date = SYSDATE
                  ,xch.xhibit_etl_status = 'X' -- Error
                  ,xch.xhibit_etl_err_message = v_err_message
            WHERE  xch.crest_court_id   =  p_crest_court_id
              AND  xch.case_no = v_case_no
              AND  xch.case_type = v_case_type;

          END; -- CTX-3252  
                 END LOOP;
                 
                 COMMIT;
                 v_xcount_ccn_no_of_rows := v_xcount_ccn_no_of_rows + xhb_ccn_xins_tt.COUNT;
      
               END IF;
        
               EXIT WHEN cur_crest_case_note_det%NOTFOUND;
        
              END LOOP;
            CLOSE cur_crest_case_note_det; 
        
        END LOOP;
    
    END IF;

        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DIARY_NOTE_ENTRY : Inserted NON MIGRATED ROWS - '||v_xcount_ccn_no_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_xcount_ccn_no_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2201:XHB_DIARY_NOTE_ENTRY inserted NON MIGRATED RECORDS - '||v_xcount_ccn_no_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

              ------ Update rest of the Unprocessed rows in XHBSTG_CASE_SUB_APPEARANCE_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
               
                       -- Update XHBSTG_CASE_NOTE_DM table for the rows NOT processed
            UPDATE xhbstg_case_note_dm xsa
            SET    xsa.xhibit_etl_date   = SYSDATE
                  ,xsa.xhibit_court_id   = v_xhibit_court_id
                  ,xsa.xhibit_enrich_date = SYSDATE
                  ,xsa.xhibit_etl_status = 'N' -- Not processed
            WHERE  xsa.crest_court_id   =  p_crest_court_id
              AND  xsa.xhibit_etl_status is NULL 
              AND  xsa.xhibit_enrich_date  is NULL
              AND  xsa.xhibit_etl_date  is NULL
              AND  xsa.xhibit_court_id is NULL
              AND  xsa.case_no IS NOT NULL --procedure below manages the rows that have null case numbers.  Without this clause, the initial cursor 
                                           --in the procedure blow would not return any data because the etl fields would be set
              ;
           
              v_ccn_np_rows := SQL%ROWCOUNT;
                         
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DIARY_NOTE_ENTRY: updating XHBSTG_CASE_NOTE_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_ccn_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2201:XHBSTG_CASE_NOTE_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_ccn_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_c_listing_entry_crest for CREST_COURT : '||p_crest_court_id||',case_no : '||v_case_no||',case_id : '||v_case_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_with_crest - CTX-2198,2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_c_list_entry_crest - Error processing case_id : '||v_case_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows+v_xcount_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

                                       
          COMMIT;     
     
          
END upd_xhb_c_list_entry_crest;


/**
  * NAME       : upd_diary_note_entry_no_case_crest;
  * DESCRIPTION: CTX-2867 New CTX fields for XHB_DIARY_NOTE_ENTRY - Sec 4.3.2.16 req [4975.DM.021]
  *            : This table is also populated from upd_xhb_c_list_entry_crest where there is a join to the case table.
  *            : upd_xhb_c_list_entry_crest has passed test at the time of writing so a seperate procedure will be used to 
  *            : manage the changes in FS rather than alter a previously successfully tested procedure
  *            : CTX-3277 Fixed XHB_DIARY_NOTE_ENTRY court_id
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * 11/12/2018 : S Sethuraman - CTX-3252 - Exception Handling moved inside loop to continue processing without RollBack
  * 07/03/2019 : S Sethuraman - CTX-3829 - Reports LODR issue - FS changes v0.31
**/
PROCEDURE upd_diary_ne_no_case_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_dne_ins_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , can_id                       xhbstg_case_note_dm.can_id%TYPE
    , case_id                      xhibit.xhb_case.case_id%TYPE
    , case_listing_entry_id        xhibit.xhb_case_listing_entry.case_listing_entry_id%TYPE
    , case_no                      xhibit.xhb_case.case_number%TYPE
    , case_type                    xhibit.xhb_case.case_type%TYPE
    , note_type_id                 xhibit.xhb_diary_note_entry.note_type_id%TYPE
    , note_classification_id       xhibit.xhb_diary_note_entry.note_classification_id%TYPE
    , diary_note_text              xhibit.xhb_diary_note_entry.diary_note_text%TYPE
    , diary_note_pre_defined_id    xhibit.xhb_diary_note_entry.diary_note_pre_defined_id%TYPE
    , diary_date                   xhibit.xhb_diary_note_entry.diary_date%TYPE
    , creation_date                xhibit.xhb_diary_note_entry.creation_date%TYPE
    );

    TYPE xhb_dne_type IS TABLE OF xhb_dne_ins_rec;
    xhb_dne_tt  xhb_dne_type;
      
    --  CTX-3829 - THIS CURSOR HANDLES ONLY CASE_NOTE.NOTE_TYPE = 'D' AND CASE_NO IS NULL 
    -- HENCE THE COURT_ID IS UPDATED AS PART OF CTX-3829 
    -- AND OBS_IND is updated as 'N'
    CURSOR cur_crest_dne_details IS
    SELECT xcn.crest_court_id
           ,v_xhibit_court_id
           ,xcn.can_id
           ,NULL case_id
           ,NULL case_listing_entry_id
           ,xcn.case_no
           ,xcn.case_type
           ,(SELECT xrl.ref_listing_data_id
              FROM   xhibit.xhb_ref_listing_data xrl
              WHERE  xrl.ref_data_type = 'NOTE_TYPE'
                AND    xrl.ref_data_value = decode(UPPER(xcn.note_type),'C','CN','H','HN','D','GDN') --cc10102018 included the UPPER
                AND    NVL(xrl.obs_ind,'N') != 'Y'
             ) as note_type_id
            ,(SELECT xrl.ref_listing_data_id
              FROM   xhibit.xhb_ref_listing_data xrl
              WHERE  xrl.ref_data_type = 'NOTE_CLASSIFICATION'
                AND    xrl.ref_data_value = decode(UPPER(xcn.note_print_ind), 'S', 'Standard', 'P', 'Priority', 'R', 'Restricted') --cc10102018 included the UPPER
                 AND    NVL(xrl.obs_ind,'N') != 'Y'
             ) as note_classification_id
            ,xcn.note as diary_note_text
            , NULL as diary_note_pre_defined_id
            ,xcn.diary_date as diary_date
          --  ,xcn.note_date -- CTX_CASE_NOTE ISSUE
          ,xcn.note_date+numtodsinterval(row_number() over( partition by note_date order by can_id),'MINUTE') as creation_date
    FROM data_mig.xhbstg_case_note_dm   xcn
    WHERE xcn.crest_court_id = p_crest_court_id -- crest_court_id matched
    AND xcn.case_no IS NULL --only concerend with the NULL case rows
    AND xcn.note_type = 'D'
    AND xcn.case_type IS NULL
    AND NVL(xcn.xhibit_etl_status,'N') NOT IN ('I','U' )
    AND xcn.xhibit_enrich_date is NULL
    ; 

    v_count_number_of_rows   NUMBER := 0;
    v_dne_upd_rows           NUMBER := 0;
    v_dne_np_rows            NUMBER := 0;
    v_dne_upd_status         CHAR(1) := 'N';
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_can_id                 xhbstg_case_note_dm.can_id%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_diary_note_entry_no_case_crest- CTX-2867'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DIARY_NOTE_ENTRY - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_DIARY_NOTE_ENTRY with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_DIARY_NOTE_ENTRY table that have not yet been populated with CREST data and are not connected with a case
    OPEN cur_crest_dne_details;
    LOOP
    FETCH cur_crest_dne_details BULK COLLECT INTO xhb_dne_tt LIMIT g_bulk_limit;
   
    IF xhb_dne_tt IS NOT NULL AND xhb_dne_tt.COUNT > 0 THEN
   
        FOR i IN xhb_dne_tt.FIRST .. xhb_dne_tt.LAST LOOP

        BEGIN -- CTX-3252
        -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_can_id     := xhb_dne_tt(i).can_id;
    
        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_diary_note_entry_no_case_crest- CTX-2687'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_DIARY_NOTE_ENTRY - inserting  xhbstg_case_note_dm.can_id - '||v_can_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('INSERTING XHB_DIARY_NOTE_ENTRY - FOR CREST_COURT_ID = '||xhb_dne_tt(i).crest_court_id);
    
             DBMS_OUTPUT.PUT_LINE('NO CASE ID : v_can_id: '||'v_can_id : '||v_can_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
            
         -- INSERT and populate this NEW table XHB_DIARY_NOTE_ENTRY with data from CREST  
                     INSERT INTO  xhibit.xhb_diary_note_entry
                                  ( diary_note_entry_id
                                  , case_listing_entry_id
                                  , note_type_id 
                                  , note_classification_id   
                                  , diary_note_text
                                  , diary_note_pre_defined_id
                                  , diary_date
                                  , court_id
                                  , obs_ind -- CTX-3829
                                  , creation_date 
                                  , created_by
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (xhibit.xhb_diary_note_entry_seq.nextval
                                  ,xhb_dne_tt(i).case_listing_entry_id
                                  ,xhb_dne_tt(i).note_type_id
                                  ,xhb_dne_tt(i).note_classification_id                                  
                                  ,xhb_dne_tt(i).diary_note_text
                                  ,xhb_dne_tt(i).diary_note_pre_defined_id
                                  ,xhb_dne_tt(i).diary_date                                                                                                      
                                 -- ,decode(xhb_dne_tt(i).case_no, null,xhb_dne_tt(i).court_id,null)
                                  ,xhb_dne_tt(i).court_id -- CTX-3829 SINCE THE CURSOR HAS ONLY NOTE_TYPE = 'D' entries, POPULATE COURT_ID
                                  , 'N' -- CTX-3829
                                  ,xhb_dne_tt(i).creation_date
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');      
                  
    v_dne_upd_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2867:XHB_DIARY_NOTE_ENTRY - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_dne_upd_rows);
    
          -- Update xhbstg_case_note_dm table for the rows processed
         IF v_dne_upd_rows > 0 THEN 
            v_dne_upd_status := 'I'; -- XHIBIT TABLE inserted
         ELSIF v_dne_upd_rows = 0 THEN
            v_dne_upd_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;  
    
               -- Update xhbstg_case_note_dm
               
            UPDATE xhbstg_case_note_dm xcn
            SET    xcn.xhibit_etl_date   = SYSDATE
                  ,xcn.xhibit_court_id   = v_xhibit_court_id
                  ,xcn.xhibit_enrich_date = SYSDATE
                  ,xcn.xhibit_etl_status = v_dne_upd_status
            WHERE  xcn.crest_court_id   =  p_crest_court_id
              AND  xcn.can_id = xhb_dne_tt(i).can_id
              ;
 EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        -- ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_diary_note_entry_no_case_crest for CREST_COURT : '||p_crest_court_id||' can_id: '||v_can_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_diary_note_entry_no_case_crest- CTX-2687'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_diary_note_entry_no_case_crest - Error processing crest_can_id : '||v_can_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
            UPDATE xhbstg_case_note_dm xch
            SET    xch.xhibit_etl_date   = SYSDATE
                  ,xch.xhibit_court_id   = v_xhibit_court_id
                  ,xch.xhibit_enrich_date = SYSDATE
                  ,xch.xhibit_etl_status = 'X' -- Error
                  ,xch.xhibit_etl_err_message = v_err_message
            WHERE  xch.crest_court_id   =  p_crest_court_id
             AND  xch.can_id = v_can_id
              ;
   
      END; -- CTX-3252
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_dne_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_dne_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_dne_details;
    
  
        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_diary_note_entry_no_case_crest- CTX-2687'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DIARY_NOTE_ENTRY : Inserted NON MIGRATED ROWS - '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2201:XHB_DIARY_NOTE_ENTRY inserted NON MIGRATED RECORDS - '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

              ------ Update rest of the Unprocessed rows in XHBSTG_CASE_SUB_APPEARANCE_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
               
            -- Update XHBSTG_CASE_NOTE_DM table for the rows NOT processed
            UPDATE xhbstg_case_note_dm xsa
            SET    xsa.xhibit_etl_date   = SYSDATE
                  ,xsa.xhibit_court_id   = v_xhibit_court_id
                  ,xsa.xhibit_enrich_date = SYSDATE
                  ,xsa.xhibit_etl_status = 'N' -- Not processed
            WHERE  xsa.crest_court_id   =  p_crest_court_id
              AND  xsa.xhibit_etl_status is NULL 
              AND  xsa.xhibit_enrich_date  is NULL
              AND  xsa.xhibit_etl_date  is NULL
              AND  xsa.xhibit_court_id is NULL
              AND  xsa.case_no IS NULL --this procedure only manages null cases
              ;
           
              v_dne_np_rows := SQL%ROWCOUNT;
                         
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_list_entry_crest- CTX-2198,CTX-2201'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DIARY_NOTE_ENTRY: updating XHBSTG_CASE_NOTE_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_dne_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2201:XHBSTG_CASE_NOTE_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_dne_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        -- ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_diary_note_entry_no_case_crest for CREST_COURT : '||p_crest_court_id||' can_id: '||v_can_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_diary_note_entry_no_case_crest- CTX-2687'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_diary_note_entry_no_case_crest - Error processing crest_can_id : '||v_can_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                           
            COMMIT;

END upd_diary_ne_no_case_crest;


/**
  * NAME       : upd_xhb_c_diary_fixture_crest
  * DESCRIPTION: CTX-2199 New CTX fields for XHB_CASE_DIARY_FIXTURE - 
  *        Cases new data to be populated from CREST - Sec 4.3.2.13 req [4975.DM.017]
  *              CTX-2200 New CTX fields for XHB_FIXTURE_DEFT_ATTENDING - 
  *        Cases new data to be populated from CREST - Sec 4.3.2.14 req [4975.DM.018]
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * 11/12/2018 : S Sethuraman - CTX-3252 - Exception Handling moved inside loop to continue processing without RollBack
  * 26/02/2019 : S Sethuraman - CTX-3773 - UPD_XHB_C_DIARY_FIXTURE_CREST - EXTRACT 35 CHARS ONLY OF REASON_REMOVED TO MATCH XHIBIT FIELD LENGTH
  * 12/03/2019 : S Sethuraman - CTX-3845 - UPD_XHB_C_DIARY_FIXTURE_CREST - both CURSORS include LIST_DATE > SYSDATE to migrate FURTURE FIXTURES ONLY of list_type = 'X'   
**/
PROCEDURE upd_xhb_c_diary_fixture_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
    
 TYPE xhb_cdf_rec IS RECORD
    ( chd_id                          data_mig.xhbstg_case_hearing_day_dm.chd_id%TYPE
    , crest_court_id                  xhibit.xhb_court.crest_court_id%TYPE
    , case_no                         data_mig.xhbstg_case_hearing_day_dm.case_no%TYPE 
    , case_type                       data_mig.xhbstg_case_hearing_day_dm.case_type%TYPE 
    , court_id                        xhibit.xhb_court.court_id%TYPE
    , case_listing_entry_id           xhibit.xhb_case_diary_fixture.case_listing_entry_id%TYPE
    , listing_date                    xhibit.xhb_case_diary_fixture.listing_date%TYPE
    , fixture_notice_required         xhibit.xhb_case_diary_fixture.fixture_notice_required%TYPE
    , court_site_id                   xhibit.xhb_case_diary_fixture.court_site_id%TYPE
    , hearing_type_id                 xhibit.xhb_case_diary_fixture.hearing_type_id%TYPE
    , list_note_pre_defined_id         xhibit.xhb_case_diary_fixture.list_note_pre_defined_id%TYPE
    , list_note_text                  xhibit.xhb_case_diary_fixture.list_note_text%TYPE
    , pre_def_note_class_id           xhibit.xhb_case_diary_fixture.pre_def_note_class_id%TYPE
    , free_text_note_class_id         xhibit.xhb_case_diary_fixture.free_text_note_class_id%TYPE
    , vacation_pre_defined_rson_id    xhibit.xhb_case_diary_fixture.vacation_pre_defined_rson_id%TYPE
    , vacation_freetext_reason        xhibit.xhb_case_diary_fixture.vacation_freetext_reason%TYPE
    , status                          xhibit.xhb_case_diary_fixture.status%TYPE
    , fxl_run_date                    xhibit.xhb_case_diary_fixture.fxl_run_date%TYPE
    , obs_ind                         xhibit.xhb_case_diary_fixture.obs_ind%TYPE --cc10102018
    , date_vacated                    xhibit.xhb_case_diary_fixture.date_vacated%TYPE --cc10102018
    );

    TYPE xhb_cdf_type IS TABLE OF xhb_cdf_rec;
    xhb_cdf_tt  xhb_cdf_type;
    
    TYPE xhb_csa_rec IS RECORD
    ( chd_id                          data_mig.xhbstg_case_hearing_day_dm.chd_id%TYPE
    , def_on_case_id                  xhibit.xhb_defendant_on_case.defendant_on_case_id%TYPE
    , case_diary_fixture_id           xhibit.xhb_case_diary_fixture.case_diary_fixture_id%TYPE
    , attending                       xhibit.xhb_fixture_deft_attending.attending%TYPE 
    , sub_id                          data_mig.xhbstg_case_sub_appearance_dm.sub_id%TYPE
    );

    TYPE xhb_csa_type IS TABLE OF xhb_csa_rec;
    xhb_csa_tt  xhb_csa_type;    
      
    CURSOR cur_crest_cdf_details IS
   SELECT cdf.chd_id,
          cdf.crest_court_id,
          cdf.case_no,
          cdf.case_type,
           xhc.court_id,
           (select case_listing_entry_id 
              from xhibit.xhb_case_listing_entry 
             where case_id = xc.case_id) as case_listing_entry_id,
           cdf.list_date,
           NVL(cc.fix_req_ind,'N') fix_req_ind,
           (select court_site_id from xhibit.xhb_court_site
             where court_id = xhc.court_id
               and court_site_code = cdf.chd_site_code
               and rownum = 1) as court_site_id,  
               -- ROWNUM 1 condition to eliminate TEST DATA Issue in TST2  as it retreives 
               --  more than 1 row where as in Live there will 1 UNIQUE record
           (select ref_hearing_type_id from xhibit.xhb_ref_hearing_type
             where court_id = xhc.court_id and
                   hearing_type_code = cdf.hearing_type and
                   category = 'X') as hearing_type_id, -- refer to category = 'X' always - CTX-2584
           NULL as list_note_pre_defined_id,
           cdf.list_notes,
           NULL as pre_def_note_class_id,
           NULL as free_text_note_class_id,
           NULL as vacation_pre_defined_rson_id,
           substr(cdf.reason_removed,1,35) reason_removed, -- CTX-3773
           decode(cdf.reason_removed,NULL,'A','D') as status,
           decode(cdf.lfxc_job_id,NULL,NULL,SYSDATE) as fxl_run_date,
           CASE
            WHEN cdf.reason_removed IS NULL THEN 'N'
            ELSE 'Y'
           END obs_ind 
           ,cdf.date_removed --cc10102018 additional change.  Date removed will populate date vacated
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_case_hearing_day_dm cdf,
         xhibit.xhb_case xc,
         xhbstg_case_dm cc
    where cdf.crest_court_id = p_crest_court_id and
          cdf.crest_court_id = xhc.crest_court_id and 
          cdf.crest_court_id = cc.crest_court_id and 
          xhc.court_id = xc.court_id and
          cdf.case_no = xc.case_number and
          cdf.case_type = xc.case_type and
          cdf.case_no = cc.case_no and
          cdf.case_type = cc.case_type and 
          cdf.list_type = 'X' and -- ONLY FUTURE FIXTURES to be MIGRATED
          cdf.list_date > SYSDATE and -- CTX-3845 FUTURE FIXTURES ONLY
          NVL(cdf.xhibit_etl_status,'N') not in ('I', 'U') and
          cdf.xhibit_enrich_date is NULL  and
          EXISTS -- check CASE_LISTING_ENTRY exists
          (select 'Y' 
           from xhibit.xhb_case_listing_entry
           where case_id = xc.case_id 
           and court_id = xhc.court_id) 
           and NOT exists (select 'X' 
                           from xhibit.xhb_case_diary_fixture
                           where case_listing_entry_id  = (select case_listing_entry_id 
                                                           from xhibit.xhb_case_listing_entry 
                                                           where case_id = xc.case_id 
                                                           and court_id = xhc.court_id)
                                                           );  

       CURSOR cur_crest_case_suject_app_det 
             ( l_court_id              IN    xhibit.xhb_court.crest_court_id%TYPE,
               l_chd_id                IN    data_mig.xhbstg_case_sub_appearance_dm.chd_id%TYPE,
               l_cdf_id                IN    xhibit.xhb_case_diary_fixture.case_diary_fixture_id%TYPE) 
       IS
       SELECT xcsa.chd_id,
              xdc.defendant_on_case_id,
              l_cdf_id  as case_diary_fixture_id,
              'Y' as attending,
              xcsa.sub_id
         FROM  data_mig.xhbstg_case_sub_appearance_dm   xcsa
              , xhibit.xhb_defendant xd
              , xhibit.xhb_defendant_on_case xdc
              , xhibit.xhb_case xc
              , xhibit.xhb_court xhc
        WHERE xcsa.crest_court_id = l_court_id -- crest_court_id matched
          AND xcsa.crest_court_id = xhc.crest_court_id
          AND xhc.court_id = xd.court_id
          AND xhc.court_id = xc.court_id
          AND xd.defendant_id = xdc.defendant_id
          AND xd.crest_defendant_id = xcsa.sub_id
          AND xcsa.case_no = xc.case_number 
          AND xcsa.case_type = xc.case_type
          AND xdc.case_id = xc.case_id
          AND xcsa.chd_id = l_chd_id -- case_hearing_day_id matched
          AND NVL(xcsa.xhibit_etl_status,'N') not in ('I','U' )
          AND xcsa.xhibit_enrich_date is NULL;
      
    v_count_number_of_rows   NUMBER := 0;
    v_cdf_upd_rows           NUMBER := 0;
    v_cdf_upd_status         CHAR(1) := 'N';
    v_cdf_np_rows            NUMBER := 0;
    v_count_fda_no_of_rows   NUMBER := 0;
    v_cfda_upd_rows          NUMBER := 0;
    v_cfda_upd_status        CHAR(1) := 'N';
--    v_cfda_np_rows           NUMBER := 0;
    v_c_diary_fixture_id     xhibit.xhb_case_diary_fixture.case_diary_fixture_id%TYPE;
    v_chd_id                 data_mig.xhbstg_case_hearing_day_dm.chd_id%TYPE;
    v_cdf_id                 xhibit.xhb_case_diary_fixture.case_diary_fixture_id%TYPE;
    v_case_no                data_mig.xhbstg_case_hearing_day_dm.case_no%TYPE;
    v_sub_id                 data_mig.xhbstg_case_sub_appearance_dm.sub_id%TYPE;
    v_def_on_case_id         xhibit.xhb_defendant_on_Case.defendant_on_case_id%TYPE;
    v_case_type              data_mig.xhbstg_case_hearing_day_dm.case_type%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest- CTX-2199'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_DIARY_FIXTURE - Starting process of inserting  rows from CREST CASE_HEARING_DAY table'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting rows from CREST CASE_HEARING_DAY table');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_CASE_DIARY_FIXTURE table that have not yet been populated with CREST data
    OPEN cur_crest_cdf_details;
    LOOP
    FETCH cur_crest_cdf_details BULK COLLECT INTO xhb_cdf_tt LIMIT g_bulk_limit;
   
    IF xhb_cdf_tt IS NOT NULL AND xhb_cdf_tt.COUNT > 0 THEN
   
        FOR i IN xhb_cdf_tt.FIRST .. xhb_cdf_tt.LAST LOOP

           BEGIN -- CTX-3252
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest- CTX-2199'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_CASE_DIARY_FIXTURE - inserting  crest case_no - '||xhb_cdf_tt(i).case_no||', case_type : '||xhb_cdf_tt(i).case_type||', chd_id : '||xhb_cdf_tt(i).chd_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
  --  DBMS_OUTPUT.PUT_LINE(' ');
  --  DBMS_OUTPUT.PUT_LINE('INSERTING XHB_DEFENDANT_HISTORY - FOR CREST_COURT_ID = '||xhb_cdf_tt(i).crest_court_id);
    
 -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
           v_chd_id     := xhb_cdf_tt(i).chd_id;
            v_case_no     := xhb_cdf_tt(i).case_no;
            v_case_type     := xhb_cdf_tt(i).case_type;
             DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', chd_id : '||xhb_cdf_tt(i).chd_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
            
         -- INSERT and populate this NEW table XHB_CASE_DIARY_FIXTURE with data from CREST  
         
            
         begin
           select xhibit.xhb_case_diary_fixture_seq.nextval into v_c_diary_fixture_id from dual;
         end;
         
                     INSERT INTO  xhibit.xhb_case_diary_fixture
                                  ( case_diary_fixture_id
                                  , case_listing_entry_id 
                                  , listing_date   
                                  , fixture_notice_required
                                  , court_site_id
                                  , hearing_type_id
                                  , list_note_pre_defined_id
                                  , list_note_text
                                  , pre_def_note_class_id 
                                  , free_text_note_class_id
                                  , vacation_pre_defined_rson_id
                                  , vacation_freetext_reason 
                                  , status
                                  , fxl_run_date
                                  , obs_ind--cc10102018
                                  , date_vacated--cc10102018
                                  , creation_date
                                  , created_by
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (v_c_diary_fixture_id
                                  ,xhb_cdf_tt(i).case_listing_entry_id
                                  ,xhb_cdf_tt(i).listing_date                                  
                                  ,xhb_cdf_tt(i).fixture_notice_required
                                  ,xhb_cdf_tt(i).court_site_id
                                  ,xhb_cdf_tt(i).hearing_type_id
                                  ,xhb_cdf_tt(i).list_note_pre_defined_id
                                  ,xhb_cdf_tt(i).list_note_text    
                                  ,xhb_cdf_tt(i).pre_def_note_class_id                                                                
                                  ,xhb_cdf_tt(i).free_text_note_class_id
                                  ,xhb_cdf_tt(i).vacation_pre_defined_rson_id
                                  ,xhb_cdf_tt(i).vacation_freetext_reason
                                  ,xhb_cdf_tt(i).status
                                  ,xhb_cdf_tt(i).fxl_run_date
                                  ,xhb_cdf_tt(i).obs_ind --cc10102018
                                  ,xhb_cdf_tt(i).date_vacated--cc10102018
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');      
                  


    
    v_cdf_upd_rows := SQL%ROWCOUNT;
  --  DBMS_OUTPUT.PUT_LINE(' ');
   -- DBMS_OUTPUT.PUT_LINE('CTX-2199:XHB_CASE_DIARY_FIXTURE - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cdf_upd_rows||', v_c_diary_fixture_id : '||v_c_diary_fixture_id);
    
               -- Update XHBSTG_CASE_HEARING_DAY_DM table for the rows processed
         IF v_cdf_upd_rows > 0 THEN 
            v_cdf_upd_status := 'I'; -- XHIBIT TABLE inserted
         ELSIF v_cdf_upd_rows = 0 THEN
            v_cdf_upd_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;  

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest- CTX-2199'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_CASE_DIARY_FIXTURE_DM - inserted chd_id '||xhb_cdf_tt(i).chd_id||' row with ETL_STATUS '||v_cdf_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_cdf_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         

           -- Update XHBSTG_CASE_DIARY_FIXTURE_DM table for the List type X rows processed
            UPDATE xhbstg_case_hearing_day_dm xch
            SET    xch.xhibit_etl_date   = SYSDATE
                  ,xch.xhibit_court_id   = v_xhibit_court_id
                  ,xch.xhibit_enrich_date = SYSDATE
                  ,xch.xhibit_etl_status = v_cdf_upd_status
            WHERE  xch.crest_court_id   =  p_crest_court_id
              AND  xch.chd_id    = xhb_cdf_tt(i).chd_id
              AND  xch.case_no   = xhb_cdf_tt(i).case_no
              AND  xch.case_type = xhb_cdf_tt(i).case_type
              AND  xch.list_type = 'X';
     

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2199:XHBSTG_CASE_HEARING_DAY_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
 
          insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest- CTX-2199,CTX-2200'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_FIXTURE_DEFT_ATTENDING - Starting process of inserting  rows from CREST CASE_SUB_APPEARANCE table'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting rows from CREST CASE_SUB_APPEARANCE table');
  
   OPEN cur_crest_case_suject_app_det(p_crest_court_id,v_chd_id,v_c_diary_fixture_id);
         LOOP
         FETCH cur_crest_case_suject_app_det BULK COLLECT INTO xhb_csa_tt LIMIT g_bulk_limit;
 
           IF xhb_csa_tt IS NOT NULL AND xhb_csa_tt.COUNT > 0 THEN
    
             FOR k IN xhb_csa_tt.FIRST .. xhb_csa_tt.LAST LOOP
          
               BEGIN -- CTX-3252
          
               -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_sub_id   := xhb_csa_tt(k).sub_id;
                v_def_on_case_id   := xhb_csa_tt(k).def_on_case_id;
                v_cdf_id   := xhb_csa_tt(k).case_diary_fixture_id;
                  DBMS_OUTPUT.PUT_LINE('v_sub_id : '||v_sub_id||', v_chd_id : '||v_chd_id||', v_cdf_id : '||v_cdf_id||', v_xhibit_court_id '||v_xhibit_court_id);                

 
                 --  Insert into XHB_FXITURE_DEFT_ATTENDING
                 INSERT INTO  xhibit.xhb_fixture_deft_attending
                                  ( fixture_deft_attending_id
                                  , defendant_on_case_id 
                                  , case_diary_fixture_id   
                                  , attending
                                  , obs_ind
                                  , creation_date
                                  , created_by
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (xhibit.Xhb_Fixture_Deft_Attending_Seq.nextval
                                  ,xhb_csa_tt(k).def_on_case_id
                                  ,xhb_csa_tt(k).case_diary_fixture_id                                  
                                  ,xhb_csa_tt(k).attending
                                  ,'N'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');      
                  


                 v_cfda_upd_rows := SQL%ROWCOUNT;
                 DBMS_OUTPUT.PUT_LINE(' ');
                 DBMS_OUTPUT.PUT_LINE('CTX-2200:XHB_FIXTURE_DEFT_ATTENDING - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_cfda_upd_rows);

               -- Update XHB_DEF_ON_CASE_REF_SOL_FIRM table for the rows processed
                IF v_cfda_upd_rows > 0 THEN 
                   v_cfda_upd_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_cfda_upd_rows = 0 THEN
                   v_cfda_upd_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest- CTX-2199,CTX-2200'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTING XHB_FIXTURE_DEFT_ATTENDING - inserted crest_sub_id '||xhb_csa_tt(k).sub_id||', def_on_case_id '||v_def_on_case_id||', v_cdf_id : '||v_cdf_id||' row with ETL_STATUS '||v_cfda_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_cfda_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
                 -- Update XHBSTG_CASE_SUB_APPEARANCE_DM table for the rows processed
                 UPDATE xhbstg_case_sub_appearance_dm xsa
                    SET    xsa.xhibit_etl_date   = SYSDATE
                          ,xsa.xhibit_court_id   = v_xhibit_court_id
                          ,xsa.xhibit_enrich_date = SYSDATE
                          ,xsa.xhibit_etl_status = v_cfda_upd_status
                  WHERE  xsa.crest_court_id   =  p_crest_court_id
                    AND  xsa.chd_id  = xhb_csa_tt(k).chd_id
                    AND  xsa.sub_id = xhb_csa_tt(k).sub_id;
                    
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2200:XHBSTG_CASE_SUB_APPEARANCE - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
       --  ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_c_diary_fixture_crest for CREST_COURT : '||p_crest_court_id||' ,case_no : '||v_case_no||' ,case_type : '||v_case_type||', v_chd_id : '||v_chd_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest - CTX-2199,CTX-2200'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_c_diary_fixture_crest - Error processing crest case_no : '||v_case_no||', v_case_type : '||v_Case_type||', v_chd_id : '||v_chd_id||', v_sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows+v_count_fda_no_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_CASE_SUB_APPEARANCE_DM
         -- tables with error status/messages
   
          UPDATE xhbstg_case_sub_appearance_dm xsa
            SET    xsa.xhibit_etl_date   = SYSDATE
                  ,xsa.xhibit_court_id   = v_xhibit_court_id
                  ,xsa.xhibit_enrich_date = SYSDATE
                  ,xsa.xhibit_etl_status = 'X' -- Error
                  ,xsa.xhibit_etl_err_message = v_err_message
            WHERE  xsa.crest_court_id   =  p_crest_court_id
              AND  xsa.chd_id = v_chd_id
              AND  xsa.sub_id = v_sub_id
              AND  xsa.case_no = v_case_no
              AND  xsa.case_type = v_case_type;
          
          END; -- CTX-3252
          
             END LOOP;
             
             v_count_fda_no_of_rows := v_count_fda_no_of_rows + xhb_csa_tt.COUNT;
  
           END IF;
    
           EXIT WHEN cur_crest_case_suject_app_det%NOTFOUND;
    
          END LOOP;
        CLOSE cur_crest_case_suject_app_det;      


EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
       --  ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_c_diary_fixture_crest for CREST_COURT : '||p_crest_court_id||' ,case_no : '||v_case_no||' ,case_type : '||v_case_type||', v_chd_id : '||v_chd_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest - CTX-2199,CTX-2200'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_c_diary_fixture_crest - Error processing crest case_no : '||v_case_no||', v_case_type : '||v_Case_type||', v_chd_id : '||v_chd_id||', v_sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows+v_count_fda_no_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_CASE_HEARING_DAY_DM
         -- tables with error status/messages
            UPDATE xhbstg_case_hearing_day_dm xch
            SET    xch.xhibit_etl_date   = SYSDATE
                  ,xch.xhibit_court_id   = v_xhibit_court_id
                  ,xch.xhibit_enrich_date = SYSDATE
                  ,xch.xhibit_etl_status = 'X' -- Error
                  ,xch.xhibit_etl_err_message = v_err_message
            WHERE  xch.crest_court_id   =  p_crest_court_id
              AND  xch.chd_id    = v_chd_id
              AND  xch.case_no   = v_case_no
              AND  xch.case_type = v_case_type
              AND  xch.list_type = 'X';

          END; -- CTX-3252
          
    END LOOP;


      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_cdf_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_cdf_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_cdf_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest- CTX-2199'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_DIARY_FIXTURE : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_CASE_HEARING_DAY_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
               
            -- Update XHBSTG_CASE_HEARING_DAY_DM table for the list type X rows NOT processed
            UPDATE xhbstg_case_hearing_day_dm xch
            SET    xch.xhibit_etl_date   = SYSDATE
                  ,xch.xhibit_court_id   = v_xhibit_court_id
                  ,xch.xhibit_enrich_date = SYSDATE
                  ,xch.xhibit_etl_status = 'N' -- Not processed
            WHERE  xch.crest_court_id   =  p_crest_court_id
              AND  xch.xhibit_etl_status is NULL 
              AND  xch.xhibit_enrich_date  is NULL
              AND  xch.xhibit_etl_date  is NULL
              AND  xch.xhibit_court_id is NULL
              AND  xch.list_type = 'X';
           
              v_cdf_np_rows := SQL%ROWCOUNT;
                         
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest- CTX-2199'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_DIARY_FIXTURE: updating XHBSTG_CASE_HEARING_DAY_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_cdf_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2199:XHBSTG_CASE_HEARING_DAY_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2199:XHBSTG_CASE_HEARING_DAY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_cdf_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
 
       insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest- CTX-2199,CTX-2200'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_FIXTURE_DEFT_ATTENDING : Processed '||v_count_fda_no_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_fda_no_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_CASE_SUB_APPEARANCE_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
        
        /* CTX-3216 Commented this out since it was stopping the table XHBSTG_CASE_SUB_APPEARANCE_DM from being processed again when the 
                    procedure upd_xhb_doc_on_list_crest was called later on during the data migration run        
            -- Update XHBSTG_CASE_SUB_APPEARANCE_DM table for the rows NOT processed
            UPDATE xhbstg_case_sub_appearance_dm xsa
            SET    xsa.xhibit_etl_date   = SYSDATE
                  ,xsa.xhibit_court_id   = v_xhibit_court_id
                  ,xsa.xhibit_enrich_date = SYSDATE
                  ,xsa.xhibit_etl_status = 'N' -- Not processed
            WHERE  xsa.crest_court_id   =  p_crest_court_id
              AND  xsa.xhibit_etl_status is NULL 
              AND  xsa.xhibit_enrich_date  is NULL
              AND  xsa.xhibit_etl_date  is NULL
              AND  xsa.xhibit_court_id is NULL;
           
              v_cfda_np_rows := SQL%ROWCOUNT;
                         
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest- CTX-2199,CTX-2200'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_FIXTURE_DEFT_ATTENDING: updating XHBSTG_CASE_SUB_APPEARANCE_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_cfda_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   
    */                     
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2200:XHBSTG_CASE_SUB_APPEARANCE_DM processed '||v_count_fda_no_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

    -- Commented out due to CTX-3216
    --DBMS_OUTPUT.PUT_LINE('CTX-2200:XHBSTG_CASE_SUB_APPEARANCE_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_cfda_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
       --  ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_c_diary_fixture_crest for CREST_COURT : '||p_crest_court_id||' ,case_no : '||v_case_no||' ,case_type : '||v_case_type||', v_chd_id : '||v_chd_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_c_diary_fixture_crest - CTX-2199,CTX-2200'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_c_diary_fixture_crest - Error processing crest case_no : '||v_case_no||', v_case_type : '||v_Case_type||', v_chd_id : '||v_chd_id||', v_sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows+v_count_fda_no_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
            COMMIT;
          
                     
END upd_xhb_c_diary_fixture_crest;

/**
  * NAME       : upd_xhb_dir_for_case_crest
  * DESCRIPTION: CTX-2206 New CTX fields for XHB_DIRECTIONS_FOR_CASE - Sec 4.3.2.15 req [4975.DM.019]
  *              CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_dir_for_case_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_dir_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , case_no                      data_mig.xhbstg_committal_charge_dm.case_no%TYPE
    , case_type                    data_mig.xhbstg_committal_charge_dm.case_type%TYPE
    , case_id                      xhibit.xhb_case.case_id%TYPE
    , trial_time_unit              xhibit.xhb_directions_for_case.trial_time_unit%TYPE
    , trial_time_estimate          xhibit.xhb_directions_for_case.trial_time_estimate%TYPE
    );

    TYPE xhb_dir_type IS TABLE OF xhb_dir_rec;
    xhb_dir_tt  xhb_dir_type;
      
    CURSOR cur_crest_dir_details IS
    SELECT cc.crest_court_id,
           xhc.court_id,
           cc.case_no,
           cc.case_type,
           xc.case_id,
           decode(cc.hrg_len_lo_unit,'H',1,'D',2,'W',3,'M',4,'Y',5) as trial_time_unit,
           cc.hrg_len_lo as trial_time_estimate
    from xhibit.xhb_court xhc,
         xhibit.xhb_case xc,
         data_mig.xhbstg_case_dm cc
    where cc.crest_court_id = p_crest_court_id and
          cc.crest_court_id = xhc.crest_court_id and 
          xc.court_id = xhc.court_id and
          xc.case_number = cc.case_no and
          xc.case_type = cc.case_type and 
          --NVL(cc.xhibit_etl_status,'N') not in ('I', 'U') and -- CC01112018 taken out because the case table is being processed before this is run
          --cc.xhibit_enrich_date is NULL  and -- CC01112018 taken out because the case table is being processed before this is run
          NOT exists  -- if CASE_ID exists, NO insert or UPDATE required, ignore CREST data
          (select 'X' from xhibit.xhb_directions_for_case 
            where case_id = xc.case_id and 
                  nvl(obs_ind,'N') != 'Y'); 

    v_count_number_of_rows   NUMBER := 0;
    v_dir_ins_rows           NUMBER := 0;
    v_case_id                xhibit.xhb_case.case_id%TYPE;
    v_case_no                data_mig.xhbstg_case_dm.case_no%TYPE; 
    v_case_type              data_mig.xhbstg_case_dm.case_type%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
    BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_dir_for_case_crest- CTX-2206'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DIRECTIONS_FOR_CASE - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_CHARGES_LOG with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_CHARGES_LOG table that have not yet been populated with CREST data
    OPEN cur_crest_dir_details;
    LOOP
    FETCH cur_crest_dir_details BULK COLLECT INTO xhb_dir_tt LIMIT g_bulk_limit;
   
    IF xhb_dir_tt IS NOT NULL AND xhb_dir_tt.COUNT > 0 THEN
   
        FOR i IN xhb_dir_tt.FIRST .. xhb_dir_tt.LAST LOOP

        BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

            -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_case_id     := xhb_dir_tt(i).case_id;
            v_case_no     := xhb_dir_tt(i).case_no;
            v_case_type    := xhb_dir_tt(i).case_type;
    
            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_dir_for_case_crest- CTX-2206'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'UPDATING XHB_DIRECTIONS_FOR_CASE - inserting  case id - '||xhb_dir_tt(i).case_id||', case_no : '||v_case_no||', case_type : '||v_case_type
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('INSERTING XHB_CHARGES_LOG - FOR CREST_COURT_ID = '||xhb_dir_tt(i).crest_court_id);
    
            DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||'v_case_type : '||v_case_type||', v_case_id : '||v_case_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
            
            -- INSERT and populate this NEW table XHB_DIRECTIONS_FOR_CASE with data from CREST  
            INSERT INTO  xhibit.xhb_directions_for_case
                          ( directions_for_case_id
                          , case_id 
                          , trial_time_unit   
                          , trial_time_estimate 
                          , creation_date
                          , created_by  
                          , last_update_date 
                          , last_updated_by )
                   VALUES 
                          (xhibit.xhb_dir_for_case_seq.nextval
                          ,xhb_dir_tt(i).case_id
                          ,xhb_dir_tt(i).trial_time_unit
                          ,xhb_dir_tt(i).trial_time_estimate
                          ,SYSDATE
                          ,'DATA_MIGRATION'
                          ,SYSDATE
                          ,'DATA_MIGRATION');      
                  


    
                v_dir_ins_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2206:XHB_DIRECTIONS_FOR_CASE - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_dir_ins_rows);
    
               -- Update XHBSTG_CASE_DM table is NOT required as
               -- this is NOT the main table that is migrated amd
               --only 2 columns are referenced for the rows processed
 
         EXCEPTION
            WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_directions_for_case_crest for CREST_COURT : '||p_crest_court_id||',case_id : '||v_case_id||' , crest_case_no : '||v_case_no||'-'||SUBSTR(v_err_message,1,100));
        
                   insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                 ,p_action_name        => 'upd_xhb_dir_for_case_crest - CTX-2206'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'upd_xhb_dir_for_case_crest - Error processing crest_case_no : '||v_case_no||' , case_id : '||v_case_id||' Error: '||SUBSTR(v_err_message,1,150)
                                 ,p_err_row_count      => NULL 
                                 ,p_success_row_count  => v_count_number_of_rows
                                 ,p_last_updated_by    => 'DATA MIGRATION'
                                 ,p_created_by         => 'DATA MIGRATION'
                                 );
 
        END;-- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
        
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_dir_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_dir_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_dir_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_dir_for_case_crest- CTX-2206'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DIRECTIONS_FOR_CASE : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2206:XHB_DIRECTIONS_FOR_CASE inserted '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; --CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_directions_for_case_crest for CREST_COURT : '||p_crest_court_id||',case_id : '||v_case_id||' , crest_case_no : '||v_case_no||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_dir_for_case_crest - CTX-2206'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_dir_for_case_crest - Error processing crest_case_no : '||v_case_no||' , case_id : '||v_case_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
   
          COMMIT;
         
          
                     
END upd_xhb_dir_for_case_crest;

/**
  * NAME       : upd_xhb_ref_jud_tckt_crest
  * DESCRIPTION: CTX-2203 New CTX fields for XHB_REF_JUDGE_TICKET_TYPE - Sec 4.3.2.20 req [4975.DM.025]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_ref_jud_tckt_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_cjt_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , judge_id                     xhibit.xhb_ref_judge_ticket.judge_id%TYPE
    , ticket_type                  xhibit.xhb_ref_judge_ticket.ticket_type%TYPE
    , crest_jud_id                 xhbstg_judge_ticket_dm.jud_id%TYPE
    );

    TYPE xhb_cjt_type IS TABLE OF xhb_cjt_rec;
    xhb_cjt_tt  xhb_cjt_type;
      
    CURSOR cur_crest_cjt_details IS
   SELECT cjt.crest_court_id,
           xhc.court_id,
           xrj.ref_judge_id as judge_id,
           cjt.ticket_type,
           cjt.jud_id
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_judge_ticket_dm cjt,
         xhibit.xhb_ref_judge xrj
    where cjt.crest_court_id = p_crest_court_id and
          cjt.crest_court_id = xhc.crest_court_id and
          xrj.court_id = xhc.court_id and
          xrj.crest_judge_id = cjt.jud_id and   
          NVL(cjt.xhibit_etl_status,'N') not in ('I', 'U') and
          cjt.xhibit_enrich_date is NULL  and
          NOT exists 
          (select 'X' from xhibit.xhb_ref_judge_ticket
            where court_id = xhc.court_id and 
                  judge_id = xrj.ref_judge_id and 
                  nvl(obs_ind,'N') != 'Y'); 

    v_count_number_of_rows   NUMBER := 0;
    v_cjt_ins_rows           NUMBER := 0;
    v_cjt_ins_status         CHAR(1) := 'N';
    v_cjt_np_rows            NUMBER := 0;
    v_judge_id               xhibit.xhb_ref_judge_ticket.judge_id%TYPE;
    v_crest_jud_id                 xhbstg_judge_ticket_dm.jud_id%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_ref_jud_tckt_crest- CTX-2203'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_REF_JUDGE_TICKET - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_CHARGES_LOG with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_CHARGES_LOG table that have not yet been populated with CREST data
    OPEN cur_crest_cjt_details;
    LOOP
    FETCH cur_crest_cjt_details BULK COLLECT INTO xhb_cjt_tt LIMIT g_bulk_limit;
   
    IF xhb_cjt_tt IS NOT NULL AND xhb_cjt_tt.COUNT > 0 THEN
   
        FOR i IN xhb_cjt_tt.FIRST .. xhb_cjt_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                   ,p_action_name        => 'upd_xhb_ref_jud_tckt_crest- CTX-2203'
                   ,p_run_time           =>  sysdate
                   ,p_log_msg_type       =>  'I' -- Information
                   ,p_log_msg            =>  'INSERTING XHB_REF_JUDGE_TICKET - inserting  judge id - '||xhb_cjt_tt(i).judge_id||', crest_jud_id : '||xhb_cjt_tt(i).crest_jud_id
                   ,p_err_row_count      =>  NULL
                   ,p_success_row_count  => NULL
                   ,p_last_updated_by    => 'DATA MIGRATION'
                   ,p_created_by         => 'DATA MIGRATION'
                   );
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_REF_JUDGE_TICKET- FOR CREST_COURT_ID = '||xhb_cjt_tt(i).crest_court_id);
                
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_judge_id     := xhb_cjt_tt(i).judge_id;
                v_crest_jud_id     := xhb_cjt_tt(i).crest_jud_id;
                DBMS_OUTPUT.PUT_LINE('v_judge_id : '||v_judge_id||', v_jud_id : '||v_crest_jud_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
                
                -- INSERT and populate this NEW table XHB_REF_JUDGE_TICKET with data from CREST  
                INSERT INTO  xhibit.xhb_ref_judge_ticket
                            ( ref_judge_ticket_id
                            , court_id 
                            , judge_id   
                            , ticket_type 
                            , creation_date
                            , created_by  
                            , last_update_date 
                            , last_updated_by )
                     VALUES 
                            (xhibit.xhb_ref_judge_ticket_seq.nextval
                            ,xhb_cjt_tt(i).court_id
                            ,xhb_cjt_tt(i).judge_id
                            ,xhb_cjt_tt(i).ticket_type
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,'DATA_MIGRATION');      
                
                
                
                
                v_cjt_ins_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2203:XHB_REF_JUDGE_TICKET - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cjt_ins_rows);
    
                 -- Update XHBSTG_JUDGE_TICKET_DM table for the rows processed
                IF v_cjt_ins_rows > 0 THEN 
                    v_cjt_ins_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_cjt_ins_rows = 0 THEN
                    v_cjt_ins_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                           ,p_action_name        => 'upd_xhb_ref_jud_tckt_crest- CTX-2203'
                           ,p_run_time           =>  sysdate
                           ,p_log_msg_type       =>  'I' -- Information
                           ,p_log_msg            =>  'UPDATING XHBSTG_JUSGE_TICKET_DM - updated crest_jud_id '||xhb_cjt_tt(i).crest_jud_id||' row with ETL_STATUS '||v_cjt_ins_status
                           ,p_err_row_count      =>  NULL
                           ,p_success_row_count  => v_cjt_ins_rows
                           ,p_last_updated_by    => 'DATA MIGRATION'
                           ,p_created_by         => 'DATA MIGRATION'
                           );
                           
                
                -- Update XHBSTG_JUDGE_TICKET_DM table for the rows processed
                UPDATE xhbstg_judge_ticket_dm xcg
                SET    xcg.xhibit_etl_date   = SYSDATE
                    ,xcg.xhibit_court_id   = v_xhibit_court_id
                    ,xcg.xhibit_enrich_date = SYSDATE
                    ,xcg.xhibit_etl_status = v_cjt_ins_status
                WHERE  xcg.crest_court_id   =  p_crest_court_id
                AND  xcg.jud_id = xhb_cjt_tt(i).crest_jud_id;     
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2203:XHBSTG_JUDGE_TICKET_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
    
            EXCEPTION
                  WHEN OTHERS THEN
                      v_err_message := SQLERRM;
                      
                      DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_charges_log_crest for CREST_COURT : '||p_crest_court_id||',crest_jud_id : '||v_crest_jud_id||'-'||SUBSTR(v_err_message,1,100));
              
                         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                       ,p_action_name        => 'upd_xhb_ref_jud_tckt_crest - CTX-2203'
                                       ,p_run_time           =>  sysdate
                                       ,p_log_msg_type       =>  'E' -- Error
                                       ,p_log_msg            =>  'upd_xhb_ref_jud_tckt_crest - Error processing crest_jud_id : '||v_crest_jud_id||' Error: '||SUBSTR(v_err_message,1,150)
                                       ,p_err_row_count      => NULL 
                                       ,p_success_row_count  => v_count_number_of_rows
                                       ,p_last_updated_by    => 'DATA MIGRATION'
                                       ,p_created_by         => 'DATA MIGRATION'
                                       );
                       -- Update XHBSTG_JUDGE_TICKET_DM
                       -- tables with error status/messages
                       BEGIN
                          UPDATE xhbstg_judge_ticket_dm xcjt
                          SET    xcjt.xhibit_etl_date   = SYSDATE
                                ,xcjt.xhibit_court_id   = v_xhibit_court_id
                                ,xcjt.xhibit_enrich_date = SYSDATE
                                ,xcjt.xhibit_etl_status = 'X' -- Error
                                ,xcjt.xhibit_etl_err_message = v_err_message
                          WHERE  xcjt.crest_court_id   =  p_crest_court_id
                            AND  xcjt.jud_id = v_crest_jud_id;
                        END;
                            
                
            END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
        END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
      v_count_number_of_rows := v_count_number_of_rows + xhb_cjt_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_cjt_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_cjt_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_ref_jud_tckt_crest- CTX-2203'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_REF_JUDGE_TICKET : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_JUDGE_TICKET_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
               
                       -- Update XHBSTG_JUDGE_TICKET_DM table for the rows NOT processed
            UPDATE xhbstg_judge_ticket_dm xcg
            SET    xcg.xhibit_etl_date   = SYSDATE
                  ,xcg.xhibit_court_id   = v_xhibit_court_id
                  ,xcg.xhibit_enrich_date = SYSDATE
                  ,xcg.xhibit_etl_status = 'N' -- Not processed
            WHERE  xcg.crest_court_id   =  p_crest_court_id
              AND  xcg.xhibit_etl_status is NULL 
              AND  xcg.xhibit_enrich_date  is NULL
              AND  xcg.xhibit_etl_date  is NULL
              AND  xcg.xhibit_court_id is NULL;
           
              v_cjt_np_rows := SQL%ROWCOUNT;
                         
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_ref_jud_tckt_crest- CTX-2203'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_REF_JUDGE_TICKET : updating XHBSTG_JUDGE_TICKET_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_cjt_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2203:XHBSTG_JUDGE_TICKET_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2203:XHBSTG_JUDGE_TICKET_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_cjt_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; --CTX-3252
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_charges_log_crest for CREST_COURT : '||p_crest_court_id||',crest_jud_id : '||v_crest_jud_id||'-'||SUBSTR(v_err_message,1,100));

        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_ref_jud_tckt_crest - CTX-2203'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_ref_jud_tckt_crest - Error processing crest_jud_id : '||v_crest_jud_id||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_count_number_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
        
        COMMIT;
          
                     
END upd_xhb_ref_jud_tckt_crest;

/**
  * NAME       : upd_xhb_bw_history_crest
  * DESCRIPTION: CTX-2204 New CTX fields for XHB_BW_HISTORY_TYPE - Sec 4.3.2.21 req [4975.DM.026]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_bw_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_bwh_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , defendant_on_case_id         xhibit.xhb_defendant_on_case.defendant_on_case_id%TYPE
    , bw_issue_date                xhibit.xhb_bw_history.bw_issue_date%TYPE
    , bw_exec_date                 xhibit.xhb_bw_history.bw_end_date%TYPE
    , bc_status_bw_issued          xhibit.xhb_bw_history.bc_status_bw_issued%TYPE
    , bc_status_bw_executed        xhibit.xhb_bw_history.bc_status_bw_ended%TYPE
    , withdrawn                    xhibit.xhb_bw_history.withdrawn%TYPE
    , absconding                   xhibit.xhb_bw_history.absconding%TYPE
    , case_no                      xhbstg_bw_history_dm.case_no%TYPE
    , case_type                    xhbstg_bw_history_dm.case_type%TYPE
    , sub_id                       xhbstg_bw_history_dm.sub_id%TYPE
    );

    TYPE xhb_bwh_type IS TABLE OF xhb_bwh_rec;
    xhb_bwh_tt  xhb_bwh_type;
      
    CURSOR cur_crest_bwh_details IS
   SELECT bwh.crest_court_id,
           xhc.court_id,
           xdc.defendant_on_case_id,
           bwh.bw_issue_date,
           bwh.bw_exec_date,
           bwh.bc_status_bw_issued,
           bwh.bc_status_bw_executed,
           NULL as withdrawn,
           NULL as absconding,
           bwh.case_no,
           bwh.case_type,
           bwh.sub_id
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_bw_history_dm bwh,
         xhibit.xhb_defendant_on_case xdc,
         xhibit.xhb_defendant xd,
         xhibit.xhb_case xc
    where bwh.crest_court_id = p_crest_court_id and
          bwh.crest_court_id = xhc.crest_court_id and
          xd.court_id = xhc.court_id and
          xc.court_id = xhc.court_id and 
          xc.case_number = bwh.case_no and
          xc.case_type = bwh.case_type and
          xd.crest_defendant_id = bwh.sub_id and
          xd.defendant_id = xdc.defendant_id and
          xdc.case_id = xc.case_id and
          NVL(bwh.xhibit_etl_status,'N') not in ('I', 'U') and
          bwh.xhibit_enrich_date is NULL and
          NOT exists 
          (select 'X' from xhibit.xhb_bw_history
            where defendant_on_case_id = xdc.defendant_on_case_id and
                  nvl(obs_ind,'N') != 'Y');
          
    v_count_number_of_rows   NUMBER := 0;
    v_bwh_ins_rows           NUMBER := 0;
    v_bwh_ins_status         CHAR(1) := 'N';
    v_bwh_np_rows            NUMBER := 0;
    v_sub_id                 xhbstg_bw_history_dm.sub_id%TYPE;
    v_case_type              xhbstg_bw_history_dm.case_type%TYPE;
    v_case_no                xhbstg_bw_history_dm.case_no%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_bw_history_crest- CTX-2204'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_BW_HISTORY - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_BW_HISTORY with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_BW_HISTORY table that have not yet been populated with CREST data
    OPEN cur_crest_bwh_details;
    LOOP
    FETCH cur_crest_bwh_details BULK COLLECT INTO xhb_bwh_tt LIMIT g_bulk_limit;
   
    IF xhb_bwh_tt IS NOT NULL AND xhb_bwh_tt.COUNT > 0 THEN
   
        FOR i IN xhb_bwh_tt.FIRST .. xhb_bwh_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_bw_history_crest- CTX-2204'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'INSERTING XHB_BW_HISTORY - inserting  sub_id - '||xhb_bwh_tt(i).sub_id||', case_no : '||xhb_bwh_tt(i).case_no||', case_type : '||xhb_bwh_tt(i).case_type
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_BW_HISTORY- FOR CREST_COURT_ID = '||xhb_bwh_tt(i).crest_court_id);
                
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_case_no     := xhb_bwh_tt(i).case_no;
                v_case_type     := xhb_bwh_tt(i).case_type;
                v_sub_id     := xhb_bwh_tt(i).sub_id;

                DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_sub_id : '||v_sub_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
                
                -- INSERT and populate this NEW table XHB_BW_HISTORY with data from CREST  
                INSERT INTO  xhibit.XHB_BW_HISTORY
                            ( bw_history_id
                            , defendant_on_case_id 
                            , bw_issue_date   
                            , bw_end_date
                            , bc_status_bw_issued
                            , bc_status_bw_ended 
                            , creation_date
                            , created_by  
                            , last_update_date 
                            , last_updated_by )
                     VALUES 
                            (xhibit.xhb_bw_history_seq.nextval
                            ,xhb_bwh_tt(i).defendant_on_case_id
                            ,xhb_bwh_tt(i).bw_issue_date
                            ,xhb_bwh_tt(i).bw_exec_date
                            ,xhb_bwh_tt(i).bc_status_bw_issued
                            ,xhb_bwh_tt(i).bc_status_bw_executed
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,'DATA_MIGRATION');      
                
                
                v_bwh_ins_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2204:XHB_BW_HISTORY - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_bwh_ins_rows);
                
                   -- Update XHBSTG_BW_HISTORY_DM table for the rows processed
                IF v_bwh_ins_rows > 0 THEN 
                v_bwh_ins_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_bwh_ins_rows = 0 THEN
                v_bwh_ins_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_bw_history_crest- CTX-2204'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'UPDATING XHBSTG_BW_HISTORY_DM - updated sub_id '||xhb_bwh_tt(i).sub_id||' row with ETL_STATUS '||v_bwh_ins_status
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => v_bwh_ins_rows
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
                             
                
                -- Update XHBSTG_BW_HISTORY_DM table for the rows processed
                UPDATE xhbstg_bw_history_dm xcg
                SET    xcg.xhibit_etl_date   = SYSDATE
                      ,xcg.xhibit_court_id   = v_xhibit_court_id
                      ,xcg.xhibit_enrich_date = SYSDATE
                      ,xcg.xhibit_etl_status = v_bwh_ins_status
                WHERE  xcg.crest_court_id   =  p_crest_court_id
                  AND  xcg.sub_id = xhb_bwh_tt(i).sub_id
                  AND  xcg.case_no = xhb_bwh_tt(i).case_no
                  AND  xcg.case_type = xhb_bwh_tt(i).case_type;     
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2204:XHBSTG_BW_HISTORY_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
 
           EXCEPTION
              WHEN OTHERS THEN
                  v_err_message := SQLERRM;
                  
                  DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_XHB_BW_HISTORY_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||'-'||SUBSTR(v_err_message,1,100));
          
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'upd_xhb_bw_history_crest - CTX-2204'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'E' -- Error
                               ,p_log_msg            =>  'upd_xhb_bw_history_crest - Error processing sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                               ,p_err_row_count      => NULL 
                               ,p_success_row_count  => v_count_number_of_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
                               
                  -- Update XHBSTG_BW_HISTORY_DM
                  -- tables with error status/messages
                  BEGIN
                  UPDATE xhbstg_bw_history_dm xbw
                  SET    xbw.xhibit_etl_date   = SYSDATE
                        ,xbw.xhibit_court_id   = v_xhibit_court_id
                        ,xbw.xhibit_enrich_date = SYSDATE
                        ,xbw.xhibit_etl_status = 'X' -- Error
                        ,xbw.xhibit_etl_err_message = v_err_message
                  WHERE  xbw.crest_court_id   =  p_crest_court_id
                    AND  xbw.sub_id = v_sub_id
                    AND  xbw.case_no = v_case_no
                    AND  xbw.case_type = v_case_type;
                  END;
 
            END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    
    v_count_number_of_rows := v_count_number_of_rows + xhb_bwh_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_bwh_details%NOTFOUND;
    
    END LOOP;
        
    CLOSE cur_crest_bwh_details;

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_bw_history_crest- CTX-2204'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_BW_HISTORY : Processed '||v_count_number_of_rows||' successfully!'
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_count_number_of_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );
     ------ Update rest of the Unprocessed rows in XHBSTG_BW_HISTORY_DM 
     ------ for given CREST COURT ID to 'N' i.e. NOT processed
     
             -- Update XHBSTG_BW_HISTORY_DM table for the rows NOT processed
    UPDATE xhbstg_bw_history_dm xbh
    SET    xbh.xhibit_etl_date   = SYSDATE
        ,xbh.xhibit_court_id   = v_xhibit_court_id
        ,xbh.xhibit_enrich_date = SYSDATE
        ,xbh.xhibit_etl_status = 'N' -- Not processed
    WHERE  xbh.crest_court_id   =  p_crest_court_id
    AND  xbh.xhibit_etl_status is NULL 
    AND  xbh.xhibit_enrich_date  is NULL
    AND  xbh.xhibit_etl_date  is NULL
    AND  xbh.xhibit_court_id is NULL;
    
    v_bwh_np_rows := SQL%ROWCOUNT;
               
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_bw_history_crest- CTX-2204'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_BW_HISTORY : updating XHBSTG_BW_HISTORY_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_bwh_np_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2204:XHBSTG_BW_HISTORY_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2204:XHBSTG_BW_HISTORY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_bwh_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
    --ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        
        --ROLLBACK; --CTX-3252
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_XHB_BW_HISTORY_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||'-'||SUBSTR(v_err_message,1,100));
        
         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                       ,p_action_name        => 'upd_xhb_bw_history_crest - CTX-2204'
                       ,p_run_time           =>  sysdate
                       ,p_log_msg_type       =>  'E' -- Error
                       ,p_log_msg            =>  'upd_xhb_bw_history_crest - Error processing sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                       ,p_err_row_count      => NULL 
                       ,p_success_row_count  => v_count_number_of_rows
                       ,p_last_updated_by    => 'DATA MIGRATION'
                       ,p_created_by         => 'DATA MIGRATION'
                       );
        
        COMMIT;
                     
END upd_xhb_bw_history_crest;

/**
  * NAME       : upd_xhb_mon_ord_track_crest
  * DESCRIPTION: CTX-2205 New CTX fields for XHB_MONETARY_ORDER_TRACKING - Sec 4.3.2.22 req [4975.DM.027]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * 12/03/2019 :  S Sethuraman - CTX-3844 - UPD_XHB_MON_ORD_TRACK_CREST, XHB_UPD_OUT_BW_HISTORY - Update Staging DISPOSAL_DM table back for updat status with disp_id filter included - both CURSORS include LIST_DATE > SYSDATE to migrate FURTURE FIXTURES ONLY of list_type = 'X'
  * 04/06/2019 : S Sethuraman - CTX-4284 - DO NOT MAP if ALL of MO_COSTS,MO_FINE AND MO_COMPENSATION are NULL or 0
**/
PROCEDURE upd_xhb_mon_ord_track_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_mot_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , case_id                      xhibit.xhb_case.case_id%TYPE
    , defendant_on_case_id         xhibit.xhb_defendant_on_case.defendant_on_case_id%TYPE
    , order_date                   xhibit.xhb_monetary_order_tracking.order_date%TYPE
    , fined                        xhibit.xhb_monetary_order_tracking.fined%TYPE
    , costs                        xhibit.xhb_monetary_order_tracking.costs%TYPE
    , compensation                 xhibit.xhb_monetary_order_tracking.compensation%TYPE
    , collect_magistrates_court_id xhibit.xhb_monetary_order_tracking.collect_magistrates_court_id%TYPE
    , acknowledgement_date         xhibit.xhb_monetary_order_tracking.acknowledgement_date%TYPE
    , case_no                      xhbstg_disposal_dm.case_no%TYPE
    , case_type                    xhbstg_disposal_dm.case_type%TYPE
    , sub_id                       xhbstg_disposal_dm.sub_id%TYPE
    , dis_id                       xhbstg_disposal_dm.dis_id%TYPE -- CTX-3844
    , disposal_code                xhbstg_disposal_dm.disposal_code%TYPE    -- CTX-3844 
    );

    TYPE xhb_mot_type IS TABLE OF xhb_mot_rec;
    xhb_mot_tt  xhb_mot_type;
      
    CURSOR cur_crest_mot_details IS
   SELECT dis.crest_court_id,
           xhc.court_id,
           xc.case_id,
           xdc.defendant_on_case_id,
           dis.outcome_date,
           dis.mo_fine,
           dis.mo_costs,
           dis.mo_compensation,
           (select x.ref_court_id from xhibit.xhb_ref_court x 
             where x.court_id = xhc.court_id and
                   x.crest_code = dis.psd_ct_code ) as collect_magistrates_court_id,
           dis.mo_acknowledgement_date,
           dis.case_no,
           dis.case_type,
           dis.sub_id,
           dis.dis_id, -- CTX-3844
           dis.disposal_code -- CTX-3844
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_disposal_dm dis,
         xhibit.xhb_defendant_on_case xdc,
         xhibit.xhb_defendant xd,
         xhibit.xhb_case xc
    where dis.crest_court_id = p_crest_court_id and
          dis.crest_court_id = xhc.crest_court_id and
          dis.disposal_code = 'NMO' and -- ONLY EXTRACT NMO DISPOSAL CODE
          (nvl(dis.mo_costs,0) <> 0 OR nvl(dis.mo_compensation,0) <> 0 OR nvl(dis.mo_fine,0) <> 0) and --CTX-4284
          xd.court_id = xhc.court_id and
          xc.court_id = xhc.court_id and 
          xc.case_number = dis.case_no and
          xc.case_type = dis.case_type and
          xd.crest_defendant_id = dis.sub_id and
          xd.defendant_id = xdc.defendant_id and
          xdc.case_id = xc.case_id and
          NVL(dis.xhibit_etl_status,'N') not in ('I', 'U') and
          dis.xhibit_enrich_date is NULL and
          NOT exists 
          (select 'X' from xhibit.xhb_monetary_order_tracking
            where case_id = xc.case_id and
                  defendant_on_case_id = xdc.defendant_on_case_id and
                  nvl(obs_ind,'N') != 'Y');
          
    v_count_number_of_rows   NUMBER := 0;
    v_mot_ins_rows           NUMBER := 0;
    v_mot_ins_status         CHAR(1) := 'N';
    v_mot_np_rows            NUMBER := 0;
    v_sub_id                 XHBSTG_DISPOSAL_DM.sub_id%TYPE;
    v_dis_id                 XHBSTG_DISPOSAL_DM.dis_id%TYPE;
    v_disposal_code          XHBSTG_DISPOSAL_DM.DISPOSAL_CODE%TYPE;    
    v_case_type              XHBSTG_DISPOSAL_DM.case_type%TYPE;
    v_case_no                XHBSTG_DISPOSAL_DM.case_no%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_mon_ord_track_crest- CTX-2205'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_MONETARY_ORDER_TRACKING - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_MONETARY_ORDER_TRACKING with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_MONETARY_ORDER_TRACKING table that have not yet been populated with CREST data
    OPEN cur_crest_mot_details;
    LOOP
    FETCH cur_crest_mot_details BULK COLLECT INTO xhb_mot_tt LIMIT g_bulk_limit;
   
    IF xhb_mot_tt IS NOT NULL AND xhb_mot_tt.COUNT > 0 THEN
   
        FOR i IN xhb_mot_tt.FIRST .. xhb_mot_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_mon_ord_track_crest- CTX-2205'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'INSERTING XHB_MONETARY_ORDER_TRACKING - inserting  dis_id - '||xhb_mot_tt(i).dis_id||', disposal_code - '||xhb_mot_tt(i).disposal_code||', sub_id - '||xhb_mot_tt(i).sub_id||', case_no : '||xhb_mot_tt(i).case_no||', case_type : '||xhb_mot_tt(i).case_type
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_MONETARY_ORDER_TRACKING- FOR CREST_COURT_ID = '||xhb_mot_tt(i).crest_court_id);
                
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_case_no     := xhb_mot_tt(i).case_no;
                v_case_type     := xhb_mot_tt(i).case_type;
                v_sub_id     := xhb_mot_tt(i).sub_id;
                v_dis_id     := xhb_mot_tt(i).dis_id; -- CTX-3844
                v_disposal_code    := xhb_mot_tt(i).disposal_code; -- CTX-3844
                                                
                DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_sub_id : '||v_sub_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
                
                -- INSERT and populate this NEW table XHB_MONETARY_ORDER_TRACKING with data from CREST  
                INSERT INTO  xhibit.XHB_MONETARY_ORDER_TRACKING
                            ( monetary_order_tracking_id
                            , case_id
                            , defendant_on_case_id 
                            , order_date   
                            , fined
                            , costs
                            , compensation
                            , collect_magistrates_court_id
                            , acknowledgement_date 
                            , creation_date
                            , created_by  
                            , last_update_date 
                            , last_updated_by )
                     VALUES 
                            (xhibit.xhb_monetary_order_track_seq.nextval
                            ,xhb_mot_tt(i).case_id
                            ,xhb_mot_tt(i).defendant_on_case_id
                            ,xhb_mot_tt(i).order_date
                            ,xhb_mot_tt(i).fined
                            ,xhb_mot_tt(i).costs
                            ,xhb_mot_tt(i).compensation
                            ,xhb_mot_tt(i).collect_magistrates_court_id
                            ,xhb_mot_tt(i).acknowledgement_date
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,'DATA_MIGRATION');      
                  
                v_mot_ins_rows := SQL%ROWCOUNT;
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2205:XHB_MONETARY_ORDER_TRACKING - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_mot_ins_rows);
                
                -- Update XHBSTG_DISPOSAL_DM table for the rows processed
                IF v_mot_ins_rows > 0 THEN 
                    v_mot_ins_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_mot_ins_rows = 0 THEN
                    v_mot_ins_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_mon_ord_track_crest- CTX-2205'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_DISPOSAL_DM - updated sub_id '||xhb_mot_tt(i).sub_id||', dis_id - '||xhb_mot_tt(i).dis_id||', disposal_code - '||xhb_mot_tt(i).disposal_code||' row with ETL_STATUS '||v_mot_ins_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_mot_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         

                -- Update XHBSTG_DISPOSAL_DM table for the rows processed
                UPDATE XHBSTG_DISPOSAL_DM xcg
                SET    xcg.xhibit_etl_date   = SYSDATE
                      ,xcg.xhibit_court_id   = v_xhibit_court_id
                      ,xcg.xhibit_enrich_date = SYSDATE
                      ,xcg.xhibit_etl_status = v_mot_ins_status
                WHERE  xcg.crest_court_id   =  p_crest_court_id
                  AND  xcg.sub_id = xhb_mot_tt(i).sub_id
                  AND  xcg.case_no = xhb_mot_tt(i).case_no
                  AND  xcg.case_type = xhb_mot_tt(i).case_type
                  AND  xcg.dis_id = xhb_mot_tt(i).dis_id -- CTX-3844
                  AND  xcg.disposal_code = xhb_mot_tt(i).disposal_code;    -- CTX-3844 
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2205:XHBSTG_DISPOSAL_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

        EXCEPTION
            WHEN OTHERS THEN

                v_err_message := SQLERRM;
                
                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_mon_ord_track_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||',dis_id : '||v_dis_id||',disposal_code : '||v_disposal_code||'-'||SUBSTR(v_err_message,1,100));
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_mon_ord_track_crest - CTX-2205'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'E' -- Error
                             ,p_log_msg            =>  'upd_xhb_mon_ord_track_crest - Error processing sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                             ,p_err_row_count      => NULL 
                             ,p_success_row_count  => v_count_number_of_rows
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
                -- Update XHBSTG_DISPOSAL_DM
                -- tables with error status/messages
                BEGIN
                UPDATE XHBSTG_DISPOSAL_DM xbw
                SET    xbw.xhibit_etl_date   = SYSDATE
                      ,xbw.xhibit_court_id   = v_xhibit_court_id
                      ,xbw.xhibit_enrich_date = SYSDATE
                      ,xbw.xhibit_etl_status = 'X' -- Error
                      ,xbw.xhibit_etl_err_message = v_err_message
                WHERE  xbw.crest_court_id   =  p_crest_court_id
                  AND  xbw.sub_id = v_sub_id
                  AND  xbw.case_no = v_case_no
                  AND  xbw.case_type = v_case_type
                  AND  xbw.dis_id = v_dis_id -- CTX-3844
                  AND  xbw.disposal_code = v_disposal_code;    -- CTX-3844 
                
                END;

        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_mot_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_mot_details%NOTFOUND;
    
    END LOOP;
        
    CLOSE cur_crest_mot_details;

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'upd_xhb_mon_ord_track_crest- CTX-2205'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_MONETARY_ORDER_TRACKING : Processed '||v_count_number_of_rows||' successfully!'
                 ,p_err_row_count      => NULL 
                 ,p_success_row_count  => v_count_number_of_rows
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );
       ------ Update rest of the Unprocessed rows in XHBSTG_DISPOSAL_DM 
       ------ for given CREST COURT ID to 'N' i.e. NOT processed
       
               -- Update XHBSTG_DISPOSAL_DM table for the rows NOT processed
    UPDATE XHBSTG_DISPOSAL_DM xbh
    SET    xbh.xhibit_etl_date   = SYSDATE
          ,xbh.xhibit_court_id   = v_xhibit_court_id
          ,xbh.xhibit_enrich_date = SYSDATE
          ,xbh.xhibit_etl_status = 'N' -- Not processed
    WHERE  xbh.crest_court_id   =  p_crest_court_id
      AND  xbh.xhibit_etl_status is NULL 
      AND  xbh.xhibit_enrich_date  is NULL
      AND  xbh.xhibit_etl_date  is NULL
      AND  xbh.xhibit_court_id is NULL;
    
    v_mot_np_rows := SQL%ROWCOUNT;
               
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_mon_ord_track_crest- CTX-2205'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_MONETARY_ORDER_TRACKING : updating XHBSTG_DISPOSAL_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_mot_np_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );   
                       
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2205:XHBSTG_DISPOSAL_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2205:XHBSTG_JBW_HISTORY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_mot_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN

        v_err_message := SQLERRM;
        
        --ROLLBACK; --CTX-3252
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_mon_ord_track_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||'-'||SUBSTR(v_err_message,1,100));
        
        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_mon_ord_track_crest - CTX-2205'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_mon_ord_track_crest - Error processing dis_id : '||v_dis_id||', sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_count_number_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
        
        COMMIT;
                     
END upd_xhb_mon_ord_track_crest;

/**
  * NAME       : upd_xhb_croom_usage_crest
  * DESCRIPTION: CTX-2659 New CTX fields for XHB_COURT_ROOM_USAGE - Sec 4.3.2.25 req [4975.DM.028]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * 05/02/2019 - S Sethuraman - CTX-3654 - Fix incorrect data_type assignments to v_ctl_id,v_courtroom_no
  * 23/04/2019  - S Sethuraman - CTX-4120 : courtroom_usage - rsit report issues -default 0 set to HOURS / MINS if NULL
**/
PROCEDURE upd_xhb_croom_usage_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_cru_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_room_no                xhbstg_courtroom_usage_dm.courtroom_no%TYPE
    , ctl_id                       xhbstg_courtroom_usage_dm.ctl_id%TYPE
    , cru_id                       xhbstg_courtroom_usage_dm.cru_id%TYPE
    , court_room_id                xhibit.xhb_court_room_usage.court_room_id%TYPE
    , am_time_civ_hours            xhibit.xhb_court_room_usage.am_time_civ_hours%TYPE
    , am_time_civ_mins             xhibit.xhb_court_room_usage.am_time_civ_mins%TYPE
    , am_time_hours                xhibit.xhb_court_room_usage.am_time_hours%TYPE
    , am_time_mins                 xhibit.xhb_court_room_usage.am_time_mins%TYPE
    , pm_time_civ_hours            xhibit.xhb_court_room_usage.pm_time_civ_hours%TYPE
    , pm_time_civ_mins             xhibit.xhb_court_room_usage.pm_time_civ_mins%TYPE
    , pm_time_hours                xhibit.xhb_court_room_usage.pm_time_hours%TYPE
    , pm_time_mins                 xhibit.xhb_court_room_usage.pm_time_mins%TYPE
    , sitting_date                 xhibit.xhb_court_room_usage.sitting_date%TYPE
    );

    TYPE xhb_cru_type IS TABLE OF xhb_cru_rec;
    xhb_cru_tt  xhb_cru_type;
      
    CURSOR cur_crest_cru_details IS
   SELECT ccru.crest_court_id,
           ccru.courtroom_no,
           ccru.ctl_id,
           ccru.cru_id,
           xcr.court_room_id,
           ccru.am_time_civ_hours,
           ccru.am_time_civ_mins,
           nvl(ccru.am_time_hours,0), -- CTX-4120
           nvl(ccru.am_time_mins,0), -- CTX-4120
           ccru.pm_time_civ_hours,
           ccru.pm_time_civ_mins,
           nvl(ccru.pm_time_hours,0), -- CTX-4120
           NVL(ccru.pm_time_mins,0), -- CTX-4120
           ccru.sitting_date
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_courtroom_usage_dm ccru,
         xhibit.xhb_court_room xcr,
         xhibit.xhb_court_site xcs,
         xhbstg_courtroom_location_dm xcl
    where ccru.crest_court_id = p_crest_court_id and
          ccru.crest_court_id = xhc.crest_court_id and
          ccru.crest_court_id = xcl.crest_court_id and
          xcl.ctl_id = ccru.ctl_id and
          xcl.site_code = xcs.court_site_code and
          xcs.court_id = xhc.court_id and 
          xcs.court_site_code = ccru.site_code  and
          xcr.crest_court_room_no = ccru.courtroom_no and
          xcr.court_site_id = xcs.court_site_id and 
          NVL(ccru.xhibit_etl_status,'N') not in ('I', 'U') and
          ccru.xhibit_enrich_date is NULL and
          NOT exists 
          (select 'X' from xhibit.xhb_court_room_usage
            where court_room_id = xcr.court_room_id and 
                  nvl(obs_ind,'N') != 'Y');
          
    v_count_number_of_rows   NUMBER := 0;
    v_cru_ins_rows           NUMBER := 0;
    v_cru_ins_status         CHAR(1) := 'N';
    v_cru_np_rows            NUMBER := 0;
    v_courtroom_no           xhbstg_courtroom_usage_dm.courtroom_no%TYPE; -- CTX-3654
    v_ctl_id                 xhbstg_courtroom_usage_dm.ctl_id%TYPE;   -- CTX--3654
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_cru_id                 xhbstg_courtroom_usage_dm.cru_id%TYPE := NULL;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_croom_usage_crest- CTX-2659' -- CTX-3654 changed incorrect action name
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_COURT_ROOM_USAGE - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_COURT_ROOM_USAGE with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_COURT_ROOM_USAGE table that have not yet been populated with CREST data
    OPEN cur_crest_cru_details;
    LOOP
    FETCH cur_crest_cru_details BULK COLLECT INTO xhb_cru_tt LIMIT 1000;
   
    IF xhb_cru_tt IS NOT NULL AND xhb_cru_tt.COUNT > 0 THEN
   
        FOR i IN xhb_cru_tt.FIRST .. xhb_cru_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_croom_usage_crest- CTX-2659'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'INSERTING XHB_COURT_ROOM_USAGE - inserting  courtroom_no - '||xhb_cru_tt(i).court_room_no||', ctl_id : '||xhb_cru_tt(i).ctl_id
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_COURT_ROOM_USAGE- FOR CREST_COURT_ID = '||xhb_cru_tt(i).crest_court_id);
    
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_courtroom_no     := xhb_cru_tt(i).court_room_no;
                v_ctl_id           := xhb_cru_tt(i).ctl_id;
                v_cru_id           := xhb_cru_tt(i).cru_id;
                
                DBMS_OUTPUT.PUT_LINE('v_courtroom_no : '||v_courtroom_no||', v_ctl_id : '||v_ctl_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
                
                -- INSERT and populate this NEW table XHB_COURT_ROOM_USAGE with data from CREST  
                INSERT INTO  xhibit.XHB_COURT_ROOM_USAGE
                            ( court_room_usage_id
                            , court_room_id
                            , am_time_civ_hours
                            , am_time_civ_mins 
                            , am_time_hours   
                            , am_time_mins
                            , pm_time_civ_hours
                            , pm_time_civ_mins 
                            , pm_time_hours   
                            , pm_time_mins
                            , sitting_date
                            , creation_date
                            , created_by  
                            , last_update_date 
                            , last_updated_by )
                     VALUES 
                            (xhibit.xhb_court_room_usage_seq.nextval
                            ,xhb_cru_tt(i).court_room_id
                            ,xhb_cru_tt(i).am_time_civ_hours
                            ,xhb_cru_tt(i).am_time_civ_mins
                            ,xhb_cru_tt(i).am_time_hours
                            ,xhb_cru_tt(i).am_time_mins
                            ,xhb_cru_tt(i).pm_time_civ_hours
                            ,xhb_cru_tt(i).pm_time_civ_mins
                            ,xhb_cru_tt(i).pm_time_hours
                            ,xhb_cru_tt(i).pm_time_mins                                                                                                      
                            ,xhb_cru_tt(i).sitting_date
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,'DATA_MIGRATION');      
                


    
                v_cru_ins_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2659:XHB_COURT_ROOM_USAGE - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cru_ins_rows);
                
                -- Update XHBSTG_COURTROOM_USAGE table for the rows processed
                IF v_cru_ins_rows > 0 THEN 
                  v_cru_ins_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_cru_ins_rows = 0 THEN
                  v_cru_ins_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_croom_usage_crest- CTX-2659'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_COURTROOM_USAGE_DM - updated courtroom_no '||xhb_cru_tt(i).court_room_no||', ctl_id : '||xhb_cru_tt(i).ctl_id||' row with ETL_STATUS '||v_cru_ins_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_cru_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         

                -- Update XHBSTG_COURTROOM_USAGE_DM table for the rows processed
                UPDATE XHBSTG_COURTROOM_USAGE_DM xcg
                SET    xcg.xhibit_etl_date   = SYSDATE
                      ,xcg.xhibit_court_id   = v_xhibit_court_id
                      ,xcg.xhibit_enrich_date = SYSDATE
                      ,xcg.xhibit_etl_status = v_cru_ins_status
                WHERE  xcg.crest_court_id   =  p_crest_court_id
                AND  xcg.courtroom_no = xhb_cru_tt(i).court_room_no
                AND  xcg.ctl_id = xhb_cru_tt(i).ctl_id
                AND  xcg.cru_id = xhb_cru_tt(i).cru_id;
                
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2659:XHBSTG_COURTROOM_USAGE_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
 
        EXCEPTION
            WHEN OTHERS THEN
            
            v_err_message := SQLERRM;
            
            DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_croom_usage_crest for CREST_COURT : '||p_crest_court_id||',courtroom_no : '||v_courtroom_no||', v_ctl_id : '||v_ctl_id||'-'||SUBSTR(v_err_message,1,100));
            
            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_croom_usage_crest - CTX-2659'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_croom_usage_crest - Error processing v_courtroom_no : '||v_courtroom_no||', v_ctl_id : '||v_ctl_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
            -- Update XHBSTG_COURTROOM_USAGE_DM
            -- tables with error status/messages
            BEGIN
            UPDATE XHBSTG_COURTROOM_USAGE_DM xbw
            SET    xbw.xhibit_etl_date   = SYSDATE
                  ,xbw.xhibit_court_id   = v_xhibit_court_id
                  ,xbw.xhibit_enrich_date = SYSDATE
                  ,xbw.xhibit_etl_status = 'X' -- Error
                  ,xbw.xhibit_etl_err_message = v_err_message
            WHERE  xbw.crest_court_id   = p_crest_court_id
              AND  xbw.courtroom_no     = v_courtroom_no
              AND  xbw.ctl_id           = v_ctl_id
              AND  xbw.cru_id           = v_cru_id;
            
            END;
 
        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_cru_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_cru_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_cru_details;

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'upd_xhb_croom_usage_crest- CTX-2659'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_COURT_ROOM_USAGE : Processed '||v_count_number_of_rows||' successfully!'
                 ,p_err_row_count      => NULL 
                 ,p_success_row_count  => v_count_number_of_rows
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );
    ------ Update rest of the Unprocessed rows in XHBSTG_COURTROOM_USAGE_DM 
    ------ for given CREST COURT ID to 'N' i.e. NOT processed
    
    -- Update XHBSTG_COURTROOM_USAGE_DM table for the rows NOT processed
    UPDATE XHBSTG_COURTROOM_USAGE_DM xbh
    SET    xbh.xhibit_etl_date   = SYSDATE
          ,xbh.xhibit_court_id   = v_xhibit_court_id
          ,xbh.xhibit_enrich_date = SYSDATE
          ,xbh.xhibit_etl_status = 'N' -- Not processed
    WHERE  xbh.crest_court_id   =  p_crest_court_id
      AND  xbh.xhibit_etl_status is NULL 
      AND  xbh.xhibit_enrich_date  is NULL
      AND  xbh.xhibit_etl_date  is NULL
      AND  xbh.xhibit_court_id is NULL;
    
    v_cru_np_rows := SQL%ROWCOUNT;
               
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_croom_usage_crest- CTX-2659'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_COURT_ROOM_USAGE : updating XHBSTG_COURTROOM_USAGE_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_cru_np_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );   
                 
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2659:XHBSTG_COURTROOM_USAGE_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2659:XHBSTG_COURTROOM_USAGE_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_cru_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        
        -- ROLLBACK; --CTX-3252
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_croom_usage_crest for CREST_COURT : '||p_crest_court_id||',courtroom_no : '||v_courtroom_no||', v_ctl_id : '||v_ctl_id||'-'||SUBSTR(v_err_message,1,100));
        
        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_croom_usage_crest - CTX-2659'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_croom_usage_crest - Error processing v_courtroom_no : '||v_courtroom_no||', v_ctl_id : '||v_ctl_id||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_count_number_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
        
        COMMIT;
          
                     
END upd_xhb_croom_usage_crest;


/**
  * NAME       : upd_xhb_doc_history_crest
  * DESCRIPTION: CTX-2579 New CTX fields for XHB_DEFENDANT_ON_CASE_HISTORY - Sec 4.3.2.33 req [4975.DM.036]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  *  CTX-3745 - S Sethuraman - CURSOR SQL - NOT EXISTS clause incorrectly refers to staging DEFENDANT_NO instead of XHIBIT DOC HISTORY.DEFENDANT_NUMBER
**/
PROCEDURE upd_xhb_doc_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_csu_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , defendant_history_id         xhibit.xhb_defendant_history.defendant_history_id%TYPE
    , case_history_id              xhibit.xhb_case_history.case_history_id%TYPE
    , defendant_no                 data_mig.xhbstg_csu_history_dm.defendant_no%TYPE
    , case_no                      data_mig.xhbstg_csu_history_dm.case_no%TYPE
    , case_type                    data_mig.xhbstg_csu_history_dm.case_type%TYPE
    , sub_id                       data_mig.xhbstg_csu_history_dm.sub_id%TYPE
    );

    TYPE xhb_csu_type IS TABLE OF xhb_csu_rec;
    xhb_csu_tt  xhb_csu_type;
      
    CURSOR cur_crest_csu_details IS
   SELECT csu.crest_court_id,
           xhc.court_id,
           xdh.defendant_history_id,
           xch.case_history_id,
           csu.defendant_no,
           csu.case_no,
           csu.case_type,
           csu.sub_id          
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_csu_history_dm csu,
         xhibit.xhb_defendant_history xdh,
         xhibit.xhb_case_history xch
    where csu.crest_court_id = p_crest_court_id and
          csu.crest_court_id = xhc.crest_court_id and
          xdh.court_id = xhc.court_id and
          xch.court_id = xhc.court_id and
          xdh.crest_defendant_id = csu.sub_id and 
          xch.case_number = csu.case_no and
          xch.case_type = csu.case_type and
          NVL(csu.xhibit_etl_status,'N') not in ('I', 'U') and
          csu.xhibit_enrich_date is NULL and
          NOT exists 
          (select 'X' from xhibit.xhb_defendant_on_case_history xdoch -- CTX-3745 added alias and updated defendant_number reference
            where xdoch.defendant_history_id = xdh.defendant_history_id and 
                  xdoch.case_history_id = xch.case_history_id  and 
                  xdoch.defendant_number = csu.defendant_no);  

    v_count_number_of_rows   NUMBER := 0;
    v_csu_upd_rows           NUMBER := 0;
    v_csu_upd_status         CHAR(1) := 'N';
    v_csu_np_rows            NUMBER := 0;
    v_sub_id                 data_mig.xhbstg_subject_history_dm.sub_id%TYPE;
    v_case_no                data_mig.xhbstg_csu_history_dm.case_no%TYPE;
    v_case_type              data_mig.xhbstg_csu_history_dm.case_type%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_doc_history_crest- CTX-2579'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DEFENDANT_ON_CASE_HISTORY - Starting process of inserting  rows from CREST SUBJECT_HISTORY table'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting rows from CREST SUBJECT_HISTORY table');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_DEFENDANT_ON_CASE_HISTORY table that have not yet been populated with CREST data
    OPEN cur_crest_csu_details;
    LOOP
    FETCH cur_crest_csu_details BULK COLLECT INTO xhb_csu_tt LIMIT g_bulk_limit;
   
    IF xhb_csu_tt IS NOT NULL AND xhb_csu_tt.COUNT > 0 THEN
   
        FOR i IN xhb_csu_tt.FIRST .. xhb_csu_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                           ,p_action_name        => 'upd_xhb_doc_history_crest- CTX-2579'
                           ,p_run_time           =>  sysdate
                           ,p_log_msg_type       =>  'I' -- Information
                           ,p_log_msg            =>  'UPDATING XHB_DEFENDANT_ON_CASE_HISTORY - inserting  crest sub_id - '||xhb_csu_tt(i).sub_id||',case_no : '||xhb_csu_tt(i).case_no||', case_type : '||xhb_csu_tt(i).case_type
                           ,p_err_row_count      =>  NULL
                           ,p_success_row_count  => NULL
                           ,p_last_updated_by    => 'DATA MIGRATION'
                           ,p_created_by         => 'DATA MIGRATION'
                           );
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_DEFENDANT_ON_CASE_HISTORY - FOR CREST_COURT_ID = '||xhb_csu_tt(i).crest_court_id);
                
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_sub_id     := xhb_csu_tt(i).sub_id;
                v_case_no  := xhb_csu_tt(i).case_no;
                v_case_type := xhb_csu_tt(i).case_type;
                
                DBMS_OUTPUT.PUT_LINE('v_sub_id : '||v_sub_id||' ,v_case_no : '||v_case_no||', case_type : '||v_case_type||', v_xhibit_court_id '||v_xhibit_court_id);         
                
                -- INSERT and populate this NEW table XHB_DEFENDANT_ON_CASE_HISTORY with data from CREST  
                INSERT INTO  xhibit.XHB_DEFENDANT_ON_CASE_HISTORY
                            ( defendant_on_case_history_id
                            , defendant_history_id
                            , case_history_id 
                            , defendant_number   
                            , creation_date 
                            , created_by
                            , last_update_date 
                            , last_updated_by )
                     VALUES 
                            (xhibit.xhb_defendant_on_case_hist_seq.nextval
                            ,xhb_csu_tt(i).defendant_history_id
                            ,xhb_csu_tt(i).case_history_id
                            ,xhb_csu_tt(i).defendant_no                                  
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,'DATA_MIGRATION');      
                
                
                
                
                v_csu_upd_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2579:XHB_DEFENDANT_ON_CASE_HISTORY - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_csu_upd_rows);
                
                -- Update XHBSTG_CSU_HISTORY_DM table for the rows processed
                IF v_csu_upd_rows > 0 THEN 
                    v_csu_upd_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_csu_upd_rows = 0 THEN
                    v_csu_upd_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  

            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_doc_history_crest- CTX-2579'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'I' -- Information
                     ,p_log_msg            =>  'UPDATING XHBSTG_CSU_HISTORY_DM - updated sub_id '||xhb_csu_tt(i).sub_id||' row with ETL_STATUS '||v_csu_upd_status
                     ,p_err_row_count      =>  NULL
                     ,p_success_row_count  => v_csu_upd_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
                     
            
            -- Update XHBSTG_CSU_HISTORY_DM table for the rows processed
            UPDATE XHBSTG_CSU_HISTORY_DM xsh
            SET    xsh.xhibit_etl_date   = SYSDATE
                  ,xsh.xhibit_court_id   = v_xhibit_court_id
                  ,xsh.xhibit_enrich_date = SYSDATE
                  ,xsh.xhibit_etl_status = v_csu_upd_status
            WHERE  xsh.crest_court_id   =  p_crest_court_id
            AND  xsh.sub_id = xhb_csu_tt(i).sub_id
            AND  xsh.case_no = xhb_csu_tt(i).case_no
            AND  xsh.case_type = xhb_csu_tt(i).case_type;
            
            
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('CTX-2579:XHBSTG_CSU_HISTORY_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

        EXCEPTION
            WHEN OTHERS THEN
            
            v_err_message := SQLERRM;
            
            DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_def_history_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||'-'||SUBSTR(v_err_message,1,110));
            
            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_def_history_with_crest - CTX-2579'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_doc_history_crest - Error processing crest sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
            -- Update XHBSTG_CSU_HISTORY_DM
            -- tables with error status/messages
            BEGIN
                UPDATE XHBSTG_CSU_HISTORY_DM xsh
                SET    xsh.xhibit_etl_date   = SYSDATE
                      ,xsh.xhibit_court_id   = v_xhibit_court_id
                      ,xsh.xhibit_enrich_date = SYSDATE
                      ,xsh.xhibit_etl_status = 'X' -- Error
                      ,xsh.xhibit_etl_err_message = v_err_message
                WHERE  xsh.crest_court_id   =  p_crest_court_id
                  AND  xsh.sub_id = v_sub_id
                  AND  xsh.case_no = v_case_no 
                  AND  xsh.case_type = v_case_type;
                END;
                
        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
  
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_csu_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_csu_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_csu_details;


    insert_dm_log (p_crest_court_id     => p_crest_court_id 
             ,p_action_name        => 'upd_xhb_doc_history_crest- CTX-2579'
             ,p_run_time           =>  sysdate
             ,p_log_msg_type       =>  'I' -- Information
             ,p_log_msg            =>  'XHB_DEFENDANT_ON_CASE_HISTORY : Processed '||v_count_number_of_rows||' successfully!'
             ,p_err_row_count      => NULL 
             ,p_success_row_count  => v_count_number_of_rows
             ,p_last_updated_by    => 'DATA MIGRATION'
             ,p_created_by         => 'DATA MIGRATION'
             );
             
    ------ Update rest of the Unprocessed rows in XHBSTG_CSU_HISTORY_DM 
    ------ for given CREST COURT ID to 'N' i.e. NOT processed
    
    -- Update XHBSTG_CSU_HISTORY_DM table for the rows NOT processed
    UPDATE XHBSTG_CSU_HISTORY_DM xsh
    SET    xsh.xhibit_etl_date   = SYSDATE
          ,xsh.xhibit_court_id   = v_xhibit_court_id
          ,xsh.xhibit_enrich_date = SYSDATE
          ,xsh.xhibit_etl_status = 'N' -- Not processed
    WHERE  xsh.crest_court_id   =  p_crest_court_id
    AND  xsh.xhibit_etl_status is NULL 
    AND  xsh.xhibit_enrich_date  is NULL
    AND  xsh.xhibit_etl_date  is NULL
    AND  xsh.xhibit_court_id is NULL;
    
    v_csu_np_rows := SQL%ROWCOUNT;
             
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
             ,p_action_name        => 'upd_xhb_doc_history_crest- CTX-2579'
             ,p_run_time           =>  sysdate
             ,p_log_msg_type       =>  'I' -- Information
             ,p_log_msg            =>  'XHB_DEFENDANT_ON_CASE_HISTORY : updating XHBSTG_CSU_HISTORY_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
             ,p_err_row_count      => NULL 
             ,p_success_row_count  => v_csu_np_rows
             ,p_last_updated_by    => 'DATA MIGRATION'
             ,p_created_by         => 'DATA MIGRATION'
             );   
             
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2579:XHBSTG_CSU_HISTORY_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2579:XHBSTG_CSU_HISTORY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_csu_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
    
        v_err_message := SQLERRM;
        
        --ROLLBACK; --CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_def_history_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||'-'||SUBSTR(v_err_message,1,110));

        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_def_history_with_crest - CTX-2579'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_doc_history_crest - Error processing crest sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_count_number_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
            COMMIT;
                     
END upd_xhb_doc_history_crest;

/**
  * NAME       : upd_xhb_jud_usage_crest
  * DESCRIPTION: CTX-2674 New CTX fields for XHB_JUDGE_USAGE - Sec 4.3.2.20 req [4975.DM.029]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_jud_usage_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_cju_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , court_room_id                xhibit.xhb_judge_usage.court_room_id%TYPE
    , court_chambers_ind           xhibit.xhb_judge_usage.court_chambers_ind%TYPE
    , ref_judge_id                 xhibit.xhb_judge_usage.ref_judge_id%TYPE
    , main_work_type               xhibit.xhb_judge_usage.main_work_type%TYPE
    , sitting_date                 xhibit.xhb_judge_usage.sitting_date%TYPE
    , type_of_work                 xhibit.xhb_judge_usage.type_of_work%TYPE
    , jud_id                       xhbstg_judge_usage_dm.jud_id%TYPE
    , courtroom_no                 xhbstg_judge_usage_dm.courtroom_no%TYPE
    , site_code                    xhbstg_judge_usage_dm.site_code%TYPE
    );

    TYPE xhb_cju_type IS TABLE OF xhb_cju_rec;
    xhb_cju_tt  xhb_cju_type;
      
    CURSOR cur_crest_cju_details IS
   SELECT cju.crest_court_id,
           xhc.court_id,
           xcr.court_room_id,
           cju.court_chambers_ind,
           xrj.ref_judge_id,
           cju.main_work_type,
           cju.sitting_date,
           cju.type_of_work,
           cju.jud_id,
           cju.courtroom_no,
           cju.site_code
    from xhibit.xhb_court xhc,
         data_mig.xhbstg_judge_usage_dm cju,
         xhibit.xhb_ref_judge xrj,
         xhibit.xhb_court_room xcr,
         xhibit.xhb_court_site xcs
    where cju.crest_court_id = p_crest_court_id and
          cju.crest_court_id = xhc.crest_court_id and
          xrj.court_id = xhc.court_id and
          xcs.court_id = xhc.court_id and
          xcr.crest_court_room_no = cju.courtroom_no and
          xcr.court_site_id = xcs.court_site_id  and
          xcs.court_site_code = cju.site_code and
          xrj.crest_judge_id = cju.jud_id and   
          NVL(cju.xhibit_etl_status,'N') not in ('I', 'U') and
          cju.xhibit_enrich_date is NULL  and
          NOT exists 
          (select 'X' from xhibit.xhb_judge_usage
            where court_room_id = xcr.court_room_id and 
                  ref_judge_id = xrj.ref_judge_id and 
                  nvl(obs_ind,'N') != 'Y'); 

    v_count_number_of_rows   NUMBER := 0;
    v_cju_ins_rows           NUMBER := 0;
    v_cju_ins_status         CHAR(1) := 'N';
    v_cju_np_rows            NUMBER := 0;
    v_jud_id               xhbstg_judge_usage_dm.jud_id%TYPE;
    v_courtroom_no                 xhbstg_judge_usage_dm.courtroom_no%TYPE;
    v_site_code                    xhbstg_judge_usage_dm.site_code%TYPE;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_jud_usage_crest- CTX-2674'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_JUDGE_USAGE - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_JUDGE_USAGE with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_JUDGE_USAGE table that have not yet been populated with CREST data
    OPEN cur_crest_cju_details;
    LOOP
    FETCH cur_crest_cju_details BULK COLLECT INTO xhb_cju_tt LIMIT g_bulk_limit;
   
    IF xhb_cju_tt IS NOT NULL AND xhb_cju_tt.COUNT > 0 THEN
   
        FOR i IN xhb_cju_tt.FIRST .. xhb_cju_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                           ,p_action_name        => 'upd_xhb_jud_usage_crest- CTX-2674'
                           ,p_run_time           =>  sysdate
                           ,p_log_msg_type       =>  'I' -- Information
                           ,p_log_msg            =>  'INSERTING XHB_JUDGE_USAGE - inserting  jud id - '||xhb_cju_tt(i).jud_id||', courtroom_no : '||xhb_cju_tt(i).courtroom_no||', site_code : '||xhb_cju_tt(i).site_code
                           ,p_err_row_count      =>  NULL
                           ,p_success_row_count  => NULL
                           ,p_last_updated_by    => 'DATA MIGRATION'
                           ,p_created_by         => 'DATA MIGRATION'
                           );
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_JUDGE_USAGE- FOR CREST_COURT_ID = '||xhb_cju_tt(i).crest_court_id);
                
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_jud_id     := xhb_cju_tt(i).jud_id;
                v_courtroom_no     := xhb_cju_tt(i).courtroom_no;
                v_site_code   :=  xhb_cju_tt(i).site_code;
                DBMS_OUTPUT.PUT_LINE('v_jud_id : '||v_jud_id||', v_courtroom_no : '||v_courtroom_no||' , v_site_code : ' ||v_site_code||' , v_xhibit_court_id '||v_xhibit_court_id);         
            
                -- INSERT and populate this NEW table XHB_JUDGE_USAGE with data from CREST  
                INSERT INTO  xhibit.XHB_JUDGE_USAGE
                            ( judge_usage_id
                            , court_room_id 
                            , court_chambers_ind   
                            , ref_judge_id
                            , main_work_type
                            , sitting_date
                            , type_of_work 
                            , creation_date
                            , created_by  
                            , last_update_date 
                            , last_updated_by )
                     VALUES 
                            (xhibit.XHB_JUDGE_USAGE_seq.nextval
                            ,xhb_cju_tt(i).court_room_id
                            ,xhb_cju_tt(i).court_chambers_ind
                            ,xhb_cju_tt(i).ref_judge_id
                            ,xhb_cju_tt(i).main_work_type
                            ,xhb_cju_tt(i).sitting_date
                            ,xhb_cju_tt(i).type_of_work
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,'DATA_MIGRATION');      
                  


    
                v_cju_ins_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2674:XHB_JUDGE_USAGE - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cju_ins_rows);
    
                -- Update XHBSTG_JUDGE_USAGE_DM table for the rows processed
                IF v_cju_ins_rows > 0 THEN 
                  v_cju_ins_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_cju_ins_rows = 0 THEN
                  v_cju_ins_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  

                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                     ,p_action_name        => 'upd_xhb_jud_usage_crest- CTX-2674'
                                     ,p_run_time           =>  sysdate
                                     ,p_log_msg_type       =>  'I' -- Information
                                     ,p_log_msg            =>  'UPDATING XHBSTG_JUSGE_TICKET_DM - updated jud_id '||xhb_cju_tt(i).jud_id||' row with ETL_STATUS '||v_cju_ins_status
                                     ,p_err_row_count      =>  NULL
                                     ,p_success_row_count  => v_cju_ins_rows
                                     ,p_last_updated_by    => 'DATA MIGRATION'
                                     ,p_created_by         => 'DATA MIGRATION'
                                     );
                         

                -- Update XHBSTG_JUDGE_USAGE_DM table for the rows processed
                UPDATE XHBSTG_JUDGE_USAGE_DM xcg
                SET    xcg.xhibit_etl_date   = SYSDATE
                      ,xcg.xhibit_court_id   = v_xhibit_court_id
                      ,xcg.xhibit_enrich_date = SYSDATE
                      ,xcg.xhibit_etl_status = v_cju_ins_status
                WHERE  xcg.crest_court_id   =  p_crest_court_id
                AND  xcg.jud_id = xhb_cju_tt(i).jud_id
                AND  xcg.courtroom_no = xhb_cju_tt(i).courtroom_no
                AND  xcg.site_code = xhb_cju_tt(i).site_code;     
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2674:XHBSTG_JUDGE_USAGE_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

          EXCEPTION
              WHEN OTHERS THEN
              
                  v_err_message := SQLERRM;

                  DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_XHB_JUDGE_USAGE_crest for CREST_COURT : '||p_crest_court_id||', jud_id : '||v_jud_id||', v_courtroom_no : '||v_courtroom_no||', v_site_code : '||v_site_code||'-'||SUBSTR(v_err_message,1,100));
                  
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'upd_xhb_jud_usage_crest - CTX-2674'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'E' -- Error
                               ,p_log_msg            =>  'upd_xhb_jud_usage_crest - Error processing jud_id : '||v_jud_id||', v_courtroom_no : '||v_courtroom_no||', v_site_code : '||v_site_code||' Error: '||SUBSTR(v_err_message,1,150)
                               ,p_err_row_count      => NULL 
                               ,p_success_row_count  => v_count_number_of_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
                               
                  -- Update XHBSTG_JUDGE_USAGE_DM
                  -- tables with error status/messages
                  BEGIN
                  UPDATE XHBSTG_JUDGE_USAGE_DM xcju
                  SET    xcju.xhibit_etl_date   = SYSDATE
                        ,xcju.xhibit_court_id   = v_xhibit_court_id
                        ,xcju.xhibit_enrich_date = SYSDATE
                        ,xcju.xhibit_etl_status = 'X' -- Error
                        ,xcju.xhibit_etl_err_message = v_err_message
                  WHERE  xcju.crest_court_id   =  p_crest_court_id
                    AND  xcju.jud_id = v_jud_id
                    AND  xcju.courtroom_no = v_courtroom_no
                    AND  xcju.site_code  = v_site_code;
                  
                  END;

          END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing

    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_cju_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_cju_details%NOTFOUND;
    
    END LOOP;
        
    CLOSE cur_crest_cju_details;

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_jud_usage_crest- CTX-2674'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_JUDGE_USAGE : Processed '||v_count_number_of_rows||' successfully!'
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_count_number_of_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );
     ------ Update rest of the Unprocessed rows in XHBSTG_JUDGE_USAGE_DM 
     ------ for given CREST COURT ID to 'N' i.e. NOT processed
     
             -- Update XHBSTG_JUDGE_USAGE_DM table for the rows NOT processed
    UPDATE XHBSTG_JUDGE_USAGE_DM xcg
    SET    xcg.xhibit_etl_date   = SYSDATE
          ,xcg.xhibit_court_id   = v_xhibit_court_id
          ,xcg.xhibit_enrich_date = SYSDATE
          ,xcg.xhibit_etl_status = 'N' -- Not processed
    WHERE  xcg.crest_court_id   =  p_crest_court_id
    AND  xcg.xhibit_etl_status is NULL 
    AND  xcg.xhibit_enrich_date  is NULL
    AND  xcg.xhibit_etl_date  is NULL
    AND  xcg.xhibit_court_id is NULL;
    
    v_cju_np_rows := SQL%ROWCOUNT;
               
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_jud_usage_crest- CTX-2674'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_JUDGE_USAGE : updating XHBSTG_JUDGE_USAGE_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_cju_np_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2674:XHBSTG_JUDGE_USAGE_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2674:XHBSTG_JUDGE_USAGE_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_cju_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
    
        v_err_message := SQLERRM;

        --ROLLBACK; --CTX-3252

        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_XHB_JUDGE_USAGE_crest for CREST_COURT : '||p_crest_court_id||', jud_id : '||v_jud_id||', v_courtroom_no : '||v_courtroom_no||', v_site_code : '||v_site_code||'-'||SUBSTR(v_err_message,1,100));

        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_jud_usage_crest - CTX-2674'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_jud_usage_crest - Error processing jud_id : '||v_jud_id||', v_courtroom_no : '||v_courtroom_no||', v_site_code : '||v_site_code||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_count_number_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
    
        COMMIT;
                     
END upd_xhb_jud_usage_crest;


/**
  * NAME       : upd_xhb_case_on_list_crest
  * DESCRIPTION: CTX-2404. New CTX fields for XHB_CASE_ON_LIST - - Sec 4.3.2.30 req [4975.DM.033] 
  *              Insert data into  XHIBIT XHB_CASE_ON_LIST table to add new data elements from CREST
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
  *
  *   05/12/2018 :  D.Kinder : CTX-3275 - Time_marking de_code column comparison updated
  *
  *   06/12/2018 : S Sethuraman : CTX-3274 - SIT_ON_LIST record selection amended as per FS V0.18 changes
  *   07/12/2018 : S Sethuraman : CTX-3330 - fixed XHBSTG_CASE_HEARING_DAY_DM - LIST_DATE SPANNING BETWEEN DATES NOT PICKED UP
  * 11/12/2018     S Sethuraman : CTX-3366 - XHB_CASE_ON_LIST - TIME_MARKING_ID NULL value issue FIXED
  * 11/12/2018     S Sethuraman : CTX-3252 - Exception Handling moved inside block to continue processing without RollBack
  * 10/01/2019     S Sethuraman : CTX-3509 - MAPPING CORRECTION FOR identifying multiple sittings on the same day / same court as per FS changes v0.22 iplemented.
  * 28/01/2019     S Sethuraman : CTX-3514 - FS V0.24 - Set default TIME_MARKING to "SITTING_AT "  is none present in CREST
  * 14/02/2019     S Sethuraman : CTX-3715 - Handle NULL LIST_SEQ for CTD_ID NULL and IS NOT NULL    
  * 21/02/2019     S Sethuraman : CTX-3729 - UPD_XHB_CASE_ON_LIST - Time_marking mapping NULL ONLY to NULL and if time set default to SITTING AT  
  * 26/02/2019     S Sethuraman : CTX-3734 - UPD_XHB_CASE_ON_LIST - CRACKED EFFECTIVE IND - to populate XHB_SCHEDULED_HEARING
  * 06/03/2019     S Sethuraman : CTX-3810 - UPD_XHB_CASE_ON_LIST - Dry run data issues handled - LIST_dATE NOT NULL included in SQL and TIME_MARKING transformation handled
  * 07/03/2019     S Sethuraman : CTX-3822 - UPD_XHB_CASE_ON_LIST - Cracked_ineffective_IND - update UPD_SCHEDULED_HEARING where multiple HEARING_IDs are fetched - UPDATE ALL
  * 14/03/2019     S Sethuraman : CTX-3855 -  parse_xhbstg_courtroom_day and upd_xhb_sitting_on_list procedures updated to address SITTING_ON_LIST / CASE_ON_LIST Duplicates issue -
  * 27/03/2019     S Sethuraman : CTX-3909 - Cracked_ineffective_id - new definition updated
  * 23/04/2019     S Sethuraman : CTX-4119 - Cracked /effective id tobe updated in CASE_ON_LST only for list type D
  * 30/04/2019     S Sethuraman : CTX-4163 - Missing COURTROOM NOs for valid court sites - map to dummy court site 'X' / courtroom no 1
  * 16/05/2019     S Sethuraman : CTX-4213 - WHEN mapping dummy site ignore jud_Seq_no check as it would be different
  * 22/05/2019     S Sethuraman : CTX-4213 - Add decode statement on both sides
  * 11/06/2019     S Sethuraman : Case_on_list Performance issue - change XCS / XCR as in-line view
  * 16/06/2019     S Sethuraman : Case_on_list Performance issue Duplicates issue fixed
**/
PROCEDURE upd_xhb_case_on_list_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
IS
   v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
  TYPE xhb_col_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , case_id                      xhibit.xhb_case_on_list.case_id%TYPE
    , list_id                      xhibit.xhb_case_on_list.list_id%TYPE
    , sitting_on_list_id           xhibit.xhb_case_on_list.sitting_on_list_id%TYPE
    , court_site_id                xhibit.xhb_case_on_list.court_site_id%TYPE
    , court_room_id                xhibit.xhb_case_on_list.court_room_id%TYPE
    , reserved                     xhibit.xhb_case_on_list.reserved%TYPE
    , floater_case                 xhibit.xhb_case_on_list.floater_case%TYPE
    , time_marking_id              xhibit.xhb_case_on_list.time_marking_id%TYPE
    , time_listed                  xhibit.xhb_case_on_list.time_listed%TYPE
    , is_court_room_list_entry     xhibit.xhb_case_on_list.is_court_room_list_entry%TYPE
    , hearing_type_id              xhibit.xhb_case_on_list.hearing_type_id%TYPE
    , reason_for_removal           xhibit.xhb_case_on_list.reason_for_removal%TYPE
    , cracked_ineffective_id       xhibit.xhb_case_on_list.cracked_ineffective_id%TYPE
    , obs_ind                      xhibit.xhb_case_on_list.obs_ind%TYPE
    , seq_no                       xhibit.xhb_case_on_list.seq_no%TYPE  
    , case_diary_fixture_id        xhibit.xhb_case_on_list.case_diary_fixture_id%TYPE
    , date_of_removal              xhibit.xhb_case_on_list.date_of_removal%TYPE
    , list_note_predefined_id      xhibit.xhb_case_on_list.list_note_predefined_id%TYPE
    , list_note_text               xhibit.xhb_case_on_list.list_note_text%TYPE
    , parent_case_on_list_id       xhibit.xhb_case_on_list.parent_case_on_list_id%TYPE
    , chd_id                       xhbstg_case_hearing_day_dm.chd_id%TYPE
    , case_no                      xhbstg_case_hearing_day_dm.case_no%TYPE
    , case_type                    xhbstg_case_hearing_day_dm.case_type%TYPE
    , ctd_id                       xhbstg_case_hearing_day_dm.ctd_id%TYPE
    , ref_cracked_effective_id     xhibit.xhb_ref_cracked_effective.ref_cracked_effective_id%TYPE -- CTX-3734
    , list_date                    xhbstg_case_hearing_day_dm.list_date%type -- ctx-3734
    , list_type                    xhbstg_case_hearing_day_dm.list_type%type -- ctx-3734
    );

    TYPE xhb_col_type IS TABLE OF xhb_col_rec;
    xhb_col_tt  xhb_col_type;
     
    /****  below are 2 cursors the 1st set is for CTD_ID is not null and 
           the 2nd set is  where CTD_ID is NULL ***/
    /* below FS Changes v0.18 (05/12/2018) - CTX-3274 
       for selection of SITTING_ON_LIST records based on COURT_ROOM implemented :
          To help identify which XHB_SITTING_ON_LIST record to return as well as 
          using the current procedure logic to link to the XHB_LIST record it should 
          also be filtered to match the courtroom.

          To do this obtain the courtroom_no from the xhbstg_courtroom_day_dm record 
          where CTD_ID = xhbstg_case_hearing_day_dm.CTD_ID.

          Then match this to the XHB_SITTING_ON_LIST.COURT_ROOM_ID 
          where XHB_COURT_ROOM.CREST_COURT_ROOM_NO = xhbstg_courtroom_day_dm.courtroom_no 
          (remembering to filter xhibit records specific to the court being migrated)
          
          CTX-3330 : XHBSTG_COURTROOM_DAY_DM LIST_DATE TO BE CHECKED 
                     BETWEEN LIST_START_DATE AND LIST_END_DATE OF XHB_LIST
                     AS FIRM AND WARNED LISTS MAY SPAN ACROSS A PERIOD RATHER THAN 1 DAY
                     
                     ALSO WHILE LOCATING SITTING_ON_LIST RECORD FOR THESE LISTS WHICH 
                     SPAN ACROSS DATES LOCATE THE TIM_LISTED MATCHED AGAINST THE XHBSTG_COURT_ROOM_DAY_DM LIST_DATE

          CTX-3509 : FS Changes v0.22 
                     If this lookup returns more than one record 
                     (i.e. there is more than one sitting in the court room on the day in question) 
                     the perform a further filter where XHB_SITTING_ON_LIST.SITTING_NUMBER = COURTROOM_DAY.JUD_SEQ_NO                     
                     
           CTX-3715 : SEQ_NO - LIST_SEQ IN XHB_CASE_ON_LIST
                      WHEN CTD_ID IS NOT NULL 
                           UPDATE FUNCTION GET_NEXT_LIST_SEQ_NO to include CREST_COURT_ID In filter
                      WHEN  CTD_ID IS NULL
                           Get a Non existing seq no for chd_id, list_date, list_type (include both D and W)                          
    */
    CURSOR cur_crest_col_details_1 (l_dummy_site_id  IN xhibit.xhb_court_site.court_site_id%TYPE,
                                    l_dummy_room_id  IN xhibit.xhb_court_room.court_room_id%TYPE)
    IS
    SELECT xlstg.crest_court_id  /** CTD_ID IS NOT NULL ***********/
    ,      xc.court_id
    ,      xc.case_id
    ,      xsol.list_id 
    ,      xsol.sitting_on_list_id
    ,      decode(xlstg.chd_site_code,NULL,NULL,xsol.court_site_id) court_site_id -- CTX-3855
    ,      decode(xlstg.ctd_id,NULL,NULL,xsol.court_room_id) court_room_id
    ,      CASE
              WHEN xlstg.list_type='F' and xlstg.priority='R' THEN 'Y'
              ELSE NULL
           END reserved
    ,      decode(xlstg.priority,'F','Y') floater_case
    ,      decode(nvl(instr(upper(xlstg.time_marking),'SITTING AT'),-1),-1,NULL,0, -- CTX-3366 NVL ISSUE FIXED -- CTX-3729 NULL ONLY TO NULL
                  decode(nvl(instr(upper(xlstg.time_marking),'NOT BEFORE'),-1),-1,NULL,0,(select xrsc.ref_system_code_id from xhibit.xhb_ref_system_code xrsc
                        where xrsc.code_type = 'EXHIBIT_TIME_FORMAT' 
                        and xrsc.de_code = 'SITTING AT ' -- CTX-3275
                        and xrsc.court_id  = xhc.court_id
                        and NVL(xrsc.obs_ind,'N') != 'Y'), -- CTX-3366 NVL ISSUE FIXED -- CTX-3514 NULL set to "SITTING_AT "
                       (select xrsc.ref_system_code_id 
                        from xhibit.xhb_ref_system_code xrsc
                        where xrsc.code_type = 'EXHIBIT_TIME_FORMAT' 
                        and upper(xrsc.de_code) = 'NOT BEFORE ' -- CTX-3275
                        and xrsc.court_id  = xhc.court_id
                        and NVL(xrsc.obs_ind,'N') != 'Y')),  
                       (select xrsc.ref_system_code_id from xhibit.xhb_ref_system_code xrsc
                        where xrsc.code_type = 'EXHIBIT_TIME_FORMAT' 
                        and xrsc.de_code = 'SITTING AT ' -- CTX-3275
                        and xrsc.court_id  = xhc.court_id
                        and NVL(xrsc.obs_ind,'N') != 'Y'))    time_marking_id  
/*  OLD CODE PRE-FIX CTX-3810 
   ,      to_date(to_char(xlstg.list_date,'DD-MON-YYYY')||' '||
           DECODE(NVL(TIME_MARKING,NULL),NULL,NULL,
                 REGEXP_SUBSTR(upper(time_marking),'[0-9][0-9]\:[0-9][0-9]')||' '||
                  DECODE(INSTR(UPPER(NVL(TIME_MARKING,NULL)),'AM'),0,(DECODE(INSTR(UPPER(nvl(TIME_MARKING,NULL)),'PM'),0,NULL,'PM')),'AM')),'DD-MON-YYYY HH:MI AM') TIME_LISTED                            
 */
 -- new code for TIME_LISTED COLUMN - CTX-3810
  , CASE 
    WHEN
    xlstg.time_marking IS NOT NULL AND LENGTH(REGEXP_SUBSTR(xlstg.time_marking,'[0-9][0-9]*\:[0-9][0-9]')) > 0 THEN
    to_date(to_char(xlstg.list_date,'DD-MON-YYYY')||' '||
             DECODE(NVL(TIME_MARKING,NULL),NULL,NULL,
                   REGEXP_SUBSTR(upper(time_marking),'[0-9][0-9]*\:[0-9][0-9]')||' '||
                    DECODE(INSTR(UPPER(NVL(TIME_MARKING,NULL)),'AM'),0,(DECODE(INSTR(UPPER(nvl(TIME_MARKING,NULL)),'PM'),0,NULL,'PM')),'AM')),'DD-MON-YYYY HH:MI AM') 
    ELSE
       xlstg.list_date                
    END AS TIME_LISTED -- CTX-3810
    ,     xlstg.court_list_ind is_court_room_list_entry
    ,     xrh.ref_hearing_type_id as hearing_type_id
    ,     xlstg.reason_removed reason_for_removal
    ,      NULL as cracked_ineffective_id -- CTX-3909 - this is now dummy - ref_cracked_effective_id is now maped to this field
    ,     decode(xlstg.reason_removed,NULL,'N','Y') obs_ind
    ,     (CASE
             WHEN xlstg.list_sequence IS NOT NULL THEN xlstg.list_sequence
             ELSE xhb_data_migration_process_pkg.get_next_list_seq_no( p_crest_court_id,xlstg.ctd_id) -- CTX-3715
           END) seq_no
    ,     NULL as case_diary_fixture_id
    ,     xlstg.date_removed date_of_removal
    ,     NULL as list_note_predefined_id
    ,     xlstg.list_notes list_notes_text
    ,     NULL as parent_case_on_list_id         
    ,      xlstg.chd_id
    ,      xlstg.case_no
    ,      xlstg.case_type
    ,      xlstg.ctd_id
    ,      decode(xlstg.list_type,'D',decode(nvl(xlstg.effective_ind,NULL),NULL,NULL,(SELECT XRCE.REF_CRACKED_EFFECTIVE_ID
                                                        FROM XHIBIT.XHB_REF_CRACKED_EFFECTIVE XRCE
                                                        WHERE XRCE.CODE = XLSTG.EFFECTIVE_IND
                                                          AND NVL(XRCE.OBS_IND,'N') <> 'Y')),NULL) ref_cracked_effective_id -- CTX-3734 -- CTX-4119
    ,     xlstg.list_date -- ctx-3734 
    ,     xlstg.list_type -- ctx-3734                                                            
    FROM xhbstg_case_hearing_day_dm xlstg
    ,    xhibit.xhb_ref_listing_data xrld
    ,    xhbstg_courtroom_day_dm  xccd
    ,    xhibit.xhb_court xhc
    ,    xhibit.xhb_case xc
    ,    xhibit.xhb_sitting_on_list xsol
    ,    xhibit.xhb_list  xl
    ,    xhibit.xhb_ref_hearing_type xrh
    -- case_on_list perf issue START
    ,   (SELECT t.court_id,t.court_site_id,t.court_site_code,tc.court_room_id,tc.crest_court_room_no from xhibit.xhb_court_site t, xhibit.xhb_court_room tc
            where t.court_site_id = tc.court_site_id and t.crest_court_id = p_crest_court_id 
                  and t.court_site_code <> 'X') xloc   -- excluding dummy site 
            -- case_on_list perf issue END
    WHERE xlstg.crest_court_id = p_crest_court_id
    AND   xlstg.crest_court_id = xhc.crest_court_id
    AND   xccd.crest_court_id = xhc.crest_court_id -- CTX-3274    
    AND   xhc.court_id = xc.court_id
    AND   xrh.court_id = xc.court_id
    AND   xl.court_id = xhc.court_id -- CTX-3330
    -- case_on_list perf issue  START  
    AND   xloc.court_id = xhc.court_id 
    -- case_on_list perf issue  END    
    -- CTX-3855 begin
--  CTX-3577 If court_site_code NOT found in XHIBIT, USE 'X' as court_site_code CTX-3577
-- CTX-4163 Further if COURT_SITE_CODE found but COURT_ROOM_NO NOT found, USE 'X' dummy site code
    -- CTX-3855 end
    AND   xrh.hearing_type_code = xlstg.hearing_type
    AND   xrh.category = 'X' -- always match to X instead of case_type - CTX-2584
    AND   xsol.list_id  = xl.list_id
    -- case_on_list perf issue  START
    AND   xloc.court_site_id = xsol.court_site_id -- CTX-3855
    AND   xloc.court_room_id = xsol.court_room_id -- CTX-3274    
    AND xloc.court_site_code = xlstg.chd_site_code
    AND xloc.crest_court_room_no = xccd.courtroom_no     
    -- case_on_list perf issue END 
   -- CTX-4163 remove below invalid condition as the mapping is changed to use dummy court room as required    
   -- AND   xcr.crest_court_room_no = xccd.courtroom_no  -- CTX-3274       
    AND   xccd.ctd_id = xlstg.ctd_id
    AND   xccd.list_type = xlstg.list_type
    AND   xccd.list_date = xlstg.list_date 
    AND   xlstg.ctd_id is NOT NULL
    AND   xlstg.case_no = xc.case_number
    AND   xlstg.case_type = xc.case_type
    AND   xrld.ref_data_type = 'LIST_TYPE'
    AND   DECODE(xlstg.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
    AND   xrld.ref_listing_data_id = xl.list_type_id
    AND   xlstg.list_date is NOT NULL -- CTX-3810
--    AND   xccd.list_date = xl.list_start_date - CTX-3330
    AND   xccd.list_date between xl.list_start_date and xl.list_end_date -- CTX-3330 -- this code fixes CTX-3362 as well
    AND   xccd.list_date = nvl(trunc(xsol.time_listed),xccd.list_date) -- CTX-3330 -- this code fixes CTX-3362 as well
    -- case_on_list perf issue  START -- exclude dummy site check for jud_seq_no  
    AND   NVL(xccd.jud_seq_no,0) = NVL(xsol.sitting_number,0) -- CTX-3509     -- CTX-4213
    -- case_on_list perf issue - END    
    AND   NVL(xrld.obs_ind,'N') <> 'Y'
    AND   NVL(xhc.obs_ind,'N') <> 'Y'
    AND   nvl(xlstg.list_type,' ') != 'X'  -- load only list_type != 'X'
    AND   NVL(xlstg.xhibit_etl_status,'N') not in ('I', 'U') 
 --   AND   xlstg.xhibit_enrich_date is NULL  -- CTX-3330 - Commenting This enables re-processing
    --cc10102018
    AND NOT EXISTS (SELECT 'z' --All of these records are to be excluded from the insert
                     FROM XHBSTG_CASE_HEARING_DAY_DM chddm
                     WHERE chddm.crest_court_id = p_crest_court_id
                     AND   chddm.CASE_TYPE IN ('B','U')
                     AND   chddm.case_no = xlstg.case_no
                     AND   chddm.case_type = xlstg.case_type
                     AND   chddm.crest_court_id = xlstg.crest_court_id
                     AND NOT EXISTS (SELECT 'x'
                                     FROM xhibit.xhb_case xca
                                     ,    xhibit.xhb_court xcrt
                                     WHERE xca.court_id = xcrt.court_id
                                     AND  xca.case_number = chddm.case_no
                                     AND  xca.case_type = chddm.case_type
                                     AND  xcrt.crest_court_id = chddm.crest_court_id)
                     )                
    --cc10102018 end                                     
    /*Make sure we dont insert a row that already exists in xhb_list*/
    AND NOT EXISTS (SELECT 'x'
                    FROM xhibit.xhb_case_on_list xcl
                    WHERE xcl.case_id = xc.case_id
                    AND   xcl.list_id = xl.list_id
                    AND   xcl.sitting_on_list_id = xsol.sitting_on_list_id
                    AND NVL(xcl.obs_ind,'N') <> 'Y'                           
                    )
   UNION -- NOW SELECT CHD_IDs INVALID LOCATIONS mapping to DUMMY SITES
         --  REMOVE REFERENCES TO XHB_COURT_SITE AND XHB_COURT_ROOM AND SIMPLY USE
         -- L_DUMMY_SITE_ID AND L_DUMMY_ROOM_ID passed in as parameters
       SELECT xlstg.crest_court_id  /** CTD_ID IS NOT NULL ***********/
    ,      xc.court_id
    ,      xc.case_id
    ,      xsol.list_id 
    ,      xsol.sitting_on_list_id
    ,      decode(xlstg.chd_site_code,NULL,NULL,xsol.court_site_id) court_site_id -- CTX-3855
    ,      decode(xlstg.ctd_id,NULL,NULL,xsol.court_room_id) court_room_id
    ,      CASE
              WHEN xlstg.list_type='F' and xlstg.priority='R' THEN 'Y'
              ELSE NULL
           END reserved
    ,      decode(xlstg.priority,'F','Y') floater_case
    ,      decode(nvl(instr(upper(xlstg.time_marking),'SITTING AT'),-1),-1,NULL,0, -- CTX-3366 NVL ISSUE FIXED -- CTX-3729 NULL ONLY TO NULL
                  decode(nvl(instr(upper(xlstg.time_marking),'NOT BEFORE'),-1),-1,NULL,0,(select xrsc.ref_system_code_id from xhibit.xhb_ref_system_code xrsc
                        where xrsc.code_type = 'EXHIBIT_TIME_FORMAT' 
                        and xrsc.de_code = 'SITTING AT ' -- CTX-3275
                        and xrsc.court_id  = xhc.court_id
                        and NVL(xrsc.obs_ind,'N') != 'Y'), -- CTX-3366 NVL ISSUE FIXED -- CTX-3514 NULL set to "SITTING_AT "
                       (select xrsc.ref_system_code_id 
                        from xhibit.xhb_ref_system_code xrsc
                        where xrsc.code_type = 'EXHIBIT_TIME_FORMAT' 
                        and upper(xrsc.de_code) = 'NOT BEFORE ' -- CTX-3275
                        and xrsc.court_id  = xhc.court_id
                        and NVL(xrsc.obs_ind,'N') != 'Y')),  
                       (select xrsc.ref_system_code_id from xhibit.xhb_ref_system_code xrsc
                        where xrsc.code_type = 'EXHIBIT_TIME_FORMAT' 
                        and xrsc.de_code = 'SITTING AT ' -- CTX-3275
                        and xrsc.court_id  = xhc.court_id
                        and NVL(xrsc.obs_ind,'N') != 'Y'))    time_marking_id  
/*  OLD CODE PRE-FIX CTX-3810 
   ,      to_date(to_char(xlstg.list_date,'DD-MON-YYYY')||' '||
           DECODE(NVL(TIME_MARKING,NULL),NULL,NULL,
                 REGEXP_SUBSTR(upper(time_marking),'[0-9][0-9]\:[0-9][0-9]')||' '||
                  DECODE(INSTR(UPPER(NVL(TIME_MARKING,NULL)),'AM'),0,(DECODE(INSTR(UPPER(nvl(TIME_MARKING,NULL)),'PM'),0,NULL,'PM')),'AM')),'DD-MON-YYYY HH:MI AM') TIME_LISTED                            
 */
 -- new code for TIME_LISTED COLUMN - CTX-3810
  , CASE 
    WHEN
    xlstg.time_marking IS NOT NULL AND LENGTH(REGEXP_SUBSTR(xlstg.time_marking,'[0-9][0-9]*\:[0-9][0-9]')) > 0 THEN
    to_date(to_char(xlstg.list_date,'DD-MON-YYYY')||' '||
             DECODE(NVL(TIME_MARKING,NULL),NULL,NULL,
                   REGEXP_SUBSTR(upper(time_marking),'[0-9][0-9]*\:[0-9][0-9]')||' '||
                    DECODE(INSTR(UPPER(NVL(TIME_MARKING,NULL)),'AM'),0,(DECODE(INSTR(UPPER(nvl(TIME_MARKING,NULL)),'PM'),0,NULL,'PM')),'AM')),'DD-MON-YYYY HH:MI AM') 
    ELSE
       xlstg.list_date                
    END AS TIME_LISTED -- CTX-3810
    ,     xlstg.court_list_ind is_court_room_list_entry
    ,     xrh.ref_hearing_type_id as hearing_type_id
    ,     xlstg.reason_removed reason_for_removal
    ,      NULL as cracked_ineffective_id -- CTX-3909 - this is now dummy - ref_cracked_effective_id is now maped to this field
    ,     decode(xlstg.reason_removed,NULL,'N','Y') obs_ind
    ,     (CASE
             WHEN xlstg.list_sequence IS NOT NULL THEN xlstg.list_sequence
             ELSE xhb_data_migration_process_pkg.get_next_list_seq_no( p_crest_court_id,xlstg.ctd_id) -- CTX-3715
           END) seq_no
    ,     NULL as case_diary_fixture_id
    ,     xlstg.date_removed date_of_removal
    ,     NULL as list_note_predefined_id
    ,     xlstg.list_notes list_notes_text
    ,     NULL as parent_case_on_list_id         
    ,      xlstg.chd_id
    ,      xlstg.case_no
    ,      xlstg.case_type
    ,      xlstg.ctd_id
    ,      decode(xlstg.list_type,'D',decode(nvl(xlstg.effective_ind,NULL),NULL,NULL,(SELECT XRCE.REF_CRACKED_EFFECTIVE_ID
                                                        FROM XHIBIT.XHB_REF_CRACKED_EFFECTIVE XRCE
                                                        WHERE XRCE.CODE = XLSTG.EFFECTIVE_IND
                                                          AND NVL(XRCE.OBS_IND,'N') <> 'Y')),NULL) ref_cracked_effective_id -- CTX-3734 -- CTX-4119
    ,     xlstg.list_date -- ctx-3734 
    ,     xlstg.list_type -- ctx-3734                                                            
    FROM xhbstg_case_hearing_day_dm xlstg
    ,    xhibit.xhb_ref_listing_data xrld
    ,    xhbstg_courtroom_day_dm  xccd
    ,    xhibit.xhb_court xhc
    ,    xhibit.xhb_case xc
    ,    xhibit.xhb_sitting_on_list xsol
    ,    xhibit.xhb_list  xl
    ,    xhibit.xhb_ref_hearing_type xrh
    WHERE xlstg.crest_court_id = p_crest_court_id
    AND   xlstg.crest_court_id = xhc.crest_court_id
    AND   xccd.crest_court_id = xhc.crest_court_id -- CTX-3274    
    AND   xhc.court_id = xc.court_id
    AND   xrh.court_id = xc.court_id
    AND   xl.court_id = xhc.court_id -- CTX-3330
    AND   xrh.hearing_type_code = xlstg.hearing_type
    AND   xrh.category = 'X' -- always match to X instead of case_type - CTX-2584
    AND   xsol.list_id  = xl.list_id
    -- case_on_list perf issue  START
    AND   xsol.court_site_id = l_dummy_site_id -- CTX-3855
    AND   xsol.court_room_id = l_dummy_room_id-- CTX-3274       
    -- case_on_list perf issue END 
   -- CTX-4163 remove below invalid condition as the mapping is changed to use dummy court room as required    
   -- AND   xcr.crest_court_room_no = xccd.courtroom_no  -- CTX-3274       
    AND   xccd.ctd_id = xlstg.ctd_id
    AND   xccd.list_type = xlstg.list_type
    AND   xccd.list_date = xlstg.list_date 
    AND   xlstg.ctd_id is NOT NULL
    AND   xlstg.case_no = xc.case_number
    AND   xlstg.case_type = xc.case_type
   -- case_on_list perf issue START -- check location doesn't exist and is to be mapped to dummy site
   AND NOT EXISTS
        (select 'X' from xhibit.xhb_court_site t, xhibit.xhb_court_room tc 
          where t.crest_court_id = p_crest_court_id and t.court_site_id = tc.court_site_id and 
                t.court_site_code = xccd.site_code and tc.crest_court_room_no = xccd.courtroom_no)    
   -- case_on_list perf issue END                       
    AND   xrld.ref_data_type = 'LIST_TYPE'
    AND   DECODE(xlstg.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
    AND   xrld.ref_listing_data_id = xl.list_type_id
    AND   xlstg.list_date is NOT NULL -- CTX-3810
--    AND   xccd.list_date = xl.list_start_date - CTX-3330
    AND   xccd.list_date between xl.list_start_date and xl.list_end_date -- CTX-3330 -- this code fixes CTX-3362 as well
    AND   xccd.list_date = nvl(trunc(xsol.time_listed),xccd.list_date) -- CTX-3330 -- this code fixes CTX-3362 as well
    -- case_on_list perf issue    -- no jud_seq condition reqd for dummy sites
    AND   NVL(xrld.obs_ind,'N') <> 'Y'
    AND   NVL(xhc.obs_ind,'N') <> 'Y'
    AND   nvl(xlstg.list_type,' ') != 'X'  -- load only list_type != 'X'
    AND   NVL(xlstg.xhibit_etl_status,'N') not in ('I', 'U') 
 --   AND   xlstg.xhibit_enrich_date is NULL  -- CTX-3330 - Commenting This enables re-processing
    --cc10102018
    AND NOT EXISTS (SELECT 'z' --All of these records are to be excluded from the insert
                     FROM XHBSTG_CASE_HEARING_DAY_DM chddm
                     WHERE chddm.crest_court_id = p_crest_court_id
                     AND   chddm.CASE_TYPE IN ('B','U')
                     AND   chddm.case_no = xlstg.case_no
                     AND   chddm.case_type = xlstg.case_type
                     AND   chddm.crest_court_id = xlstg.crest_court_id
                     AND NOT EXISTS (SELECT 'x'
                                     FROM xhibit.xhb_case xca
                                     ,    xhibit.xhb_court xcrt
                                     WHERE xca.court_id = xcrt.court_id
                                     AND  xca.case_number = chddm.case_no
                                     AND  xca.case_type = chddm.case_type
                                     AND  xcrt.crest_court_id = chddm.crest_court_id)
                     )                
    --cc10102018 end                                     
    /*Make sure we dont insert a row that already exists in xhb_list*/
    AND NOT EXISTS (SELECT 'x'
                    FROM xhibit.xhb_case_on_list xcl
                    WHERE xcl.case_id = xc.case_id
                    AND   xcl.list_id = xl.list_id
                    AND   xcl.sitting_on_list_id = xsol.sitting_on_list_id
                    AND NVL(xcl.obs_ind,'N') <> 'Y'                           
                    );
                    

    CURSOR cur_crest_col_details_2 IS     
    SELECT xlstg.crest_court_id  /** CTD_ID IS NULL ***********/
    ,      xc.court_id
    ,      xc.case_id
    ,      decode(xlstg.priority,
                 'F',
                 decode(xlstg.list_type,
                     'D',(select xl.list_id 
                          from xhibit.xhb_list xl, xhibit.xhb_ref_listing_data xrld
                          where xrld.ref_listing_data_id = xl.list_type_id
                          and xl.list_start_date = xlstg.list_date
                          and xl.court_id = xc.court_id
                          and   xrld.ref_data_type = 'LIST_TYPE'
                          and   DECODE(xlstg.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
                          and   nvl(xrld.obs_ind,'N') != 'Y' and rownum = 1),
                     'F',(select xl.list_id 
                          from xhibit.xhb_list xl, xhibit.xhb_ref_listing_data xrld
                          where xrld.ref_listing_data_id = xl.list_type_id
                          and xlstg.list_date between xl.list_start_date  and xl.list_end_date
                          and xl.court_id = xc.court_id
                          and   xrld.ref_data_type = 'LIST_TYPE'
                          and   DECODE(xlstg.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
                          and   nvl(xrld.obs_ind,'N') != 'Y' and rownum = 1)),
                     'R',(select xl.list_id 
                          from xhibit.xhb_list xl, xhibit.xhb_ref_listing_data xrld
                          where xrld.ref_listing_data_id = xl.list_type_id
                          and xl.list_start_date = xlstg.list_date
                          and xl.court_id = xc.court_id
                          and   xrld.ref_data_type = 'LIST_TYPE'
                          and   DECODE(xlstg.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
                          and   nvl(xrld.obs_ind,'N') != 'Y' and rownum = 1),
                      decode(xlstg.list_type,
                     'W',(select xl.list_id 
                          from xhibit.xhb_list xl, xhibit.xhb_ref_listing_data xrld
                          where xrld.ref_listing_data_id = xl.list_type_id
                          and xl.list_start_date = xlstg.list_date
                          and xl.court_id = xc.court_id
                          and   xrld.ref_data_type = 'LIST_TYPE'
                          and   DECODE(xlstg.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
                          and   nvl(xrld.obs_ind,'N') != 'Y' and rownum = 1))) as list_id
    ,      NULL as sitting_on_list_id
    ,      decode(xlstg.chd_site_code,NULL,NULL,
                   (select court_site_id
                    from xhibit.xhb_court_site
                    where crest_court_id = xhc.crest_court_id
                    and court_site_code = NVL((SELECT t.court_site_code 
                                                 from xhibit.xhb_court_site t
                                                where t.court_id = xc.court_id and 
                                                t.court_site_code = xlstg.chd_site_code),'X') -- CTX-3855
                    and rownum = 1)) court_site_id
    ,      NULL as court_room_id
    ,      CASE
              WHEN xlstg.list_type='F' and xlstg.priority='R' THEN 'Y'
              ELSE NULL
           END reserved
    ,      decode(xlstg.priority,'F','Y') floater_case
    ,      decode(nvl(instr(upper(xlstg.time_marking),'SITTING AT'),-1),-1,NULL,0, -- CTX-3366 NVL ISSUE FIXED -- CTX-3729 NULL ONLY TO NULL
                  decode(nvl(instr(upper(xlstg.time_marking),'NOT BEFORE'),-1),-1,NULL,0,(select xrsc.ref_system_code_id 
                        from xhibit.xhb_ref_system_code xrsc
                        where xrsc.code_type = 'EXHIBIT_TIME_FORMAT' 
                        and xrsc.de_code = 'SITTING AT ' -- CTX-3275
                        and xrsc.court_id  = xhc.court_id
                        and NVL(xrsc.obs_ind,'N') != 'Y'), -- CTX-3366 NVL ISSUE FIXED -- CTX-3514 NULL set to "SITTING_AT "
                       (select xrsc.ref_system_code_id 
                        from xhibit.xhb_ref_system_code xrsc
                        where xrsc.code_type = 'EXHIBIT_TIME_FORMAT' 
                        and upper(xrsc.de_code) = 'NOT BEFORE '  -- CTX-3275
                        and xrsc.court_id  = xhc.court_id
                        and NVL(xrsc.obs_ind,'N') != 'Y')), 
                       (select xrsc.ref_system_code_id 
                        from xhibit.xhb_ref_system_code xrsc
                        where xrsc.code_type = 'EXHIBIT_TIME_FORMAT' 
                        and xrsc.de_code = 'SITTING AT ' -- CTX-3275
                        and xrsc.court_id  = xhc.court_id
                        and NVL(xrsc.obs_ind,'N') != 'Y')) time_marking_id  
/*  OLD CODE PRE-FIX CTX-3810
    ,      to_date(to_char(xlstg.list_date,'DD-MON-YYYY')||' '||
           DECODE(NVL(TIME_MARKING,NULL),NULL,NULL,
                  REGEXP_SUBSTR(upper(time_marking),'[0-9][0-9]\:[0-9][0-9]')||' '||
                 DECODE(INSTR(UPPER(NVL(TIME_MARKING,NULL)),'AM'),0,(DECODE(INSTR(UPPER(nvl(TIME_MARKING,NULL)),'PM'),0,NULL,'PM')),'AM')),'DD-MON-YYYY HH:MI AM') TIME_LISTED                      
*/
-- new code for TIME_LISTED COLUMN - CTX-3810
, CASE 
  WHEN
  xlstg.time_marking IS NOT NULL AND LENGTH(REGEXP_SUBSTR(xlstg.time_marking,'[0-9][0-9]*\:[0-9][0-9]')) > 0 THEN
  to_date(to_char(xlstg.list_date,'DD-MON-YYYY')||' '||
           DECODE(NVL(TIME_MARKING,NULL),NULL,NULL,
                 REGEXP_SUBSTR(upper(time_marking),'[0-9][0-9]*\:[0-9][0-9]')||' '||
                  DECODE(INSTR(UPPER(NVL(TIME_MARKING,NULL)),'AM'),0,(DECODE(INSTR(UPPER(nvl(TIME_MARKING,NULL)),'PM'),0,NULL,'PM')),'AM')),'DD-MON-YYYY HH:MI AM') 
  ELSE
     xlstg.list_date                
  END AS TIME_LISTED -- CTX-3810
    ,     xlstg.court_list_ind is_court_room_list_entry
    ,     xrh.ref_hearing_type_id as hearing_type_id
    ,     xlstg.reason_removed reason_for_removal
    ,      NULL as cracked_ineffective_id -- CTX-3909 - This is now dummy ref_cracked_effective_id is now being mapped to this field
    ,     decode(xlstg.reason_removed,NULL,'N','Y') obs_ind
    ,     decode(xlstg.list_sequence,NULL,(select seq_no 
                                           from (select chd_id,
                                                        list_date,
                                                        list_type,
                                                        row_number () 
                                                  over (partition by list_date 
                                                  order by list_date) seq_no 
                                                  from xhbstg_case_hearing_day_dm
                                                  where crest_court_id = p_crest_court_id
                                                --  and rownum = 1 --CC26102018 - was returning more than than one row
                                                  and nvl(list_type,'X') != 'X' -- ctx-3715 include D as well and NOT just W
                                                  and list_sequence is null                                                
                                                  order by list_date) t
                                        where t.chd_id = xlstg.chd_id
                                        and rownum = 1),xlstg.list_sequence) seq_no                                            
             -- since list_sequence is NULL in CREST for WARNED / DAILY LISTS, generate the SEQ_NO  bychd_id within list_datE             
    ,     NULL as case_diary_fixture_id
    ,     xlstg.date_removed date_of_removal
    ,     NULL as list_note_predefined_id
    ,     xlstg.list_notes list_notes_text
    ,     NULL as parent_case_on_list_id         
    ,      xlstg.chd_id
    ,      xlstg.case_no
    ,      xlstg.case_type
    ,      xlstg.ctd_id
    ,      decode(xlstg.list_type,'D',decode(nvl(xlstg.effective_ind,NULL),NULL,NULL,(SELECT XRCE.REF_CRACKED_EFFECTIVE_ID
                                                        FROM XHIBIT.XHB_REF_CRACKED_EFFECTIVE XRCE
                                                        WHERE XRCE.CODE = XLSTG.EFFECTIVE_IND
                                                          AND NVL(XRCE.OBS_IND,'N') <> 'Y')),NULL) ref_cracked_effective_id -- CTX-3734 -- CTX-4119
    ,   xlstg.list_date    -- ctx-3734   
    ,     xlstg.list_type -- ctx-3734                                                       
    FROM xhbstg_case_hearing_day_dm xlstg
    ,    xhibit.xhb_court xhc
    ,    xhibit.xhb_case xc
    ,    xhibit.xhb_ref_hearing_type xrh
    WHERE xlstg.crest_court_id = p_crest_court_id
    AND   xlstg.crest_court_id = xhc.crest_court_id
    AND   xhc.court_id = xc.court_id
    AND   xrh.court_id = xc.court_id
    AND   xrh.hearing_type_code = xlstg.hearing_type
    AND   xrh.category = 'X' -- always match to X instead of case_type - CTX-2584
    AND   xlstg.ctd_id is  NULL
    AND   (xlstg.priority is NOT NULL OR xlstg.list_type = 'W')
    AND   xlstg.list_type is NOT NULL
    AND   xlstg.list_date is NOT NULL -- CTX-3810
    AND   xlstg.case_no = xc.case_number
    AND   xlstg.case_type = xc.case_type
    AND   NVL(xhc.obs_ind,'N') <> 'Y'
    AND   nvl(xlstg.list_type,' ') != 'X'  -- load only list_type != 'X'
  
    AND   NVL(xlstg.xhibit_etl_status,'N') not in ('I', 'U') 
    AND   xlstg.xhibit_enrich_date is NULL 
   --cc10102018 "IF CASE_HEARING_DAY.CASE_TYPE = B or U and the lookup to XHB_CASE using CASE_TYPE, CASE_NO and parent COPURT_ID then do not load this row to CREST"
    AND NOT EXISTS (SELECT 'z' --All of these records are to be excluded from the insert
                     FROM XHBSTG_CASE_HEARING_DAY_DM chddm
                     WHERE chddm.crest_court_id = p_crest_court_id
                     AND   chddm.CASE_TYPE IN ('B','U')
                     AND   chddm.case_no = xlstg.case_no
                     AND   chddm.case_type = xlstg.case_type
                     AND   chddm.crest_court_id = xlstg.crest_court_id
                     AND NOT EXISTS (SELECT 'x'
                                     FROM xhibit.xhb_case xca
                                     ,    xhibit.xhb_court xcrt
                                     WHERE xca.court_id = xcrt.court_id
                                     AND  xca.case_number = chddm.case_no
                                     AND  xca.case_type = chddm.case_type
                                     AND  xcrt.crest_court_id = chddm.crest_court_id)
                     )        
    --cc10102018 end    
     /*Make sure we dont insert a row that already exists in xhb_case_on_list*/
    AND NOT EXISTS (SELECT 'x'
                    FROM xhibit.xhb_case_on_list xcl
                    WHERE xcl.case_id = xc.case_id
                    AND   xcl.list_id = list_id
                    AND   xcl.sitting_on_list_id is NULL
                    AND NVL(xcl.obs_ind,'N') <> 'Y'                           
                    );  
                    
                      
    v_count_number_of_rows   NUMBER := 0;
    v_col_ins_rows           NUMBER := 0;
    v_col_ins_status         CHAR(1) := 'N';
     v_sch_ins_rows           NUMBER := 0; -- CTX-3734
    v_sch_ins_status         CHAR(1) := 'N'; -- CTX-3734
    v_col_np_rows            NUMBER := 0;
    v_chd_id                 xhbstg_case_hearing_day_dm.chd_id%TYPE;
    v_case_no                xhbstg_case_hearing_day_dm.case_no%TYPE;
    v_case_type              xhbstg_case_hearing_day_dm.case_type%TYPE;
    v_ctd_id                 xhbstg_case_hearing_day_dm.ctd_id%TYPE;
    v_ref_cracked_eff_ind    xhibit.xhb_ref_cracked_effective.ref_cracked_effective_id%type; -- CTX-3734              
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    -- case_on_perf issue
    v_dummy_site_id         xhibit.xhb_court_site.court_site_id%TYPE;
    v_dummy_room_id         xhibit.xhb_court_room.court_room_id%TYPE;
    
BEGIN


          insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_ON_LIST - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_CASE_ON_LIST with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places

    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
    
    BEGIN
         SELECT   xt.court_site_id,xtc.court_room_id INTO v_dummy_site_id,v_dummy_room_id
           FROM   xhibit.xhb_court_site xt, xhibit.xhb_court_room xtc
           WHERE  xt.crest_court_id = p_crest_court_id AND
                  xt.court_site_id = xtc.court_site_id AND
                  xt.court_site_code = 'X' AND
                  xtc.crest_court_room_no = 1;
    EXCEPTION WHEN OTHERS THEN
         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Information
                         ,p_log_msg            =>  'XHB_CASE_ON_LIST - Unable to fetch DUMMY SITE-ROOM IDs for CREST COURT : '||p_crest_court_id 
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );                       
    END;
  
      -- Here we are going to populate the rows in XHB_CASE_ON_LIST table that have not yet been populated with CREST data
      -- for rows where CTD_ID is not null
    OPEN cur_crest_col_details_1 (v_dummy_site_id,v_dummy_room_id);
    LOOP
 
    FETCH cur_crest_col_details_1 BULK COLLECT INTO xhb_col_tt LIMIT g_bulk_limit;
 
    IF xhb_col_tt IS NOT NULL AND xhb_col_tt.COUNT > 0 THEN

        FOR i IN xhb_col_tt.FIRST .. xhb_col_tt.LAST LOOP
           
           BEGIN -- CTX-3252
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTING XHB_CASE_ON_LIST - inserting  chd id - '||xhb_col_tt(i).chd_id||', case_no : '||xhb_col_tt(i).case_no||', case_type : '||xhb_col_tt(i).case_type||', ctd_id : '||xhb_col_tt(i).ctd_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('INSERTING XHB_CASE_ON_LIST- FOR CREST_COURT_ID = '||xhb_col_tt(i).crest_court_id);
    
 -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_chd_id     := xhb_col_tt(i).chd_id;
            v_case_no     := xhb_col_tt(i).case_no;
            v_case_type   :=  xhb_col_tt(i).case_type;
            v_ctd_id     := xhb_col_tt(i).ctd_id;
            v_ref_cracked_eff_ind := xhb_col_tt(i).ref_cracked_effective_id; -- ctx-3734
            
             DBMS_OUTPUT.PUT_LINE('v_chd_id : '||v_chd_id||', v_case_no : '||v_case_no||' , v_case_type : ' ||v_case_type||', v_ctd_id : '||v_ctd_id||', v_ref_cracked_eff_ind : '||v_ref_cracked_eff_ind||' , v_xhibit_court_id '||v_xhibit_court_id);         
            
         -- INSERT and populate this NEW table XHB_CASE_ON_LIST with data from CREST  
                     INSERT INTO  xhibit.XHB_CASE_ON_LIST
                                  ( case_on_list_id
                                  , case_id 
                                  , list_id   
                                  , sitting_on_list_id
                                  , court_site_id
                                  , court_room_id
                                  , reserved 
                                  , floater_case
                                  , time_marking_id
                                  , time_listed
                                  , is_court_room_list_entry
                                  , hearing_type_id
                                  , reason_for_removal
                                  , cracked_ineffective_id
                                  , obs_ind
                                  , seq_no
                                  , case_diary_fixture_id
                                  , date_of_removal
                                  , list_note_predefined_id
                                  , list_note_text
                                  , parent_case_on_list_id
                                  , creation_date
                                  , created_by  
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (xhibit.XHB_CASE_ON_LIST_seq.nextval
                                  ,xhb_col_tt(i).case_id
                                  ,xhb_col_tt(i).list_id
                                  ,xhb_col_tt(i).sitting_on_list_id
                                  ,xhb_col_tt(i).court_site_id
                                  ,xhb_col_tt(i).court_room_id
                                  ,xhb_col_tt(i).reserved
                                  ,xhb_col_tt(i).floater_case
                                  ,xhb_col_tt(i).time_marking_id
                                  ,xhb_col_tt(i).time_listed
                                  ,xhb_col_tt(i).is_court_room_list_entry
                                  ,xhb_col_tt(i).hearing_type_id
                                  ,xhb_col_tt(i).reason_for_removal
                                  ,xhb_col_tt(i).ref_cracked_effective_id -- CTX-3909
                                  ,xhb_col_tt(i).obs_ind
                                  ,xhb_col_tt(i).seq_no
                                  ,xhb_col_tt(i).case_diary_fixture_id
                                  ,xhb_col_tt(i).date_of_removal
                                  ,xhb_col_tt(i).list_note_predefined_id
                                  ,xhb_col_tt(i).list_note_text
                                  ,xhb_col_tt(i).parent_case_on_list_id
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');      


    
    v_col_ins_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2404:XHB_CASE_ON_LIST - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_col_ins_rows);
    
               -- Update XHBSTG_CASE_HEARING_DAY_DM table for the rows processed
         IF v_col_ins_rows > 0 THEN 
            v_col_ins_status := 'I'; -- XHIBIT TABLE inserted
         ELSIF v_col_ins_rows = 0 THEN
            v_col_ins_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;  

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_CASE_HEARING_DAY_DM - updated chd_id '||xhb_col_tt(i).chd_id||' row with ETL_STATUS '||v_col_ins_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_col_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         

           -- Update XHBSTG_CASE_HEARING_DAY_DM table for the rows processed
            UPDATE XHBSTG_CASE_HEARING_DAY_DM xcg
            SET    xcg.xhibit_etl_date   = SYSDATE
                  ,xcg.xhibit_court_id   = v_xhibit_court_id
                  ,xcg.xhibit_enrich_date = SYSDATE
                  ,xcg.xhibit_etl_status = v_col_ins_status                
            WHERE  xcg.crest_court_id   =  p_crest_court_id
              AND  xcg.chd_id = xhb_col_tt(i).chd_id
              AND  xcg.case_no = xhb_col_tt(i).case_no
              AND  xcg.case_type = xhb_col_tt(i).case_type
              AND  xcg.ctd_id = xhb_col_tt(i).ctd_id;     

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2404:XHBSTG_CASE_HEARING_DAY_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
                
                BEGIN -- CTX-3734
                
                   IF xhb_col_tt(i).ref_cracked_effective_id is NOT NULL and 
                        xhb_col_tt(i).list_type = 'D' THEN
                      
                    BEGIN
                      UPDATE xhibit.XHB_SCHEDULED_HEARING xsh
                         SET xsh.REF_CRACKED_EFFECTIVE_ID = xhb_col_tt(i).ref_cracked_effective_id 
                       WHERE xsh.SITTING_ID in (SELECT XS.SITTING_ID
                                                 FROM XHIBIT.XHB_SITTING XS,
                                                      XHIBIT.XHB_HEARING_LIST XHL
                                                 WHERE XHL.COURT_ID = xhb_col_tt(i).court_id
                                                   AND XHL.START_DATE = xhb_col_tt(i).list_date
                                                   AND XHL.LIST_ID = XS.LIST_ID                                                   
                                                   AND XHL.LIST_TYPE = 'D') 
                                                   -- CTX-3822 Accomodate multiple Hearing IDs using IN   
                         AND xsh.HEARING_ID in (SELECT xh.HEARING_ID
                                                 FROM XHIBIT.XHB_HEARING xh
                                                WHERE xh.court_id = xhb_col_tt(i).court_id
                                                  AND xh.CASE_ID = xhb_col_tt(i).case_id
                                                  AND xh.HEARING_START_DATE = xhb_col_tt(i).list_date);
                            
                           v_sch_ins_rows := SQL%ROWCOUNT;  
                           
                           IF v_sch_ins_rows > 0 THEN 
                              v_sch_ins_status := 'U'; -- XHIBIT TABLE updated
                           ELSIF v_sch_ins_rows = 0 THEN
                                 v_sch_ins_status := 'N'; -- No Action Performed on XHIBIT table
                           END IF;  

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2404:XHB_CASE_ON_LIST - UPDATE XHB_SCHEDULED_HEARING - '||v_sch_ins_rows||' updated!');

                         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATE XHB_SCHEDULED_HEARING - updated '||v_sch_ins_rows||' rows for chd_id : '||xhb_col_tt(i).chd_id||' , list_date : '||xhb_col_tt(i).list_date||' , list_type : '||xhb_col_tt(i).list_type||' ,  ref_cracked_effective_id : '||xhb_col_tt(i).ref_cracked_effective_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_sch_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
                    EXCEPTION WHEN OTHERS THEN
                               v_err_message := SQLERRM;
       
                               DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN UPDATE XHB_SCHEDULED_HEARING for CREST_COURT : '||p_crest_court_id||', chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_ctd_id : '||v_ctd_id||'-'||SUBSTR(v_err_message,1,100));

                                 insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                               ,p_action_name        => 'upd_xhb_case_on_list_crest - CTX-2404'
                                               ,p_run_time           =>  sysdate
                                               ,p_log_msg_type       =>  'E' -- Error
                                               ,p_log_msg            =>  'UPDATE XHB_SCHEDULED_HEARING - Error processing chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', list_date : '||xhb_col_tt(i).list_date||' ,  ref_cracked_effective_id : '||xhb_col_tt(i).ref_cracked_effective_id||' Error: '||SUBSTR(v_err_message,1,150)
                                               ,p_err_row_count      => NULL 
                                               ,p_success_row_count  => v_count_number_of_rows
                                               ,p_last_updated_by    => 'DATA MIGRATION'
                                               ,p_created_by         => 'DATA MIGRATION'
                                              );
                    END;                                                  
          
                  END IF;
                
                END; -- CTX-3734
 EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
       --  ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_XHB_CASE_ON_LIST_crest for CREST_COURT : '||p_crest_court_id||', chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_ctd_id : '||v_ctd_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest - CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_case_on_list_crest - Error processing chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_ctd_id : '||v_ctd_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_CASE_HEARING_DAY_DM
         -- tables with error status/messages

            UPDATE XHBSTG_CASE_HEARING_DAY_DM xcol
            SET    xcol.xhibit_etl_date   = SYSDATE
                  ,xcol.xhibit_court_id   = v_xhibit_court_id
                  ,xcol.xhibit_enrich_date = SYSDATE
                  ,xcol.xhibit_etl_status = 'X' -- Error
                  ,xcol.xhibit_etl_err_message = v_err_message
            WHERE  xcol.crest_court_id   =  p_crest_court_id
              AND  xcol.chd_id = v_chd_id
              AND  xcol.case_no = v_case_no
              AND  xcol.case_type = v_case_type
              AND  xcol.ctd_id = v_ctd_id; 
              
          END; -- CTX-3252
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_col_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_col_details_1%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_col_details_1;

------------------------------------------------------------------------

      -- Here we are going to populate the rows in XHB_CASE_ON_LIST table that have not yet been populated with CREST data
      -- for rows where CTD_ID is null
    xhb_col_tt := NULL;   
    OPEN cur_crest_col_details_2;
    LOOP
 
    FETCH cur_crest_col_details_2 BULK COLLECT INTO xhb_col_tt LIMIT g_bulk_limit;
 
    IF xhb_col_tt IS NOT NULL AND xhb_col_tt.COUNT > 0 THEN

        FOR i IN xhb_col_tt.FIRST .. xhb_col_tt.LAST LOOP
 
           BEGIN -- CTX-3252
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTING XHB_CASE_ON_LIST - inserting  chd id - '||xhb_col_tt(i).chd_id||', case_no : '||xhb_col_tt(i).case_no||', case_type : '||xhb_col_tt(i).case_type||', ctd_id : '||xhb_col_tt(i).ctd_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('INSERTING XHB_CASE_ON_LIST- FOR CREST_COURT_ID = '||xhb_col_tt(i).crest_court_id);
    
 -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_chd_id     := xhb_col_tt(i).chd_id;
            v_case_no     := xhb_col_tt(i).case_no;
            v_case_type   :=  xhb_col_tt(i).case_type;
            v_ctd_id     := xhb_col_tt(i).ctd_id;
            v_ref_cracked_eff_ind := xhb_col_tt(i).ref_cracked_effective_id;
            
             DBMS_OUTPUT.PUT_LINE('v_chd_id : '||v_chd_id||', v_case_no : '||v_case_no||' , v_case_type : ' ||v_case_type||', v_ctd_id : '||v_ctd_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
            
         -- INSERT and populate this NEW table XHB_CASE_ON_LIST with data from CREST  
                     INSERT INTO  xhibit.XHB_CASE_ON_LIST
                                  ( case_on_list_id
                                  , case_id 
                                  , list_id   
                                  , sitting_on_list_id
                                  , court_site_id
                                  , court_room_id
                                  , reserved 
                                  , floater_case
                                  , time_marking_id
                                  , time_listed
                                  , is_court_room_list_entry
                                  , hearing_type_id
                                  , reason_for_removal
                                  , cracked_ineffective_id
                                  , obs_ind
                                  , seq_no
                                  , case_diary_fixture_id
                                  , date_of_removal
                                  , list_note_predefined_id
                                  , list_note_text
                                  , parent_case_on_list_id
                                  , creation_date
                                  , created_by  
                                  , last_update_date 
                                  , last_updated_by )
                           VALUES 
                                  (xhibit.XHB_CASE_ON_LIST_seq.nextval
                                  ,xhb_col_tt(i).case_id
                                  ,xhb_col_tt(i).list_id
                                  ,xhb_col_tt(i).sitting_on_list_id
                                  ,xhb_col_tt(i).court_site_id
                                  ,xhb_col_tt(i).court_room_id
                                  ,xhb_col_tt(i).reserved
                                  ,xhb_col_tt(i).floater_case
                                  ,xhb_col_tt(i).time_marking_id
                                  ,xhb_col_tt(i).time_listed
                                  ,xhb_col_tt(i).is_court_room_list_entry
                                  ,xhb_col_tt(i).hearing_type_id
                                  ,xhb_col_tt(i).reason_for_removal
                                  ,xhb_col_tt(i).ref_cracked_effective_id -- CTX-3909
                                  ,xhb_col_tt(i).obs_ind
                                  ,xhb_col_tt(i).seq_no
                                  ,xhb_col_tt(i).case_diary_fixture_id
                                  ,xhb_col_tt(i).date_of_removal
                                  ,xhb_col_tt(i).list_note_predefined_id
                                  ,xhb_col_tt(i).list_note_text
                                  ,xhb_col_tt(i).parent_case_on_list_id
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,SYSDATE
                                  ,'DATA_MIGRATION');      


    
    v_col_ins_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2404:XHB_CASE_ON_LIST - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_col_ins_rows);
    
               -- Update XHBSTG_CASE_HEARING_DAY_DM table for the rows processed
         IF v_col_ins_rows > 0 THEN 
            v_col_ins_status := 'I'; -- XHIBIT TABLE inserted
         ELSIF v_col_ins_rows = 0 THEN
            v_col_ins_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;  

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_CASE_HEARING_DAY_DM - updated chd_id '||xhb_col_tt(i).chd_id||' row with ETL_STATUS '||v_col_ins_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_col_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         

           -- Update XHBSTG_CASE_HEARING_DAY_DM table for the rows processed
            UPDATE XHBSTG_CASE_HEARING_DAY_DM xcg
            SET    xcg.xhibit_etl_date   = SYSDATE
                  ,xcg.xhibit_court_id   = v_xhibit_court_id
                  ,xcg.xhibit_enrich_date = SYSDATE
                  ,xcg.xhibit_etl_status = v_col_ins_status                
            WHERE  xcg.crest_court_id   =  p_crest_court_id
              AND  xcg.chd_id = xhb_col_tt(i).chd_id
              AND  xcg.case_no = xhb_col_tt(i).case_no
              AND  xcg.case_type = xhb_col_tt(i).case_type;
              --AND  xcg.ctd_id = xhb_col_tt(i).ctd_id;     

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2404:XHBSTG_CASE_HEARING_DAY_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
                
                BEGIN -- CTX-3734
                
                   IF xhb_col_tt(i).ref_cracked_effective_id is NOT NULL and 
                        xhb_col_tt(i).list_type = 'D' THEN
                      
                    BEGIN
                      UPDATE xhibit.XHB_SCHEDULED_HEARING xsh
                         SET xsh.REF_CRACKED_EFFECTIVE_ID = xhb_col_tt(i).ref_cracked_effective_id 
                       WHERE xsh.SITTING_ID in (SELECT XS.SITTING_ID
                                                 FROM XHIBIT.XHB_SITTING XS,
                                                      XHIBIT.XHB_HEARING_LIST XHL
                                                 WHERE XHL.COURT_ID = xhb_col_tt(i).court_id
                                                   AND XHL.START_DATE = xhb_col_tt(i).list_date
                                                   AND XHL.LIST_ID = XS.LIST_ID                                                   
                                                   AND XHL.LIST_TYPE = 'D') 
                                                   -- CTX-3822 Accomodate multiple Hearing IDs using IN                                                      
                         AND xsh.HEARING_ID in (SELECT xh.HEARING_ID
                                                 FROM XHIBIT.XHB_HEARING xh
                                                WHERE xh.court_id = xhb_col_tt(i).court_id
                                                  AND xh.CASE_ID = xhb_col_tt(i).case_id
                                                  AND xh.HEARING_START_DATE = xhb_col_tt(i).list_date);
                            
                           v_sch_ins_rows := SQL%ROWCOUNT;  
                           
                           IF v_sch_ins_rows > 0 THEN 
                              v_sch_ins_status := 'U'; -- XHIBIT TABLE updated
                           ELSIF v_sch_ins_rows = 0 THEN
                                 v_sch_ins_status := 'N'; -- No Action Performed on XHIBIT table
                           END IF;  

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2404:XHB_CASE_ON_LIST - UPDATE XHB_SCHEDULED_HEARING - '||v_sch_ins_rows||' updated!');

                         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATE XHB_SCHEDULED_HEARING - updated '||v_sch_ins_rows||' rows for chd_id : '||xhb_col_tt(i).chd_id||' , list_date : '||xhb_col_tt(i).list_date||' , list_type : '||xhb_col_tt(i).list_type||' ,  ref_cracked_effective_id : '||xhb_col_tt(i).ref_cracked_effective_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_sch_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
                    EXCEPTION WHEN OTHERS THEN
                               v_err_message := SQLERRM;
       
                               DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN UPDATE XHB_SCHEDULED_HEARING for CREST_COURT : '||p_crest_court_id||', chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_ctd_id : '||v_ctd_id||'-'||SUBSTR(v_err_message,1,100));

                                 insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                               ,p_action_name        => 'upd_xhb_case_on_list_crest - CTX-2404'
                                               ,p_run_time           =>  sysdate
                                               ,p_log_msg_type       =>  'E' -- Error
                                               ,p_log_msg            =>  'UPDATE XHB_SCHEDULED_HEARING - Error processing chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', list_date : '||xhb_col_tt(i).list_date||' ,  ref_cracked_effective_id : '||xhb_col_tt(i).ref_cracked_effective_id||' Error: '||SUBSTR(v_err_message,1,150)
                                               ,p_err_row_count      => NULL 
                                               ,p_success_row_count  => v_count_number_of_rows
                                               ,p_last_updated_by    => 'DATA MIGRATION'
                                               ,p_created_by         => 'DATA MIGRATION'
                                              );
                    END;                                                  
          
                  END IF;
                
                END; -- CTX-3734                
 EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
       --  ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_XHB_CASE_ON_LIST_crest for CREST_COURT : '||p_crest_court_id||', chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_ctd_id : '||v_ctd_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest - CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_case_on_list_crest - Error processing chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_ctd_id : '||v_ctd_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_CASE_HEARING_DAY_DM
         -- tables with error status/messages

            UPDATE XHBSTG_CASE_HEARING_DAY_DM xcol
            SET    xcol.xhibit_etl_date   = SYSDATE
                  ,xcol.xhibit_court_id   = v_xhibit_court_id
                  ,xcol.xhibit_enrich_date = SYSDATE
                  ,xcol.xhibit_etl_status = 'X' -- Error
                  ,xcol.xhibit_etl_err_message = v_err_message
            WHERE  xcol.crest_court_id   =  p_crest_court_id
              AND  xcol.chd_id = v_chd_id
              AND  xcol.case_no = v_case_no
              AND  xcol.case_type = v_case_type
              AND  xcol.ctd_id = v_ctd_id; 
   
        END; -- CTX-3252
 
    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_col_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_col_details_2%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_col_details_2;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_ON_LIST : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_CASE_HEARING_DAY_DM 
               ------ for given CREST COURT ID to 'N' i.e. NOT processed
               
                       -- Update XHBSTG_CASE_HEARING_DAY_DM table for the rows NOT processed
            UPDATE XHBSTG_CASE_HEARING_DAY_DM xcg
            SET    xcg.xhibit_etl_date   = SYSDATE
                  ,xcg.xhibit_court_id   = v_xhibit_court_id
                  ,xcg.xhibit_enrich_date = SYSDATE
                  ,xcg.xhibit_etl_status = 'N' -- Not processed
            WHERE  xcg.crest_court_id   =  p_crest_court_id
              AND  xcg.xhibit_etl_status is NULL 
              AND  xcg.xhibit_enrich_date  is NULL
              AND  xcg.xhibit_etl_date  is NULL;
            --  AND  xcg.xhibit_court_id is NULL; -- incorrect condition excluded
           
              v_col_np_rows := SQL%ROWCOUNT;
                         
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest- CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_ON_LIST : updating XHBSTG_CASE_HEARING_DAY_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_col_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2404:XHBSTG_CASE_HEARING_DAY_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2404:XHBSTG_CASE_HEARING_DAY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_col_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
       --  ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_XHB_CASE_ON_LIST_crest for CREST_COURT : '||p_crest_court_id||', chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_ctd_id : '||v_ctd_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_on_list_crest - CTX-2404'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_case_on_list_crest - Error processing chd_id : '||v_chd_id||', v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_ctd_id : '||v_ctd_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
              
            COMMIT;


END upd_xhb_case_on_list_crest;

/**
  * NAME       : upd_xhb_pros_ref_sol_firm_with_crest
  * DESCRIPTION: CTX-2184. New CTX fields for XHB_PROSECUTOR_REF_SOL_FIRM - - Sec 4.3.2.7 req [4975.DM.010] 
  *              Update existing rows in XHIBIT xhb_prosecutor_ref_sol_firm table to add new data elements from CREST
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE update_xhb_prsf_with_crest(p_crest_court_id IN xhbstg_case_party_sof_dm.crest_court_id%TYPE)
IS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
    TYPE xhbstg_rsf_dm_rec IS RECORD
    (
      sol_ref        xhbstg_case_party_sof_dm.sol_ref%TYPE
    , cpf_id         xhbstg_case_party_sof_dm.cpf_id%TYPE
    , crest_court_id xhbstg_case_party_sof_dm.crest_court_id%TYPE
    , prsf_id        xhibit.xhb_prosecutor_ref_sol_firm.prosecutor_ref_sol_firm_id%TYPE
    );

    TYPE xhbstg_rsf_dm_type IS TABLE OF xhbstg_rsf_dm_rec;
    xhbstg_rsf_dm_tt  xhbstg_rsf_dm_type;
  
    CURSOR ref_sol_firm_cur IS
    SELECT xcpsd.sol_ref
    ,      xcpsd.cpf_id
    ,      xcpsd.crest_court_id
    ,      xprsf.prosecutor_ref_sol_firm_id
    FROM xhibit.xhb_prosecutor_ref_sol_firm xprsf
    ,    xhbstg_case_party_sof_dm xcpsd
    ,    xhibit.xhb_case_prosecutor_agency xcpa
    ,    xhibit.xhb_case xc
    WHERE xprsf.crest_cpf_id = xcpsd.cpf_id
    AND   xcpsd.crest_court_id = p_crest_court_id
    AND   xc.court_id = v_xhibit_court_id
    AND   xprsf.case_pros_agency_id = xcpa.case_pros_agency_id
    AND   xcpa.case_id = xc.case_id
    AND   NVL(xcpa.obs_ind,'N') <> 'Y'
    AND   NVL(xprsf.obs_ind,'N') <> 'Y'
    /* CTX-3214 Commented these out so to process again rows already processed by upd_xhb_defendant_with_crest since each procedure look for different columns in the same rows in the table xhbstg_case_party_sof_dm
    AND   NVL(xcpsd.xhibit_etl_status,'N') NOT IN ('U','I')
    AND   xcpsd.xhibit_enrich_date IS NULL */
    ;
    
    v_err_message          VARCHAR2(2000);
    v_cpf_id               xhbstg_case_party_sof_dm.cpf_id%TYPE;
    v_prsf_upd_rows        NUMBER := 0;
    v_prsf_upd_status      VARCHAR2(10);
    v_count_number_of_rows NUMBER := 0;
    v_total_row_count      NUMBER :=0;

BEGIN

    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing row in xhb_prosecutor_ref_sol_firm for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
    
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'update_xhb_prsf_with_crest- CTX-2184'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_PROSECUTOR_REF_SOL_FIRM - Starting process of updating existing rows for new columns with the required data from CREST'
                 ,p_err_row_count      =>  NULL
                 ,p_success_row_count  =>  NULL
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );
                 
   DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing rows in XHB_PROSECUTOR_REF_SOL_FIRM for new columns with the required data from CREST');

    
    OPEN ref_sol_firm_cur;
    LOOP
    FETCH ref_sol_firm_cur BULK COLLECT INTO xhbstg_rsf_dm_tt LIMIT g_bulk_limit;
    
     IF xhbstg_rsf_dm_tt IS NOT NULL AND xhbstg_rsf_dm_tt.COUNT > 0 THEN  -- prevents numeric or value error later if nothing in the array
        
            v_total_row_count := v_total_row_count + xhbstg_rsf_dm_tt.COUNT;  -- we want to count number of cases found as we collect them
            
            FOR i IN xhbstg_rsf_dm_tt.FIRST .. xhbstg_rsf_dm_tt.LAST 
             LOOP
              BEGIN -- CTX3252 - BEGIN BLOCK for handling exception for each record and to continue processing 
                  v_cpf_id  := xhbstg_rsf_dm_tt(i).cpf_id;
              
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                           ,p_action_name        => 'update_xhb_prsf_with_crest- CTX-2184'
                           ,p_run_time           =>  sysdate
                           ,p_log_msg_type       =>  'I' -- Information
                           ,p_log_msg            =>  'UPDATING XHB_PROSECUTOR_REF_SOL_FIRM - processing ref sol firm id - '||xhbstg_rsf_dm_tt(i).prsf_id||' set sol ref to: '||xhbstg_rsf_dm_tt(i).sol_ref
                           ,p_err_row_count      =>  NULL
                           ,p_success_row_count  => NULL
                           ,p_last_updated_by    => 'DATA MIGRATION'
                           ,p_created_by         => 'DATA MIGRATION'
                           );
                  DBMS_OUTPUT.PUT_LINE(' ');
                  DBMS_OUTPUT.PUT_LINE('UPDATING XHB_PROSECUTOR_REF_SOL_FIRM - processing ref sol firm id - '||xhbstg_rsf_dm_tt(i).prsf_id||' set sol ref to: '||xhbstg_rsf_dm_tt(i).sol_ref);
      
              -- Update the rows in XHB_PROSECUTOR_REF_SOL_FIRM with data from CREST  
              
                  -- Update the rows in xhb_prosecutor_ref_sol_firm with data from CREST
                  UPDATE  xhibit.xhb_prosecutor_ref_sol_firm xprsf
                  SET     xprsf.solicitor_ref = xhbstg_rsf_dm_tt(i).sol_ref 
                        , xprsf.last_update_date = SYSDATE
                        , xprsf.last_updated_by  = 'DATA MIGRATION'
                  WHERE  xprsf.prosecutor_ref_sol_firm_id  =  xhbstg_rsf_dm_tt(i).prsf_id
                  AND  NVL(xprsf.obs_ind,'N')!='Y'
                  ;
                  
                  v_prsf_upd_rows := SQL%ROWCOUNT;
                  DBMS_OUTPUT.PUT_LINE(' ');
                  DBMS_OUTPUT.PUT_LINE('CTX-2184:XHB_PROSECUTOR_REF_SOL_FIRM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_prsf_upd_rows);
              
                         -- Update XHBSTG_SUBJECT_DM table for the rows processed
                   IF v_prsf_upd_rows > 0 THEN 
                      v_prsf_upd_status := 'U'; -- XHIBIT TABLE updated
                   ELSIF v_prsf_upd_rows = 0 THEN
                      v_prsf_upd_status := 'N'; -- No Action Performed on XHIBIT table
                   END IF;  
                    
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'update_xhb_prsf_with_crest- CTX-2184'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'XHB_PROSECUTOR_REF_SOL_FIRM updated. Solicitor ref set to : '||xhbstg_rsf_dm_tt(i).sol_ref 
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  =>  v_prsf_upd_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );  
      
                  -- Update xhbstg_case_party_sof_dm table for the rows processed
                  UPDATE xhbstg_case_party_sof_dm xcpsf
                  SET    xcpsf.xhibit_etl_date   = SYSDATE
                        ,xcpsf.xhibit_court_id   = v_xhibit_court_id
                        ,xcpsf.xhibit_enrich_date = SYSDATE
                        ,xcpsf.xhibit_etl_status = v_prsf_upd_status
                  WHERE  xcpsf.crest_court_id   =  p_crest_court_id
                  AND    xcpsf.cpf_id =  v_cpf_id
                  ;
                  
               DBMS_OUTPUT.PUT_LINE(' ');
               DBMS_OUTPUT.PUT_LINE('CTX-2184:XHBSTG_CASE_PARTY_SOF_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
               
               EXCEPTION
                  WHEN OTHERS THEN
                      v_err_message := SQLERRM;
                      
                      DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_pros_ref_sol_firm_with_crest for CREST_COURT_ID : '||p_crest_court_id||' - '||SUBSTR(v_err_message,1,110));
                       
                       insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                       ,p_action_name        => 'update_xhb_prsf_with_crest- CTX-2184'
                                       ,p_run_time           =>  sysdate
                                       ,p_log_msg_type       =>  'E' -- Error
                                       ,p_log_msg            =>  'XHB_PROSECUTOR_REF_SOL_FIRM: Updated with Error :'||v_err_message||' Error: '||SUBSTR(v_err_message,1,150)
                                       ,p_err_row_count      =>  v_count_number_of_rows
                                       ,p_success_row_count  =>  NULL
                                       ,p_last_updated_by    => 'DATA MIGRATION'
                                       ,p_created_by         => 'DATA MIGRATION'
                                       ); 
                       
                       BEGIN
                          UPDATE xhbstg_case_party_sof_dm xprsf
                          SET    xprsf.xhibit_etl_date   = SYSDATE
                                ,xprsf.xhibit_court_id   = v_xhibit_court_id
                                ,xprsf.xhibit_enrich_date = SYSDATE
                                ,xprsf.xhibit_etl_status = 'X' -- Error
                                ,xprsf.xhibit_etl_err_message = v_err_message
                          WHERE  xprsf.crest_court_id   =  p_crest_court_id
                          AND    xprsf.cpf_id =  v_cpf_id
                          ;
                        
                        END;
               
             END;    -- CTX3252 - END BLOCK for handling exception for each record and to continue processing 
            END LOOP;
          
          COMMIT; ---Commit after every successful processing BULK COLLECT of rows 
          v_count_number_of_rows := v_count_number_of_rows + xhbstg_rsf_dm_tt.COUNT;
          
        END IF;
        
    EXIT WHEN ref_sol_firm_cur%NOTFOUND;
    
    END LOOP;
    CLOSE ref_sol_firm_cur;


     insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'update_xhb_prsf_with_crest- CTX-2184'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_PROSECUTOR_REF_SOL_FIRM : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               
          -- Update XHBSTG_CASE_PARTY_SOF_DM table for the rows NOT processed
            UPDATE xhbstg_case_party_sof_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = 'N' -- Not processed
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.xhibit_etl_status is NULL 
              AND  xl.xhibit_enrich_date  is NULL
              AND  xl.xhibit_etl_date  is NULL
              AND  xl.xhibit_court_id is NULL;
           
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'update_xhb_prsf_with_crest- CTX-2184'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_PROSECUTOR_REF_SOL_FIRM : updating XHBSTG_CASE_PARTY_SOF_DM with ETL_STATUS = N  where rows NOT processed for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2184:XHB_PROSECUTOR_REF_SOL_FIRM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

    DBMS_OUTPUT.PUT_LINE('XHB_PROSECUTOR_REF_SOL_FIRM Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
--ROLLBACK;

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK;
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_pros_ref_sol_firm_with_crest for CREST_COURT_ID : '||p_crest_court_id||' - '||SUBSTR(v_err_message,1,110));
         
         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'update_xhb_prsf_with_crest- CTX-2184'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'XHB_PROSECUTOR_REF_SOL_FIRM: Updated with Error :'||v_err_message||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      =>  v_count_number_of_rows
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         ); 
 
        COMMIT; -- Commit when running for real    
  
          
END update_xhb_prsf_with_crest;

/**
  * NAME       : update_xhb_rsf_with_crest
  * DESCRIPTION: CTX-2187. New CTX fields for XHB_REF_SOLICITOR_FIRM - - Sec 4.3.2.19 req [4975.DM.024] 
  *              Update existing rows in XHIBIT XHB_REF_SOLICITOR_FIRM table to add new data elements from CREST
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE update_xhb_rsf_with_crest(p_crest_court_id IN xhbstg_case_party_sof_dm.crest_court_id%TYPE)
IS
    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;

    TYPE xhbstg_rsf_dm_rec IS RECORD
    (
      sof_id           xhbstg_solicitor_firm_dm.sof_id%TYPE
    , crest_court_id   xhbstg_solicitor_firm_dm.crest_court_id%TYPE
    , la_code          xhbstg_solicitor_firm_dm.la_code%TYPE
    , court_id         xhibit.xhb_court.court_id%TYPE
    );

    TYPE xhbstg_rsf_dm_type IS TABLE OF xhbstg_rsf_dm_rec;
    xhbstg_rsf_dm_tt  xhbstg_rsf_dm_type;
  
    CURSOR ref_sol_firm_cur IS
    SELECT xstgf.sof_id
    ,      xstgf.crest_court_id
    ,      xstgf.la_code
    ,      xcrt.court_id
    FROM  xhbstg_solicitor_firm_dm   xstgf
    ,     xhibit.xhb_court xcrt
    WHERE xstgf.crest_court_id = p_crest_court_id
    AND   xstgf.crest_court_id = xcrt.crest_court_id
    AND   xstgf.sof_id IS NOT NULL
    AND   NVL(xcrt.obs_ind,'N') <> 'Y'
    AND   NVL(xstgf.xhibit_etl_status,'N') NOT IN ('U','I')
    AND   xstgf.xhibit_enrich_date IS NULL
    AND EXISTS (SELECT 'x' --only return rows that can be updated rather than return everything
                FROM  xhibit.xhb_ref_solicitor_firm   xrsf
                WHERE xcrt.court_id = xrsf.court_id 
                AND   xrsf.crest_sof_id = xstgf.sof_id
                 AND   NVL(xrsf.obs_ind,'N') <> 'Y'
                )
                ;

    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_sof_id                 xhbstg_solicitor_firm_dm.sof_id%TYPE; 
    v_rsf_upd_rows           NUMBER := 0;
    v_rsf_upd_status         CHAR(1) := 'N';
    v_count_number_of_rows   NUMBER := 0;
    v_unprocessed_cnt        NUMBER := 0;
    v_total_row_count        NUMBER := 0;
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing row in XHB_REF_SOLICITOR_FIRM for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
    
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'update_xhb_rsf_with_crest- CTX-2187'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_REF_SOLICITOR_FIRM - Starting process of updating existing rows for new columns with the required data from CREST'
                 ,p_err_row_count      =>  NULL
                 ,p_success_row_count  =>  NULL
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );
    
    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing row in xhb_prosecutor_ref_sol_firm for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    
    OPEN ref_sol_firm_cur;
    LOOP
    FETCH ref_sol_firm_cur BULK COLLECT INTO xhbstg_rsf_dm_tt LIMIT g_bulk_limit;
    
     IF xhbstg_rsf_dm_tt IS NOT NULL AND xhbstg_rsf_dm_tt.COUNT > 0 THEN  -- prevents numeric or value error later if nothing in the array
        
            v_total_row_count := v_total_row_count + xhbstg_rsf_dm_tt.COUNT;  -- we want to count number of cases found as we collect them
            
            FOR i IN xhbstg_rsf_dm_tt.FIRST .. xhbstg_rsf_dm_tt.LAST 
              LOOP
            
                BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'update_xhb_rsf_with_crest- CTX-2187'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'XHB_REF_SOLICITOR_FIRM processing ref sol firm id - '||xhbstg_rsf_dm_tt(i).sof_id||' set la_code ref to: '||xhbstg_rsf_dm_tt(i).la_code
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  =>  NULL
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );  
                  
                  DBMS_OUTPUT.PUT_LINE(' ');
                  DBMS_OUTPUT.PUT_LINE('UPDATING XHB_REF_SOLICITOR_FIRM - processing ref sol firm id - '||xhbstg_rsf_dm_tt(i).sof_id||' set la_code ref to: '||xhbstg_rsf_dm_tt(i).la_code);
                               
                  -- Update the rows in xhb_ref_solicitor_firm with data from CREST
                  UPDATE  xhibit.xhb_ref_solicitor_firm xrsf 
                  SET     xrsf.la_code = xhbstg_rsf_dm_tt(i).la_code
                        , xrsf.last_update_date = SYSDATE
                        , xrsf.last_updated_by  = 'DATA MIGRATION'
                  WHERE  xrsf.crest_sof_id      =  xhbstg_rsf_dm_tt(i).sof_id
                  AND    xrsf.court_id          =  xhbstg_rsf_dm_tt(i).court_id
                  AND  NVL(xrsf.obs_ind,'N')!='Y'
                  ;
    
                  v_rsf_upd_rows := SQL%ROWCOUNT;
                  DBMS_OUTPUT.PUT_LINE(' ');
                  DBMS_OUTPUT.PUT_LINE('CTX-2187:UPDATING XHB_REF_SOLICITOR_FIRM - FOR CREST_COURT_ID = '||xhbstg_rsf_dm_tt(i).crest_court_id||' sof_id : '||xhbstg_rsf_dm_tt(i).sof_id||' LA Code :'||xhbstg_rsf_dm_tt(i).la_code);              
    
                  -- Update xhbstg_solicitor_firm_dm table for the rows processed
                  IF v_rsf_upd_rows > 0 THEN 
                    v_rsf_upd_status := 'U'; -- XHIBIT TABLE updated
                  ELSIF v_rsf_upd_rows = 0 THEN
                    v_rsf_upd_status := 'N'; -- No Action Performed on XHIBIT table
                  END IF ; 
                   
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                   ,p_action_name        => 'update_xhb_rsf_with_crest- CTX-2187'
                                   ,p_run_time           =>  sysdate
                                   ,p_log_msg_type       =>  'I' -- Information
                                   ,p_log_msg            =>  'UPDATING XHB_REF_SOLICITOR_FIRM - FOR CREST_COURT_ID = '||xhbstg_rsf_dm_tt(i).crest_court_id||' sof_id : '||xhbstg_rsf_dm_tt(i).sof_id||' LA Code :'||xhbstg_rsf_dm_tt(i).la_code||' row with ETL_STATUS '||v_rsf_upd_status
                                   ,p_err_row_count      =>  NULL
                                   ,p_success_row_count  => v_rsf_upd_rows
                                   ,p_last_updated_by    => 'DATA MIGRATION'
                                   ,p_created_by         => 'DATA MIGRATION'
                                   );
         
         
                  -- Update xhbstg_solicitor_firm_dm table for the rows processed
                  UPDATE xhbstg_solicitor_firm_dm xsf
                  SET    xsf.xhibit_etl_date   = SYSDATE
                        ,xsf.xhibit_court_id   = v_xhibit_court_id
                        ,xsf.xhibit_enrich_date = SYSDATE
                        ,xsf.xhibit_etl_status = v_rsf_upd_status --Updated
                  WHERE  xsf.crest_court_id   =  p_crest_court_id
                  AND   xsf.sof_id = xhbstg_rsf_dm_tt(i).sof_id
                  ;
    
                  DBMS_OUTPUT.PUT_LINE(' ');
                  DBMS_OUTPUT.PUT_LINE('CTX-2187:update_xhb_rsf_with_crest - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

            EXCEPTION
                WHEN OTHERS THEN
                    v_err_message := SQLERRM;
                    DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_rss_with_crest for CREST_COURT_ID : '||p_crest_court_id||' - '||SUBSTR(v_err_message,1,110));
                     
                    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                   ,p_action_name        => 'update_xhb_rsf_with_crest- CTX-2187'
                                   ,p_run_time           =>  sysdate
                                   ,p_log_msg_type       =>  'E' -- Error
                                   ,p_log_msg            =>  'update_xhb_rsf_with_crest - Error processing court_id: '||p_crest_court_id||' Error: '||SUBSTR(v_err_message,1,150)
                                   ,p_err_row_count      => NULL 
                                   ,p_success_row_count  => v_count_number_of_rows
                                   ,p_last_updated_by    => 'DATA MIGRATION'
                                   ,p_created_by         => 'DATA MIGRATION'
                                   );
                    
                    BEGIN
                        UPDATE xhbstg_solicitor_firm_dm xsf
                        SET    xsf.xhibit_etl_date   = SYSDATE
                              ,xsf.xhibit_court_id   = v_xhibit_court_id
                              ,xsf.xhibit_enrich_date = SYSDATE
                              ,xsf.xhibit_etl_status = 'X' -- Error
                              ,xsf.xhibit_etl_err_message = v_err_message
                        WHERE  xsf.crest_court_id   =  p_crest_court_id
                        AND    xsf.sof_id = v_sof_id;
                      
                    END;

            END;  -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

        END LOOP;
  
      COMMIT; ---Commit after every successful processing BULK COLLECT of rows 

        v_count_number_of_rows := v_count_number_of_rows + xhbstg_rsf_dm_tt.COUNT;

     END IF;
        
    EXIT WHEN ref_sol_firm_cur%NOTFOUND;
    
    END LOOP;
    CLOSE ref_sol_firm_cur;

   
     insert_dm_log (p_crest_court_id     => p_crest_court_id 
                   ,p_action_name        => 'update_xhb_rsf_with_crest- CTX-2187'
                   ,p_run_time           =>  sysdate
                   ,p_log_msg_type       =>  'I' -- Information
                   ,p_log_msg            =>  'XHB_REF_SOLICITOR_FIRM : Processed '||v_count_number_of_rows||' successfully!'
                   ,p_err_row_count      => NULL 
                   ,p_success_row_count  => v_count_number_of_rows
                   ,p_last_updated_by    => 'DATA MIGRATION'
                   ,p_created_by         => 'DATA MIGRATION'
                   );
               
      -- Update xhbstg_solicitor_firm_dm table for the rows NOT processed
      UPDATE xhbstg_solicitor_firm_dm xl
      SET    xl.xhibit_etl_date   = SYSDATE
            ,xl.xhibit_court_id   = v_xhibit_court_id
            ,xl.xhibit_enrich_date = SYSDATE
            ,xl.xhibit_etl_status = 'N' -- Not processed
      WHERE  xl.crest_court_id   =  p_crest_court_id
        AND  xl.xhibit_etl_status is NULL 
        AND  xl.xhibit_enrich_date  is NULL
        AND  xl.xhibit_etl_date  is NULL
        AND  xl.xhibit_court_id is NULL;
     
     v_unprocessed_cnt := SQL%ROWCOUNT;
     
     insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'update_xhb_rsf_with_crest- CTX-2187'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_REF_SOLICITOR_FIRM : updating xhbstg_solicitor_firm_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  '||p_crest_court_id
                 ,p_err_row_count      => NULL 
                 ,p_success_row_count  => v_unprocessed_cnt
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );   

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2187:XHB_REF_SOLICITOR_FIRM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

    DBMS_OUTPUT.PUT_LINE('XHB_REF_SOLICITOR_FIRM Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

    COMMIT;
    
EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;

        --ROLLBACK; --CTX-3252
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_rss_with_crest for CREST_COURT_ID : '||p_crest_court_id||' - '||SUBSTR(v_err_message,1,110));
         
        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                       ,p_action_name        => 'update_xhb_rsf_with_crest- CTX-2187'
                       ,p_run_time           =>  sysdate
                       ,p_log_msg_type       =>  'E' -- Error
                       ,p_log_msg            =>  'update_xhb_rsf_with_crest - Error processing court_id: '||p_crest_court_id||' Error: '||SUBSTR(v_err_message,1,150)
                       ,p_err_row_count      => NULL 
                       ,p_success_row_count  => v_count_number_of_rows
                       ,p_last_updated_by    => 'DATA MIGRATION'
                       ,p_created_by         => 'DATA MIGRATION'
                       );
         
        COMMIT; -- Commit when running for real 
                      
END update_xhb_rsf_with_crest;


/**
  * NAME       : update_xhb_rsf_email_with_crest
  * DESCRIPTION: CTX-2982. New CTX fields for XHB_REF_SOLICITOR_FIRM - - Sec 4.3.2.19 req [4975.DM.024/A] 
  *              Update existing rows in XHIBIT XHB_REF_SOLICITOR_FIRM table to add new data elements from CREST
  * PARAMETERS : p_court_id  - The CREST Court ID being processed will be passed to this procedure
  * 11/12/2018     S Sethuraman : CTX-3252 - Exception Handling moved inside block to continue processing without RollBack
**/
PROCEDURE update_xhb_rsf_mail_with_crest(p_crest_court_id IN xhbstg_case_party_sof_dm.crest_court_id%TYPE)
IS
    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;

    TYPE xhbstg_rsf_email_dm_rec IS RECORD
    (
     contact_type       VARCHAR2(20)
    ,mail              xhbstg_solicitor_firm_dm.sof_email%type
    ,email_format      XHIBIT.XHB_CONTACT_DETAIL.email_format%TYPE
    ,pager_net         XHIBIT.XHB_CONTACT_DETAIL.pager_net%TYPE
    ,address_id        XHIBIT.xhb_address.address_id%TYPE
    ,sof_id            xhbstg_solicitor_firm_dm.sof_id%TYPE
    ,crest_court_id    xhbstg_solicitor_firm_dm.crest_court_id%TYPE
    ,court_id          xhibit.xhb_court.court_id%TYPE
    );

    TYPE xhbstg_rsf_email_dm_type IS TABLE OF xhbstg_rsf_email_dm_rec;
    xhbstg_rsf_email_dm_tt  xhbstg_rsf_email_dm_type;
  
   /*There is no checking of the ETL fields in this cursor because they would have been set in 
    update_xhb_rsf_with_crest procedure.  If they were included here, this cursor would
    never return data*/
    CURSOR ref_sol_firm_cur IS
    SELECT 'Non Secure Email' AS CONTACT_TYPE
    ,      stg.sof_email as MAIL
    ,      NULL as email_format
    ,      NULL as pager_net
    ,      xsf.address_id
    ,      stg.sof_id
    ,      xcrt.crest_court_id
    ,      xcrt.court_id
    FROM xhbstg_solicitor_firm_dm stg
    ,    xhibit.xhb_court xcrt
    ,    xhibit.xhb_ref_solicitor_firm xsf
    WHERE stg.crest_court_id = p_crest_court_id
    AND   stg.crest_court_id = xcrt.crest_court_id
    AND   xsf.crest_sof_id = stg.sof_id  
    AND   xcrt.court_id = xsf.court_id
    AND   stg.sof_email IS NOT NULL
    --AND   stg.sof_id = 11400
    UNION
    select 'Secure Email' AS CONTACT_TYPE
    ,      stg.sof_email_secure as MAIL
    ,      NULL as email_format
    ,      NULL as pager_net
    ,      xsf.address_id
    ,      stg.sof_id
    ,      xcrt.crest_court_id
    ,      xcrt.court_id
    FROM xhbstg_solicitor_firm_dm stg
    ,    xhibit.xhb_court xcrt
    ,    xhibit.xhb_ref_solicitor_firm xsf
    WHERE stg.crest_court_id = p_crest_court_id
    AND   stg.crest_court_id = xcrt.crest_court_id
    AND   xsf.crest_sof_id = stg.sof_id  
    AND   xcrt.court_id = xsf.court_id
    AND   stg.sof_email_secure IS NOT NULL
    --AND   stg.sof_id = 11400
    ;         

    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_sof_id                 xhbstg_solicitor_firm_dm.sof_id%TYPE; 
    v_rsf_upd_rows           NUMBER := 0;
    v_rsf_upd_status         CHAR(1) := 'N';
    v_count_number_of_rows   NUMBER := 0;
    v_unprocessed_cnt        NUMBER := 0;
    v_total_row_count        NUMBER := 0;
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating inserting data to XHB_CONTACT_DETAIL from xhbstg_solicitor_firm_dm from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
    
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'update_xhb_rsf_email_with_crest- CTX-2982'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_CONTACT_DETAIL - Starting process of updating existing rows for new columns with the required data from CREST'
                 ,p_err_row_count      =>  NULL
                 ,p_success_row_count  =>  NULL
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );
    
    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing row in xhb_prosecutor_ref_sol_firm for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    
    OPEN ref_sol_firm_cur;
    LOOP
    FETCH ref_sol_firm_cur BULK COLLECT INTO xhbstg_rsf_email_dm_tt LIMIT g_bulk_limit;
    
     IF xhbstg_rsf_email_dm_tt IS NOT NULL AND xhbstg_rsf_email_dm_tt.COUNT > 0 THEN  -- prevents numeric or value error later if nothing in the array
        
            v_total_row_count := v_total_row_count + xhbstg_rsf_email_dm_tt.COUNT;  -- we want to count number of cases found as we collect them
            
            FOR i IN xhbstg_rsf_email_dm_tt.FIRST .. xhbstg_rsf_email_dm_tt.LAST 
             LOOP
               BEGIN -- CTX-3252
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'update_xhb_rsf_email_with_crest- CTX-2982'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'XHB_REF_SOLICITOR_FIRM processing ref sol firm id - '||
                                                       xhbstg_rsf_email_dm_tt(i).sof_id||
                                                       ' Contact Type : '||xhbstg_rsf_email_dm_tt(i).contact_type||
                                                       ' Mail : '||xhbstg_rsf_email_dm_tt(i).MAIL||
                                                       ' Email Format : ' ||xhbstg_rsf_email_dm_tt(i).email_format||
                                                       ' Pager Net : '    ||xhbstg_rsf_email_dm_tt(i).pager_net||
                                                       ' Address ID : '   ||xhbstg_rsf_email_dm_tt(i).address_id
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  =>  NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );  
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('UPDATING XHB_REF_SOLICITOR_FIRM - processing ref sol firm id - ''XHB_REF_SOLICITOR_FIRM processing ref sol firm id - '||
                                                       xhbstg_rsf_email_dm_tt(i).sof_id||
                                                       ' Contact Type : '||xhbstg_rsf_email_dm_tt(i).contact_type||
                                                       ' Mail : '||xhbstg_rsf_email_dm_tt(i).MAIL||
                                                       ' Email Format : ' ||xhbstg_rsf_email_dm_tt(i).email_format||
                                                       ' Pager Net : '    ||xhbstg_rsf_email_dm_tt(i).pager_net||
                                                       ' Address ID : '   ||xhbstg_rsf_email_dm_tt(i).address_id
                                                       );
                             
                -- Insert the rows to xhb_contact_detail with data from XHBSTG_SOLICITOR_FIRM_DM
                INSERT INTO xhibit.xhb_contact_detail (CONTACT_ID
                                                      ,CONTACT_TYPE
                                                      ,CONTACT_VALUE
                                                      ,EMAIL_FORMAT
                                                      ,PAGER_NET
                                                      ,ADDRESS_ID)
                VALUES  (xhibit.xhb_contact_detail_seq.nextval
                        ,xhbstg_rsf_email_dm_tt(i).contact_type
                        ,xhbstg_rsf_email_dm_tt(i).MAIL
                        ,xhbstg_rsf_email_dm_tt(i).EMAIL_FORMAT
                        ,xhbstg_rsf_email_dm_tt(i).PAGER_NET
                        ,xhbstg_rsf_email_dm_tt(i).ADDRESS_ID
                        );

    v_rsf_upd_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2982:UPDATING XHB_REF_SOLICITOR_FIRM - FOR CREST_COURT_ID = '||xhbstg_rsf_email_dm_tt(i).crest_court_id||' sof_id : '||xhbstg_rsf_email_dm_tt(i).sof_id);              
    
     -- Update xhbstg_solicitor_firm_dm table for the rows processed
         IF v_rsf_upd_rows > 0 THEN 
            v_rsf_upd_status := 'U'; -- XHIBIT TABLE updated
         ELSIF v_rsf_upd_rows = 0 THEN
            v_rsf_upd_status := 'N'; -- No Action Performed on XHIBIT table
         END IF ; 
         
         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'update_xhb_rsf_email_with_crest- CTX-2982'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_REF_SOLICITOR_FIRM - FOR CREST_COURT_ID = '||xhbstg_rsf_email_dm_tt(i).crest_court_id||
                                                       xhbstg_rsf_email_dm_tt(i).sof_id||
                                                       ' Contact Type : '||xhbstg_rsf_email_dm_tt(i).contact_type||
                                                       ' Mail : '||xhbstg_rsf_email_dm_tt(i).MAIL||
                                                       ' Email Format : ' ||xhbstg_rsf_email_dm_tt(i).email_format||
                                                       ' Pager Net : '    ||xhbstg_rsf_email_dm_tt(i).pager_net||
                                                       ' Address ID : '   ||xhbstg_rsf_email_dm_tt(i).address_id||
                                                       ' row with ETL_STATUS '||v_rsf_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_rsf_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         
         
                -- Update xhbstg_solicitor_firm_dm table for the rows processed
                UPDATE xhbstg_solicitor_firm_dm xsf
                SET    xsf.xhibit_etl_date   = SYSDATE
                      ,xsf.xhibit_court_id   = v_xhibit_court_id
                      ,xsf.xhibit_enrich_date = SYSDATE
                      ,xsf.xhibit_etl_status = v_rsf_upd_status --Updated
                WHERE  xsf.crest_court_id   =  p_crest_court_id
                AND   xsf.sof_id = xhbstg_rsf_email_dm_tt(i).sof_id
                ;
    
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('CTX-2982:update_xhb_rsf_with_crest - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        -- ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_rss_with_crest for CREST_COURT_ID : '||p_crest_court_id||' - '||SUBSTR(v_err_message,1,120));
         
         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'update_xhb_rsf_email_with_crest- CTX-2982'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'update_xhb_rsf_email_with_crest - Error processing court_id: '||p_crest_court_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         

            UPDATE xhbstg_solicitor_firm_dm xsf
            SET    xsf.xhibit_etl_date   = SYSDATE
                  ,xsf.xhibit_court_id   = v_xhibit_court_id
                  ,xsf.xhibit_enrich_date = SYSDATE
                  ,xsf.xhibit_etl_status = 'X' -- Error
                  ,xsf.xhibit_etl_err_message = v_err_message
            WHERE  xsf.crest_court_id   =  p_crest_court_id
            AND    xsf.sof_id = v_sof_id;
            
          END; -- CTX-3252

        END LOOP;
  
      COMMIT; ---Commit after every successful processing BULK COLLECT of rows 

        v_count_number_of_rows := v_count_number_of_rows + xhbstg_rsf_email_dm_tt.COUNT;

     END IF;
        
    EXIT WHEN ref_sol_firm_cur%NOTFOUND;
    
    END LOOP;
    CLOSE ref_sol_firm_cur;

   
     insert_dm_log (p_crest_court_id     => p_crest_court_id 
                   ,p_action_name        => 'update_xhb_rsf_email_with_crest- CTX-2982'
                   ,p_run_time           =>  sysdate
                   ,p_log_msg_type       =>  'I' -- Information
                   ,p_log_msg            =>  'XHB_REF_SOLICITOR_FIRM : Processed '||v_count_number_of_rows||' successfully!'
                   ,p_err_row_count      => NULL 
                   ,p_success_row_count  => v_count_number_of_rows
                   ,p_last_updated_by    => 'DATA MIGRATION'
                   ,p_created_by         => 'DATA MIGRATION'
                   );
               
      -- Update xhbstg_solicitor_firm_dm table for the rows NOT processed
      UPDATE xhbstg_solicitor_firm_dm xl
      SET    xl.xhibit_etl_date   = SYSDATE
            ,xl.xhibit_court_id   = v_xhibit_court_id
            ,xl.xhibit_enrich_date = SYSDATE
            ,xl.xhibit_etl_status = 'N' -- Not processed
      WHERE  xl.crest_court_id   =  p_crest_court_id
        AND  xl.xhibit_etl_status is NULL 
        AND  xl.xhibit_enrich_date  is NULL
        AND  xl.xhibit_etl_date  is NULL
        AND  xl.xhibit_court_id is NULL;
     
     v_unprocessed_cnt := SQL%ROWCOUNT;
     
     insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'update_xhb_rsf_email_with_crest- CTX-2187'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_REF_SOLICITOR_FIRM : updating xhbstg_solicitor_firm_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  '||p_crest_court_id
                 ,p_err_row_count      => NULL 
                 ,p_success_row_count  => v_unprocessed_cnt
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );   

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2982:XHB_REF_SOLICITOR_FIRM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

    DBMS_OUTPUT.PUT_LINE('XHB_REF_SOLICITOR_FIRM Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        -- ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_rss_with_crest for CREST_COURT_ID : '||p_crest_court_id||' - '||SUBSTR(v_err_message,1,120));
         
         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'update_xhb_rsf_email_with_crest- CTX-2982'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'update_xhb_rsf_email_with_crest - Error processing court_id: '||p_crest_court_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         
             COMMIT; -- Commit when running for real 
            
END update_xhb_rsf_mail_with_crest;


/**
  * NAME       : upd_xhb_court_site_with_crest
  * DESCRIPTION: CTX-2188. New CTX fields for XHB_COURT_SITE - - Sec 4.3.2.23 req [4975.DM.028] 
  *              Update existing rows in XHIBIT XHB_COURT_SITE table to add new data elements from CREST
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_court_site_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE)
IS
   v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
   TYPE xhbstg_crl_dm_rec IS RECORD
    (
      crest_court_id   xhbstg_courtroom_location_dm.crest_court_id%TYPE
    , site_code        xhbstg_courtroom_location_dm.site_code%TYPE
    , site_group       xhbstg_courtroom_location_dm.site_group%TYPE
    , floater_text     xhbstg_courtroom_location_dm.floater_text%TYPE
    , list_name        xhbstg_courtroom_location_dm.list_name%TYPE
    , court_id         xhibit.xhb_court.court_id%TYPE
    );

    TYPE xhbstg_crl_dm_type IS TABLE OF xhbstg_crl_dm_rec;
    xhbstg_crl_tt  xhbstg_crl_dm_type;
     
    CURSOR courtroom_loc_cur IS
    SELECT xcrl.crest_court_id
    ,      xcrl.site_code
    ,      xcrl.site_group
    ,      xcrl.floater_text
    ,      xcrl.list_name
    ,      xcrt.court_id
    FROM  xhbstg_courtroom_location_dm   xcrl
    ,     xhibit.xhb_court xcrt
    WHERE xcrl.crest_court_id = p_crest_court_id
    AND   xcrl.crest_court_id = xcrt.crest_court_id
    AND   NVL(xcrt.obs_ind,'N') <> 'Y'
    AND   NVL(xcrl.xhibit_etl_status,'N') NOT IN ('U','I')
    AND   xcrl.xhibit_enrich_date IS NULL
    AND EXISTS (SELECT 'x' --only return rows that can be updated rather than return everything
                FROM  xhibit.xhb_court_site xcs
                WHERE xcrt.court_id = xcs.court_id
                AND   xcs.court_site_code = xcrl.site_code
                AND   NVL(xcs.obs_ind,'-')<> 'Y'
                );

    
    v_err_message          xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_site_code            xhibit.xhb_court_site.court_site_code%TYPE;
    v_crl_upd_rows         NUMBER := 0;
    v_crl_upd_status       CHAR(1) := 'N';
    v_crl_no_of_rows       NUMBER := 0;
    v_crl_unprocessed_cnt  NUMBER := 0;
    v_total_row_count      NUMBER := 0;
   
    
BEGIN

      DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing row in XHB_COURT_SITE for new columns with the required data from CREST');
     -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
      v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'update_xhb_crl_with_crest- CTX-2188'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_COURT_SITE - Starting process of updating existing rows for new columns with the required data from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing rows in XHB_COURT_SITE for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    
      -- Here we are going to update the rest of the columns the rows in XHB_LEGAL_AID_ORDER table that have not yet been populated with CREST data
    OPEN courtroom_loc_cur;
    LOOP
    FETCH courtroom_loc_cur BULK COLLECT INTO xhbstg_crl_tt LIMIT g_bulk_limit;
   
    IF xhbstg_crl_tt IS NOT NULL AND xhbstg_crl_tt.COUNT > 0 THEN
   
     v_total_row_count := v_total_row_count + xhbstg_crl_tt.COUNT;  -- we want to count number of cases found as we collect them
       
        FOR i IN xhbstg_crl_tt.FIRST .. xhbstg_crl_tt.LAST 
        LOOP
        
            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                v_site_code := xhbstg_crl_tt(i).site_code;
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_site_with_crest- CTX-2188'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_COURT_SITE - processing crest court id - '||xhbstg_crl_tt(i).crest_court_id||
                                                   ' site code = '||xhbstg_crl_tt(i).site_code||
                                                   ' set site group = '||xhbstg_crl_tt(i).site_group||
                                                   ' floater  text = '||xhbstg_crl_tt(i).floater_text||
                                                   ' list name = '||xhbstg_crl_tt(i).list_name
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('UPDATING UPDATING XHB_COURT_SITE - processing crest court id - '||xhbstg_crl_tt(i).crest_court_id||
                                                   ' site code = '||xhbstg_crl_tt(i).site_code||
                                                   ' set site group = '||xhbstg_crl_tt(i).site_group||
                                                   ' floater  text = '||xhbstg_crl_tt(i).floater_text||
                                                   ' list name = '||xhbstg_crl_tt(i).list_name
                                    );               
                
                -- Update the rows in XHB_COURT_SITE with data from CREST  
                UPDATE  XHIBIT.XHB_COURT_SITE xcs 
                SET     xcs.site_group        = xhbstg_crl_tt(i).site_group
                      , xcs.floater_text      = xhbstg_crl_tt(i).floater_text
                      , xcs.list_name         = xhbstg_crl_tt(i).list_name
                      , xcs.last_update_date = SYSDATE
                      , xcs.last_updated_by  = 'DATA MIGRATION'
                WHERE  xcs.court_id          =  v_xhibit_court_id
                AND    xcs.court_site_code   =  xhbstg_crl_tt(i).site_code
                AND  NVL(xcs.obs_ind,'N')!='Y'
                ;
                v_crl_upd_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2188:XHB_COURT_SITE - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_crl_upd_rows);
    
                -- Update XHBSTG_SUBJECT_DM table for the rows processed
                IF v_crl_upd_rows > 0 THEN 
                    v_crl_upd_status := 'U'; -- XHIBIT TABLE updated
                ELSIF v_crl_upd_rows = 0 THEN
                    v_crl_upd_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'upd_xhb_court_site_with_crest- CTX-2188'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'UPDATING UPDATING XHB_COURT_SITE - processing crest court id - '||xhbstg_crl_tt(i).crest_court_id||
                                                         ' site code = '||xhbstg_crl_tt(i).site_code||
                                                         ' set site group = '||xhbstg_crl_tt(i).site_group||
                                                         ' floater  text = '||xhbstg_crl_tt(i).floater_text||
                                                         ' list name = '||xhbstg_crl_tt(i).list_name||
                                                         ' with ETL_STATUS '||v_crl_upd_status
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  => v_crl_upd_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );

                -- Update xhbstg_courtroom_location_dm table for the rows processed
                UPDATE xhbstg_courtroom_location_dm xl
                SET    xl.xhibit_etl_date   = SYSDATE
                      ,xl.xhibit_court_id   = v_xhibit_court_id
                      ,xl.xhibit_enrich_date = SYSDATE
                      ,xl.xhibit_etl_status = v_crl_upd_status
                WHERE  xl.crest_court_id   =  p_crest_court_id
                AND    xl.site_code = v_site_code;
                
                    DBMS_OUTPUT.PUT_LINE(' ');
                    DBMS_OUTPUT.PUT_LINE('CTX-2188:xhbstg_courtroom_location_dm - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

          EXCEPTION
              WHEN OTHERS THEN
              
                  v_err_message := SQLERRM;
                  
                  DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_court_site_with_crest for CREST_COURT : '||SUBSTR(v_err_message,1,120));
          
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'upd_xhb_court_site_with_crest- CTX-2188'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'E' -- Error
                               ,p_log_msg            =>  'upd_xhb_court_site_with_crest - Error processing'||' Error: '||SUBSTR(v_err_message,1,150)
                               ,p_err_row_count      => NULL 
                               ,p_success_row_count  => v_crl_no_of_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
                  -- Update xhbstg_courtroom_location_dm
                  -- tables with error status/messages
                  BEGIN
                      UPDATE xhbstg_courtroom_location_dm xl
                      SET    xl.xhibit_etl_date   = SYSDATE
                            ,xl.xhibit_court_id   = v_xhibit_court_id
                            ,xl.xhibit_enrich_date = SYSDATE
                            ,xl.xhibit_etl_status = 'X' -- Error
                            ,xl.xhibit_etl_err_message = v_err_message
                      WHERE  xl.crest_court_id   =  p_crest_court_id
                      AND    xl.site_code = v_site_code
                      ;
                  
                  END;

          END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing

      END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
      v_crl_no_of_rows := v_crl_no_of_rows + xhbstg_crl_tt.COUNT;

    END IF;
    
    EXIT WHEN courtroom_loc_cur%NOTFOUND;
    END LOOP;
    CLOSE courtroom_loc_cur;

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'upd_xhb_court_site_with_crest- CTX-2188'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_COURT_SITE : Processed '||v_crl_no_of_rows||' successfully!'
                 ,p_err_row_count      => NULL 
                 ,p_success_row_count  => v_crl_no_of_rows
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );
       
    UPDATE xhbstg_courtroom_location_dm xl
    SET    xl.xhibit_etl_date   = SYSDATE
          ,xl.xhibit_court_id   = v_xhibit_court_id
          ,xl.xhibit_enrich_date = SYSDATE
          ,xl.xhibit_etl_status = 'N' -- Not processed
    WHERE  xl.crest_court_id   =  p_crest_court_id
      AND  xl.xhibit_etl_status is NULL 
      AND  xl.xhibit_enrich_date  is NULL
      AND  xl.xhibit_etl_date  is NULL
      AND  xl.xhibit_court_id is NULL;
    
    v_crl_unprocessed_cnt := SQL%ROWCOUNT;
    
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_court_site_with_crest- CTX-2188'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_COURT_SITE : updating xhbstg_courtroom_location_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  '||p_crest_court_id
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_crl_unprocessed_cnt
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );   

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2188:XHB_COURT_SITE processed '||v_crl_no_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    DBMS_OUTPUT.PUT_LINE('XHB_COURT_SITE Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        
        --ROLLBACK; --CTX-3252
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_court_site_with_crest for CREST_COURT : '||SUBSTR(v_err_message,1,120));
        
        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_court_site_with_crest- CTX-2188'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_court_site_with_crest - Error processing'||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_crl_no_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
        
        COMMIT;-- Commit when running for real 
          
END upd_xhb_court_site_with_crest;


/**
  * NAME       : upd_xhb_court_room_with_crest
  * DESCRIPTION: CTX-2189. New CTX fields for XHB_COURT_ROOM - - Sec 4.3.2.24 req [4975.DM.029] 
  *              Update existing rows in XHIBIT XHB_COURT_ROOM table to add new data elements from CREST
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
  *  07/03/2019  - S Sethuraman - CTX-3823 UPD_XHB_COURT_ROOM_WITH_CREST - Link COURTROOM_LOCATION COURT_SITE
  * 14/03/2019 - S Sethuraman - CTX-3848 - Staging records U status update does not tally with XHIBIT migrated record count
**/
PROCEDURE upd_xhb_court_room_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE)
IS
   v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
   TYPE xhbstg_cr_dm_rec IS RECORD
    (
      crest_court_id   xhbstg_courtroom_location_dm.crest_court_id%TYPE
    , courtroom_no     xhbstg_courtroom_dm.courtroom_no%TYPE
    , new_security_ind xhbstg_courtroom_dm.security_ind%TYPE
    , old_security_ind xhibit.xhb_court_room.security_ind%TYPE
    , new_video_ind    xhbstg_courtroom_dm.video_ind%TYPE
    , old_video_ind    xhibit.xhb_court_room.video_ind%TYPE
    , court_site_code  xhibit.xhb_court_site.court_site_code%TYPE
    , court_id         xhibit.xhb_court.court_id%TYPE
    , court_room_id    xhibit.xhb_court_room.court_room_id%TYPE
    , ctl_id           xhbstg_courtroom_dm.ctl_id%TYPE -- CTX-3848
    );

    TYPE xhbstg_cr_dm_type IS TABLE OF xhbstg_cr_dm_rec;
    xhbstg_cr_tt  xhbstg_cr_dm_type;
     
    CURSOR courtroom_cur IS
    SELECT xcrdm.crest_court_id
    ,      xcrdm.courtroom_no
    ,      xcrdm.security_ind AS new_security_ind
    ,      xcr.security_ind AS old_security_ind
    ,      xcrdm.video_ind AS new_video_ind
    ,      xcr.video_ind AS old_video_ind
    ,      xcs.court_site_code
    ,      xcs.court_id
    ,      xcr.court_room_id
    ,      xcrdm.ctl_id -- CTX-3848
    FROM  xhbstg_courtroom_dm xcrdm
    ,     xhibit.xhb_court_site xcs
    ,     xhibit.xhb_court_room xcr 
    ,     xhbstg_courtroom_location_dm xcl  -- CTX-3823     
    WHERE xcrdm.crest_court_id = p_crest_court_id
    AND   xcrdm.crest_court_id = xcl.crest_court_id  -- CTX-3823  
    AND   xcs.court_id = v_xhibit_court_id
    AND   xcrdm.courtroom_no = xcr.crest_court_room_no
    AND   xcl.site_code = xcs.court_site_code     -- CTX-3823  
    AND   xcs.court_site_id   = xcr.court_site_id
    AND   xcl.ctl_id = xcrdm.ctl_id           -- CTX-3823                                          
    AND   NVL(xcs.obs_ind,'N')<> 'Y'
    AND   NVL(xcr.obs_ind,'N')<> 'Y'
    AND   (NVL(xcr.security_ind,'-') <> NVL(xcrdm.security_ind,'-')
     OR NVL(xcr.video_ind,'-') <> NVL(xcrdm.video_ind,'-'))
    AND   NVL(xcrdm.xhibit_etl_status,'N') NOT IN ('U','I')
    AND   xcrdm.xhibit_enrich_date IS NULL   
    ;  

    v_err_message          xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_court_room_no        xhibit.xhb_court_room.crest_court_room_no%TYPE;
    v_cr_upd_rows          NUMBER := 0;
    v_cr_upd_status        CHAR(1) := 'N';
    v_cr_no_of_rows        NUMBER := 0;
    v_cr_unprocessed_cnt   NUMBER := 0;
    v_total_row_count      NUMBER := 0;
    
    
BEGIN

      DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing row in XHB_COURT_ROOM for new columns with the required data from CREST');
     -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
      v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_room_with_crest- CTX-2189'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_COURT_ROOM - Starting process of updating existing rows for new columns with the required data from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing rows in XHB_COURT_ROOM for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    
      -- Here we are going to update the rest of the columns the rows in XHB_LEGAL_AID_ORDER table that have not yet been populated with CREST data
    OPEN courtroom_cur;
    LOOP
    FETCH courtroom_cur BULK COLLECT INTO xhbstg_cr_tt LIMIT g_bulk_limit;
   
    IF xhbstg_cr_tt IS NOT NULL AND xhbstg_cr_tt.COUNT > 0 THEN
   
     v_total_row_count := v_total_row_count + xhbstg_cr_tt.COUNT;  -- we want to count number of cases found as we collect them
       
        FOR i IN xhbstg_cr_tt.FIRST .. xhbstg_cr_tt.LAST 
        LOOP
        
        BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

            v_court_room_no := xhbstg_cr_tt(i).courtroom_no;
            
            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                           ,p_action_name        => 'upd_xhb_court_room_with_crest- CTX-2189'
                           ,p_run_time           =>  sysdate
                           ,p_log_msg_type       =>  'I' -- Information
                           ,p_log_msg            =>  'UPDATING XHB_COURT_ROOM - processing crest court id - '||xhbstg_cr_tt(i).crest_court_id||
                                                     ' setting security ind from '||xhbstg_cr_tt(i).old_security_ind||' to '||xhbstg_cr_tt(i).new_security_ind||
                                                     ' video ind from '||xhbstg_cr_tt(i).old_video_ind||' to '||xhbstg_cr_tt(i).new_video_ind
                           ,p_err_row_count      =>  NULL
                           ,p_success_row_count  =>  NULL
                           ,p_last_updated_by    => 'DATA MIGRATION'
                           ,p_created_by         => 'DATA MIGRATION'
                           );
               
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('UPDATING XHB_COURT_ROOM - processing crest court id - '||xhbstg_cr_tt(i).crest_court_id||', ctl_id : '||xhbstg_cr_tt(i).ctl_id||
                                 ' setting security ind from '||xhbstg_cr_tt(i).old_security_ind||' to '||xhbstg_cr_tt(i).new_security_ind||
                                 ' video ind from '||xhbstg_cr_tt(i).old_video_ind||' to '||xhbstg_cr_tt(i).new_video_ind
                                );               
    
            -- Update the rows in XHB_COURT_ROOM with data from CREST  
            UPDATE  XHIBIT.XHB_COURT_ROOM xcr 
            SET     xcr.security_ind = xhbstg_cr_tt(i).new_security_ind
                  , xcr.video_ind    = xhbstg_cr_tt(i).new_video_ind
                  , xcr.last_update_date = SYSDATE
                  , xcr.last_updated_by  = 'DATA MIGRATION'
            WHERE  xcr.court_room_id  =  xhbstg_cr_tt(i).court_room_id
            AND    NVL(xcr.obs_ind,'N')!='Y'
            ;
            
            v_cr_upd_rows := SQL%ROWCOUNT;
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('CTX-2189:XHB_COURT_ROOM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_cr_upd_rows);
    
            -- Update staging table for the rows processed
            IF v_cr_upd_rows > 0 THEN 
              v_cr_upd_status := 'U'; -- XHIBIT TABLE updated
            ELSIF v_cr_upd_rows = 0 THEN
              v_cr_upd_status := 'N'; -- No Action Performed on XHIBIT table
            END IF;  

            insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_room_with_crest- CTX-2189'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_COURT_ROOM - processing crest court id - '||xhbstg_cr_tt(i).crest_court_id||
                                                   ' setting security ind from '||xhbstg_cr_tt(i).old_security_ind||' to '||xhbstg_cr_tt(i).new_security_ind||
                                                   ' video ind from '||xhbstg_cr_tt(i).old_video_ind||' to '||xhbstg_cr_tt(i).new_video_ind||
                                                   ' with ETL_STATUS '||v_cr_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_cr_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

            -- Update xhbstg_courtroom_dm table for the rows processed
            UPDATE xhbstg_courtroom_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = v_cr_upd_status
            WHERE  xl.crest_court_id   =  p_crest_court_id
            AND    xl.ctl_id = xhbstg_cr_tt(i).ctl_id -- CTX-3848
            AND    xl.courtroom_no = v_court_room_no;
            
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('CTX-2189:xhbstg_courtroom_dm - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

            EXCEPTION
                WHEN OTHERS THEN
                
                    v_err_message := SQLERRM;
                    
                    DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_court_room_with_crest for CREST_COURT : '||SUBSTR(v_err_message,1,110));
                    
                    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                 ,p_action_name        => 'upd_xhb_court_room_with_crest- CTX-2189'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'upd_xhb_court_room_with_crest - Error processing'||' Error: '||SUBSTR(v_err_message,1,150)
                                 ,p_err_row_count      => NULL 
                                 ,p_success_row_count  => v_cr_no_of_rows
                                 ,p_last_updated_by    => 'DATA MIGRATION'
                                 ,p_created_by         => 'DATA MIGRATION'
                                 );
                    -- Update xhbstg_courtroom_dm
                    -- tables with error status/messages
                    BEGIN
                        UPDATE xhbstg_courtroom_dm xl
                        SET    xl.xhibit_etl_date   = SYSDATE
                              ,xl.xhibit_court_id   = v_xhibit_court_id
                              ,xl.xhibit_enrich_date = SYSDATE
                              ,xl.xhibit_etl_status = 'X' -- Error
                              ,xl.xhibit_etl_err_message = v_err_message
                        WHERE  xl.crest_court_id   =  p_crest_court_id
                        AND    xl.courtroom_no = v_court_room_no
                        ;
                        
                    END;

            END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing

        END LOOP;
        
        COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_cr_no_of_rows := v_cr_no_of_rows + xhbstg_cr_tt.COUNT;

    END IF;
    
     EXIT WHEN courtroom_cur%NOTFOUND;
    END LOOP;
    CLOSE courtroom_cur;

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_room_with_crest- CTX-2189'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_COURT_ROOM : Processed '||v_cr_no_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_cr_no_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               
            UPDATE xhbstg_courtroom_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = 'N' -- Not processed
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.xhibit_etl_status is NULL 
              AND  xl.xhibit_enrich_date  is NULL
              AND  xl.xhibit_etl_date  is NULL;
            --  AND  xl.xhibit_court_id is NULL;
           
            v_cr_unprocessed_cnt := SQL%ROWCOUNT;
           
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_court_room_with_crest- CTX-2189'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_COURT_ROOM : updating xhbstg_courtroom_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_cr_unprocessed_cnt
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2189:XHB_COURT_ROOM processed '||v_cr_no_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    DBMS_OUTPUT.PUT_LINE('XHB_COURT_ROOM Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        
        --ROLLBACK; --CTX-3252
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_court_room_with_crest for CREST_COURT : '||SUBSTR(v_err_message,1,110));
        
        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_court_room_with_crest- CTX-2189'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_court_room_with_crest - Error processing'||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_cr_no_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
        
        COMMIT;-- Commit when running for real 
            

END upd_xhb_court_room_with_crest;



/**
  * NAME       : upd_xhb_case_nad_with_crest
  * DESCRIPTION: CTX-2202. New CTX fields for XHB_CASE_NON_AVAIL_DAYS - - Sec 4.3.2.17 req [4975.DM.022] 
  *              Insert rows in XHIBIT XHB_CASE_NON_AVAIL_DAYS table to add new data elements from CREST
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
  * 26/02/2019  S Sethuraman : CTX-3776 - Map NULL REASON with '-'
**/
PROCEDURE upd_xhb_case_nad_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE)
IS
   v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
   TYPE xhbstg_nad_dm_rec IS RECORD
    (
      case_id    xhibit.xhb_case.case_id%TYPE
     ,start_date XHBSTG_NON_AVAIL_DATES_DM.start_date%TYPE
     ,end_date   XHBSTG_NON_AVAIL_DATES_DM.end_date%TYPE
     ,reason     XHBSTG_NON_AVAIL_DATES_DM.reason%TYPE
     ,case_no    XHBSTG_NON_AVAIL_DATES_DM.case_no%TYPE
     ,case_type  XHBSTG_NON_AVAIL_DATES_DM.case_type%TYPE
    );

    TYPE xhbstg_nad_dm_type IS TABLE OF xhbstg_nad_dm_rec;
    xhbstg_nad_tt  xhbstg_nad_dm_type;
     
    CURSOR nad_cur IS
    SELECT  xc.case_id
    ,       naddm.start_date
    ,       naddm.end_date
    ,       NVL(naddm.reason,'-') reason -- CTX-3776
    ,       naddm.case_no
    ,       naddm.case_type
    FROM XHBSTG_NON_AVAIL_DATES_DM naddm
    ,    xhibit.xhb_case xc
    WHERE naddm.case_no = xc.case_number
    AND   naddm.case_type = xc.case_type
    AND   xc.court_id = v_xhibit_court_id
    AND   trunc(naddm.end_date) > trunc(SYSDATE)
    AND   naddm.crest_court_id = p_crest_court_id
    AND   NVL(naddm.xhibit_etl_status,'N') NOT IN ('U','I') --check for updates and inserts
    AND   naddm.xhibit_enrich_date IS NULL
    /*Make sure we don't insert a record that already exists */
   AND NOT EXISTS (SELECT 'x'
                   FROM xhibit.XHB_CASE_NON_AVAIL_DAYS xnad
                   WHERE xnad.case_id = xc.case_id
                   AND trunc(xnad.start_date) = trunc(naddm.start_date)
                   AND trunc(xnad.end_date) = trunc(naddm.end_date)
                   AND xnad.reason = naddm.reason
                   AND nvl(xnad.obs_ind,'N') != 'Y'
                  );  

    v_err_message           xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_case_id               xhibit.xhb_case.case_id%TYPE;
    v_case_no               xhbstg_case_dm.case_no%TYPE;
    v_case_type             xhbstg_case_dm.case_type%TYPE;
    v_nad_upd_rows          NUMBER := 0;
    v_nad_upd_status        CHAR(1) := 'N';
    v_nad_no_of_rows        NUMBER := 0;
    v_nad_unprocessed_cnt   NUMBER := 0;
    v_total_row_count       NUMBER := 0;
    
    
BEGIN

      DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting data IN XHB_CASE_NON_AVAIL_DAYS with the required data from CREST');
     -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
      v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_nad_with_crest- CTX-2202'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_NON_AVAIL_DAYS - Starting process of inserting data IN XHB_CASE_NON_AVAIL_DAYS with the required data from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    
      -- Here we are going to update the rest of the columns the rows in XHB_LEGAL_AID_ORDER table that have not yet been populated with CREST data
    OPEN nad_cur;
    LOOP
    FETCH nad_cur BULK COLLECT INTO xhbstg_nad_tt LIMIT g_bulk_limit;
   
    IF xhbstg_nad_tt IS NOT NULL AND xhbstg_nad_tt.COUNT > 0 THEN
   
     v_total_row_count := v_total_row_count + xhbstg_nad_tt.COUNT;  -- we want to count number of cases found as we collect them
       
        FOR i IN xhbstg_nad_tt.FIRST .. xhbstg_nad_tt.LAST 
        LOOP
        
        BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing
        
          v_case_id := xhbstg_nad_tt(i).case_id;
          v_case_no := xhbstg_nad_tt(i).case_no;
          v_case_type := xhbstg_nad_tt(i).case_type;
          
                     insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_nad_with_crest- CTX-2202'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTING non avail days data into XHB_CASE_NON_AVAIL_DAYS with values : '||
                                                   ' case_id : '||v_case_id||
                                                   ' start_date : '||xhbstg_nad_tt(i).start_date||
                                                   ' end_date : '||xhbstg_nad_tt(i).start_date||
                                                   ' reason : '||xhbstg_nad_tt(i).reason
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
             DBMS_OUTPUT.PUT_LINE(' ');
             DBMS_OUTPUT.PUT_LINE('Insert into XHB_CASE_NON_AVAIL_DAYS - non avail days data into XHB_CASE_NON_AVAIL_DAYS with values : '||
                                                   ' case_id : '||v_case_id||
                                                   ' start_date : '||xhbstg_nad_tt(i).start_date||
                                                   ' end_date : '||xhbstg_nad_tt(i).start_date||
                                                   ' reason : '||xhbstg_nad_tt(i).reason
                                    );               
    
            INSERT INTO xhibit.XHB_CASE_NON_AVAIL_DAYS (NAD_ID
                                                      ,CASE_ID
                                                      ,START_DATE
                                                      ,END_DATE
                                                      ,REASON
                                                      ,OBS_IND
                                                      ,LAST_UPDATE_DATE
                                                      ,CREATION_DATE
                                                      ,LAST_UPDATED_BY
                                                      ,CREATED_BY
                                                      ,VERSION)
            VALUES (XHIBIT.XHB_CASE_NON_AVAIL_DAYS_SEQ.NEXTVAL
                   ,v_case_id
                   ,xhbstg_nad_tt(i).start_date
                   ,xhbstg_nad_tt(i).end_date
                   ,xhbstg_nad_tt(i).reason
                   ,'N'
                   ,sysdate
                   ,sysdate
                   ,'DATA MIGRATION'
                   ,'DATA MIGRATION'
                   ,1
                    );

          v_nad_upd_rows := SQL%ROWCOUNT;
          DBMS_OUTPUT.PUT_LINE(' ');
          DBMS_OUTPUT.PUT_LINE('CTX-2202:XHB_CASE_NON_AVAIL_DAYS - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_nad_upd_rows);
    
          -- Update staging table for the rows processed
           IF v_nad_upd_rows > 0 THEN 
              v_nad_upd_status := 'I'; -- INSERT
           ELSIF v_nad_upd_rows = 0 THEN
              v_nad_upd_status := 'N'; -- No Action Performed on XHIBIT table
           END IF;  
  
          insert_dm_log (p_crest_court_id     => p_crest_court_id 
                       ,p_action_name        => 'upd_xhb_case_nad_with_crest- CTX-2202'
                       ,p_run_time           =>  sysdate
                       ,p_log_msg_type       =>  'I' -- Information
                       ,p_log_msg            =>  'INSERTING non avail days data into XHB_CASE_NON_AVAIL_DAYS with values : '||
                                                         ' case_id : '||v_case_id||
                                                         ' start_date : '||xhbstg_nad_tt(i).start_date||
                                                         ' end_date : '||xhbstg_nad_tt(i).start_date||
                                                         ' reason : '||xhbstg_nad_tt(i).reason||
                                                         ' with ETL_STATUS '||v_nad_upd_status
                       ,p_err_row_count      =>  NULL
                       ,p_success_row_count  => v_nad_upd_rows
                       ,p_last_updated_by    => 'DATA MIGRATION'
                       ,p_created_by         => 'DATA MIGRATION'
                       );

           -- Update xhbstg_courtroom_dm table for the rows processed
            UPDATE XHBSTG_NON_AVAIL_DATES_DM xnad
            SET    xnad.xhibit_etl_date   = SYSDATE
                  ,xnad.xhibit_court_id   = v_xhibit_court_id
                  ,xnad.xhibit_enrich_date = SYSDATE
                  ,xnad.xhibit_etl_status = v_nad_upd_status
            WHERE  xnad.crest_court_id   =  p_crest_court_id
            AND    xnad.case_no = v_case_no
            AND    xnad.case_type = v_case_type
            ;
          
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2202:XHBSTG_NON_AVAIL_DATES_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

        EXCEPTION
            WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_court_room_with_crest for CREST_COURT : '||SUBSTR(v_err_message,1,120));
        
                   insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                 ,p_action_name        => 'upd_xhb_case_nad_with_crest- CTX-2202'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'upd_xhb_case_nad_with_crest - Error processing'||' Error: '||SUBSTR(v_err_message,1,150)
                                 ,p_err_row_count      => NULL 
                                 ,p_success_row_count  => v_nad_no_of_rows
                                 ,p_last_updated_by    => 'DATA MIGRATION'
                                 ,p_created_by         => 'DATA MIGRATION'
                                 );
                 -- Update staging table
                 -- tables with error status/messages
                 BEGIN
                    UPDATE XHBSTG_NON_AVAIL_DATES_DM xnad
                    SET    xnad.xhibit_etl_date   = SYSDATE
                          ,xnad.xhibit_court_id   = v_xhibit_court_id
                          ,xnad.xhibit_enrich_date = SYSDATE
                          ,xnad.xhibit_etl_status = 'X' -- Error
                          ,xnad.xhibit_etl_err_message = v_err_message
                    WHERE  xnad.crest_court_id   =  p_crest_court_id
                      AND  xnad.case_no = v_case_no
                      AND  xnad.case_type = v_case_type
                    ;
                    
                  END;
   
        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
   
      END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_nad_no_of_rows := v_nad_no_of_rows + xhbstg_nad_tt.COUNT;

    END IF;
    
     EXIT WHEN nad_cur%NOTFOUND;
    END LOOP;
    CLOSE nad_cur;

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_nad_with_crest- CTX-2202'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_NON_AVAIL_DAYS : Processed '||v_nad_no_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_nad_no_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               
            UPDATE XHBSTG_NON_AVAIL_DATES_DM xnad
            SET    xnad.xhibit_etl_date   = SYSDATE
                  ,xnad.xhibit_court_id   = v_xhibit_court_id
                  ,xnad.xhibit_enrich_date = SYSDATE
                  ,xnad.xhibit_etl_status = 'N' -- Not processed
            WHERE  xnad.crest_court_id   =  p_crest_court_id
              AND  xnad.xhibit_etl_status is NULL 
              AND  xnad.xhibit_enrich_date  is NULL
              AND  xnad.xhibit_etl_date  is NULL
              AND  xnad.xhibit_court_id is NULL;
           
            v_nad_unprocessed_cnt := SQL%ROWCOUNT;
           
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_case_nad_with_crest- CTX-2202'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_CASE_NON_AVAIL_DAYS : updating XHBSTG_NON_AVAIL_DATES_DM with ETL_STATUS = N  where rows NOT processed for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_nad_unprocessed_cnt
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );   

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2202:XHB_CASE_NON_AVAIL_DAYS processed '||v_nad_no_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    DBMS_OUTPUT.PUT_LINE('XHB_CASE_NON_AVAIL_DAYS Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; --CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_court_room_with_crest for CREST_COURT : '||SUBSTR(v_err_message,1,120));

       insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_case_nad_with_crest- CTX-2202'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_case_nad_with_crest - Error processing'||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_nad_no_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );

        COMMIT;-- Commit when running for real 
              
END upd_xhb_case_nad_with_crest;

/**
  * NAME       : upd_xhb_offence_with_crest
  * DESCRIPTION: CTX-2468. New CTX fields for XHB_OFFENCE - - Sec 4.3.2.32 req [4975.DM.035] 
  *              Update existing rows in XHIBIT XHB_OFFENCE table to add new data elements from CREST
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE upd_xhb_offence_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE)
IS
   v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
   TYPE xhbstg_off_dm_rec IS RECORD
    (appeal_type      xhbstg_charge_dm.appeal_type%TYPE
    ,case_id          xhibit.xhb_case.case_id%TYPE
    ,xhb_offence_id   xhibit.xhb_offence.offence_id%TYPE
    ,cr_offence_id    xhibit.xhb_offence.crest_offence_id%TYPE
    ,charge_id        xhbstg_charge_dm.chg_id%TYPE
    );

    TYPE xhbstg_off_dm_type IS TABLE OF xhbstg_off_dm_rec;
    xhbstg_off_tt  xhbstg_off_dm_type;
     
    CURSOR offence_cur IS
    SELECT stg_dm.appeal_type
    ,      xc.case_id
    ,      xo.offence_id
    ,      xo.crest_offence_id
    ,      stg_dm.chg_id
    FROM  xhbstg_charge_dm stg_dm
    ,     xhibit.xhb_offence xo
    ,     xhibit.xhb_charge xchg
    ,     xhibit.xhb_case xc
    WHERE stg_dm.chg_id = xo.crest_offence_id
    AND   xo.charge_id = xchg.charge_id
    AND   xchg.case_id = xc.case_id
    AND   xc.court_id = v_xhibit_court_id
    AND   stg_dm.crest_court_id = p_crest_court_id
    AND   xc.case_type = 'A'
    AND   NVL(xo.obs_ind,'N')<> 'Y'
    AND   NVL(xchg.obs_ind,'N')<> 'Y'
    AND   NVL(stg_dm.xhibit_etl_status,'N') NOT IN ('U','I')
    AND   stg_dm.xhibit_enrich_date IS NULL
    ;

    v_err_message          xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_off_upd_rows          NUMBER := 0;
    v_off_upd_status        CHAR(1) := 'N';
    v_off_no_of_rows        NUMBER := 0;
    v_off_unprocessed_cnt   NUMBER := 0;
    v_crest_offence_id      xhibit.xhb_offence.offence_id%TYPE;
    v_offence_id            xhibit.xhb_offence.offence_id%TYPE;
    v_total_row_count       NUMBER := 0;
    
BEGIN

      DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing row in XHB_OFFENCE for new columns with the required data from CREST');
     -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
      v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_offence_with_crest- CTX-2468'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_OFFENCE - Starting process of updating existing rows for new columns with the required data from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating existing rows in XHB_OFFENCE for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    
      -- Here we are going to update the rest of the columns the rows in xhibit table that have not yet been populated with CREST data
    OPEN offence_cur;
    LOOP
    FETCH offence_cur BULK COLLECT INTO xhbstg_off_tt LIMIT g_bulk_limit;
   
    IF xhbstg_off_tt IS NOT NULL AND xhbstg_off_tt.COUNT > 0 THEN
   
        v_total_row_count := v_total_row_count + xhbstg_off_tt.COUNT;  -- we want to count number of cases found as we collect them
        
        FOR i IN xhbstg_off_tt.FIRST .. xhbstg_off_tt.LAST 
          LOOP
          
            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing
          
                v_crest_offence_id := xhbstg_off_tt(i).cr_offence_id;
                v_offence_id       := xhbstg_off_tt(i).xhb_offence_id;
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                   ,p_action_name        => 'upd_xhb_offence_with_crest- CTX-2468'
                   ,p_run_time           =>  sysdate
                   ,p_log_msg_type       =>  'I' -- Information
                   ,p_log_msg            =>  'UPDATING XHB_OFFENCE - processing crest court id - '||p_crest_court_id||
                                             ' setting appeal_type to '||xhbstg_off_tt(i).appeal_type||
                                             ' for offence id '||xhbstg_off_tt(i).xhb_offence_id||
                                             ' for charge id '||xhbstg_off_tt(i).charge_id||
                                             ' on case id '||xhbstg_off_tt(i).case_id
                   ,p_err_row_count      =>  NULL
                   ,p_success_row_count  =>  NULL
                   ,p_last_updated_by    => 'DATA MIGRATION'
                   ,p_created_by         => 'DATA MIGRATION'
                   );
                   
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('UPDATING XHB_OFFENCE processing crest court id - '||p_crest_court_id||
                                             ' setting appeal_type to '||xhbstg_off_tt(i).appeal_type||
                                             ' for offence id '||xhbstg_off_tt(i).xhb_offence_id||
                                             ' for charge id '||xhbstg_off_tt(i).charge_id||
                                             ' on case id '||xhbstg_off_tt(i).case_id
                              );               
                
                -- Update the rows in XHB_COURT_ROOM with data from CREST  
                UPDATE  XHIBIT.XHB_OFFENCE xo 
                SET     appeal_type = xhbstg_off_tt(i).appeal_type
                      , xo.last_update_date = SYSDATE
                      , xo.last_updated_by  = 'DATA MIGRATION'
                WHERE  xo.offence_id = xhbstg_off_tt(i).xhb_offence_id
                AND    NVL(xo.obs_ind,'N')!='Y'
                ;
                
                v_off_upd_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2468:XHB_OFFENCE - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||v_off_upd_rows);
                
                -- Update staging table for the rows processed
                IF v_off_upd_rows > 0 THEN 
                    v_off_upd_status := 'U'; -- XHIBIT TABLE updated
                ELSIF v_off_upd_rows = 0 THEN
                    v_off_upd_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                ,p_action_name        => 'upd_xhb_offence_with_crest- CTX-2468'
                ,p_run_time           =>  sysdate
                ,p_log_msg_type       =>  'I' -- Information
                ,p_log_msg            =>  'UPDATING XHB_OFFENCE processing crest court id - '||p_crest_court_id||
                                     ' setting appeal_type to '||xhbstg_off_tt(i).appeal_type||
                                     ' for offence id '||xhbstg_off_tt(i).xhb_offence_id||
                                     ' for charge id '||xhbstg_off_tt(i).charge_id||
                                     ' on case id '||xhbstg_off_tt(i).case_id||
                                     ' with ETL_STATUS '||v_off_upd_status
                ,p_err_row_count      =>  NULL
                ,p_success_row_count  => v_off_upd_rows
                ,p_last_updated_by    => 'DATA MIGRATION'
                ,p_created_by         => 'DATA MIGRATION'
                );
                
                -- Update xhbstg_courtroom_dm table for the rows processed
                UPDATE xhbstg_charge_dm xl
                SET    xl.xhibit_etl_date   = SYSDATE
                      ,xl.xhibit_court_id   = v_xhibit_court_id
                      ,xl.xhibit_enrich_date = SYSDATE
                      ,xl.xhibit_etl_status = v_off_upd_status
                WHERE  xl.crest_court_id   =  p_crest_court_id
                AND    xl.chg_id  = v_crest_offence_id;
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2468:xhbstg_charge_dm - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
        
            EXCEPTION
                WHEN OTHERS THEN
                
                    v_err_message := SQLERRM;
        
                    DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_court_rupd_xhb_offence_with_crestoom_with_crest for CREST_COURT : '||SUBSTR(v_err_message,1,100));
                    
                    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                 ,p_action_name        => 'upd_xhb_offence_with_crest- CTX-2468'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'upd_xhb_offence_with_crest - Error processing'||' Error: '||SUBSTR(v_err_message,1,150)
                    
                                 ,p_err_row_count      => NULL 
                                 ,p_success_row_count  => v_off_no_of_rows
                                 ,p_last_updated_by    => 'DATA MIGRATION'
                                 ,p_created_by         => 'DATA MIGRATION'
                                 );
                                 
                    -- Update xhbstg_courtroom_dm
                    -- tables with error status/messages
                    BEGIN
                    UPDATE xhbstg_charge_dm xl
                    SET    xl.xhibit_etl_date   = SYSDATE
                          ,xl.xhibit_court_id   = v_xhibit_court_id
                          ,xl.xhibit_enrich_date = SYSDATE
                          ,xl.xhibit_etl_status = 'X' -- Error
                          ,xl.xhibit_etl_err_message = v_err_message
                    WHERE  xl.crest_court_id   =  p_crest_court_id
                    AND    xl.chg_id = v_crest_offence_id
                    ;
                    END;
                
            END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
        END LOOP;
        
        COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
          v_off_no_of_rows := v_off_no_of_rows + xhbstg_off_tt.COUNT;

    END IF;
    
    EXIT WHEN offence_cur%NOTFOUND;
    
    END LOOP;
    CLOSE offence_cur;

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'upd_xhb_offence_with_crest- CTX-2468'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_OFFENCE : Processed '||v_off_no_of_rows||' successfully!'
                 ,p_err_row_count      => NULL 
                 ,p_success_row_count  => v_off_no_of_rows
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );
       
    UPDATE xhbstg_charge_dm xl
    SET    xl.xhibit_etl_date   = SYSDATE
          ,xl.xhibit_court_id   = v_xhibit_court_id
          ,xl.xhibit_enrich_date = SYSDATE
          ,xl.xhibit_etl_status = 'N' -- Not processed
    WHERE  xl.crest_court_id   =  p_crest_court_id
      AND  xl.xhibit_etl_status is NULL 
      AND  xl.xhibit_enrich_date  is NULL
      AND  xl.xhibit_etl_date  is NULL
      ;
    
    v_off_unprocessed_cnt := SQL%ROWCOUNT;
    
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_offence_with_crest- CTX-2468'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_OFFENCE : updating xhbstg_charge_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  '||p_crest_court_id
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_off_unprocessed_cnt
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );   
    
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2468:XHB_OFFENCE processed '||v_off_no_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    DBMS_OUTPUT.PUT_LINE('XHB_OFFENCE Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
    --ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
    
        v_err_message := SQLERRM;
        
        --ROLLBACK; --CTX-3252
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_court_rupd_xhb_offence_with_crestoom_with_crest for CREST_COURT : '||SUBSTR(v_err_message,1,100));

        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_offence_with_crest- CTX-2468'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_offence_with_crest - Error processing'||' Error: '||SUBSTR(v_err_message,1,150)
        
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_off_no_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );

        COMMIT;-- Commit when running for real 

          
END upd_xhb_offence_with_crest;


/**
  * NAME       : upd_xhb_lists_with_crest
  * DESCRIPTION: CTX-2402. New CTX fields for XHB_LISTS - - Sec 4.3.2.28 req [4975.DM.031] 
  *              Insert data into  XHIBIT XHB_LISTS table to add new data elements from CREST
  * PARAMETERS : p_court_id         - The CREST Court ID being processed will be passed to this procedure
  * S Sethuraman 05/12/2018 - CTX-3252 - Exception Handling block implemented within loop to continue processing byjust logging errors for failures
**/
PROCEDURE upd_xhb_lists_with_crest(p_crest_court_id IN xhbstg_courtroom_location_dm.crest_court_id%TYPE)
IS
   v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
   TYPE xhbstg_list_dm_rec IS RECORD
    ( LIST_TYPE_ID         xhibit.xhb_ref_listing_data.ref_listing_data_id%TYPE
    , LIST_PARENT_ID       xhibit.xhb_list.list_parent_id%TYPE 
    , COURT_ID             xhibit.xhb_court.court_id%TYPE
    , LIST_START_DATE      xhibit.xhb_list.list_start_date%TYPE
    , LIST_END_DATE        xhibit.xhb_list.list_end_date%TYPE
    , date_published       xhibit.xhb_list.publish_date%TYPE
    , PUBLISH_STATUS       xhibit.xhb_list.publish_status%TYPE
    , PUBLISH_ERROR_REASON xhibit.xhb_list.publish_error_reason%TYPE
    , DRAFT_OR_FINAL       xhibit.xhb_list.draft_or_final%TYPE
    , LIST_NUMBER          xhibit.xhb_list.list_number%TYPE
    , OBS_IND              xhibit.xhb_list.obs_ind%TYPE
    , CREST_LIST_ID        xhbstg_lists_dm.lst_id%TYPE
    );

    TYPE xhbstg_list_dm_type IS TABLE OF xhbstg_list_dm_rec;
    xhbstg_list_tt  xhbstg_list_dm_type;
     
    CURSOR list_cur IS
    SELECT xrld.ref_listing_data_id AS LIST_TYPE_ID
    ,      NULL AS LIST_PARENT_ID
    ,      xc.court_id
    ,      xlstg.start_date AS LIST_START_DATE
    ,      xlstg.end_date AS LIST_END_DATE
    ,      xlstg.date_published
    ,      CASE
            WHEN xlstg.date_published IS NOT NULL THEN 'SUCCESS'
            WHEN xlstg.date_published IS NULL THEN NULL
           END PUBLISH_STATUS
    ,      NULL AS PUBLISH_ERROR_REASON
    --,      NVL(xlstg.list_status,'D') DRAFT_OR_FINAL
    ,      SUBSTR(NVL(xlstg.list_status,'D'),1,1) DRAFT_OR_FINAL
    ,      NVL(xlstg.edition_no,1) AS LIST_NUMBER --Brian confirmed to se to 1 if null.  XHB_LIST has a not null constraint on this field
    ,     'N' AS OBS_IND
    ,     xlstg.lst_id
    FROM xhbstg_lists_dm xlstg
    ,    xhibit.xhb_ref_listing_data xrld
    ,    xhibit.xhb_court xc
    WHERE xlstg.crest_court_id = p_crest_court_id
    AND   xrld.ref_data_type = 'LIST_TYPE'
    AND   DECODE(xlstg.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
    AND   NVL(xrld.obs_ind,'N') <> 'Y'
    AND   NVL(xc.obs_ind,'N') <> 'Y'
    AND   xlstg.crest_court_id = xc.crest_court_id
    AND   NVL(xlstg.xhibit_etl_status,'N') not in ('I', 'U') 
    AND   xlstg.xhibit_enrich_date is NULL
    /*Make sure we dont insert a row that already exists in xhb_list*/
    AND NOT EXISTS (SELECT 'x'
                    FROM xhibit.xhb_list xl
                    WHERE xl.court_id = xc.court_id
                    AND xl.draft_or_final = xrld.ref_data_value
                    AND xl.list_number = NVL(xlstg.edition_no,1)
                    AND trunc(xl.list_start_date) = xlstg.start_date
                    AND trunc(xl.list_end_date) = xlstg.end_date
                    AND xl.draft_or_final =  SUBSTR(NVL(list_status,'D'),1,1)
                    AND xl.publish_date = xlstg.date_published
                    AND UPPER(xl.publish_status) = (CASE
                                                    WHEN xlstg.date_published IS NOT NULL THEN 'SUCCESS'
                                                    WHEN xlstg.date_published IS NULL THEN NULL
                                                    END)
                    AND NVL(xl.obs_ind,'N') <> 'Y'                           
                    );

    v_count_number_of_rows   NUMBER := 0;
    v_list_ins_rows          NUMBER := 0;
    v_list_ins_status        CHAR(1) := 'N';
    v_list_np_rows           NUMBER := 0;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_list_id                xhibit.xhb_list.list_id%TYPE;
    v_crest_list_id          xhbstg_lists_dm.lst_id%TYPE;
    v_no_of_rows             NUMBER := 0;
    v_total_row_count        NUMBER := 0;
    
BEGIN

     -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
      v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_lists_with_crest- CTX-2402'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_LIST - Starting inserting rows into XHB_LIST with data from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting inserting rows into XHB_LIST with data from CREST');
    
      -- Here we are going to update the rest of the columns the rows in xhibit table that have not yet been populated with CREST data
    OPEN list_cur;
    LOOP
    FETCH list_cur BULK COLLECT INTO xhbstg_list_tt LIMIT g_bulk_limit;
   
    IF xhbstg_list_tt IS NOT NULL AND xhbstg_list_tt.COUNT > 0 THEN
   
     v_total_row_count := v_total_row_count + xhbstg_list_tt.COUNT;  -- we want to count number of cases found as we collect them
       
        FOR i IN xhbstg_list_tt.FIRST .. xhbstg_list_tt.LAST 
        LOOP
          
         BEGIN -- CTX-3252
          SELECT xhibit.xhb_list_seq.NEXTVAL INTO v_list_id FROM dual;
          v_crest_list_id := xhbstg_list_tt(i).crest_list_id;
          
                     insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_lists_with_crest- CTX-2402'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTING XHB_LIST - processing crest court id - '||p_crest_court_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         
            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('INSERTING XHB_LIST - processing crest court id - '||p_crest_court_id);               
          
            DBMS_OUTPUT.PUT_LINE('Court ID : '||xhbstg_list_tt(i).COURT_ID||', list IS : '||v_list_id||' , v_xhibit_court_id '||v_xhibit_court_id);         
    
            -- Insert the rows in XHB_LIST with data from CREST 
               INSERT INTO xhibit.xhb_list (list_id
                                            ,list_type_id
                                            ,list_parent_id
                                            ,court_id
                                            ,draft_or_final
                                            ,list_number
                                            ,list_start_date
                                            ,list_end_date
                                            ,publish_date
                                            ,publish_status
                                            ,publish_error_reason
                                            ,obs_ind
                                            ,created_by
                                            ,last_updated_by
                                            ,creation_date
                                            ,last_update_date
                                            ,version)
                VALUES (v_list_id
                      , xhbstg_list_tt(i).LIST_TYPE_ID         
                      , xhbstg_list_tt(i).LIST_PARENT_ID
                      , xhbstg_list_tt(i).COURT_ID
                      , xhbstg_list_tt(i).DRAFT_OR_FINAL
                      , xhbstg_list_tt(i).LIST_NUMBER
                      , xhbstg_list_tt(i).LIST_START_DATE      
                      , xhbstg_list_tt(i).LIST_END_DATE        
                      , xhbstg_list_tt(i).date_published       
                      , xhbstg_list_tt(i).PUBLISH_STATUS       
                      , xhbstg_list_tt(i).PUBLISH_ERROR_REASON 
                      , xhbstg_list_tt(i).OBS_IND
                      ,'DATA MIGRATION'
                      ,'DATA MIGRATION'
                      ,SYSDATE
                      ,SYSDATE
                      ,1
                      );
                
    v_list_ins_rows := SQL%ROWCOUNT;
    
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2402:XHB_LIST - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_list_ins_rows);
    
        -- Update staging table for the rows processed
         IF v_list_ins_rows > 0 THEN 
            v_list_ins_status := 'I'; -- XHIBIT TABLE inserted
         ELSIF v_list_ins_rows = 0 THEN
            v_list_ins_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;   

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
                 ,p_action_name        => 'upd_xhb_lists_with_crest- CTX-2402'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'INSERTING XHB_LIST - processing crest court id - '||p_crest_court_id||' adding list id'||v_list_id||' with ETL_STATUS '||v_list_ins_status
                 ,p_err_row_count      =>  NULL
                 ,p_success_row_count  => v_list_ins_rows
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );

           -- Update xhbstg_courtroom_dm table for the rows processed
            UPDATE xhbstg_lists_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = v_list_ins_status
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.lst_id = xhbstg_list_tt(i).crest_list_id;
            
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2402:xhbstg_list_dm - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
 EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_lists_with_crest for CREST_COURT : '||p_crest_court_id||'-'||SUBSTR(v_err_message,1,110));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_lists_with_crest - CTX-2402'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_lists_with_crest - Error processing crest list : '||v_crest_list_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_COURTROOM_USAGE_DM
         -- tables with error status/messages

            UPDATE xhbstg_lists_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = 'X' -- Error
                  ,xl.xhibit_etl_err_message = v_err_message
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.lst_id = v_crest_list_id;

          END; -- CTX-3252  
      END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_no_of_rows := v_no_of_rows + xhbstg_list_tt.COUNT;

    END IF;
    
     EXIT WHEN list_cur%NOTFOUND;
    END LOOP;
    CLOSE list_cur;

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        =>  'upd_xhb_lists_with_crest- CTX-2402'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_LIST:  Processed '||v_no_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_no_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
           
               
            UPDATE xhbstg_lists_dm xl
            SET    xl.xhibit_etl_date   = SYSDATE
                  ,xl.xhibit_court_id   = v_xhibit_court_id
                  ,xl.xhibit_enrich_date = SYSDATE
                  ,xl.xhibit_etl_status = 'N' -- Not processed
            WHERE  xl.crest_court_id   =  p_crest_court_id
              AND  xl.xhibit_etl_status is NULL 
              AND  xl.xhibit_enrich_date  is NULL
              AND  xl.xhibit_etl_date  is NULL
              ;
           
             v_list_np_rows := SQL%ROWCOUNT;
           
             insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_lists_with_crest- CTX-2402'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_LIST : updating xhbstg_list_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_list_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         ); 
             


    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2402:XHB_LIST processed '||v_no_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE('XHB_LIST Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2402:xhbstg_lists_dm processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_list_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);

    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_lists_with_crest for CREST_COURT : '||p_crest_court_id||'-'||SUBSTR(v_err_message,1,110));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_lists_with_crest - CTX-2402'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_lists_with_crest - Error processing crest list : '||v_crest_list_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

            COMMIT;

          
END upd_xhb_lists_with_crest;


/**
  * NAME       : upd_xhb_sit_on_list_crest
  * DESCRIPTION: CTX-2403 New CTX fields for XHB_SITTING_ON_LIST - Sec 4.3.2.33 req [4975.DM.032]
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * 
  *  CTX-3273 - SIT_ON_LIST record whould be selected even if REF_JUDGE_ID is NULL or doesn;t match XHIBIT xhb_ref_judge
  * S Sethuraman 05/12/2018 - CTX-3252 - Exception Handling block implemented within loop to continue processing byjust logging errors for failures
  * S Sethuraman 23/01/2019 - CTX-3577 - UPD_XHB_SIT_ON_LIST_CREST - DEFAULT TO COURT_SITE_CODE = 'X' if COURT_SITE_CODE NOT FOUND IN XHIBIT
  * S Sethuraman 31/01/2019 - CTX-3643 - UPD_XHB_SIT_ON_LIST_CREST - update XHBSTG_COURTTOOM_DAY_DM for default court_sites as well
  * S Sethuraman 14/03/2019 - CTX-3855 -  parse_xhbstg_courtroom_day and upd_xhb_sitting_on_list procedures updated to address SITTING_ON_LIST / CASE_ON_LIST Duplicates issue -
  * S Sethuraman 30/04/2019 - CTX-4163 - Use DUMMY COURT SITE X COURTROOM 1 where crest court site code found BUT Crest COURTROOM not FOUND
  * S Sethuraman 15/05/2019 - CTX-     - update statement handled for NULL values
**/
PROCEDURE upd_xhb_sit_on_list_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;

 TYPE xhb_sol_rec IS RECORD
    ( SITTING_NUMBER                  xhbstg_courtroom_day_dm.jud_seq_no%TYPE
    , list_id                         xhibit.xhb_sitting_on_list.list_id%TYPE
    , TIME_MARKING_ID                 xhibit.xhb_sitting_on_list.TIME_MARKING_ID%TYPE
    , TIME_LISTED                     xhibit.xhb_sitting_on_list.TIME_LISTED%TYPE
    , JUDGE_REF_ID                    xhibit.xhb_sitting_on_list.JUDGE_REF_ID%TYPE
    , JP1                             xhibit.xhb_sitting_on_list.jp1%TYPE
    , JP2                             xhibit.xhb_sitting_on_list.jp2%TYPE
    , JP3                             xhibit.xhb_sitting_on_list.jp3%TYPE
    , JP4                             xhibit.xhb_sitting_on_list.jp4%TYPE
    , list_note_pre_defined_id        xhibit.xhb_sitting_on_list.list_note_pre_defined_id%TYPE
    , LIST_NOTE_TEXT                  xhibit.xhb_sitting_on_list.LIST_NOTE_TEXT%TYPE
    , PRE_DEF_NOTE_CLASSIFICATION_ID  xhibit.xhb_sitting_on_list.PRE_DEF_NOTE_CLASSIFICATION_ID%TYPE
    , FREE_TEXT_NOTE_CLASS_ID         xhibit.xhb_sitting_on_list.FREE_TEXT_NOTE_CLASS_ID%TYPE
    , OBS_IND                         xhibit.xhb_sitting_on_list.obs_ind%TYPE
    , court_room_id                   xhibit.xhb_sitting_on_list.court_room_id%TYPE
    , court_site_id                   xhibit.xhb_sitting_on_list.court_site_id%TYPE
    , courtroom_no                    xhbstg_courtroom_day_dm.courtroom_no%TYPE
    , ctd_id                          xhbstg_courtroom_day_dm.ctd_id%TYPE
    , list_date                       xhbstg_courtroom_day_dm.list_date%TYPE
    , site_code                       xhbstg_courtroom_day_dm.site_code%TYPE
    , crest_court_id                  xhibit.xhb_court.crest_court_id%TYPE
    -- CTX-3643 
    , stg_court_site_code             xhbstg_courtroom_day_dm.site_code%TYPE
    , stg_court_room_no              xhbstg_courtroom_day_dm.courtroom_no%TYPE
    -- CTX-3643
    );

    TYPE xhb_sol_type IS TABLE OF xhb_sol_rec;
    xhb_sol_tt  xhb_sol_type;

    CURSOR cur_crest_sol_details IS
    SELECT stgcd.jud_seq_no AS SITTING_NUMBER
    ,      xl.list_id
    ,      CASE
            WHEN stgcd.start_time IS NOT NULL THEN (SELECT ref_system_code_id
                                                    FROM xhibit.xhb_ref_system_code
                                                    WHERE TRIM(code_type) = 'EXHIBIT_TIME_FORMAT'
                                                    AND TRIM(de_code) = 'SITTING AT'
                                                    AND court_id = v_xhibit_court_id
                                                    AND nvl(obs_ind,'N') <> 'Y'
                                                    )
            ELSE NULL
            END TIME_MARKING_ID
    ,     to_date(to_char(stgcd.list_date,'DD-MON-YYYY')||' '||stgcd.start_time,'DD-MON-YYYY HH:MI AM') TIME_LISTED
    ,     (SELECT xrj.ref_judge_id    -- removed unnecessary NVL 07/03/2019 - performance issue
            FROM xhibit.xhb_ref_judge xrj
            where xrj.court_id = xc.court_id
            AND xrj.crest_judge_id = stgcd.jud_id
           ) as  JUDGE_REF_ID
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
    ,     xcr.crest_court_room_no -- CTX-3577 SELECT XHIBIT COURT_ROOM_NO
    ,     stgcd.ctd_id
    ,     stgcd.list_date
    ,     xcs.court_site_code -- CTX-3577 select XHIBIT COURT_SITE_CODE
    ,     xc.crest_court_id
    -- CTX-3643    
    ,     stgcd.site_code  as stg_court_site_code
    ,     stgcd.courtroom_no   as stg_court_room_no 
    -- CTX-3643
    FROM xhbstg_courtroom_day_dm stgcd
    ,    xhibit.xhb_list xl
    ,    xhibit.xhb_ref_listing_data xrld
--    ,    xhibit.xhb_ref_judge xrj   -- CTX-3273 - remove reference and add in sub query
    ,    xhibit.xhb_court xc
    ,    xhibit.xhb_court_site xcs
    ,    xhibit.xhb_court_room xcr
    WHERE trunc(stgcd.list_date) BETWEEN  trunc(xl.list_start_date) AND trunc(xl.list_end_date)
    AND   xrld.ref_data_type = 'LIST_TYPE'
    AND   DECODE(stgcd.list_type,'D','Daily','W','Warned','F','Firm') = xrld.ref_data_value
    AND   xl.list_type_id = xrld.ref_listing_data_id
    AND   stgcd.crest_court_id = xc.crest_court_id
    AND   stgcd.crest_court_id = p_crest_court_id
    AND   xl.court_id = xc.court_id
   -- AND   nvl(xl.obs_ind,'N') <> 'Y' -- duplicate check removed
 --   AND   stgcd.jud_id = xrj.crest_judge_id  -- CTX-3273  - remove reference and add in sub query
 --   AND   nvl(xrj.obs_ind,'N') <> 'Y'  -- CTX-3273 -- COndition NOT valid for old obsolete judgesin CREST
    AND   xc.court_id = xcs.court_id
    AND   xcs.court_site_id = xcr.court_site_id
--  CTX-3577 If court_site_code NOT found in XHIBIT, USE 'X' as court_site_code CTX-3577
-- CTX-4163 Further if COURT_SITE_CODE found but COURT_ROOM_NO NOT found, USE 'X' dummy site code
    AND   xcs.court_site_code =
          NVL((SELECT t.court_site_code from xhibit.xhb_court_site t, xhibit.xhb_court_room tc
            where t.court_site_id = tc.court_site_id and t.court_id = xc.court_id and 
                  t.court_site_code = stgcd.site_code and tc.crest_court_room_no = stgcd.courtroom_no),'X')
-- CTX 3577 - CREST_COURT_ROOM_NO TO DEFAULT TO '1' IF DUMMY COURT_SITE_CODE = 'X'
-- CTX-4163 Further if COURT_SITE_CODE found but COURT_ROOM_NO NOT found, USE 'X' dummy site code and dummy courtroom_no
    AND   xcr.CREST_COURT_ROOM_NO =  decode(xcs.court_site_code,'X','1',stgcd.courtroom_no)                                  
--    AND   xrj.court_id = xc.court_id  -- CTX-3273 - remove reference and add in sub query
    AND   nvl(xc.obs_ind,'N') <> 'Y'
    AND   nvl(xcs.obs_ind,'N') <> 'Y'
    AND   nvl(xcr.obs_ind,'N') <> 'Y'
    AND   nvl(xl.obs_ind,'N') <> 'Y'
    AND   nvl(xrld.obs_ind,'N') <> 'Y'
    AND   NVL(stgcd.xhibit_etl_status,'N') not in ('I', 'U','D') -- CTX-3855 eliminate D (Duplicates) records
    AND   stgcd.xhibit_enrich_date is NULL
    AND   NOT EXISTS (SELECT 'x'
                      FROM xhibit.xhb_sitting_on_list xsol
                      WHERE xsol.list_id = xl.list_id
                      AND   NVL(xsol.obs_ind,'N') <> 'Y')
    ;

    v_count_number_of_rows   NUMBER := 0;
    v_sol_upd_rows           NUMBER := 0;
    v_sol_upd_status         CHAR(1) := 'N';
    v_sol_np_rows            NUMBER := 0;
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_list_id                xhibit.xhb_list.list_id%TYPE;
    prev_v_list_id           xhibit.xhb_list.list_id%TYPE;
    v_courtroom_no           xhibit.xhb_court_room.crest_court_room_no%TYPE;
    v_court_site_code        xhbstg_courtroom_day_dm.site_code%TYPE;
        -- CTX-3643
    v_stg_courtroom_no           xhibit.xhb_court_room.crest_court_room_no%TYPE;
    v_stg_court_site_code        xhbstg_courtroom_day_dm.site_code%TYPE; 
        -- CTX-3643
    v_sitting_number         xhibit.xhb_sitting_on_list.sitting_number%TYPE;
    v_ctd_id                 xhbstg_courtroom_day_dm.ctd_id%TYPE;
    v_list_date              xhbstg_courtroom_day_dm.list_date%TYPE;

BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_sit_on_list_crest- CTX-2403'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_SITTING_ON_LIST - Starting process of inserting  rows from CREST COURTROOM_DAY table'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

    prev_v_list_id := 0;
    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting rows from CREST COURTROOM_DAY table');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

    -- Here we are going to populate the rows in XHB_SITTING_ON_LIST table that have not yet been populated with CREST data
    OPEN cur_crest_sol_details;
    LOOP
    FETCH cur_crest_sol_details BULK COLLECT INTO xhb_sol_tt LIMIT g_bulk_limit;

    IF xhb_sol_tt IS NOT NULL AND xhb_sol_tt.COUNT > 0 THEN

        FOR i IN xhb_sol_tt.FIRST .. xhb_sol_tt.LAST LOOP

           BEGIN -- CTX-3252
           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_sit_on_list_crest- CTX-2403'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHB_SITTING_ON_LIST - inserting  list_id - '||xhb_sol_tt(i).list_id||' sitting_number : '||xhb_sol_tt(i).sitting_number||', court room ID : '||xhb_sol_tt(i).court_room_id||' ,court site ID : '||xhb_sol_tt(i).court_site_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
 
 -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            v_list_id         := xhb_sol_tt(i).list_id;
            v_sitting_number  := xhb_sol_tt(i).sitting_number;
            v_courtroom_no    := xhb_sol_tt(i).courtroom_no;
            v_court_site_code := xhb_sol_tt(i).site_code;
                -- CTX-3643
            v_stg_courtroom_no    := xhb_sol_tt(i).stg_court_room_no;
            v_stg_court_site_code := xhb_sol_tt(i).stg_court_site_code;
                -- CTX-3643
            v_ctd_id          := xhb_sol_tt(i).ctd_id;
            v_list_date       := xhb_sol_tt(i).list_date;

           /*  CTX-3273 TO SUPPRESS TOO MANY DBMS_OUT_PUT LINES - LIMIT THE OUTPUT*/
            IF prev_v_list_id <> v_list_id THEN
               DBMS_OUTPUT.PUT_LINE('v_list_id : '||v_list_id||' ,v_sitting_number : '||v_sitting_number||', courtroom No : '||v_courtroom_no||' court site code '||v_court_site_code||', v_xhibit_court_id '||v_xhibit_court_id);
               prev_v_list_id := v_list_id;
            END IF;   
            
         -- INSERT and populate this NEW table XHB_DEFENDANT_ON_CASE_HISTORY with data from CREST
                     INSERT INTO  xhibit.XHB_SITTING_ON_LIST
                                  ( SITTING_ON_LIST_ID
                                    ,SITTING_NUMBER
                                    ,LIST_ID
                                    ,TIME_MARKING_ID
                                    ,TIME_LISTED
                                    ,JUDGE_REF_ID
                                    ,JP1
                                    ,JP2
                                    ,JP3
                                    ,JP4
                                    ,LIST_NOTE_PRE_DEFINED_ID
                                    ,LIST_NOTE_TEXT
                                    ,PRE_DEF_NOTE_CLASSIFICATION_ID
                                    ,FREE_TEXT_NOTE_CLASS_ID
                                    ,OBS_IND
                                    ,LAST_UPDATE_DATE
                                    ,CREATION_DATE
                                    ,LAST_UPDATED_BY
                                    ,CREATED_BY
                                    ,VERSION
                                    ,COURT_ROOM_ID
                                    ,COURT_SITE_ID)
                           VALUES
                                  (xhibit.xhb_sitting_on_list_seq.nextval
                                  ,xhb_sol_tt(i).SITTING_NUMBER
                                  ,xhb_sol_tt(i).LIST_ID
                                  ,xhb_sol_tt(i).TIME_MARKING_ID
                                  ,xhb_sol_tt(i).TIME_LISTED
                                  ,xhb_sol_tt(i).JUDGE_REF_ID
                                  ,xhb_sol_tt(i).JP1
                                  ,xhb_sol_tt(i).JP2
                                  ,xhb_sol_tt(i).JP3
                                  ,xhb_sol_tt(i).JP4
                                  ,xhb_sol_tt(i).LIST_NOTE_PRE_DEFINED_ID
                                  ,xhb_sol_tt(i).LIST_NOTE_TEXT
                                  ,xhb_sol_tt(i).PRE_DEF_NOTE_CLASSIFICATION_ID
                                  ,xhb_sol_tt(i).FREE_TEXT_NOTE_CLASS_ID
                                  ,xhb_sol_tt(i).OBS_IND
                                  ,SYSDATE
                                  ,SYSDATE
                                  ,'DATA_MIGRATION'
                                  ,'DATA_MIGRATION'
                                  ,1
                                  ,xhb_sol_tt(i).COURT_ROOM_ID
                                  ,xhb_sol_tt(i).COURT_SITE_ID
                                  );

    v_sol_upd_rows := SQL%ROWCOUNT;
 --   DBMS_OUTPUT.PUT_LINE(' ');
 --   DBMS_OUTPUT.PUT_LINE('CTX-2403:XHB_SITTING_ON_LIST - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_sol_upd_rows);

               -- Update XHBSTG_CSU_HISTORY_DM table for the rows processed
         IF v_sol_upd_rows > 0 THEN
            v_sol_upd_status := 'I'; -- XHIBIT TABLE inserted
         ELSIF v_sol_upd_rows = 0 THEN
            v_sol_upd_status := 'N'; -- No Action Performed on XHIBIT table
         END IF;

    insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_sit_on_list_crest- CTX-2403'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_COURTOOM_DAY_DM - updated ctl_id '||v_ctd_id||' row with ETL_STATUS '||v_sol_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_sol_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

           -- Update XHBSTG_COURTOOM_DAY_DM table for the rows processed
            UPDATE XHBSTG_COURTROOM_DAY_DM xcd
            SET    xcd.xhibit_etl_date   = SYSDATE
                  ,xcd.xhibit_court_id   = v_xhibit_court_id
                  ,xcd.xhibit_enrich_date = SYSDATE
                  ,xcd.xhibit_etl_status = v_sol_upd_status
            WHERE  xcd.crest_court_id   =  p_crest_court_id
              AND  xcd.list_date        = v_list_date
              AND  xcd.jud_seq_no       = v_sitting_number
                  -- CTX-3643
                  -- CTX BM ISSUE NVL FIX
              AND  nvl(xcd.courtroom_no,0)     = nvl(v_stg_courtroom_no,0)
               AND nvl(xcd.site_code,'X')       = NVL(v_stg_court_site_code ,'X')
                   -- CTX-3643
              AND  xcd.ctd_id           = v_ctd_id
              ;


--                DBMS_OUTPUT.PUT_LINE(' ');
--                DBMS_OUTPUT.PUT_LINE('CTX-2403:XHBSTG_COURTOOM_DAY_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        -- ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_sit_on_list_crest for CREST_COURT : '||p_crest_court_id||', ctd_id : '||v_ctd_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_sit_on_list_crest - CTX-2403'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_sit_on_list_crest - Error processing crest ctl_id : '||v_ctd_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
         -- Update XHBSTG_COURTOOM_DAY_DM
         -- tables with error status/messages

            UPDATE XHBSTG_COURTROOM_DAY_DM xcd
            SET    xcd.xhibit_etl_date   = SYSDATE
                  ,xcd.xhibit_court_id   = v_xhibit_court_id
                  ,xcd.xhibit_enrich_date = SYSDATE
                  ,xcd.xhibit_etl_status = 'X' -- Error
                  ,xcd.xhibit_etl_err_message = v_err_message
            WHERE  xcd.crest_court_id   = p_crest_court_id
              AND  xcd.list_date        = v_list_date
              AND  xcd.jud_seq_no       = v_sitting_number
                  -- CTX-3643
              AND  xcd.courtroom_no     = v_stg_courtroom_no 
              AND  xcd.site_code        = v_stg_court_site_code
                  -- CTX-3643
              AND  xcd.ctd_id           = v_ctd_id
              ;

          END; -- CTX-3252

    END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
        v_count_number_of_rows := v_count_number_of_rows + xhb_sol_tt.COUNT;

    END IF;

    EXIT WHEN cur_crest_sol_details%NOTFOUND;

    END LOOP;


    CLOSE cur_crest_sol_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_sit_on_list_crest- CTX-2403'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_SITTING_ON_LIST : Processed '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
               ------ Update rest of the Unprocessed rows in XHBSTG_COURTOOM_DAY_DM
               ------ for given CREST COURT ID to 'N' i.e. NOT processed

                       -- Update XHBSTG_COURTOOM_DAY_DM table for the rows NOT processed
            UPDATE XHBSTG_COURTROOM_DAY_DM xcd
            SET    xcd.xhibit_etl_date   = SYSDATE
                  ,xcd.xhibit_court_id   = v_xhibit_court_id
                  ,xcd.xhibit_enrich_date = SYSDATE
                  ,xcd.xhibit_etl_status = 'N' -- Not processed
            WHERE  xcd.crest_court_id   =  p_crest_court_id
              AND  xcd.xhibit_etl_status is NULL
              AND  xcd.xhibit_enrich_date  is NULL
              AND  xcd.xhibit_etl_date  is NULL
              AND  xcd.xhibit_court_id is NULL;

              v_sol_np_rows := SQL%ROWCOUNT;

             insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_sit_on_list_crest- CTX-2403'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_SITTING_ON_LIST : updating XHBSTG_COURTOOM_DAY_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_sol_np_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2403:XHBSTG_COURTOOM_DAY_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2403:XHBSTG_COURTOOM_DAY_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_sol_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);

    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_sit_on_list_crest for CREST_COURT : '||p_crest_court_id||', ctd_id : '||v_ctd_id||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'upd_xhb_sit_on_list_crest - CTX-2403'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'upd_xhb_sit_on_list_crest - Error processing crest ctl_id : '||v_ctd_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
            COMMIT;

END upd_xhb_sit_on_list_crest;

/**
  * NAME       : upd_xhb_doc_on_list_crest
  * DESCRIPTION: CTX-2405 New CTX fields for XHB_DEF_ON_CASE_ON_LIST - Sec 4.3.2.31 req [4975.DM.034]
  *  CTX-3252: Structural change - approach for exception handling
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
 * S Sethuraman :  CTX-3813 - UPD_XHB_DOC_ON_LIST_CREST - inculde mapping with SITTING_ON_LIST to avoid duplicates
 * 14/03/2019     S Sethuraman   CTX-3856 - DOC_ON_LIST - Floater Cases issue fixed
 * 21/03/2019     A Dennis  CTX-3887 - Fix to migrate defendants on Warned Lists
 * 02/05/2019    S Sethuraman - CTX-4179  -Fix to avoid multiple records for multiple sitting/hearing on the same day i.e. more than 1 case_on_list record for 1 case_sub_appearance record
 * 13/06/2019    S Sethuraman - CTX-  --  Treat those records as Floater cases where priority is R and List type is F and CTD_ID is null
**/
PROCEDURE upd_xhb_doc_on_list_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_docol_rec IS RECORD
    ( defendant_on_case_id  xhibit.xhb_defendant_on_case.defendant_on_case_id%TYPE
    , case_on_list_id       xhibit.xhb_case_on_list.case_on_list_id%TYPE
    , case_id               xhibit.xhb_case.case_id%TYPE
    , obs_ind               xhibit.xhb_def_on_case_on_list.obs_ind%TYPE
    , case_no               xhbstg_case_sub_appearance_dm.case_no%TYPE
    , list_type             xhbstg_case_hearing_day_dm.list_type%TYPE
    , case_type             xhbstg_case_sub_appearance_dm.case_type%TYPE
    , chd_id                xhbstg_case_sub_appearance_dm.chd_id%TYPE
    , sub_id                xhbstg_case_sub_appearance_dm.sub_id%TYPE
    );

    TYPE xhb_docol_type IS TABLE OF xhb_docol_rec;
    xhb_docol_tt  xhb_docol_type;

    TYPE xhb_docol_fl_type IS TABLE OF xhb_docol_rec;
    xhb_docol_fl_tt  xhb_docol_fl_type;
    
    --CTX-3887
    TYPE xhb_docol_warned_type IS TABLE OF xhb_docol_rec;
    xhb_docol_warned_tt  xhb_docol_warned_type;
      
    CURSOR docol_cur IS
      SELECT defendant_on_case_id, -- CTX-4179 introduced row_number() over partition
             case_on_list_id,
             case_id,
             obs_ind,
             case_no,
             list_type,
             case_type,
             chd_id,
             sub_id
        FROM (
    SELECT xdoc.defendant_on_case_id
    , xcol.case_on_list_id
    , xc.case_id
    , 'N' as OBS_IND
    , csastg.case_no
    , chdstg.list_type       
    , chdstg.case_type
    , chdstg.chd_id
    , csastg.sub_id
    , ROW_NUMBER() over  (partition by csastg.chd_id,xc.case_id,xdoc.defendant_on_Case_id
                          order by csastg.chd_id,xc.case_id,xdoc.defendant_on_Case_id) as rn    
    FROM xhbstg_case_sub_appearance_dm csastg
    ,    xhbstg_case_hearing_day_dm chdstg
    ,    xhibit.xhb_case xc
    ,    xhibit.xhb_defendant_on_case xdoc
    ,    xhibit.xhb_defendant xd
    ,    xhibit.xhb_case_on_list xcol
    ,    xhibit.xhb_sitting_on_list xsol -- CTX-3813
    ,    xhibit.xhb_list xl
    WHERE csastg.crest_court_id = p_crest_court_id
    AND   csastg.crest_court_id = chdstg.crest_court_id
    AND   csastg.chd_id = chdstg.chd_id
    AND   chdstg.list_type != 'X' 
    AND   csastg.case_no = xc.case_number
    AND   csastg.case_type = xc.case_type
    AND   xc.court_id = v_xhibit_court_id
    AND   xc.case_id = xdoc.case_id
    AND   nvl(xdoc.obs_ind,'N') <> 'Y'
    AND   xdoc.defendant_id = xd.defendant_id
    AND   xd.crest_defendant_id = csastg.sub_id
    AND   xd.court_id = xc.court_id
    AND   xc.case_id = xcol.case_id
    AND   xc.court_id = xl.court_id 
    AND   xsol.sitting_on_list_id = xcol.sitting_on_list_id  -- CTX-3813
    AND   xsol.list_id = xl.list_id    -- CTX-3813   
    AND   TRUNC(chdstg.list_date) BETWEEN TRUNC(xl.list_start_date) AND TRUNC(xl.list_end_date)
    AND   chdstg.list_date = trunc(xsol.time_listed)  -- CTX-3813 
    AND   xcol.list_id = xl.list_id
    AND   xl.list_type_id = (SELECT x.ref_listing_data_id
                             FROM xhibit.xhb_ref_listing_data x
                             WHERE x.ref_data_type = 'LIST_TYPE'
                             AND DECODE(chdstg.list_type,'D','Daily','F','Firm','W','Warned') = x.ref_data_value
                             AND NVL(x.obs_ind,'N') <> 'Y')
    AND   nvl(xcol.floater_case,'N') <> 'Y' -- CTX-3856                             
    AND   nvl(xcol.obs_ind,'N') <> 'Y'
   -- CTX-4179
    AND   xcol.hearing_type_id = (select xrh.ref_hearing_type_id from xhibit.xhb_ref_hearing_type xrh
                                   where  xrh.court_id = xc.court_id and xrh.hearing_type_code = chdstg.hearing_type
                                        and xrh.category = 'X' and nvl(xrh.obs_ind,'N') <> 'Y')         
    /* CTX-3216 Commented these out so to process again rows already processed by upd_xhb_c_diary_fixture_crest since each procedure looks for different columns in the same rows in the table xhbstg_case_sub_appearance_dm
    AND   NVL(csastg.xhibit_etl_status,'N') NOT IN ('I', 'U') 
    AND   csastg.xhibit_enrich_date IS NULL */
    /*Make sure no duplicate rows are created*/
    AND   NOT EXISTS (SELECT 'x'
                      FROM  xhibit.xhb_def_on_case_on_list xdocol
                      WHERE xdocol.defendant_on_case_id = xdoc.defendant_on_case_id
                      AND   xdocol.case_on_list_id = xcol.case_on_list_id
                      AND   xdocol.case_id = xc.case_id)
      ) WHERE rn = 1;

-- For FLOATER CASES there is NO Sitting_on_list - so references removed and
-- CASE_ON_LIST.FLOATER_CASE flag check included 

CURSOR docol_floater_cur IS
      SELECT defendant_on_case_id, -- CTX-4179 introduced row_number() over partition
             case_on_list_id,
             case_id,
             obs_ind,
             case_no,
             list_type,
             case_type,
             chd_id,
             sub_id
        FROM (
    SELECT xdoc.defendant_on_case_id
    , xcol.case_on_list_id
    , xc.case_id
    , 'N' as OBS_IND
    , csastg.case_no
    , chdstg.list_type
    , chdstg.case_type
    , chdstg.chd_id
    , csastg.sub_id
    , ROW_NUMBER() over  (partition by csastg.chd_id,xc.case_id,xdoc.defendant_on_Case_id
                          order by csastg.chd_id,xc.case_id,xdoc.defendant_on_Case_id) as rn        
    FROM xhbstg_case_sub_appearance_dm csastg
    ,    xhbstg_case_hearing_day_dm chdstg
    ,    xhibit.xhb_case xc
    ,    xhibit.xhb_defendant_on_case xdoc
    ,    xhibit.xhb_defendant xd
    ,    xhibit.xhb_case_on_list xcol
    ,    xhibit.xhb_list xl
    WHERE csastg.crest_court_id = p_crest_court_id
    AND   csastg.crest_court_id = chdstg.crest_court_id
    AND   csastg.chd_id = chdstg.chd_id
    AND   chdstg.list_type != 'X'
    AND   csastg.case_no = xc.case_number
    AND   csastg.case_type = xc.case_type
    AND   xc.court_id = v_xhibit_court_id
    AND   xc.case_id = xdoc.case_id
    AND   nvl(xdoc.obs_ind,'N') <> 'Y'
    AND   xdoc.defendant_id = xd.defendant_id
    AND   xd.crest_defendant_id = csastg.sub_id
    AND   xd.court_id = xc.court_id
    AND   xc.case_id = xcol.case_id
    AND   xc.court_id = xl.court_id
    AND   xcol.list_id = xl.list_id    -- CTX-3813
    AND   TRUNC(chdstg.list_date) BETWEEN TRUNC(xl.list_start_date) AND TRUNC(xl.list_end_date)
    AND   xcol.list_id = xl.list_id
    AND   xl.list_type_id = (SELECT x.ref_listing_data_id
                             FROM xhibit.xhb_ref_listing_data x
                             WHERE x.ref_data_type = 'LIST_TYPE'
                             AND DECODE(chdstg.list_type,'D','Daily','F','Firm','W','Warned') = x.ref_data_value
                             AND NVL(x.obs_ind,'N') <> 'Y')
    AND   ((nvl(xcol.floater_case,'N') = 'Y') -- CTX-3856   
           --  Treat those records as Floater cases where priority is R and List type is F and CTD_ID is null 
             OR   ((nvl(xcol.floater_case,'N') = 'N') and chdstg.list_type = 'F' and chdstg.priority = 'R' and chdstg.ctd_id is NULL))                       
    AND   nvl(xcol.obs_ind,'N') <> 'Y'
   -- CTX-4179
    AND   xcol.hearing_type_id = (select xrh.ref_hearing_type_id from xhibit.xhb_ref_hearing_type xrh
                                   where  xrh.court_id = xc.court_id and xrh.hearing_type_code = chdstg.hearing_type
                                        and xrh.category = 'X' and nvl(xrh.obs_ind,'N') <> 'Y')             
    /* CTX-3216 Commented these out so to process again rows already processed by upd_xhb_c_diary_fixture_crest since each procedure looks for different columns in the same rows in the table xhbstg_case_sub_appearance_dm
    AND   NVL(csastg.xhibit_etl_status,'N') NOT IN ('I', 'U')
    AND   csastg.xhibit_enrich_date IS NULL */
    /*Make sure no duplicate rows are created*/
    AND   NOT EXISTS (SELECT 'x'
                      FROM  xhibit.xhb_def_on_case_on_list xdocol
                      WHERE xdocol.defendant_on_case_id = xdoc.defendant_on_case_id
                      AND   xdocol.case_on_list_id = xcol.case_on_list_id
                      AND   xdocol.case_id = xc.case_id)
      ) WHERE rn = 1;
    
--CTX-3887 - Created this cursor to retrieve defendants on Warned Lists which are not been fetched by the cursors above.
-- So List_type = 'W', CTD_ID IS NULL, PRIORITY IS NULL and XHB_CASE_ON_LIST records ahve FLOATER IS NULL
CURSOR docol_warned_cur IS
    SELECT defendant_on_case_id, -- CTX-4179 introduced row_number() over partition
             case_on_list_id,
             case_id,
             obs_ind,
             case_no,
             list_type,
             case_type,
             chd_id,
             sub_id
        FROM (
    SELECT xdoc.defendant_on_case_id
    , xcol.case_on_list_id
    , xc.case_id
    , 'N' as OBS_IND
    , csastg.case_no
    , chdstg.list_type
    , chdstg.case_type
    , chdstg.chd_id
    , csastg.sub_id
    , ROW_NUMBER() over  (partition by csastg.chd_id,xc.case_id,xdoc.defendant_on_Case_id
                          order by csastg.chd_id,xc.case_id,xdoc.defendant_on_Case_id) as rn        
    FROM xhbstg_case_sub_appearance_dm csastg
    ,    xhbstg_case_hearing_day_dm chdstg
    ,    xhibit.xhb_case xc
    ,    xhibit.xhb_defendant_on_case xdoc
    ,    xhibit.xhb_defendant xd
    ,    xhibit.xhb_case_on_list xcol
    ,    xhibit.xhb_list xl
    WHERE csastg.crest_court_id = p_crest_court_id
    AND   csastg.crest_court_id = chdstg.crest_court_id
    AND   csastg.chd_id = chdstg.chd_id
    AND   chdstg.list_type = 'W'   -- CTX-3887
    AND   chdstg.ctd_id   IS NULL  -- CTX-3887
    AND   chdstg.priority IS NULL  -- CTX-3887
    AND   csastg.case_no = xc.case_number
    AND   csastg.case_type = xc.case_type
    AND   xc.court_id = v_xhibit_court_id
    AND   xc.case_id = xdoc.case_id
    AND   nvl(xdoc.obs_ind,'N') <> 'Y'
    AND   xdoc.defendant_id = xd.defendant_id
    AND   xd.crest_defendant_id = csastg.sub_id
    AND   xd.court_id = xc.court_id
    AND   xc.case_id = xcol.case_id
    AND   xc.court_id = xl.court_id
    AND   xcol.list_id = xl.list_id    -- CTX-3813
    AND   TRUNC(chdstg.list_date) BETWEEN TRUNC(xl.list_start_date) AND TRUNC(xl.list_end_date)
    AND   xcol.list_id = xl.list_id
    AND   xl.list_type_id = (SELECT x.ref_listing_data_id
                             FROM xhibit.xhb_ref_listing_data x
                             WHERE x.ref_data_type = 'LIST_TYPE'
                             AND DECODE(chdstg.list_type,'D','Daily','F','Firm','W','Warned') = x.ref_data_value
                             AND NVL(x.obs_ind,'N') <> 'Y')
    AND   xcol.floater_case IS NULL  -- CTX-3887                         
    AND   nvl(xcol.obs_ind,'N') <> 'Y'
 -- CTX--  4179  
    AND   xcol.hearing_type_id = (select xrh.ref_hearing_type_id from xhibit.xhb_ref_hearing_type xrh
                                   where  xrh.court_id = xc.court_id and xrh.hearing_type_code = chdstg.hearing_type
                                        and xrh.category = 'X' and nvl(xrh.obs_ind,'N') <> 'Y')        
    /* CTX-3216 Commented these out so to process again rows already processed by upd_xhb_c_diary_fixture_crest since each procedure looks for different columns in the same rows in the table xhbstg_case_sub_appearance_dm
    AND   NVL(csastg.xhibit_etl_status,'N') NOT IN ('I', 'U')
    AND   csastg.xhibit_enrich_date IS NULL */
    /*Make sure no duplicate rows are created*/
    AND   NOT EXISTS (SELECT 'x'
                      FROM  xhibit.xhb_def_on_case_on_list xdocol
                      WHERE xdocol.defendant_on_case_id = xdoc.defendant_on_case_id
                      AND   xdocol.case_on_list_id = xcol.case_on_list_id
                      AND   xdocol.case_id = xc.case_id)
  ) WHERE rn = 1;

    v_count_number_of_rows  NUMBER := 0;
    v_docol_upd_rows        NUMBER := 0;
    v_docol_upd_status      CHAR(1) := 'N';
    v_docol_np_rows         NUMBER := 0;
    v_err_message           xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_doc_id                xhibit.xhb_defendant_on_case.defendant_on_case_id%TYPE;
    v_col_id                xhibit.xhb_case_on_list.case_on_list_id%TYPE;
    v_case_id               xhibit.xhb_case.case_id%TYPE;
    v_case_no               xhbstg_case_sub_appearance_dm.case_no%TYPE;
    v_list_type             xhbstg_case_hearing_day_dm.list_type%TYPE;
    v_case_type             xhbstg_case_sub_appearance_dm.case_type%TYPE;
    v_chd_id                xhbstg_case_sub_appearance_dm.chd_id%TYPE;
    v_sub_id                xhbstg_case_sub_appearance_dm.sub_id%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_DEF_ON_CASE_ON_LIST - Starting process of inserting  rows from CREST CASE_SUBJECT_APPEARANCE table'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  =>  NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting rows from CREST CASE_SUBJECT_APPEARANCE table');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
    -- Here we are going to populate the rows in XHB_DEF_ON_CASE_ON_LIST table that have not yet been populated with CREST data
    OPEN docol_cur;
    LOOP
    FETCH docol_cur BULK COLLECT INTO xhb_docol_tt LIMIT g_bulk_limit;
   
    IF xhb_docol_tt IS NOT NULL AND xhb_docol_tt.COUNT > 0 THEN
   
        FOR i IN xhb_docol_tt.FIRST .. xhb_docol_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                v_doc_id       := xhb_docol_tt(i).defendant_on_case_id;
                v_col_id       := xhb_docol_tt(i).case_on_list_id;
                v_case_id      := xhb_docol_tt(i).case_id;
                v_case_no      := xhb_docol_tt(i).case_no;
                v_case_type    := xhb_docol_tt(i).case_type;
                v_chd_id       := xhb_docol_tt(i).chd_id;
                v_list_type    := xhb_docol_tt(i).list_type;
                v_sub_id       := xhb_docol_tt(i).sub_id;
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - '||v_doc_id||' case_on_list_id : '||v_col_id||', case_id : '||v_case_id||' list type: '||v_list_type
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = '||p_crest_court_id);
                
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
            
            
                DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||' ,v_case_type : '||v_case_type||', v_sub_id: '||v_sub_id||' v_chd_id '||v_chd_id||', v_xhibit_court_id '||v_xhibit_court_id);         
                
                -- INSERT and populate this NEW table XHB_DEF_ON_CASE_ON_LIST with data from CREST  
                INSERT INTO  xhibit.XHB_DEF_ON_CASE_ON_LIST
                            ( DEF_ON_CASE_ON_LIST_ID
                              ,DEFENDANT_ON_CASE_ID
                              ,CASE_ON_LIST_ID
                              ,CASE_ID
                              ,OBS_IND
                              ,CREATED_BY 
                              ,CREATION_DATE 
                              ,LAST_UPDATE_DATE
                              ,LAST_UPDATED_BY
                              ,VERSION
                            )
                     VALUES 
                            (xhibit.XHB_DEF_ON_CASE_ON_LIST_SEQ.nextval
                            ,v_doc_id
                            ,v_col_id
                            ,v_case_id
                            ,xhb_docol_tt(i).OBS_IND
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,1
                            );
                
                v_docol_upd_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_docol_upd_rows);
    
                -- Update XHBSTG_CSU_HISTORY_DM table for the rows processed
                IF v_docol_upd_rows > 0 THEN 
                    v_docol_upd_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_docol_upd_rows = 0 THEN
                    v_docol_upd_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: '||v_case_no||' case type '||v_case_type||' sub id '||v_sub_id||' row with ETL_STATUS '||v_docol_upd_status
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  => v_docol_upd_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
                         
                -- Update XHBSTG_CASE_SUB_APPEARANCE_DM table for the rows processed
                UPDATE XHBSTG_CASE_SUB_APPEARANCE_DM xcd
                SET    xcd.xhibit_etl_date   = SYSDATE
                              ,xcd.xhibit_court_id   = v_xhibit_court_id
                              ,xcd.xhibit_enrich_date = SYSDATE
                              ,xcd.xhibit_etl_status = v_docol_upd_status
                WHERE  xcd.crest_court_id   =  p_crest_court_id
                AND  xcd.case_type        = v_case_type
                AND  xcd.case_no          = v_case_no
                AND  xcd.sub_id           = v_sub_id
                AND  xcd.chd_id           = v_chd_id
                ;
                                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2405:XHBSTG_CASE_SUB_APPEARANCE_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

        EXCEPTION
            WHEN OTHERS THEN

                v_err_message := SQLERRM;
                
                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_doc_on_list_crest for CREST_COURT : '||p_crest_court_id
                                                                                                         ||', chd_id : '||v_chd_id
                                                                                                         ||' v_sub_id'||v_sub_id
                                                                                                         ||' v_case_no '||v_case_no
                                                                                                         ||'-'||SUBSTR(v_err_message,1,100));
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_doc_on_list_crest - CTX-2405'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'E' -- Error
                             ,p_log_msg            =>  'upd_xhb_doc_on_list_crest - Error processing crest chd_id : '||v_chd_id||' Error: '||SUBSTR(v_err_message,1,150)
                             ,p_err_row_count      => NULL 
                             ,p_success_row_count  => v_count_number_of_rows
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
    
                -- Update XHBSTG_CASE_SUB_APPEARANCE_DM
                -- tables with error status/messages
                BEGIN
                              
                    UPDATE XHBSTG_CASE_SUB_APPEARANCE_DM xcd
                    SET    xcd.xhibit_etl_date   = SYSDATE
                          ,xcd.xhibit_court_id   = v_xhibit_court_id
                          ,xcd.xhibit_enrich_date = SYSDATE
                          ,xcd.xhibit_etl_status = 'X' --Error
                    WHERE  xcd.crest_court_id   = p_crest_court_id
                      AND  xcd.case_type        = v_case_type
                      AND  xcd.case_no          = v_case_no
                      AND  xcd.sub_id           = v_sub_id
                      AND  xcd.chd_id           = v_chd_id
                      ;
                      
                END;

        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_docol_tt.COUNT;

    END IF;
    
    EXIT WHEN docol_cur%NOTFOUND;
    
    END LOOP;
        
    CLOSE docol_cur;

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_DEF_ON_CASE_ON_LIST : Processed '||v_count_number_of_rows||' successfully!'
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_count_number_of_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );

--------- ctx-3856 - FLOATER CASES ONLY

    -- Here we are going to populate the rows in XHB_DEF_ON_CASE_ON_LIST table that have not yet been populated with CREST data
    OPEN docol_floater_cur;
    LOOP
    FETCH docol_floater_cur BULK COLLECT INTO xhb_docol_fl_tt LIMIT g_bulk_limit;

    IF xhb_docol_fl_tt IS NOT NULL AND xhb_docol_fl_tt.COUNT > 0 THEN

        FOR i IN xhb_docol_fl_tt.FIRST .. xhb_docol_fl_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                v_doc_id       := xhb_docol_fl_tt(i).defendant_on_case_id;
                v_col_id       := xhb_docol_fl_tt(i).case_on_list_id;
                v_case_id      := xhb_docol_fl_tt(i).case_id;
                v_case_no      := xhb_docol_fl_tt(i).case_no;
                v_case_type    := xhb_docol_fl_tt(i).case_type;
                v_chd_id       := xhb_docol_fl_tt(i).chd_id;
                v_list_type    := xhb_docol_fl_tt(i).list_type;
                v_sub_id       := xhb_docol_fl_tt(i).sub_id;

                insert_dm_log (p_crest_court_id     => p_crest_court_id
                             ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - '||v_doc_id||' case_on_list_id : '||v_col_id||', case_id : '||v_case_id||' list type: '||v_list_type
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = '||p_crest_court_id);

                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update


                DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||' ,v_case_type : '||v_case_type||', v_sub_id: '||v_sub_id||' v_chd_id '||v_chd_id||', v_xhibit_court_id '||v_xhibit_court_id);

                -- INSERT and populate this NEW table XHB_DEF_ON_CASE_ON_LIST with data from CREST
                INSERT INTO  xhibit.XHB_DEF_ON_CASE_ON_LIST
                            ( DEF_ON_CASE_ON_LIST_ID
                              ,DEFENDANT_ON_CASE_ID
                              ,CASE_ON_LIST_ID
                              ,CASE_ID
                              ,OBS_IND
                              ,CREATED_BY
                              ,CREATION_DATE
                              ,LAST_UPDATE_DATE
                              ,LAST_UPDATED_BY
                              ,VERSION
                            )
                     VALUES
                            (xhibit.XHB_DEF_ON_CASE_ON_LIST_SEQ.nextval
                            ,v_doc_id
                            ,v_col_id
                            ,v_case_id
                            ,xhb_docol_fl_tt(i).OBS_IND
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,1
                            );

                v_docol_upd_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_docol_upd_rows);

                -- Update table for the rows processed
                IF v_docol_upd_rows > 0 THEN
                    v_docol_upd_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_docol_upd_rows = 0 THEN
                    v_docol_upd_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;

                insert_dm_log (p_crest_court_id     => p_crest_court_id
                               ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: '||v_case_no||' case type '||v_case_type||' sub id '||v_sub_id||' row with ETL_STATUS '||v_docol_upd_status
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  => v_docol_upd_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );

                -- Update XHBSTG_CASE_SUB_APPEARANCE_DM table for the rows processed
                UPDATE XHBSTG_CASE_SUB_APPEARANCE_DM xcd
                SET    xcd.xhibit_etl_date   = SYSDATE
                              ,xcd.xhibit_court_id   = v_xhibit_court_id
                              ,xcd.xhibit_enrich_date = SYSDATE
                              ,xcd.xhibit_etl_status = v_docol_upd_status
                WHERE  xcd.crest_court_id   =  p_crest_court_id
                AND  xcd.case_type        = v_case_type
                AND  xcd.case_no          = v_case_no
                AND  xcd.sub_id           = v_sub_id
                AND  xcd.chd_id           = v_chd_id
                ;

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2405:XHBSTG_CASE_SUB_APPEARANCE_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

        EXCEPTION
            WHEN OTHERS THEN

                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_doc_on_list_crest for CREST_COURT : '||p_crest_court_id
                                                                                                         ||', chd_id : '||v_chd_id
                                                                                                         ||' v_sub_id'||v_sub_id
                                                                                                         ||' v_case_no '||v_case_no
                                                                                                         ||'-'||SUBSTR(v_err_message,1,100));

                insert_dm_log (p_crest_court_id     => p_crest_court_id
                             ,p_action_name        => 'upd_xhb_doc_on_list_crest - CTX-2405'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'E' -- Error
                             ,p_log_msg            =>  'upd_xhb_doc_on_list_crest - Error processing crest chd_id : '||v_chd_id||' Error: '||SUBSTR(v_err_message,1,150)
                             ,p_err_row_count      => NULL
                             ,p_success_row_count  => v_count_number_of_rows
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );

                -- Update XHBSTG_CASE_SUB_APPEARANCE_DM
                -- tables with error status/messages
                BEGIN

                    UPDATE XHBSTG_CASE_SUB_APPEARANCE_DM xcd
                    SET    xcd.xhibit_etl_date   = SYSDATE
                          ,xcd.xhibit_court_id   = v_xhibit_court_id
                          ,xcd.xhibit_enrich_date = SYSDATE
                          ,xcd.xhibit_etl_status = 'X' --Error
                    WHERE  xcd.crest_court_id   = p_crest_court_id
                      AND  xcd.case_type        = v_case_type
                      AND  xcd.case_no          = v_case_no
                      AND  xcd.sub_id           = v_sub_id
                      AND  xcd.chd_id           = v_chd_id
                      ;

                END;

        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing

    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_docol_fl_tt.COUNT;

    END IF;

    EXIT WHEN docol_floater_cur%NOTFOUND;

    END LOOP;

    CLOSE docol_floater_cur;

    insert_dm_log (p_crest_court_id     => p_crest_court_id
               ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_DEF_ON_CASE_ON_LIST : Processed '||v_count_number_of_rows||' successfully!'
               ,p_err_row_count      => NULL
               ,p_success_row_count  => v_count_number_of_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );

----------- ctx-3856
   
    --------------- Start of CTX-3887 Warned Lists
    -- Here we are going to populate the rows in XHB_DEF_ON_CASE_ON_LIST table with data on the Warned Lists. These have not yet been populated with CREST data
    OPEN docol_warned_cur;
    LOOP
    FETCH docol_warned_cur BULK COLLECT INTO xhb_docol_warned_tt LIMIT g_bulk_limit;

    IF xhb_docol_warned_tt IS NOT NULL AND xhb_docol_warned_tt.COUNT > 0 THEN

        FOR i IN xhb_docol_warned_tt.FIRST .. xhb_docol_warned_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                v_doc_id       := xhb_docol_warned_tt(i).defendant_on_case_id;
                v_col_id       := xhb_docol_warned_tt(i).case_on_list_id;
                v_case_id      := xhb_docol_warned_tt(i).case_id;
                v_case_no      := xhb_docol_warned_tt(i).case_no;
                v_case_type    := xhb_docol_warned_tt(i).case_type;
                v_chd_id       := xhb_docol_warned_tt(i).chd_id;
                v_list_type    := xhb_docol_warned_tt(i).list_type;
                v_sub_id       := xhb_docol_warned_tt(i).sub_id;

                insert_dm_log (p_crest_court_id     => p_crest_court_id
                             ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'UPDATING XHB_DEF_ON_CASE_ON_LIST - inserting  defendant_on_case_id - '||v_doc_id||' case_on_list_id : '||v_col_id||', case_id : '||v_case_id||' list type: '||v_list_type
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_DEF_ON_CASE_ON_LIST - FOR CREST_COURT_ID = '||p_crest_court_id);

                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update


                DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||' ,v_case_type : '||v_case_type||', v_sub_id: '||v_sub_id||' v_chd_id '||v_chd_id||', v_xhibit_court_id '||v_xhibit_court_id);

                -- INSERT and populate this NEW table XHB_DEF_ON_CASE_ON_LIST with data from CREST
                INSERT INTO  xhibit.XHB_DEF_ON_CASE_ON_LIST
                            ( DEF_ON_CASE_ON_LIST_ID
                              ,DEFENDANT_ON_CASE_ID
                              ,CASE_ON_LIST_ID
                              ,CASE_ID
                              ,OBS_IND
                              ,CREATED_BY
                              ,CREATION_DATE
                              ,LAST_UPDATE_DATE
                              ,LAST_UPDATED_BY
                              ,VERSION
                            )
                     VALUES
                            (xhibit.XHB_DEF_ON_CASE_ON_LIST_SEQ.nextval
                            ,v_doc_id
                            ,v_col_id
                            ,v_case_id
                            ,xhb_docol_warned_tt(i).OBS_IND
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,1
                            );

                v_docol_upd_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2405:XHB_DEF_ON_CASE_ON_LIST - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_docol_upd_rows);

                -- Update table for the rows processed
                IF v_docol_upd_rows > 0 THEN
                    v_docol_upd_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_docol_upd_rows = 0 THEN
                    v_docol_upd_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;

                insert_dm_log (p_crest_court_id     => p_crest_court_id
                               ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'UPDATING XHBSTG_CASE_SUB_APPEARANCE_DM - updated case no: '||v_case_no||' case type '||v_case_type||' sub id '||v_sub_id||' row with ETL_STATUS '||v_docol_upd_status
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  => v_docol_upd_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );

                -- Update XHBSTG_CASE_SUB_APPEARANCE_DM table for the rows processed
                UPDATE XHBSTG_CASE_SUB_APPEARANCE_DM xcd
                SET    xcd.xhibit_etl_date   = SYSDATE
                              ,xcd.xhibit_court_id   = v_xhibit_court_id
                              ,xcd.xhibit_enrich_date = SYSDATE
                              ,xcd.xhibit_etl_status = v_docol_upd_status
                WHERE  xcd.crest_court_id   =  p_crest_court_id
                AND  xcd.case_type        = v_case_type
                AND  xcd.case_no          = v_case_no
                AND  xcd.sub_id           = v_sub_id
                AND  xcd.chd_id           = v_chd_id
                ;

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-2405:XHBSTG_CASE_SUB_APPEARANCE_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

        EXCEPTION
            WHEN OTHERS THEN

                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_doc_on_list_crest for CREST_COURT : '||p_crest_court_id
                                                                                                         ||', chd_id : '||v_chd_id
                                                                                                         ||' v_sub_id'||v_sub_id
                                                                                                         ||' v_case_no '||v_case_no
                                                                                                         ||'-'||SUBSTR(v_err_message,1,100));

                insert_dm_log (p_crest_court_id     => p_crest_court_id
                             ,p_action_name        => 'upd_xhb_doc_on_list_crest - CTX-2405'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'E' -- Error
                             ,p_log_msg            =>  'upd_xhb_doc_on_list_crest - Error processing crest chd_id : '||v_chd_id||' Error: '||SUBSTR(v_err_message,1,150)
                             ,p_err_row_count      => NULL
                             ,p_success_row_count  => v_count_number_of_rows
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );

                -- Update XHBSTG_CASE_SUB_APPEARANCE_DM
                -- tables with error status/messages
                BEGIN

                    UPDATE XHBSTG_CASE_SUB_APPEARANCE_DM xcd
                    SET    xcd.xhibit_etl_date   = SYSDATE
                          ,xcd.xhibit_court_id   = v_xhibit_court_id
                          ,xcd.xhibit_enrich_date = SYSDATE
                          ,xcd.xhibit_etl_status = 'X' --Error
                    WHERE  xcd.crest_court_id   = p_crest_court_id
                      AND  xcd.case_type        = v_case_type
                      AND  xcd.case_no          = v_case_no
                      AND  xcd.sub_id           = v_sub_id
                      AND  xcd.chd_id           = v_chd_id
                      ;

                END;

        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing

    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_docol_warned_tt.COUNT;

    END IF;

    EXIT WHEN docol_warned_cur%NOTFOUND;

    END LOOP;

    CLOSE docol_warned_cur;

    insert_dm_log (p_crest_court_id     => p_crest_court_id
               ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_DEF_ON_CASE_ON_LIST : Processed '||v_count_number_of_rows||' successfully!'
               ,p_err_row_count      => NULL
               ,p_success_row_count  => v_count_number_of_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );
               
    --------- End of CTX-3887
               
     ------ Update rest of the Unprocessed rows in XHBSTG_COURTOOM_DAY_DM 
     ------ for given CREST COURT ID to 'N' i.e. NOT processed
     
    -- Update XHBSTG_CASE_SUB_APPEARANCE_DM table for the rows NOT processed
    UPDATE XHBSTG_CASE_SUB_APPEARANCE_DM xcd
    SET    xcd.xhibit_etl_date   = SYSDATE
        ,xcd.xhibit_court_id   = v_xhibit_court_id
        ,xcd.xhibit_enrich_date = SYSDATE
        ,xcd.xhibit_etl_status = 'N' -- Not processed
    WHERE  xcd.crest_court_id   =  p_crest_court_id
    AND  xcd.xhibit_etl_status is NULL 
    AND  xcd.xhibit_enrich_date  is NULL
    AND  xcd.xhibit_etl_date  is NULL;
  -- AND  xcd.xhibit_court_id is NULL; -- incorrect condition eliminated
    
    v_docol_np_rows := SQL%ROWCOUNT;
                         
    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_doc_on_list_crest- CTX-2405'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_DEF_ON_CASE_ON_LIST : updating XHBSTG_CASE_SUB_APPEARANCE_DM with ETL_STATUS = N  where rows NOT updated for CREST Court id  '||p_crest_court_id
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_docol_np_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2405:XHBSTG_CASE_SUB_APPEARANCE_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-2405:XHBSTG_CASE_SUB_APPEARANCE_DM processed, BUT XHIBIT NOT UPDATED as no matching criteria found - '||v_docol_np_rows||' for CREST_COURT_ID : '||p_crest_court_id);
    
    COMMIT;
    --ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN

        v_err_message := SQLERRM;
        
        --ROLLBACK; --CTX-3252

        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_doc_on_list_crest for CREST_COURT : '||p_crest_court_id
                                                                                                     ||', chd_id : '||v_chd_id
                                                                                                     ||' v_sub_id'||v_sub_id
                                                                                                     ||' v_case_no '||v_case_no
                                                                                                     ||'-'||SUBSTR(v_err_message,1,100));

        insert_dm_log (p_crest_court_id     => p_crest_court_id 
                     ,p_action_name        => 'upd_xhb_doc_on_list_crest - CTX-2405'
                     ,p_run_time           =>  sysdate
                     ,p_log_msg_type       =>  'E' -- Error
                     ,p_log_msg            =>  'upd_xhb_doc_on_list_crest - Error processing crest chd_id : '||v_chd_id||' Error: '||SUBSTR(v_err_message,1,150)
                     ,p_err_row_count      => NULL 
                     ,p_success_row_count  => v_count_number_of_rows
                     ,p_last_updated_by    => 'DATA MIGRATION'
                     ,p_created_by         => 'DATA MIGRATION'
                     );
                     
        COMMIT;
                     
END upd_xhb_doc_on_list_crest;

/**
  * NAME       : update_xhb_rpa_email_with_crest
  * DESCRIPTION: CTX-3007 - Update XHB_REF_PROSECUTOR_AGENCY - EMAIL  in XHB_CONTACT_DETAIL - Sec 4.3.2.35 req [4975.DM.037]
  *              includes CTX-3252 changes
  * PARAMETERS : p_court_id  - The CREST Court ID being processed will be passed to this procedure
**/
PROCEDURE update_xhb_rpa_mail_with_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
IS
    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;

    TYPE xhbstg_rpa_email_dm_rec IS RECORD
    (
     contact_type       XHIBIT.XHB_CONTACT_DETAIL.CONTACT_TYPE%type
    ,email_format      XHIBIT.XHB_CONTACT_DETAIL.email_format%TYPE
    ,pager_net         XHIBIT.XHB_CONTACT_DETAIL.pager_net%TYPE     
    ,mail              XHIBIT.XHB_CONTACT_DETAIL.CONTACT_VALUE%type
    ,address_id        XHIBIT.XHB_REF_PROSECUTOR_AGENCY.address_id%TYPE
    ,opp_id            xhbstg_opposer_dm.opp_id%TYPE
    ,crest_court_id    xhibit.xhb_court.crest_court_id%TYPE
    ,court_id          xhibit.xhb_court.court_id%TYPE
    );

    TYPE xhbstg_rpa_email_dm_type IS TABLE OF xhbstg_rpa_email_dm_rec;
    xhbstg_rpa_email_dm_tt  xhbstg_rpa_email_dm_type;

    CURSOR ref_pro_agency_cur IS
    SELECT 'Non Secure Email' AS CONTACT_TYPE
    ,      NULL as email_format
    ,      NULL as pager_net
    ,      stg.opp_email as MAIL    
    ,      xpa.address_id
    ,      stg.opp_id
    ,      xcrt.crest_court_id
    ,      xcrt.court_id
    FROM xhbstg_opposer_dm stg
    ,    xhibit.xhb_court xcrt
    ,    xhibit.xhb_ref_prosecutor_agency xpa
    WHERE stg.crest_court_id = p_crest_court_id
    AND   stg.crest_court_id = xcrt.crest_court_id
    AND   xpa.crest_opposer_id = to_char(stg.opp_id)
    AND   xcrt.court_id = xpa.court_id
    AND   stg.opp_email IS NOT NULL
    AND   NVL(stg.xhibit_etl_status,'N') not in ('I', 'U') 
    AND   stg.xhibit_enrich_date is NULL 
    AND   NOT exists
          (select 'X' from xhibit.xhb_contact_detail
            where address_id = xpa.address_id
              and contact_type = 'Non Secure Email')
    UNION
    select 'Secure Email' AS CONTACT_TYPE
    ,      NULL as email_format
    ,      NULL as pager_net
    ,      stg.opp_email_secure as MAIL    
    ,      xpa.address_id
    ,      stg.opp_id
    ,      xcrt.crest_court_id
    ,      xcrt.court_id
    FROM xhbstg_opposer_dm stg
    ,    xhibit.xhb_court xcrt
    ,    xhibit.xhb_ref_prosecutor_agency xpa
    WHERE stg.crest_court_id = p_crest_court_id
    AND   stg.crest_court_id = xcrt.crest_court_id
    AND   xpa.crest_opposer_id = to_char(stg.opp_id)
    AND   xcrt.court_id = xpa.court_id
    AND   stg.opp_email_secure IS NOT NULL
    AND   NVL(stg.xhibit_etl_status,'N') not in ('I', 'U') 
    AND   stg.xhibit_enrich_date is NULL 
    AND   NOT exists
          (select 'X' from xhibit.xhb_contact_detail
            where address_id = xpa.address_id
              and contact_type = 'Secure Email')    
    ;

    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    v_opp_id                 xhbstg_solicitor_firm_dm.sof_id%TYPE;
    v_rpa_upd_rows           NUMBER := 0;
    v_rpa_upd_status         CHAR(1) := 'N';
    v_count_number_of_rows   NUMBER := 0;
    v_unprocessed_cnt        NUMBER := 0;
    v_total_row_count        NUMBER := 0;

BEGIN

    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of updating inserting data to XHB_CONTACT_DETAIL from xhbstg_opposer_dm from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);

    insert_dm_log (p_crest_court_id     => p_crest_court_id
                 ,p_action_name        => 'update_xhb_rpa_email_with_crest- CTX-3007'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHB_CONTACT_DETAIL - Starting process of updating existing rows for new columns with the required data from CREST'
                 ,p_err_row_count      =>  NULL
                 ,p_success_row_count  =>  NULL
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );

    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of adding email detail from existing row in xhb_prosecutor_agency for new columns with the required data from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places

    OPEN ref_pro_agency_cur;
    LOOP
    FETCH ref_pro_agency_cur BULK COLLECT INTO xhbstg_rpa_email_dm_tt LIMIT g_bulk_limit;

     IF xhbstg_rpa_email_dm_tt IS NOT NULL AND xhbstg_rpa_email_dm_tt.COUNT > 0 THEN  -- prevents numeric or value error later if nothing in the array

            v_total_row_count := v_total_row_count + xhbstg_rpa_email_dm_tt.COUNT;  -- we want to count number of cases found as we collect them

            FOR i IN xhbstg_rpa_email_dm_tt.FIRST .. xhbstg_rpa_email_dm_tt.LAST
             LOOP
     
             BEGIN -- CTX-3007 Begin block to include exception inside loop
                insert_dm_log (p_crest_court_id     => p_crest_court_id
                             ,p_action_name        => 'update_xhb_rpa_email_with_crest- CTX-3007'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'XHB_CONTACT_DETAIL processing CREST OPPOSER ID - '||
                                                       xhbstg_rpa_email_dm_tt(i).opp_id||
                                                       ' Contact Type : '||xhbstg_rpa_email_dm_tt(i).contact_type||
                                                       ' Mail : '||xhbstg_rpa_email_dm_tt(i).MAIL||
                                                       ' Email Format : ' ||xhbstg_rpa_email_dm_tt(i).email_format||
                                                       ' Pager Net : '    ||xhbstg_rpa_email_dm_tt(i).pager_net||
                                                       ' Address ID : '   ||xhbstg_rpa_email_dm_tt(i).address_id
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  =>  NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('UPDATING XHB_CONTACT_DETAIL - processing CREST OPPOSER OPP id - '||
                                                       xhbstg_rpa_email_dm_tt(i).opp_id||
                                                       ' Contact Type : '||xhbstg_rpa_email_dm_tt(i).contact_type||
                                                       ' Mail : '||xhbstg_rpa_email_dm_tt(i).MAIL||
                                                       ' Email Format : ' ||xhbstg_rpa_email_dm_tt(i).email_format||
                                                       ' Pager Net : '    ||xhbstg_rpa_email_dm_tt(i).pager_net||
                                                       ' Address ID : '   ||xhbstg_rpa_email_dm_tt(i).address_id
                                                       );

                -- Insert the rows to xhb_contact_detail with data from XHBSTG_SOLICITOR_FIRM_DM
                INSERT INTO xhibit.xhb_contact_detail (CONTACT_ID
                                                      ,CONTACT_TYPE
                                                      ,CONTACT_VALUE
                                                      ,EMAIL_FORMAT
                                                      ,PAGER_NET
                                                      ,ADDRESS_ID)
                VALUES  (xhibit.xhb_contact_detail_seq.nextval
                        ,xhbstg_rpa_email_dm_tt(i).contact_type
                        ,xhbstg_rpa_email_dm_tt(i).MAIL
                        ,xhbstg_rpa_email_dm_tt(i).EMAIL_FORMAT
                        ,xhbstg_rpa_email_dm_tt(i).PAGER_NET
                        ,xhbstg_rpa_email_dm_tt(i).ADDRESS_ID
                        );

    v_rpa_upd_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3007:UPDATING XHBSTG_OPPOSER_DM - FOR CREST_COURT_ID = '||xhbstg_rpa_email_dm_tt(i).crest_court_id||'address_id : '||xhbstg_rpa_email_dm_tt(i).address_id||' opp_id : '||xhbstg_rpa_email_dm_tt(i).opp_id);

     -- Update xhbstg_opposer_dm table for the rows processed
         IF v_rpa_upd_rows > 0 THEN
            v_rpa_upd_status := 'I'; -- XHIBIT TABLE Inserted
         ELSIF v_rpa_upd_rows = 0 THEN
            v_rpa_upd_status := 'N'; -- No Action Performed on XHIBIT table
         END IF ;

         insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'update_xhb_rpa_email_with_crest- CTX-3007'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_OPPOSER_DM - FOR CREST_COURT_ID = '||xhbstg_rpa_email_dm_tt(i).crest_court_id||
                                                       ' Opp ID : ' ||xhbstg_rpa_email_dm_tt(i).opp_id||
                                                       ' Contact Type : '||xhbstg_rpa_email_dm_tt(i).contact_type||
                                                       ' Mail : '||xhbstg_rpa_email_dm_tt(i).MAIL||
                                                       ' Email Format : ' ||xhbstg_rpa_email_dm_tt(i).email_format||
                                                       ' Pager Net : '    ||xhbstg_rpa_email_dm_tt(i).pager_net||
                                                       ' Address ID : '   ||xhbstg_rpa_email_dm_tt(i).address_id||
                                                       ' row with ETL_STATUS '||v_rpa_upd_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_rpa_upd_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


                -- Update xhbstg_opposer_dm table for the rows processed
                UPDATE xhbstg_opposer_dm xopp
                SET    xopp.xhibit_etl_date   = SYSDATE
                      ,xopp.xhibit_court_id   = v_xhibit_court_id
                      ,xopp.xhibit_enrich_date = SYSDATE
                      ,xopp.xhibit_etl_status = v_rpa_upd_status --Updated
                WHERE  xopp.crest_court_id   =  p_crest_court_id
                AND   xopp.opp_id = xhbstg_rpa_email_dm_tt(i).opp_id
                ;

            DBMS_OUTPUT.PUT_LINE(' ');
            DBMS_OUTPUT.PUT_LINE('CTX-3007:update_xhb_rpa_with_crest - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);
 
      EXCEPTION
       WHEN OTHERS THEN
            v_err_message := SQLERRM;
         --   ROLLBACK; -- CTX-3007 - Continue processing remaining records after logging in the error for the failed row
            DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_rpa_with_crest for CREST_COURT_ID : '||p_crest_court_id||' - '||SUBSTR(v_err_message,1,120));

         insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'update_xhb_rpa_email_with_crest- CTX-3007'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'update_xhb_rpa_email_with_crest - Error processing court_id: '||p_crest_court_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );

            UPDATE xhbstg_opposer_dm xopp
            SET    xopp.xhibit_etl_date   = SYSDATE
                  ,xopp.xhibit_court_id   = v_xhibit_court_id
                  ,xopp.xhibit_enrich_date = SYSDATE
                  ,xopp.xhibit_etl_status = 'X' -- Error
                  ,xopp.xhibit_etl_err_message = v_err_message
            WHERE  xopp.crest_court_id   =  p_crest_court_id
            AND    xopp.opp_id = v_opp_id;

         END; -- CTX-3007 End Block including Exception
        END LOOP;

      COMMIT; ---Commit after every successful processing BULK COLLECT of rows

        v_count_number_of_rows := v_count_number_of_rows + xhbstg_rpa_email_dm_tt.COUNT;

     END IF;

    EXIT WHEN ref_pro_agency_cur%NOTFOUND;

    END LOOP;
    CLOSE ref_pro_agency_cur;


     insert_dm_log (p_crest_court_id     => p_crest_court_id
                   ,p_action_name        => 'update_xhb_rpa_email_with_crest- CTX-3007'
                   ,p_run_time           =>  sysdate
                   ,p_log_msg_type       =>  'I' -- Information
                   ,p_log_msg            =>  'XHBSTG_OPPOSER_DM : Processed '||v_count_number_of_rows||' successfully!'
                   ,p_err_row_count      => NULL
                   ,p_success_row_count  => v_count_number_of_rows
                   ,p_last_updated_by    => 'DATA MIGRATION'
                   ,p_created_by         => 'DATA MIGRATION'
                   );

      -- Update xhbstg_opposer_dm table for the rows NOT processed
      UPDATE xhbstg_opposer_dm xl
      SET    xl.xhibit_etl_date   = SYSDATE
            ,xl.xhibit_court_id   = v_xhibit_court_id
            ,xl.xhibit_enrich_date = SYSDATE
            ,xl.xhibit_etl_status = 'N' -- Not processed
      WHERE  xl.crest_court_id   =  p_crest_court_id
        AND  xl.xhibit_etl_status is NULL
        AND  xl.xhibit_enrich_date  is NULL
        AND  xl.xhibit_etl_date  is NULL
        AND  xl.xhibit_court_id is NULL;

     v_unprocessed_cnt := SQL%ROWCOUNT;

     insert_dm_log (p_crest_court_id     => p_crest_court_id
                 ,p_action_name        => 'update_xhb_rpa_email_with_crest- CTX-3007'
                 ,p_run_time           =>  sysdate
                 ,p_log_msg_type       =>  'I' -- Information
                 ,p_log_msg            =>  'XHBSTG_OPPOSER_DM : updating xhbstg_opposer_dm with ETL_STATUS = N  where rows NOT processed for CREST Court id  '||p_crest_court_id
                 ,p_err_row_count      => NULL
                 ,p_success_row_count  => v_unprocessed_cnt
                 ,p_last_updated_by    => 'DATA MIGRATION'
                 ,p_created_by         => 'DATA MIGRATION'
                 );

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3007:XHBSTG_OPPOSER_DM processed '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

    DBMS_OUTPUT.PUT_LINE('XHB_CONTACT_DETAIL - Updated for CREST_COURT_ID : '||p_crest_court_id||' successfully!');

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK; -- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_xhb_rpa_with_crest for CREST_COURT_ID : '||p_crest_court_id||' - '||SUBSTR(v_err_message,1,120));

         insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'update_xhb_rpa_email_with_crest- CTX-3007'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'update_xhb_rpa_email_with_crest - Error processing court_id: '||p_crest_court_id||' Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
             COMMIT; -- Commit when running for real

END update_xhb_rpa_mail_with_crest;

/**
  * NAME       : parse_xhbstg_courtroom_day
  * DESCRIPTION: CTX-3575 - Parse XHBSTG_COURTROOM_DAY_DM - Sec 4.2.1 List Information Update
  *              add missing XHBSTG_LISTS_DM  records for those sitting_on_list where parent Lists are Deleted in CREST
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * 04/02/2019   S Sethuraman - CTX-3644 - parse_xhbstg_courtroom_day_dm - Filter Condition for crest_court_id added in CURSOR SQL when when XHBSTG_LISTS
  * 13/02/2019   S Sethuraman - CTX-3705 - recreate missing courtroom_day_dm records for orphan case_hearing_day_dm records
  * 20/02/2019   S Sethuraman - CTX-3731 - add missing crest_court_id in subquery in the COURTROOM_DAY CURSOR SQL
  * 26/02/2019   S Sethuraman  - CTX-3778 - ignore records with NULL LIST DATES when adding missing CASE_HEARING_DAY_DM
  * 27/02/2019   D Kinder      - CTX-3772 - DATA CLEANSE START_TIME Column in COURTROOM_DAY_DM
  * 28/02/2019   S Sethuraman  - CTX-3779 - ADD courtroom_day_dm records where case_hearing_day.ctd_id is NULL and also add missing lists records where courtroom_day list_type is other than D as well
  * 06/03/2019   S Sethuraman  - CTX-3810 - DATA CLEANSE TIME_MARKING in CASE_HEARING_DAY_DM
  * 06/03/2019   S Sethuraman  - CTX-3814 - parse_xhbstg_courtroom_day_dm - START_TIME Time before mignight to be handled i.e. 00:15 am tobe set as 12:15 am
  * 14/03/2019   S Sethuraman  - CTX-3855 -  parse_xhbstg_courtroom_day and upd_xhb_sitting_on_list procedures updated to address SITTING_ON_LIST / CASE_ON_LIST Duplicates issue -
  * 26/03/2019   S Sethuraman   - CTX-3894 - parse_xhbstg_courtroom_day updated to cater for more data cleansing of START_TIME column in COURTROOM_DAY_DM
  * 28/03/2019   S Sethuraman   - CTX-3913 - PARSE_XHBSTG_COURTROOM_DAY_DM updated to mark duplicate COURTROOM_DAY_DM CREST entries
  * 01/04/2019   S Sethuraman   - CTX-3934   - PARSE_XHBSTG_COURTROOM_DAY_DM - START_TIME - DOUBLE OCCURANCES OF SPECIAL CHARACTERS CAUSING ISSUE IN TRANSFORMAtion - set to NULL 
  * 23/04/2019   S Sethuraman  - CTX-4138 - CTX-4138 : parse_xhbstg_courtroom_day -  Birmingham issues - START_TIME needs to be TIMMED in all usages when transforming data
  * 30/04/2019   S Sethuraman  - CTX-4166 : parse_xhbstg_courtroom_day - FIX incorrect setting of jud_seq_no  where 1 to 9 already exists - assume default 1
  * 14/05/2019   S Sethuraman  - CTX-     : Parse procedure - duplicate marking process for dummy sites to include missing courtrooms check (ctx-4163 change)
  * 28/05/2019   S Sethuraman  - CTX-     : BRISTOL DR run issue - PARSE_XHB_COURTROOM_DAY_DM : CASE_ON_LIST "Hour must be between 1 and 12" -
  * 13/06/2019   S Sethuraman  - CTX-     : re-run etl1 issue - include xhibit_status is NULL condition when marking duplicates in COURTROOM_DAY
  **/
PROCEDURE parse_xhbstg_courtroom_day(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

  
 TYPE xhb_crd_rec IS RECORD
    ( crest_court_id             xhbstg_courtroom_day_dm.crest_court_id%type
     ,ctd_id                     xhbstg_courtroom_day_dm.ctd_id%type
     ,jp1_name                   xhbstg_courtroom_day_dm.jp1_name%type
     ,jud_id                     xhbstg_courtroom_day_dm.jud_id%type
     ,jp2_name                   xhbstg_courtroom_day_dm.jp2_name%type
     ,jp4_name                   xhbstg_courtroom_day_dm.jp4_name%type
     ,sitting_note               xhbstg_courtroom_day_dm.sitting_note%type
     ,list_type                  xhbstg_courtroom_day_dm.list_type%type
     ,commit_flag                xhbstg_courtroom_day_dm.commit_flag%type
     ,start_time                 xhbstg_courtroom_day_dm.start_time%type
     ,old_ctd_id                 xhbstg_courtroom_day_dm.old_ctd_id%type
     ,jud_seq_no                 xhbstg_courtroom_day_dm.jud_seq_no%type
     ,jud_sit_type               xhbstg_courtroom_day_dm.jud_sit_type%type
     ,jp3_name                   xhbstg_courtroom_day_dm.jp3_name%type
     ,list_date                  xhbstg_courtroom_day_dm.list_date%type
     ,courtroom_no               xhbstg_courtroom_day_dm.courtroom_no%type
     ,site_code                  xhbstg_courtroom_day_dm.site_code%type
     ,jud_sit_ind                xhbstg_courtroom_day_dm.jud_sit_ind%type
    );
    
 TYPE xhb_lst_rec IS RECORD
    ( 
      crest_court_id               xhbstg_lists_dm.crest_court_id%TYPE
     ,start_date                   xhbstg_lists_dm.start_date%TYPE
     ,end_date                     xhbstg_lists_dm.end_date%TYPE
     ,list_type                    xhbstg_lists_dm.list_type%TYPE
     ,date_published               xhbstg_lists_dm.date_published%TYPE
     ,cc_ind                       xhbstg_lists_dm.cc_ind%TYPE
     ,list_status                  xhbstg_lists_dm.list_status%TYPE
     ,edition_no                   xhbstg_lists_dm.edition_no%TYPE
     ,fcl_start_time               xhbstg_lists_dm.fcl_start_time%TYPE
    );

    TYPE xhb_crd_type IS TABLE OF xhb_crd_rec;
    xhb_crd_tt  xhb_crd_type;
    xhb_crd_tt_null  xhb_crd_type;
    
    TYPE xhb_lst_type IS TABLE OF xhb_lst_rec;
    xhb_lst_tt  xhb_lst_type;

   -- CURSOR 1 : LOCATE MISSING COURTROOM_DAY_DM RECORDS FOR ORPHAN CASE_HEARING_DAY_DM RECORDS
   -- CTD_ID IS NOT NULL - covers all LIST_TYPES othan than X
   CURSOR cur_crest_crd_details IS
   SELECT DISTINCT chd.crest_court_id, -- DISTINCT to avoid redundant dates as it is many to 1 between case_hearing_day and courtroom_day
           chd.ctd_id,
           NULL as JP1_NAME,
           NULL as JUD_ID,
           NULL as JP2_NAME,
           NULL as JP4_NAME,
           'Created by Data Migration run' as SITTING_NOTE,
           chd.list_type,
           NULL as COMMIT_FLAG,
           NULL as  START_TIME,
           NULL as OLD_CTD_ID,
           -- lookup xhbstg_courtroom_day_dm and find non existing jud_seq_no
           --  for the list_date, list_type and courtroom_no = 1 
           --  which is between 1 and 9 
           -- so instead finding max + 1 
           -- find a suitable number between 1 and 9 
           -- which is not allocated 
           -- if no records found this will be 1
           --  JUD_SEQNO is generated inside the FOR LOOP now - CTX-3855
 /*          nvl((select min(column_value) from table(sys.odcinumberlist(1,2,3,4,5,6,7,8,9))
              where column_value NOT IN
                    (select nvl(cdd.jud_seq_no,0) from xhbstg_courtroom_day_dm cdd
                      where cdd.crest_court_id = p_crest_court_id and -- CTX-3731
                            cdd.list_date = chd.list_date and
                            cdd.list_type = chd.list_type and
                            cdd.courtroom_no = 1 )),1) */
           NULL as JUD_SEQ_NO,
           NULL as JUD_SIT_TYPE,
           NULL as JP3_NAME,
           chd.list_date,
           1 as COURTROOM_NO,
           chd.chd_site_code as site_code,
           NULL as JUD_SIT_IND
    from  data_mig.xhbstg_case_hearing_day_dm chd
    where chd.crest_court_id = p_crest_court_id and
          NVL(chd.xhibit_etl_status,'N') not in ('I', 'U') and
          chd.xhibit_enrich_date is NULL  and
          chd.ctd_id is NOT NULL and 
          chd.list_date is NOT NULL and -- CTX-3778
          nvl(chd.list_type,'X') != 'X' and
          NOT exists 
          (select 'X' from data_mig.xhbstg_courtroom_day_dm stgl
            where stgl.crest_court_id = chd.crest_court_id and 
                  stgl.ctd_id = chd.ctd_id);
  
 ------------ CTX-3779 new CURSOR
 
 -- CURSOR 2 : LOCATE MISSING COURTROOM_DAY_DM RECORDS FOR ORPHAN CASE_HEARING_DAY_DM RECORDS
 -- CTD_ID IS NULL - covers all LIST_TYPES other than X
    CURSOR cur_crest_crd_ctd_null IS
   SELECT DISTINCT chd.crest_court_id, -- DISTINCT to avoid redundant dates as it is many to 1 between case_hearing_day and courtroom_day
           NULL as ctd_id, -- to be generated
           NULL as JP1_NAME,
           NULL as JUD_ID,
           NULL as JP2_NAME,
           NULL as JP4_NAME,
           'Created by Data Migration run' as SITTING_NOTE,
           chd.list_type,
           NULL as COMMIT_FLAG,
           NULL as  START_TIME,
           NULL as OLD_CTD_ID,
           -- lookup xhbstg_courtroom_day_dm and find non existing jud_seq_no
           --  for the list_date, list_type and courtroom_no = 1 
           --  which is between 1 and 9 
           -- so instead finding max + 1 
           -- find a suitable number between 1 and 9 
           -- which is not allocated 
           -- if no records found this will be 1
           -- jud_seno is generated inside the FOR loop now - CTX-3855
/*           nvl((select min(column_value) from table(sys.odcinumberlist(1,2,3,4,5,6,7,8,9))
              where column_value NOT IN
                    (select nvl(cdd.jud_seq_no,0) from xhbstg_courtroom_day_dm cdd
                      where cdd.crest_court_id = p_crest_court_id and -- CTX-3731
                            cdd.list_date = chd.list_date and
                            cdd.list_type = chd.list_type and
                            cdd.courtroom_no = 1 )),1)*/
           NULL as JUD_SEQ_NO,
           NULL as JUD_SIT_TYPE,
           NULL as JP3_NAME,
           chd.list_date,
           1 as COURTROOM_NO,
           chd.chd_site_code as site_code,
           NULL as JUD_SIT_IND
    from  data_mig.xhbstg_case_hearing_day_dm chd
    where chd.crest_court_id = p_crest_court_id and
          NVL(chd.xhibit_etl_status,'N') not in ('I', 'U') and
          chd.xhibit_enrich_date is NULL  and
          chd.ctd_id is NULL and  -- CTX-3779
          chd.list_date is NOT NULL and -- CTX-3778
          nvl(chd.list_type,'X') != 'X' and
          NOT exists 
          (select 'X' from data_mig.xhbstg_courtroom_day_dm stgl
            where stgl.crest_court_id = chd.crest_court_id and 
                  stgl.list_date = chd.list_date and
                  stgl.list_type = chd.list_type and
                  stgl.site_code = chd.chd_site_code);
  
    -- CURSOR 3 : LOCATE MISSING LISTS_DM RECORDS FOR ORPHAN COURTROOM_DAY_DM RECORDS                     
    CURSOR cur_crest_lst_details IS
    -- list_type = 'D' -- Union eliminates duplicates, DISTINCT makes it obvious
   SELECT DISTINCT cdd.crest_court_id, -- DISTINCT to avoid redundant dates as it is many to 1 between COURTROOM and LISTS
           cdd.list_date start_date,
           cdd.list_date end_date,
           cdd.list_type,
           NULL as date_published, 
           'CR' as CC_IND,
           'FINAL' as list_status,
           '1' as edition_no,
           NULL as FCL_START_TIME
    from  data_mig.xhbstg_courtroom_day_dm cdd
    where cdd.crest_court_id = p_crest_court_id and
          NVL(cdd.xhibit_etl_status,'N') not in ('I', 'U') and
          cdd.xhibit_enrich_date is NULL  and
          cdd.list_type = 'D' and
          NOT exists 
          (select 'X' from data_mig.xhbstg_lists_dm stgl
            where stgl.crest_court_id = cdd.crest_court_id and -- CTX-3644
                  cdd.list_date = stgl.start_date and 
                  cdd.list_type = stgl.list_type)
     UNION  -- LIST_TYPE <> 'D'
        SELECT DISTINCT cdd.crest_court_id,
           cdd.list_date start_date,
           cdd.list_date end_date,
           cdd.list_type,
           NULL as date_published, 
           'CR' as CC_IND,
           'FINAL' as list_status,
           '1' as edition_no,
           NULL as FCL_START_TIME
    from  data_mig.xhbstg_courtroom_day_dm cdd
    where cdd.crest_court_id = p_crest_court_id and
          NVL(cdd.xhibit_etl_status,'N') not in ('I', 'U') and
          cdd.xhibit_enrich_date is NULL  and
          cdd.list_type <> 'D' and
          NOT exists 
          (select 'X' from data_mig.xhbstg_lists_dm stgl
            where stgl.crest_court_id = cdd.crest_court_id and -- CTX-3644
                  cdd.list_date between stgl.start_date and stgl.end_date and
                  cdd.list_type = stgl.list_type);
 

    v_count_number_of_rows   NUMBER := 0;
    v_cdd_upd_rows           NUMBER := 0;
    v_list_date              data_mig.xhbstg_courtroom_day_dm.list_date%TYPE;
    v_list_type              data_mig.xhbstg_courtroom_day_dm.list_type%TYPE;
    v_err_message            xhbstg_lists_dm.xhibit_etl_err_message%TYPE;
    v_list_id                xhbstg_lists_dm.lst_id%TYPE;
    v_ctd_id                 xhbstg_courtroom_day_dm.ctd_id%type;
    v_new_ctd_id             xhbstg_courtroom_day_dm.ctd_id%type; -- CTX-3779
    v_jud_seqno NUMBER := 0; -- CTX-3855
    v_courtroom_no NUMBER := 0; -- CTX-3855
    v_site_code xhbstg_courtroom_day_dm.site_code%type;
        
BEGIN

-- CTX-3934   UPDATE START_TIME to NULL where START_TIME is not in format [0-9]*(:|.|,|-|;|/|)[0-9]*.*

    FOR a IN (
          SELECT CREST_COURT_ID, CTD_ID, START_TIME
            FROM XHBSTG_COURTROOM_DAY_DM
            WHERE CREST_COURT_ID = P_CREST_COURT_ID AND
                          XHIBIT_ETL_STATUS is NULL AND
                          START_TIME is NOT NULL AND
                          NOT(REGEXP_LIKE(REPLACE(UPPER(TRIM(START_TIME)),'O','0'),'^[0-9]*(\:|\/|\-|\.|\,|\;)[0-9].*$'))
        ORDER BY CTD_ID ASC
        )
        LOOP
          BEGIN
              DBMS_OUTPUT.PUT_LINE ('Updating CTD_ID '|| a.CTD_ID || ' old START_TIME : ' ||  a.START_TIME||' setting to NULL');
    
              --Populate DM LOG table with i.CREST_COURT_ID,i.CTD_ID, i.START_TIME  and message record being fixed
               insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'parse_xhbstg_courtroom_day'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM - Updating CTD_ID '|| a.CTD_ID || ' START_TIME : ' ||  a.START_TIME||' to NULL'
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
   
                        
              UPDATE XHBSTG_COURTROOM_DAY_DM
                SET START_TIME = NULL
                WHERE CREST_COURT_ID = a.CREST_COURT_ID AND
                            CTD_ID = a.CTD_ID AND
                            XHIBIT_ETL_STATUS is NULL;                       
          END;
        END LOOP;
        COMMIT;
        


-- CTX- 3934  END


 -- CTX-3772 BEFORE CREATING/LOOPING CURSORS TO MIGRATE DATA, 
  -- PERFORM DATA CLEANSE ON XHBSTG_COURTROOM_DAY_DM.START_TIME 
  -- TO CLEANSE INVALID TIMES
    --BEGIN    
      FOR i IN (
          SELECT CREST_COURT_ID, CTD_ID, START_TIME
            FROM XHBSTG_COURTROOM_DAY_DM
            WHERE CREST_COURT_ID = P_CREST_COURT_ID AND
                          XHIBIT_ETL_STATUS is NULL AND
                          START_TIME is NOT NULL AND
                         ( INSTR(UPPER(TRIM(START_TIME)),'O') <> 0  OR
                        INSTR(UPPER(TRIM(START_TIME)),'.') <> 0 OR
                        INSTR(UPPER(TRIM(START_TIME)),';') <> 0 OR -- CTX-3894
                         INSTR(UPPER(TRIM(START_TIME)),'/') <> 0 OR -- CTX-3894
                         INSTR(UPPER(TRIM(START_TIME)),',') <> 0 OR -- CTX-3894
                          INSTR(UPPER(TRIM(START_TIME)),'-') <> 0 OR -- CTX-3894
                        INSTR(UPPER(TRIM(START_TIME)),':AM') <> 0 OR
                        INSTR(UPPER(TRIM(START_TIME)),':PM') <> 0 OR
                        INSTR(UPPER(TRIM(START_TIME)),'.AM') <> 0 OR
                        INSTR(UPPER(TRIM(START_TIME)),'00:') <> 0 OR
                        INSTR(UPPER(TRIM(START_TIME)),'00.') <> 0 OR
                        INSTR(UPPER(TRIM(START_TIME)),'00;') <> 0 OR -- CTX-3894
                        INSTR(UPPER(TRIM(START_TIME)),'00/') <> 0 OR -- CTX-3894
                        SUBSTR(UPPER(TRIM(START_TIME)),1,2) = '0:' or -- ctx-3894
                        SUBSTR(UPPER(TRIM(START_TIME)),1,2) = '0.' OR -- CTX-3894
                        SUBSTR(UPPER(TRIM(START_TIME)),1,2) = '0;' OR -- CTX-3894
                        SUBSTR(UPPER(TRIM(START_TIME)),1,2) = '0/' or -- CTX-3894
                        SUBSTR(UPPER(TRIM(START_TIME)),1,2) = '0,' or -- CTX-3894
                        INSTR(UPPER(TRIM(START_TIME)),'.PM') <> 0)
        ORDER BY CTD_ID ASC
        )
        LOOP
          BEGIN
              DBMS_OUTPUT.PUT_LINE ('Updating CTD_ID '|| i.CTD_ID || ' old START_TIME : ' ||  i.START_TIME);
    
              --Populate DM LOG table with i.CREST_COURT_ID,i.CTD_ID, i.START_TIME  and message record being fixed
               insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'parse_xhbstg_courtroom_day'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM - Updating CTD_ID '|| i.CTD_ID || ' Correcting START_TIME : ' ||  i.START_TIME
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
    
            /* CTX-3894 */
            IF (SUBSTR(UPPER(TRIM(i.START_TIME)),1,2) = '0:' or -- ctx-3894
                        SUBSTR(UPPER(TRIM(i.START_TIME)),1,2) = '0.' OR -- CTX-3894
                        SUBSTR(UPPER(TRIM(i.START_TIME)),1,2) = '0;' OR -- CTX-3894
                        SUBSTR(UPPER(TRIM(i.START_TIME)),1,2) = '0/' OR -- CTX-3894
                        SUBSTR(UPPER(TRIM(i.START_TIME)),1,2) = '0-' OR -- CTX-3894
                        SUBSTR(UPPER(TRIM(i.START_TIME)),1,2) = '0,' ) -- CTX-3894
            THEN
           
                  UPDATE XHBSTG_COURTROOM_DAY_DM
                SET START_TIME = '00:'||SUBSTR(upper(TRIM(start_time)),3) -- CTX-3814 -- CTX-4138
                WHERE CREST_COURT_ID = i.CREST_COURT_ID AND
                            CTD_ID = i.CTD_ID AND
                            XHIBIT_ETL_STATUS is NULL; 
                       
                       
            END IF;
            -- CTX-3894
                        
              UPDATE XHBSTG_COURTROOM_DAY_DM
                SET START_TIME = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(UPPER(TRIM(START_TIME)),'O','0'),'.',':'),';',':'),'-',':'),'/',':'),',',':'),'00:','12:'),':AM',' AM'),':PM',' PM'),'00AM','00 AM'),'00PM','00 PM') -- CTX-3814
                WHERE CREST_COURT_ID = i.CREST_COURT_ID AND
                            CTD_ID = i.CTD_ID AND
                            XHIBIT_ETL_STATUS is NULL;                       
          END;
        END LOOP;
        COMMIT;
        
 
        
      FOR j in (
          SELECT CREST_COURT_ID,CTD_ID,START_TIME
            FROM  XHBSTG_COURTROOM_DAY_DM
            WHERE 
              CREST_COURT_ID = p_crest_court_id 
              AND (CTD_ID) IN (
                SELECT CTD_ID 
                FROM XHBSTG_COURTROOM_DAY_DM 
                WHERE CREST_COURT_ID = p_crest_court_id AND
                     XHIBIT_ETL_STATUS is NULL AND
                      START_TIME is NOT NULL 
                MINUS
                SELECT  CTD_ID 
                FROM XHBSTG_COURTROOM_DAY_DM 
                WHERE CREST_COURT_ID = p_crest_court_id AND
                     XHIBIT_ETL_STATUS is NULL AND
                      START_TIME is NOT NULL AND
                (( REGEXP_LIKE(REPLACE(TRIM(UPPER(START_TIME)),'O','0'),'^[0-9][0-9]*(\.|\:)[0-9][0-9]*[ ]*(AM|PM)$')) 
                OR (REGEXP_LIKE(REPLACE(TRIM(UPPER(START_TIME)),'O','0'),'^[0-9][0-9]*(\.|\:)[0-9][0-9]* (.|:)*(AM|PM)$'))) 
              )
            )
            LOOP
              BEGIN
              
              IF (NOT( REGEXP_LIKE(REPLACE(TRIM(UPPER(j.START_TIME)),'O','0'),'^[0-9][0-9][0-9]*[ ]*(AM|PM)$'))) 
              THEN 
                  DBMS_OUTPUT.PUT_LINE ('Updating CTD_ID '|| j.CTD_ID || ' Setting invalid START_TIME to NULL : ' || j.START_TIME);
                  
                  --Populate DM LOG table with j.CREST_COURT_ID,j.CTD_ID, j.START_TIME  and message record being fixed
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'parse_xhbstg_courtroom_day'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM - Updating CTD_ID '|| j.CTD_ID || ' Setting invalid START_TIME to NULL : ' ||  j.START_TIME
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  => NULL
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
                               
              UPDATE XHBSTG_COURTROOM_DAY_DM
                SET START_TIME = NULL
                WHERE CREST_COURT_ID = j.CREST_COURT_ID AND
                CTD_ID = j.CTD_ID AND
                XHIBIT_ETL_STATUS is NULL;
            END IF;    
            END;
          END LOOP;
        COMMIT;
        
       -- CTX-3894
       
            FOR k in (
          SELECT CREST_COURT_ID,CTD_ID,START_TIME
            FROM  XHBSTG_COURTROOM_DAY_DM
            WHERE 
              CREST_COURT_ID = p_crest_court_id 
               AND XHIBIT_ETL_STATUS is NULL AND
                      START_TIME is NOT NULL AND
                (( REGEXP_LIKE(REPLACE(TRIM(UPPER(START_TIME)),'O','0'),'^[0-9][0-9][0-9]*[ ]*(AM|PM)$'))))
            LOOP
              BEGIN
                   DBMS_OUTPUT.PUT_LINE ('Updating CTD_ID '|| k.CTD_ID || ' correcting invalid START_TIME : ' || k.START_TIME);
                  
                  --Populate DM LOG table with j.CREST_COURT_ID,j.CTD_ID, j.START_TIME  and message record being fixed
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'parse_xhbstg_courtroom_day'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM - Updating CTD_ID '|| k.CTD_ID || ' correcting invalid START_TIME : ' ||  k.START_TIME
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  => NULL
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
            
              /* CTX-3894 */
            IF (REGEXP_LIKE(REPLACE(TRIM(UPPER(k.START_TIME)),'O','0'),'^[0-9][0-9][0-9][ ]*(AM|PM)$'))
            THEN                   
              UPDATE XHBSTG_COURTROOM_DAY_DM
                SET START_TIME = '0'||substr(TRIM(start_time),1,1)||':'||substr(UPPER(TRIM(start_time)),2,2)||' AM' -- CTX-4138
                WHERE CREST_COURT_ID = k.CREST_COURT_ID AND
                CTD_ID = k.CTD_ID AND
                XHIBIT_ETL_STATUS is NULL;
             END IF;
             
             IF (REGEXP_LIKE(REPLACE(TRIM(UPPER(k.START_TIME)),'O','0'),'^[0-9][0-9][0-9][0-9][ ]*(AM|PM)$'))
            THEN                   
              UPDATE XHBSTG_COURTROOM_DAY_DM
                SET START_TIME = substr(TRIM(start_time),1,2)||':'||substr(upper(TRIM(start_time)),3,2)||' AM' -- CTX-4138
                WHERE CREST_COURT_ID = k.CREST_COURT_ID AND
                CTD_ID = k.CTD_ID AND
                XHIBIT_ETL_STATUS is NULL;
             END IF;
                 
            END;
          END LOOP;
        COMMIT;
        
       -- CTX-3894 
              
        --CTX-3894
        
         FOR l in (
          SELECT CREST_COURT_ID,CTD_ID,START_TIME
            FROM  XHBSTG_COURTROOM_DAY_DM
            WHERE 
              CREST_COURT_ID = p_crest_court_id 
               AND XHIBIT_ETL_STATUS is NULL AND
                      START_TIME is NOT NULL AND
                REGEXP_LIKE(TRIM(UPPER(START_TIME)),'^[0-9][0-9]\:[0-9][0-9][ ](AM|PM)$'))
            LOOP
              BEGIN
                   DBMS_OUTPUT.PUT_LINE ('Updating CTD_ID '|| l.CTD_ID || ' Correcting invalid START_TIME  : ' || l.START_TIME);
                  
                  --Populate DM LOG table with j.CREST_COURT_ID,j.CTD_ID, j.START_TIME  and message record being fixed
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'parse_xhbstg_courtroom_day'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM - Updating CTD_ID '|| l.CTD_ID || ' correcting invalid START_TIME : ' ||  l.START_TIME
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  => NULL
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
            
              /* CTX-3894 */
            IF (to_number(substr(l.start_time,1,2)) > 12)
            THEN                   
              UPDATE XHBSTG_COURTROOM_DAY_DM
                SET START_TIME = lpad(to_char(to_number(substr(upper(trim(l.start_time)),1,2))-12),2,'0')||':'||substr(upper(TRIM(start_time)),4) -- CTX-4138
                WHERE CREST_COURT_ID = l.CREST_COURT_ID AND
                CTD_ID = l.CTD_ID AND
                XHIBIT_ETL_STATUS is NULL;
             END IF;
                 
            END;
          END LOOP;
        COMMIT;
        
        -- CTX-3894

        -- END CTX-3772
 
  -- CTX-3810 BEFORE CREATING/LOOPING CURSORS TO MIGRATE DATA, 
  -- PERFORM DATA CLEANSE ON XHBSTG_CASE_HEARING_DAY_DM.TIME_MARKING
  -- TO CLEANSE INVALID TIMES
    --BEGIN    
      FOR i IN (
          SELECT CREST_COURT_ID, CHD_ID, TIME_MARKING
            FROM XHBSTG_CASE_HEARING_DAY_DM
            WHERE CREST_COURT_ID = P_CREST_COURT_ID AND
                          XHIBIT_ETL_STATUS is NULL AND
                          TIME_MARKING is NOT NULL AND
                         ( INSTR(UPPER(TRIM(TIME_MARKING)),'1O') <> 0  OR
                         INSTR(UPPER(TRIM(TIME_MARKING)),'.O') <> 0  OR
                         INSTR(UPPER(TRIM(TIME_MARKING)),':O') <> 0  OR                         
                        INSTR(UPPER(TRIM(TIME_MARKING)),'.') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),'00:') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),'00.') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),'OO:') <> 0 OR 
                        INSTR(UPPER(TRIM(TIME_MARKING)),'OO.') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),':OO') <> 0 OR                        
                        INSTR(UPPER(TRIM(TIME_MARKING)),'.OO') <> 0 OR                                                                       
                        INSTR(UPPER(TRIM(TIME_MARKING)),':AM') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),':PM') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),'A.M') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),'P.M') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),'.AM') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),'.PM') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),'AM.') <> 0 OR
                        INSTR(UPPER(TRIM(TIME_MARKING)),'PM.') <> 0)
        ORDER BY CHD_ID ASC
        )
        LOOP
          BEGIN
              DBMS_OUTPUT.PUT_LINE ('Updating CHD_ID '|| i.CHD_ID || ' old TIME_MARKING : ' ||  i.TIME_MARKING);
    
              --Populate DM LOG table with i.CREST_COURT_ID,i.CHD_ID, i.TIME_MARKING  and message record being fixed
               insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'parse_xhbstg_courtroom_day'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'XHBSTG_CASE_HEARING_DAY_DM - Updating CHD_ID '|| i.CHD_ID || ' Correcting TIME_MARKING : ' ||  i.TIME_MARKING
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );
    
              UPDATE XHBSTG_CASE_HEARING_DAY_DM
                SET TIME_MARKING =  REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(UPPER(TRIM(TIME_MARKING)),'1O','10'),'.',':'),':O',':0'),'O:','0:'),':0O',':00'),'O0:','00:'),'A:M','AM'),'P:M','PM'),':AM',' AM'),':PM',' PM'),'AM:','AM'),'PM:','PM'),'00:','12:')
                WHERE CREST_COURT_ID = i.CREST_COURT_ID AND
                            CHD_ID = i.CHD_ID AND
                            XHIBIT_ETL_STATUS is NULL;                       
          END;
        END LOOP;
        COMMIT;
        

        -- END CTX-3810

         -- CTX-Bristol Issue START
         -- DR Run showed BRISTOL Data TIME_MARKING IN CASE_HEARING_DAY_DM coing in as 13:45 pm, which needs to be corrected
       
         FOR m in (
                   SELECT CREST_COURT_ID, CHD_ID, TIME_MARKING
            FROM XHBSTG_CASE_HEARING_DAY_DM
            WHERE CREST_COURT_ID = P_CREST_COURT_ID AND
                          XHIBIT_ETL_STATUS is NULL AND
                          TIME_MARKING is NOT NULL AND
                        ( INSTR(UPPER(TRIM(TIME_MARKING)),'13:') <> 0  OR
                          INSTR(UPPER(TRIM(TIME_MARKING)),'14:') <> 0  OR
                          INSTR(UPPER(TRIM(TIME_MARKING)),'15:') <> 0  OR 
                          INSTR(UPPER(TRIM(TIME_MARKING)),'16:') <> 0  OR
                          INSTR(UPPER(TRIM(TIME_MARKING)),'17:') <> 0  OR
                          INSTR(UPPER(TRIM(TIME_MARKING)),'18:') <> 0  OR
                          INSTR(UPPER(TRIM(TIME_MARKING)),'19:') <> 0  OR
                          INSTR(UPPER(TRIM(TIME_MARKING)),'20:') <> 0  OR
                          INSTR(UPPER(TRIM(TIME_MARKING)),'21:') <> 0  OR
                          INSTR(UPPER(TRIM(TIME_MARKING)),'22:') <> 0  OR
                          INSTR(UPPER(TRIM(TIME_MARKING)),'23:') <> 0                                                                                                                                                              
                        )                           
                          order by chd_id)
            LOOP
              BEGIN
                   DBMS_OUTPUT.PUT_LINE ('Updating CHD_ID '|| m.CHD_ID || ' Correcting invalid TIME_MARKING  : ' ||m.TIME_MARKING);
                  
                  --Populate DM LOG table with m.CREST_COURT_ID,m.CHD_ID, m.TIME_MARKING  and message record being fixed
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'parse_xhbstg_courtroom_day'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'I' -- Information
                               ,p_log_msg            =>  'XHBSTG_CASE_HEARING_DAY_DM - Updating CHD_ID '|| m.CHD_ID || ' correcting invalid TIME_MARKING : ' ||  m.TIME_MARKING
                               ,p_err_row_count      =>  NULL
                               ,p_success_row_count  => NULL
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
            
              /* CTX- BRISTOL Issue */
             UPDATE XHBSTG_CASE_HEARING_DAY_DM
                SET TIME_MARKING =  REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(UPPER(TRIM(TIME_MARKING)),'13:','01:'),'14:','02:'),'15:','03:'),'16:','04:'),'17:','05:'),'18:','06:'),'19:','07:'),'20:','08:'),'21:','09:'),'22:','10:'),'23:','11:')
                WHERE CREST_COURT_ID = m.CREST_COURT_ID AND
                            CHD_ID = m.CHD_ID AND
                            XHIBIT_ETL_STATUS is NULL;                       

                 
            END;
          END LOOP;
          COMMIT;      


           -- CTX-Bristol Isse END 
           
           
          -- CTX_3705  START  for CTD_ID is NOT NULL
          
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM - Starting process of inserting  missing records into XHBSTG_COURTROOM_DAY_DM table for CREST Court : '||p_crest_court_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting missing rows into XHBSTG_COURTROOM_DAY_DM table');
   
      -- Here we are going to populate the rows in XHBSTG_COURTROOM_DAY_DM table that have been deleted in CREST
    OPEN cur_crest_crd_details;
    LOOP
    FETCH cur_crest_crd_details BULK COLLECT INTO xhb_crd_tt LIMIT g_bulk_limit;
   
    IF xhb_crd_tt IS NOT NULL AND xhb_crd_tt.COUNT > 0 THEN
   
        FOR i IN xhb_crd_tt.FIRST .. xhb_crd_tt.LAST LOOP
    
          BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

 -- Should the insert fail we want to capture the failing row in the exception block so hold them here before update
            v_list_date     := xhb_crd_tt(i).list_date;
            v_list_type     := xhb_crd_tt(i).list_type;
            v_ctd_id        := xhb_crd_tt(i).ctd_id;

            -- ctx-3855 start
            v_jud_seqno := 0; 
            v_courtroom_no := xhb_crd_tt(i).courtroom_no;
            
            -- check and assign jud_seq_no and courtroom_no 
            -- below checks if there are any records existing for the courtroom 1 with some jud_seq
            -- and finds a non existing jud_seqno between 1 and 9 and assigns
            -- if for courtroom 1 all 1 to 9 are present then checks for courtoom 2
            -- and assigns a non existing jud_seq no for courtroom 2
            -- CTX-4166 : When all 1 to 9 jud_seq_no exist assume default 1 
            BEGIN          
             select min(column_value) into v_jud_seqno
             from table(sys.odcinumberlist(1,2,3,4,5,6,7,8,9))
              where column_value NOT IN
                    (select nvl(cdd.jud_seq_no,0) from xhbstg_courtroom_day_dm cdd
                      where cdd.crest_court_id = p_crest_court_id and -- CTX-3731
                            cdd.list_date = xhb_crd_tt(i).list_date and
                            cdd.list_type = xhb_crd_tt(i).list_type and
                            cdd.site_code = xhb_crd_tt(i).site_code and
                            cdd.courtroom_no = 1);
    -- CTX-4166 If COURTROOM_NO 1 didnt fetch jud_seq_no try COURTROOM 2 and then default jud_seq_no to 1 
              if (v_jud_seqno = 0 OR v_jud_seqno is NULL) then
                  select nvl(min(column_value),1) into v_jud_seqno --  CTX-4166 NVL will assign 1 if NOT found
                  from table(sys.odcinumberlist(1,2,3,4,5,6,7,8,9))
                  where column_value NOT IN
                    (select nvl(cdd.jud_seq_no,0) from xhbstg_courtroom_day_dm cdd
                      where cdd.crest_court_id = p_crest_court_id and -- CTX-3731
                            cdd.list_date = xhb_crd_tt(i).list_date and
                            cdd.list_type = xhb_crd_tt(i).list_type and
                            cdd.site_code = xhb_crd_tt(i).site_code and
                            cdd.courtroom_no = 2);
                    v_courtroom_no := 2;
              end if;                  

             EXCEPTION WHEN OTHERS THEN
                  v_jud_seqno := 1;
             END;  
             -- ctx-3855 end                  
            
             DBMS_OUTPUT.PUT_LINE('v_list_date : '||v_list_date||', v_list_type : '||v_list_type||' ,v_ctd_id : '||v_ctd_id||' , p_crest_court_id '||p_crest_court_id);         
               
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTING XHBSTG_COURTROOM_DAY_DM - inserting  List_date - '||xhb_crd_tt(i).list_date||', list_type - '||xhb_crd_tt(i).list_type||', v_ctd_id - '||v_ctd_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('INSERTING XHBSTG_COURTROOM_DAY_DM - FOR CREST_COURT_ID = '||xhb_crd_tt(i).crest_court_id);
    
            
         -- INSERT missing records from XHBSTG_CASE_HEARING_DAY_DM into XHBSTG_COURTROOM_DAY_DM from CREST  
                     INSERT INTO  data_mig.xhbstg_courtroom_day_dm
                                  ( ctd_id
                                  , jp1_name 
                                  , jud_id   
                                  , jp2_name
                                  , jp4_name
                                  , sitting_note
                                  , list_type
                                  , commit_flag
                                  , start_time
                                  , old_ctd_id
                                  , jud_seq_no
                                  , jud_sit_type
                                  , jp3_name
                                  , list_date
                                  , courtroom_no
                                  , site_code
                                  , jud_sit_ind
                                  , crest_court_id
                                  )
                           VALUES 
                                  (xhb_crd_tt(i).ctd_id
                                  ,xhb_crd_tt(i).jp1_name
                                  ,xhb_crd_tt(i).jud_id
                                  ,xhb_crd_tt(i).jp2_name
                                  ,xhb_crd_tt(i).jp4_name
                                  ,xhb_crd_tt(i).sitting_note
                                  ,xhb_crd_tt(i).list_type
                                  ,xhb_crd_tt(i).commit_flag
                                  ,xhb_crd_tt(i).start_time
                                  ,xhb_crd_tt(i).old_ctd_id
                                  ,v_jud_seqno -- CTX-3855
                                  ,xhb_crd_tt(i).jud_sit_type
                                  ,xhb_crd_tt(i).jp3_name
                                  ,xhb_crd_tt(i).list_date
                                  ,v_courtroom_no -- CTX-3855
                                  ,xhb_crd_tt(i).site_code
                                  ,xhb_crd_tt(i).jud_sit_ind
                                  ,xhb_crd_tt(i).crest_court_id);      
                  
    
    v_cdd_upd_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3705:XHBSTG_COURTROOM_DAY_DM - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cdd_upd_rows);
    
        EXCEPTION
            WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN parse_xhbstg_courtroom_day / COURTTOOM_DAY_DM INSERT for CREST_COURT : '||p_crest_court_id||',list_date : '||v_list_date||' , list_type : '||v_list_type||' , v_ctd_id : '||v_ctd_id||'-'||SUBSTR(v_err_message,1,100));
        
                   insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                 ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'parse_xhbstg_courtroom_day - Error inserting list_date : '||v_list_date||' , list_type : '||', v_ctd_id : '||v_ctd_id||'- Error: '||SUBSTR(v_err_message,1,150)
                                 ,p_err_row_count      => NULL 
                                 ,p_success_row_count  => v_count_number_of_rows
                                 ,p_last_updated_by    => 'PRE DATA MIGRATION'
                                 ,p_created_by         => 'PRE DATA MIGRATION'
                                 );

        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_crd_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_crd_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_crd_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM : Inserted '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3705:XHBSTG_COURTROOM_DAY_DM inserted '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
     
    v_count_number_of_rows := 0;     

          -- CTX_3705 END 
      
    -- CTX-3779 START - for CTD_ID is NULL 
    -- Here we are going to populate the rows in XHBSTG_COURTROOM_DAY_DM table that have been deleted in CREST
      
    -- SELECT max(CTD_id) from XHBSTG_COURTROOM_DAY_DM table for the CREST_COURT_ID

    BEGIN
          SELECT max(NVL(CTD_ID,0)) into v_new_ctd_id
            from XHBSTG_COURTROOM_DAY_DM  WHERE CREST_COURT_ID = p_crest_court_id;
    END;

    OPEN cur_crest_crd_ctd_null;
    LOOP
    FETCH cur_crest_crd_ctd_null BULK COLLECT INTO xhb_crd_tt_null LIMIT g_bulk_limit;
   
    IF xhb_crd_tt_null IS NOT NULL AND xhb_crd_tt_null.COUNT > 0 THEN
   
        FOR i IN xhb_crd_tt_null.FIRST .. xhb_crd_tt_null.LAST LOOP
    
          BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

 -- Should the insert fail we want to capture the failing row in the exception block so hold them here before update
            v_list_date     := xhb_crd_tt_null(i).list_date;
            v_list_type     := xhb_crd_tt_null(i).list_type;

         -- set v_new_ctd_id := v_new_ctd_id + 1 i.e. max + 1
         
             v_new_ctd_id := v_new_ctd_id + 1;

          -- ctx-3855 start
            v_jud_seqno := 0; 
            v_courtroom_no := xhb_crd_tt_null(i).courtroom_no;
            
            -- check and assign jud_seq_no and courtroom_no 
            -- below checks if there are any records existing for the courtroom 1 with some jud_seq
            -- and finds a non existing jud_seqno between 1 and 9 and assigns
            -- if for courtroom 1 all 1 to 9 are present then checks for courtoom 2
            -- and assigns a non existing jud_seq no for courtroom 2 
          -- CTX-4166 : When all 1 to 9 jud_seq_no exist assume default 1                        
            BEGIN
            
             select min(column_value) into v_jud_seqno
             from table(sys.odcinumberlist(1,2,3,4,5,6,7,8,9))
              where column_value NOT IN
                    (select nvl(cdd.jud_seq_no,0) from xhbstg_courtroom_day_dm cdd
                      where cdd.crest_court_id = p_crest_court_id and -- CTX-3731
                            cdd.list_date = xhb_crd_tt_null(i).list_date and
                            cdd.list_type = xhb_crd_tt_null(i).list_type and
                            cdd.site_code = xhb_crd_tt_null(i).site_code and                            
                            cdd.courtroom_no = 1);
  -- CTX-4166 If COURTROOM_NO 1 didnt fetch jud_seq_no try COURTROOM 2 and then default jud_seq_no to 1 
              if (v_jud_seqno = 0 OR v_jud_seqno is NULL) then
                  select nvl(min(column_value),1) into v_jud_seqno -- CTX-4166 NVL will assign 1
                  from table(sys.odcinumberlist(1,2,3,4,5,6,7,8,9))
                  where column_value NOT IN
                    (select nvl(cdd.jud_seq_no,0) from xhbstg_courtroom_day_dm cdd
                      where cdd.crest_court_id = p_crest_court_id and -- CTX-3731
                            cdd.list_date = xhb_crd_tt_null(i).list_date and
                            cdd.list_type = xhb_crd_tt_null(i).list_type and
                            cdd.site_code = xhb_crd_tt_null(i).site_code and                            
                            cdd.courtroom_no = 2);
                    v_courtroom_no := 2;
              end if;                  

             EXCEPTION WHEN OTHERS THEN
                  v_jud_seqno := 1;
             END;  
             -- ctx-3855 end                  
            
             DBMS_OUTPUT.PUT_LINE('v_list_date : '||v_list_date||', v_list_type : '||v_list_type||' , adding new ctd_id : '||v_new_ctd_id||' , p_crest_court_id '||p_crest_court_id);         
               
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTING XHBSTG_COURTROOM_DAY_DM : CTD_ID is NULL - inserting  List_date - '||xhb_crd_tt_null(i).list_date||', list_type - '||xhb_crd_tt_null(i).list_type||', adding new ctd_id - '||v_new_ctd_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('INSERTING XHBSTG_COURTROOM_DAY_DM : CTD_ID is NULL - FOR CREST_COURT_ID = '||xhb_crd_tt_null(i).crest_court_id);
    
            
         -- INSERT missing records from XHBSTG_CASE_HEARING_DAY_DM into XHBSTG_COURTROOM_DAY_DM from CREST  
                     INSERT INTO  data_mig.xhbstg_courtroom_day_dm
                                  ( ctd_id
                                  , jp1_name 
                                  , jud_id   
                                  , jp2_name
                                  , jp4_name
                                  , sitting_note
                                  , list_type
                                  , commit_flag
                                  , start_time
                                  , old_ctd_id
                                  , jud_seq_no
                                  , jud_sit_type
                                  , jp3_name
                                  , list_date
                                  , courtroom_no
                                  , site_code
                                  , jud_sit_ind
                                  , crest_court_id
                                  )
                           VALUES 
                                  (v_new_ctd_id
                                  ,xhb_crd_tt_null(i).jp1_name
                                  ,xhb_crd_tt_null(i).jud_id
                                  ,xhb_crd_tt_null(i).jp2_name
                                  ,xhb_crd_tt_null(i).jp4_name
                                  ,xhb_crd_tt_null(i).sitting_note
                                  ,xhb_crd_tt_null(i).list_type
                                  ,xhb_crd_tt_null(i).commit_flag
                                  ,xhb_crd_tt_null(i).start_time
                                  ,xhb_crd_tt_null(i).old_ctd_id
                                  ,v_jud_seqno -- CTX-3855
                                  ,xhb_crd_tt_null(i).jud_sit_type
                                  ,xhb_crd_tt_null(i).jp3_name
                                  ,xhb_crd_tt_null(i).list_date
                                  ,v_courtroom_no -- CTX-3855
                                  ,xhb_crd_tt_null(i).site_code
                                  ,xhb_crd_tt_null(i).jud_sit_ind
                                  ,xhb_crd_tt_null(i).crest_court_id);      
                  
    
    v_cdd_upd_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3705:XHBSTG_COURTROOM_DAY_DM-CTD_ID is NULL - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cdd_upd_rows);
    
        EXCEPTION
            WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN parse_xhbstg_courtroom_day / COURTTOOM_DAY_DM / CTD_ID NULL/ INSERT for CREST_COURT : '||p_crest_court_id||',list_date : '||v_list_date||' , list_type : '||v_list_type||' , v_new_ctd_id : '||v_new_ctd_id||'-'||SUBSTR(v_err_message,1,100));
        
                   insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                 ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'parse_xhbstg_courtroom_day - Error inserting list_date : '||v_list_date||' , list_type : '||', v_new_ctd_id : '||v_new_ctd_id||'- Error: '||SUBSTR(v_err_message,1,150)
                                 ,p_err_row_count      => NULL 
                                 ,p_success_row_count  => v_count_number_of_rows
                                 ,p_last_updated_by    => 'PRE DATA MIGRATION'
                                 ,p_created_by         => 'PRE DATA MIGRATION'
                                 );

        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_crd_tt_null.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_crd_ctd_null%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_crd_ctd_null;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM:CTD_ID NULL : Inserted '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3705:XHBSTG_COURTROOM_DAY_DM:CTD_ID NULL inserted '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
     
    v_count_number_of_rows := 0;     

          -- CTX_3779 END 
          
          
          -- NOW PROCESS and add the missing xhbstg_lists_dm records
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHBSTG_LISTS_DM - Starting process of inserting  missing records into XHBSTG_LISTS_DM table for CREST Court : '||p_crest_court_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting missing rows into XHBSTG_LISTS_DM table');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    --v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
-- SELECT max(lst_id) from XHBSTG_LISTS_DM table for the CREST_COURT_ID

    BEGIN
          SELECT max(NVL(LST_ID,0)) into v_list_id
            from XHBSTG_LISTS_DM  WHERE CREST_COURT_ID = p_crest_court_id;
    END;
      -- Here we are going to populate the rows in XHBSTG_LISTS_DM table that have been deleted in CREST
    OPEN cur_crest_lst_details;
    LOOP
    FETCH cur_crest_lst_details BULK COLLECT INTO xhb_lst_tt LIMIT g_bulk_limit;
   
    IF xhb_lst_tt IS NOT NULL AND xhb_lst_tt.COUNT > 0 THEN
   
        FOR i IN xhb_lst_tt.FIRST .. xhb_lst_tt.LAST LOOP
    
          BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

           -- SET V_LIST_ID = V_LIST_ID + 1 
               v_list_id := v_list_id + 1;
               
           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'INSERTING XHBSTG_LISTS_DM - inserting  List_date - '||xhb_lst_tt(i).start_date||', list_type - '||xhb_lst_tt(i).list_type||', v_list_id - '||v_list_id
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('INSERTING XHBSTG_LISTS_DM - FOR CREST_COURT_ID = '||xhb_lst_tt(i).crest_court_id);
    
 -- Should the insert fail we want to capture the failing row in the exception block so hold them here before update
            v_list_date     := xhb_lst_tt(i).start_date;
            v_list_type     := xhb_lst_tt(i).list_type;
             DBMS_OUTPUT.PUT_LINE('v_list_date : '||v_list_date||', v_list_type : '||v_list_type||' , p_crest_court_id '||p_crest_court_id);         
            
         -- INSERT missing records from XHBSTG_COURTROOM_DAY_DM into XHBSTG_LISTS_DM from CREST  
                     INSERT INTO  data_mig.xhbstg_lists_dm
                                  ( lst_id
                                  , start_Date 
                                  , end_date   
                                  , list_type
                                  , date_published
                                  , cc_ind
                                  , list_status
                                  , edition_no
                                  , fcl_start_time
                                  , crest_court_id )
                           VALUES 
                                  (v_list_id
                                  ,xhb_lst_tt(i).start_date
                                  ,xhb_lst_tt(i).end_date
                                  ,xhb_lst_tt(i).list_type
                                  ,xhb_lst_tt(i).date_published
                                  ,xhb_lst_tt(i).cc_ind
                                  ,xhb_lst_tt(i).list_status
                                  ,xhb_lst_tt(i).edition_no
                                  ,xhb_lst_tt(i).fcl_start_time
                                  ,xhb_lst_tt(i).crest_court_id);      
                  
    
    v_cdd_upd_rows := SQL%ROWCOUNT;
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3575:XHBSTG_LISTS_DM - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_cdd_upd_rows);
    
        EXCEPTION
            WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN parse_xhbstg_courtroom_day for CREST_COURT : '||p_crest_court_id||',list_date : '||v_list_date||' , list_type : '||v_list_type||'-'||SUBSTR(v_err_message,1,100));
        
                   insert_dm_log (p_crest_court_id     => p_crest_court_id 
                                 ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'parse_xhbstg_courtroom_day - Error inserting list_date : '||v_list_date||' , list_type : '||' Error: '||SUBSTR(v_err_message,1,150)
                                 ,p_err_row_count      => NULL 
                                 ,p_success_row_count  => v_count_number_of_rows
                                 ,p_last_updated_by    => 'PRE DATA MIGRATION'
                                 ,p_created_by         => 'PRE DATA MIGRATION'
                                 );

        END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    v_count_number_of_rows := v_count_number_of_rows + xhb_lst_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_lst_details%NOTFOUND;
    
    END LOOP;
        

    CLOSE cur_crest_lst_details;


           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHBSTG_LISTS_DM : Inserted '||v_count_number_of_rows||' successfully!'
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );   
                         
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3575:XHBSTG_LISTS_DM inserted '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
--ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

  --- CTX-3855 --  Mark Duplicate COURTROOM_DAY records where 
  --   for the same list_date, list_type and Dummy COurt site there are MORE than 
  --  1 record. Leave 1 record to process andmark Rest as Duplicates
  --  These Duplicates will NOT be picked up for processing in
  --  SITTING ON LIST procedure
  
    FOR i in 
       (select ctd_id,list_date,list_type 
          from ( 
              select xccd.*,
                     row_number() over (partition by xccd.list_date,xccd.list_type order by xccd.list_date,xccd.list_type) rn
              from xhbstg_courtroom_day_dm xccd,
                   xhibit.xhb_court xc,
                   xhibit.xhb_court_site xcs,
                   xhibit.xhb_court_room xcr
              where xccd.crest_court_id = xc.crest_court_id and
                    xccd.crest_court_id = p_crest_court_id and
                    xc.court_id = xcs.court_id and
                    xccd.xhibit_etl_status is null and  -- re-run etl1 issue
                    xcr.court_site_id = xcs.court_site_id and
 /*                   xcs.court_site_code =
                           NVL((SELECT t.court_site_code from xhibit.xhb_court_site t
                                 where t.court_id = xc.court_id and 
                                       t.court_site_code = xccd.site_code),'X')  and
                    xcr.CREST_COURT_ROOM_NO =  
                           decode(NVL((SELECT t1.court_site_code 
                                         from xhibit.xhb_court_site t1
                                        where t1.court_id = xc.court_id and 
                                        t1.court_site_code = xccd.site_code),'X'),'X','1',xccd.courtroom_no)  */
-- 14/05/2019 ctx-4163 included to minclude missing courtrooms as dummy sites
-- CTX-4163 Further if COURT_SITE_CODE found but COURT_ROOM_NO NOT found, USE 'X' dummy site code
       xcs.court_site_code =
          NVL((SELECT t.court_site_code from xhibit.xhb_court_site t, xhibit.xhb_court_room tc
            where t.court_site_id = tc.court_site_id and t.court_id = xc.court_id and 
                  t.court_site_code = xccd.site_code and tc.crest_court_room_no = xccd.courtroom_no),'X') AND
-- CTX 3577 - CREST_COURT_ROOM_NO TO DEFAULT TO '1' IF DUMMY COURT_SITE_CODE = 'X'
-- CTX-4163 Further if COURT_SITE_CODE found but COURT_ROOM_NO NOT found, USE 'X' dummy site code and dummy courtroom_no
       xcr.CREST_COURT_ROOM_NO =  decode(xcs.court_site_code,'X','1',xccd.courtroom_no)                                                                          
                    and xcs.court_site_code = 'X' 
                    order by xccd.list_date,xccd.list_type ) 
           where rn > 1 ) 
    loop

        BEGIN
        v_list_date := i.list_date;
        v_list_type := i.list_type;

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM : Updating Duplicate - CTD_ID : '||i.ctd_id||', list_date - '||i.list_date||', i.list_type - '||i.list_type
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );
        
           update xhbstg_courtroom_day_dm 
              set xhibit_etl_status = 'D',
                  xhibit_etl_err_message = 'Setting Duplicate Dummy Court Site records to Duplicate status',
                  xhibit_enrich_date = sysdate,
                  xhibit_etl_date = sysdate
            where crest_court_id = p_crest_court_id and
                  xhibit_etl_status is NULL and
                  xhibit_enrich_date is NULL and
                  ctd_id = i.ctd_id and
                  list_date = i.list_date and
                  list_type = i.list_type;
        
         EXCEPTION WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN parse_xhbstg_courtroom_day for CREST_COURT when marking Duplicates : '||p_crest_court_id||',list_date : '||v_list_date||' , list_type : '||v_list_type||'-'||SUBSTR(v_err_message,1,100));

                   insert_dm_log (p_crest_court_id     => p_crest_court_id
                                 ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'parse_xhbstg_courtroom_day - Error updating Duplicates list_date : '||v_list_date||' , list_type : '||v_list_type||' Error: '||SUBSTR(v_err_message,1,150)
                                 ,p_err_row_count      => NULL
                                 ,p_success_row_count  => v_count_number_of_rows
                                 ,p_last_updated_by    => 'PRE DATA MIGRATION'
                                 ,p_created_by         => 'PRE DATA MIGRATION'
                                 );         
         END;        
        end loop;
        COMMIT;
        
    --- CTX-3855      

 --- CTX-3913 --  Mark Duplicate COURTROOM_DAY records where 
  --   for the same list_date, list_type , courtroom_no, site_code, jud_seqno 
  -- data set have duplicates existing in CREST 
  -- Leave 1 record which has link to CASE_HEARING_DAY_DM and mark rest as Rest as Duplicates
  --  These Duplicates will NOT be picked up for processing in
  --  SITTING ON LIST procedure
  
    FOR i in 
       (
       select stg.ctd_id,stg.list_date,stg.list_type,stg.courtroom_no,stg.site_code,stg.jud_seq_no
        from xhbstg_courtroom_day_dm  stg
         where stg.crest_court_id = p_crest_court_id and
               (stg.list_date,stg.list_type,stg.courtroom_no,stg.site_code,stg.jud_seq_no)
           in
          (select list_date,list_type,courtroom_no,site_code,jud_seq_no 
            from ( 
              select xccd.*,
                     row_number() over (partition by xccd.list_date,xccd.list_type,xccd.courtroom_no,
                                                     xccd.site_code,xccd.jud_seq_no order by xccd.list_date,xccd.list_type) rn
              from xhbstg_courtroom_day_dm xccd,
                   xhibit.xhb_court xc,
                   xhibit.xhb_court_site xcs,
                   xhibit.xhb_court_room xcr
              where xccd.crest_court_id = xc.crest_court_id and
                    xccd.crest_court_id = p_crest_court_id and
                    xc.court_id = xcs.court_id and
                    xccd.xhibit_etl_status is NULL and -- re-run etl1 issue
                    xcr.court_site_id = xcs.court_site_id and
  /*                  xcs.court_site_code =
                           NVL((SELECT t.court_site_code from xhibit.xhb_court_site t
                                 where t.court_id = xc.court_id and 
                                       t.court_site_code = xccd.site_code),'X')  and
                    xcr.CREST_COURT_ROOM_NO =  
                           decode(NVL((SELECT t1.court_site_code 
                                         from xhibit.xhb_court_site t1
                                        where t1.court_id = xc.court_id and 
                                        t1.court_site_code = xccd.site_code),'X'),'X','1',xccd.courtroom_no)   */
-- 14/05/2019 ctx-4163 included to include missing courtrooms as dummy sites
-- CTX-4163 Further if COURT_SITE_CODE found but COURT_ROOM_NO NOT found, USE 'X' dummy site code
       xcs.court_site_code =
          NVL((SELECT t.court_site_code from xhibit.xhb_court_site t, xhibit.xhb_court_room tc
            where t.court_site_id = tc.court_site_id and t.court_id = xc.court_id and 
                  t.court_site_code = xccd.site_code and tc.crest_court_room_no = xccd.courtroom_no),'X') AND
-- CTX 3577 - CREST_COURT_ROOM_NO TO DEFAULT TO '1' IF DUMMY COURT_SITE_CODE = 'X'
-- CTX-4163 Further if COURT_SITE_CODE found but COURT_ROOM_NO NOT found, USE 'X' dummy site code and dummy courtroom_no
       xcr.CREST_COURT_ROOM_NO =  decode(xcs.court_site_code,'X','1',xccd.courtroom_no)                                                                                                                  
                    and xcs.court_site_code <> 'X' -- exclude dummy court sites as these are handled already
                    order by xccd.list_date,xccd.list_type ) 
           where rn > 1 )  and
           NOT EXISTS (select 'X' from xhbstg_case_hearing_day_dm case_stg 
                        where case_stg.crest_court_id = p_crest_court_id and 
                              case_stg.ctd_id = stg.ctd_id) ) 
    loop

        BEGIN
        v_ctd_id := i.ctd_id;
        v_list_date := i.list_date;
        v_list_type := i.list_type;
        v_courtroom_no := i.courtroom_no;
        v_site_code := i.site_code;
        v_jud_seqno := i.jud_seq_no;

           insert_dm_log (p_crest_court_id     => p_crest_court_id
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHBSTG_COURTROOM_DAY_DM : Updating CREST Duplicate - CTD_ID : '||i.ctd_id||', list_date - '||i.list_date||', i.list_type - '||i.list_type||', courtroom_no - '||i.courtroom_no||', site_code - '||i.site_code||', jud_seq_no - '||i.jud_seq_no
                         ,p_err_row_count      => NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );
        
           update xhbstg_courtroom_day_dm 
              set xhibit_etl_status = 'D',
                  xhibit_etl_err_message = 'Setting Duplicate CREST COURTROOM_DAY records to Duplicate status',
                  xhibit_enrich_date = sysdate,
                  xhibit_etl_date = sysdate
            where crest_court_id = p_crest_court_id and
                  xhibit_etl_status is NULL and
                  xhibit_enrich_date is NULL and
                  ctd_id = i.ctd_id and
                  list_date = i.list_date and
                  list_type = i.list_type and
                  courtroom_no = i.courtroom_no and
                  site_code = i.site_code and
                  jud_seq_no = i.jud_seq_no;
        
         EXCEPTION WHEN OTHERS THEN
                v_err_message := SQLERRM;

                DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN parse_xhbstg_courtroom_day for CREST_COURT when marking Duplicates : '||p_crest_court_id||',list_date : '||v_list_date||' , list_type : '||v_list_type||'-'||SUBSTR(v_err_message,1,100));

                   insert_dm_log (p_crest_court_id     => p_crest_court_id
                                 ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                                 ,p_run_time           =>  sysdate
                                 ,p_log_msg_type       =>  'E' -- Error
                                 ,p_log_msg            =>  'parse_xhbstg_courtroom_day - Error updating CREST Duplicates - ctd_id : '||v_ctd_id||', list_date : '||v_list_date||' , list_type : '||v_list_type||' Error: '||SUBSTR(v_err_message,1,150)
                                 ,p_err_row_count      => NULL
                                 ,p_success_row_count  => v_count_number_of_rows
                                 ,p_last_updated_by    => 'PRE DATA MIGRATION'
                                 ,p_created_by         => 'PRE DATA MIGRATION'
                                 );         
         END;        
        end loop;
        COMMIT;
        
    --- CTX-3913      

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        --ROLLBACK;-- CTX-3252
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN parse_xhbstg_courtroom_day for CREST_COURT : '||p_crest_court_id||',list_date : '||v_list_date||' , list_type : '||v_list_type||'-'||SUBSTR(v_err_message,1,100));

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'parse_xhbstg_courtroom_day - CTX-3705,CTX-3575'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'E' -- Error
                         ,p_log_msg            =>  'parse_xhbstg_courtroom_day - Error inserting list_date : '||v_list_date||' , v_list_type : '||v_list_type||' , v_ctd_id : '||v_ctd_id||' ,v_list_id : '||v_list_id||' - Error: '||SUBSTR(v_err_message,1,150)
                         ,p_err_row_count      => NULL 
                         ,p_success_row_count  => v_count_number_of_rows
                         ,p_last_updated_by    => 'PRE DATA MIGRATION'
                         ,p_created_by         => 'PRE DATA MIGRATION'
                         );

        COMMIT;

          
                     
END parse_xhbstg_courtroom_day;

/**
  * NAME       : upd_xhb_out_bw_history_crest
  * DESCRIPTION: CTX-3743 Add Outstanding Bench Waranty from CREST
  * PARAMETERS : p_crest_court_id - The CREST Court ID being processed will be passed to this procedure
  * 12/03/2019 :  S Sethuraman - CTX-3844 - UPD_XHB_MON_ORD_TRACK_CREST, XHB_UPD_OUT_BW_HISTORY - Update Staging DISPOSAL_DM table back for updat status with disp_id filter included - both CURSORS include LIST_DATE > SYSDATE to migrate FURTURE FIXTURES ONLY of list_type = 'X'
**/
PROCEDURE upd_xhb_out_bw_history_crest(p_crest_court_id IN xhibit.xhb_court.crest_court_id%TYPE)
AS

    v_xhibit_court_id xhibit.xhb_court.court_id%TYPE;
  
 TYPE xhb_obwh_rec IS RECORD
    ( 
      crest_court_id               xhibit.xhb_court.crest_court_id%TYPE
    , court_id                     xhibit.xhb_court.court_id%TYPE
    , defendant_on_case_id         xhibit.xhb_defendant_on_case.defendant_on_case_id%TYPE
    , bw_issue_date                xhibit.xhb_bw_history.bw_issue_date%TYPE
    , bw_end_date                 xhibit.xhb_bw_history.bw_end_date%TYPE
    , bc_status_bw_issued          xhibit.xhb_bw_history.bc_status_bw_issued%TYPE
    , live_status                 xhbstg_case_dm.live_status%type
    , case_no                      xhbstg_bw_history_dm.case_no%TYPE
    , case_type                    xhbstg_bw_history_dm.case_type%TYPE
    , sub_id                       xhbstg_bw_history_dm.sub_id%TYPE
    , bench_warrant_exec_date      xhbstg_case_subject_dm.bench_warrant_exec_date%type
    , dis_id                       xhbstg_disposal_dm.dis_id%type -- CTX-3844
    , disposal_code                xhbstg_disposal_dm.disposal_code%type -- CTX-3844
    );

    TYPE xhb_obwh_type IS TABLE OF xhb_obwh_rec;
    xhb_obwh_tt  xhb_obwh_type;
      
    CURSOR cur_crest_obwh_details IS
    SELECT DISTINCT CAS.CREST_COURT_ID,
                XC.COURT_ID,
                XDOC.DEFENDANT_ON_CASE_ID,
                DIS.OUTCOME_DATE AS BW_ISSUE_DATE,
                NULL AS BW_END_DATE,
                CSU.BC_STATUS_BW_ISSUED,
                CAS.LIVE_STATUS,
                CSU.CASE_NO,
                CSU.CASE_TYPE,                
                S.SUB_ID,
                CSU.BENCH_WARRANT_EXEC_DATE,
                DIS.DIS_ID, -- CTX-3844
                DIS.DISPOSAL_CODE -- CTX-3844
           FROM XHBSTG_CASE_DM CAS, 
                XHBSTG_CASE_SUBJECT_DM CSU,
                XHBSTG_DISPOSAL_DM DIS,
                XHBSTG_SUBJECT_DM S,
                XHIBIT.XHB_COURT XC,
                XHIBIT.XHB_CASE XCAS,
                XHIBIT.XHB_DEFENDANT XD,
                XHIBIT.XHB_DEFENDANT_ON_CASE XDOC
           WHERE CAS.CREST_COURT_ID = p_crest_court_id 
              AND XC.CREST_COURT_ID = CAS.CREST_COURT_ID
              AND XCAS.COURT_ID = XC.COURT_ID
              AND CSU.CREST_COURT_ID = CAS.CREST_COURT_ID
              AND DIS.CREST_COURT_ID = CAS.CREST_COURT_ID
              AND S.CREST_COURT_ID = CAS.CREST_COURT_ID
              AND XD.COURT_ID = XC.COURT_ID
              AND XD.DEFENDANT_ID = XDOC.DEFENDANT_ID
              AND CAS.CASE_NO = CSU.CASE_NO
              AND CAS.CASE_TYPE = CSU.CASE_TYPE
              AND CSU.Case_No = DIS.Case_No
              AND CSU.CASE_TYPE = DIS.CASE_TYPE
              AND S.SUB_ID = CSU.SUB_ID
              AND DIS.SUB_ID = CSU.SUB_ID
              AND XD.CREST_DEFENDANT_ID = S.SUB_ID
              AND XCAS.CASE_NUMBER = CAS.CASE_NO
              AND XCAS.CASE_TYPE = CAS.CASE_TYPE
              AND XDOC.CASE_ID = XCAS.CASE_ID
              AND DIS.DISPOSAL_CODE LIKE 'BW%'              
              AND (CSU.BENCH_WARRANT_EXEC_DATE IS NULL OR
                   (CSU.BENCH_WARRANT_EXEC_DATE < (SELECT MAX(DISP.OUTCOME_DATE) 
                                                          FROM XHBSTG_DISPOSAL_DM DISP
                                                          WHERE DISP.CREST_COURT_ID = DIS.CREST_COURT_ID
                                                            AND DISP.DIS_ID = DIS.DIS_ID
                                                            AND DISP.SUB_ID = DIS.SUB_ID)))
              AND NOT EXISTS (SELECT 'X' FROM XHIBIT.XHB_BW_HISTORY BWH
                                    WHERE BWH.DEFENDANT_ON_CASE_ID = XDOC.DEFENDANT_ON_CASE_ID
                                       AND BWH.BW_ISSUE_DATE = DIS.OUTCOME_DATE
                                       AND BWH.BC_STATUS_BW_ISSUED = CSU.BC_STATUS_BW_ISSUED
                                       AND nvl(BWH.OBS_IND,'N') != 'Y');      
          
    v_count_number_of_rows   NUMBER := 0;
    v_obwh_ins_rows           NUMBER := 0;
    v_obwh_ins_status         CHAR(1) := 'N';
    v_sub_id                 xhbstg_bw_history_dm.sub_id%TYPE;
    v_dis_id                 xhbstg_disposal_dm.dis_id%TYPE; -- CTX-3844
    v_disposal_code          xhbstg_disposal_dm.disposal_code%TYPE;  -- CTX-3844   
    v_case_type              xhbstg_bw_history_dm.case_type%TYPE;
    v_case_no                xhbstg_bw_history_dm.case_no%TYPE;
    v_live_status            xhbstg_case_dm.live_status%type;
    v_bw_exec_date           xhbstg_case_subject_dm.bench_warrant_exec_date%type;            
    v_err_message            xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

           insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_out_bw_history_crest- CTX-3743'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'XHB_BW_HISTORY - Starting process of inserting new rows with the required data to be populated from CREST'
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => NULL
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );


    DBMS_OUTPUT.PUT_LINE('CREST - COURT : '||p_crest_court_id||' - Starting process of inserting new rows in XHB_BW_HISTORY with the required data to be populated from CREST');
    -- Get the corresponding XHIBIT Court ID from the CREST Court ID being processed. This value will be used at several places
    v_xhibit_court_id := get_xhb_court_from_crest_court(p_crest_court_id);
   
      -- Here we are going to populate the rows in XHB_BW_HISTORY table that have not yet been populated with CREST data
    OPEN cur_crest_obwh_details;
    LOOP
    FETCH cur_crest_obwh_details BULK COLLECT INTO xhb_obwh_tt LIMIT g_bulk_limit;
   
    IF xhb_obwh_tt IS NOT NULL AND xhb_obwh_tt.COUNT > 0 THEN
   
        FOR i IN xhb_obwh_tt.FIRST .. xhb_obwh_tt.LAST LOOP

            BEGIN -- CTX-3252 - BEGIN BLOCK for handling exception for each record and to continue processing

                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                             ,p_action_name        => 'upd_xhb_out_bw_history_crest- CTX-3743'
                             ,p_run_time           =>  sysdate
                             ,p_log_msg_type       =>  'I' -- Information
                             ,p_log_msg            =>  'INSERTING XHB_BW_HISTORY - inserting  DIS_ID : '||xhb_obwh_tt(i).dis_id||', sub_id - '||xhb_obwh_tt(i).sub_id||', case_no : '||xhb_obwh_tt(i).case_no||', case_type : '||xhb_obwh_tt(i).case_type||' , live_status : '||xhb_obwh_tt(i).live_status||' , bw_exec_date : '||xhb_obwh_tt(i).bench_warrant_exec_date
                             ,p_err_row_count      =>  NULL
                             ,p_success_row_count  => NULL
                             ,p_last_updated_by    => 'DATA MIGRATION'
                             ,p_created_by         => 'DATA MIGRATION'
                             );

                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('INSERTING XHB_BW_HISTORY- FOR CREST_COURT_ID = '||xhb_obwh_tt(i).crest_court_id);
                
                -- Should the update fail we want to capture the failing row in the exception block so hold them here before update
                v_case_no     := xhb_obwh_tt(i).case_no;
                v_case_type     := xhb_obwh_tt(i).case_type;
                v_sub_id     := xhb_obwh_tt(i).sub_id;
                v_dis_id     := xhb_obwh_tt(i).dis_id; -- CTX-3844
                v_disposal_code     := xhb_obwh_tt(i).disposal_code;  -- CTX-3844               
                v_live_status := xhb_obwh_tt(i).live_status;
                v_bw_exec_date := xhb_obwh_tt(i).bench_warrant_exec_date;

                DBMS_OUTPUT.PUT_LINE('v_case_no : '||v_case_no||', v_case_type : '||v_case_type||', v_dis_id : '||v_dis_id||', v_sub_id : '||v_sub_id||' ,v_live_status : '||v_live_status||' ,v_bw_exec_date : '||v_bw_exec_date||' , v_xhibit_court_id : '||v_xhibit_court_id);         
                
                -- INSERT and populate this NEW table XHB_BW_HISTORY with data from CREST  
                INSERT INTO  xhibit.XHB_BW_HISTORY
                            ( bw_history_id
                            , defendant_on_case_id 
                            , bw_issue_date   
                            , bw_end_date
                            , bc_status_bw_issued 
                            , creation_date
                            , created_by  
                            , last_update_date 
                            , last_updated_by )
                     VALUES 
                            (xhibit.xhb_bw_history_seq.nextval
                            ,xhb_obwh_tt(i).defendant_on_case_id
                            ,xhb_obwh_tt(i).bw_issue_date
                            ,xhb_obwh_tt(i).bw_end_date
                            ,xhb_obwh_tt(i).bc_status_bw_issued
                            ,SYSDATE
                            ,'DATA_MIGRATION'
                            ,SYSDATE
                            ,'DATA_MIGRATION');      
                
                
                v_obwh_ins_rows := SQL%ROWCOUNT;
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-3743:XHB_BW_HISTORY - Crest Court : '||p_crest_court_id||' - Inserted no of rows : '||v_obwh_ins_rows);

              -- CTX-3844 start - update back staging XHBSTG_DISPOSAL_DM
              
                -- Update XHBSTG_DISPOSAL_DM table for the rows processed
                IF v_obwh_ins_rows > 0 THEN 
                    v_obwh_ins_status := 'I'; -- XHIBIT TABLE inserted
                ELSIF v_obwh_ins_rows = 0 THEN
                    v_obwh_ins_status := 'N'; -- No Action Performed on XHIBIT table
                END IF;  
                
                insert_dm_log (p_crest_court_id     => p_crest_court_id 
                         ,p_action_name        => 'upd_xhb_out_bw_history_crest- CTX-3743'
                         ,p_run_time           =>  sysdate
                         ,p_log_msg_type       =>  'I' -- Information
                         ,p_log_msg            =>  'UPDATING XHBSTG_DISPOSAL_DM - updated sub_id '||xhb_obwh_tt(i).sub_id||', dis_id - '||xhb_obwh_tt(i).dis_id||', disposal_code - '||xhb_obwh_tt(i).disposal_code||' row with ETL_STATUS '||v_obwh_ins_status
                         ,p_err_row_count      =>  NULL
                         ,p_success_row_count  => v_obwh_ins_rows
                         ,p_last_updated_by    => 'DATA MIGRATION'
                         ,p_created_by         => 'DATA MIGRATION'
                         );
                         

                -- Update XHBSTG_DISPOSAL_DM table for the rows processed
                UPDATE XHBSTG_DISPOSAL_DM xcg
                SET    xcg.xhibit_etl_date   = SYSDATE
                      ,xcg.xhibit_court_id   = v_xhibit_court_id
                      ,xcg.xhibit_enrich_date = SYSDATE
                      ,xcg.xhibit_etl_status = v_obwh_ins_status
                WHERE  xcg.crest_court_id   =  p_crest_court_id
                  AND  xcg.sub_id = xhb_obwh_tt(i).sub_id
                  AND  xcg.case_no = xhb_obwh_tt(i).case_no
                  AND  xcg.case_type = xhb_obwh_tt(i).case_type
                  AND  xcg.dis_id = xhb_obwh_tt(i).dis_id
                  AND  xcg.disposal_code = xhb_obwh_tt(i).disposal_code;
                  --  the above statement will NOT overwrite any records updated by MON_ORD_TRACK as the disposal_code is different for BOTH  
                
                DBMS_OUTPUT.PUT_LINE(' ');
                DBMS_OUTPUT.PUT_LINE('CTX-3743:XHBSTG_DISPOSAL_DM - Crest Court : '||p_crest_court_id||' - Updated no of rows : '||SQL%ROWCOUNT);

                 -- CTX - 3844 end                 
           EXCEPTION
              WHEN OTHERS THEN
                  v_err_message := SQLERRM;
                  
                  DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_XHB_BW_HISTORY_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||'-'||SUBSTR(v_err_message,1,100));
          
                  insert_dm_log (p_crest_court_id     => p_crest_court_id 
                               ,p_action_name        => 'upd_xhb_out_bw_history_crest - CTX-3743'
                               ,p_run_time           =>  sysdate
                               ,p_log_msg_type       =>  'E' -- Error
                               ,p_log_msg            =>  'upd_xhb_out_bw_history_crest - Error processing dis_id : '||v_dis_id||', sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                               ,p_err_row_count      => NULL 
                               ,p_success_row_count  => v_count_number_of_rows
                               ,p_last_updated_by    => 'DATA MIGRATION'
                               ,p_created_by         => 'DATA MIGRATION'
                               );
                                
            END; -- CTX-3252 - END BLOCK for handling exception for each record and to continue processing
 
    END LOOP;

    COMMIT; ---Commit after every successful processing BULK COLLECT of rows   ----????????????????????????????????????
    
    v_count_number_of_rows := v_count_number_of_rows + xhb_obwh_tt.COUNT;

    END IF;
    
    EXIT WHEN cur_crest_obwh_details%NOTFOUND;
    
    END LOOP;
        
    CLOSE cur_crest_obwh_details;

    insert_dm_log (p_crest_court_id     => p_crest_court_id 
               ,p_action_name        => 'upd_xhb_out_bw_history_crest- CTX-3743'
               ,p_run_time           =>  sysdate
               ,p_log_msg_type       =>  'I' -- Information
               ,p_log_msg            =>  'XHB_BW_HISTORY : Processed '||v_count_number_of_rows||' successfully!'
               ,p_err_row_count      => NULL 
               ,p_success_row_count  => v_count_number_of_rows
               ,p_last_updated_by    => 'DATA MIGRATION'
               ,p_created_by         => 'DATA MIGRATION'
               );

    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('CTX-3743:XHB_BW_HISTORY inserted '||v_count_number_of_rows||' for CREST_COURT_ID : '||p_crest_court_id||' successfully!');
    
    COMMIT;
    --ROLLBACK; ---???????????????????????????????? FOR TESTING ONLY SO REMOVE

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SQLERRM;
        
        --ROLLBACK; --CTX-3252
        
        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN update_XHB_BW_HISTORY_crest for CREST_COURT : '||p_crest_court_id||',sub_id : '||v_sub_id||'-'||SUBSTR(v_err_message,1,100));
        
         insert_dm_log (p_crest_court_id     => p_crest_court_id 
                       ,p_action_name        => 'upd_xhb_out_bw_history_crest - CTX-3743'
                       ,p_run_time           =>  sysdate
                       ,p_log_msg_type       =>  'E' -- Error
                       ,p_log_msg            =>  'upd_xhb_out_bw_history_crest - Error processing dis_id : '||v_dis_id||', sub_id : '||v_sub_id||' Error: '||SUBSTR(v_err_message,1,150)
                       ,p_err_row_count      => NULL 
                       ,p_success_row_count  => v_count_number_of_rows
                       ,p_last_updated_by    => 'DATA MIGRATION'
                       ,p_created_by         => 'DATA MIGRATION'
                       );
        
        COMMIT;
                     
END upd_xhb_out_bw_history_crest;

/**
  * NAME       : upd_xhb_case_on_list_fixture
  * DESCRIPTION: CTX-3908: Procedure to be called to update, as in link, XHB_CASE_ON_LIST.CASE_DIARY_FIXTURE_ID with its equivalent XHB_CASE_DIARY_FIXTURE.CASE_DIARY_FIXTURE_ID
  *              after both tables have been populated during the data migration run for a court.
  * PARAMETERS : p_court_id  - The CREST Court ID being processed will be passed to this procedure            
**/
PROCEDURE upd_xhb_case_on_list_fixture(p_crest_court_id IN xhbstg_case_dm.crest_court_id%TYPE)
AS

    TYPE xhb_case_fixtures_rec IS RECORD
    ( case_diary_fixture_id  xhibit.xhb_case_diary_fixture.case_diary_fixture_id%TYPE
    , case_on_list_id        xhibit.xhb_case_on_list.case_on_list_id%TYPE);
    
    TYPE xhb_case_fixtures_type IS TABLE OF xhb_case_fixtures_rec;
    xhb_case_fixtures_tt xhb_case_fixtures_type;

    v_count_number_of_rows     NUMBER := 0;
    v_case_diary_fixture_id    xhibit.xhb_case_diary_fixture.case_diary_fixture_id%TYPE  := NULL; 
    v_case_on_list_id          xhibit.xhb_case_on_list.case_on_list_id%TYPE := NULL;
    v_err_message              xhbstg_case_dm.xhibit_etl_err_message%TYPE;
    
BEGIN

    -- Log the Crest Court, procedure name, Jira ticket and the Xhibit table
    insert_dm_log ( p_crest_court_id     =>  p_crest_court_id 
                  , p_action_name        => 'upd_xhb_case_on_list_fixture - CTX-3908'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            =>  'XHB_CASE_CASE_ON_LIST - START UPDATING CASE_DIARY_FIXTURE_ID with equivalent XHB_CASE_DIARY_FIXTURE.CASE_DIARY_FIXTURE_ID'
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  =>  NULL
                  , p_created_by         =>  'DATA MIGRATION'              
                  , p_last_updated_by    =>  'DATA MIGRATION'        
                  );

    DBMS_OUTPUT.PUT_LINE('START UPDATING XHB_CASE_CASE_ON_LIST.CASE_DIARY_FIXTURE_ID with equivalent XHB_CASE_DIARY_FIXTURE.CASE_DIARY_FIXTURE_ID');
    
    -- Fetch the matching rows in XHB_CASE_DIARY_FIXTURE and XHB_CASE_ON_LIST that has to be linked
    -- NOTE that the only data that will exist for a court at the time of Data Migration will be the data created by Data Migration. 
    -- No user data will have been entered (and these are new CTX tables). Also, as DM does not set the CASE_ON_LIST.CASE_DIARY_FIXTURE anywhere else it will always be null.
    SELECT cdf.case_diary_fixture_id
         , con.case_on_list_id
    BULK COLLECT INTO xhb_case_fixtures_tt
    FROM xhibit.xhb_case_diary_fixture cdf
       , xhibit.xhb_case_listing_entry cle
       , xhibit.xhb_case_on_list con
    WHERE cdf.case_listing_entry_id    = cle.case_listing_entry_id
    AND   cle.case_id                  = con.case_id
    AND   cle.court_id                 = get_xhb_court_from_crest_court(p_crest_court_id)  
    AND   cdf.hearing_type_id          = con.hearing_type_id
    AND   TRUNC(cdf.listing_date)      = TRUNC(con.time_listed);  
   
    -- If no rows were BULK COLLECTED above it will not fall over so check that there are rows before processing.
    IF xhb_case_fixtures_tt IS NOT NULL AND xhb_case_fixtures_tt.COUNT > 0 THEN
      
        FOR i IN xhb_case_fixtures_tt.FIRST .. xhb_case_fixtures_tt.LAST LOOP
   
            v_case_diary_fixture_id := xhb_case_fixtures_tt(i).case_diary_fixture_id;
            v_case_on_list_id       := xhb_case_fixtures_tt(i).case_on_list_id;
            -- If no row updated it will not fail, the loop will iterate till end
            UPDATE xhibit.xhb_case_on_list con
            SET    con.case_diary_fixture_id = v_case_diary_fixture_id
            WHERE  con.case_on_list_id       = v_case_on_list_id;
            
            IF SQL%ROWCOUNT > 0 THEN
            
                -- Log the fact that we have processed the row fetched for update
                insert_dm_log ( p_crest_court_id     =>  p_crest_court_id 
                              , p_action_name        =>  'upd_xhb_case_on_list_fixture - CTX-3908'
                              , p_run_time           =>  SYSDATE
                              , p_log_msg_type       =>  'I' -- Information
                              , p_log_msg            =>  'XHB_CASE_ON_LIST - case_on_list_id - '||v_case_on_list_id||' updated with case_diary_fixture_id = '||v_case_diary_fixture_id
                              , p_err_row_count      =>   NULL
                              , p_success_row_count  =>  1
                              , p_created_by         =>  'DATA MIGRATION'              
                              , p_last_updated_by    =>  'DATA MIGRATION'        
                              );
                              
                    v_count_number_of_rows := v_count_number_of_rows + 1;
                
            END IF;
            
            DBMS_OUTPUT.PUT_LINE('case_diary_fixture_id = '||v_case_diary_fixture_id||' used to update case_on_list_id = '||v_case_on_list_id );
        
        END LOOP;
    
    END IF;
    
    DBMS_OUTPUT.PUT_LINE('Total number of rows in XHB_CASE_ON_LIST where CASE_DIARY_FIXTURE_ID was updated = '||v_count_number_of_rows);
    DBMS_OUTPUT.PUT_LINE('FINISHED UPDATING XHB_CASE_CASE_ON_LIST.CASE_DIARY_FIXTURE_ID with equivalent XHB_CASE_DIARY_FIXTURE.CASE_DIARY_FIXTURE_ID'); 
   
    -- Log the total number of rows processed for the update
    insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                  , p_action_name        => 'upd_xhb_case_on_list_fixture - CTX-3908'
                  , p_run_time           =>  SYSDATE
                  , p_log_msg_type       =>  'I' -- Information
                  , p_log_msg            => 'FINISHED UPDATING XHB_CASE_CASE_ON_LIST.CASE_DIARY_FIXTURE_ID. Total number of rows in XHB_CASE_ON_LIST where CASE_DIARY_FIXTURE_ID was updated = '||v_count_number_of_rows
                  , p_err_row_count      =>  NULL
                  , p_success_row_count  => v_count_number_of_rows
                  , p_created_by         => 'DATA MIGRATION'              
                  , p_last_updated_by    => 'DATA MIGRATION'        
                  ); 

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        v_err_message := SUBSTR(SQLERRM,1,500);

        DBMS_OUTPUT.PUT_LINE('!!! ERROR HAS OCCURRED IN upd_xhb_case_on_list_fixture: '||SUBSTR(v_err_message,1,150));
        DBMS_OUTPUT.PUT_LINE('The number of rows updated and committed in XHB_CASE_ON_LIST before Error = '||v_count_number_of_rows);
        
        -- Log the error with the failing row details and the Oracle Exception
        insert_dm_log ( p_crest_court_id     => p_crest_court_id 
                      , p_action_name        => 'upd_xhb_case_on_list_fixture- CTX-3908'
                      , p_run_time           =>  SYSDATE
                      , p_log_msg_type       =>  'E' -- Error
                      , p_log_msg            => 'XHB_CASE_ON_LIST - ERROR processing case_diary_fixture_id = '||v_case_diary_fixture_id||' case_on_list_id = '||v_case_on_list_id||' ERROR: '||SUBSTR(v_err_message,1,200)
                      , p_err_row_count      =>  1
                      , p_success_row_count  =>  NULL
                      , p_created_by         => 'DATA MIGRATION'              
                      , p_last_updated_by    => 'DATA MIGRATION'        
                      );
        
    COMMIT;  

END upd_xhb_case_on_list_fixture;

END xhb_data_migration_process_pkg;
/