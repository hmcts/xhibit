/**
* CGI CREST to XHIBIT Program
*
* MODULE      : xhb_populate_rcs_screen_test1
*
* DESCRIPTION : This script tests the xhb_populate_rcs_screen stored procedure. 
* 				xhb_populate_rcs_screen accepts 3 user-selectable parameters (court_id, start_date, end_date ).
*
*  PROCEDURE xhb_populate_rcs_screen(p_court_id  IN XHB_CASE.COURT_ID%TYPE
*                                  , p_from_date IN DATE 
*                                  , p_to_date   IN DATE )
*
*		
*
*
*************************************************************************************
*
*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 24/10/2018    J Riley         1.0         First Version
**/
SET SERVEROUTPUT ON SIZE 1000000
SET LINESIZE 180
SET PAGESIZE 300

DECLARE
   
    v_found             VARCHAR2(1) := 'Y';
    v_row               NUMBER(8)   := 0;

    v_message_filename  VARCHAR2(100)   := 'xhb_populate_rcs_screen_test1';
    
    v_system            VARCHAR2(100)   := NULL;
    v_directory         VARCHAR2(4000)  := NULL;
    v_database          VARCHAR2(100)   := NULL;
    v_slash             INTEGER;
    v_step              VARCHAR2(10) ;
    v_str_doc           VARCHAR2(30); 
    v_test_fail_msg     VARCHAR2(500)   := ' ';
    v_resultset         SYS_REFCURSOR   := NULL;  
	
    -- Parameters to pass to the procedure under test	
    v_court_id               XHB_CASE.COURT_ID%TYPE  := 81;  
    v_start_date             DATE                    := '05-May-2018';  
    v_end_date               DATE                    := '10-May-2018';  
    
    -- Variables to hold (one row of) returned database query
--    v_doc_id                 XHB_DEFENDANT_ON_CASE.DEFENDANT_ON_CASE_ID%TYPE;  -- Not needed in report, just used to sort on.
    v_doc_id                 NUMBER(8);
    v_hearingtype            XHB_REF_HEARING_TYPE.HEARING_TYPE_DESC%TYPE;
    v_case_number            VARCHAR2(10);
    v_defendant              VARCHAR2(255);
    v_representative         VARCHAR2(500);
    v_telephoneno            XHB_CONTACT_DETAIL.CONTACT_VALUE%TYPE;
    v_ptiurn                 XHB_DEFENDANT_ON_CASE.PTIURN%TYPE;
    v_hearingdate            XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE;
    v_hearingvenue           XHB_COURT.COURT_NAME%TYPE;
    v_prosecutor             VARCHAR2(500);
    v_notes                  XHB_CASE_DIARY_FIXTURE.LIST_NOTE_TEXT%TYPE;
    v_court_address          VARCHAR2(500);
    v_casediaryfixture       XHB_CASE_DIARY_FIXTURE.CASE_DIARY_FIXTURE_ID%TYPE;
    v_PROSECUTOR_AGENCY_ID   XHB_REF_PROSECUTOR_AGENCY.REF_PROSECUTOR_AGENCY_ID%TYPE;
    
    v_val1                   VARCHAR2(10);
    v_val2                   XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE;
    v_val3                   XHB_COURT.COURT_NAME%TYPE;
    v_val4                   NUMBER(8);
    
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 ' || v_message_filename || ' START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);

    v_step := '2';
    
--*  PROCEDURE get_list_of_fixed_dates_report(p_results_out out SYS_REFCURSOR
--*                                         , p_court_id IN XHB_CASE.COURT_ID%TYPE
--*                                         , p_run_date IN XHB_CASE_DIARY_FIXTURE.LISTING_DATE%TYPE)
    -- Call the procedure we are testing
    xhb_populate_rcs_screen( p_court_id     =>  v_court_id 
	                     ,   p_from_date    =>  v_start_date
                         ,   p_to_date      =>  v_end_date);
