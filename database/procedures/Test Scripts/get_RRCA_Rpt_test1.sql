/**
* CGI CREST to XHIBIT Program
*
* MODULE      : getRUMO_Rpt_test1
*
* DESCRIPTION : This script tests the get_RRCA_Rpt report stored procedure. 
* 				get_RRCA_Rpt accepts 2 user-selectable parameters (court_id and month-ending (date)).
*
* PROCEDURE get_RRCA_Rpt(p_resultset  OUT SYS_REFCURSOR
*                      , p_resultset2 OUT SYS_REFCURSOR
*                      , p_date       IN  DATE
*                      , p_court_id   IN  XHB_REF_COURT.REF_COURT_ID%TYPE )  
*
*
*		This test looks for open cases by age by court_id = 81 (Snaresbrook).
*
* Columns:      ct.court_name, status, waiting_time, case_num, age_band
*
*************************************************************************************
*
*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 29/08/2018    J Riley         1.0         First Version
**/
SET SERVEROUTPUT ON SIZE 1000000
SET LINESIZE 180
SET PAGESIZE 300

DECLARE
   
    v_found             VARCHAR2(1) := 'Y';
    v_row               NUMBER(8)   := 0;
    v_roww              NUMBER(8)   := 0;

    v_message_filename  VARCHAR2(100)   := 'get_RRCA_Rpt_test1';
    
    v_system            VARCHAR2(100)   := NULL;
    v_directory         VARCHAR2(4000)  := NULL;
    v_database          VARCHAR2(100)   := NULL;
    v_slash             INTEGER;
    v_step              VARCHAR2(10) ;
    v_str_doc           VARCHAR2(30);
    v_test_fail_msg     VARCHAR2(500)   := ' ';
    v_resultset         SYS_REFCURSOR   := NULL;  
    v_resultset2        SYS_REFCURSOR   := NULL;  
	
    -- Parameters to pass to the procedure under test	
    v_court_id          NUMBER(8)      := 81;  
    v_rpt_date          DATE           := '31-Jul-2018';
    v_report_check      VARCHAR2(500);
    
    -- Variables to hold (one row of) returned database query
    v_court_full_name        XHB_COURT.COURT_NAME%TYPE;
    v_case_number            VARCHAR2(20);
    v_r1_case_number         VARCHAR2(20);
    v_case_count             NUMBER(8) := 0;
    v_r1_case_count          NUMBER(8) := 0;
    v_status                 VARCHAR2(255);
    v_r1_status              VARCHAR2(255);
    v_trial_date             DATE := '01-Jan-1900';
    v_r1_trial_date          DATE := '01-Jan-1900';
    v_waiting_time           NUMBER(8, 2) ;
