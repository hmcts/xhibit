/* CGI CREST to XHIBIT Program
*
* MODULE      : getOUTC_Rpt_test10
*
* DESCRIPTION : This script tests the get_OUTC_report stored procedure. 
* 				get_drsr_report accepts 3user-selectable parameters (court_id, month (string) and year(string)).
*
*  PROCEDURE get_outc_report(p_results_out OUT SYS_REFCURSOR,
*                            p_court_id IN XHB_CASE.COURT_ID%TYPE,
*                            p_CASE_TYPE IN VARCHAR2,
*                            p_CASE_CLASS IN VARCHAR2,
*                            p_BC_STATUS IN VARCHAR2,
*                            p_DEFAULT_HEARING_TYPE IN xhb_ref_hearing_type.HEARING_TYPE_CODE%TYPE,
*                            p_TIME_EST_FROM IN NUMBER,
*                            p_TIME_EST_TO IN NUMBER,
*                            p_UNITS IN NUMBER,
*                            p_REQUIRED_JUDGE_TYPE IN xhb_case_listing_entry.REF_JUDGE_TYPE_ID%TYPE,
*                            p_UNITS_WEEKS IN NUMBER,
*                            p_SECURE_COURTROOM IN VARCHAR2,
*                            p_JUVENILE_ONLY IN VARCHAR2,
*                            p_PRIOITY_NOTES_Y_N IN VARCHAR2,
*                            p_RESTRICTED_NOTES_Y_N IN VARCHAR2,
*                            p_STANDARD_NOTES_Y_N IN VARCHAR2,
*                            p_SORTBY IN VARCHAR2);	
*	
*
* Columns:      DIARY_NOTE_ENTRY_ID, CASE_NUMBER, CASE_TITLE, is_juvenile, Commited_Sent, 
*               CLASS_CODE, HEARING_TYPE, monitoring_category_code, LOEST, CASE_GROUP_NUMBER, 
*               First_NAD, Listed, NOTE_CLASSIFICATION, DIARY_NOTE_TEXT, NOTE_TYPE   
*                        
*
*************************************************************************************
*
*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 13/03/2019    J Riley         1.0         First Version-
**/
SET SERVEROUTPUT ON SIZE 1000000X
SET LINESIZE 180
SET PAGESIZE 300

DECLARE
   
    v_found             VARCHAR2(1) := 'Y';
    v_row               NUMBER(8)   := 0;

    v_message_filename  VARCHAR2(100)   := 'get_outc_report_test1';
    
    v_system            VARCHAR2(100)   := NULL;
    v_directory         VARCHAR2(4000)  := NULL;
    v_database          VARCHAR2(100)   := NULL;
    v_slash             INTEGER;
    v_step              VARCHAR2(10) ;
    v_str_doc           VARCHAR2(30); 
    v_test_fail_msg     VARCHAR2(500)   := ' ';
    v_resultset         SYS_REFCURSOR   := NULL;  
    v_text              VARCHAR2(4000)  := NULL;
	
    -- Parameters to pass to the procedure under test	
    v_court_id          XHB_CASE.COURT_ID%TYPE      := 81;    -- Snaresbrook
    v_case_type         XHB_CASE.CASE_TYPE%TYPE     := 'T';   -- Trial (Can be A, S, T, null)
    v_case_class        XHB_CASE.CLASS_CODE%TYPE    := NULL;   -- Can be 1, 2, 3, null
	v_bc_status         VARCHAR2(2)                 := NULL;  -- Can be 'C', 'B', 'N/A', null                  
	v_df_hearing_type   XHB_REF_HEARING_TYPE.HEARING_TYPE_CODE%TYPE := NULL;   -- Can be any of 125 3-letter codes, or null
	v_time_est_from     XHB_DIRECTIONS_FOR_CASE.TRIAL_TIME_ESTIMATE%TYPE := NULL;
	v_time_est_to       XHB_DIRECTIONS_FOR_CASE.TRIAL_TIME_ESTIMATE%TYPE := NULL;
	v_units             XHB_DIRECTIONS_FOR_CASE.TRIAL_TIME_UNIT%TYPE := NULL;
	v_reqd_judge_type   xhb_case_listing_entry.ref_judge_type_id%TYPE := NULL ;    -- Can be 111101, 112536, 114008, 111073 or null
	v_units_weeks       NUMBER(8) := 5;
	v_secure_ctrm       XHB_CASE.SECURE_COURT%TYPE  := 'N';   -- 'N' selects both options, 'Y' selects only secure court cases
	v_juvenile_only     xhb_defendant_on_case.is_juvenile%TYPE := 'Y';  -- 'N' selects both options, 'Y' selects only cases where at least one defendant is a juvenile
	v_priority_notes    XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE  := 'N' ;
	v_restricted_notes  XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE  := 'N' ;
	v_std_notes         XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE  := 'N' ;
	v_sortby            VARCHAR2(20) := 'CASENUMBER';-- Can be 'CASENUMBER' or null/anything else (defaults to trial date)
    
    -- Variables to hold (one row of) returned database query
  v_diary_note_entry_id    NUMBER(8);
  v_case_number            VARCHAR2(12);
  v_custody_case           VARCHAR2(2);
	v_case_title             VARCHAR2(50);
	v_juv_flg                VARCHAR2(12);   
	v_committed_sent         VARCHAR2(12);  
	v_class_code             XHB_CASE.CLASS_CODE%TYPE;
  v_hearingtype            XHB_REF_HEARING_TYPE.HEARING_TYPE_DESC%TYPE;
	v_mon_cat_cd             XHB_REF_MONITORING_CATEGORY.monitoring_category_code%TYPE;
	v_loest                  VARCHAR2(12);  
	v_case_no_grp            XHB_CASE.CASE_GROUP_NUMBER%TYPE;
	v_1st_NAD                VARCHAR2(12); 
	v_listed                 VARCHAR2(2); 
	v_note_class             XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE;
	v_diary_note_txt         XHB_DIARY_NOTE_ENTRY.DIARY_NOTE_TEXT%TYPE;
	v_note_type              XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE;
   
    v_val1                   VARCHAR2(12);
    v_val2                   XHB_REF_HEARING_TYPE.HEARING_TYPE_DESC%TYPE;
    v_val3                   XHB_CASE.CASE_GROUP_NUMBER%TYPE;
    v_val4                   XHB_REF_LISTING_DATA.REF_DATA_VALUE%TYPE;
    
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 10 ' || v_message_filename || ' START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);

    v_step := '2';
    
