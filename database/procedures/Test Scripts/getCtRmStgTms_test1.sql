/**
* CGI CREST to XHIBIT Program
*
* MODULE      : getCtRmStgTms_test1
*
* DESCRIPTION : This script tests the getCtRMSittingTimes report stored procedure. 
* 				getAppealHearingNotifnRpt accepts 2 user-selectable parameters (court_id and week-ending (date)).
*
* PROCEDURE getCtRmSittingTimes(p_court_id    IN  XHB_COURT.COURT_ID%TYPE 
*                            ,  p_week_ending IN  DATE
*                            ,  p_resultset   OUT SYS_REFCURSOR );                 *
*
*		This test looks for sittings at court_id = 81 (Snaresbrook) for the week ending 12/5/2018.
*
* Columns:      court_site_name, court_room_name, am_pm, monday, tuesday, wednesday, thursday, 
*               friday, saturday, total 
*
*************************************************************************************
*
*       
* VERSION HISTORY:
*
* Date          Author          Version     Nature of Change
* ----------    -------         --------    ----------------------------------------
* 21/05/2018    J Riley         1.0         First Version
* 02/10/2018    J Riley         1.1         Update for changed report using xhb_court_rume_usage table
**/
SET SERVEROUTPUT ON SIZE 1000000
SET LINESIZE 180
SET PAGESIZE 300

DECLARE
   
    v_found             VARCHAR2(1) := 'Y';
    v_row               NUMBER(8)   := 0;

    v_message_filename  VARCHAR2(100)   := 'getCtRmStgTms_test1';
    
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
    v_court_site_id     NUMBER(8)      := null;   -- signifies all sites for this court
    v_week_ending       DATE           := '12-May-2018';
    
    -- Variables to hold (one row of) returned database query
    -- Columns:      court_site_name, court_room_name, am_pm, monday, tuesday, wednesday, thursday, 
    --               friday, saturday, total_hours, total_days (that court sat) 
    v_crest_ct_rm_no         NUMBER(8);  -- Not needed in report, just used to sort on.
    v_court_site_name        VARCHAR2(255);
    v_court_name             VARCHAR2(255);  
    v_court_room_name        VARCHAR2(255);
    v_am_pm                  VARCHAR2(30);
    v_monday                 NUMBER(8, 2);
    v_tuesday                NUMBER(8, 2);
    v_wednesday              NUMBER(8, 2) ;
    v_thursday               NUMBER(8, 2) ;
    v_friday                 NUMBER(8, 2);
    v_saturday               NUMBER(8, 2) ;
    v_total_hours            NUMBER(8, 2) ; --line 28
    v_total_days             NUMBER(8, 0) ; --line 28
	v_sort                   NUMBER(8, 0) ; 	
    
    v_val1                   NUMBER(8, 2);
    v_val2                   NUMBER(8, 2);
    v_val3                   NUMBER(8, 2);

    
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 getCtRmSittingTimes START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);

    v_step := '2';
    
    -- Call the procedure we are testing
    XHB_REPORT_PKG_JR.getCtRmSittingTimes( p_court_id       =>  v_court_id 
                                         , p_court_site_id  =>  v_court_site_id
                                         , p_week_ending    =>  v_week_ending
	                                     , p_resultset      =>  v_resultset);

    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := ' 3';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
    END IF;
	
  -- check message is correct

    v_step := ' 4';
    DBMS_OUTPUT.PUT_LINE(' ');
    DBMS_OUTPUT.PUT_LINE('Court Site Name, Court Room Name, am/pm, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Total hours     Total Days');
    
    LOOP        
        FETCH v_resultset INTO v_court_name, v_crest_ct_rm_no, v_court_site_name, v_court_room_name, v_am_pm , v_monday, v_tuesday, v_wednesday, v_thursday, v_friday, v_saturday, v_total_hours, v_total_days, v_sort ;
        EXIT  WHEN v_resultset%NOTFOUND;
        
        v_row := v_row + 1;
        
        IF v_row = 1 then 
            v_val1 := v_thursday;
            v_val2 := v_total_hours;
        END IF;
        IF v_row = 2 THEN
            v_val3 := v_monday;
        END IF;
        
        DBMS_OUTPUT.PUT_LINE(rpad(v_court_site_name, 17, ' ') ||rpad(v_court_room_name, 17, ' ') ||  rpad(v_am_pm, 7, ' ') || rpad(v_monday, 8, ' ') || rpad(v_tuesday, 9, ' ') || rpad(v_wednesday, 11, ' ')|| rpad(v_thursday, 10, ' ') || rpad(v_friday, 8, ' ') || rpad(v_saturday, 10, ' ') || rpad(v_total_hours, 16,  ' ') || rpad(v_total_days, 10,  ' '));
    END LOOP;
    
    CLOSE v_resultset;
        
    -- Check we have retrieved the data we expect to
    IF v_val1 != 1.75 THEN 
        v_test_fail_msg := 'Thursday am: expected 1.75, got ' || v_val1 || Chr(10);
    END IF;
    IF v_val2 != 11.5 THEN 
        v_test_fail_msg := 'am total: expected 11.5, got ' || v_val2 || Chr(10);
    END IF;
    IF v_val3 != 2.25 THEN 
        v_test_fail_msg := 'Monday pm: expected 2.25, got ' || v_val3 || Chr(10);
    END IF;
    
    IF v_court_site_name != 'SNARESBROOK' THEN  
        v_test_fail_msg := v_test_fail_msg || 'v_court_site_name: expected SNARESBROOK, got ' || v_court_site_name || Chr(10);   -- Has to contain this tag
    END IF;
  
    IF v_row != 63 THEN 
        v_test_fail_msg := v_test_fail_msg || 'Rows retrieved: expected 63, got ' || v_row  || Chr(10); 
    END IF;
    
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
 
    DBMS_OUTPUT.PUT_LINE('         --------- Test 1 getCtRmSittingTimes  END --------------------------' || chr(10));
    
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
