/**
* CGI CREST to XHIBIT Program
*
* MODULE      : get_RREC_SUMMARY_test1
*
* DESCRIPTION : This script tests the get_RREC_SUMMARY stored procedure. 
* 				get_RREC_SUMMARY accepts 2 user-selectable parameters (court_id and week-ending (date)).
*               It returns a sys_refcursor as output parameter.
* Procedure get_RREC_detail (p_resultset   OUT sys_refcursor
*                         ,  p_court_id    IN  XHB_COURT.COURT_ID%TYPE 
*                         ,  p_end_date    IN  DATE  )
*                          
*
*		This test retrieves counts of outstanding court cases.
*
*************************************************************************************
*
*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 02/07/2018    J Riley         1.0         First Version
**/
SET SERVEROUTPUT ON SIZE 1000000
SET LINESIZE 180
SET PAGESIZE 300

DECLARE
   
    v_found              VARCHAR2(1) := 'Y';
    v_row                NUMBER(8)   := 0;

    v_message_filename   VARCHAR2(100)      := 'get_RREC_report_test1';
    v_resultset1         sys_refcursor;  
   
    v_system             VARCHAR2(100)      := NULL;
    v_directory          VARCHAR2(4000)     := NULL;
    v_database           VARCHAR2(100)      := NULL;
    v_slash              INTEGER;
    v_step               VARCHAR2(10)        := ' 0';
    v_str_doc            VARCHAR2(30);
    v_user               VARCHAR2(10)       := 'd82015';
    v_section_num        NUMBER(8)          := 0;
    v_last_section       NUMBER(8)          := 0;
    v_sort_order         NUMBER(8)          := 0;
    v_court_site         VARCHAR2(255)      := '-';
    v_case_type          VARCHAR2(50)       := '-';
    v_case_number        NUMBER(8)          := 0;
    v_case_subhdg        VARCHAR2(50)       := '-';
    v_case_id            NUMBER(8)          := 0;  
    v_test_fail_msg      VARCHAR2(500)      := ' ';
    v_count              NUMBER(8)          := 0;
    v_rpt_log_id         NUMBER(8)          := 0;
    v_err_msg            VARCHAR2(2000);     
    v_run_date           DATE;
    v_report_name        VARCHAR2(100)      := ' ';
    v_report_code        VARCHAR2(10)       := ' ';
    v_section0           NUMBER(8)          := 0;
    v_section1           NUMBER(8)          := 0;
    v_section2           NUMBER(8)          := 0;
    v_section3           NUMBER(8)          := 0;
    v_section4           NUMBER(8)          := 0;
    v_section5           NUMBER(8)          := 0;
    v_section6           NUMBER(8)          := 0;
    v_section7           NUMBER(8)          := 0;
    v_section8           NUMBER(8)          := 0;
    
    -- Parameters to pass to the procedure under test	
    v_court_id           NUMBER(8)          := 81;  
    v_court_id2          NUMBER(8)          := 0;
    v_end_date           DATE               := '14-Jun-2018';
    
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 get_RREC_summary_test1 START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
    v_step := '2';
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);
    
    v_step := '3';
    
    -- Call the procedure we are testing
    XHB_REPORT_PKG_JR.get_RREC_summary (v_resultset1 
                                     ,  v_court_id  
                                     ,  v_end_date );