/*  PROCEDURE get_outc_report(p_results_out OUT SYS_REFCURSOR,
*                            p_court_id IN XHB_CASE.COURT_ID%TYPE,
*                            p_CASE_TYPE IN VARCHAR2,
*                            p_CASE_CLASS IN VARCHAR2,
*                            p_BC_STATUS IN VARCHAR2,
*                            p_DEFAULT_HEARING_TYPE IN xhb_ref_hearing_type.HEARING_TYPE_CODE%TYPE,
*                            p_TIME_EST_FROM IN NUMBER,
*                            p_TIME_EST_TO IN NUMBER,
*                            p_UNITS IN NUMBER,
*                            p_REQUIRED_JUDGE_TYPE IN xhb_case_listing_entry.REF_JUDGE_TYPE_ID%TYPE,
*                            p_UNITS_WEEKS IN NUMBER,
*                            p_SECURE_COURTROOM IN VARCHAR2,
*                            p_JUVENILE_ONLY IN VARCHAR2,
*                            p_PRIOITY_NOTES_Y_N IN VARCHAR2,
*                            p_RESTRICTED_NOTES_Y_N IN VARCHAR2,
*                            p_STANDARD_NOTES_Y_N IN VARCHAR2,
*                            p_SORTBY IN VARCHAR2);	
*/
    -- Call the procedure we are testing
    XHB_REPORT_PKG_JR.get_outc_report( p_results_out          =>  v_resultset
                                     , p_court_id             =>  v_court_id 
                                     , p_CASE_TYPE            =>  v_case_type
                                     , p_CASE_CLASS           =>  v_case_class
									 , p_BC_STATUS            =>  v_bc_status
									 , p_DEFAULT_HEARING_TYPE =>  v_df_hearing_type
									 , p_TIME_EST_FROM        =>  v_time_est_from
									 , p_TIME_EST_TO          =>  v_time_est_to
									 , p_UNITS                =>  v_units
									 , p_REQUIRED_JUDGE_TYPE  =>  v_reqd_judge_type
									 , p_UNITS_WEEKS          =>  v_units_weeks 
									 , p_SECURE_COURTROOM     =>  v_secure_ctrm 
									 , p_JUVENILE_ONLY        =>  v_juvenile_only
									 , p_PRIOITY_NOTES_Y_N    =>  v_priority_notes
									 , p_RESTRICTED_NOTES_Y_N =>  v_restricted_notes 
									 , p_STANDARD_NOTES_Y_N   =>  v_std_notes 
									 , p_SORTBY               =>  v_sortby );
-- l 58
    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := ' 3';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
	
  -- check message is correct

    v_step := ' 4';
	
