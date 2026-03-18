/**
* CGI CREST to XHIBIT Program
*
* MODULE      : getRELCJ_Rpt_test1
*
* DESCRIPTION : This script tests the getRELCJ_Report report stored procedure. 
* 				getAppealHearingNotifnRpt accepts 2 user-selectable parameters (court_id and week-ending (date)).
*
* PROCEDURE get_relcj_report(p_results_out OUT SYS_REFCURSOR
*                         ,  p_court_id    IN XHB_CASE.COURT_ID%TYPE)
*
*		This test looks for cases requiring a specific judge.
*
* Columns:    HEARING_TYPE_ID, CASE_ID, CASE_NUMBER, CASE_TITLE, CLASS_CODE, HEARING_TYPE, 
*			  TRIAL_TIME_ESTIMATE, REQUIRED_JUDGE_SURNAME, CHARGES_INFO
*
*
*************************************************************************************
*
*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 18/10/2018    J Riley         1.0         First Version
**/
SET SERVEROUTPUT ON SIZE 1000000
SET LINESIZE 180
SET PAGESIZE 300

DECLARE
   
    v_found             VARCHAR2(1) := 'Y';
    v_row               NUMBER(8)   := 0;

    v_message_filename  VARCHAR2(100)   := 'getRUMO_Rpt_test1';
    
    v_system            VARCHAR2(100)   := NULL;
    v_directory         VARCHAR2(4000)  := NULL;
    v_database          VARCHAR2(100)   := NULL;
    v_slash             INTEGER;
    v_step              VARCHAR2(2) ;
    v_str_doc           VARCHAR2(30);
    v_test_fail_msg     VARCHAR2(500)   := ' ';
    v_resultset         SYS_REFCURSOR   := NULL;  
	
    -- Parameters to pass to the procedure under test	
    v_court_id          NUMBER(8)      := 81;  
    
    -- Variables to hold (one row of) returned database query
    v_hearing_type_id        XHB_CASE_DIARY_FIXTURE.HEARING_TYPE_ID%TYPE;  -- Not needed in report, just used to sort on.
    v_case_id                XHB_CASE.CASE_ID%TYPE;
    v_case_number            VARCHAR2(50);
    v_case_title             XHB_CASE.CASE_TITLE%TYPE;
    v_class_code             XHB_CASE.CLASS_CODE%TYPE;
    v_hearing_type_code      XHB_REF_HEARING_TYPE.HEARING_TYPE_CODE%TYPE;
    v_trial_tm_est           VARCHAR2(20);
    v_reqd_judge_surname     XHB_REF_JUDGE.SURNAME%TYPE;
    v_charges_info           VARCHAR2(500);
    
    v_val1                   XHB_CASE_DIARY_FIXTURE.HEARING_TYPE_ID%TYPE; 
    v_val2                   VARCHAR2(50);
    v_val3                   XHB_CASE.CASE_TITLE%TYPE;
    v_val4                   XHB_REF_JUDGE.SURNAME%TYPE;
    
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 getRELCJ_Rpt START ============================--');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);

    v_step := '2';
    
    -- Call the procedure we are testing
    XHB_REPORT_PKG.get_relcj_report( p_results_out    =>  v_resultset
                                   , p_court_id       =>  v_court_id );
-- l 58
    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := ' 3';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
	
  -- check message is correct

    v_step := ' 4';
    
    DBMS_OUTPUT.PUT_LINE(' ');    
    DBMS_OUTPUT.PUT_LINE('HEARING_TYPE_ID, CASE_ID,    CASE_NUMBER,   CASE_TITLE,              CLASS_CODE, HEARING_TYPE, TRIAL_TIME_ESTIMATE, REQUIRED_JUDGE_SURNAME,  CHARGES_INFO');
/* * Columns:    HEARING_TYPE_ID, CASE_ID, CASE_NUMBER, CASE_TITLE, CLASS_CODE, HEARING_TYPE, 
*			  TRIAL_TIME_ESTIMATE, REQUIRED_JUDGE_SURNAME, CHARGES_INFO
 */    
    v_step := '5';

    LOOP        
       FETCH v_resultset INTO v_hearing_type_id, v_case_id, v_case_number, v_case_title, v_class_code, v_hearing_type_code, v_trial_tm_est, v_reqd_judge_surname, v_charges_info ;
        EXIT  WHEN v_resultset%NOTFOUND;
           
        v_step := ' 6';
        v_row := v_row + 1;

        IF v_row = 5 then 
            v_val1 := v_hearing_type_id;
            v_val2 := v_case_number;
            v_val3 := v_case_title;
            v_val4 := v_reqd_judge_surname ;
        END IF;
        
        v_step := ' 7';
        DBMS_OUTPUT.PUT_LINE(rpad(v_hearing_type_id, 17, ' ') || rpad(v_case_id, 12, ' ') ||  rpad(v_case_number, 15, ' ') || rpad(v_case_title, 28, ' ') || rpad(v_class_code, 12, ' ') || rpad(v_hearing_type_code, 15, ' ')|| rpad(v_trial_tm_est, 17, ' ') || rpad(v_reqd_judge_surname, 25, ' ') || rpad(v_charges_info, 10, ' ') );
    END LOOP;
    
    v_step := ' 8';
    CLOSE v_resultset;
        
    v_step := ' 9';
    -- Check we have retrieved the data we expect to
    IF v_val1 != 18322 THEN 
        v_test_fail_msg := 'hearing_type_id: expected 18322, got ' || v_val1 || Chr(10);
    END IF;
    
    v_step := '10';
    IF v_val2 != 'T20140006' THEN 
        v_test_fail_msg := v_test_fail_msg || 'case_number: expected T20140006, got ' || v_val2 || Chr(10);
    END IF;
  
    v_step := '11';
    IF v_val3 != 'BOB' THEN 
        v_test_fail_msg := v_test_fail_msg || 'case_title: expected BOB, got ' || v_val3 || Chr(10);
    END IF;
    
    v_step := '12';
    IF v_val4 != 'JONES' THEN  
        v_test_fail_msg := v_test_fail_msg || 'reqd_judge_surname: expected JONES, got ' || v_val4 || Chr(10);   -- Has to contain this tag
    END IF;
  
    v_step := '13';
    IF v_row != 19 THEN 
        v_test_fail_msg := v_test_fail_msg || 'Rows retrieved: expected 19, got ' || v_row  || Chr(10); 
    END IF;
    
    IF v_test_fail_msg = ' ' THEN
        -- expected success
        DBMS_OUTPUT.PUT_LINE('Test Succeeded');        
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed');
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE(v_test_fail_msg);
    END IF;   

--    DBMS_OUTPUT.PUT_LINE('output message file in utl_file_hold directory = ' || v_message_filename || chr(10));
 
    DBMS_OUTPUT.PUT_LINE('         ------------------- Test 1 getRUMO_Rpt  END --------------------------' || chr(10));
    
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


