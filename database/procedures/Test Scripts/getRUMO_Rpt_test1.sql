/**
* CGI CREST to XHIBIT Program
*
* MODULE      : getRUMO_Rpt_test1
*
* DESCRIPTION : This script tests the getRUMO_Rpt report stored procedure. 
* 				getAppealHearingNotifnRpt accepts 2 user-selectable parameters (court_id and week-ending (date)).
*
* PROCEDURE getRUMO_Rpt(p_court_id    IN  XHB_COURT.COURT_ID%TYPE 
*                    ,  p_resultset   OUT SYS_REFCURSOR );                 *
*
*		This test looks for unacknowledged monetary orders issued by court_id = 81 (Snaresbrook).
*
* Columns:      monetary_order_tracking_id, court_full_name, court_address, court_phone, collect_court_name, collect_court_address, 
*               case_number, defendantnum, defendantname, ptiurn, order_date, fined (amount), compensation (amount), costs (amount), 
*               today_date
*
*************************************************************************************
*
*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 14/06/2018    J Riley         1.0         First Version
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
    v_mo_trk_id              XHB_MONETARY_ORDER_TRACKING.MONETARY_ORDER_TRACKING_ID%TYPE;  -- Not needed in report, just used to sort on.
    v_iss_court_full_name    XHB_REF_COURT.COURT_FULL_NAME%TYPE;
    v_iss_court_address      VARCHAR2(500);
    v_iss_court_phone        VARCHAR2(20);
    v_rec_court_full_name    XHB_REF_COURT.COURT_FULL_NAME%TYPE;
    v_court_address          VARCHAR2(500);
    v_case_number            VARCHAR2(10);
    v_defendant_no           XHB_DEFENDANT_ON_CASE.DEFENDANT_NUMBER%TYPE;
    v_defendant              VARCHAR2(255);
    v_ptiurn                 XHB_DEFENDANT_ON_CASE.PTIURN%TYPE;
    v_order_date             DATE;
    v_fine_amt               VARCHAR2(20);
    v_compensation_amt       VARCHAR2(20);
    v_costs_amt              VARCHAR2(20);
    v_today                  DATE;
    
    v_val1                   VARCHAR2(20);
    v_val2                   VARCHAR2(20);
    v_val3                   VARCHAR2(20);
    v_val4                   XHB_REF_COURT.COURT_FULL_NAME%TYPE;
    
    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 getRUMO_Rpt START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);

    v_step := '2';
    
    -- Call the procedure we are testing
    XHB_REPORT_PKG.getRUMO_Rpt( p_resultset      =>  v_resultset
                              , p_court_id       =>  v_court_id );
-- l 58
    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := ' 3';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
	
  -- check message is correct

    v_step := ' 4';
    DBMS_OUTPUT.PUT_LINE(' MO Track ID | Iss.Court Name,           |    Rec. Court,                           | Rec Ct Phone,      | Case No.  |      Defendant                 | Order Date | Fine Amt  | Compensation Amt | Costs AmtD |');
    DBMS_OUTPUT.PUT_LINE('|------------|---------------------------|------------------------------------------|--------------------|-----------|--------------------------------|------------|-----------|------------------|------------|');
/*  * Columns:      monetary_order_tracking_id, court_full_name, court_address, court_phone, collect_court_name, collect_court_address, 
  *                 case_number, defendantnum, defendantname, ptiurn, order_date, fined (amount), compensation (amount), costs (amount), 
  *                 today_date

 */    
    LOOP        
       v_step := ' 5';
       FETCH v_resultset INTO v_mo_trk_id, v_iss_court_full_name, v_iss_court_address, v_iss_court_phone, v_rec_court_full_name, v_court_address, v_case_number, v_defendant_no, v_defendant, v_ptiurn, v_order_date, v_fine_amt, v_compensation_amt, v_costs_amt, v_today ;
        EXIT  WHEN v_resultset%NOTFOUND;
           
        v_row := v_row + 1;

        v_step := ' 6';
        IF v_row = 5 then 
            v_val1 := v_fine_amt;
            v_val2 := v_compensation_amt;
            v_val3 := v_costs_amt;
            v_val4 := v_iss_court_full_name ;
        END IF;
        
        v_step := ' 7';
        DBMS_OUTPUT.PUT_LINE('|' || rpad(v_mo_trk_id, 11, ' ') || ' | ' || rpad(v_iss_court_full_name, 25, ' ') || ' | ' || rpad(v_rec_court_full_name, 40, ' ') || ' | ' || rpad(nvl(v_iss_court_phone, '-'), 18, ' ') || ' | ' || rpad(v_case_number, 9, ' ') || ' | ' || rpad(v_defendant, 30, ' ') || ' | ' || rpad(v_order_date, 10, ' ') || ' | ' || rpad(nvl(v_fine_amt, '-'), 9, ' ') || ' | ' || rpad(nvl(v_compensation_amt, '-'), 16, ' ') || ' | ' || rpad(v_costs_amt, 11, ' ') || '|');
    END LOOP;
    
    v_step := ' 8';
    CLOSE v_resultset;
        
    -- Check we have retrieved the data we expect to
    v_step := ' 9';
    IF v_val1 != 100 THEN 
        v_test_fail_msg := 'v_fine_amt: expected 100, got ' || v_val1 || Chr(10);
    END IF;
    v_step := ' 9';
    IF v_val2 != 250 THEN 
        v_test_fail_msg := 'v_compensation_amt: expected 250, got ' || v_val2 || Chr(10);
    END IF;
    v_step := '10';
    IF v_val3 != 35 THEN 
        v_test_fail_msg := 'v_costs_amt: expected 35, got ' || v_val3 || Chr(10);
    END IF;
    
    IF v_val4 != 'ABINGDON MAGISTRATES'' COURT' THEN  
        v_test_fail_msg := v_test_fail_msg || 'v_iss_court_full_name: expected ABINGDON MAGISTRATES'' COURT, got ' || v_val4 || Chr(10);   -- Has to contain this tag
    END IF;
  
    IF v_row != 3 THEN 
        v_test_fail_msg := v_test_fail_msg || 'Rows retrieved: expected 3, got ' || v_row  || Chr(10); 
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