/* Columns:      DIARY_NOTE_ENTRY_ID, CASE_NUMBER, CUSTODY_CASE, CASE_TITLE, is_juvenile, Commited_Sent, 
*               CLASS_CODE, HEARING_TYPE, monitoring_category_code, LOEST, CASE_GROUP_NUMBER, 
*               First_NAD, Listed, NOTE_CLASSIFICATION, DIARY_NOTE_TEXT, NOTE_TYPE   */
	
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('DIARY_NOTE_ENTRY_ID|CASE_NUMBER | CASE_TITLE                            | Juv  | Commited_Sent | CLASS_CODE |HEARING_TYPE |mon_cat_cd | LOEST |CASE_GRP_NO |First_NAD |Listed |NOTE_CLASS |DIARY_NOTE_TEXT       |NOTE_TYPE |');
    DBMS_OUTPUT.PUT_LINE('-------------------|------------|---------------------------------------|------|---------------|------------|-------------|-----------|-------|------------|----------|-------|-----------|----------------------|----------|');

    LOOP        
	         
        v_step := ' 5.' || To_char(v_row);
--                            number                          
        FETCH v_resultset INTO v_diary_note_entry_id, v_case_number, v_custody_case, v_case_title, v_juv_flg, v_committed_sent, v_class_code, v_hearingtype, v_mon_cat_cd, v_loest, v_case_no_grp, v_1st_NAD, v_listed, v_note_class, v_diary_note_txt, v_note_type ;
        EXIT  WHEN v_resultset%NOTFOUND;
           
        v_row := v_row + 1;

        IF v_diary_note_entry_id = 504 then 
            v_step := ' 6';
            v_val1 := v_case_number;
            v_val2 := v_hearingtype;
            v_val3 := v_case_no_grp;
            v_val4 := v_note_class ;
        END IF;
        
        v_text := rpad(nvl(v_diary_note_entry_id, -1), 18, ' ') || ' | ' || rpad(v_case_number, 10, ' ') || ' | ' || rpad(v_case_title, 37, ' ') || ' | ' || rpad(v_juv_flg, 4, ' ') || ' | ' || rpad(v_committed_sent, 13, ' ');
        v_text := v_text || ' | ' || rpad(v_class_code, 10, ' ') || ' | ' || rpad(nvl(v_hearingtype, 'null'), 11, ' ') || ' | ' || rpad(v_mon_cat_cd, 9, ' ');
        v_text := v_text || ' | ' || rpad(nvl(v_loest, '-'), 5, ' ') || ' | ' || rpad(nvl(v_case_no_grp, -1), 10, ' ') || ' | ' || rpad(nvl(v_1st_NAD, '-'), 8, ' ') || ' | ' || rpad(v_listed, 5, ' ');
        v_text := v_text || ' | ' || rpad(nvl(v_note_class, 'null'), 9, ' ') || ' | ' || rpad(nvl(v_diary_note_txt, 'null'), 20, ' ') || ' | ' || rpad(v_note_type, 9, ' ' || '|');
        DBMS_OUTPUT.PUT_LINE(v_text);
    END LOOP;
    
    v_step := ' 7';
    
    CLOSE v_resultset;
    
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('Rows retrieved: ' || to_char(v_row));




    -- Check we have retrieved the data we expect to
    
    v_step := ' 8';
    IF v_val1 != 'T20140005' THEN 
        v_test_fail_msg := 'Case Number: expected T20140005, got ' || v_val1 || Chr(10);
    END IF;
    
    v_step := ' 9';
    IF v_val2 != 'BOO' THEN 
        v_test_fail_msg := 'Hearing Type: expected BOO, got ' || v_val2 || Chr(10);
    END IF;
    
    v_step := '10';
    IF v_val3 != 640 THEN 
        v_test_fail_msg := 'Case Group Number: expected 640, got ' || v_val3 || Chr(10);
    END IF;
      
    v_step := '11';
    
    IF v_val4 != 'Priority' THEN  
        v_test_fail_msg := v_test_fail_msg || 'Note Type: expected Priority, got ' || v_val4 || Chr(10);   -- Has to contain this tag
    END IF;
    
    v_step := '12';
  
    IF v_row != 18 THEN 
        v_test_fail_msg := v_test_fail_msg || 'Rows retrieved: expected 18, got ' || v_row  || Chr(10); 
    END IF;
    
    v_step := '13';
    
    DBMS_OUTPUT.PUT_LINE(' ');
    
    IF v_test_fail_msg = ' ' THEN
        -- expected success
        DBMS_OUTPUT.PUT_LINE('Test Succeeded');        
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed');
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE(v_test_fail_msg);
    END IF;   

--    DBMS_OUTPUT.PUT_LINE('output message file in utl_file_hold directory = ' || v_message_filename || chr(10));
 
    DBMS_OUTPUT.PUT_LINE('         ------------------- Test 10 ' || v_message_filename || '  END --------------------------' || chr(10));
    
    ROLLBACK;
    
    EXCEPTION 
        WHEN OTHERS THEN
            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE(SUBSTR(SQLERRM, 1, 150 ));
            DBMS_OUTPUT.PUT_LINE('ERROR HAS OCCURRED !!!!!!!!!!!');         
            DBMS_OUTPUT.PUT_LINE('AT STEP ' || v_step);         
        
END;
/
SPOOL OFF