-- l 58
    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := ' 3';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
	
  -- check message is correct
/*
    v_step := ' 4';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('docid     HearingType                       CaseNumber     Defendant                               representative                                                                            Telephoneno           ptiurn    HearingDate      HearingVenue                            Prosecutor  v_notes, v_courtaddress, v_casediaryfixture, v_PROSECUTOR_AGENCY_ID');
    LOOP        
        v_step := ' 5.' || To_char(v_row);
        FETCH v_resultset INTO v_doc_id, v_hearingtype, v_case_number, v_defendant, v_representative, v_telephoneno, v_ptiurn, v_hearingdate, v_hearingvenue, v_prosecutor, v_notes, v_court_address, v_casediaryfixture, v_PROSECUTOR_AGENCY_ID ;
--        FETCH v_resultset INTO v_doc_id, v_case_number, v_representative, v_ptiurn, v_hearingvenue, v_notes, v_PROSECUTOR_AGENCY_ID ;
        EXIT  WHEN v_resultset%NOTFOUND;
           
        v_row := v_row + 1;

        IF v_row = 5 then 
            v_step := ' 6';
            v_val1 := v_case_number;
            v_val2 := v_hearingdate;
            v_val3 := v_hearingvenue;
            v_val4 := v_casediaryfixture ;
        END IF;
        
        DBMS_OUTPUT.PUT_LINE(rpad(v_doc_id, 10, ' ') || rpad(v_hearingtype, 34, ' ') ||  rpad(v_case_number, 13, ' ') || rpad(v_defendant, 42, ' ') || rpad(v_representative, 50, ' ') || rpad(v_telephoneno, 22, ' ')|| rpad(v_ptiurn, 10, ' ') || rpad(v_hearingdate, 14, ' ') || rpad(v_hearingvenue, 36, ' ') || rpad(v_prosecutor, 30, ' ') || rpad(v_notes, 50, ' ') || rpad(v_court_address, 30, ' ') || rpad(v_casediaryfixture, 10, ' ') || rpad(v_PROSECUTOR_AGENCY_ID, 10, ' '));
    END LOOP;
    
    v_step := ' 7';
    CLOSE v_resultset;
    
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('Rows retrieved: ' || to_char(v_row));

/*        
    -- Check we have retrieved the data we expect to
    IF v_val1 != 100 THEN 
        v_test_fail_msg := 'v_fine_amt: expected 100, got ' || v_val1 || Chr(10);
    END IF;
    IF v_val2 != 250 THEN 
        v_test_fail_msg := 'v_compensation_amt: expected 250, got ' || v_val2 || Chr(10);
    END IF;
    IF v_val3 != 35 THEN 
        v_test_fail_msg := 'v_costs_amt: expected 35, got ' || v_val3 || Chr(10);
    END IF;
    
    IF v_val4 != 'ABINGDON MAGISTRATES'' COURT' THEN  
        v_test_fail_msg := v_test_fail_msg || 'v_iss_court_full_name: expected ABINGDON MAGISTRATES'' COURT, got ' || v_val4 || Chr(10);   -- Has to contain this tag
    END IF;
  
    IF v_row != 8 THEN 
        v_test_fail_msg := v_test_fail_msg || 'Rows retrieved: expected 8, got ' || v_row  || Chr(10); 
    END IF;
 */   
    DBMS_OUTPUT.PUT_LINE(' ');
    
    IF v_test_fail_msg = ' ' THEN
        -- expected success
        DBMS_OUTPUT.PUT_LINE('Test Succeeded');        
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed');
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE(v_test_fail_msg);
    END IF;   /**

--    DBMS_OUTPUT.PUT_LINE('output message file in utl_file_hold directory = ' || v_message_filename || chr(10));
 
    DBMS_OUTPUT.PUT_LINE('         ------------------- Test 1 ' || v_message_filename || '  END --------------------------' || chr(10));
    
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
