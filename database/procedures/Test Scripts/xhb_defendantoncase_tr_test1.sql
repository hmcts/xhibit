/**
* CGI CREST to XHIBIT Program
*
* MODULE      : xhb_defendantoncase_bur_tr_test1
*
* DESCRIPTION : This script tests the xhb_defendantoncase_bur_tr trigger. 
* 				
*
*
*		This test checks that the audit table contains the nationality value from the 
*       xhb_defendant_on_case table.
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

    v_message_filename  VARCHAR2(100)   := 'xhb_defendantoncase_bur_tr_test1';
    
    v_system            VARCHAR2(100)   := NULL;
    v_directory         VARCHAR2(4000)  := NULL;
    v_database          VARCHAR2(100)   := NULL;
    v_slash             INTEGER;
    v_step              VARCHAR2(2) ;
    v_str_doc           VARCHAR2(30);
    v_test_fail_msg     VARCHAR2(500)   := ' ';
    v_doc_id            NUMBER(8);
    v_nationality       VARCHAR2(5);
    v_col_mag_ct        NUMBER(8);
	
    -- Parameters to pass to the procedure under test	
    v_court_id          NUMBER(8)      := 81;  
    
   
BEGIN

    DBMS_OUTPUT.PUT_LINE('--======================== Test 1 xhb_defendantoncase_bur_tr START ============================');
    
    v_step := '1';
    -- get the directory path and operating system
    SELECT   dbms_utility.port_string, ora_database_name --, ad.directory_name 
    INTO   v_system, v_database 
    FROM   dual
    ;
      
    DBMS_OUTPUT.PUT_LINE('System is '|| v_system || ', database is ' || v_database);

    v_step := '2';
    
    -- Update the table for which we are testing the trigger.   First test is to change Nationality.
    Update xhb_defendant_on_case Set nationality = 'GBR' where defendant_on_case_id = 954400;  
    
    v_step := '3';
    -- Second test is to changecollect_magistrate_court_id (for a different record)
    Update xhb_defendant_on_case Set collect_magistrate_court_id = 111197 where defendant_on_case_id = 954013;  
    
    v_step := '4';
    -- Now see if our changed columns have been recorded in the audit table
    select defendant_on_case_id, nationality, collect_magistrate_court_id 
    into   v_doc_id, v_nationality, v_col_mag_ct
    from   aud_defendant_on_case 
    where  defendant_on_case_id = 954400
    and    last_update_date > sysdate - 0.2;
--51 bleh!!    
    v_step := '5';
    -- Expecting v_doc_id, v_nationality, v_col_mag_ct = 954400, 'GBR', null
    If v_nationality !=  'GBR'  THEN
        v_test_fail_msg := 'v_nationality: expected ''GBR'', got ' || v_nationality  || Chr(10);
    END IF;
    
    v_step := '6';

    If nvl(v_col_mag_ct, 99) != 99 then 
        v_test_fail_msg := v_test_fail_msg || 'v_col_mag_ct: epected null, got ' || v_col_mag_ct || Chr(10);    
    End If;        

    v_step := '7';

    select defendant_on_case_id, nationality, collect_magistrate_court_id 
    into   v_doc_id, v_nationality, v_col_mag_ct
    from   aud_defendant_on_case 
    where  defendant_on_case_id = 954013
    and last_update_date > sysdate - 0.2;
    
    v_step := '8';
    -- Expecting v_doc_id, v_nationality, v_col_mag_ct = 954013, null, 111197
    If nvl(v_nationality, 'CAN') !=  'CAN'  THEN
        v_test_fail_msg := v_test_fail_msg || 'v_nationality: expected null, got ' || v_nationality  || Chr(10);
    END IF;
    
    v_step := '9';

    If nvl(v_col_mag_ct, 99) != 111197 then 
        v_test_fail_msg := v_test_fail_msg || 'v_col_mag_ct: expected 81, got ' || nvl(v_col_mag_ct, 'null') || Chr(10);    
    End If;        
    
    
    v_slash := 92;      -- backslash, as per Windows file system
    v_step := '10';
	  
    IF v_system like '%Linux%' THEN
	    v_slash := 47;  -- Forward slash, as per Linux/Unix file systems
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
 
    DBMS_OUTPUT.PUT_LINE('         --------- Test 1 xhb_defendantoncase_bur_tr  END --------------------------' || chr(10));
    
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


