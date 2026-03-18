/* CGI CREST to XHIBIT Program
*
* MODULE      : getDRSR_Rpt_test1
*
* DESCRIPTION : This script tests the get_drsr_report stored procedure. 
* 				get_drsr_report accepts 3user-selectable parameters (court_id, month (string) and year(string)).
*
*  PROCEDURE get_drsr_report(p_results_out  OUT SYS_REFCURSOR                        
*                          , p_court_id     IN XHB_CASE.COURT_ID%TYPE
*                          , p_MONTH_PERIOD IN VARCHAR2
*                          , p_YEAR_PERIOD  IN VARCHAR2 )
*	
*
* Columns:      NOT_BEFORE_TIME, LISTING_DATE, casenumber, HEARING_TYPE_CODE, SITTING_SEQUENCE_NO, COURT_ROOM_NAME, SITE             
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

    v_message_filename  VARCHAR2(100)   := 'getDRSR_Rpt_test1';
    
    v_system            VARCHAR2(100)   := NULL;
    v_directory         VARCHAR2(4000)  := NULL;
    v_database          VARCHAR2(100)   := NULL;
    v_slash             INTEGER;
    v_step              VARCHAR2(10) ;
    v_str_doc           VARCHAR2(30); 
    v_test_fail_msg     VARCHAR2(500)   := ' ';
    v_resultset         SYS_REFCURSOR   := NULL;  
	
    -- Parameters to pass to the procedure under test	
    v_court_id          XHB_CASE.COURT_ID%TYPE      := 81;  
	v_month             VARCHAR2(5)     := 'Oct';
	v_year              VARCHAR2(5)     := '2018';
    
    -- Variables to hold (one row of) returned database query
    v_not_bf_time            XHB_SCHEDULED_HEARING.NOT_BEFORE_TIME%TYPE;
	  v_listingdate            VARCHAR2(12);
    v_hearingtype            XHB_REF_HEARING_TYPE.HEARING_TYPE_DESC%TYPE;
    v_case_number            VARCHAR2(12);
    v_sittng_seq             NUMBER(8);
    v_ct_rm_name             VARCHAR2(500);
    v_ct_site                VARCHAR2(500);
    
    v_val1                   VARCHAR2(12);
    v_val2                   XHB_REF_HEARING_TYPE.HEARING_TYPE_DESC%TYPE;
    v_val3                   XHB_COURT.COURT_NAME%TYPE;
    v_val4                   VARCHAR2(100);
    
    
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
    
--*  PROCEDURE get_drsr_report(p_results_out  OUT SYS_REFCURSOR                        
--*                          , p_court_id     IN XHB_CASE.COURT_ID%TYPE
--*                          , p_MONTH_PERIOD IN VARCHAR2
--*                          , p_YEAR_PERIOD  IN VARCHAR2 )
    -- Call the procedure we are testing
    XHB_REPORT_PKG.get_drsr_report( p_results_out    =>  v_resultset
                                  , p_court_id       =>  v_court_id 
                                  , p_MONTH_PERIOD   =>  v_month
                                  , p_YEAR_PERIOD    =>  v_year);
-- l 58
    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := ' 3';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
	
  -- check message is correct

    v_step := ' 4';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('NOT_BEFORE_TIME|LISTING_DATE| CaseNumber    | HEARING_TYPE_CODE | SITTING_SEQUENCE_NO | COURT_ROOM_NAME               | SITE                       |');
    DBMS_OUTPUT.PUT_LINE('---------------|------------|---------------|-------------------|---------------------|-------------------------------|----------------------------|');
    /*  * Columns:      NOT_BEFORE_TIME, LISTING_DATE, casenumber, HEARING_TYPE_CODE, SITTING_SEQUENCE_NO, COURT_ROOM_NAME, SITE             
                     
 */    
    LOOP        
        v_step := ' 5.' || To_char(v_row);
        FETCH v_resultset INTO v_not_bf_time, v_listingdate, v_case_number, v_hearingtype, v_sittng_seq, v_ct_rm_name, v_ct_site ;
--        FETCH v_resultset INTO v_doc_id, v_case_number, v_representative, v_ptiurn, v_hearingvenue, v_notes, v_PROSECUTOR_AGENCY_ID ;
        EXIT  WHEN v_resultset%NOTFOUND;
           
        v_row := v_row + 1;

        IF v_row = 1 then 
            v_step := ' 6';
            v_val1 := v_case_number;
            v_val2 := v_hearingtype;
            v_val3 := v_ct_rm_name;
            v_val4 := v_ct_site ;
        END IF;
        
        DBMS_OUTPUT.PUT_LINE(rpad(v_not_bf_time, 14, ' ') || ' | ' || rpad(v_listingdate, 10, ' ') || ' | ' || rpad(v_case_number, 13, ' ') || ' | ' || rpad(v_hearingtype, 17, ' ') || ' | ' || rpad(v_sittng_seq, 19, ' ') || ' | ' || rpad(v_ct_rm_name, 29, ' ') || ' | ' || rpad(v_ct_site, 30, ' '));
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
    END IF;   

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

