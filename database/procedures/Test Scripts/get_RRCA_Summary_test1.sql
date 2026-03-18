/**
* CGI CREST to XHIBIT Program
*
* MODULE      : get_RRCA_Summary_test1
*
* DESCRIPTION : This script tests the get_RRCA_Rpt report stored procedure. 
* 				get_RRCA_Rpt accepts 2 user-selectable parameters (court_id and month-ending (date)).
*
* PROCEDURE get_RRCA_Summary(p_resultset  OUT SYS_REFCURSOR
*                          , p_date       IN  DATE
*                          , p_court_id   IN  XHB_REF_COURT.REF_COURT_ID%TYPE )  
*
*
*		This test looks for open cases by age by court_id = 81 (Snaresbrook).
*
* Columns:      court_name, court site, custody_status, age_band, case_count
*
*************************************************************************************
*
*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 26/09/2018    J Riley         1.0         First Version
**/
SET SERVEROUTPUT ON SIZE 1000000
SET LINESIZE 180
SET PAGESIZE 300

DECLARE
   
    v_found             VARCHAR2(1) := 'Y';
    v_row               NUMBER(8)   := 0;
    v_roww              NUMBER(8)   := 0;

    v_message_filename  VARCHAR2(100)   := 'get_RRCA_Summary_test1';
    
    v_system            VARCHAR2(100)   := NULL;
    v_directory         VARCHAR2(4000)  := NULL;
    v_database          VARCHAR2(100)   := NULL;
    v_slash             INTEGER;
    v_step              VARCHAR2(10) ;
    v_str_doc           VARCHAR2(30);
    v_test_fail_msg     VARCHAR2(500)   := ' ';
    v_resultset         SYS_REFCURSOR   := NULL;  
 	
    -- Parameters to pass to the procedure under test	
    v_court_id          NUMBER(8)      := 81;  
    v_rpt_date          DATE           := '31-Jul-2018';
    v_rpt_log_exp       VARCHAR2(500)  := 'Outstanding Trial cases by age ';
    v_report_check      VARCHAR2(500);
    
    -- Variables to hold (one row of) returned database query
    v_court_full_name        XHB_COURT.COURT_NAME%TYPE;
    v_court_site_name        XHB_COURT_SITE.COURT_SITE_NAME%TYPE;
    v_case_number            VARCHAR2(20);
    v_r1_case_number         VARCHAR2(20);
    v_case_count             NUMBER(8) := 0;
    v_r1_case_count          NUMBER(8) := 0;
    v_status                 VARCHAR2(255);
    v_r1_status              VARCHAR2(255);
    v_trial_date             DATE := '01-Jan-1900';
    v_r1_trial_date          DATE := '01-Jan-1900';
    v_waiting_time           NUMBER(8, 2) ;
    v_age_band               VARCHAR2(10);
    v_r1_age_band            VARCHAR2(10);
    v_error                  VARCHAR2(255);
        
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 get_RRCA_Summary START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);

    v_step := '2';
    
    -- PROCEDURE get_RRCA_Summary(p_resultset  OUT SYS_REFCURSOR 
    --                          , p_date       IN DATE 
    --                          , p_court_id   IN  XHB_COURT.COURT_ID%TYPE) AS 
    
    -- Call the procedure we are testing
    XHB_REPORT_PKG_JR.get_RRCA_Summary( p_resultset      =>  v_resultset
                                      , p_date           =>  v_rpt_date
                                      , p_court_id       =>  v_court_id );
-- l 65
    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := ' 3';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
	
  -- check message is correct

    v_step := ' 4';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('Court Name      Court Site   Status  Age Band   Case Count');

    LOOP        
--    Result Set 2 columns: ct.court_name,  status, age_band2, case_count     
--
       v_step := ' 5';
       FETCH v_resultset INTO v_court_full_name, v_court_site_name, v_status, v_age_band, v_case_count;
        EXIT  WHEN v_resultset%NOTFOUND;
           
        v_step := ' 6';
        v_roww := v_roww + 1;
        
        IF v_roww = 2 then 
            v_step := ' 7';
            v_r1_status     := v_status;
            v_r1_case_count := v_case_count;
            v_r1_age_band   := v_age_band;
        END IF;
        
        DBMS_OUTPUT.PUT_LINE(rpad(v_court_full_name, 16, ' ') || rpad(v_court_site_name, 16, ' ') || rpad(v_status, 10, ' ') || rpad(v_age_band, 12, ' ') || rpad(v_case_count, 16, ' '));
    END LOOP;
    
    v_step := ' 8';
    CLOSE v_resultset;
    
    v_step := '9';
    IF v_r1_status != 'B' THEN 
        v_test_fail_msg := v_test_fail_msg || 'Test 6: Row 2 v_status: expected ''B'', got ' || v_r1_status || Chr(10);
    END IF;
    
    v_step := '10';
    IF v_r1_age_band != 'B' THEN  
        v_test_fail_msg := v_test_fail_msg || 'Test 7: Row 2  v_age_band: expected ''B'', got ' || v_r1_age_band || Chr(10);   
    END IF;
    
    v_step := '11';
    IF v_r1_case_count != 101 THEN 
        v_test_fail_msg := v_test_fail_msg || 'Test 8: Row 2 v_case_count: expected 101, got ' || v_r1_case_count || Chr(10);
    END IF;
    
--  l 118       
    v_step := '12';
    -- Check the running of the report has been logged.
    Select report_name || ' ' || To_char(date_last_run, 'DD-MON-RRRR HH:MI:SS') 
    Into   v_report_check 
    from   XHB_REPORT_LOG
    Where  date_last_run = (Select max(rl2.date_last_run) from XHB_REPORT_LOG rl2 where rl2.crest_report_code = 'RRCA') 
    and    crest_report_code = 'RRCA';
    
    DBMS_OUTPUT.PUT_LINE('Report Log entry: ' || v_report_check);        

    v_step := '17';
    -- The report log entry should have the correct report title and be no more that a few seconds earlier than sysdate. We trim the seconds off for that reason.
    v_rpt_log_exp := v_rpt_log_exp || To_char(sysdate, 'DD-MON-RRRR HH:MI');
    
    -- Test 9
    IF substr(v_report_check, 1, 48) != v_rpt_log_exp THEN
        v_test_fail_msg := v_test_fail_msg || 'Test 9: Report log: expected ' || v_rpt_log_exp || ', got ' || substr(v_report_check, 1, 48) || chr(10);
    END IF;

    v_step := '18';
    IF v_test_fail_msg = ' ' THEN
        -- expected success
        DBMS_OUTPUT.PUT_LINE('Test Succeeded');        
    ELSE
        DBMS_OUTPUT.PUT_LINE('Test Failed');
        DBMS_OUTPUT.PUT_LINE(' ');
        DBMS_OUTPUT.PUT_LINE(v_test_fail_msg);
    END IF;   

    DBMS_OUTPUT.PUT_LINE('         ------------------- Test 1 get_RRCA_Summary  END --------------------------' || chr(10));
-- l 149    
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