--69
    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := '5';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
    
    v_step := '6';
    
    -- Get the summary.
    v_row := 0;
    v_step := '7';
    DBMS_OUTPUT.PUT_LINE(' ');        
    DBMS_OUTPUT.PUT_LINE('Case Type               Case Subhdg                   Sect 1  Sect 2  Sect 3  Sect 4  Sect 5  Sect 6  Sect 7  Sect 8');        
    DBMS_OUTPUT.PUT_LINE('---------               -----------                   ------  ------  ------  ------  ------  ------  ------  ------');        
    -- Find out how many rows we got. (Not set until all rows have been Fetched.)
    v_step := '8';
    Loop
        FETCH v_resultset1 into v_sort_order, v_court_site, v_case_type, v_case_subhdg, v_section1, v_section2, v_section3, v_section4, v_section5, v_section6, v_section7, v_section8 ;    
        EXIT WHEN v_resultset1%NOTFOUND;       
        
        v_row := v_row + 1;
        v_section6 := v_section2 + v_section3 + v_section4 - v_section5;
        v_step := '9.' || to_char(v_row);
        
        --Check the Row 1 values
        IF v_row = 1 then
            IF (v_case_type != 'COMMITTAL FOR SENTENCE') OR (v_case_subhdg != 'BRING BACK') OR (v_section1 != 196) OR (v_section2 != 27) THEN
                v_err_msg := 'Row 1: Expected COMMITTAL FOR SENTENCE, BRING BACK, 196, 27 but got ' || v_case_type || ', ' || v_case_subhdg || ', ' || to_char(v_section1) || ', ' || to_char(v_section2) || chr(10);
            END IF;
        END IF;
        
        DBMS_OUTPUT.PUT_LINE(rpad(v_court_site, 24) || rpad(v_case_type, 24) || rpad(v_case_subhdg, 30) || rpad(v_section1, 8) || rpad(v_section2, 8) || rpad(v_section3, 8) || rpad(v_section4, 8) || rpad(v_section5, 8) || rpad(v_section6, 8) || rpad(v_section7, 8) || rpad(v_section8, 8) );        
        
    End Loop;
        
    v_step := '10';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('Summary: Rows retrieved: ' || v_row);

    IF v_row != 12 THEN
        v_err_msg := v_err_msg || 'Rows retrieved: expected 12, received ' || to_char(v_row) || chr(10);
    END IF;
    
    -- Check that the report has logged that it has been run.
    v_step := '11';
    SELECT report_log_id, court_id,    report_name,   crest_report_code, date_last_run 
    INTO   v_rpt_log_id,  v_court_id2, v_report_name, v_report_code,     v_run_date 
    FROM   XHB_REPORT_LOG 
    WHERE  date_last_run > (sysdate - 0.01)   -- in the last 14 minutes
    AND    crest_report_code = 'RREC'; 
    
    v_step := '12';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('report_log_id  court_id  report_name                      crest_report_code  date_last_run'); 
    DBMS_OUTPUT.PUT_LINE('-------------  --------  -----------                      -----------------  -------------');
    DBMS_OUTPUT.PUT_LINE(v_rpt_log_id || '                 ' || v_court_id2 || '  ' || v_report_name || '   ' || v_report_code || '               ' || v_run_date);
    DBMS_OUTPUT.PUT_LINE(' ');
    
    IF (v_court_id2 != 81) OR  (v_report_code != 'RREC') OR (Trunc(v_run_date) != Trunc(sysdate)) THEN
        v_err_msg := v_err_msg || 'Report log: Expected court_id = 81, crest_report_code = RREC, date_last_run = ' || Trunc(sysdate) || ', got court_id = ' || v_court_id2 || ', crest_report_code = ' || v_report_code || ', date_last_run = ' || v_run_date || chr(10); 
    END IF;

    
    v_step := '13';
    IF v_test_fail_msg = ' ' THEN
        -- expected success
        DBMS_OUTPUT.PUT_LINE('Test Succeeded');        
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed');
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE(v_test_fail_msg);
    END IF;   

--    DBMS_OUTPUT.PUT_LINE('output message file in utl_file_hold directory = ' || v_message_filename || chr(10));
 
    DBMS_OUTPUT.PUT_LINE('         --------- Test 1 get_RREC_summary_test1  END --------------------------' || chr(10));
       
    EXCEPTION 
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SUBSTR(SQLERRM, 1, 200 ));
            DBMS_OUTPUT.PUT_LINE('ERROR HAS OCCURRED !!!!!!!!!!!');         
            DBMS_OUTPUT.PUT_LINE('AT STEP ' || v_step);   
            
            SELECT COUNT(REPORTS_LOG_ID) INTO v_count FROM XHB_REPORT_ERR_LOG WHERE  v_run_date > sysdate - 0.01;  -- in the last 14 minutes
            
            IF v_count > 0 THEN
            
                SELECT REPORTS_LOG_ID, error_message, run_date
                INTO   v_rpt_log_id,   v_err_msg,     v_run_date
                FROM   XHB_REPORT_ERR_LOG
                WHERE  v_run_date > sysdate - 0.01;  -- in the last 14 minutes
            ELSE
                v_err_msg := ' No error log entry found.';
            END IF;
        
            DBMS_OUTPUT.PUT_LINE('REPORT_ERR_LOG: ' || v_rpt_log_id || v_err_msg || ' ' || v_run_date);  
            
            ROLLBACK;
END;
/
SPOOL OFF
