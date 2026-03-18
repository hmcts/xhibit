/**
* CGI CREST to XHIBIT Program
*
* MODULE      : get_RREC_REPORT_test1
*
* DESCRIPTION : This script tests the get_RREC_REPORT stored procedure. 
* 				get_RREC_REPORT accepts 2 user-selectable parameters (court_id and week-ending (date)).
*               It returns 2 collection variables as output parameters (see RREC_TYPES.sql for definitions)
* Procedure get_RREC_report (p_resultset1  OUT rrec_case_array p_court_id    IN  XHB_COURT.COURT_ID%TYPE 
*                         ,  p_resultset2  OUT rrec_summary_array
*                         ,  p_court_id    IN  XHB_COURT.COURT_ID%TYPE 
*                         ,  p_end_date    IN  DATE  )
*                          
*
*		This test creates a case and its subsidiary records, then calls delete_case_ctx
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
    v_resultset2         sys_refcursor;  
    
    v_system             VARCHAR2(100)      := NULL;
    v_directory          VARCHAR2(4000)     := NULL;
    v_database           VARCHAR2(100)      := NULL;
    v_slash              INTEGER;
    v_step               VARCHAR2(10)        := ' 0';
    v_str_doc            VARCHAR2(30);
    v_user               VARCHAR2(10)       := 'd82015';
    v_section_num        NUMBER(8)          := 0;
    v_last_section       NUMBER(8)          := 0;
    v_case_type          VARCHAR2(50)       := '-';
    v_case_number        NUMBER(8)          := 0;
    v_case_subhdg        VARCHAR2(50)       := '-';
    v_case_id            NUMBER(8)          := 0;  
    v_case_pros_agy_id   NUMBER(8)          := 0;       
    v_skeleton_id        NUMBER(8)          := 0;
    v_skeleton_day_id    NUMBER(8)          := 0;
    v_def_on_case_id     NUMBER(8)          := 0;
    v_legal_aid_order_id NUMBER(8)          := 0;
    v_test_fail_msg      VARCHAR2(500)      := ' ';
    v_count              NUMBER(8)          := 0;
    v_rpt_log_id         NUMBER(8)          := 0;
    v_err_msg            VARCHAR2(2000);     
    v_run_date           DATE;
    v_report_name        VARCHAR2(100)      := ' ';
    v_report_code        VARCHAR2(10)       := ' ';
    v_sections           rrec_section_array;
    
    -- Parameters to pass to the procedure under test	
    v_court_id           NUMBER(8)          := 81;  
    v_court_id2          NUMBER(8)          := 0;
    v_end_date           DATE               := '14-Jun-2018';
    
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 get_RREC_report_test1 START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
    v_step := '2';
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);
    
    v_step := '3';
    
    DBMS_OUTPUT.PUT_LINE('Line 89: After Initialise the arrays');        

    v_step := '4';
    
    -- Call the procedure we are testing
    XHB_REPORT_PKG_JR.get_RREC_report (v_resultset1 
                                    ,  v_resultset2 
                                    ,  v_court_id  
                                    ,  v_end_date );
--69
    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := '5';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
    
    v_step := '6';
    DBMS_OUTPUT.PUT_LINE(' ');        
    DBMS_OUTPUT.PUT_LINE('Section No  Case Type                     Case Number  Case Subhdg                               Case_id');        
    DBMS_OUTPUT.PUT_LINE('----------  ---------                     -----------  -----------                               --------');        
    -- Find out how many rows we got. (Not set until all rows have been Fetched.)
    v_step := '7';
    Loop
        FETCH v_resultset1 into v_section_num, v_case_type, v_case_number, v_case_subhdg, v_case_id;    
        EXIT WHEN v_resultset1%NOTFOUND;       
        
        v_row := v_row + 1;
        
        v_step := '8';
        IF (v_row < 11) OR (v_section_num != v_last_section) THEN
            DBMS_OUTPUT.PUT_LINE(rpad(v_section_num, 12) || rpad(v_case_type, 30) || rpad(v_case_number, 13) || rpad(v_case_subhdg, 42) || rpad(v_case_id, 11));        
        END IF;
        
        v_last_section := v_section_num;
    End Loop;
        
    v_step := '9';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('First Part: Rows retrieved: ' || v_row);
    
    -- Get the second part - the summary.
    v_row := 0;
    v_step := '10';
    DBMS_OUTPUT.PUT_LINE(' ');        
    DBMS_OUTPUT.PUT_LINE('Case Type  Case Subhdg  Sect 1  Sect 2  Sect 3  Sect 4  Sect 5  Sect 6  Sect 7  Sect 8');        
    DBMS_OUTPUT.PUT_LINE('---------  -----------  ------  ------  ------  ------  ------  ------  ------  ------');        
    -- Find out how many rows we got. (Not set until all rows have been Fetched.)
    v_step := '11';
    Loop
        FETCH v_resultset2 into v_case_type, v_case_subhdg, v_sections;    
        EXIT WHEN v_resultset2%NOTFOUND;       
        
        v_row := v_row + 1;
        v_step := '12.' || to_char(v_row);
        
        DBMS_OUTPUT.PUT_LINE(rpad(v_case_type, 16) || rpad(v_case_subhdg, 11) || rpad(v_sections(1), 8) || rpad(v_sections(2), 8) || rpad(v_sections(3), 8) || rpad(v_sections(4), 8) || rpad(v_sections(5), 8) || rpad(v_sections(6), 8) || rpad(v_sections(7), 8) || rpad(v_sections(8), 8) );        
        
    End Loop;
        
    v_step := '13';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('Second Part: Rows retrieved: ' || v_row);

    
    -- Check that the report has logged that it has been run.
    v_step := '14';
    SELECT report_log_id, court_id,    report_name,   crest_report_code, date_last_run 
    INTO   v_rpt_log_id,  v_court_id2, v_report_name, v_report_code,     v_run_date 
    FROM   XHB_REPORT_LOG 
    WHERE  date_last_run > (sysdate - 0.01)   -- in the last 14 minutes
    AND    crest_report_code = 'RREC'; 
    
    v_step := '15';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('report_log_id  court_id  report_name                      crest_report_code  date_last_run'); 
    DBMS_OUTPUT.PUT_LINE('-------------  --------  -----------                      -----------------  -------------');
    DBMS_OUTPUT.PUT_LINE(v_rpt_log_id || '                 ' || v_court_id2 || '  ' || v_report_name || '   ' || v_report_code || '               ' || v_run_date);
    DBMS_OUTPUT.PUT_LINE(' ');
    
    v_step := '16';
    IF v_test_fail_msg = ' ' THEN
        -- expected success
        DBMS_OUTPUT.PUT_LINE('Test Succeeded');        
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed');
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE(v_test_fail_msg);
    END IF;   

--    DBMS_OUTPUT.PUT_LINE('output message file in utl_file_hold directory = ' || v_message_filename || chr(10));
 
    DBMS_OUTPUT.PUT_LINE('         --------- Test 1 get_RREC_report_test1  END --------------------------' || chr(10));
       
    EXCEPTION 
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SUBSTR(SQLERRM, 1, 150 ));
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

--select to_char(run_date, 'DD-MON-YYYY HH:MI'), REPORTS_LOG_ID, error_message from xhb_report_err_log where run_date > sysdate - 1 order by REPORTS_LOG_ID desc;


