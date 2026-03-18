/**
* CGI CREST to XHIBIT Program
*
* MODULE      : get_RREC_DETAIL_test1
*
* DESCRIPTION : This script tests the get_RREC_DETAIL stored procedure. 
* 				get_RREC_DETAIL accepts 2 user-selectable parameters (court_id and week-ending (date)).
*               It returns 1 sysref_cursor as an output parameter. 
* Procedure get_RREC_detail (p_resultset  OUT sys_refcursor
*                         ,  p_court_id    IN  XHB_COURT.COURT_ID%TYPE 
*                         ,  p_end_date    IN  DATE  )
*                          
*
*		This test retrieves details of outstanding cases for the specified court.
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
    
    -- Parameters to pass to the procedure under test	
    v_court_id           NUMBER(8)          := 81;  
    v_court_id2          NUMBER(8)          := 0;
    v_end_date           DATE               := '14-Jun-2018';
    
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 get_RREC_detail_test1 START ============================');
    
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
    XHB_REPORT_PKG.get_RREC_detail (v_resultset1 
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
    DBMS_OUTPUT.PUT_LINE('Court Site    Section No  Case Type                     Case Number  Case Subhdg                               Case_id');        
    DBMS_OUTPUT.PUT_LINE('----------    ----------  ---------                     -----------  -----------                               --------');        
    -- Find out how many rows we got. (Not set until all rows have been Fetched.)
    v_step := '7';
    Loop
        FETCH v_resultset1 into v_section_num, v_court_site, v_case_type, v_case_number, v_case_subhdg, v_case_id;    
        EXIT WHEN v_resultset1%NOTFOUND;       
        
        v_row := v_row + 1;
        
        v_step := '8';
        IF (v_row < 11) OR (v_section_num != v_last_section) THEN   -- We only display a few, because there could be thousands.
            DBMS_OUTPUT.PUT_LINE(rpad(v_court_site, 17) || rpad(v_section_num, 12) || rpad(v_case_type, 30) || rpad(v_case_number, 13) || rpad(v_case_subhdg, 42) || rpad(v_case_id, 11));     
--91            
            IF v_row = 1 THEN 
                IF (v_section_num != 1) OR (v_case_number != 20080092) OR (v_case_type != 'CRIMINAL APPEAL') THEN
                    v_err_msg := 'Row 1: expected section = 1, case no. = 20080092, case type = CRIMINAL APPEAL, got section = ' || v_section_num || ', case no. = ' || v_case_number || ', case type = ' || v_case_type || chr(10);
                END IF;
            END IF;
        END IF;
        
        v_last_section := v_section_num;
    End Loop;
        
    v_step := '9';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('Rows retrieved: ' || v_row);
    
    IF v_row != 1340 THEN
        v_err_msg := v_err_msg || 'Rows retrieved:  Expected 1340, got ' || TO_CHAR(v_row) || chr(10);
    END IF;
    
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
    
    IF (v_court_id2 != 81) OR  (v_report_code != 'RREC') OR (Trunc(v_run_date) != Trunc(sysdate)) THEN
        v_err_msg := v_err_msg || 'Report log: Expected court_id = 81, crest_report_code = RREC, date_last_run = ' || Trunc(sysdate) || ', got court_id = ' || v_court_id2 || ', crest_report_code = ' || v_report_code || ', date_last_run = ' || v_run_date || chr(10); 
    END IF;
    
    
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
 
    DBMS_OUTPUT.PUT_LINE('         --------- Test 1 get_RREC_detail_test1  END --------------------------' || chr(10));
       
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