--    v_waiting_time           DATE;
    v_age_band               VARCHAR2(10);
    v_r1_age_band            VARCHAR2(10);
    v_error                  VARCHAR2(255);
        
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 get_RRCA_Rpt START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);

    v_step := '2';
    
    -- PROCEDURE get_RRCA_Rpt(p_resultset  OUT SYS_REFCURSOR 
    --                      , p_resultset2 OUT SYS_REFCURSOR 
    --                      , p_date       IN DATE 
    --                      , p_court_id   IN  XHB_COURT.COURT_ID%TYPE) AS 
    
    -- Call the procedure we are testing
    XHB_REPORT_PKG.get_RRCA_Rpt( p_resultset      =>  v_resultset
                               , p_resultset2     =>  v_resultset2
                               , p_date           =>  v_rpt_date
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
    DBMS_OUTPUT.PUT_LINE('Court Name      Trial Date      Status  Waiting Time    Case Num     Age Band');
/*  * Columns:      ct.court_name, trial date, status, waiting_time, case_num, age_band
 */    
    LOOP        
        v_step := ' 5';
        FETCH v_resultset INTO v_court_full_name, v_trial_date, v_status, v_waiting_time , v_case_number, v_age_band;
        EXIT  WHEN v_resultset%NOTFOUND;
        
        v_step := ' 6';
           
        v_row := v_row + 1;
        
        IF v_row < 11 THEN
            DBMS_OUTPUT.PUT_LINE(rpad(v_court_full_name, 16, ' ') || rpad(v_trial_date, 17, ' ') || rpad(v_status, 10, ' ') ||  rpad(v_waiting_time, 10, ' ') || rpad(v_case_number, 16, ' ') || rpad(v_age_band, 12, ' '));
        END IF;
        
        IF v_row = 1 then 
            v_step := ' 7';
            v_r1_trial_date  := v_trial_date;
            v_r1_status      := v_status;
            v_r1_case_number := LTrim(v_case_number);
            v_r1_age_band    := v_age_band;
        END IF;

--        v_step := ' 8';                                                                                            
--        DBMS_OUTPUT.PUT_LINE(rpad(v_court_full_name, 16, ' ') ||  rpad(v_trial_date, 19, ' ') || rpad(v_status, 10, ' ') || rpad( v_waiting_time, 13, ' ') || rpad(v_case_number, 16, ' ')|| rpad(v_age_band, 10, ' '));
    END LOOP;
    
    CLOSE v_resultset;
    
    v_step := ' 9';
        
    -- Check we have retrieved the data we expect to
    IF v_r1_trial_date != '09-JUL-18' THEN 
        v_test_fail_msg := 'Test 1: Row 1 v_trial_date: expected ''09-JUL-18'', got ' || v_r1_trial_date || Chr(10);
    END IF;
    
    v_step := '10';
    IF v_r1_status != 'N/A' THEN 
        v_test_fail_msg := v_test_fail_msg || 'Test 2: Row 1 v_status: expected ''N/A'', got ' || v_r1_status || Chr(10);
    END IF;
    
    v_step := '11';
    IF v_r1_case_number = 'T20087514' THEN 
        NULL;
    ELSE
        v_test_fail_msg := v_test_fail_msg || 'Test 3: Row 1 v_case_number: expected ''T20087514'', got ' || '''' || v_r1_case_number || '''' || Chr(10);
    END IF;
    
    v_step := '12';
    IF v_r1_age_band != 'A' THEN  
        v_test_fail_msg := v_test_fail_msg || 'Test 4: Row 1  v_age_band: expected ''A'', got ' || v_r1_age_band || Chr(10);   
    END IF;
  
    v_step := '13';
    IF v_row != 574 THEN 
        v_test_fail_msg := v_test_fail_msg || 'Test 5: Rows retrieved: expected 574, got ' || v_row  || Chr(10); 
    END IF;
    
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('Court Name      Status  Age Band   Case Count');

    LOOP        
--    Result Set 2 columns: ct.court_name,  status, age_band2, case_count     
--
       v_step := '14';
       FETCH v_resultset2 INTO v_court_full_name, v_status, v_age_band, v_case_count;
        EXIT  WHEN v_resultset2%NOTFOUND;
           
        v_step := '15';
        v_roww := v_roww + 1;
        
        IF v_roww = 2 then 
            v_r1_status     := v_status;
            v_r1_case_count := v_case_count;
            v_r1_age_band   := v_age_band;
        END IF;
        
        DBMS_OUTPUT.PUT_LINE(rpad(v_court_full_name, 16, ' ') || rpad(v_status, 10, ' ') || rpad(v_age_band, 12, ' ') || rpad(v_case_count, 16, ' '));
    END LOOP;
    
    CLOSE v_resultset2;
    
    v_step := '16';
    IF v_r1_status != 'B' THEN 
        v_test_fail_msg := v_test_fail_msg || 'Test 6: Row 2 v_status: expected ''B'', got ' || v_r1_status || Chr(10);
    END IF;
    
    v_step := '17';
    IF v_r1_age_band != 'B' THEN  
        v_test_fail_msg := v_test_fail_msg || 'Test 7: Row 2  v_age_band: expected ''B'', got ' || v_r1_age_band || Chr(10);   
    END IF;
    
    v_step := '18';
    IF v_r1_case_count != 101 THEN 
        v_test_fail_msg := v_test_fail_msg || 'Test 8: Row 2 v_case_count: expected 101, got ' || v_r1_case_count || Chr(10);
    END IF;
    
-- l 141       
    v_step := '19';
    -- Check the running of the report has been logged.
    Select report_name || ' ' || To_char(date_last_run, 'DD-MON-RRRR  HH:MI:SS') 
    Into   v_report_check 
    from   XHB_REPORT_LOG
    Where  date_last_run = (Select max(rl2.date_last_run) from XHB_REPORT_LOG rl2 where rl2.crest_report_code = 'RRCA') 
    and    crest_report_code = 'RRCA';
    
    DBMS_OUTPUT.PUT_LINE('Report Log entry: ' || v_report_check);        

    v_step := '17';

    IF v_test_fail_msg = ' ' THEN
        -- expected success
        DBMS_OUTPUT.PUT_LINE('Test Succeeded');        
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed');
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE(v_test_fail_msg);
    END IF;   

    DBMS_OUTPUT.PUT_LINE('         ------------------- Test 1 get_RRCA_Rpt  END --------------------------' || chr(10));
    
    ROLLBACK;
    
    EXCEPTION 
        WHEN OTHERS THEN
--            ROLLBACK;
            DBMS_OUTPUT.PUT_LINE(SUBSTR(SQLERRM, 1, 150 ));
            DBMS_OUTPUT.PUT_LINE('ERROR HAS OCCURRED !!!!!!!!!!!');         
            DBMS_OUTPUT.PUT_LINE('AT STEP ' || v_step);         
        
            -- This query gets the last entry in the report error log, which may or may not be from the latest run. Use with caution.
            select to_char(rel.run_date, 'DD-MON-RRRR HH:MI:SS'), rel.error_message  
            into v_str_doc, v_error 
            from XHB_REPORT_ERR_LOG rel where run_date = (select max(rel2.run_date) from XHB_REPORT_ERR_LOG rel2);
            
            DBMS_OUTPUT.PUT_LINE(v_str_doc || ': ' ||v_error);         
END;
/
SPOOL OFF
